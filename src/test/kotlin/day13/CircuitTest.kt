package day13

import day13.Direction.*
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import util.IPosition
import util.Position
import util.p
import java.io.File

class CircuitTest {
    @Test
    fun carts() {
        val circuit = Circuit(listOf(
            "/<--\\",
            "|   |",
            "\\->-/"
        ))
        assertEquals(circuit.carts.size, 2)
        assertEquals(circuit.carts[0].direction, LEFT)
        assertEquals(circuit.carts[1].direction, RIGHT)
    }

    @Test
    fun right() {
        val circuit = Circuit(listOf(
            ">---"
        ))
        circuit.nextTick()
        assertEquals(circuit.carts[0].position, Position(1, 0))
    }

    @Test
    fun cart_turns_at_right_turn() {
        val circuit = Circuit(listOf(
            ">\\",
            " |"
        ))
        circuit.nextTick()
        assertEquals(circuit.carts[0].position, Position(1, 0))
        assertEquals(circuit.carts[0].direction, Direction.DOWN)
    }


    @Test
    fun cart_down() {
        val circuit = Circuit(listOf(
            " v",
            " |"
        ))
        circuit.nextTick()
        assertEquals(circuit.carts[0].position, Position(1, 1))
    }

    @Test
    fun cart_down_right_turn() {
        val circuit = Circuit(listOf(
            " v",
            "-/"
        ))
        circuit.nextTick()
        assertEquals(circuit.carts[0].position, Position(1, 1))
        assertEquals(circuit.carts[0].direction, LEFT)
        circuit.nextTick()
        assertEquals(circuit.carts[0].position, Position(0, 1))
    }

    @Test
    fun cart_right_circle() {
        val circuit = Circuit(listOf(
            "/>\\",
            "\\-/"
        ))
        val cart = circuit.carts[0]

        circuit.nextTick()
        assert(cart, p(2, 0), DOWN)

        circuit.nextTick()
        assert(cart, p(2, 1), LEFT)

        circuit.nextTick()
        circuit.nextTick()
        assert(cart, p(0, 1), UP)

        circuit.nextTick()
        assert(cart, p(0, 0), RIGHT)
    }

    @Test
    fun cart_left_circle() {
        val circuit = Circuit(listOf(
            "/<\\",
            "\\-/"
        ))
        val cart = circuit.carts[0]

        circuit.nextTick()
        assert(cart, p(0, 0), DOWN)

        circuit.nextTick()
        assert(cart, p(0, 1), RIGHT)

        circuit.nextTick()
        circuit.nextTick()
        assert(cart, p(2, 1), UP)

        circuit.nextTick()
        assert(cart, p(2, 0), LEFT)
    }

    @Test
    fun cart_intersection_first_turn_left() {
        val circuit = Circuit(listOf(
            ">+"
        ))
        val cart = circuit.carts[0]

        circuit.nextTick()
        assert(cart, p(1, 0), UP)
    }

    @Test
    fun cart_intersection_second_go_straight() {
        val circuit = Circuit(listOf(
            " |",
            " +",
            ">+"
        ))
        val cart = circuit.carts[0]

        circuit.nextTick()
        circuit.nextTick()
        assert(cart, UP)
    }

    @Test
    fun cart_intersection_third_turn_right() {
        val circuit = Circuit(listOf(
            " +",
            " +",
            ">+"
        ))
        val cart = circuit.carts[0]

        circuit.nextTick()
        circuit.nextTick()
        circuit.nextTick()
        assert(cart, RIGHT)
    }

    @Test
    fun cart_intersection_fourth_turn_left() {
        val circuit = Circuit(listOf(
            " ++",
            " +",
            ">+"
        ))
        val cart = circuit.carts[0]

        circuit.nextTick()
        circuit.nextTick()
        circuit.nextTick()
        circuit.nextTick()
        assert(cart, UP)
    }

    @Test
    fun car_crash() {
        val circuit = Circuit(listOf(
            "><"
        ))
        val cart1 = circuit.carts[0]
        val cart2 = circuit.carts[1]

        circuit.nextTick()
        assertEquals(Cart.Status.CRASHED, cart1.status)
        assertEquals(Cart.Status.CRASHED, cart2.status)
        assert(cart1, p(1, 0))
        assert(cart2, p(1, 0))
    }


    @Test
    fun cars_chasing_to_right_will_crash() {
        val circuit = Circuit(listOf(
            "->>---"
        ))
        val cart1 = circuit.carts[0]
        val cart2 = circuit.carts[1]

        circuit.nextTick()
        assertEquals(Cart.Status.CRASHED, cart1.status)
        assertEquals(Cart.Status.CRASHED, cart2.status)
    }

    @Test
    fun cars_chasing_to_left_will_not_crash() {
        val circuit = Circuit(listOf(
            "-<<---"
        ))
        val cart1 = circuit.carts[0]
        val cart2 = circuit.carts[1]

        circuit.nextTick()
        assertEquals(Cart.Status.RUNNING, cart1.status)
        assertEquals(Cart.Status.RUNNING, cart2.status)
    }

    @Test
    fun cars_chasing_down_will_crash() {
        val circuit = Circuit(listOf(
            "v",
            "v",
            "|"
        ))
        val cart1 = circuit.carts[0]
        val cart2 = circuit.carts[1]

        circuit.nextTick()
        assertEquals(Cart.Status.CRASHED, cart1.status)
        assertEquals(Cart.Status.CRASHED, cart2.status)
    }

    @Test
    fun cars_chasing_up_will_not_crash() {
        val circuit = Circuit(listOf(
            "|",
            "^",
            "^"
        ))
        val cart1 = circuit.carts[0]
        val cart2 = circuit.carts[1]

        circuit.nextTick()
        assertEquals(Cart.Status.RUNNING, cart1.status)
        assertEquals(Cart.Status.RUNNING, cart2.status)
    }

    @Test
    fun car_no_crash() {
        val circuit = Circuit(listOf(
            " | ",
            ">+-",
            " |",
            " ^"
        ))
        val cart1 = circuit.carts[0]
        val cart2 = circuit.carts[1]

        circuit.nextTick()
        assert(cart1, p(1, 1))
        assert(cart2, p(1, 2))

        circuit.nextTick()
        assertEquals(Cart.Status.RUNNING, cart1.status)
        assertEquals(Cart.Status.RUNNING, cart2.status)
        assert(cart1, p(1, 0))
        assert(cart2, p(1, 1))
    }

    @Test
    fun render() {
        val input = listOf(
            "/<--\\",
            "|   |",
            "\\->-/"
        )
        val circuit = Circuit(input)
        assertEquals(input.joinToString("\n"), circuit.render())
    }

    @Test
    fun render2() {
        val input = listOf(
            "/<--\\",
            "|   |",
            "\\->-/"
        )
        val circuit = Circuit(input)
        circuit.simulateTillCrash(1000)
    }

    @Ignore
    @Test
    fun example() {
        val input =
            """
                /->-\
                |   |  /----\
                | /-+--+-\  |
                | | |  | v  |
                \-+-/  \-+--/
                  \------/
            """.trimIndent().lines()

        Circuit(input).simulateTillCrash(500)
    }

    @Test
    fun complex_example() {
        val input =
            """
                /->-\
                |   |  /----\
                | /-+--+-\  |
                | | |  | ^  |
                \-+-/  \-+--/
                  \------/
            """.trimIndent().lines()

        Circuit(input).simulateTillCrash(400)
    }

    @Ignore
    @Test
    fun simulation2() {
        val input =
            """
            /<<-\
            |   |
            \---/
            """.trimIndent().lines()

        Circuit(input).simulateTillCrash(500)
    }

    @Test
    fun sorted() {
        val points = listOf(p(1, 1), p(0, 0), p(0, 1), p(1, 0))
        val sortedPoints = points.sortedWith(compareBy({ it.y }, { it.x }))
        assertEquals(listOf(p(0, 0), p(1, 0), p(0, 1), p(1, 1)), sortedPoints)
    }

    @Test
    fun sortedCarts() {
        val carts = listOf(Cart(p(1, 1), LEFT), Cart(p(0, 0), LEFT), Cart(p(0, 1), LEFT), Cart(p(1, 0), LEFT))

        val sortedCarts = carts.sortedWith(compareBy({ it.y }, { it.x }))

        assertEquals(listOf(
            Cart(p(0, 0), LEFT),
            Cart(p(1, 0), LEFT),
            Cart(p(0, 1), LEFT),
            Cart(p(1, 1), LEFT)
        ), sortedCarts)


        carts[1].position = p(9, 9)

        val sortedCarts2 = carts.sortedWith(compareBy({ it.y }, { it.x }))

        assertEquals(listOf(
            Cart(p(1, 0), LEFT),
            Cart(p(0, 1), LEFT),
            Cart(p(1, 1), LEFT),
            Cart(p(9, 9), LEFT)
        ), sortedCarts2)
    }

    @Test
    fun day13A() {
        File("inputs/13.txt").useLines { lines ->
            val circuit = Circuit(lines.toList())

            circuit.simulateTillCrash(10)
        }
    }

    @Test
    fun day13B() {
        File("inputs/13.txt").useLines { lines ->
            val circuit = Circuit(lines.toList())

            circuit.simulateTillOneLivingCar(10)
        }
    }

    private fun assert(cart: Cart, position: Position, direction: Direction) {
        assertEquals(position, cart.position)
        assertEquals(direction, cart.direction)
    }

    private fun assert(cart: Cart, direction: Direction) {
        assertEquals(direction, cart.direction)
    }


    private fun assert(cart: Cart, position: Position) {
        assertEquals(position, cart.position)
    }
}