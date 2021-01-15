package com.example.c

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CServiceApplication

fun main(args: Array<String>) {
	runApplication<CServiceApplication>(*args)
}
