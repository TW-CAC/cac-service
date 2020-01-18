package com.cac.service.api

import com.cac.service.domain.course.Course
import com.cac.service.infrastructure.CourseRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class CourseCommandController(private val courseRepository: CourseRepository) {

    @PostMapping("/course")
    fun createCourse(@RequestBody course: Course) {
        courseRepository.create(course)
    }
}
