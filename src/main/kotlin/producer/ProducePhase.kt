package producer

import FacetPair

const val ASSOCIATIONS_SIZE = 72

fun produceRandomPhase(pool: Data.Pool): Data.Phase {
    var randomPhase: Data.Phase? = null
    while (randomPhase == null) {
        try {
            randomPhase = tryProducePhase(pool)
        } catch (_: NoSuchElementException) {}
    }
    return randomPhase
}

private fun tryProducePhase(pool: Data.Pool): Data.Phase {
    val eoProps = pool.propositionsEo.toSet()
    val (cPropsNegative, cPropsPositive) = pool.propositionsCA.partition { it.isNegative == true }
    val associations = mutableListOf<Data.Association>()
    val addedEoProps = mutableListOf<Data.Proposition>()
    val addedCProps = mutableListOf<Data.Proposition>()
    val addedFacetPairs = mutableListOf<FacetPair>()

    for (i in 0 until ASSOCIATIONS_SIZE) {
        val isPositiveAssociation = i % 2 == 0

        val eoProp = if (isPositiveAssociation) {
            val remainingEoProps = eoProps - addedEoProps.toSet()
            remainingEoProps.random()
        } else {
            addedEoProps.last()
        }

        val cProps = if (isPositiveAssociation) cPropsPositive else cPropsNegative
        val remainingCProps = cProps - addedCProps.toSet()
        val possibleCProps = remainingCProps.filter { it.isNewFacetPair(eoProp, addedFacetPairs) }
        val cProp = possibleCProps.random()

        val newAssociation = Data.Association(eoProp, cProp)
        associations.add(newAssociation)

        addedEoProps.add(eoProp)
        addedCProps.add(cProp)
        addedFacetPairs.add(newAssociation.getFacetPair())
    }

    return Data.Phase(associations)
}

private fun Data.Proposition.isNewFacetPair(
    matchingEoProp: Data.Proposition,
    existingFacetPairs: List<FacetPair>
): Boolean {
    val prospectiveFacetPair = FacetPair(matchingEoProp.facet.text, this.facet.text)
    return !existingFacetPairs.contains(prospectiveFacetPair)
}

private fun Data.Association.getFacetPair() =
    FacetPair(
        eoFacet = eoProp.facet.text,
        cFacet = cProp.facet.text
    )