package com.sevdesk.lite.user

import com.sevdesk.lite.user.entities.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findUserByMail(mail: String) : User?
}