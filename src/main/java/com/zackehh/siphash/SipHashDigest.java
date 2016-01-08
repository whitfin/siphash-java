package com.zackehh.siphash;

import static com.zackehh.siphash.SipHashConstants.DEFAULT_C;
import static com.zackehh.siphash.SipHashConstants.DEFAULT_D;

/**
 * A streaming implementation of SipHash, to be used when
 * you don't have all input available at the same time. Chunks
 * of bytes can be applied as they're received, and will be hashed
 * accordingly.
 *
 * As with ${@link SipHash}, the compression and finalization rounds
 * can be customized.
 */
public class SipHashDigest {

    /**
     * The values of SipHash-c-d, to determine which of the SipHash
     * family we're using for this hash.
     */
    private final int c, d;

    /**
     * Initial seeded value of v0.
     */
    private long v0;

    /**
     * Initial seeded value of v1.
     */
    private long v1;

    /**
     * Initial seeded value of v2.
     */
    private long v2;

    /**
     * Initial seeded value of v3.
     */
    private long v3;

    /**
     * A counter to keep track of the length of the input.
     */
    private byte input_len = 0;

    /**
     * A counter to keep track of the current position inside
     * of a chunk of bytes. Seeing as bytes are applied in chunks
     * of 8, this is necessary.
     */
    private int m_idx = 0;

    /**
     * The `m` value from the SipHash algorithm. Every 8 bytes, this
     * value will be applied to the current state of the hash.
     */
    private long m;

    /**
     * Accepts a 16 byte key input, and uses it to initialize
     * the state of the hash. This uses the default values of
     * c/d, meaning that we default to SipHash-2-4.
     *
     * @param key a 16 byte key input
     */
    public SipHashDigest(byte[] key) {
        this(key, DEFAULT_C, DEFAULT_D);
    }

    /**
     * Accepts a 16 byte key input, and uses it to initialize
     * the state of the hash. This constructor allows for
     * providing the c/d values, allowing the developer to
     * select any of the SipHash family to use for hashing.
     *
     * @param key a 16 byte key input
     * @param c the number of compression rounds
     * @param d the number of finalization rounds
     */
    public SipHashDigest(byte[] key, int c, int d) {
        this.c = c;
        this.d = d;

        SipHashKey hashKey = new SipHashKey(key);

        this.v0 = SipHashConstants.INITIAL_V0 ^ hashKey.k0;
        this.v1 = SipHashConstants.INITIAL_V1 ^ hashKey.k1;
        this.v2 = SipHashConstants.INITIAL_V2 ^ hashKey.k0;
        this.v3 = SipHashConstants.INITIAL_V3 ^ hashKey.k1;
    }

    /**
     * This constructor is used by the ${@link SipHash} implementation,
     * and takes an initial (seeded) value of v0/v1/v2/v3. This is used
     * when the key has been pre-calculated. This constructor also
     * receives the values of `c` and `d` to use in this hash.
     *
     * @param v0 an initial seeded v0
     * @param v1 an initial seeded v1
     * @param v2 an initial seeded v2
     * @param v3 an initial seeded v3
     * @param c the number of compression rounds
     * @param d the number of finalization rounds
     */
    SipHashDigest(long v0, long v1, long v2, long v3, int c, int d) {
        this.c = c;
        this.d = d;

        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    /**
     * Updates the current state of the hash with a single byte. This
     * is the streaming implementation which shifts as required to ensure
     * we can take an arbitrary number of bytes at any given time. We only
     * apply the block once the index (`m_idx`) has reached 8. The number
     * of compression rounds is determined by the `c` value passed in by
     * the developer.
     *
     * This method returns this instance, as a way of allowing the developer
     * to chain.
     *
     * @return a ${@link SipHashDigest} instance
     */
    public SipHashDigest update(byte b) {
        input_len++;
        m |= (((long) b & 0xff) << (m_idx * 8));
        m_idx++;
        if (m_idx >= 8) {
            v3 ^= m;
            for (int i = 0; i < c; i++) {
                round();
            }
            v0 ^= m;
            m_idx = 0;
            m = 0;
        }
        return this;
    }

    /**
     * A convenience method to allow passing a chunk of bytes at once, rather
     * than a byte at a time.
     *
     * @return a ${@link SipHashDigest} instance
     */
    public SipHashDigest update(byte[] bytes) {
        for (byte b : bytes) {
            update(b);
        }
        return this;
    }

    /**
     * Finalizes the hash by padding 0s until the next multiple of
     * 8 (as we operate in 8 byte chunks). The last byte added to
     * the hash is the length of the input, which we keep inside the
     * `input_len` counter. The number of rounds is based on the value
     * of `d` as specified by the developer.
     *
     * This method returns a ${@link SipHashResult}, as no further updates
     * should occur (i.e. the lack of chaining here shows we're done).
     *
     * @return a ${@link SipHashResult} instance
     */
    public SipHashResult finish() {
        byte msgLenMod256 = input_len;

        while (m_idx < 7) {
            update((byte) 0);
        }
        update(msgLenMod256);

        v2 ^= 0xff;
        for (int i = 0; i < d; i++) {
            round();
        }

        return new SipHashResult(v0 ^ v1 ^ v2 ^ v3);
    }

    /**
     * Performs the equivalent of SipRound on the provided state.
     * This method affects the state of this digest, in that it
     * mutates the v states directly.
     */
    private void round() {
        v0 += v1;
        v2 += v3;
        v1 = rotateLeft(v1, 13);
        v3 = rotateLeft(v3, 16);

        v1 ^= v0;
        v3 ^= v2;
        v0 = rotateLeft(v0, 32);

        v2 += v1;
        v0 += v3;
        v1 = rotateLeft(v1, 17);
        v3 = rotateLeft(v3, 21);

        v1 ^= v2;
        v3 ^= v0;
        v2 = rotateLeft(v2, 32);
    }

    /**
     * Rotates an input number `val` left by `shift` number of bits. Bits which are
     * pushed off to the left are rotated back onto the right, making this a left
     * rotation (a circular shift).
     *
     * @param val the value to be shifted
     * @param shift how far left to shift
     * @return a long value once shifted
     */
    private long rotateLeft(long val, int shift) {
        return (val << shift) | val >>> (64 - shift);
    }

}
