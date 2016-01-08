package com.zackehh.siphash;

/**
 * Class containing several constants for use alongside
 * hashing. Fields such as initial states and defaults,
 * as they will not change throughout hashing.
 */
class SipHashConstants {

    /**
     * Initial magic number for v0.
     */
    static final long INITIAL_V0 = 0x736f6d6570736575L;

    /**
     * Initial magic number for v1.
     */
    static final long INITIAL_V1 = 0x646f72616e646f6dL;

    /**
     * Initial magic number for v2.
     */
    static final long INITIAL_V2 = 0x6c7967656e657261L;

    /**
     * Initial magic number for v3.
     */
    static final long INITIAL_V3 = 0x7465646279746573L;

    /**
     * The default number of rounds of compression during per block.
     * This defaults to 2 as the default implementation is SipHash-2-4.
     */
    static final int DEFAULT_C = 2;

    /**
     * The default number of rounds of compression during finalization.
     * This defaults to 4 as the default implementation is SipHash-2-4.
     */
    static final int DEFAULT_D = 4;

    /**
     * Whether or not we should pad any hashes by default.
     */
    static final boolean DEFAULT_PADDING = false;

    /**
     * The default String casing for any output Hex Strings. We default
     * to lower case as it's the least expensive path.
     */
    static final SipHashCase DEFAULT_CASE = SipHashCase.LOWER;

}
