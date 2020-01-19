package com.cac.service.api

import com.cac.service.domain.course.Course
import com.cac.service.infrastructure.CourseRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*


@RestController
@RequestMapping("/api")
class SubscriptionCommandController(private val courseRepository: CourseRepository) {

    @PostMapping("/subscription")
    fun subscribeCourse(@RequestParam("courseId", required = true) courseId:String, @RequestHeader("userId", required = true) userId:String): Course {
        val course = courseRepository.courseOfId(courseId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find course $courseId")
        course.subscribe(UUID.fromString(userId))
        return courseRepository.update(course)
    }

}