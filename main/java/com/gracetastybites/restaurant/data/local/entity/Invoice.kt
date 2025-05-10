package com.gracetastybites.restaurant.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "invoices",
    foreignKeys = [
        ForeignKey(
            entity = Staff::class,
            parentColumns = ["staffId"],
            childColumns = ["staffId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("staffId")]
)
data class Invoice(
    @PrimaryKey(autoGenerate = true)
    val invoiceId: Long = 0,
    val staffId: Long,
    val month: Int,
    val year: Int,
    val totalAmount: Double,
    val hoursWorked: Double,
    val datePrepared: Long, // Store as timestamp
    val isPaid: Boolean = false,
    val pdfPath: String? = null
)