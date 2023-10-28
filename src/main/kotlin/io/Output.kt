package io

import Data
import ROOT_PATH
import java.io.File
import java.util.*

fun writeExperiment(experiment: Data.Experiment) {
    val text = buildString {
        appendGroup(experiment.groupA, GroupType.A)
        appendGroup(experiment.groupB, GroupType.B)
    }
    val path = getUniquePath("experiment")
    println("Writing experiment...")
    File(path).writeText(text)
}

fun writeGroup(group: Data.Group) {
    val text = buildString {
        appendGroup(group)
    }
    val path = getUniquePath("group")
    println("Writing group...")
    File(path).writeText(text)
}

fun writePhase(phase: Data.Phase) {
    val text = buildString {
        appendPhase(phase)
    }
    val path = getUniquePath("phase")
    File(path).writeText(text)
}

private fun getUniquePath(title: String, extension: String = ".csv") =
    "$ROOT_PATH/${title}_${UUID.randomUUID()}$extension"

fun writeLog() {
    Logger.registerTimeOfWriting()
    val path = getUniquePath("log", ".txt")
    println("Writing log...")
    File(path).writeText(Logger.text)
}