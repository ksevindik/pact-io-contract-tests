package com.example.c.grpc

import com.example.hello.GreeterGrpc
import com.example.hello.HelloReply
import com.example.hello.HelloRequest
import io.grpc.stub.StreamObserver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.grpc.server.GrpcService

@GrpcService
class HelloServiceGrpcAdapter @Autowired constructor(private val helloService: HelloService) : GreeterGrpc.GreeterImplBase() {

    override fun sayHello(request: HelloRequest?, responseObserver: StreamObserver<HelloReply>?) {
        val reply = helloService.sayHello(request!!)
        responseObserver?.onNext(reply)
        responseObserver?.onCompleted()
    }
}
