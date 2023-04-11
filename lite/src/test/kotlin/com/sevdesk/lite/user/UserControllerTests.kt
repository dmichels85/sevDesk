package com.sevdesk.lite.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.sevdesk.lite.invoice.InvoiceRepository
import com.sevdesk.lite.user.entities.User
import com.sevdesk.lite.util.converters.IdToInvoiceConverter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var userService: UserService

    @Test
    fun testCreateUser() {
        Mockito.`when`(userService.createUser(Mockito.anyString(), Mockito.anyString())).thenAnswer { invocation ->
            val mail: String = invocation.getArgument(0)
            val password: String = invocation.getArgument(1)

            val user = User()
            user.id = 1
            user.mail = mail
            user.passwordHash = password

            user
        }

        val servletResponse = mockMvc.perform(MockMvcRequestBuilders
            .post("/api/users/")
            .contentType(MediaType.APPLICATION_JSON)
            .content( ObjectMapper().writeValueAsString(UserController.CreateUserRequest("john@doe.de", "password"))))
            .andReturn()

        Assertions.assertEquals(201, servletResponse.response.status)
        Assertions.assertEquals("{\"id\":1}", servletResponse.response.contentAsString)
    }

    @Test
    fun testDuplicateUser() {
        Mockito.`when`(userService.createUser(Mockito.anyString(), Mockito.anyString())).thenThrow(UserService.UserExistsException())

        val servletResponse = mockMvc.perform(MockMvcRequestBuilders
            .post("/api/users/")
            .contentType(MediaType.APPLICATION_JSON)
            .content( ObjectMapper().writeValueAsString(UserController.CreateUserRequest("john@doe.de", "password"))))
            .andReturn()

        Assertions.assertEquals(409, servletResponse.response.status)
    }


}