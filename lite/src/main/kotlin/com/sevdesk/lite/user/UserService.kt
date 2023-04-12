package com.sevdesk.lite.user

import com.sevdesk.lite.exceptions.BadRequestException
import com.sevdesk.lite.exceptions.ConflictException
import com.sevdesk.lite.exceptions.NotFoundException
import com.sevdesk.lite.user.entities.User
import com.sevdesk.lite.util.jwt.JwtTokenUtil
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository, val jwtTokenUtil: JwtTokenUtil) {

    class UserExistsException : ConflictException()
    class UserNotFoundException : NotFoundException()

    class AuthenticationException : BadRequestException()

    fun getUserById(id: Long) = userRepository.findByIdOrNull(id) ?: throw UserNotFoundException()

    fun createUser(mail: String, password: String): User {
        val user = User().also { user: User ->
            user.mail = mail
            user.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())
        }

        try {
            return userRepository.save(user)
        } catch (dException: DataIntegrityViolationException) {
            // Constraint violation, indicates a user with the same email already exists
            throw UserExistsException()
        }
    }

    fun authenticateUser(mail: String, password: String): String {
        val user = userRepository.findUserByMail(mail) ?: throw AuthenticationException()

        if (!BCrypt.checkpw(password, user.passwordHash))
            throw AuthenticationException()

        return jwtTokenUtil.createToken(user.id!!)
    }
}