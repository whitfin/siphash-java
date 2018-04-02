package io.whitfin.siphash;

import org.testng.annotations.Test;

/**
 * Test cases for the {@link SipHasherContainer} class.
 */
public class SipHasherContainerTest extends SipHasherTest {

    /**
     * Tests invalid key exceptions are thrown.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExceptionOnInvalidKey() {
        SipHasher.container(new byte[0]).hash(new byte[0]);
    }

    /**
     * Tests all vectors using the container hash implementation.
     */
    @Test
    public void testVectorsForContainerHash() {
        testVectors(new Hasher() {
            @Override
            public long hash(byte[] key, byte[] data) {
                return SipHasher.container(key).hash(data);
            }
        });
    }
}
