package util

import java.time.LocalDateTime

fun getSeconds() = LocalDateTime.now().second

fun printSecondsDurationFor(start: Int, end: Int, event: String) {
    val difference = end - start
    val duration = if (difference < 0) 60 + difference else difference
    println("$event in $duration seconds")
}

fun getNano() = LocalDateTime.now().nano
fun getMillis() = System.currentTimeMillis()

fun printDurationFor(start: Int, end: Int, event: String, unit: String = "nanos") {
    println("$event in ${end - start} $unit")
}

fun printDurationFor(start: Long, end: Long, event: String, unit: String = "millis") {
    val duration = end - start
    val secondsTotal = duration / 1000
    val minutes = secondsTotal / 60
    val seconds = secondsTotal % 60
    println("$event in $duration $unit - $minutes minutes $seconds seconds")
}