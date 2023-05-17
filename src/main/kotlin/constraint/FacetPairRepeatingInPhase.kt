package constraint

import FacetPair
import FacetsPool

const val ALLOWED_FACET_PAIR_OCCURENCES = 1

fun Data.Phase.checkFacetPairRepeating(facetsPool: FacetsPool): Boolean {
    val mapOfFacetPairToOccurences = mutableMapOf<FacetPair, Int>()
    facetsPool.facetPairs.forEach { facetPair -> mapOfFacetPairToOccurences[facetPair] = 0 }

    associations.forEach { association ->
        val facetPair = FacetPair(association.eoProp.facet.text, association.cProp.facet.text)
        mapOfFacetPairToOccurences[facetPair] = mapOfFacetPairToOccurences[facetPair]?.inc() ?: 0
    }

    mapOfFacetPairToOccurences.forEach { (facetPair, occurences) ->
        if (occurences != ALLOWED_FACET_PAIR_OCCURENCES) {
            println("wrong occurences: $occurences for facet pair $facetPair")
            return false
        }
    }

    return true
}