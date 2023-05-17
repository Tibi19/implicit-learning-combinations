package io

enum class GroupType(val title: String) {
    A("GRUPA A"),
    B("GRUPA B")
}

enum class PhaseType(val title: String) {
    ACQUISITION("ACHIZITIE"),
    TESTING("TESTARE")
}

fun StringBuilder.appendGroup(group: Data.Group, type: GroupType? = null) {
    type?.let {
        appendLine(it.title)
    }
    appendPhase(group.acquisition, PhaseType.ACQUISITION)
    appendPhase(group.testing, PhaseType.TESTING)
    appendLine()
}

fun StringBuilder.appendPhase(phase: Data.Phase, type: PhaseType? = null) {
    type?.let {
        appendLine(it.title)
    }
    appendLine("eo_proposition,c_proposition,eo_facet,c_facet")
    phase.associations.forEach { association ->
        appendAssociation(association)
    }
}

fun StringBuilder.appendAssociation(association: Data.Association) {
    // TODO replace '.' with ','
    appendCsv(association.eoProp.text)
    appendCsv(association.cProp.text)
    appendCsv(association.eoProp.facet.text)
    appendLine(association.cProp.facet.text)
}

fun StringBuilder.appendCsv(cell: String) {
    append("$cell,")
}