package com.sevdesk.lite.user

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTests {

    @Autowired
    lateinit var userService: UserService

    @Test
    fun testUserCreation() {
        val user = userService.createUser("user1", "geheim")

        Assertions.assertNotNull(user)
        Assertions.assertTrue(BCrypt.checkpw("geheim", user.passwordHash))

        Assertions.assertNotNull(userService.authenticateUser("user1", "geheim"))
    }

    @Test
    fun testDuplicateUserCreation() {
        Assertions.assertNotNull(userService.createUser("duplicateUser", "geheim"))

        Assertions.assertThrows(UserService.UserExistsException::class.java) {
            userService.createUser("duplicateUser", "otherPassword")
        }
    }

    @Test
    fun testAuthentication() {
        val user = userService.createUser("user1", "geheim")

        val token = userService.authenticateUser("user1", "geheim")

        Assertions.assertNotNull(token)
    }

}