package com.sevdesk.lite.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Fuck you")
open class NotFoundException : RuntimeException() {
}