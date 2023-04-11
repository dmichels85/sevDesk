package com.sevdesk.lite.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationTests {

    @Autowired
    lateinit var userService: UserService

    @MockBean
    lateinit var userRepository: UserRepository

    fun testAuthentication() {

    }

}