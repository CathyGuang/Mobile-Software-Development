package com.ait.minesweeper.Model

import java.util.*

object MinesweeperModel {

    public val EMPTY: Short = 0
    public val FLAG: Short = 1
    public val MINE: Short = 2
    public val VISITED: Short = 3

    private var model = arrayOf(
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, MINE, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, MINE, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, MINE, EMPTY)
    )

    private val originalModel = arrayOf(
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, MINE, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, MINE, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, MINE, EMPTY)
    )

    fun resetModel() {
        model = originalModel
    }

    fun getFieldContent(x: Int, y: Int) = model[x][y]

    fun setFieldContent(x: Int, y: Int, content: Short) {
        model[x][y] = content
    }

    fun trackMines(x: Int, y: Int): Int {

        var numOfMines = 0
        if (y+1<5 && originalModel[x][y + 1] == MINE) {
            numOfMines++
        }
        if (y-1>-1 && originalModel[x][y - 1] == MINE) {
            numOfMines++
        }
        if (x+1<5 && originalModel[x + 1][y] == MINE) {
            numOfMines++
        }
        if (x-1>-1 && originalModel[x - 1][y] == MINE) {
            numOfMines++
        }
        if (y+1<5 && x+1<5 && originalModel[x + 1][y + 1] == MINE) {
            numOfMines++
        }
        if (y-1>-1 && x+1<5 && originalModel[x + 1][y - 1] == MINE) {
            numOfMines++
        }
        if (x-1>-1 && y-1>-1 && originalModel[x - 1][y - 1] == MINE) {
            numOfMines++
        }
        if (x-1>-1 && y+1<5 && originalModel[x - 1][y + 1] == MINE) {
            numOfMines++
        }
//        val neighbors = listOf<Short>(
//            model[x][y + 1], model[x][y - 1], model[x + 1][y], model[x - 1][y],
//            model[x + 1][y + 1], model[x + 1][y - 1], model[x - 1][y + 1], model[x - 1][y - 1]
//        )
//
//        return Collections.frequency(
//            neighbors,
//            MINE
//        )
        return numOfMines
    }
}
