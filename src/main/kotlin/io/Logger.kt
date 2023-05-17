package io

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

data class CheckedCounter(
    var total: Int = 0,
    var passed: Int = 0,
    var failed: Int = 0
)

object Logger {

    private lateinit var timeOfWriting: String
    private val groupsCounter = CheckedCounter()
    private val experimentsCounter = CheckedCounter()
    private val failedSequences = mutableListOf<String>()

    val text: String get() =
        buildString {
            appendLine("Time of log: $timeOfWriting")
            appendLine()
            appendCounter(groupsCounter, "Single Groups")
            appendCounter(experimentsCounter, "Full Experiments")
            appendLine()
            appendLine("Failed sequences:")
            failedSequences.forEach { sequence ->
                appendLine(sequence)
            }
        }

    private fun StringBuilder.appendCounter(counter: CheckedCounter, label: String) {
        appendLine("$label total: ${counter.total}")
        appendLine("\tpassed: ${counter.passed}")
        appendLine("\tfailed: ${counter.failed}")
    }

    fun countGroup(passed: Boolean) {
        groupsCounter.total++
        if (passed) groupsCounter.passed++ else groupsCounter.failed++
    }

    fun countExperiment(passed: Boolean) {
        experimentsCounter.total++
        if (passed) experimentsCounter.passed++ else experimentsCounter.failed++
    }

    fun addFailedSequence(sequence: String) {
        failedSequences.add(sequence)
    }

    fun registerTimeOfWriting() {
        timeOfWriting = formatTime(System.currentTimeMillis())
    }

    private fun formatTime(millis: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(millis),
            ZoneId.systemDefault()
        )
        val formatter = DateTimeFormatter.ofPattern(
            "HH:mm:ss",
            Locale.getDefault()
        )
        return localDateTime.format(formatter)
    }

}