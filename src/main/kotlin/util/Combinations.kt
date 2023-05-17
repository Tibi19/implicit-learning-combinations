package util

import java.math.BigInteger

fun comb2(ofN: Int): Int {
    val formulaResult = fact(ofN) / (BigInteger.valueOf(2) * fact(ofN - 2))
    return formulaResult.toInt()
}

private fun fact(n: Int): BigInteger {
    var result = BigInteger.valueOf(1)
    for (i in 1..n) {
        result *= i.toBigInteger()
    }
    return result
}