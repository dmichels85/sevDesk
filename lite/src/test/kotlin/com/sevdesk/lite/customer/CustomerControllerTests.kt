package com.sevdesk.lite.customer

import com.fasterxml.jackson.databind.ObjectMapper
import com.sevdesk.lite.customer.entities.Customer
import com.sevdesk.lite.user.UserController
import com.sevdesk.lite.user.entities.User
import com.sevdesk.lite.util.converters.IdToUserConverter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CustomerControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var customerService: CustomerService

    @MockBean
    lateinit var idToUserConverter: IdToUserConverter

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

    @Test
    fun testCreateCustomer() {
        Mockito.`when`(idToUserConverter.convert(Mockito.anyString())).thenReturn(User())

        Mockito.`when`(customerService.createCustomer(any(User::class.java), Mockito.anyString(), Mockito.anyString()))
            .thenAnswer { invocation ->
                val customer = Customer()
                customer.id = 1
                customer.owner = invocation.getArgument(0)
                customer.surname = invocation.getArgument(1)
                customer.givenname = invocation.getArgument(2)
                customer
            }


        val servletResponse = mockMvc.perform(
            MockMvcRequestBuilders
            .post("/api/users/1/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content( ObjectMapper().writeValueAsString(CustomerController.CreateCustomerRequest("foo", "bar"))))
            .andReturn()

        Assertions.assertEquals(201, servletResponse.response.status)


    }

}