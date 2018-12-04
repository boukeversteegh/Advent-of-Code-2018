package day1

import java.io.File

fun main(vararg args: String) {

    File("inputs/01.txt").useLines {
        val deltas = it.toList().map(String::toInt)

        val frequencies = mutableSetOf<Int>()

        var freq = 0

        while (true) {
            for (delta in deltas) {
                freq += delta

                if (freq in frequencies) {
                    println("First double frequency is: $freq")
                    return
                }
                frequencies.add(freq)
            }
        }
    }
}