package com.cac.service.api

import com.cac.service.app.user.UserService
import com.cac.service.domain.course.Course
import com.cac.service.domain.user.UserInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api")
class UserInfoQueryController(@Autowired val userService: UserService) {

    @GetMapping("/userinfo")
    fun getUsers(): List<UserInfo> = userService.findAllUsers().map { UserInfo(it.id, it.userName) }
}

