package day13

import day13.Cart.Status.CRASHED
import day13.Cart.Status.RUNNING
import day13.Rotation.LEFT
import day13.Rotation.RIGHT
import util.IPosition
import util.Position
import util.getNext
import util.getPrev

sealed class Cell
abstract class Track(private val position: IPosition, var cart: Cart? = null) : IPosition by position, Cell() {
    fun addCart(cart: Cart) {
        val existingCart = this.cart

        if (existingCart is Cart) {
            cart.status = CRASHED
            existingCart.status = CRASHED
            this.cart = null
        } else {
            this.cart = cart
        }
    }

    override fun toString(): String {
        val cart = this.cart
        val RED = 27.toChar() + "[31m"
        val RESET = 27.toChar() + "[0m"
        return if (cart != null) {
            return RED + cart.render() + RESET
        } else {
            this.render()
        }
    }

    abstract fun render(): String
}

object Empty : Cell() {
    override fun toString(): String {
        return " "
    }
}

sealed class Turn(position: IPosition) : Track(position)
class PositiveTurn(position: IPosition) : Turn(position) {
    override fun render(): String = "/"
}

class NegativeTurn(position: IPosition) : Turn(position) {
    override fun render(): String = "\\"
}

class InterSection(position: IPosition) : Track(position) {
    override fun render(): String = "+"
}

sealed class TrackWithCart(position: IPosition, cart: Cart? = null) : Track(position, cart)
class HorizontalTrack(position: IPosition, cart: Cart? = null) : TrackWithCart(position, cart) {
    override fun render(): String = "-"
}

class VerticalTrack(position: IPosition, cart: Cart? = null) : TrackWithCart(position, cart) {
    override fun render(): String = "|"
}

private fun cellOf(char: Char, position: Position): Cell {
    return when (char) {
        '-' -> HorizontalTrack(position)
        '>' -> HorizontalTrack(position, Cart(position, Direction.RIGHT))
        '<' -> HorizontalTrack(position, Cart(position, Direction.LEFT))
        '^' -> VerticalTrack(position, Cart(position, Direction.UP))
        'v' -> VerticalTrack(position, Cart(position, Direction.DOWN))
        '|' -> VerticalTrack(position)
        '/' -> PositiveTurn(position)
        '\\' -> NegativeTurn(position)
        '+' -> InterSection(position)
        else -> Empty
    }
}

enum class Orientation {
    HORIZONTAL,
    VERTICAL
}

enum class Direction(position: IPosition, val orientation: Orientation) : IPosition by position {
    UP(Position(0, -1), Orientation.VERTICAL),
    DOWN(Position(0, 1), Orientation.VERTICAL),
    LEFT(Position(-1, 0), Orientation.HORIZONTAL),
    RIGHT(Position(1, 0), Orientation.HORIZONTAL);

    fun turn(rotation: Rotation): Direction {
        return when (rotation) {
            Rotation.LEFT -> order.getPrev(this)
            Rotation.RIGHT -> order.getNext(this)
            else -> this
        }
    }

    companion object {
        val order = listOf(UP, RIGHT, DOWN, LEFT)
    }
}

data class Cart(var position: IPosition, var direction: Direction) : IPosition {
    override fun distance(position: IPosition): Int {
        return this.position.distance(position)
    }

    override fun plus(position: IPosition): IPosition {
        return this.position + position
    }

    override val x: Int get() = position.x
    override val y: Int get() = position.y

    private var intersectionSequenceStep = 0
    var status: Status = Status.RUNNING

    enum class Status {
        RUNNING,
        CRASHED
    }

    fun ride() {
        position += direction
    }

    fun turn(rotation: Rotation) {
        direction = direction.turn(rotation)
    }

    fun turn(turn: Turn) {
        when (direction.orientation) {
            Orientation.VERTICAL -> when (turn) {
                is PositiveTurn -> turn(RIGHT)
                is NegativeTurn -> turn(LEFT)
            }
            Orientation.HORIZONTAL -> when (turn) {
                is PositiveTurn -> turn(LEFT)
                is NegativeTurn -> turn(RIGHT)
            }
        }
    }

    fun turn(interSection: InterSection) {
        turn(intersectionSequence[intersectionSequenceStep])
        intersectionSequenceStep = (intersectionSequenceStep + 1) % intersectionSequence.size
    }

    fun render(): String {

        val directionChar = when (this.direction) {
            Direction.UP -> "^"
            Direction.DOWN -> "v"
            Direction.RIGHT -> ">"
            Direction.LEFT -> "<"
        }

        return if (status == CRASHED) "X" else directionChar
    }

    override fun toString(): String {
        return "Cart($position, ${render()})"
    }

    companion object {
        var intersectionSequence = listOf(Rotation.LEFT, Rotation.NONE, RIGHT)
    }
}

enum class Rotation {
    LEFT, NONE, RIGHT
}

class Circuit(lines: List<String>) {
    private val grid: Array<Array<Cell>>

    val carts: List<Cart>
    val width: Int = lines.map { it.length }.max()!!
    val height: Int = lines.size
    val crashedCarts: List<Cart> get() = carts.filter { it.status == CRASHED }
    val runningCarts: List<Cart> get() = carts.filter { it.status == RUNNING }

    init {
        grid = Array(height) { Array<Cell>(width) { Empty } }

        val carts = mutableListOf<Cart>()
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                val position = Position(x, y)
                val cell = cellOf(char, position)

                if (cell is Track) {
                    val cart = cell.cart
                    if (cart != null) {
                        carts.add(cart)
                    }
                }

                grid[y][x] = cell
            }
        }
        this.carts = carts.toList()
    }

    private fun getTrack(cart: Cart): Track {
        val cell = try {
            grid[cart.position.y][cart.position.x]
        } catch (e: ArrayIndexOutOfBoundsException) {
            null
        }
        if (cell is Track) {
            return cell
        } else {
            throw Exception("Cart is not on a track!")
        }
    }

    fun nextTick() {
        val mcarts = carts.sortedWith(compareBy({ it.y }, { it.x }))
        mcarts.forEach(this::ride)
    }

    private fun ride(cart: Cart) {
        if (cart.status == CRASHED) {
            return
        }

        val track = moveToNextTrack(cart)

        if (cart.status == CRASHED) {
            return
        }
        when (track) {
            is Turn -> cart.turn(track)
            is InterSection -> cart.turn(track)
        }

    }

    private fun moveToNextTrack(cart: Cart): Track {
        val oldTrack = this.getTrack(cart)
        cart.ride()
        val newTrack = this.getTrack(cart)

        moveCart(cart, oldTrack, newTrack)
        return newTrack
    }

    private fun moveCart(cart: Cart, oldTrack: Track, newTrack: Track) {
        oldTrack.cart = null
        newTrack.addCart(cart)
    }


    fun render(): String {
        val s = StringBuilder()
        for (row in grid) {
            for (cell in row) {
                s.append(cell.toString())
            }
            s.append("\n")
        }
        return s.removeSuffix("\n").toString()
    }

    fun simulateTillCrash(interval: Long) {
        println(render())
        while (crashedCarts.isEmpty()) {
            Thread.sleep(interval)
            nextTick()
            println(render())
        }

        println(crashedCarts)
    }

    fun simulateTillOneLivingCar(interval: Long, render: Boolean = false) {
        while (runningCarts.size > 1) {
            if (render) {
                println(render())
                Thread.sleep(interval)
            }
            nextTick()
        }

        println(render())
        println(runningCarts.first())
    }
}