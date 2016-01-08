package com.zackehh.siphash;

import static com.zackehh.siphash.SipHashConstants.*;

/**
 * Main entry point for SipHash, providing a basic hash
 * interface. Assuming you have your full String to hash,
 * you can simply provide it to ${@link SipHash#hash(byte[])}.
 *
 * This class can be initialized and stored in case the
 * developer wishes to use the same key over and over again.
 *
 * This avoids the overhead of having to create the initial
 * key over and over again.
 *
 * <pre>
 * {@code
 * List<String> inputs = Arrays.asList("input1", "input2", "input3");
 * SipHash hasher = new SipHash("this key is mine".getBytes());
 * for (int i = 0; i < inputs.size(); i++) {
 *     hasher.hash(inputs.get(i));
 * }
 * }
 * </pre>
 */
public class SipHash {

    /**
     * The values of SipHash-c-d, to determine which of the SipHash
     * family we're using for this hash.
     */
    private final int c, d;

    /**
     * Initial seeded value of v0.
     */
    private final long v0;

    /**
     * Initial seeded value of v1.
     */
    private final long v1;

    /**
     * Initial seeded value of v2.
     */
    private final long v2;

    /**
     * Initial seeded value of v3.
     */
    private final long v3;

    /**
     * Accepts a 16 byte key input, and uses it to initialize
     * the state of the hash. This uses the default values of
     * c/d, meaning that we default to SipHash-2-4.
     *
     * @param key a 16 byte key input
     */
    public SipHash(byte[] key){
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
    public SipHash(byte[] key, int c, int d){
        this.c = c;
        this.d = d;

        SipHashKey hashKey = new SipHashKey(key);

        this.v0 = (INITIAL_V0 ^ hashKey.k0);
        this.v1 = (INITIAL_V1 ^ hashKey.k1);
        this.v2 = (INITIAL_V2 ^ hashKey.k0);
        this.v3 = (INITIAL_V3 ^ hashKey.k1);
    }

    /**
     * The basic hash implementation provided in the library.
     * Assuming you have your full input, you can provide it and
     * it will be hashed based on the values which were provided
     * to the constructor of this class.
     *
     * @param data the bytes to hash
     * @return a ${@link SipHashResult} instance
     */
    public SipHashResult hash(byte[] data) {
        SipHashDigest digest = new SipHashDigest(v0, v1, v2, v3, c, d);

        for (byte aData : data) {
            digest.update(aData);
        }

        return digest.finish();
    }

}