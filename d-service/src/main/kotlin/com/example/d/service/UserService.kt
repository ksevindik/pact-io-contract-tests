package com.example.d.service

import com.example.d.async.EventPublisher
import com.example.d.model.Country
import com.example.d.model.User
import com.example.event.UserCreatedEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService @Autowired constructor(private val eventPublisher: EventPublisher) {
    fun create(user: User) {
        user.id = Date().time
        print("User created :$user")
        eventPublisher.publish(
                UserCreatedEvent.newBuilder()
                        .setFirstName(user.firstName)
                        .setLastName(user.lastName)
                        .setEmail(user.email).build())
    }


    fun getUsers():List<User> {
        val c1 = Country("c1")
        c1.id=1
        val c2 = Country("c2")
        c2.id = 2

        val u1 = User("a","b","a@b")
        u1.id = 1
        u1.country = c1
        val u2 = User("c","d","c@d")
        u2.id = 2
        u2.country = c1
        val u3 = User("e","f","e@f")
        u3.id = 3
        u3.country = c2

        return listOf(u1,u2,u3)
    }
}
