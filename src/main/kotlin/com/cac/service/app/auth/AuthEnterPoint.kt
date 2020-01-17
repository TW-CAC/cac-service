package com.cac.service.app.auth

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.Serializable

@Component
class AuthEntryPoint : AuthenticationEntryPoint, Serializable {

    override fun commence(request: HttpServletRequest,
                          response: HttpServletResponse,
                          e: AuthenticationException) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
    }

    companion object {

        private const val serialVersionUID = -7858869558953243875L
    }
}
