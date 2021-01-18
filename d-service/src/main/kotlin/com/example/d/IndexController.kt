package com.example.d

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class IndexController {
    @RequestMapping("/")
    fun index(): String {
        return "redirect:/swagger-ui.html"
    }
}
