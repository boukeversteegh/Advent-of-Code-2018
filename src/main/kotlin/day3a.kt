import util.CountMap
import java.io.File


fun main(vararg args: String) {

    File("inputs/03.txt").useLines { lines ->
        val sheetClaims = getSheetClaims(lines)
        println(sheetClaims.values.filter { it > 1 }.count())
    }
}

fun getSheetClaims(lines: Sequence<String>): CountMap<String> {
    val sheetClaims = CountMap<String>()

    lines.forEach { line ->
        val result = Regex("(\\d+),(\\d+): (\\d+)x(\\d+)").find(line)

        val (left, top, width, height) = result?.destructured ?: return@forEach

        val horizontal = IntRange(left.toInt(), left.toInt() + width.toInt() - 1)
        val vertical = IntRange(top.toInt(), top.toInt() + height.toInt() - 1)

        for (x in horizontal) {
            for (y in vertical) {
                sheetClaims.inc("$x,$y")
            }
        }
    }
    return sheetClaims
}
