package com.faigenbloom.currencyconvertertest.common

import java.math.BigDecimal
import java.math.RoundingMode

fun Long.toStringMoney(): String =
    String.format("%.2f", this.toDouble() / 100.0)
        .replace(",", ".")

fun String.toLongMoney(): Long = if (this.isNotBlank()) {
    if (this.contains(".")) {
        this.replace(".", "").toLong()
    } else {
        this.toLong() * 100
    }
} else 0L

fun Long.toDoubleMoney() = BigDecimal(this) / BigDecimal(100.0)

fun BigDecimal.toLongMoney() = (this * BigDecimal(100.0)).setScale(2, RoundingMode.HALF_UP).toLong()
