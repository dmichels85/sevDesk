package com.sevdesk.lite.customer

import com.sevdesk.lite.customer.entities.Customer
import com.sevdesk.lite.user.entities.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import java.util.Optional


@SpringBootTest
@ActiveProfiles("test")
class CustomerServiceTests {
    @Autowired
    lateinit var customerService: CustomerService

    @MockBean
    lateinit var customerRepository: CustomerRepository

    @Test
    fun testCreateCustomer() {
        Mockito.`when`(customerRepository.save(Mockito.any(Customer::class.java))).thenAnswer { invocation ->
            val customer: Customer = invocation.getArgument(0)
            customer.id = 1

            customer
        }

        val customer = customerService.createCustomer(User(), "foo", "bar")

        Assertions.assertNotNull(customer)
        Assertions.assertEquals(1, customer.id)
    }

    @Test
    fun testGetCustomerById() {
        val customerToReturn = Customer()
        customerToReturn.id = 1
        customerToReturn.surname = "foo"
        customerToReturn.givenname = "bar"

        Mockito.`when`(customerRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(customerToReturn))

        val customer = customerService.getCustomerById(1)
        Assertions.assertNotNull(customer)
        Assertions.assertEquals(1, customer.id)
    }


    @Test
    fun testCustomerNotFound() {
        Mockito.`when`(customerRepository.findById(Mockito.anyLong())).thenThrow(CustomerService.CustomerNotFoundException())

        Assertions.assertThrows(CustomerService.CustomerNotFoundException::class.java) {
            customerService.getCustomerById(1)
        }
    }
}