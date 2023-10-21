package util

import io.NEGATIVE_INDICATOR

/**
 * Intended only for C Props
 */
fun Data.Proposition.mirrored(): Data.Proposition {
    if (isNegative == true) {
        return copy(text = text.removePrefix(NEGATIVE_INDICATOR), isNegative = false)
    }
    return copy(text = "$NEGATIVE_INDICATOR${text}", isNegative = true)
}