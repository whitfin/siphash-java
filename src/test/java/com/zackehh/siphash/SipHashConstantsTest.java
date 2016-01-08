package com.zackehh.siphash;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class SipHashConstantsTest {

    @Test
    public void ensureAllConstants() throws Exception {
        Assert.assertEquals(SipHashConstants.INITIAL_V0, 0x736f6d6570736575L);
        Assert.assertEquals(SipHashConstants.INITIAL_V1, 0x646f72616e646f6dL);
        Assert.assertEquals(SipHashConstants.INITIAL_V2, 0x6c7967656e657261L);
        Assert.assertEquals(SipHashConstants.INITIAL_V3, 0x7465646279746573L);
        Assert.assertEquals(SipHashConstants.DEFAULT_C, 2);
        Assert.assertEquals(SipHashConstants.DEFAULT_D, 4);
        Assert.assertEquals(SipHashConstants.DEFAULT_CASE, SipHashCase.LOWER);
        Assert.assertEquals(SipHashConstants.DEFAULT_PADDING, false);
    }

    @Test(expectedExceptions = InvocationTargetException.class)
    public void ensureCannotInstance() throws Exception {
        Constructor<SipHashConstants> ctor = SipHashConstants.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        Assert.assertTrue(Modifier.isPrivate(ctor.getModifiers()));
        ctor.newInstance();
    }

}
