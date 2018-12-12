import java.io.File

fun time(s: String): Int {
    return s[0].toInt() - 4
}

typealias Node = String

data class Rule(val dependency: Node, val target: Node)

data class Worker(val node: Node, var remainingTime: Int)

fun main(vararg args: String) {
    File("inputs/07.txt").useLines { lines ->
        val rules = mutableSetOf<Rule>()
        lines.forEach { line ->
            val rule = Regex("Step (.) must be finished before step (.) can begin\\.").find(line)
            if (rule != null) {
                val (dependency, target) = rule.destructured
                rules.add(Rule(dependency, target))
            }
        }

        val solver = Solver(rules, 5)
        solver.solve()
        println(solver.steps)
        println(solver.totalTime)
    }
}


class Solver(private val rules: MutableSet<Rule>, private val maxWorkers: Int) {
    private val targets = rules.flatMap { listOf(it.dependency, it.target) }.toMutableSet()
    private val workers = mutableListOf<Worker>()

    private var _steps = ""
    private var _totalTime = 0

    val steps get() = _steps
    val totalTime get() = _totalTime

    fun solve() {
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

            _totalTime += completedWorker.remainingTime
            _steps += completedWorker.node

            workers.remove(completedWorker)
            rules.removeIf { completedWorker.node == it.dependency }

            workers.forEach {
                it.remainingTime -= completedWorker.remainingTime
            }
        }

        if (workers.isNotEmpty()) {
            _totalTime += workers.last().remainingTime
        }
    }
}
