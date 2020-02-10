package com.cac.service.app.exercise

import com.cac.service.domain.`class`.ClassService
import com.cac.service.domain.course.Question
import com.cac.service.domain.exercise.Answer
import com.cac.service.domain.exercise.Comment
import com.cac.service.domain.exercise.Exercise
import com.cac.service.exception.AnswerNotFoundException
import com.cac.service.exception.ClassNotFoundException
import com.cac.service.exception.ExerciseNotFoundException
import com.cac.service.infrastructure.ExerciseRepository
import com.cac.service.infrastructure.view.AnswerView
import com.cac.service.infrastructure.view.CommentView
import com.cac.service.infrastructure.view.ExerciseView
import com.cac.service.infrastructure.view.QuestionView
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ExerciseService (
    private val exerciseRepository: ExerciseRepository,
    private val classService: ClassService
) {
    fun saveExercise(exercise: Exercise): UUID {
        if (!classService.classExists(exercise.classId)) {
            throw ClassNotFoundException("Class ${exercise.classId} does not exist.")
        }

        exerciseRepository.save(exercise.toExerciseView())

        return exercise.id
    }

    fun publishExercise(exerciseId: UUID) {
        exerciseRepository.findByIdOrNull(exerciseId)?.let {
            it.isPublished = true

            exerciseRepository.save(it)
        } ?: throw ExerciseNotFoundException("Exercise $exerciseId does not exist.")
    }

    fun saveExerciseAnswer(exerciseId: UUID, answer: Answer): UUID {
        exerciseRepository.findByIdOrNull(exerciseId)?.let {
            it.answers.add(answer.toAnswerView())

            exerciseRepository.save(it)
        } ?: throw ExerciseNotFoundException("Exercise $exerciseId does not exist.")

        return answer.id
    }

    fun saveExerciseComment(exerciseId: UUID, comment: Comment) {
        exerciseRepository.findByIdOrNull(exerciseId)?.let {
            it.comments.add(comment.toCommentView())

            exerciseRepository.save(it)
        } ?: throw ExerciseNotFoundException("Exercise $exerciseId does not exist.")
    }

    fun saveAnswerComment(exerciseId: UUID, answerId: UUID, comment: Comment) {
        exerciseRepository.findByIdOrNull(exerciseId)?.let { exercise ->
            exercise.answers.firstOrNull { answer -> answer.id == answerId }?.let { answerView ->
                answerView.comments.add(comment.toCommentView())
            } ?: throw AnswerNotFoundException("Answer $answerId does not exists.")


            exerciseRepository.save(exercise)
        } ?: throw ExerciseNotFoundException("Exercise $exerciseId does not exist.")
    }

    private fun Exercise.toExerciseView() = ExerciseView(
        id = this.id,
        classId = this.classId,
        question = this.question.toQuestionView(),
        isPublished = this.isPublished,
        creatorId = this.creatorId
    )

    private fun Question.toQuestionView() = QuestionView(
        title = this.title,
        description = this.description
    )

    private fun Answer.toAnswerView() = AnswerView(
        id = this.id,
        userId = this.userId,
        dateTime = this.dateTime,
        answer = this.answer
    )

    private fun Comment.toCommentView() = CommentView(
        id = this.id,
        userId = this.userId,
        dateTime = this.dateTime,
        comment = this.comment
    )
}