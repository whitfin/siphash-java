package io.whitfin.siphash;

import org.testng.annotations.Test;

/**
 * Test cases for the {@link SipHasherStream} class.
 */
public class SipHasherStreamTest extends SipHasherTest {

    /**
     * Tests invalid key exceptions are thrown.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExceptionOnInvalidKey() {
        SipHasher.init(new byte[0]);
    }

    /**
     * Tests all vectors using the streaming hash implementation.
     */
    @Test
    public void testVectorsForStreamHash() {
        testVectors(new Hasher() {
            @Override
            public long hash(byte[] key, byte[] data) {
                return SipHasher.init(key).update(data).digest();
            }
        });
    }
}
