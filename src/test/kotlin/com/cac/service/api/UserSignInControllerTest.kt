package com.cac.service.api

import com.cac.service.app.auth.AuthUserDetailsService
import com.cac.service.app.auth.JwtTokenService
import com.cac.service.app.user.UserService
import com.cac.service.domain.user.User
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.Runs
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import org.hamcrest.Matchers
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(MockKExtension::class)
@WebMvcTest(controllers = [UserSignInController::class])
class UserSignInControllerTest {
    companion object {
        const val AUTO_LOGIN_URL = "/api/rest/auth/user/auto-login"
        const val LOGIN_URL = "/api/rest/auth/user/login"
        const val REGISTER_URL = "/api/rest/auth/user/register"
    }

    @TestConfiguration
    class TestConfig {
        @Bean
        fun jwtTokenService(): JwtTokenService = mockk(relaxed = true)

        @Bean
        fun userService(): UserService = mockk(relaxed = true)

        @Bean
        fun authUserDetailsService(): AuthUserDetailsService = mockk(relaxed = true)
    }

    @Autowired
    private lateinit var jwtTokenService: JwtTokenService
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var mockMVC: MockMvc

    fun postCommand(uri: String, requestJson: String? = null) =
        mockMVC.perform(
            MockMvcRequestBuilders.post(uri)
                .header("Authorization", "TestHeaderValue")
                .contentType(MediaType.APPLICATION_JSON_UTF8).apply {
                    requestJson?.let { content(requestJson) }
                }
        )

    fun asJson(obj: Any) = objectMapper.writeValueAsString(obj)

    @Nested
    inner class AutoLoginTest {
        @Test
        fun `should return 403 when user auto login given token expired`() {
            every { jwtTokenService.isTokenExpired(any()) } returns true

            postCommand(AUTO_LOGIN_URL).andExpect(status().isForbidden)
        }

        @Test
        fun `should return 403 when user auto login given there is no user name in token`() {
            every { jwtTokenService.isTokenExpired(any()) } returns true
            every { jwtTokenService.getUserName(any()) } returns null

            postCommand(AUTO_LOGIN_URL).andExpect(status().isForbidden)
        }

        @Test
        fun `should return 403 when user auto login given user name is invalid`() {
            every { jwtTokenService.isTokenExpired(any()) } returns true
            every { jwtTokenService.getUserName(any()) } returns "test"
            every { userService.findByUserName(any()) } returns null

            postCommand(AUTO_LOGIN_URL).andExpect(status().isForbidden)
        }

        @Test
        fun `should return 403 when user auto login given token is invalid`() {
            every { jwtTokenService.isTokenExpired(any()) } returns true
            every { jwtTokenService.getUserName(any()) } returns "test"
            every {
                userService.findByUserName(any())
            } returns User("test", "password", null, null)
            every { jwtTokenService.validateToken(any(), any()) } returns false

            postCommand(AUTO_LOGIN_URL).andExpect(status().isForbidden)
        }
    }

    @Nested
    inner class LoginTest {
        @Test
        fun `should return 403 when user login given user name is invalid`() {
            every { userService.findByUserName(any()) } returns null

            postCommand(LOGIN_URL, userLoginRequest()).andExpect(status().isForbidden)
        }

        @Test
        fun `should return 403 when user login given user password is invalid`() {
            every {
                userService.findByUserName(any())
            } returns User("test", "error_password", null, null)

            postCommand(LOGIN_URL, userLoginRequest()).andExpect(status().isForbidden)
        }

        @Test
        fun `should return token when user login given user request is valid`() {
            every {
                userService.findByUserName(any())
            } returns User("test", "password", null, null)
            every { jwtTokenService.generateToken(any()) } returns "fake-token"

            postCommand(LOGIN_URL, userLoginRequest())
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.token", Matchers.`is`("fake-token")))
        }

        fun userLoginRequest(): String = asJson(UserLoginRequest(
            userName = "test",
            password = "password"
        ))
    }

    @Nested
    inner class UserRegisterTest {
        @Test
        fun `should return 400 when user register given user name already exists`() {
            every { userService.findByUserName(any()) } returns User("test", "password", null, null)

            postCommand(REGISTER_URL, userRegisterRequest())
                .andExpect(status().isBadRequest)
        }

        @Test
        fun `should return token when user register given user request is valid`() {
            every { userService.findByUserName(any()) } returns null
            every { userService.save(any()) } just Runs
            every { jwtTokenService.generateToken(any()) } returns "fake-token"


            postCommand(REGISTER_URL, userRegisterRequest())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.token", Matchers.`is`("fake-token")))
        }

        fun userRegisterRequest() = asJson(
            UserRegisterRequest(
                userName = "test",
                password = "password",
                email = "test@qq.com",
                phoneNumber = "123456789"
            )
        )
    }
}