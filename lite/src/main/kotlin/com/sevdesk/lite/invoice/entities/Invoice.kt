package com.sevdesk.lite.invoice.entities

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.sevdesk.lite.customer.entities.Customer
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "INVOICES")
class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "status", length = 50)
    @Enumerated(EnumType.STRING)
    var status: InvoiceStatus? = null

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @Column(name = "creation_date")
    var creationDate: LocalDateTime? = null

    @JsonFormat(pattern = "dd.MM.yyyy")
    @Column(name = "due_date")
    var dueDate: LocalDate? = null

    @Column(name = "invoice_number")
    var invoiceNumber: String? = null

    @Column(name = "quantity")
    var quantity: BigDecimal? = null

    @Column(name = "price_net")
    var priceNet: BigDecimal? = null

    @Column(name = "price_gross")
    var priceGross: BigDecimal? = null

    @JsonIgnore
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var customer: Customer? = null

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var payments: MutableList<Payment> = mutableListOf()
}
