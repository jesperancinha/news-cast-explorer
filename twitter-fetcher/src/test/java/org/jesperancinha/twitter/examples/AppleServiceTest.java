package org.jesperancinha.twitter.examples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppleServiceTest {

    @Test
    void givenApples_when3Fridge6Market_thenReturn9() {
        final AppleService appleServiceMock = new AppleService(){
            @Override
            public int getAppleFromFrige() {
                return 3;
            }

            @Override
            public int getAppleFromSupermarket() {
                return 6;
            }
        };

        assertEquals(9, appleServiceMock.getTotalApples());
    }
}