package com.example.b.graphql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/users")
class UserRestController @Autowired constructor(private val userService: UserService) {
    @GetMapping
    fun getUsers():List<User> {
        return userService.getUsers()
    }
}