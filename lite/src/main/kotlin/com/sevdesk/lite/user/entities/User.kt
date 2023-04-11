package com.sevdesk.lite.user.entities

import com.sevdesk.lite.customer.entities.Customer
import javax.persistence.*

@Entity
@Table(name = "USERS")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "mail", nullable = false, unique = true)
    var mail: String? = null

    @Column(name = "password_hash", nullable = false)
    var passwordHash: String? = null
}