package com.bmamaral.learning_logic.controller.app

import com.bmamaral.learning_logic.services.app.NotFoundException
import com.bmamaral.learning_logic.services.app.PreconditionFailedException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class HTTPNotFoundException(s: String) : Exception(s)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class HTTPBadRequestException(s: String) : Exception(s)

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
class HTTPIllegalArgumentException(s: String) : Exception(s)

fun <T> handle4xx(inner: () -> T): T = try {
    inner()
} catch (e: IllegalArgumentException) {
    throw HTTPIllegalArgumentException(e.message ?: "Illegal Arguments")
} catch (e: NotFoundException) {
    throw HTTPNotFoundException(e.message ?: "Not Found")
} catch (e: PreconditionFailedException) {
    throw HTTPBadRequestException(e.message ?: "Bad Request")
}
