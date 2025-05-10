package com.gracetastybites.restaurant.data.local.entity

import androidx.room.TypeConverter
import java.util.Date


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromStaffRole(value: StaffRole): String {
        return value.name
    }

    @TypeConverter
    fun toStaffRole(value: String): StaffRole {
        return StaffRole.valueOf(value)
    }

    @TypeConverter
    fun fromMenuCategory(value: MenuCategory): String {
        return value.name
    }

    @TypeConverter
    fun toMenuCategory(value: String): MenuCategory {
        return MenuCategory.valueOf(value)
    }

    @TypeConverter
    fun fromOrderStatus(value: OrderStatus): String {
        return value.name
    }

    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus {
        return OrderStatus.valueOf(value)
    }
}