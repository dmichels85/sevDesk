package com.sevdesk.lite.customer

import com.sevdesk.lite.customer.entities.Customer
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : CrudRepository<Customer, Long> {
}