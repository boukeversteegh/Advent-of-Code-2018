package day17

import org.junit.Test
import util.IPosition
import util.p

class AutomatonTest {
    @Test
    fun construct() {
        Automaton<Char>()
    }

    @Test
    fun add() {
        val automaton = Automaton<Char>()
        automaton.add(p(0, 0), '.')

        val cell = automaton.get(p(0, 0))
        assert(cell == '.')
    }

    @Test
    fun nextWithoutRules() {
        val automaton = Automaton<Char>()
        automaton.add(p(0, 0), '|')
        automaton.add(p(0, 1), '.')

        automaton.next()

        assert(automaton.get(p(0, 0)) == '|')
        assert(automaton.get(p(0, 1)) == '.')
    }

    @Test
    fun next() {
        val automaton = Automaton<Char>()
        automaton.add(p(0, 0), '|')
        automaton.add(p(0, 1), '.')

        automaton.addRule(Rule<Char>(
            mapOf(
                p(0, 0) to '|',
                p(0, 1) to '.'
            ),
            mapOf(
                p(0, 0) to '|',
                p(0, 1) to '|'
            ))
        )

        automaton.next()

        assert(automaton.get(p(0, 0)) == '.')
        assert(automaton.get(p(0, 1)) == '|')
    }
}