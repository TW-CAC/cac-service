package com.cac.service.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class CourseQueryController {

    @GetMapping("/courses")
    fun getCourses(): List<Course>  {
        return listOf(Course("hello world"));
    }
}

data class Course(val courseName: String)