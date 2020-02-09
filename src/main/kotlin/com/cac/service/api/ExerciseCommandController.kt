package com.cac.service.api

import com.cac.service.app.exercise.ExerciseService
import com.cac.service.domain.course.Question
import com.cac.service.domain.exercise.Answer
import com.cac.service.domain.exercise.Comment
import com.cac.service.domain.exercise.Exercise
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.UUID

@RestController
@RequestMapping("/api")
class ExerciseCommandController(
    private val exerciseService: ExerciseService
) {
    @PostMapping("/exercises/{exerciseId}/answer}")
    fun submitExerciseAnswer(@PathVariable exerciseId: UUID,
                             @RequestBody createExerciseAnswer: CreateExerciseAnswerRequest) {
        exerciseService.saveExerciseAnswer(exerciseId, createExerciseAnswer.toAnswer())
    }

    @PostMapping("/exercises/{exerciseId}/comment")
    fun submitExerciseComment(@PathVariable exerciseId: UUID,
                              @RequestBody createCommentRequest: CreateCommentRequest) {
        exerciseService.saveExerciseComment(exerciseId, createCommentRequest.toComment())
    }

    @PostMapping("/exercises/{exerciseId}/answers/{answerId}/comment")
    fun submitAnswerComment(@PathVariable exerciseId: UUID,
                            @PathVariable answerId: UUID,
                            @RequestBody createCommentRequest: CreateCommentRequest) {
        exerciseService.saveAnswerComment(exerciseId, answerId, createCommentRequest.toComment())
    }

    @PutMapping("/exercises/{exerciseId}")
    fun publishExercise(@PathVariable exerciseId: UUID) {
        exerciseService.publishExercise(exerciseId)
    }

    @PostMapping("/exercise")
    fun createExercise(@RequestBody createExerciseRequest: CreateExerciseRequest) {
        exerciseService.saveExercise(createExerciseRequest.toExercise())
    }
}

data class CreateExerciseRequest(
    val classId: UUID,
    val isPublished: Boolean,
    val title: String,
    val description: String? = null
)

data class CreateExerciseAnswerRequest(
    val userId: UUID,
    val answer: String
)

data class CreateCommentRequest (
    val userId: UUID,
    val comment: String
)

private fun CreateExerciseRequest.toExercise() = Exercise(
    id = UUID.randomUUID(),
    classId = this.classId,
    question = Question(this.title, this.description),
    isPublished = this.isPublished
)

private fun CreateExerciseAnswerRequest.toAnswer() = Answer(
    id = UUID.randomUUID(),
    userId = this.userId,
    dateTime = LocalDateTime.now(),
    answer = this.answer
)

private fun CreateCommentRequest.toComment() = Comment(
    id = UUID.randomUUID(),
    userId = this.userId,
    dateTime = LocalDateTime.now(),
    comment = this.comment
)