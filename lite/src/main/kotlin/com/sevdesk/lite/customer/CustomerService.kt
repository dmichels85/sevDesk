package com.sevdesk.lite.customer

import com.sevdesk.lite.customer.entities.Customer
import com.sevdesk.lite.exceptions.NotFoundException
import com.sevdesk.lite.user.entities.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CustomerService (private val customerRepository: CustomerRepository) {

    class CustomerNotFoundException : NotFoundException()

    fun getCustomerById(id: Long) = customerRepository.findByIdOrNull(id) ?: throw CustomerNotFoundException()

    fun createCustomer(owner: User, surname: String, givenname: String): Customer {
        val customer = Customer()
        customer.surname = surname
        customer.givenname = givenname
        customer.owner = owner

        return customerRepository.save(customer)
    }

}