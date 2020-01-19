package com.cac.service.infrastructure

import com.cac.service.domain.course.Course

interface CourseRepository {
    fun courseOfId(courseId: String):Course?
    fun allCourses(): List<Course>
    fun create(course: Course):Course
    fun update(course: Course):Course
    fun delete(courseId: String)
}
