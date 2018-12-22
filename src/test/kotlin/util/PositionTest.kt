package util

import org.junit.Test

class PositionTest {
    @Test
    fun compare() {
        assert(p(0, 0) == p(0, 0))
        assert(p(0, 0) < p(1, 0))
        assert(p(0, 0) < p(0, 1))
        assert(p(0, 0) < p(1, 1))

        assert(p(1, 0) > p(0, 0))
        assert(p(0, 1) > p(0, 0))
        assert(p(1, 1) > p(0, 0))

        assert(p(10, 0) < p(0, 1))
    }
}