fun main() {
    fun part1(input: List<String>): Int {
        var x = 0
        var y = 0
        val commands = input.map { it.split(" ") }
        for ((command, valueStr) in commands) {
            val value = valueStr.toInt()
            when (command) {
                "up" -> y -= value
                "down" -> y += value
                "forward" -> x += value
            }
        }
        return x * y
    }

    data class Command(val direction: String, val amount: Int)

    fun part2(input: List<String>): Int {
        var aim = 0
        var x = 0
        var y = 0
        val commands = input
            .map { it.split(" ") }
            .map { Command(it[0], it[1].toInt()) }

        for ((direction, amount) in commands) {
            when (direction) {
                "up" -> aim -= amount
                "down" -> aim += amount
                "forward" -> {
                    x += amount
                    y += amount * aim
                }
            }
        }
        return x * y
    }


    val testInput = readInput("Day02_test")
    val input = readInput("Day02")

    check(part1(testInput) == 150)
    println(part1(input))

    check(part2(testInput) == 900)
    println(part2(input))
}