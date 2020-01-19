package com.cac.service.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class CACExceptionHandler : ResponseEntityExceptionHandler() {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(CACExceptionHandler::class.java)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errorDetails = ex.bindingResult.allErrors.map {
            val message = when (it) {
                is FieldError -> "The field ${it.field} in ${it.objectName} failed validation"
                else -> "The object ${it.objectName} failed validation"
            }
            LOGGER.info(message)
            ErrorDetails(it.defaultMessage ?: "Validation failed", message)
        }
        return ResponseEntity(ErrorResponse(errorDetails), HttpStatus.BAD_REQUEST)
    }

    override fun handleExceptionInternal(ex: java.lang.Exception, body: Any?, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        LOGGER.info(ex.message);
        return super.handleExceptionInternal(ex, body, headers, status, request)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<ErrorResponse> = when (exception) {
        is AccessDeniedException -> handleForbidden(exception)
        is UsernameFoundException -> handleRepeatUsername(exception)
        else -> ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    private fun handleForbidden(exception: AccessDeniedException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ErrorResponse(listOf(
                ErrorDetails(exception.message ?: HttpStatus.FORBIDDEN.reasonPhrase, null)
            )))
    }

    private fun handleRepeatUsername(exception: UsernameFoundException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(listOf(
                ErrorDetails("User name already exists.", null)
            )))
}

class UsernameFoundException(): Exception()