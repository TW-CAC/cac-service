package com.cac.service.infrastructure.view

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Id

@Document("exercise")
data class ExerciseView (
    @Id
    val id: UUID,
    val classId: UUID,
    val question: QuestionView,
    val answers: MutableList<AnswerView> = mutableListOf(),
    val comments: MutableList<CommentView> = mutableListOf(),
    var isPublished: Boolean = false,
    val creatorId: UUID? = null
)

data class QuestionView(
    val title: String,
    val description: String?
)

data class AnswerView (
    val id: UUID,
    val userId: UUID,
    val dateTime: LocalDateTime,
    var answer: String,
    val comments: MutableList<CommentView> = mutableListOf()
)

data class CommentView (
    val id: UUID,
    val userId: UUID,
    val dateTime: LocalDateTime,
    var comment: String
)