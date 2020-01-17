package com.cac.service.infrastructure

import com.cac.service.infrastructure.view.UserView
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface UserRepository: MongoRepository<UserView, UUID> {
    fun findByUserName(userName: String): UserView?
}