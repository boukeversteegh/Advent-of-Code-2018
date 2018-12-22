package day17

import util.IPosition

class Rule<C>(val input: Map<IPosition, C>, output: Map<IPosition, C>) {
    val size: IPosition
        get() = input.keys.max()!! - input.keys.min()!!

}

class Automaton<C> {
    private val cells: MutableMap<IPosition, C> = mutableMapOf()
    private val rules: MutableSet<Rule<C>> = mutableSetOf()

    fun add(p: IPosition, t: C) {
        cells[p] = t
    }

    fun get(p: IPosition): C? {
        return cells[p]
    }

    fun addRule(rule: Rule<C>) {
        rules.add(rule)
    }

    fun next() {
        val newCells: MutableMap<IPosition, C> = mutableMapOf()

        cells.forEach { position, cell ->
            val x = position.x
            val y = position.y
            rules.forEach { rule ->
                // see if this rule matches the area of its own size


//                val (width: Int, height: Int) = rule.size
                val width = rule.size.x
                val height = rule.size.y

                val ruleArea = getArea(position, position + rule.size)
                val ruleMatches = (ruleArea - rule.input).isEmpty()

                if (ruleMatches) {
//                    newCells.putAll()
                }
//                for (dy in y until y + height) {
//                    for (dx in x until x + width) {
//
//                    }
//                }
            }
        }
    }

    fun getArea(topLeft: IPosition, bottomRight: IPosition): Map<IPosition, C> {
        return cells.filterKeys { it >= topLeft && it <= bottomRight }
    }

}
