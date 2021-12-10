fun main() {

    fun readFishes(input: List<String>): Map<Int, Long> {
        return input
            .flatMap { row ->
                row.split(",")
                    .map { it.trim().toInt() }
            }
            .groupingBy(keySelector = { it })
            .eachCount()
            .mapValues { (_, value) -> value.toLong() }
    }

    fun List<Pair<Int, Long>>.toMapMerge(): Map<Int, Long> {
        return this
            .groupBy(keySelector = { (cycle, _) -> cycle })
            .map { (k, v) -> Pair(k, v.sumOf { it.second }) }
            .toMap()
    }

    fun simulateCount(initialPopulation: Map<Int, Long>, cycles: Int): Long {
        var fishes = initialPopulation
        for (day in 1..cycles) {
            fishes = fishes
                .flatMap { (cycle, count) ->
                    if (cycle == 0) listOf(Pair(6, count), Pair(8, count))
                    else listOf(Pair(cycle - 1, count))
                }
                .toMapMerge()
        }
        return fishes.map { it.value }.sum()
    }

    fun part1(input: List<String>): Long {
        val initialPopulation = readFishes(input)
        return simulateCount(initialPopulation, 80)
    }

    fun part2(input: List<String>): Long {
        val initialPopulation = readFishes(input)
        return simulateCount(initialPopulation, 256)
    }

    val testInput = readInput("Day06_test")
    val input = readInput("Day06")

    val part1Test = part1(testInput)
    println(part1Test)
    check(part1Test == 5934L)
    println(part1(input))

    val part2Test = part2(testInput)
    println(part2Test)
    check(part2Test == 26984457539)
    println(part2(input))
}