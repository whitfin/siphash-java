package io.whitfin.siphash;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test cases for the {@link SipHasher} class.
 *
 * This also contains reference methods which can be used by other
 * implementations in order to make sure of the same vector bootstrap.
 */
public class SipHasherTest {

    // test vectors via https://www.131002.net/siphash/siphash24.c
    private static final long[] EXPECTED = new long[] {
        0x726fdb47dd0e0e31L, 0x74f839c593dc67fdL, 0x0d6c8009d9a94f5aL, 0x85676696d7fb7e2dL,
        0xcf2794e0277187b7L, 0x18765564cd99a68dL, 0xcbc9466e58fee3ceL, 0xab0200f58b01d137L,
        0x93f5f5799a932462L, 0x9e0082df0ba9e4b0L, 0x7a5dbbc594ddb9f3L, 0xf4b32f46226bada7L,
        0x751e8fbc860ee5fbL, 0x14ea5627c0843d90L, 0xf723ca908e7af2eeL, 0xa129ca6149be45e5L,
        0x3f2acc7f57c29bdbL, 0x699ae9f52cbe4794L, 0x4bc1b3f0968dd39cL, 0xbb6dc91da77961bdL,
        0xbed65cf21aa2ee98L, 0xd0f2cbb02e3b67c7L, 0x93536795e3a33e88L, 0xa80c038ccd5ccec8L,
        0xb8ad50c6f649af94L, 0xbce192de8a85b8eaL, 0x17d835b85bbb15f3L, 0x2f2e6163076bcfadL,
        0xde4daaaca71dc9a5L, 0xa6a2506687956571L, 0xad87a3535c49ef28L, 0x32d892fad841c342L,
        0x7127512f72f27cceL, 0xa7f32346f95978e3L, 0x12e0b01abb051238L, 0x15e034d40fa197aeL,
        0x314dffbe0815a3b4L, 0x027990f029623981L, 0xcadcd4e59ef40c4dL, 0x9abfd8766a33735cL,
        0x0e3ea96b5304a7d0L, 0xad0c42d6fc585992L, 0x187306c89bc215a9L, 0xd4a60abcf3792b95L,
        0xf935451de4f21df2L, 0xa9538f0419755787L, 0xdb9acddff56ca510L, 0xd06c98cd5c0975ebL,
        0xe612a3cb9ecba951L, 0xc766e62cfcadaf96L, 0xee64435a9752fe72L, 0xa192d576b245165aL,
        0x0a8787bf8ecb74b2L, 0x81b3e73d20b49b6fL, 0x7fa8220ba3b2eceaL, 0x245731c13ca42499L,
        0xb78dbfaf3a8d83bdL, 0xea1ad565322a1a0bL, 0x60e61c23a3795013L, 0x6606d7e446282b93L,
        0x6ca4ecb15c5f91e1L, 0x9f626da15c9625f3L, 0xe51b38608ef25f57L, 0x958a324ceb064572L
    };

    /**
     * Tests constructor usage to suppress Jacoco.
     */
    @Test
    public void testConstructorUsage() {
        new SipHasher();
    }

    /**
     * Tests all vectors using the 0A hash implementation.
     */
    @Test
    public void testVectorsForZeroAllocHash() {
        testVectors(new Hasher() {
            @Override
            public long hash(byte[] key, byte[] data) {
                return SipHasher.hash(key, data);
            }
        });
    }

    /**
     * Tests conversion of hashes to hexidecimal.
     *
     * This makes sure to validate results which may need padding
     * to the left with 0s in order to fill to 16 bytes.
     */
    @Test
    public void testConversionToHexString() {
        long hash1 = -3891084581787974112L;
        long hash2 =    77813817455948350L;

        String hex1 = SipHasher.toHexString(hash1);
        String hex2 = SipHasher.toHexString(hash2);

        Assert.assertEquals(hex1, "ca0017304f874620");
        Assert.assertEquals(hex2, "011473413414323e");
    }

    /**
     * Tests invalid key exceptions are thrown.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExceptionOnInvalidKey() {
        SipHasher.hash(new byte[0], new byte[0]);
    }

    /**
     * Vector test implementation to allow passing a hasher
     * to avoid vector boilerplate.
     *
     * @param hasher
     *      the hasher implementation used to generate a hash.
     */
    void testVectors(Hasher hasher) {
        byte[] key = new byte[16];
        for (int i = 0; i < 16; i++) {
            key[i] = (byte) i;
        }

        for (int i = 0; i < EXPECTED.length; i++) {
            byte[] data = new byte[i];
            for (int j = 0; j < i; j++) {
                data[j] = (byte) j;
            }

            long expected = EXPECTED[i];
            long actual = hasher.hash(key, data);

            Assert.assertEquals(actual, expected);
        }
    }

    /**
     * Simple interface for use with {@link #testVectors(Hasher)}.
     */
    interface Hasher {

        /**
         * Given a key and input data, return a hash.
         *
         * @param key
         *      the key to seed the hash with.
         * @param data
         *      the data being hashed.
         * @return
         *      a long representation of a hash.
         */
        long hash(byte[] key, byte[] data);
    }
}
