package com.eneskayiklik.yellowtaxi.util.extensions

fun String.getDayFromDate(): Int {
    return this.substringBefore(' ').substringAfterLast('-').toInt() - 1
}