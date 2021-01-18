package com.example.c

import io.grpc.Attributes
import io.grpc.CallCredentials
import io.grpc.CallCredentials.MetadataApplier
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import java.util.concurrent.Executor

internal class JwtCallCredentials(private val authorizationHeader: String) : CallCredentials {
    override fun applyRequestMetadata(method: MethodDescriptor<*, *>?, attrs: Attributes, appExecutor: Executor, applier: MetadataApplier) {
        val metadata = Metadata()
        metadata.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), authorizationHeader)
        applier.apply(metadata)
    }

    override fun thisUsesUnstableApi() {}
}
