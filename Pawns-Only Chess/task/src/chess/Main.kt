package chess

import kotlin.math.abs

const val EMPTY_CELL: String = " "
const val WHITE_CELL: String = "W"
const val BLACK_CELL: String = "B"
const val INVALID_MESSAGE: String = "Invalid Input"

fun getGrid(): MutableList<MutableList<String>> {
    val grid: MutableList<MutableList<String>> = mutableListOf()

    repeat(8) {
        val row: MutableList<String> = mutableListOf()

        val cell: String = when (it) {
            1 -> BLACK_CELL
            6 -> WHITE_CELL
            else -> EMPTY_CELL
        }

        repeat(8) {
            row.add(cell)
        }

        grid.add(row)
    }

    return grid
}

fun updateGrid(grid: MutableList<MutableList<String>>, player: String, move: String): MutableList<MutableList<String>> {
    val alphabet: List<Char> = ('a'..'z').toList()

    val fromXIndex = alphabet.indexOf(move[0])
    val fromYIndex = 8 - Integer.parseInt(move[1].toString());

    val toXIndex = alphabet.indexOf(move[2])
    val toYIndex = 8 - Integer.parseInt(move[3].toString());

    grid[fromYIndex][fromXIndex] = EMPTY_CELL
    grid[toYIndex][toXIndex] = player

    return grid
}

fun printBoard(grid: MutableList<MutableList<String>>) {
    var rowNumber: Int = grid.size;

    for (row: MutableList<String> in grid) {
        println("  " + row.joinToString(separator = "+", prefix = "+", postfix = "+", transform = { "---" }))
        println(
            "${rowNumber--} " + row.joinToString(
                separator = "|",
                prefix = "|",
                postfix = "|",
                transform = { " $it " })
        )
    }

    println("  " + ('a'..'h').joinToString(separator = "+", prefix = "+", postfix = "+", transform = { "---" }))
    println("  " + ('a'..'h').joinToString(separator = " ", prefix = " ", postfix = " ", transform = { " $it " }))
}

fun isValidMoveRegex(move: String): Boolean {
    return "[a-h][1-8][a-h][1-8]".toRegex().matches(move)
}

fun isNotMove(move: String): Boolean {
    return (move[0] == move[2] && move[1] == move[3])
}

fun isVerticalMove(move: String): Boolean {
    return (move[0] == move[2] && move[1] != move[3])
}

fun isHorizontalMove(move: String): Boolean {
    return (move[0] != move[2] && move[1] == move[3])
}

fun isDiagonalMove(move: String): Boolean {
    return (move[0] != move[2])
}

fun isStartMovePosition(player: String, move: String): Boolean {
    if (player == WHITE_CELL && move[1] == '2') return true
    if (player == BLACK_CELL && move[1] == '7') return true

    return false
}

fun isValidMoveDistance(player: String, move: String): Boolean {
    val distance: Int = getVerticalMoveDistance(move)

    if (isStartMovePosition(player, move) && distance <= 2) return true
    if (distance == 1) return true

    return false
}

fun isBackwardMove(player: String, move: String): Boolean {
    if (player == WHITE_CELL && move[1].toString().toInt() > move[3].toString().toInt()) return true
    if (player == BLACK_CELL && move[1].toString().toInt() < move[3].toString().toInt()) return true

    return false
}

fun isForwardMove(player: String, move: String): Boolean {
    if (player == WHITE_CELL && move[1].toString().toInt() < move[3].toString().toInt()) return true
    if (player == BLACK_CELL && move[1].toString().toInt() > move[3].toString().toInt()) return true

    return false
}

fun isPlayerMove(grid: MutableList<MutableList<String>>, player: String, move: String): Boolean {
    return getFromCell(grid, move) == player
}

fun isEnPassantMove(
    grid: MutableList<MutableList<String>>,
    player: String,
    move: String,
    history: MutableList<String>
): Boolean {
    if (history.isEmpty()) return false

    val previousMove: String = history.last()
    val previousPlayer: String = if (history.lastIndex % 2 == 0) WHITE_CELL else BLACK_CELL
    val previousVerticalDistance: Int = getVerticalMoveDistance(previousMove)

    if (!isStartMovePosition(previousPlayer, previousMove)) return false
    if (previousVerticalDistance != 2) return false
    if (!isDiagonalMove(move)) return false

    if (getToCell(grid, move) != EMPTY_CELL) return false

    if (previousMove[3] != move[1]) return false
    if (previousMove[2] != move[2]) return false

    return true
}

fun getFromCell(grid: MutableList<MutableList<String>>, move: String): String {
    val gridIndexList: List<Int> = convertBoardMoveToGridMove(move)

    return grid[gridIndexList[1]][gridIndexList[0]]
}

fun getToCell(grid: MutableList<MutableList<String>>, move: String): String {
    val gridIndexList: List<Int> = convertBoardMoveToGridMove(move)

    return grid[gridIndexList[3]][gridIndexList[2]]
}

fun getVerticalMoveDistance(move: String): Int {
    return abs(move[1].toString().toInt() - move[3].toString().toInt())
}

fun getHorizontalMoveDistance(move: String): Int {
    val gridIndexList: List<Int> = convertBoardMoveToGridMove(move)

    return abs(gridIndexList[0] - gridIndexList[2])
}

fun isValidMove(
    grid: MutableList<MutableList<String>>,
    player: String,
    move: String,
    history: MutableList<String>,
    isPrintMessage: Boolean = true
): Boolean {
    if (!isValidMoveRegex(move)) {
        if (isPrintMessage) println(INVALID_MESSAGE)

        return false
    }

    if (!isPlayerMove(grid, player, move)) {
        if (player == WHITE_CELL) {
            if (isPrintMessage) println("No white pawn at ${move[0]}${move[1]}")
        } else {
            if (isPrintMessage) println("No black pawn at ${move[0]}${move[1]}")
        }

        return false
    }

    if (isNotMove(move)) {
        if (isPrintMessage) println(INVALID_MESSAGE)

        return false
    }

    if (isHorizontalMove(move)) {
        if (isPrintMessage) println(INVALID_MESSAGE)

        return false
    }

    if (isBackwardMove(player, move)) {
        if (isPrintMessage) println(INVALID_MESSAGE)

        return false
    }

    if (!isValidMoveDistance(player, move)) {
        if (isPrintMessage) println(INVALID_MESSAGE)

        return false
    }

    if (isVerticalMove(move) && getToCell(grid, move) != EMPTY_CELL) {
        if (isPrintMessage) println(INVALID_MESSAGE)

        return false
    }

    val isEnPassantMove: Boolean = isEnPassantMove(grid, player, move, history)

    // diagonal and check en passant
    if (isDiagonalMove(move)) {
        if (getHorizontalMoveDistance(move) != 1) {
            if (isPrintMessage) println(INVALID_MESSAGE)

            return false
        }

        if (!isEnPassantMove && player == WHITE_CELL && getToCell(grid, move) != BLACK_CELL) {
            if (isPrintMessage) println(INVALID_MESSAGE)

            return false
        }

        if (!isEnPassantMove && player == BLACK_CELL && getToCell(grid, move) != WHITE_CELL) {
            if (isPrintMessage) println(INVALID_MESSAGE)

            return false
        }
    }

    return true
}

fun convertBoardMoveToGridMove(move: String): List<Int> {
    val alphabet: List<Char> = ('a'..'z').toList()

    val fromXIndex = alphabet.indexOf(move[0])
    val fromYIndex = 8 - Integer.parseInt(move[1].toString())

    val toXIndex = alphabet.indexOf(move[2])
    val toYIndex = 8 - Integer.parseInt(move[3].toString())

    return listOf(fromXIndex, fromYIndex, toXIndex, toYIndex)
}

fun convertGridMoveToBoardMove(gridIndexList: List<Int>): String {
    val alphabet: List<Char> = ('a'..'z').toList()

    val fromXIndex = alphabet[gridIndexList[0]]
    val fromYIndex = 8 - gridIndexList[1]
    val toXIndex = alphabet[gridIndexList[2]]
    val toYIndex = 8 - gridIndexList[3]

    return listOf<String>(
        fromXIndex.toString(),
        fromYIndex.toString(),
        toXIndex.toString(),
        toYIndex.toString()
    ).joinToString("")
}

fun isWinner(
    grid: MutableList<MutableList<String>>,
    player: String,
    move: String,
): Boolean {
    var hasOtherPlayer: Boolean = false

    loop@ for (row in grid) {
        for (cell in row) {
            if (cell != EMPTY_CELL && cell != player) {
                hasOtherPlayer = true
                break@loop
            }
        }
    }

    if (player == WHITE_CELL && (move[3] == '8' || !hasOtherPlayer)) {
        println("White wins!")
        return true
    }

    if (player == BLACK_CELL && (move[3] == '1' || !hasOtherPlayer)) {
        println("Black wins!")
        return true
    }

    return false
}

fun generateForwardMove(player: String, startMove: String): String {
    val list: MutableList<String> = mutableListOf()

    list.add(startMove[0].toString())
    list.add(startMove[1].toString())
    list.add(startMove[2].toString())

    if (player == WHITE_CELL) list.add((startMove[3].toString().toInt() + 1).toString())
    if (player == BLACK_CELL) list.add((startMove[3].toString().toInt() - 1).toString())

    return list.joinToString("")
}

fun generateDiagonalMove(player: String, startMove: String, isLeft: Boolean = true): String {
    val list: MutableList<String> = mutableListOf()

    list.add(startMove[0].toString())
    list.add(startMove[1].toString())

    var char: Char = startMove[2]

    if (isLeft) {
        char--
    } else {
        char++
    }

    list.add(char.toString())

    if (player == WHITE_CELL) list.add((startMove[3].toString().toInt() + 1).toString())
    if (player == BLACK_CELL) list.add((startMove[3].toString().toInt() - 1).toString())

    return list.joinToString("")
}

fun hasValidMove(grid: MutableList<MutableList<String>>, player: String, history: MutableList<String>): Boolean {
    for (rowIndex in grid.indices) {
        val row = grid[rowIndex]

        for (cellIndex in row.indices) {
            val cell = row[cellIndex]

            if (cell != player) continue

            val startMove = convertGridMoveToBoardMove(listOf(cellIndex, rowIndex, cellIndex, rowIndex))

            val forwardMove: String = generateForwardMove(player = player, startMove = startMove)
            val diagonalLeftMove: String = generateDiagonalMove(player = player, startMove = startMove)
            val diagonalRightMove: String = generateDiagonalMove(player = player, startMove = startMove, false)

            if (isValidMove(grid, player, forwardMove, history, false)) return true
            if (isValidMove(grid, player, diagonalLeftMove, history, false)) return true
            if (isValidMove(grid, player, diagonalRightMove, history, false)) return true
        }
    }

    return false
}

fun isStalemate(grid: MutableList<MutableList<String>>, player: String, history: MutableList<String>): Boolean {
    val hasValidMove = hasValidMove(grid, player, history)

    if (!hasValidMove) {
        println("Stalemate!")
    }

    return !hasValidMove
}

fun main() {
    val playerList: MutableList<String> = mutableListOf()
    var index: Int = 0

    println("Pawns-Only Chess")

    println("First Player's name:")
    playerList.add(readln())

    println("Second Player's name:")
    playerList.add(readln())

    var grid: MutableList<MutableList<String>> = getGrid()
    val history: MutableList<String> = mutableListOf()

    printBoard(grid)

    do {
        val player = if (index % 2 == 0) WHITE_CELL else BLACK_CELL
        if (isStalemate(grid, player, history)) break

        println("${playerList[index % 2]}'s turn:")

        val move: String = readln()

        if (move == "exit") break
        if (!isValidMove(grid, player, move, history)) continue

        if (isEnPassantMove(grid, player, move, history)) {
            updateGrid(grid, EMPTY_CELL, history.last())
        }

        history.add(move)

        grid = updateGrid(grid, player, move)

        printBoard(grid)

        if (isWinner(grid, player, move)) break

        index++
    } while (true)

    println("Bye!")
}