package producer

import Facet
import FacetPair
import FacetsPool

fun produceFacets(pool: Data.Pool): FacetsPool {
    val eoFacets = mutableSetOf<Facet>()
    val cFacets = mutableSetOf<Facet>()
    pool.propositionsEo.forEach { proposition ->
        eoFacets.add(proposition.facet)
    }
    pool.propositionsCA.forEach { proposition ->
        cFacets.add(proposition.facet)
    }
    return FacetsPool(
        eoFacets,
        cFacets,
        produceFacetPairs(eoFacets, cFacets)
    )
}

private fun produceFacetPairs(eoFacets: Set<Facet>, cFacets: Set<Facet>): List<FacetPair> {
    val facetPairs = mutableListOf<FacetPair>()
    eoFacets.forEach { eoFacet ->
        cFacets.forEach { cFacet ->
            facetPairs.add(FacetPair(eoFacet.text, cFacet.text))
        }
    }
    return facetPairs
}