package com.cac.service.api

import com.cac.service.app.auth.JwtTokenService
import com.cac.service.app.user.UserService
import com.cac.service.domain.user.User
import com.cac.service.domain.user.toUserDetails
import com.cac.service.exception.UsernameFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/rest/auth")
class UserSignInController(
    @Autowired private val jwtTokenService: JwtTokenService,
    @Autowired private val userService: UserService
) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(UserSignInController::class.java)
    }

    @PostMapping("/user/auto-login")
    fun autoLogin(
        @RequestHeader("Authorization") token: String
    ) {
        if (jwtTokenService.isTokenExpired(token)) {
            throw AccessDeniedException("User token expired.")
        }

        val userName = jwtTokenService.getUserName(token)
        userName?.let {
            userService.findByUserName(it)?.let { user ->
                jwtTokenService.validateToken(token, user.toUserDetails())
            }
        } ?: throw AccessDeniedException("User token Invalid.")
    }

    @PostMapping("/user/login")
    fun login(@RequestBody request: UserLoginRequest): ResponseEntity<UserLoginResponse> {
        return userService.findByUserName(request.userName)?.let { user ->
            if (user.password != request.password) {
                throw AccessDeniedException("User password error.")
            }

            status(HttpStatus.OK).body(
                UserLoginResponse(jwtTokenService.generateToken(user.toUserDetails()))
            )
        } ?: throw AccessDeniedException("No such user")
    }

    @PostMapping("/user/register")
    fun registerUser(
        @RequestBody @Valid request: UserRegisterRequest
    ): ResponseEntity<UserLoginResponse> {
        val findUser = userService.findByUserName(request.userName)
        if (findUser != null) {
            LOGGER.info("User name {} already exists.", request.userName)
            throw UsernameFoundException()
        }

        userService.save(request.toUser());

        return status(HttpStatus.OK).body(
            UserLoginResponse(jwtTokenService.generateToken(request.toUser().toUserDetails()))
        )
    }
}

data class UserLoginRequest(
    val userName: String,
    val password: String
)

data class UserRegisterRequest(
    val userName: String,
    val password: String,
    val email: String?,
    val phoneNumber: String?
)

data class UserLoginResponse(
    val token: String
)

private fun UserRegisterRequest.toUser() =
    User(this.userName, this.password, this.email, this.phoneNumber)