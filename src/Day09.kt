fun main() {
    data class Location(val x: Int, val y: Int, val height: Int)

    class HeightMap(input: List<String>) {
        val map = input.mapIndexed { y, line ->
            line.mapIndexed { x, c -> Location(x, y, c.digitToInt()) }
        }

        val limitX = input[0].length - 1
        val limitY = input.size - 1

        fun getHeight(x: Int, y: Int) = map[y][x].height
        fun get(x: Int, y: Int) = map[y][x]

        fun isHigherThan(x: Int, y: Int, height: Int) = getHeight(x, y) > height

        fun validBounds(x: Int, y: Int) = (x in 0..limitX && y in 0..limitY)

        fun isLowest(x: Int, y: Int): Boolean {
            val adjacent = listOf(
                Pair(x - 1, y),
                Pair(x + 1, y),
                Pair(x, y - 1),
                Pair(x, y + 1)
            )
            val height = getHeight(x, y)
            return adjacent
                .filter { (x, y) -> validBounds(x, y) }
                .all { (x, y) -> isHigherThan(x, y, height) }
        }

        fun findLowPoints(): List<Location> {
            return IntRange(0, limitX).flatMap { x ->
                IntRange(0, limitY)
                    .filter { y -> isLowest(x, y)  }
                    .map { y-> get(x, y) }
            }
        }

        fun basinMap(): Map<Pair<Int, Int>, Int> {
            val result = mutableMapOf<Pair<Int, Int>, Int>()

            val visited = mutableSetOf<Location>()
            val queue = mutableListOf<Location>()

            findLowPoints().forEachIndexed { idx, low ->
                queue.add(low)
                while (queue.isNotEmpty()) {
                    val loc = queue.removeLast()
                    if (loc in visited) continue

                    visited.add(loc)

                    val x = loc.x
                    val y = loc.y
                    result[Pair(x, y)] = idx

                    val adjacent = listOf(
                        Pair(x - 1, y),
                        Pair(x + 1, y),
                        Pair(x, y - 1),
                        Pair(x, y + 1)
                    )

                    adjacent
                        .filter { (x, y) -> validBounds(x, y) }
                        .forEach { (x, y) ->
                            val adj = get(x, y)
                            if (adj.height != 9) queue.add(adj)
                        }
                }
            }
            return result
        }

        fun printBasinMap(basins: Map<Pair<Int, Int>, Int>) {
            for (y in 0..limitY) {
                for (x in 0..limitX) {
                    val loc = if (Pair(x, y) in basins) basins[Pair(x, y)].toString()
                    else "."
                    print(loc)
                }
                println()
            }
        }
    }

    fun part1(input: List<String>): Int {
        val map = HeightMap(input)
        return map
            .findLowPoints()
            .sumOf { it.height + 1 }
    }

    fun part2(input: List<String>): Int {
        val basinMap = HeightMap(input).basinMap()
        val basinSizes = basinMap.values.groupingBy { it }.eachCount().values
        return basinSizes
            .sorted().reversed().take(3)
            .reduce { acc, i -> acc * i }
    }

    val testInput = readInput("Day09_test")
    val input = readInput("Day09")

    val part1Test = part1(testInput)
    println(part1Test)
    check(part1Test == 15)
    println(part1(input))

    val part2Test = part2(testInput)
    println(part2Test)
    check(part2Test == 1134)
    println(part2(input))
}