fun main() {
    class Page(val dots: Set<Pair<Int, Int>>, val folds: List<FoldInstruction>) {

        fun executeFold(currentDots: Set<Pair<Int, Int>>, fold: FoldInstruction): Set<Pair<Int, Int>> {
            return currentDots.map { (x, y) ->
                val dimension = if (fold.direction == FoldDirection.X) x else y
                if (dimension > fold.size) {
                    val newDimension = 2 * fold.size - dimension
                    if (fold.direction == FoldDirection.X)
                        Pair(newDimension, y)
                    else
                        Pair(x, newDimension)
                } else {
                    Pair(x, y)
                }
            }.toSet()
        }

        fun executeFolds(): Set<Pair<Int, Int>> {
            var currentDots = dots

            folds.forEach { fold ->
                currentDots = executeFold(currentDots, fold)
            }

            return currentDots
        }
    }

    fun parse(input: List<String>): Page {
        val dots = mutableSetOf<Pair<Int, Int>>()
        val folds = mutableListOf<FoldInstruction>()

        var foldPart = false
        val foldRegex = """fold along ([xy])=(\d+)""".toRegex()
        input.forEach { line ->
            if (line.isBlank()) {
                foldPart = true
            } else {
                if (!foldPart) {
                    val (x, y) = line.split(",").map{it.toInt()}
                    dots.add(Pair(x, y))
                } else {
                    val (_, direction, size) = foldRegex.find(line)!!.groupValues
                    val dir = if (direction == "x") FoldDirection.X else FoldDirection.Y
                    folds.add(FoldInstruction(dir, size.toInt()))
                }
            }
        }

        return Page(dots, folds)
    }

    fun part1(input: List<String>): Int {
        val page = parse(input)
        return page.executeFold(page.dots, page.folds.first()).size
    }

    fun part2(input: List<String>) {
        val dots = parse(input).executeFolds()

        val limitX = dots.maxOf { it.first }
        val limitY = dots.maxOf { it.second }
        println("--- ${limitX+1}x${limitY+1}")

        val dotsMap = dots.associate { (x, y) -> Pair(Pair(x, y), "#") }
        for (y in 0..limitY) {
            println(
                (0..limitX)
                    .joinToString("") { x ->
                        dotsMap.getOrDefault(Pair(x, y), " ")
                    }
            )
        }
    }

    val testInput = readInput("Day13_test")
    val input = readInput("Day13")

    val part1Test = part1(testInput)
    println(part1Test)
    check(part1Test == 17)
    println(part1(input))

    part2(testInput)
    part2(input)
}

private enum class FoldDirection {
    X, Y
}
private data class FoldInstruction(val direction: FoldDirection, val size: Int)