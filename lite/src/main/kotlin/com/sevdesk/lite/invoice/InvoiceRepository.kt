package com.sevdesk.lite.invoice

import com.sevdesk.lite.invoice.entities.Invoice
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository

interface InvoiceRepository : CrudRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice>
