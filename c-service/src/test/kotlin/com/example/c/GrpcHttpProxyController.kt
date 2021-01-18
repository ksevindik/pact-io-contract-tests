package com.example.c

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import kotlin.Throws
import javax.servlet.http.HttpServletRequest
import com.google.protobuf.InvalidProtocolBufferException
import com.google.protobuf.Message
import com.google.protobuf.util.JsonFormat
import io.grpc.*
import io.grpc.stub.ClientCalls
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.grpc.server.GrpcServerLifecycle
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.util.ReflectionUtils
import org.springframework.web.bind.annotation.RequestBody
import java.io.*
import java.nio.charset.Charset
import java.util.HashMap
import java.util.concurrent.Executor

@RestController
class GrpcHttpProxyController {
    // @Value("\${grpc.inProcessServerName}")
    var inProcessServerName: String? = null

    @Value("\${grpc.server.port}")
    var port = 6565
    private val methodsByName: MutableMap<String, MethodDescriptor<Any, Any>> = HashMap()
    @PostMapping(value = ["/grpc/**"], produces = ["application/json"])
    @ResponseBody
    @Throws(IOException::class)
    fun grpcProxy(@RequestBody json: String, request: HttpServletRequest): String {
        val auth: String? = null // StringUtils.firstNonBlank(request.getHeader(AUTHORIZATION), request.getHeader(AUTHORIZATION.toLowerCase()))
        val grpcMethodName = request.requestURI.replace("/grpc/", "")
        return grcpCall(grpcMethodName, json, auth)
    }

    @Throws(IOException::class)
    protected fun grcpCall(methodName: String, jsonRequest: String, auth: String?): String {
        log.info("Performing grpc call to method {}", methodName)
        log.debug("Performing grpc call to method {} with payload {} and auth {}", methodName, jsonRequest, auth)
        val method = methodsByName[methodName]!!
        val requestMarshaller = method.requestMarshaller as MethodDescriptor.PrototypeMarshaller<Any>
        val responseMarshaller = method.responseMarshaller as MethodDescriptor.PrototypeMarshaller<Any>
        val jsonRequestMarshaller = this.jsonMarshaller(requestMarshaller.messagePrototype as Message)
        val jsonResponseMarshaller = this.jsonMarshaller(responseMarshaller.messagePrototype as Message)
        val requestGrpcObject = jsonRequestMarshaller.parse(ByteArrayInputStream(jsonRequest.toByteArray()))
        val responseGrpcObject = blockingUnaryCall(method, requestGrpcObject, auth)
        val responseStream = jsonResponseMarshaller.stream(responseGrpcObject as Message)
        val jsonResponse = IOUtils.toString(responseStream, Charset.defaultCharset())
        log.debug("Returning grpc call to method {} with response {}", jsonResponse)
        return jsonResponse
    }

    protected fun blockingUnaryCall(
        method: MethodDescriptor<Any, Any>?,
        req: Any,
        auth: String?
    ): Any {
        // val channel: Channel = if (StringUtils.isNotBlank(inProcessServerName)) InProcessChannelBuilder.forName(inProcessServerName).directExecutor().build() else NettyChannelBuilder.forAddress("localhost", port).directExecutor().build()
        val channel: Channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()
        // val credentials: CallCredentials = JwtCallCredentials(auth!!)
        val callOptions = CallOptions.DEFAULT // .withCallCredentials(credentials)
        return ClientCalls.blockingUnaryCall(channel, method, callOptions, req)
    }

    @EventListener
    fun handleEvent(event: ContextRefreshedEvent) {
        log.info("Reading grpc configured method descriptors")
        if (!methodsByName.isEmpty()) return
        val lc = event.applicationContext.getBean(GrpcServerLifecycle::class.java)
        val f = ReflectionUtils.findField(GrpcServerLifecycle::class.java, "server")
        f!!.trySetAccessible()
        val server = f.get(lc) as Server
        server.services.forEach({
            it.methods.forEach({
                methodsByName[it.methodDescriptor.fullMethodName] = it.methodDescriptor as MethodDescriptor<Any, Any>
            })
        })

        // log.debug("MethodDescriptors: {}", methodsByName.keys)
    }

    protected fun <T : Message> jsonMarshaller(defaultInstance: T): MethodDescriptor.Marshaller<T> {
        val parser = JsonFormat.parser()
        val printer = JsonFormat.printer()
        return this.jsonMarshaller(defaultInstance, parser, printer)
    }

    protected fun <T : Message> jsonMarshaller(
        defaultInstance: T,
        parser: JsonFormat.Parser,
        printer: JsonFormat.Printer
    ): MethodDescriptor.Marshaller<T> {
        val charset = Charset.forName("UTF-8")
        return object : MethodDescriptor.Marshaller<T> {
            override fun stream(value: T): InputStream {
                return try {
                    ByteArrayInputStream(printer.print(value).toByteArray(charset))
                } catch (e: InvalidProtocolBufferException) {
                    throw Status.INTERNAL
                            .withCause(e)
                            .withDescription("Unable to print json proto")
                            .asRuntimeException()
                }
            }

            override fun parse(stream: InputStream): T {
                val builder = defaultInstance!!.newBuilderForType()
                val reader: Reader = InputStreamReader(stream, charset)
                val proto: T
                try {
                    parser.merge(reader, builder)
                    proto = builder.build() as T
                    reader.close()
                } catch (e: InvalidProtocolBufferException) {
                    throw Status.INTERNAL.withDescription("Invalid protobuf byte sequence")
                            .withCause(e)
                            .asRuntimeException()
                } catch (e: IOException) {
                    // Same for now, might be unavailable
                    throw Status.INTERNAL.withDescription("Invalid protobuf byte sequence")
                            .withCause(e)
                            .asRuntimeException()
                }
                return proto
            }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(GrpcHttpProxyController::class.java)
        private const val AUTHORIZATION = "Authorization"
    }
}

internal class JwtCallCredentials(private val authorizationHeader: String) : CallCredentials {
    override fun applyRequestMetadata(method: MethodDescriptor<*, *>?, attrs: Attributes, appExecutor: Executor, applier: CallCredentials.MetadataApplier) {
        val metadata = Metadata()
        metadata.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), authorizationHeader)
        applier.apply(metadata)
    }

    override fun thisUsesUnstableApi() {}
}
