package com.zackehh.siphash;

/**
 * A container class to store both k0 and k1. These
 * values are created from a 16 byte key passed into
 * the constructor. This isn't ideal as it's another
 * alloc, but it'll do for now.
 */
class SipHashKey {

    /**
     * The value of k0.
     */
    final long k0;

    /**
     * The value of k1.
     */
    final long k1;

    /**
     * Accepts a 16 byte input key and converts the
     * first and last 8 byte chunks to little-endian.
     * These values become k0 and k1.
     *
     * @param key the 16 byte key input
     */
    public SipHashKey(byte[] key) {
        if (key.length != 16) {
            throw new IllegalArgumentException("Key must be exactly 16 bytes.");
        }
        this.k0 = bytesToLong(key, 0);
        this.k1 = bytesToLong(key, 8);
    }

    /**
     * Converts a chunk of 8 bytes to a number in little-endian
     * format. Accepts an offset to determine where the chunk
     * begins in the byte array.
     *
     * @param b our byte array
     * @param offset the index to start at
     * @return a little-endian long representation
     */
    private static long bytesToLong(byte[] b, int offset) {
        long m = 0;
        for (int i = 0; i < 8; i++) {
            m |= ((((long) b[i + offset]) & 0xff) << (8 * i));
        }
        return m;
    }

}
