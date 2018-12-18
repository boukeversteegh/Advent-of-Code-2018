package day11

import util.Position
import util.p
import kotlin.streams.toList

data class Cell(val position: util.Position, val powerLevel: Int) {
    companion object {
        fun generate(serialNumber: Int, x: Int, y: Int): Cell {
            val rackID = x + 10
            var powerLevel = rackID * y
            powerLevel += serialNumber
            powerLevel *= rackID
            powerLevel = (powerLevel / 100) % 10
            powerLevel -= 5

            return Cell(position = Position(x, y), powerLevel = powerLevel)
        }
    }
}


open class PowerGrid(private val cells: Array<Array<Cell>>) {
    open fun calculatePower(x: Int, y: Int, size: Int): Int {
        return calculatePower(x, y, x + size - 1, y + size - 1)
    }

    open fun calculatePower(minX: Int, minY: Int, maxX: Int, maxY: Int): Int {
        var power = 0
        for (cellY in minY..maxY) {
            for (cellX in minX..maxX) {
                power += cells[cellY][cellX].powerLevel
            }
        }
        return power
    }

    fun maxGroupPower(size: Int): Pair<Position, Int>? {
        var maxPowerGroup: Position? = null
        var maxPower: Int? = null
        for (posY in 0..(width - size)) {
            for (posX in 0..(height - size)) {
                val power = calculatePower(posX, posY, size)
                if (maxPower == null || power > maxPower) {
                    maxPower = power
                    maxPowerGroup = p(posX, posY)
                }
            }
        }
        return if (maxPowerGroup != null && maxPower != null) {
            maxPowerGroup to maxPower
        } else {
            null
        }
    }

    /**
     * @return Triple<Position, power: Int, size: Int>
     */
    fun findMaxPowerGroup(): Triple<Position, Int, Int> {
        val sizes = 1..width

        val groupPowers = sizes.toList().stream().parallel().map { size ->
            val maxGroupPower = maxGroupPower(size)!!
            Triple(maxGroupPower.first, maxGroupPower.second, size)
        }.toList()

        return groupPowers.maxBy { triple -> triple.second }!!
    }

    val width: Int get() = cells[0].size
    val height: Int get() = cells.size

    companion object {
        fun generate(serialNumber: Int, width: Int, height: Int): PowerGrid {
            val cells: Array<Array<Cell>> = Array(height) { y ->
                Array(width) { x ->
                    Cell.generate(serialNumber, x, y)
                }
            }
            return OptimizedPowerGrid(cells)
        }

        fun generate(powerLevels: Array<Array<Int>>): PowerGrid {
            val cells = powerLevels.mapIndexed { y, row ->
                row.mapIndexed { x, powerLevel ->
                    Cell(p(x, y), powerLevel)
                }.toTypedArray()
            }.toTypedArray()
            return OptimizedPowerGrid(cells)
        }

    }
}

class OptimizedPowerGrid(cells: Array<Array<Cell>>) : PowerGrid(cells) {
    private val summedAreaTable: Array<Array<Long>> = Array(height) {
        Array(width) { 0L }
    }

    init {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val left = if (x > 0) summedAreaTable[y][x - 1] else 0
                val above = if (y > 0) summedAreaTable[y - 1][x] else 0
                val leftAbove = if (x > 0 && y > 0) summedAreaTable[y - 1][x - 1] else 0
                summedAreaTable[y][x] = left + above - leftAbove + cells[y][x].powerLevel
            }
        }
    }

    override fun calculatePower(minX: Int, minY: Int, maxX: Int, maxY: Int): Int {
        val bottomRight = summedAreaTable[maxY][maxX]
        val topLeft = if (minY >0 && minX > 0) summedAreaTable[minY-1][minX-1] else 0
        val topRight = if (minY > 0) summedAreaTable[minY - 1][maxX] else 0
        val bottomLeft = if (minX > 0) summedAreaTable[maxY][minX - 1] else 0
        return (bottomRight + topLeft - bottomLeft - topRight).toInt()
    }
}