package com.cac.service.domain.exercise

import java.util.UUID

class Comment {
    lateinit var id: UUID
    lateinit var comment: String
    var isPublished: Boolean = false
}