package day11

import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import util.p

class PowerGridTest {
    @Ignore
    @Test
    fun day11A() {
        val grid = PowerGrid.generate(serialNumber = 8141, width = 300, height = 300)
        println(grid.maxGroupPower(3))
    }

    @Ignore
    @Test
    fun day11B() {
        val grid = PowerGrid.generate(serialNumber = 8141, width = 300, height = 300)

        println(grid.findMaxPowerGroup())
    }

    @Test
    fun generateGrid() {
        val grid = PowerGrid.generate(serialNumber = 1234, width = 10, height = 10)

        Assert.assertEquals(grid.width, 10)
        Assert.assertEquals(grid.height, 10)
    }

    @Test
    fun generateGridFromPowerLevels() {
        val grid = PowerGrid.generate(powerLevels = arrayOf(
            arrayOf(0, 1, 0),
            arrayOf(5, 1, 2),
            arrayOf(2, 1, -1)
        ))

        Assert.assertEquals(grid.width, 3)
        Assert.assertEquals(grid.height, 3)
    }


    @Test
    fun totalPowerOfCell() {
        val grid = PowerGrid.generate(powerLevels = arrayOf(
            arrayOf(-15)
        ))
        val power: Int = grid.calculatePower(x = 0, y = 0, size = 1)

        Assert.assertEquals(power, -15)
    }

    @Test
    fun totalPower() {
        val grid = PowerGrid.generate(powerLevels = arrayOf(
            arrayOf(0, -1, 0),
            arrayOf(5, 1, 0),
            arrayOf(-5, 1, 0)
        ))
        val power: Int = grid.calculatePower(x = 0, y = 0, size = 3)

        Assert.assertEquals(power, 1)
    }

    @Test
    fun totalPowerByMinMax() {
        val grid = PowerGrid.generate(powerLevels = arrayOf(
            arrayOf(0, -1, 0),
            arrayOf(5, 1, 0),
            arrayOf(-5, 1, 0)
        ))
        val power: Int = grid.calculatePower(0, 0, 1, 1)

        Assert.assertEquals(power, 5)
    }

    @Test
    fun totalPower1by1() {
        val grid = PowerGrid.generate(powerLevels = arrayOf(
            arrayOf(3, -1, 0),
            arrayOf(5, 1, 0),
            arrayOf(-5, 1, 0)
        ))
        val power: Int = grid.calculatePower(0, 0, 0, 0)

        Assert.assertEquals(power, 3)
    }

    @Test
    fun totalPower1by1offset() {
        val grid = PowerGrid.generate(powerLevels = arrayOf(
            arrayOf(3, -1, 0),
            arrayOf(5, 1, 0),
            arrayOf(-5, 1, 0)
        ))
        val power: Int = grid.calculatePower(0, 1, 2, 2)

        Assert.assertEquals(power, 2)
    }

    @Test
    fun highestPowerGroup() {
        val grid = PowerGrid.generate(powerLevels = arrayOf(
            arrayOf(0, -1, 0),
            arrayOf(5, 2, 4),
            arrayOf(-5, 3, 0)
        ))
        val group = grid.maxGroupPower(size = 2)

        Assert.assertEquals(p(1, 1) to (2 + 3 + 4), group)
    }


    @Test
    fun highestPowerGroupExample() {
        val grid = PowerGrid.generate(18, 300, 300)
        val (position, power, size) = grid.findMaxPowerGroup()

        Assert.assertEquals(p(89 + 1, 268 + 1), position)
        Assert.assertEquals(113, power)
        Assert.assertEquals(16, size)
    }
}