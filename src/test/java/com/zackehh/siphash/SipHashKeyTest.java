package com.zackehh.siphash;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SipHashKeyTest {

    @Test
    public void initializeWithKey() throws Exception {
        SipHashKey key = new SipHashKey("0123456789ABCDEF".getBytes());

        long k0 = SipHashTestUtils.getPrivateField(key, "k0", Long.class);
        long k1 = SipHashTestUtils.getPrivateField(key, "k1", Long.class);

        Assert.assertEquals(k0, 3978425819141910832L);
        Assert.assertEquals(k1, 5063528411713059128L);
    }

    @Test(
        expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "Key must be exactly 16 bytes!"
    )
    public void initializeWithKeyTooLong() throws Exception {
        new SipHashKey("0123456789ABCDEFG".getBytes()); // 17 bytes
    }

}
