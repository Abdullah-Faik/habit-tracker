package com.fola.habit_tracker.data.data_base

import androidx.room.TypeConverter
import java.time.LocalTime
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
    fun fromReminderTime(time: LocalTime): Int {
        return (time.hour * 100 + time.minute)
    }

    @TypeConverter
    fun toReminderTime(time: Int): LocalTime {
        return LocalTime.of(time / 100, time % 100)
    }
}
