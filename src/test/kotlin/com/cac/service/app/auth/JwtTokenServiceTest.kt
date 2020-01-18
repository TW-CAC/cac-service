package com.cac.service.app.auth

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.boot.test.context.SpringBootTest


@RunWith(SpringRunner::class)
@ActiveProfiles("test")
@SpringBootTest(classes = [JwtTokenService::class])
class JwtTokenServiceTest {
    @Autowired
    private lateinit var jwtTokenService: JwtTokenService

    @Test
    fun `should generate token when call generateToken given valid user details`() {
        val userDetails = User("test", "test", emptyList())

        val generatedToken = jwtTokenService.generateToken(userDetails)

        assertNotNull(generatedToken)
        assertEquals(jwtTokenService.getUserName(generatedToken), "test")
        assertFalse(jwtTokenService.isTokenExpired(generatedToken))
        assertTrue(jwtTokenService.validateToken(generatedToken, userDetails))
    }
}