package com.sevdesk.lite.invoices

import com.sevdesk.lite.customer.entities.Customer
import com.sevdesk.lite.invoice.InvoiceRepository
import com.sevdesk.lite.invoice.InvoiceService
import com.sevdesk.lite.invoice.entities.Invoice
import com.sevdesk.lite.invoice.entities.InvoiceStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
class InvoiceServiceTests {

    @Autowired
    lateinit var invoiceService: InvoiceService

    @MockBean
    lateinit var invoiceRepository: InvoiceRepository

    @Test
    fun testFullPayment() {
        val invoice = Invoice()
        invoice.priceGross = BigDecimal.valueOf(10)

        invoiceService.pay(invoice, BigDecimal.valueOf(10))

        Assertions.assertEquals(InvoiceStatus.PAID, invoice.status)
    }

    @Test
    fun testPartlyPayment() {
        val invoice = Invoice()
        invoice.priceGross = BigDecimal.valueOf(10)

        invoiceService.pay(invoice, BigDecimal.valueOf(7.8))

        Assertions.assertEquals(InvoiceStatus.PARTLY_PAID, invoice.status)

        invoiceService.pay(invoice, BigDecimal.valueOf(2.2))

        Assertions.assertEquals(InvoiceStatus.PAID, invoice.status)
    }

    @Test
    fun testDueDateInPast() {
        Assertions.assertThrows(InvoiceService.DueDateInPastException::class.java) {
            invoiceService.createInvoice(
                Customer(), LocalDate.now().minusDays(1), "foo-11-2023",
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE
            )
        }
    }

    @Test
    fun testNegativePrice() {
        Assertions.assertThrows(InvoiceService.NegativePriceException::class.java) {
            invoiceService.createInvoice(
                Customer(), LocalDate.now().plusDays(2), "foo-11-2023",
                BigDecimal.ONE, BigDecimal.valueOf(-10), BigDecimal.valueOf(-10)
            )
        }
    }

}