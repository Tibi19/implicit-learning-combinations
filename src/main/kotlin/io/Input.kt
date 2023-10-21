package io

import Data
import Facet
import Trait
import java.io.File

const val CSV_DELIMITER = ','
const val COL_EO = 0
const val COL_C_A = 1
const val COL_C_B = 2
const val COL_FACET_EO = 3
const val COL_FACET_C_A = 4
const val COL_FACET_C_B = 5
const val NEGATIVE_INDICATOR = "nu "

fun readPool(path: String): Data.Pool {
    val lines = File(path).readLines()
    val propositionsEo = mutableListOf<Data.Proposition>()
    val propositionsCA = mutableListOf<Data.Proposition>()
    val propositionsCB = mutableListOf<Data.Proposition>()

    lines.forEachIndexed { index, line ->
        if (index == 0) return@forEachIndexed

        val cells = line.split(CSV_DELIMITER)
        val eoProp = readProposition(cells, COL_EO, COL_FACET_EO, Trait.EO, false)
        val cPropA = readProposition(cells, COL_C_A, COL_FACET_C_A, Trait.C, true)
        val cPropB = readProposition(cells, COL_C_B, COL_FACET_C_B, Trait.C, true)
        propositionsEo.add(eoProp)
        propositionsCA.add(cPropA)
        propositionsCB.add(cPropB)
    }

    return Data.Pool(propositionsEo, propositionsCA, propositionsCB)
}

fun readProposition(
    cells: List<String>,
    textCol: Int,
    facetCol: Int,
    trait: Trait,
    canHaveNegation: Boolean
): Data.Proposition =
    Data.Proposition(
        text = cells[textCol],
        trait = trait,
        facet = Facet(text = cells[facetCol], ofTrait = trait),
        isNegative = if (canHaveNegation) cells[textCol].contains(NEGATIVE_INDICATOR) else null
    )