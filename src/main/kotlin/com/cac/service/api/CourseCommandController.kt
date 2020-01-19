package com.cac.service.api

import com.cac.service.domain.course.Course
import com.cac.service.infrastructure.CourseRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class CourseCommandController(private val courseRepository: CourseRepository) {

    @PostMapping("/course")
    fun createCourse(@RequestBody course: Course) {
        courseRepository.create(course)
    }

    @DeleteMapping("/course")
    fun deleteCourse(@RequestParam("courseId", required = true) courseId: String) {
        courseRepository.delete(courseId)
    }
}
