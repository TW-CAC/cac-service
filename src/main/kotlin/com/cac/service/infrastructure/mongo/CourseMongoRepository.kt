package com.cac.service.infrastructure.mongo

import com.cac.service.domain.course.Course
import com.cac.service.infrastructure.CourseRepository
import com.cac.service.infrastructure.view.CourseView
import com.cac.service.infrastructure.view.toCourse
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import java.util.*


interface CourseMongoRepositoryImpl: MongoRepository<CourseView, UUID>

@Component
class CourseMongoRepository(private val courseMongoRepositoryImpl: CourseMongoRepositoryImpl): CourseRepository {
    override fun courseOfId(courseId: String) = courseMongoRepositoryImpl.findById(UUID.fromString(courseId)).orElse(null)?.toCourse()
    override fun allCourses(): List<Course> = courseMongoRepositoryImpl.findAll().map { it.toCourse() }
    override fun create(course: Course): Course {
        val courseView = CourseView(course.id, course.title, course.description, course.creator.id, course.questions, course.subscribers.map { it.id })
        return courseMongoRepositoryImpl.save(courseView).toCourse()
    }

    override fun update(course: Course): Course {
        val courseView = CourseView(course.id, course.title, course.description, course.creator.id, course.questions, course.subscribers.map { it.id })
        return courseMongoRepositoryImpl.save(courseView).toCourse()
    }

    override fun delete(courseId: String) {
        courseMongoRepositoryImpl.deleteById(UUID.fromString(courseId))
    }

}



