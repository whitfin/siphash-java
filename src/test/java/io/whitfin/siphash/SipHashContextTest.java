package io.whitfin.siphash;

import org.testng.annotations.Test;

/**
 * Test cases for the {@link SipHashContext} class.
 */
public class SipHashContextTest extends SipHashTest {

    /**
     * Tests invalid key exceptions are thrown.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExceptionOnInvalidKey() {
        SipHash.context(new byte[0]).hash(new byte[0]);
    }

    /**
     * Tests all vectors using the context hash implementation.
     */
    @Test
    public void testVectorsForContainerHash() {
        testVectors(new Hasher() {
            @Override
            public long hash(byte[] key, byte[] data) {
                return SipHash.context(key).hash(data);
            }
        });
    }
}
