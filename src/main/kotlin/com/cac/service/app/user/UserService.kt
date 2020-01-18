package com.cac.service.app.user

import com.cac.service.domain.user.User
import com.cac.service.infrastructure.UserRepository
import com.cac.service.infrastructure.view.UserView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val userRepository: UserRepository
) {
    fun save(user: User) {
        userRepository.save(user.toUserView())
    }

    fun findByUserName(userName: String) =
        userRepository.findByUserName(userName)?.toUser()

    fun findAllUsers() =
            userRepository.findAll().map { it.toUser() }

    private fun User.toUserView() =
        UserView(this.id, this.userName, this.password, this.email, this.phoneNumber)

    private fun UserView.toUser() =
        User(this.userName, this.password, this.email, this.phoneNumber, this.id)
}
