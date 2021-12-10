import kotlin.math.abs

fun main() {

    fun readCrabs(input: List<String>): List<Int> {
        return input
            .flatMap { row ->
                row.split(",")
                    .map { it.trim().toInt() }
            }
    }

    fun part1(input: List<String>): Int {
        val crabs = readCrabs(input)
        val median = crabs.sorted()[crabs.size/2]
        return crabs.sumOf { abs(it - median) }
    }

    fun part2(input: List<String>): Int {
        val crabs = readCrabs(input)
        val first = crabs.minOf{v -> v}
        val last = crabs.maxOf { v -> v }
        return (first..last).minOf { position ->
            crabs.sumOf {
                val dist = abs(it - position)
                (dist + 1) * dist / 2
            }
        }
    }

    val testInput = readInput("Day07_test")
    val input = readInput("Day07")

    val part1Test = part1(testInput)
    println(part1Test)
    check(part1Test == 37)
    println(part1(input))

    val part2Test = part2(testInput)
    println(part2Test)
    check(part2Test == 168)
    println(part2(input))
}