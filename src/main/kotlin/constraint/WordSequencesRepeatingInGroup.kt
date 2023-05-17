package constraint

import Data
import io.Logger

fun Data.Group.checkWordSequencesRepeating(): Boolean {
    val acquisitionTextPairs = acquisition.associations.toTextPairs()
    val testingTextPairs = testing.associations.toTextPairs()

    val wordToValueMap = acquisitionTextPairs.asWordToValueMap()
    val acquisitionValuesPairs = acquisitionTextPairs.asValuesPairs(wordToValueMap)
    val sequences = mutableSetOf<Values>()
    acquisitionValuesPairs.forEach { values ->
        sequences.addAll(values.getSequences())
    }

    val testingValueArrayPairs = testingTextPairs.asValuesPairs(wordToValueMap)
    testingValueArrayPairs.forEachIndexed { i, valueArrayPair ->
        val testingSequences = valueArrayPair.getSequences()
        for (sequence in testingSequences) {
            if (sequences.contains(sequence)) {
                val textPair = testingTextPairs[i]
                val matchingSequence = sequence.toWords(wordToValueMap)
                Logger.addFailedSequence("$matchingSequence -> $textPair")
                return false
            }
        }
    }

    return true
}

private fun Values.toWords(wordToValueMap: Map<String, Int>): String {
    val valueToWordMap = mutableMapOf<Int, String>()
    wordToValueMap.forEach { entry ->
        valueToWordMap[entry.value] = entry.key
    }
    return intArray.joinToString(" ") { value -> valueToWordMap[value]!! }
}

private fun List<Pair<String, String>>.asValuesPairs(valueToWordMap: Map<String, Int>): List<Pair<Values, Values>> {
    val valueArrayPairs = mutableListOf<Pair<Values, Values>>()
    for (textPair in this) {
        val firstWords = textPair.first.split(" ")
        val secondWords = textPair.second.split(" ")
        val first = IntArray(firstWords.size) { valueToWordMap[firstWords[it]]!! }
        val second = IntArray(secondWords.size) { valueToWordMap[secondWords[it]]!! }
        val newPair = Pair(Values(first), Values(second))
        valueArrayPairs.add(newPair)
    }
    return valueArrayPairs
}

private fun List<Pair<String, String>>.asWordToValueMap(): Map<String, Int> {
    val wordsToValueMap = mutableMapOf<String, Int>()
    val wordsSet = mutableSetOf<String>()
    for (textsPair in this) {
        val words = "${textsPair.first} ${textsPair.second}".split(" ")
        println(words)
        wordsSet.addAll(words)
    }
    for ((i, word) in wordsSet.withIndex()) {
        wordsToValueMap[word] = i
    }
    return wordsToValueMap
}

// For testing purposes
private fun List<Pair<String, String>>.asWordToValueMap(extraTextsToMap: List<String>): Map<String, Int> {
    val wordsToValueMap = this.asWordToValueMap().toMutableMap()
    val extraWordsSet = mutableSetOf<String>()
    for (text in extraTextsToMap) {
        extraWordsSet.addAll(text.split(" "))
    }
    val newWordsSet = extraWordsSet - wordsToValueMap.keys
    var continuingValue = wordsToValueMap.entries.size
    for (word in newWordsSet) {
        wordsToValueMap[word] = continuingValue
        continuingValue++
    }
    return wordsToValueMap
}

private fun List<Data.Association>.toTextPairs(): List<Pair<String, String>> =
    map { association ->
        Pair(
            first = association.eoProp.text.stripUnneededSymbols(),
            second = association.cProp.text.stripUnneededSymbols()
        )
    }

private fun String.stripUnneededSymbols(): String =
    this
        .replace(".", "")
        .replace("nu ", "") // TODO Check if this is a viable product option
        .replace("ma ", "")

private fun Pair<Values, Values>.getSequences(): Set<Values> {
    val firstSequences = first.getSequences()
    val secondSequences = second.getSequences()
    val sequences = mutableSetOf<Values>()
    for (sequenceFirst in firstSequences) {
        for (sequenceSecond in secondSequences) {
            val newSequence = Values(sequenceFirst.intArray + sequenceSecond.intArray)
            newSequence.sortDescending()
            sequences.add(newSequence)
        }
    }
    return sequences
}

private fun Values.getSequences(): Set<Values> {
    val sequences = mutableSetOf<Values>()
    for (value in intArray) {
        for (sequence in sequences.toList()) {
            val newValues = Values(sequence.intArray + value)
            sequences.add(newValues)
        }
        val newValues = Values(intArrayOf(value))
        sequences.add(newValues)
    }
    return sequences
}

private class Values(val intArray: IntArray) {
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Values) return false
        return intArray.contentEquals(other.intArray)
    }
    override fun hashCode(): Int {
        return intArray.contentHashCode()
    }
    fun sortDescending() {
        intArray.sortDescending()
    }
}
