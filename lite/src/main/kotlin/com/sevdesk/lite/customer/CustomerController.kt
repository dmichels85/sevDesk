package com.sevdesk.lite.customer

import com.sevdesk.lite.customer.entities.Customer
import com.sevdesk.lite.user.entities.User
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users/{user}/customers")
class CustomerController(private val customerService: CustomerService) {

    data class CreateCustomerRequest(val surname: String, val givenname: String)

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(@PathVariable("user") user: User,
                       @RequestBody createCustomerRequest: CreateCustomerRequest
    ) : Customer {
        return customerService.createCustomer(user, createCustomerRequest.surname, createCustomerRequest.givenname)
    }

}