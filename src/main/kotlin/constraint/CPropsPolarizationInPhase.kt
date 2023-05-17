package constraint

// Polarization refers to a conscientiousness proposition being positive or negative
// We describe polarization as an integer to check the balance of multiple c propositions
// We want the polarization of 2 associations having the same extraversion/openness proposition to be equally balanced
// Therefore, the allowed polarization is 0
const val ALLOWED_POLARIZATION = 0

/**
 * This check assumes the phase has already been checked with "checkEoPropsRepeating"
 */
fun Data.Phase.checkCPropsPolarization(pool: Data.Pool): Boolean {
    val mapOfEoPropToPolarization = mutableMapOf<Data.Proposition, Int>()
    pool.propsEoSet.forEach { proposition -> mapOfEoPropToPolarization[proposition] = 0 }

    associations.forEach { association ->
        val eoProp = association.eoProp
        val cPropPolarization = if (association.cProp.isNegative == true) -1 else 1
        mapOfEoPropToPolarization[eoProp] = mapOfEoPropToPolarization[eoProp]?.plus(cPropPolarization) ?: -1
    }

    mapOfEoPropToPolarization.forEach { (_, polarization) ->
        if (polarization != ALLOWED_POLARIZATION) return false
    }

    return true
}