fun main() {
    fun <T>addCount(map: MutableMap<T, Long>, key: T, count: Long) {
        val current = map.getOrDefault(key, 0)
        map[key] = current + count
    }

    class Polymers(input: List<String>) {
        val template = input.first()
        val rules = input
            .subList(1, input.size)
            .filter { it.isNotEmpty() }
            .associate {
                val (key, value) = it.split("->")
                Pair(key.trim(), value.trim())
            }

        fun process(steps: Int): Map<String, Long> {
            var frequencies = template.toList().windowed(2)
                .map { it.joinToString("")}
                .groupingBy { it }.eachCount().mapValues { it.value.toLong() }

            for (step in 1..steps) {
                val next = mutableMapOf<String, Long>()
                for ((key, count) in frequencies) {
                    val insertion = rules[key]
                    if (insertion != null) {
                        addCount(next, "${key[0]}$insertion", count)
                        addCount(next, "$insertion${key[1]}", count)
                    } else {
                        next[key] = count
                    }
                }
                frequencies = next
            }

            return frequencies
        }

    }

    fun compute(input: List<String>, steps: Int): Long {
        val factory = Polymers(input)
        val frequencies = factory.process(steps)

        val eachCount = frequencies
            .flatMap { (key, count) -> key.map { Pair(it, count) } }
            .groupingBy { it.first }.fold(0L) { acc, el -> acc + el.second }

        val corrected = eachCount.toMutableMap()
        addCount(corrected, factory.template.first(), 1)
        addCount(corrected, factory.template.last(), 1)

        val counts = corrected.values.sorted()
        return (counts.last() - counts.first()) / 2
    }

    fun part1(input: List<String>): Long {
        return compute(input, 10)
    }

    fun part2(input: List<String>): Long {
        return compute(input, 40)
    }

    val testInput = readInput("Day14_test")
    val input = readInput("Day14")

    val part1Test = part1(testInput)
    println(part1Test)
    check(part1Test == 1588L)
    println(part1(input))

    val part2Test = part2(testInput)
    println(part2Test)
    check(part2Test == 2188189693529L)
    println(part2(input))
}