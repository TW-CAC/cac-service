package com.cac.service.app.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthRequestFilter(
    @Autowired val jwtTokenService: JwtTokenService,
    @Autowired val authUserDetailsService: AuthUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  filterChain: FilterChain) {
        val token = request.getHeader("Authorization")
        token?.let {
            val userName = jwtTokenService.getUserName(token)

            if (userName != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails = authUserDetailsService.loadUserByUsername(userName)
                if (jwtTokenService.validateToken(token, userDetails)) {
                    val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }

        filterChain.doFilter(request, response)
    }
}
