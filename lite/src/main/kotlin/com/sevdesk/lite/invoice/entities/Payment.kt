package com.sevdesk.lite.invoice.entities

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "PAYMENTS")
data class Payment(
    @Column(name = "creation_date")
    val creationDate: LocalDateTime,

    @Column(name = "amount")
    val amount: BigDecimal,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null
}