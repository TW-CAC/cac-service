package com.cac.service.app.auth

import assertk.assert
import assertk.assertions.isEqualTo
import com.cac.service.app.user.UserService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkClass
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.userdetails.UsernameNotFoundException

@ExtendWith(MockKExtension::class)
class AuthUserDetailsServiceTest {
    private val userService = mockkClass(UserService::class)

    private lateinit var authUserDetailsService: AuthUserDetailsService

    @BeforeEach
    fun setup() {
        authUserDetailsService = AuthUserDetailsService(userService)
    }

    @Test
    fun `should load user success when call loadUserByUsername given then user name is valid`() {
        val userName = "tester"
        val password = "123,qaz"
        val email = "test@qq.com"
        val phoneNumber = "133333333333"
        every { userService.findByUserName(userName) } returns com.cac.service.domain.user.User(userName, password, email, phoneNumber)

        val userDetails = authUserDetailsService.loadUserByUsername(userName)

        assert(userDetails.username).isEqualTo(userName)
        assert(userDetails.password).isEqualTo(password)
    }

    @Test
    fun `should throw UsernameNotFoundException when call loadUserByUsername given can not find user`() {
        val userName = "tester"
        every { userService.findByUserName(any()) } returns null

        assertThrows<UsernameNotFoundException> {
            authUserDetailsService.loadUserByUsername(userName)
        }
    }
}