package util

import kotlin.math.abs

interface IPosition {
    fun distance(position: IPosition): Int
    operator fun plus(position: IPosition): IPosition

    val x: Int
    val y: Int
}

data class Position(override val x: Int, override val y: Int) : IPosition {
    override fun distance(position: IPosition): Int {
        return abs(x - position.x) + abs(y - position.y)
    }

    override operator fun plus(otherPosition: IPosition): IPosition {
        return Position(x + otherPosition.x, y + otherPosition.y)
    }

    override fun toString(): String {
        return "($x, $y)"
    }
}

fun p(x: Int, y: Int) = Position(x, y)