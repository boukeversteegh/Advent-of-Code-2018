import java.io.File

fun time(s: String): Int {
    return s[0].toInt() - 4
}

typealias Node = String

data class Rule(val dependency: Node, val target: Node)

data class Worker(val node: Node, var remainingTime: Int)

fun main(vararg args: String) {
    File("inputs/07.txt").useLines { lines ->
        val targets = mutableSetOf<Node>()
        val rules = mutableSetOf<Rule>()
        lines.forEach { line ->
            val rule = Regex("Step (.) must be finished before step (.) can begin\\.").find(line)
            if (rule != null) {
                val (dependency, target) = rule.destructured

                targets.add(target)
                targets.add(dependency)
                rules.add(Rule(dependency, target))
            }
        }

        var steps = ""
        var totalTime = 0

        val maxWorkers = 5
        var workers = mutableListOf<Worker>()

        while (targets.isNotEmpty()) {
            for (node in targets.sorted()) {
                val nodeDeps = rules.filter { node == it.target }

                if (nodeDeps.isEmpty()) {
                    targets.remove(node)
                    val nodeTime = time(node)

                    workers.add(Worker(node, nodeTime))
                    if (workers.size == maxWorkers) {
                        break
                    }
                }
            }

            workers.sortBy { it.remainingTime }
            val completedWorker = workers.first()

            totalTime += completedWorker.remainingTime
            steps += completedWorker.node

            workers.remove(completedWorker)
            rules.removeIf { completedWorker.node == it.dependency }

            workers.forEach {
                it.remainingTime -= completedWorker.remainingTime
            }
        }

        if (workers.isNotEmpty()) {
            totalTime += workers.last().remainingTime
        }

        println(steps)
        println(totalTime)
    }
}
