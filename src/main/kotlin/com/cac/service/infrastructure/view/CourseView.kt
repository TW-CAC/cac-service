package com.cac.service.infrastructure.view

import com.cac.service.domain.course.Course
import com.cac.service.domain.course.Question
import com.cac.service.domain.user.UserInfo
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*


@Document
data class CourseView(
        @Id val id: UUID,
        val title: String,
        val description: String,
        val creator: UUID,
        val questions: List<Question>,
        val subscribers: List<UUID> = listOf()
)

fun CourseView.toCourse() = Course(id, title, description, UserInfo(creator), questions, subscribers.map { UserInfo(it) })
