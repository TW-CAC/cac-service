package com.cac.service.domain.course

import java.util.UUID

class Course {
    lateinit var id: UUID
    lateinit var code: String
    lateinit var title: String
    lateinit var description: String
    lateinit var creator: UUID
    lateinit var questions: List<Question>
}

enum class CourseStatus {
    ENABLE_SUBSCRIBED, ENABLE_CLASS, CLASS_OPENED
}