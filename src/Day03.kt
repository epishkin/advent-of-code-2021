fun main() {
    fun part1(input: List<String>): Int {
        val columns = input[0].indices
        val gamma = columns
            .map { input.countBitsIn(it) }
            .joinToString("") { (zeroes, ones) ->
                if (zeroes > ones) "0" else "1"
            }
        val epsilon = gamma.invertBinaryString()
        return gamma.toInt(2) * epsilon.toInt(2)
    }

    fun part2(input: List<String>): Int {
        fun rating(type: RatingType): String {
            val columns = input[0].indices
            var candidates = input
            for (column in columns) {
                val (zeroes, ones) = candidates.countBitsIn(column)
                val mostCommon = if (zeroes > ones) '0' else '1'
                candidates = candidates.filter {
                    when (type) {
                        RatingType.OXY -> it[column] == mostCommon
                        RatingType.CO2 -> it[column] != mostCommon
                    }
                }
                if (candidates.size == 1) break
            }
            return candidates.single()
        }
        return rating(RatingType.OXY).toInt(2) * rating(RatingType.CO2).toInt(2)
    }

    val testInput = readInput("Day03_test")
    val input = readInput("Day03")

    val part1Test = part1(testInput)
    println(part1Test)
    check(part1Test == 198)
    println(part1(input))

    val part2Test = part2(testInput)
    println(part2Test)
    check(part2Test == 230)
    println(part2(input))
}

private fun List<String>.countBitsIn(column: Int): BitCount {
    var zeroes = 0
    var ones = 0
    for (row in this) {
        if (row[column] == '0') zeroes++ else ones++
    }
    return BitCount(zeroes,ones)
}

data class BitCount(val zeroes: Int, val ones: Int)

private enum class RatingType {
    OXY,
    CO2
}

private fun String.invertBinaryString() = this
    .asIterable()
    .joinToString("") { if (it == '0') "1" else "0" }