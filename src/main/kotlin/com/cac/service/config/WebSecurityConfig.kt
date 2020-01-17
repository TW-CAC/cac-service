package com.cac.service.config

import com.cac.service.app.auth.AuthEntryPoint
import com.cac.service.app.auth.AuthRequestFilter
import com.cac.service.app.auth.AuthUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private val authEntryPoint: AuthEntryPoint? = null

    @Autowired
    private val userDetailsService: AuthUserDetailsService? = null

    @Autowired
    private val authRequestFilter: AuthRequestFilter? = null

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity.csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/rest/auth/**").permitAll()
            .anyRequest().authenticated().and()
            .exceptionHandling().authenticationEntryPoint(authEntryPoint).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        httpSecurity.addFilterBefore(authRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}
