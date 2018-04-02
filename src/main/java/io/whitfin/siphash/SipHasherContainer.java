package io.whitfin.siphash;

import static io.whitfin.siphash.SipHasher.*;

/**
 * Small container of state to aid SipHash throughput.
 *
 * This will keep a constant key and seeded v* values for use across many
 * hashes. As such, this avoids a small amount of overhead on each hash which
 * might prove useful in the case you have constant keys (hash tables, etc).
 */
public final class SipHasherContainer {

    /**
     * The seeded value for the magic v0 number.
     */
    private final long v0;

    /**
     * The seeded value for the magic v1 number.
     */
    private final long v1;

    /**
     * The seeded value for the magic v2 number.
     */
    private final long v2;

    /**
     * The seeded value for the magic v3 number.
     */
    private final long v3;

    /**
     * Initializes a container from a key seed.
     *
     * @param key
     *      the key to use to seed this hash container.
     */
    SipHasherContainer(byte[] key) {
        if (key.length != 16) {
            throw new IllegalArgumentException("Key must be exactly 16 bytes!");
        }

        long k0 = bytesToLong(key, 0);
        long k1 = bytesToLong(key, 8);

        this.v0 = INITIAL_V0 ^ k0;
        this.v1 = INITIAL_V1 ^ k1;
        this.v2 = INITIAL_V2 ^ k0;
        this.v3 = INITIAL_V3 ^ k1;
    }

    /**
     * Hashes input data using the preconfigured state.
     *
     * @param data
     *      the data to hash and digest.
     * @return
     *      a long value as the output of the hash.
     */
    public final long hash(byte[] data) {
        return SipHasher.hash(
            DEFAULT_C,
            DEFAULT_D,
            this.v0,
            this.v1,
            this.v2,
            this.v3,
            data
        );
    }

    /**
     * Hashes input data using the preconfigured state.
     *
     * @param data
     *      the data to hash and digest.
     * @param c
     *      the desired rounds of C compression.
     * @param d
     *      the desired rounds of D compression.
     * @return
     *      a long value as the output of the hash.
     */
    public final long hash(byte[] data, int c, int d) {
        return SipHasher.hash(
            c, d,
            this.v0,
            this.v1,
            this.v2,
            this.v3,
            data
        );
    }
}
