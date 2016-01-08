package com.zackehh.siphash;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SipHashCaseTest {

    @Test
    public void ensureAllValues() throws Exception {
        SipHashCase[] cases = SipHashCase.values();

        Assert.assertEquals(cases[0], SipHashCase.UPPER);
        Assert.assertEquals(cases[1], SipHashCase.LOWER);
    }

    @Test
    public void ensureValueOf() throws Exception {
        Assert.assertEquals(SipHashCase.valueOf("UPPER"), SipHashCase.UPPER);
        Assert.assertEquals(SipHashCase.valueOf("LOWER"), SipHashCase.LOWER);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void invalidCaseValueOf() throws Exception {
        SipHashCase.valueOf("invalid");
    }

}
