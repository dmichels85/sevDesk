package com.sevdesk.lite.user

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/public/api/users")
@Validated
class UserController(val userService: UserService) {

    data class CreateUserRequest(val mail: String, val password: String)

    data class CreateUserResponse(val id: Long)

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody createUserRequest: CreateUserRequest) : CreateUserResponse {
        val user = userService.createUser(createUserRequest.mail, createUserRequest.password)
        return CreateUserResponse(user.id!!)
    }

    data class AuthenticationResponse(val token: String)

    @PostMapping("/authenticate")
    fun authenticateUser(@RequestBody authenticationRequest: CreateUserRequest) : AuthenticationResponse {
        return AuthenticationResponse(userService.authenticateUser(authenticationRequest.mail, authenticationRequest.password))
    }
}
