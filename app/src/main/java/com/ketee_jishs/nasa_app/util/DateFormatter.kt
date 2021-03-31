package com.ketee_jishs.nasa_app.util

import java.util.*

fun getCurrentDate(): Date {
    return Calendar.getInstance().time
}

fun getYesterdayDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -1)
    return calendar.time
}

fun getDayBeforeYesterdayDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -2)
    return calendar.time
}

fun getThirdDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -3)
    return calendar.time
}

fun getForthDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -4)
    return calendar.time
}