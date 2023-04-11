package com.sevdesk.lite.invoice

import com.fasterxml.jackson.annotation.JsonFormat
import com.sevdesk.lite.customer.CustomerService
import com.sevdesk.lite.exceptions.NotAllowedException
import com.sevdesk.lite.invoice.entities.Invoice
import com.sevdesk.lite.invoice.entities.Payment
import com.sevdesk.lite.user.entities.User
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.LocalDate

@RestController
@RequestMapping("/api/")
@Validated
class InvoiceController(
    private val invoiceRepository: InvoiceRepository,
    private val invoiceService: InvoiceService,
    private val customerService: CustomerService,
) {

   @GetMapping("/users/{user}/invoices")
   fun getInvoicesOfUser(page: Pageable = Pageable.unpaged(),
                         @PathVariable("user") user: User): List<Invoice> {
        return invoiceService.getUserInvoices(user)
    }


    @GetMapping("/invoices/{id}")
    fun getInvoice(@PathVariable("id") id: Long): Invoice {
        return invoiceService.getInvoice(id)
    }


    data class CreateInvoiceRequest(
        @JsonFormat(pattern = "dd.MM.yyyy", timezone = "Etc/UTC")
        val dueDate: LocalDate,

        val invoiceNumber: String, val quantity: BigDecimal,
        val priceNet: BigDecimal, val priceGross: BigDecimal, val customerId: Long
    )


    @PostMapping("/users/{user}/invoices", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createInvoice(
        @PathVariable("user") user: User,
        @RequestBody createInvoiceRequest: CreateInvoiceRequest
    ): Invoice {
        val customer = customerService.getCustomerById(createInvoiceRequest.customerId)

        if (customer.owner != user) {
            throw NotAllowedException()
        }

        return invoiceService.createInvoice(customer, createInvoiceRequest.dueDate,
            createInvoiceRequest.invoiceNumber, createInvoiceRequest.quantity, createInvoiceRequest.priceNet,
            createInvoiceRequest.priceGross)
    }


    @DeleteMapping("/invoices/{invoice}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun deleteInvoice(@PathVariable("invoice") invoice: Invoice) {
        invoiceService.deleteInvoice(invoice)
    }

    data class PaymentRequest(val amount: BigDecimal)

    @PostMapping("/invoices/{invoice}/payments")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun payment(@PathVariable("invoice") invoice: Invoice, @RequestBody paymentRequest: PaymentRequest) : Payment {
        return invoiceService.pay(invoice, paymentRequest.amount)
    }
}
