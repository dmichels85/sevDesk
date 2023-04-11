package com.sevdesk.lite.util.converters

import com.sevdesk.lite.exceptions.NotAllowedException
import com.sevdesk.lite.exceptions.NotFoundException
import com.sevdesk.lite.user.UserRepository
import com.sevdesk.lite.user.entities.User
import org.springframework.core.convert.converter.Converter
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder

@Component
class IdToUserConverter(private val userRepository: UserRepository) : Converter<String, User> {
    override fun convert(source: String): User {
        val currentUser = RequestContextHolder.currentRequestAttributes().getAttribute("user", RequestAttributes.SCOPE_REQUEST) as User


        val user = userRepository.findByIdOrNull(source.toLong()) ?: throw NotFoundException()

        if (user.id != currentUser.id) {
            throw NotAllowedException()
        }

        return user
    }
}