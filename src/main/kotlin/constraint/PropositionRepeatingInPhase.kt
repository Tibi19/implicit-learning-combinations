package constraint

const val ALLOWED_EO_PROP_OCCURENCES = 2
const val ALLOWED_C_PROP_OCCURENCES = 1

fun Data.Phase.checkEoPropsRepeating(pool: Data.Pool): Boolean {
    val eoProps = associations.map { it.eoProp }
    return checkPropsRepeating(
        props = eoProps,
        propsSet = pool.propsEoSet,
        allowedOccurences = ALLOWED_EO_PROP_OCCURENCES
    )
}

fun Data.Phase.checkCPropsRepeating(pool: Data.Pool): Boolean {
    val cProps = associations.map { it.cProp }
    return checkPropsRepeating(
        props = cProps,
        propsSet = pool.propsCSet,
        allowedOccurences = ALLOWED_C_PROP_OCCURENCES
    )
}

private fun checkPropsRepeating(
    props: List<Data.Proposition>,
    propsSet: Set<Data.Proposition>,
    allowedOccurences: Int
): Boolean {
    val mapOfPropTextToOccurences = mutableMapOf<String, Int>()
    propsSet.forEach { proposition -> mapOfPropTextToOccurences[proposition.text] = 0 }

    props.forEach { proposition ->
        mapOfPropTextToOccurences[proposition.text] = mapOfPropTextToOccurences[proposition.text]?.inc() ?: 0
    }

    mapOfPropTextToOccurences.forEach { (prop, occurences) ->
        if (occurences != allowedOccurences) return false
    }

    return true
}