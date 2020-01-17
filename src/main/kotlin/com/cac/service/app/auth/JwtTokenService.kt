package com.cac.service.app.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

import java.util.Date
import java.util.HashMap

@Service
class JwtTokenService {
    companion object {
        const val NICK_NAME = "nickname";
    }

    @Value("\${auth.jwt.secret}")
    private val secret: String? = null
    @Value("#{\${auth.token.valid.time}}")
    private val tokenValidTime: Long ?= null
    @Value("\${auth.jwt.subject}")
    private val subject: String? = null

    fun generateToken(userDetails: UserDetails): String {
        val claims = HashMap<String, Any>()

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + tokenValidTime!!))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact()
    }

    fun getUserName(token: String): String? = getAllClaims(token).subject

    fun validateToken(token: String, userDetails: UserDetails) =
        getUserName(token) == userDetails.username && !isTokenExpired(token)

    fun getExpirationDate(token: String): Date = getAllClaims(token).expiration

    fun isTokenExpired(token: String) = getExpirationDate(token).before(Date())

    private fun getAllClaims(token: String) =
        Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
}
