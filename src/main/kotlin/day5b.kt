import java.io.File

fun main(vararg args: String) {

    File("inputs/05.txt").useLines { lines ->
        val polymer = lines.first()

        val units = 'a'..'z'

        val m = units
            .map { polymer.replace(it.toString(), "", ignoreCase = true) }
            .map { react(it).length }
            .min()

        println(m)
    }

}
