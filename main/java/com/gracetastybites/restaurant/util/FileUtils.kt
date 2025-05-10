package com.gracetastybites.restaurant.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.gracetastybites.restaurant.data.local.entity.Invoice
import com.gracetastybites.restaurant.data.local.entity.Order
import com.gracetastybites.restaurant.data.local.entity.OrderItem
import com.gracetastybites.restaurant.data.local.entity.Staff
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {

    fun getAppExternalFilesDir(context: Context, dirName: String): File {
        val dir = File(context.getExternalFilesDir(null), dirName)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    fun createFileUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "com.gracetastybites.restaurant.fileprovider",
            file
        )
    }

    @Throws(IOException::class)
    fun createPdfInvoice(
        context: Context,
        invoice: Invoice,
        staff: Staff,
        shifts: Int
    ): File {
        val invoiceDir = getAppExternalFilesDir(context, Constants.INVOICE_DIRECTORY)
        val fileName = "invoice_${staff.staffId}_${invoice.month}_${invoice.year}.pdf"
        val file = File(invoiceDir, fileName)

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        // Draw invoice content
        // This is a simplified implementation - in a real app you'd use a proper PDF generating library
        val paint = android.graphics.Paint()
        paint.textSize = 12f

        // Header
        canvas.drawText("INVOICE", 250f, 50f, paint)
        canvas.drawText("Grace Tasty Bites", 250f, 70f, paint)

        // Employee Info
        canvas.drawText("Employee: ${staff.name}", 50f, 120f, paint)
        canvas.drawText("Role: ${staff.role}", 50f, 140f, paint)
        canvas.drawText("Month: ${getMonthName(invoice.month)} ${invoice.year}", 50f, 160f, paint)

        // Invoice Details
        canvas.drawText("Total Shifts: $shifts", 50f, 200f, paint)
        canvas.drawText("Pay Per Shift: $${String.format("%.2f", staff.payPerShift)}", 50f, 220f, paint)
        canvas.drawText("Total Amount: $${String.format("%.2f", invoice.totalAmount)}", 50f, 240f, paint)

        pdfDocument.finishPage(page)

        try {
            FileOutputStream(file).use { out ->
                pdfDocument.writeTo(out)
            }
        } finally {
            pdfDocument.close()
        }

        return file
    }

    @Throws(IOException::class)
    fun createPdfReceipt(
        context: Context,
        order: Order,
        orderItems: List<OrderItem>,
        menuItemNames: Map<Long, String>
    ): File {
        val receiptDir = getAppExternalFilesDir(context, Constants.RECEIPT_DIRECTORY)
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val fileName = "receipt_${dateFormat.format(order.orderDate)}.pdf"
        val file = File(receiptDir, fileName)

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        // Draw receipt content
        val paint = android.graphics.Paint()
        paint.textSize = 12f

        // Header
        canvas.drawText("RECEIPT", 250f, 50f, paint)
        canvas.drawText("Grace Tasty Bites", 250f, 70f, paint)
        canvas.drawText("Order #: ${order.orderId}", 250f, 90f, paint)

        // Customer Info
        canvas.drawText("Customer: ${order.customerName}", 50f, 120f, paint)
        if (order.customerPhone != null) {
            canvas.drawText("Phone: ${order.customerPhone}", 50f, 140f, paint)
        }

        // Order Items
        canvas.drawText("Items:", 50f, 180f, paint)

        var yPosition = 200f
        for (item in orderItems) {
            val name = menuItemNames[item.itemId] ?: "Unknown Item"
            canvas.drawText("${item.quantity}x $name", 70f, yPosition, paint)
            canvas.drawText("$${String.format("%.2f", item.priceAtOrder)}", 350f, yPosition, paint)
            yPosition += 20f
        }

        // Total
        canvas.drawText("Total: $${String.format("%.2f", order.totalAmount)}", 300f, yPosition + 20f, paint)

        pdfDocument.finishPage(page)

        try {
            FileOutputStream(file).use { out ->
                pdfDocument.writeTo(out)
            }
        } finally {
            pdfDocument.close()
        }

        return file
    }

    private fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Unknown"
        }
    }
}