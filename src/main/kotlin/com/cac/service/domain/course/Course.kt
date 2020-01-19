package com.cac.service.domain.course

import com.cac.service.domain.user.UserInfo
import java.lang.IllegalArgumentException
import java.util.UUID

data class Question(val title: String,
                    val description: String?
)

data class Course(val id: UUID = UUID.randomUUID(),
                  val title: String,
                  val description: String,
                  val creator: UserInfo,
                  val questions: List<Question>,
                  var subscribers: List<UserInfo> = listOf()
) {
    fun subscribe(uuid: UUID) {
        if (creator.id == uuid) {
            throw IllegalArgumentException("Creator $uuid can't subscribe his/her own courses!")
        }
        if (subscribers.find { it.id == uuid } == null) {
            subscribers = subscribers.plus(UserInfo(uuid))
        }
    }
}

enum class CourseStatus {
    ENABLE_SUBSCRIBED, ENABLE_CLASS, CLASS_OPENED
}
