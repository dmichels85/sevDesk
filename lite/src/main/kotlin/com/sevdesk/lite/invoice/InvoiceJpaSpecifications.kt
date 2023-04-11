package com.sevdesk.lite.invoice

import com.sevdesk.lite.customer.entities.Customer
import com.sevdesk.lite.invoice.entities.Invoice
import com.sevdesk.lite.user.entities.User
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.Join


class InvoiceJpaSpecifications {

    companion object {
        fun invoicesOfUser(user: User): Specification<Invoice> {
            return Specification<Invoice> { root, _, criteriaBuilder ->
                val customerInvoices: Join<Customer, Invoice> = root.join("customer")
                criteriaBuilder.equal(customerInvoices.get<User>("owner"), user)
            }
        }
    }
}