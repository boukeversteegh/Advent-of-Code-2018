package day13

import org.junit.Assert.assertEquals
import org.junit.Test

class DirectionTest {
    @Test
    fun turnRight() {
        assertEquals(Direction.RIGHT, Direction.UP.turn(Rotation.RIGHT))
    }
}