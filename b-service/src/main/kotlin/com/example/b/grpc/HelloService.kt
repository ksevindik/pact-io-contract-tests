package com.example.b.grpc

import com.example.hello.GreeterGrpc
import com.example.hello.HelloReply
import com.example.hello.HelloRequest
import io.grpc.Channel
import io.grpc.ManagedChannelBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
class HelloService @Autowired constructor(private val channelFactory: ChannelFactory) {

    fun sayHello(helloRequest: HelloRequest): HelloReply {
        var mc = channelFactory.createChannel()

        var stub = GreeterGrpc.newBlockingStub(mc)

        var response = stub.sayHello(helloRequest)

        return response
    }
}

interface ChannelFactory {
    fun createChannel(): Channel
}

@Component
class ManagedChannelFactory : ChannelFactory {

    var grpcServerHost: String = "localhost"
    @Value("\${grpc.server.port}")
    var grpcServerPort: Int = 8082

    override fun createChannel(): Channel {
        var mc = ManagedChannelBuilder.forAddress(grpcServerHost, grpcServerPort).usePlaintext().build()
        return mc
    }
}
    