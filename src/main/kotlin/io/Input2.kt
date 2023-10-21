package io

import util.mirrored
import java.io.File

const val COL_PROP = 0
const val COL_TRAIT = 1
const val COL_FACET = 2
const val C_TRAIT_TEXT = "Constiinciozitate"

/**
 * Pool2 is different from Pool1. Pool1 represents an entire selection of propositions
 * as they would be used in the end experiment, but not respecting all the constraints.
 * (It is the goal of this program to produce a selection of propositions that do respect the constraints)
 *
 * Pool2, on the other hand, simply holds all the individual propositions needed for the experiment,
 * without negations, repetitions, or an experiment-like structure.
 *
 * This method produces a Data.Pool from Pool2 that mimics the structure of a Data.Pool from Pool1.
 * This is because the program has been built so far based on that structure,
 * with E/O propositions repeating, and C propositions existing in both positive and negative forms.
 */
fun readPool2(path: String): Data.Pool {
    val lines = File(path).readLines()
    val propositionsEo = mutableListOf<Data.Proposition>()
    val propositionsC = mutableListOf<Data.Proposition>()

    lines.forEachIndexed { index, line ->
        if (index == 0) return@forEachIndexed

        val cells = line.split(CSV_DELIMITER)
        val traitText = cells[COL_TRAIT]

        if (traitText.contains(C_TRAIT_TEXT)) {
            val cProp = readProposition(cells, COL_PROP, COL_FACET, Trait.C, true)
            propositionsC.add(cProp)
            propositionsC.add(cProp.mirrored())
            return@forEachIndexed
        }

        val eoProp = readProposition(cells, COL_PROP, COL_FACET, Trait.EO, false)
        propositionsEo.add(eoProp)
        propositionsEo.add(eoProp)
    }

    val (propositionsCA, propositionsCB) = produceCPropsLikePool1Structure(propositionsC)

    return Data.Pool(propositionsEo, propositionsCA, propositionsCB)
}

private fun produceCPropsLikePool1Structure(propositionsC: List<Data.Proposition>): Pair<List<Data.Proposition>, List<Data.Proposition>> {
    val propositionsCA = mutableListOf<Data.Proposition>()
    val propositionsCB = mutableListOf<Data.Proposition>()

    val (cPropsNegative, cPropsPositive) = propositionsC.partition { it.isNegative == true }
    val (cPropsNegativeShuffled, cPropsPositiveShuffled) = Pair(cPropsNegative.shuffled(), cPropsPositive.shuffled())

    for(i in cPropsPositiveShuffled.indices) {
        val positiveProp = cPropsPositiveShuffled[i]
        val negativeProp = cPropsNegativeShuffled[i]
        propositionsCA.add(positiveProp)
        propositionsCA.add(negativeProp)
        propositionsCB.add(positiveProp.mirrored())
        propositionsCB.add(negativeProp.mirrored())
    }

    return Pair(propositionsCA, propositionsCB)
}
