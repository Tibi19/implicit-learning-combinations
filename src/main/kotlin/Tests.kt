import constraint.*
import io.*
import producer.*
import util.getMillis
import util.printDurationFor

fun testFacetsPool2() {
    val pool = readPool(POOL_PATH)
    val pool2 = readPool2(POOL2_PATH)
    testFacets(pool)
    println()
    testFacets(pool2)
}

fun testPool2() {
    val pool = readPool(POOL_PATH)
    val facetsPool = produceFacets(pool)

    val pool2 = readPool2(POOL2_PATH)
    val facetsPool2 = produceFacets(pool2)

    println("POOL1 EO Props (${pool.propositionsEo.size}):")
    showEoPropsTexts(pool)
    println("POOL1 CA Props (${pool.propositionsCA.size}):")
    showCAPropsTexts(pool)

    println("POOL2 EO Props (${pool2.propositionsEo.size}):")
    showEoPropsTexts(pool2)
    println("POOL2 CA Props (${pool2.propositionsEo.size}):")
    showCAPropsTexts(pool2)
}

fun testProduceGroups(pool: Data.Pool) {
    val phases = List(277) {
        produceRandomPhase(pool)
    }
    val groups = produceGroupsFromPhasesChunk(phases)
    println("Found ${groups.size} groups")
}

fun testWritePhase(pool: Data.Pool) {
    val phase = produceRandomPhase(pool)
    val passed = Checker.checkPhase(phase)
    println(passed)
    writePhase(phase)
}

fun testWriteGroup(pool: Data.Pool) {
    val phaseA = produceRandomPhase(pool)
    val phaseT = produceRandomPhase(pool)
    val group = Data.Group(phaseA, phaseT)
    val passedAll = Checker.checkGroup(group)
    val passedLight = Checker.checkGroupLight(group)
    println("light: $passedLight \nall: $passedAll")
    writeGroup(group)
}

fun testWriteExperiment(pool: Data.Pool) {
    val phaseA = produceRandomPhase(pool)
    val phaseT = produceRandomPhase(pool)
    val groupA = Data.Group(phaseA, phaseT)
    val groupB = produceParallelGroup(groupA)
    val experiment = Data.Experiment(groupA, groupB)
    writeExperiment(experiment)
}

fun testWriteLogger(pool: Data.Pool) {
    val phases = List(50) {
        produceRandomPhase(pool)
    }
    val groups = produceGroupsFromPhasesChunk(phases)
    println("Found ${groups.size} groups")
    val start = getMillis()
    groups.take(50).forEach { group ->
        val passedGroupA = Checker.checkGroupHeavyWithLogger(group)
        if (!passedGroupA) return@forEach
        val groupB = produceParallelGroup(group)
        Checker.checkGroupHeavyWithLogger(groupB, true)
    }
    val end = getMillis()
    printDurationFor(start, end, "Checked groups")
    writeLog()
}

fun testRandomPhasesChunk(pool: Data.Pool) {
    val start = getMillis()
    val phases = mutableListOf<Data.Phase>()
    for (i in (0 until 100_000)) {
        phases.add(produceRandomPhase(pool))
    }
    val end = getMillis()

    val uniquePhases = phases.toSet()
    printDurationFor(start, end, "Produced phases chunk")
    println("Of ${phases.size} phases, unique are ${uniquePhases.size}")
}

fun testRandomPhaseTrying(pool: Data.Pool) {
    val phase = produceRandomPhase(pool)
    val phaseCheckPassed = Checker.checkPhase(phase)
    println(phaseCheckPassed)
}

fun testPhaseCheck(pool: Data.Pool) {
    val associations: List<Data.Association> = pool.propositionsEo.mapIndexed { i, proposition ->
        Data.Association(eoProp = proposition, cProp = pool.propositionsCA[i])
    }
    val phase = Data.Phase(associations)
    println(Checker.checkPhase(phase))
}

fun printAssociation(association: Data.Association) {
    println("${association.eoProp.text} -- ${association.cProp.text} -- negative:${association.cProp.isNegative} -- ${association.eoProp.facet.text} -- ${association.cProp.facet.text}")
}

fun testGroupChecking(pool: Data.Pool) {
    val phaseA = Data.Phase(
        associations = pool
            .propositionsEo
            .mapIndexed { index, proposition ->
                Data.Association(
                    eoProp = proposition,
                    cProp = pool.propositionsCA[index]
                )
            }
    )
    val phaseT = Data.Phase(
        associations = pool
            .propositionsEo
            .mapIndexed { index, proposition ->
                Data.Association(
                    eoProp = proposition,
                    cProp = pool.propositionsCB[index]
                )
            }
    )
    val group = Data.Group(phaseA, phaseT)
    val checkGroupPassed = Checker.checkGroup(group)
    println("Check group passed? $checkGroupPassed")
}

fun testPhaseChecking(pool: Data.Pool) {
    val phaseA = Data.Phase(
        associations = pool
            .propositionsEo
            .mapIndexed { index, proposition ->
                Data.Association(
                    eoProp = proposition,
                    cProp = pool.propositionsCA[index]
                )
            }
    )
    val phaseCheckPassed = Checker.checkPhase(phaseA)
    println("Phase check passed? $phaseCheckPassed")
}

fun testExperimentChecking(pool: Data.Pool) {
    val phaseA = Data.Phase(
        associations = pool
            .propositionsEo
            .mapIndexed { index, proposition ->
                Data.Association(
                    eoProp = proposition,
                    cProp = pool.propositionsCA[index]
                )
            }
    )
    val phaseT = Data.Phase(
        associations = pool
            .propositionsEo
            .mapIndexed { index, proposition ->
                Data.Association(
                    eoProp = proposition,
                    cProp = pool.propositionsCB[index]
                )
            }
    )
    val group = Data.Group(phaseA, phaseT)
    val experiment = Data.Experiment(group, group)
    val checkExperimentPassed = Checker.checkExperiment(experiment)
    println("Experiment passed? $checkExperimentPassed")
}

fun testWordSequencesRepeating(pool: Data.Pool) {
    val phaseA = Data.Phase(
        associations = pool
            .propositionsEo
            .mapIndexed { index, proposition ->
                Data.Association(
                    eoProp = proposition,
                    cProp = pool.propositionsCA[index]
                )
            }
    )
    val phaseT = Data.Phase(
        associations = pool
            .propositionsEo
            .mapIndexed { index, proposition ->
                Data.Association(
                    eoProp = proposition,
                    cProp = pool.propositionsCB[index]
                )
            }
    )
    val group = Data.Group(phaseA, phaseT)

    val start = getMillis()
    val checkWordSequencesRepeatingPassed = group.checkWordSequencesRepeating()
    val end = getMillis()
    printDurationFor(start, end, "Word sequences repeating check")
    println("Word sequences repeating passed? $checkWordSequencesRepeatingPassed")
}

fun testPolarization(pool: Data.Pool) {
    val phaseA = Data.Phase(
        associations = pool
            .propositionsEo
            .mapIndexed { index, proposition ->
                Data.Association(
                    eoProp = proposition,
                    cProp = pool.propositionsCA[index]
                )
            }
    )
    val phaseT = Data.Phase(
        associations = pool
            .propositionsEo
            .mapIndexed { index, proposition ->
                Data.Association(
                    eoProp = proposition,
                    cProp = pool.propositionsCB[index]
                )
            }
    )
    val checkPhaseAPassed = phaseA.checkCPropsPolarization(pool)
    println("C props polarization check in phase A: $checkPhaseAPassed")
    val checkPhaseTPassed = phaseT.checkCPropsPolarization(pool)
    println("C props polarization check in phase T: $checkPhaseTPassed")
}

fun testFacetsRepeating(pool: Data.Pool) {
    val facetsPool = produceFacets(pool)
    val phaseA = Data.Phase(
        associations = pool
            .propositionsEo
            .mapIndexed { index, proposition ->
                Data.Association(
                    eoProp = proposition,
                    cProp = pool.propositionsCA[index]
                )
            }
    )
    val checkFacetsPassed = phaseA.checkFacetPairRepeating(facetsPool)
    println("Facets repeating passed? $checkFacetsPassed")
}

fun testAssociationsRepeatingConstraint(pool: Data.Pool) {
    val phaseA = Data.Phase(
        associations = pool
            .propositionsEo
            .mapIndexed { index, proposition ->
                Data.Association(
                    eoProp = proposition,
                    cProp = pool.propositionsCA[index]
                )
            }
    )
    val phaseT = Data.Phase(
        associations = pool
            .propositionsEo
            .mapIndexed { index, proposition ->
                Data.Association(
                    eoProp = proposition,
                    cProp = pool.propositionsCB[index]
                )
            }
    )
    val group = Data.Group(phaseA, phaseT)
    val checkAssociationsPassed = group.checkAssociationsRepeating()
    println("Assosications repeating passed? $checkAssociationsPassed")
}

fun testPropsRepeatingConstraint(pool: Data.Pool) {
    val phase = Data.Phase(
        associations = pool
            .propositionsEo
            .mapIndexed { index, proposition ->
                Data.Association(
                    eoProp = proposition,
                    cProp = pool.propositionsCB[index]
                )
            }
    )
    val checkEoPassed = phase.checkEoPropsRepeating(pool)
    println("Eo repeating check: $checkEoPassed")
    val checkCPassed = phase.checkCPropsRepeating(pool)
    println("C repeating check: $checkCPassed")
}

fun showEoProps(pool: Data.Pool) {
    val allEoProps = pool.propositionsEo.joinToString("\n")
    println(allEoProps)
    println()
}

fun showEoPropsTexts(pool: Data.Pool) {
    val allEoPropsTexts = pool.propositionsEo.joinToString("\n") { it.text }
    println(allEoPropsTexts)
    println()
}

fun showCAProps(pool: Data.Pool) {
    val allCAProps = pool.propositionsCA.joinToString("\n")
    println(allCAProps)
    println()
}

fun showCAPropsTexts(pool: Data.Pool) {
    val allCAPropsTexts = pool.propositionsCA.joinToString("\n") { it.text }
    println(allCAPropsTexts)
    println()
}

fun testFacets(pool: Data.Pool) {
    val facetsPool = produceFacets(pool)
//    println("eo facets:\n${facetsPool.eoFacets.joinToString("\n") { it.facet } }")
//    println("c facets:\n${facetsPool.cFacets.joinToString("\n") {it.facet} }")
    println("facet pairs of size ${facetsPool.facetPairs.size}:\n${facetsPool.facetPairs.joinToString("\n")}")
}

fun testParallel(pool: Data.Pool) {
    val phase = Data.Phase(
        associations = pool
            .propositionsEo
            .mapIndexed { index, proposition ->
                Data.Association(
                    eoProp = proposition,
                    cProp = pool.propositionsCA[index]
                )
            }
    )
    val parallelPhase = produceParallelPhase(phase)
    val proposition = parallelPhase.associations[35].eoProp
    println(proposition.text)
    println(proposition.trait)
    println(proposition.facet)
    println(proposition.isNegative)
}