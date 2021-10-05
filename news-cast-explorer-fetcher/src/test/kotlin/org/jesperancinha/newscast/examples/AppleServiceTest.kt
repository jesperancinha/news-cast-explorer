package org.jesperancinha.newscast.examples

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class AppleServiceTest {
    @Test
    fun givenApples_when3Fridge6Market_thenReturn9() {
        val appleServiceMock: AppleService = object : AppleService() {
            override val appleFromFrige: Int
                get() = 3
            override val appleFromSupermarket: Int
                get() = 6
        }
        Assertions.assertEquals(9, appleServiceMock.totalApples)
    }
}