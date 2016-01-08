package com.zackehh.siphash;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SipHashDigestTest {

    @Test
    public void initializeDigestWithKey() throws Exception {
        SipHashDigest sipHash = new SipHashDigest("0123456789ABCDEF".getBytes());

        long v0 = SipHashTestUtils.getPrivateField(sipHash, "v0", Long.class);
        long v1 = SipHashTestUtils.getPrivateField(sipHash, "v1", Long.class);
        long v2 = SipHashTestUtils.getPrivateField(sipHash, "v2", Long.class);
        long v3 = SipHashTestUtils.getPrivateField(sipHash, "v3", Long.class);

        Assert.assertEquals(v0, 4925064773550298181L);
        Assert.assertEquals(v1, 2461839666708829781L);
        Assert.assertEquals(v2, 6579568090023412561L);
        Assert.assertEquals(v3, 3611922228250500171L);

        int c = SipHashTestUtils.getPrivateField(sipHash, "c", Integer.class);
        int d = SipHashTestUtils.getPrivateField(sipHash, "d", Integer.class);

        Assert.assertEquals(c, SipHashConstants.DEFAULT_C);
        Assert.assertEquals(d, SipHashConstants.DEFAULT_D);
    }

    @Test
    public void initializeDigestWithKeyAndCD() throws Exception {
        SipHashDigest sipHash = new SipHashDigest("0123456789ABCDEF".getBytes(), 4, 8);

        long v0 = SipHashTestUtils.getPrivateField(sipHash, "v0", Long.class);
        long v1 = SipHashTestUtils.getPrivateField(sipHash, "v1", Long.class);
        long v2 = SipHashTestUtils.getPrivateField(sipHash, "v2", Long.class);
        long v3 = SipHashTestUtils.getPrivateField(sipHash, "v3", Long.class);

        Assert.assertEquals(v0, 4925064773550298181L);
        Assert.assertEquals(v1, 2461839666708829781L);
        Assert.assertEquals(v2, 6579568090023412561L);
        Assert.assertEquals(v3, 3611922228250500171L);

        int c = SipHashTestUtils.getPrivateField(sipHash, "c", Integer.class);
        int d = SipHashTestUtils.getPrivateField(sipHash, "d", Integer.class);

        Assert.assertEquals(c, 4);
        Assert.assertEquals(d, 8);
    }

    @Test
    public void initializeDigestWithKeyThenHash() throws Exception {
        SipHashDigest sipHash = new SipHashDigest("0123456789ABCDEF".getBytes());

        long v0 = SipHashTestUtils.getPrivateField(sipHash, "v0", Long.class);
        long v1 = SipHashTestUtils.getPrivateField(sipHash, "v1", Long.class);
        long v2 = SipHashTestUtils.getPrivateField(sipHash, "v2", Long.class);
        long v3 = SipHashTestUtils.getPrivateField(sipHash, "v3", Long.class);

        Assert.assertEquals(v0, 4925064773550298181L);
        Assert.assertEquals(v1, 2461839666708829781L);
        Assert.assertEquals(v2, 6579568090023412561L);
        Assert.assertEquals(v3, 3611922228250500171L);

        int c = SipHashTestUtils.getPrivateField(sipHash, "c", Integer.class);
        int d = SipHashTestUtils.getPrivateField(sipHash, "d", Integer.class);

        Assert.assertEquals(c, SipHashConstants.DEFAULT_C);
        Assert.assertEquals(d, SipHashConstants.DEFAULT_D);

        for(byte b : "zymotechnics".getBytes()){
            sipHash.update(b);
        }

        SipHashResult hashResult = sipHash.finish();

        Assert.assertEquals(hashResult.get(), 699588702094987020L);
        Assert.assertEquals(hashResult.getHex(), "9b57037cd3f8f0c");
        Assert.assertEquals(hashResult.getHex(true), "09b57037cd3f8f0c");
        Assert.assertEquals(hashResult.getHex(SipHashCase.UPPER), "9B57037CD3F8F0C");
        Assert.assertEquals(hashResult.getHex(true, SipHashCase.UPPER), "09B57037CD3F8F0C");
    }

    @Test
    public void initializeStateWithKeyAndCDThenHash() throws Exception {
        SipHashDigest sipHash = new SipHashDigest("0123456789ABCDEF".getBytes(), 4, 8);

        long v0 = SipHashTestUtils.getPrivateField(sipHash, "v0", Long.class);
        long v1 = SipHashTestUtils.getPrivateField(sipHash, "v1", Long.class);
        long v2 = SipHashTestUtils.getPrivateField(sipHash, "v2", Long.class);
        long v3 = SipHashTestUtils.getPrivateField(sipHash, "v3", Long.class);

        Assert.assertEquals(v0, 4925064773550298181L);
        Assert.assertEquals(v1, 2461839666708829781L);
        Assert.assertEquals(v2, 6579568090023412561L);
        Assert.assertEquals(v3, 3611922228250500171L);

        int c = SipHashTestUtils.getPrivateField(sipHash, "c", Integer.class);
        int d = SipHashTestUtils.getPrivateField(sipHash, "d", Integer.class);

        Assert.assertEquals(c, 4);
        Assert.assertEquals(d, 8);

        for(byte b : "zymotechnics".getBytes()){
            sipHash.update(b);
        }

        SipHashResult hashResult = sipHash.finish();

        Assert.assertEquals(hashResult.get(), -3891084581787974112L); // overflow
        Assert.assertEquals(hashResult.getHex(), "ca0017304f874620");
        Assert.assertEquals(hashResult.getHex(true), "ca0017304f874620");
        Assert.assertEquals(hashResult.getHex(SipHashCase.UPPER), "CA0017304F874620");
        Assert.assertEquals(hashResult.getHex(true, SipHashCase.UPPER), "CA0017304F874620");
    }

    @Test
    public void updateWithAByteArrayChunk() throws Exception {
        SipHashDigest sipHash = new SipHashDigest("0123456789ABCDEF".getBytes());

        long v0 = SipHashTestUtils.getPrivateField(sipHash, "v0", Long.class);
        long v1 = SipHashTestUtils.getPrivateField(sipHash, "v1", Long.class);
        long v2 = SipHashTestUtils.getPrivateField(sipHash, "v2", Long.class);
        long v3 = SipHashTestUtils.getPrivateField(sipHash, "v3", Long.class);

        Assert.assertEquals(v0, 4925064773550298181L);
        Assert.assertEquals(v1, 2461839666708829781L);
        Assert.assertEquals(v2, 6579568090023412561L);
        Assert.assertEquals(v3, 3611922228250500171L);

        int c = SipHashTestUtils.getPrivateField(sipHash, "c", Integer.class);
        int d = SipHashTestUtils.getPrivateField(sipHash, "d", Integer.class);

        Assert.assertEquals(c, SipHashConstants.DEFAULT_C);
        Assert.assertEquals(d, SipHashConstants.DEFAULT_D);

        sipHash.update("zymo".getBytes());
        sipHash.update("techni".getBytes());
        sipHash.update("cs".getBytes());

        SipHashResult hashResult = sipHash.finish();

        Assert.assertEquals(hashResult.get(), 699588702094987020L);
        Assert.assertEquals(hashResult.getHex(), "9b57037cd3f8f0c");
        Assert.assertEquals(hashResult.getHex(true), "09b57037cd3f8f0c");
        Assert.assertEquals(hashResult.getHex(SipHashCase.UPPER), "9B57037CD3F8F0C");
        Assert.assertEquals(hashResult.getHex(true, SipHashCase.UPPER), "09B57037CD3F8F0C");
    }
}
