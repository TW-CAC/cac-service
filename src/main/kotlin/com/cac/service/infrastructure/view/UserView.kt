package com.cac.service.infrastructure.view

import java.util.UUID
import javax.persistence.Id

data class UserView(
    @Id
    val id: UUID,
    val userName: String,
    val password: String,
    val email: String?,
    val phoneNumber: String?
)
