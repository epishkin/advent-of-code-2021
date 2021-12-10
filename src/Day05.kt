fun main() {
    data class Point(val x: Int, val y: Int)
    data class Line(val start: Point, val end: Point) {
        fun isStraight() =
            start.x == end.x || start.y == end.y
    }

    fun readPoint(text: String): Point {
        val (x, y) = text.split(",").map { it.trim().toInt() }
        return Point(x, y)
    }

    fun readLines(input: List<String>) = input
        .map { row ->
            val (start, end) = row.split("->").map { readPoint(it) }
            Line(start, end)
        }

    fun dotsFrom(d1: Int, d2: Int): List<Int> {
        val ds = if (d1 <= d2) (d1..d2) else (d1 downTo d2)
        return ds.toList()
    }

    fun ventsScore(lines: List<Line>): Int {
        return lines
            .flatMap { line ->
                val xs = dotsFrom(line.start.x, line.end.x)
                val ys = dotsFrom(line.start.y, line.end.y)
                if (line.isStraight())
                    xs.flatMap { x -> ys.map { y -> Point(x, y) } }
                else
                    xs.zip(ys).map { (x, y) -> Point(x, y) }
            }
            .groupingBy(keySelector = { it }).eachCount()
            .count { (_, count) -> (count > 1) }
    }

    fun part1(input: List<String>): Int {
        return ventsScore(
            readLines(input).filter { it.isStraight() }
        )
    }

    fun part2(input: List<String>): Int {
        return ventsScore(readLines(input))
    }

    val testInput = readInput("Day05_test")
    val input = readInput("Day05")

    val part1Test = part1(testInput)
    println(part1Test)
    check(part1Test == 5)
    println(part1(input))

    val part2Test = part2(testInput)
    println(part2Test)
    check(part2Test == 12)
    println(part2(input))
}