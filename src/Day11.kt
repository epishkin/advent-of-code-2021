fun main() {
    class Octopus(val x: Int, val y: Int, var energy: Int, var flashed: Boolean = false)

    class OctopusMap(input: List<String>) {
        var flashes: Int = 0

        val map = input.mapIndexed { y, line ->
            line.mapIndexed { x, c -> Octopus(x, y, c.digitToInt()) }
        }

        val limitX = input[0].length - 1
        val limitY = input.size - 1

        fun octopus(x: Int, y: Int) = map[y][x]

        fun validBounds(x: Int, y: Int) = (x in 0..limitX && y in 0..limitY)

        fun flashIfEnergized(queue: MutableSet<Octopus>, octopus: Octopus) {
            if (octopus.energy > 9 && !octopus.flashed) {
                flashes++
                octopus.flashed = true
                queue.add(octopus)
            }
        }

        fun simulateStep() {
            val queue = mutableSetOf<Octopus>()

            map.flatten().forEach { it.flashed = false }

            //inc energy +1 to every octopus
            for (x in 0..limitX) {
                for (y in 0..limitY) {
                    val current = octopus(x, y)
                    current.energy += 1
                    flashIfEnergized(queue, current)
                }
            }

            //flashing party!
            while (queue.isNotEmpty()) {
                val current = queue.first()
                queue.remove(current)

                val x = current.x
                val y = current.y
                val adjacent = listOf(
                    Pair(x - 1, y - 1), Pair(x, y - 1), Pair(x + 1, y - 1),
                    Pair(x - 1, y), Pair(x + 1, y),
                    Pair(x - 1, y + 1), Pair(x, y + 1), Pair(x + 1, y + 1)
                )
                adjacent
                    .filter { (x, y) -> validBounds(x, y) }
                    .forEach { (x, y) ->
                        val adj = octopus(x, y)
                        adj.energy += 1
                        flashIfEnergized(queue, adj)
                    }
            }

            //if flashed reset energy
            map.flatten().forEach {
                if (it.flashed) {
                    it.energy = 0
                }
            }
        }

        fun syncFlashing(): Boolean {
            return map.flatten().all { it.flashed }
        }

        fun printStep(step: Int) {
            println("--Step: $step------------------")
            map.forEach {
                println(it.joinToString(separator = " ") { o ->
                  val cell = if (o.flashed) "*" else o.energy.toString()
                  cell.padStart(3)
                })
            }
            println("--Flashes: $flashes------------------")
        }
    }

    fun part1(input: List<String>, steps: Int): Int {
        val map = OctopusMap(input)
        for (step in 1..steps) {
            map.simulateStep()
        }
        return map.flashes
    }

    fun part2(input: List<String>): Int {
        val map = OctopusMap(input)
        for (step in 1..9999) {
            map.simulateStep()
            if (map.syncFlashing()) return step
        }

        return -1
    }

    val testInput = readInput("Day11_test")
    val input = readInput("Day11")

    val part1Test10 = part1(testInput, 10)
    println(part1Test10)
    check(part1Test10 == 204)

    val part1Test100 = part1(testInput, 100)
    println(part1Test100)
    check(part1Test100 == 1656)

    println(part1(input, 100))

    val part2Test = part2(testInput)
    println(part2Test)
    check(part2Test == 195)
    println(part2(input))
}