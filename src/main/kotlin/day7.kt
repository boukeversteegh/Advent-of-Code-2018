import java.io.File


fun main(vararg args: String) {
    File("inputs/07.txt").useLines { lines ->
        val targets = mutableSetOf<String>()
        val rules = mutableSetOf<Pair<String, String>>()
        lines.forEach { line ->
            val rule = Regex("Step (.) must be finished before step (.) can begin\\.").find(line)
            if (rule != null) {
                val (dependency, target) = rule.destructured

                targets.add(target)
                targets.add(dependency)
                rules.add(dependency to target)
            }
        }

        var steps = ""

        while (targets.isNotEmpty()) {
            for (node in targets.sorted()) {
                val nodeDeps = rules.filter { node == it.second }

                if (nodeDeps.isEmpty()) {
                    steps += node
                    targets.remove(node)
                    rules.removeIf { node == it.first }
                    break
                }
            }
        }

        println(steps)
    }
}
