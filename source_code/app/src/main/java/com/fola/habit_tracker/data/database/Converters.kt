package com.fola.habit_tracker.data.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date


class Converters {

    // Convert Date to Long (timestamp)
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    // Convert Long (timestamp) to Date
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    // Convert RepeatedType to String
    @TypeConverter
    fun fromRepeatedType(type: RepeatedType?): String? {
        return type?.name
    }

    // Convert String to RepeatedType
    @TypeConverter
    fun toRepeatedType(name: String?): RepeatedType? {
        return name?.let { RepeatedType.valueOf(it) }
    }

    //convertDateAndTime
    @TypeConverter
    fun fromStartTime(time: LocalTime): Int {
        return (time.hour * 100 + time.minute)
    }

    @TypeConverter
    fun toStartTime(time: Int): LocalTime {
        return LocalTime.of(time / 100, time % 100)
    }

    // Convert LocalDate to String
    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    // Convert String to LocalDate
    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate {
        return   LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    @TypeConverter
    fun fromIntList(list: List<Int>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toIntList(data: String?): List<Int> {
        return data?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()
    }

    @TypeConverter
    fun fromIntSet(set: Set<Int>?): String {
        return set?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toIntSet(data: String?): MutableSet<Int> {
        return data?.split(",")?.mapNotNull { it.toIntOrNull() }?.toMutableSet() ?: mutableSetOf<Int>()
    }

}
