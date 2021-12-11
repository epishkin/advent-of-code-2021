fun main() {
    val closingExpected = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>'
    )
    val errorPoints = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )
    val scorePoints = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )
    val closingCommands = closingExpected.values

    fun part1(input: List<String>): Int {
        fun scoreLine(line: String): Int {
            var score = 0

            fun incScore(cmd: Char) {
                score += errorPoints.getValue(cmd)
            }

            val commands = mutableListOf<Char>()
            for (cmd in line) {
                if (commands.isEmpty() && (cmd in closingCommands)) {
                    incScore(cmd)
                } else if (cmd !in closingCommands) {
                    commands.add(cmd)
                } else {
                    val lastCmd = commands[commands.size - 1]
                    val expected = closingExpected[lastCmd]
                    if (cmd != expected) {
//                        println("Error in line: $line - Expected $expected, but found $cmd instead.")
                        incScore(cmd)
                        break
                    } else {
                        commands.removeLast()
                    }
                }
            }

            return score
        }
        
        return input.sumOf { scoreLine(it) }
    }

    fun part2(input: List<String>): Long {
        fun scoreLine(line: String): Long {
            var score: Long = 0

            fun incScore(cmd: Char) {
                score *= 5
                score += scorePoints.getValue(cmd)
            }

            val commands = mutableListOf<Char>()
            for (cmd in line) {
                if (commands.isEmpty() && (cmd in closingCommands)) {
                    //do nothing
                } else if (cmd !in closingCommands) {
                    commands.add(cmd)
                } else {
                    val lastCmd = commands[commands.size - 1]
                    val expected = closingExpected[lastCmd]
                    if (cmd != expected) {
                        return 0 //discard corrupted line
                    } else {
                        commands.removeLast()
                    }
                }
            }

            //complete if needed
            val completion = mutableListOf<Char>()
            for (cmd in commands.reversed()) {
                val expected = closingExpected.getValue(cmd)
                completion.add(expected)
                incScore(expected)
            }
//            println("$line - Complete by adding ${completion.joinToString("")}.")

            return score
        }

        val scores = input
            .map { scoreLine(it) }
            .filter { it > 0 }
            .sorted()
        return scores[scores.size / 2]
    }

    val testInput = readInput("Day10_test")
    val input = readInput("Day10")

    val part1Test = part1(testInput)
    println(part1Test)
    check(part1Test == 26397)
    println(part1(input))

    val part2Test = part2(testInput)
    println(part2Test)
    check(part2Test == 288957L)
    println(part2(input))
}