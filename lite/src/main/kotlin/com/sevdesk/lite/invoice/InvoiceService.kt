package com.sevdesk.lite.invoice

import com.sevdesk.lite.customer.entities.Customer
import com.sevdesk.lite.exceptions.BadRequestException
import com.sevdesk.lite.exceptions.ConflictException
import com.sevdesk.lite.exceptions.NotFoundException
import com.sevdesk.lite.invoice.entities.Invoice
import com.sevdesk.lite.invoice.entities.InvoiceStatus
import com.sevdesk.lite.invoice.entities.Payment
import com.sevdesk.lite.user.entities.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.HttpClientErrorException.BadRequest
import java.time.LocalDate
import java.math.BigDecimal
import java.time.LocalDateTime


@Service
class InvoiceService(private val invoiceRepository: InvoiceRepository) {

    class DueDateInPastException : ConflictException()
    class NegativePriceException : BadRequestException()

    fun getUserInvoices(user: User): List<Invoice> {
        return invoiceRepository.findAll(InvoiceJpaSpecifications.invoicesOfUser(user)).toList()
    }

    fun getInvoice(id: Long): Invoice {
        return invoiceRepository.findByIdOrNull(id) ?: throw NotFoundException()
    }

    fun deleteInvoice(invoice: Invoice) {
        invoiceRepository.delete(invoice)
    }


    fun createInvoice(customer: Customer, dueDate: LocalDate, invoiceNumber: String, quantity: BigDecimal,
                      priceNet: BigDecimal, priceGross: BigDecimal): Invoice {

        if (dueDate.isBefore(LocalDate.now())) {
            throw DueDateInPastException()
        }

        if (priceNet <= BigDecimal.ZERO || priceGross <= BigDecimal.ZERO) {
            throw NegativePriceException()
        }

        val invoice = Invoice()
        invoice.invoiceNumber = invoiceNumber
        invoice.creationDate = LocalDateTime.now()
        invoice.customer = customer
        invoice.dueDate = dueDate
        invoice.quantity = quantity
        invoice.priceNet = priceNet
        invoice.priceGross = priceGross
        invoice.status = InvoiceStatus.OPEN

        return invoiceRepository.save(invoice)
    }

    @Transactional
    fun pay(invoice: Invoice, amount: BigDecimal) : Payment {
        val payment = Payment(LocalDateTime.now(), amount)

        invoice.payments.add(payment)

        val totalSum = invoice.payments.sumOf { it.amount }

        if (totalSum >= invoice.priceGross)
            invoice.status = InvoiceStatus.PAID
        else
            invoice.status = InvoiceStatus.PARTLY_PAID

        return payment
    }


}
