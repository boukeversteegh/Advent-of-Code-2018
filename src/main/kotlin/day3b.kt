import util.CountMap
import java.io.File


fun main(vararg args: String) {

    File("inputs/03.txt").useLines { lines ->

        val sheetClaims = getSheetClaims(lines)

        println(sheetClaims.values.filter { it > 1 }.count())
    }
}
