package com.example.c

import com.example.hello.GreeterGrpc
import com.example.hello.HelloReply
import com.example.hello.HelloRequest
import io.grpc.stub.StreamObserver
import org.springframework.boot.autoconfigure.grpc.server.GrpcService

@GrpcService
class HelloService : GreeterGrpc.GreeterImplBase() {

    override fun sayHello(request: HelloRequest?, responseObserver: StreamObserver<HelloReply>?) {
        val reply = HelloReply.newBuilder().setMessage("Hello from gRPC :" + request?.name).build()
        responseObserver?.onNext(reply)
        responseObserver?.onCompleted()
    }
}
