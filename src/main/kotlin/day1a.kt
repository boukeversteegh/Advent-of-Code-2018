import java.io.File

fun main(vararg args: String) {

    File("inputs/01.txt").useLines {
        println(it.toList().map(String::toInt).sum())
    }
}