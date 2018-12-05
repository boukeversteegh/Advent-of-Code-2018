import java.io.File
import kotlin.math.max

fun main(vararg args: String) {

    File("inputs/05.txt").useLines {
        val polymer = it.first()
        var result = react(polymer)

        println(result.length)
    }

}

private fun react(polymer: String): String {
    var result = polymer

    var i = 0
    do {
        val pair = result.substring(i, i + 2)
        if (reacts(pair)) {
            result = result.substring(0, i) + result.substring(i + 2)
            i = max(i - 1, 0) // go back
        } else {
            i++
        }
    } while (i < result.length - 1)
    return result
}

fun reacts(pair: String): Boolean {
    return !pair[0].equals(pair[1], false) && pair[0].equals(pair[1], true)
}
