package util

import kotlin.math.abs

interface IPosition {
    fun distance(position: IPosition): Int

    val x: Int
    val y: Int
}

data class Position(override val x: Int, override val y: Int) : IPosition {
    override fun distance(position: IPosition): Int {
        return abs(x - position.x) + abs(y - position.y)
    }
}