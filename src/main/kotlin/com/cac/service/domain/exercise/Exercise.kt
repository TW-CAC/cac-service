package com.cac.service.domain.exercise

import com.cac.service.domain.course.Question
import java.time.LocalDateTime
import java.util.UUID

data class Exercise (
    val id: UUID,
    val classId: UUID,
    val question: Question,
    val answers: MutableList<Answer> = mutableListOf(),
    val comments: MutableList<Comment> = mutableListOf(),
    val isPublished: Boolean = false,
    val creatorId: UUID? = null
)

data class Answer (
    val id: UUID,
    val userId: UUID,
    val dateTime: LocalDateTime,
    var answer: String,
    val comments: MutableList<Comment> = mutableListOf()
)

data class Comment (
    val id: UUID,
    val userId: UUID,
    val dateTime: LocalDateTime,
    var comment: String
)