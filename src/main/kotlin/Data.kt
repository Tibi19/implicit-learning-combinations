sealed class Data {

    data class Proposition(
        val text: String,
        val trait: Trait,
        val facet: Facet,
        // EO propositions can't be negative and should be null by default
        val isNegative: Boolean? = null
    ): Data()
    data class Association(val eoProp: Proposition, val cProp: Proposition): Data()
    data class Phase(val associations: List<Association>): Data()
    data class Group(val acquisition: Phase, val testing: Phase): Data()
    data class Experiment(val groupA: Group, val groupB: Group): Data()

    // A collection of all available propositions that can be combined into other types of data
    data class Pool(
        val propositionsEo: List<Proposition>,
        val propositionsCA: List<Proposition>,
        val propositionsCB: List<Proposition>
    ): Data() {
        val propsEoSet = propositionsEo.toSet()
        val propsCSet = propositionsCA.toSet()
    }
}

enum class Trait { EO, C }

data class Facet(val text: String, val ofTrait: Trait)

data class FacetPair(val eoFacet: String, val cFacet: String)

data class FacetsPool(
    val eoFacets: Set<Facet>,
    val cFacets: Set<Facet>,
    val facetPairs: List<FacetPair>
)