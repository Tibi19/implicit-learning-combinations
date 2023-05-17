package constraint

fun Data.Group.checkAssociationsRepeating(): Boolean {
    acquisition.associations.forEach { associationAcquisition ->
        testing.associations.forEach { associationTesting ->
            val isEoPropSame = associationAcquisition.eoProp.text == associationTesting.eoProp.text
            val isCPropSame = associationAcquisition.cProp.text == associationTesting.cProp.text
            if (isEoPropSame && isCPropSame) return false
        }
    }
    return true
}