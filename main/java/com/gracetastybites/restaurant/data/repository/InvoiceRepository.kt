package com.gracetastybites.restaurant.data.repository

import androidx.lifecycle.LiveData
import com.gracetastybites.restaurant.data.local.dao.InvoiceDao
import com.gracetastybites.restaurant.data.local.entity.Invoice

class InvoiceRepository(private val invoiceDao: InvoiceDao) {

    fun getInvoicesByStaffId(staffId: Long): LiveData<List<Invoice>> {
        return invoiceDao.getInvoicesByStaffId(staffId)
    }

    fun getInvoiceByMonthYear(staffId: Long, month: Int, year: Int): LiveData<Invoice?> {
        return invoiceDao.getInvoiceByMonthYear(staffId, month, year)
    }

    suspend fun addInvoice(invoice: Invoice): Long {
        return invoiceDao.insert(invoice)
    }

    suspend fun updateInvoice(invoice: Invoice) {
        invoiceDao.update(invoice)
    }

    suspend fun updatePdfPath(invoiceId: Long, pdfPath: String) {
        invoiceDao.updatePdfPath(invoiceId, pdfPath)
    }
}