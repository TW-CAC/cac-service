package com.cac.service.api

import com.cac.service.domain.course.Course
import com.cac.service.infrastructure.CourseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class CourseQueryController(@Autowired val courseRepository: CourseRepository) {

    @GetMapping("/course")
    fun getCourses(): List<Course>  {
        return courseRepository.allCourses()
    }
}

