import java.io.File

fun main(vararg args: String) {

    File("inputs/02.txt").useLines {
        val guardSleepMinutes = hashMapOf<String, Int>()

        var activeGuardId: String? = null
        var asleepSinceMinute: Int = 0
        it
            .toList()
            .sorted()
            .map { line ->
                val beginShift = Regex("Guard #(\\d+) begins shift").find(line)
                val time = Regex("00:(\\d+)").find(line)
                val minute = time!!.groups[1]!!.value.toInt()
                val wakeUp = Regex("wakes up").find(line)
                val sleep = Regex("falls asleep").find(line)

                if (beginShift != null) {
                    val guardId = beginShift.groupValues[1]
                    guardSleepMinutes.putIfAbsent(guardId, 0)
                    activeGuardId = guardId
                }

                if (wakeUp != null && activeGuardId != null) {
                    val minutesAsleep = minute - asleepSinceMinute
                    guardSleepMinutes[activeGuardId!!] = (guardSleepMinutes.getOrDefault(activeGuardId!!, 0) + minutesAsleep)
                }

                if (sleep != null) {
                    asleepSinceMinute = minute
                }


                guardSleepMinutes
            }

        println(guardSleepMinutes)
    }
}