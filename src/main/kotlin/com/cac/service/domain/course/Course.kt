package com.cac.service.domain.course

import com.cac.service.domain.user.UserInfo
import java.util.UUID

data class Question(val title: String,
               val description: String?
)

data class Course(val id: UUID = UUID.randomUUID(),
                  val title: String,
                  val description: String,
                  val creator: UserInfo,
                  val questions: List<Question>,
                  var subscribers: List<UserInfo>? = null
)

enum class CourseStatus {
    ENABLE_SUBSCRIBED, ENABLE_CLASS, CLASS_OPENED
}
