package com.example.a.async_request

import com.example.msg.UserCreationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(private val asyncRequestSender: AsyncRequestSender) {
    fun create(user: User) {
        print("User will be created :$user")
        asyncRequestSender.send(
                UserCreationRequest.newBuilder()
                        .setFirstName(user.firstName)
                        .setLastName(user.lastName)
                        .setEmail(user.email).build())
    }
}
