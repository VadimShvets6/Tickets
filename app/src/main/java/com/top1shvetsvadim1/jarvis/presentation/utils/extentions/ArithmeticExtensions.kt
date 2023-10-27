package com.top1shvetsvadim1.jarvis.presentation.utils.extentions

fun Double.roundToTwoDecimal(): Double {
    val number3digits: Double = Math.round(this * 1000.0) / 1000.0
    val number2digits: Double = Math.round(number3digits * 100.0) / 100.0
    return Math.round(number2digits * 10.0) / 10.0
}