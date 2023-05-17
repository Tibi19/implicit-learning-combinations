package producer

fun produceParallelProposition(cPropOfA: Data.Proposition): Data.Proposition {
    if (cPropOfA.isNegative == true) {
        val newText = cPropOfA.text.removeRange(0..2)
        return Data.Proposition(
            text = newText,
            trait = cPropOfA.trait,
            facet = cPropOfA.facet,
            isNegative = false
        )
    }

    val newText = "nu ${cPropOfA.text}"
    return Data.Proposition(
        text = newText,
        trait = cPropOfA.trait,
        facet = cPropOfA.facet,
        isNegative = true
    )
}

fun produceParallelPropositions(cPropsOfA: List<Data.Proposition>): List<Data.Proposition> =
    cPropsOfA.map { proposition -> produceParallelProposition(proposition) }

fun produceParallelAssociation(associationOfA: Data.Association): Data.Association {
    val cPropOfB = produceParallelProposition(associationOfA.cProp)
    return Data.Association(
        eoProp = associationOfA.eoProp,
        cProp = cPropOfB
    )
}

fun produceParallelPhase(phaseOfA: Data.Phase): Data.Phase =
    Data.Phase(
        associations = phaseOfA
            .associations
            .map { association -> produceParallelAssociation(association) }
    )

fun produceParallelGroup(groupA: Data.Group): Data.Group =
    Data.Group(
        acquisition = produceParallelPhase(groupA.acquisition),
        testing = produceParallelPhase(groupA.testing)
    )

fun produceExperimentFromGroupA(groupA: Data.Group): Data.Experiment =
    Data.Experiment(
        groupA = groupA,
        groupB = produceParallelGroup(groupA)
    )