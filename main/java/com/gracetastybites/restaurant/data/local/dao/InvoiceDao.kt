package com.gracetastybites.restaurant.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gracetastybites.restaurant.data.local.entity.Invoice

@Dao
interface InvoiceDao {
    @Query("SELECT * FROM invoices WHERE staffId = :staffId ORDER BY year DESC, month DESC")
    fun getInvoicesByStaffId(staffId: Long): LiveData<List<Invoice>>

    @Query("SELECT * FROM invoices WHERE staffId = :staffId AND month = :month AND year = :year LIMIT 1")
    fun getInvoiceByMonthYear(staffId: Long, month: Int, year: Int): LiveData<Invoice?>

    @Insert
    suspend fun insert(invoice: Invoice): Long

    @Update
    suspend fun update(invoice: Invoice)

    @Query("UPDATE invoices SET pdfPath = :pdfPath WHERE invoiceId = :invoiceId")
    suspend fun updatePdfPath(invoiceId: Long, pdfPath: String)
}