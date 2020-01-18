package com.cac.service.domain.user

import java.util.UUID

class User(
    var userName: String,
    var password: String,
    var email: String?,
    var phoneNumber: String?,
    val id: UUID = UUID.randomUUID()
) {
}

fun User.toUserDetails() =
    org.springframework.security.core.userdetails.User(this.userName, this.password, emptyList())

data class UserInfo( val id: UUID,  var userName: String? = null)
