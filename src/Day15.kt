fun main() {
    data class Cell(val x: Int, val y: Int, val level: Int)
    data class CellCost(val cell: Cell, val cost: Int)

    class CavernMap(input: List<String>, tiles: Int = 1) {
        val map: List<List<Int>> = input.map { line ->
            line.map { it.digitToInt() }
        }

        val width = map[0].size
        val height = map.size

        val limitX = width * tiles - 1
        val limitY = height * tiles - 1

        fun cell(x: Int, y: Int): Cell {
            val level = map[y % height][x % width] + (x / width) + (y / height)
            val newLevel = (level - 1) % 9 + 1
            return Cell(x, y, newLevel)
        }

        fun validBounds(x: Int, y: Int) = (x in 0..limitX && y in 0..limitY)

        fun findLowestRisk(): Int {
            val cost = mutableMapOf<Pair<Int, Int>, Int>()

            val queue = mutableSetOf<CellCost>()
            queue.add(CellCost(cell(0, 0), 0))

            val visited = mutableSetOf<Cell>()

            while (queue.isNotEmpty()) {
                val currentCellCost = queue.minByOrNull {it.cost}!!
                queue.remove(currentCellCost)

                val current = currentCellCost.cell
                if (current in visited) {
                    continue
                }

                visited.add(current)
                val x = current.x
                val y = current.y
                cost[Pair(x, y)] = currentCellCost.cost

                if (x == limitX && y == limitY) {
                    break
                }

                val adjacent = listOf(
                    Pair(x + 1, y),
                    Pair(x - 1, y),
                    Pair(x, y + 1),
                    Pair(x, y - 1)
                )
                adjacent
                    .filter { (x, y) -> validBounds(x, y) }
                    .forEach { (x, y) ->
                        val adj = cell(x, y)
                        queue.add(CellCost(adj, adj.level + currentCellCost.cost))
                    }
            }

            return cost.getOrDefault(Pair(limitX, limitY), Int.MAX_VALUE)
        }
    }

    fun part1(input: List<String>): Int {
        val map = CavernMap(input)
        return map.findLowestRisk()
    }

    fun part2(input: List<String>): Int {
        val map = CavernMap(input, 5)
        return map.findLowestRisk()
    }

    val testInput = readInput("Day15_test")
    val input = readInput("Day15")

    val part1Test = part1(testInput)
    println(part1Test)
    check(part1Test == 40)
    println(part1(input))

    val part2Test = part2(testInput)
    println(part2Test)
    check(part2Test == 315)
    println(part2(input))
}