package com.chuzi.android.shared.db

import androidx.room.TypeConverter
import java.util.*

/**
 * desc
 * author: 朱子楚
 * time: 2020/10/26 3:39 PM
 * since: v 1.0.0
 */
class Converters {

    /**
     *  Calendar 与 时间戳 相互转换
     */
    @TypeConverter
    fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun datestampToCalendar(value: Long): Calendar = Calendar.getInstance().apply { timeInMillis = value }

}