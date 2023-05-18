package producer

import constraint.checkAssociationsRepeating

fun produceGroupsFromPhasesChunk(phases: List<Data.Phase>): List<Data.Group> {
    val groups = mutableListOf<Data.Group>()
    for ((i, phase) in phases.withIndex()) {
        println("Producing groups for phase $i of ${phases.size}")
        val matchingPhases = phases.filter {
            val possibleGroup = Data.Group(acquisition = phase, testing = it)
            possibleGroup.checkAssociationsRepeating()
        }
        for (matchingPhase in matchingPhases) {
            val newGroup = Data.Group(acquisition = phase, testing = matchingPhase)
            groups.add(newGroup)
        }
    }
    return groups
}