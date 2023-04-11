package com.sevdesk.lite.customer.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.sevdesk.lite.user.entities.User
import javax.persistence.*

@Entity
@Table(name = "CUSTOMERS")
class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "givenname")
    var givenname: String? = null

    @Column(name = "surname")
    var surname: String? = null

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var owner: User
}
