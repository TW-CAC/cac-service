package com.cac.service.exception

data class ErrorResponse(val errors: List<ErrorDetails>)
data class ErrorDetails(val title: String, val detail: String?)