package com.gracetastybites.restaurant.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "shifts",
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
data class `Shift`(
    @PrimaryKey(autoGenerate = true)
    val shiftId: Long = 0,
    val staffId: Long,
    val date: Date,
    val startTime: String,
    val endTime: String,
    val isAccepted: Boolean? = null, // null=pending, true=accepted, false=declined
    val isCompleted: Boolean = false
)