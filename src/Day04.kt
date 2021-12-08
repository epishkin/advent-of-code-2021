fun main() {
    fun part1(input: List<String>): Int {
        fun findWinningBoard(drawNumbers: List<Int>, boards: List<Board>): WinningBoard {
            var currentBoards = boards
            for (drawNumber in drawNumbers) {
                currentBoards = currentBoards.map { board ->
                    var fullRow = false
                    val newRows = mutableListOf<List<Int>>()
                    for (row in board.numbers) {
                        val newRow = row.filter { it != drawNumber }
                        newRows.add(newRow)
                        if (newRow.isEmpty())
                            fullRow = true
                    }

                    val newBoard = Board(newRows.toList())
                    if (fullRow) {
                        return WinningBoard(drawNumber, newBoard)
                    }
                    newBoard
                }
            }
            error("Can't find a winning board")
        }

        val drawNumbers = input.first().split(",").map { it.toInt() }
        val boards = readBoards(input.subList(1, input.size))

        return findWinningBoard(drawNumbers, boards).score()
    }

    fun part2(input: List<String>): Int {
        fun findWinningBoards(drawNumbers: List<Int>, boards: List<Board>): List<WinningBoard> {
            val winningBoards = mutableListOf<WinningBoard>()
            var currentBoards = boards
            for (drawNumber in drawNumbers) {
                val newBoards = mutableListOf<Board>()
                for (board in currentBoards) {
                    var fullRow = false
                    val newRows = mutableListOf<List<Int>>()
                    for (row in board.numbers) {
                        val newRow = row.filter { it != drawNumber }
                        newRows.add(newRow)
                        if (newRow.isEmpty())
                            fullRow = true
                    }

                    val newBoard = Board(newRows.toList())
                    if (fullRow) {
                        winningBoards.add(WinningBoard(drawNumber, newBoard))
                    } else {
                        newBoards.add(newBoard)
                    }
                }
                currentBoards = newBoards
            }
            return winningBoards.toList()
        }

        val drawNumbers = input.first().split(",").map { it.toInt() }
        val boards = readBoards(input.subList(1, input.size))

        return findWinningBoards(drawNumbers, boards).last().score()
    }

    val testInput = readInput("Day04_test")
    val input = readInput("Day04")

    val part1Test = part1(testInput)
    println(part1Test)
    check(part1Test == 4512)
    println(part1(input))

    val part2Test = part2(testInput)
    println(part2Test)
    check(part2Test == 1924)
    println(part2(input))
}

private fun readBoards(lines: List<String>): List<Board> {
    val chunks = lines
        .filter { it.isNotEmpty() }
        .chunked(5)
    val boards = chunks.map { chunk ->
        val rows = chunk
            .map { it.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } }
        val columns = rows[0].indices.map { i ->
            rows.map { it[i] }
        }
        Board(rows + columns)
    }
    return boards
}

private data class Board(var numbers: List<List<Int>>) {
    fun sum(): Int = numbers.map { it.sum() }.sum() / 2
}

private data class WinningBoard(val drawNumber: Int, val board: Board) {
    fun score(): Int = drawNumber * board.sum()
}