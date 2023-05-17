package io

import Data
import Facet
import Trait
import java.io.File

const val CSV_DELIMITER = ','
const val ROW_EO = 0
const val ROW_C_A = 1
const val ROW_C_B = 2
const val ROW_FACET_EO = 3
const val ROW_FACET_C_A = 4
const val ROW_FACET_C_B = 5
const val NEGATIVE_INDICATOR = "nu "

fun readPool(path: String): Data.Pool {
    val lines = File(path).readLines()
    val propositionsEo = mutableListOf<Data.Proposition>()
    val propositionsCA = mutableListOf<Data.Proposition>()
    val propositionsCB = mutableListOf<Data.Proposition>()

    lines.forEachIndexed { index, line ->
        if (index == 0) return@forEachIndexed

        val cells = line.split(CSV_DELIMITER)
        val eoProp = readProposition(cells, ROW_EO, ROW_FACET_EO, Trait.EO, false)
        val cPropA = readProposition(cells, ROW_C_A, ROW_FACET_C_A, Trait.C, true)
        val cPropB = readProposition(cells, ROW_C_B, ROW_FACET_C_B, Trait.C, true)
        propositionsEo.add(eoProp)
        propositionsCA.add(cPropA)
        propositionsCB.add(cPropB)
    }

    return Data.Pool(propositionsEo, propositionsCA, propositionsCB)
}

private fun readProposition(
    cells: List<String>,
    textRow: Int,
    facetRow: Int,
    trait: Trait,
    canHaveNegation: Boolean
): Data.Proposition =
    Data.Proposition(
        text = cells[textRow],
        trait = trait,
        facet = Facet(text = cells[facetRow], ofTrait = trait),
        isNegative = if (canHaveNegation) cells[textRow].contains(NEGATIVE_INDICATOR) else null
    )