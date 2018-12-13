import util.IPosition
import util.Position
import java.io.File


data class Point(var position: IPosition, var velocity: IPosition)

class Screen(val w: Int, val h: Int) {
    private val screen = BooleanArray(w * h)

    private fun offset(x: Int, y: Int): Int {
        return x + (w * y)
    }

    private fun validOffset(offset: Int): Boolean {
        return offset < screen.size && offset >= 0
    }

    fun set(x: Int, y: Int) {
        val offset = offset(x, y)
        if (validOffset(offset)) {
            screen[offset] = true
        }
    }

    fun get(x: Int, y: Int): Boolean {
        val offset = offset(x, y)
        return validOffset(offset) && screen[offset]
    }
}


fun render(points: List<Point>, w: Int, h: Int) {

    val minX = points.map { it.position }.minBy { it.x }!!.x
    val minY = points.map { it.position }.minBy { it.y }!!.y
    val maxX = points.map { it.position }.maxBy { it.x }!!.x
    val maxY = points.map { it.position }.maxBy { it.y }!!.y

    val sW = maxX - minX
    val sH = maxY - minY
    val screen = Screen(w, h)

    points.forEach { point ->
        //        screen.set(point.position.x - minX, point.position.y - minY)
        screen.set(point.position.x, point.position.y)
    }

    for (y in minY..maxY) {
        for (x in minX..maxX) {
            print(if (screen.get(x, y)) '#' else ' ')
        }
        println("_____")
    }
}

fun main(vararg args: String) {
    File("inputs/10.txt").useLines { lines ->
        val points = lines.map { line ->
            val pointData = Regex("(-?\\d+)").findAll(line).map { it.value.toInt() }.toMutableList()
            val (x, y, dx, dy) = pointData

            Point(Position(x, y), Position(dx, dy))
        }.toList()

//        val points = listOf(
//            Point(Position(-10, -10), Position(1, 1)),
//            Point(Position(-10, -10), Position(0, 0))
//        )

        val w = 500
        val h = 300
        while (true) {
//            render(points, w, h)
            iterate(points)
            if (points.any { point ->
                    point.position.x in 0..w &&
                        point.position.y in 0..h
                }) {
                render(points, w, h)
                Thread.sleep(1)
                println()
//                break
            }
//            print("\r")
        }
    }
}

fun iterate(points: List<Point>) {
    points.forEach {
        it.position = it.position + it.velocity
    }
}
