package org.monsing

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    fun handleIllegalException(e: Exception): ResponseEntity<String> {
        return ResponseEntity.status(BAD_REQUEST).body(e.message)
    }

    @ExceptionHandler
    fun handleException(e: Exception): ResponseEntity<String> {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(e.message)
    }
}
