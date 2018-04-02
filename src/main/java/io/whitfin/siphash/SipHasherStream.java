package io.whitfin.siphash;

import static io.whitfin.siphash.SipHasher.*;

/**
 * Streaming implementation of the SipHash algorithm.
 *
 * This implementation is slower than the 0A implementation, but allows for
 * unknown input this.lengths to enable hashing as more data is received. Chunks
 * are processed in 8-byte blocks in the SipHash algorithms, so if you're
 * using huge input, you should expect slowness.
 *
 * Although this implementation requires an initial allocation, there are
 * no further allocations - so memory should prove similar to the non-streaming
 * implementation.
 */
public final class SipHasherStream {

    /**
     * The specified rounds of C compression.
     */
    private final int c;

    /**
     * The specified rounds of D compression.
     */
    private final int d;

    /**
     * Counter to keep track of the input
     */
    private byte len;

    /**
     * Index to keep track of chunk positioning.
     */
    private int m_idx;

    /**
     * The current value for the m number.
     */
    private long m;

    /**
     * The current value for the this.v0 number.
     */
    private long v0;

    /**
     * The current value for the this.v1 number.
     */
    private long v1;

    /**
     * The current value for the this.v2 number.
     */
    private long v2;

    /**
     * The current value for the this.v3 number.
     */
    private long v3;

    /**
     * Initializes a streaming digest using a key and compression rounds.
     *
     * @param key
     *      the key to use to seed this hash container.
     * @param c
     *      the desired rounds of C compression.
     * @param d
     *      the desired rounds of D compression.
     */
    SipHasherStream(byte[] key, int c, int d) {
        if (key.length != 16) {
            throw new IllegalArgumentException("Key must be exactly 16 bytes!");
        }

        long k0 = bytesToLong(key, 0);
        long k1 = bytesToLong(key, 8);

        this.v0 = INITIAL_V0 ^ k0;
        this.v1 = INITIAL_V1 ^ k1;
        this.v2 = INITIAL_V2 ^ k0;
        this.v3 = INITIAL_V3 ^ k1;

        this.c = c;
        this.d = d;

        this.m = 0;
        this.len = 0;
        this.m_idx = 0;
    }

    /**
     * Updates the hash with a single byte.
     *
     * This will only modify the internal `m` value, nothing will be modified
     * in the actual `v*` states until an 8-byte block has been provided.
     *
     * @param b
     *      the byte being added to the digest.
     * @return
     *      the same {@link SipHasherStream} for chaining.
     */
    public final SipHasherStream update(byte b) {
        this.len++;
        this.m |= (((long) b & 0xff) << (this.m_idx++ * 8));
        if (this.m_idx < 8) {
            return this;
        }
        this.v3 ^= this.m;
        for (int i = 0; i < this.c; i++) {
            round();
        }
        this.v0 ^= this.m;
        this.m_idx = 0;
        this.m = 0;
        return this;
    }

    /**
     * Updates the hash with an array of bytes.
     *
     * @param bytes
     *      the bytes being added to the digest.
     * @return
     *      the same {@link SipHasherStream} for chaining.
     */
    public final SipHasherStream update(byte[] bytes) {
        for (byte b : bytes) {
            update(b);
        }
        return this;
    }

    /**
     * Finalizes the digest and returns the hash.
     *
     * This works by padding to the next 8-byte block, before applying
     * the compression rounds once more - but this time using D rounds
     * of compression rather than C.
     *
     * @return
     *      the final result of the hash as a long.
     */
    public final long digest() {
        byte msgLenMod256 = this.len;

        while (this.m_idx < 7) {
            update((byte) 0);
        }
        update(msgLenMod256);

        this.v2 ^= 0xff;
        for (int i = 0; i < this.d; i++) {
            round();
        }

        return this.v0 ^ this.v1 ^ this.v2 ^ this.v3;
    }

    /**
     * SipRound implementation for internal use.
     */
    private void round() {
        this.v0 += this.v1;
        this.v2 += this.v3;
        this.v1 = rotateLeft(this.v1, 13);
        this.v3 = rotateLeft(this.v3, 16);

        this.v1 ^= this.v0;
        this.v3 ^= this.v2;
        this.v0 = rotateLeft(this.v0, 32);

        this.v2 += this.v1;
        this.v0 += this.v3;
        this.v1 = rotateLeft(this.v1, 17);
        this.v3 = rotateLeft(this.v3, 21);

        this.v1 ^= this.v2;
        this.v3 ^= this.v0;
        this.v2 = rotateLeft(this.v2, 32);
    }
}
