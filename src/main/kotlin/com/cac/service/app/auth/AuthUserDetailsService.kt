package com.cac.service.app.auth

import com.cac.service.app.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthUserDetailsService(
    @Autowired val userService: UserService
) : UserDetailsService {
    override fun loadUserByUsername(userName: String): UserDetails {
        val user = userService.findByUserName(userName)
        return user?.let {
            User(user.userName, user.password, emptyList())
        } ?: throw UsernameNotFoundException("User not found with username: $userName")

    }
}
