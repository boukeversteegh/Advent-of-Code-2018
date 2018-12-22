package util

import kotlin.math.abs

interface IPosition: Comparable<IPosition> {
    fun distance(position: IPosition): Int
    operator fun plus(position: IPosition): IPosition
    operator fun minus(position: IPosition): IPosition

    override operator fun compareTo(other: IPosition): Int {
        if (y == other.y && x == other.x) {
            return 0
        }
        if (y > other.y) {
            return 1
        }
        if (y < other.y) {
            return -1
        }

        return if (x > other.x) {
            1
        } else {
            -1
        }
    }


    val x: Int
    val y: Int
}

data class Position(override val x: Int, override val y: Int) : IPosition {
    override fun distance(position: IPosition): Int {
        return abs(x - position.x) + abs(y - position.y)
    }

    override operator fun plus(position: IPosition): IPosition {
        return Position(x + position.x, y + position.y)
    }


    override operator fun minus(position: IPosition): IPosition {
        return Position(x - position.x, y - position.y)
    }

    override fun toString(): String {
        return "($x, $y)"
    }
}

fun p(x: Int, y: Int) = Position(x, y)