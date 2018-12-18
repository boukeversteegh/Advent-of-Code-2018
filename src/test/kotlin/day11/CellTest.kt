package day11

import org.junit.Assert
import org.junit.Test
import util.Position

class CellTest {
    @Test
    fun generateCell() {
        val cell: Cell = Cell.generate(serialNumber = 8, x = 3, y = 5)

        Assert.assertEquals(Cell(Position(3, 5), powerLevel = 4), cell)
    }
}