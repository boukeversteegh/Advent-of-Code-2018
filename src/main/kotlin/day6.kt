import util.IPosition
import util.Position
import java.io.File

class Area(var area: Int, var infinite: Boolean = false) {
    fun inc() {
        area++
    }

    override fun toString(): String {
        return "$area/$infinite"
    }
}

data class Coordinate(val position: Position, var area: Area = Area(0)) : IPosition by position {
    override fun toString(): String {
        return "C($position, $area)"
    }
}

fun main(vararg args: String) {
    File("inputs/06.txt").useLines { lines ->
        val coordinates = lines.map {
            val (x, y) = it.split(", ").map(String::toInt)
            Coordinate(Position(x, y))
        }.toList()

        val topLeft = Position(coordinates.minBy { it.x }!!.x, coordinates.minBy { it.y }!!.y)
        val bottomRight = Position(coordinates.maxBy { it.x }!!.x, coordinates.maxBy { it.y }!!.y)


        val horizontal = IntRange(topLeft.x - 100, bottomRight.x + 100)
        val vertical = IntRange(topLeft.y - 100, bottomRight.y + 100)

        for (x in horizontal) {
            for (y in vertical) {
                val position = Position(x, y)
                val distances = coordinates
                    .map { it to it.distance(position) }
                    .sortedBy { it.second }

                if (distances.get(0).second == distances.get(1).second) {
                    // no shortest distance, don't count area
                    continue
                }
                val closestCoordinate = distances.first().first

                val isBoundaryPosition = (
                    x == horizontal.start || x == horizontal.endInclusive ||
                        y == vertical.start || y == vertical.endInclusive
                    )

                val area = closestCoordinate?.area!!
                if (isBoundaryPosition) {
                    closestCoordinate.area.infinite = true
                } else {
                    area.inc()
                }
            }
        }

        println(coordinates
            .filter { !it.area.infinite })

        val biggestArea = coordinates
            .filter { !it.area.infinite }
            .maxBy { it.area.area }

        println(biggestArea?.area!!.area)
    }
}
