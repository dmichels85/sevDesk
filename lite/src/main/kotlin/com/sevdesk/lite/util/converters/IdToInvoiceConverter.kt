package com.sevdesk.lite.util.converters

import com.sevdesk.lite.exceptions.NotAllowedException
import com.sevdesk.lite.exceptions.NotFoundException
import com.sevdesk.lite.invoice.InvoiceRepository
import com.sevdesk.lite.invoice.entities.Invoice
import com.sevdesk.lite.user.entities.User
import org.springframework.core.convert.converter.Converter
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder


@Component
class IdToInvoiceConverter(private val invoiceRepository: InvoiceRepository) : Converter<String, Invoice> {
    override fun convert(source: String): Invoice {
        val invoice = invoiceRepository.findByIdOrNull(source.toLong()) ?: throw NotFoundException()

        val currentUser = RequestContextHolder.currentRequestAttributes().getAttribute("user", RequestAttributes.SCOPE_REQUEST) as User

        if (invoice.customer!!.owner.id != currentUser.id)
            throw NotAllowedException()

        return invoice

    }
}