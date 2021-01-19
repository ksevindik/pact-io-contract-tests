package com.example.d.model

data class User(val firstName: String, val lastName: String, val email: String) {
    var id:Long? = null
    var country:Country? = null
}
