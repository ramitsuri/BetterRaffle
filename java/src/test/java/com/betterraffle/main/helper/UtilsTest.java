package com.betterraffle.main.helper;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void testTokenGeneration() {
        int token = Utils.generateToken();
        Assert.assertEquals(Constants.START_TOKEN + 1, token);

        token = Utils.generateToken();
        Assert.assertEquals(Constants.START_TOKEN + 2, token);

        token = Utils.generateToken();
        Assert.assertEquals(Constants.START_TOKEN + 3, token);

        token = Utils.generateToken();
        Assert.assertEquals(Constants.START_TOKEN + 4, token);

        token = Utils.generateToken();
        Assert.assertEquals(Constants.START_TOKEN + 5, token);

        token = Utils.generateToken();
        Assert.assertEquals(Constants.START_TOKEN + 6, token);
    }

    @Test
    public void testRandomGeneration() {
        for (int i = 0; i < 10000; i++) {
            int random = Utils.pickRandomInt(100);
            System.out.println(random);
            Assert.assertTrue(random < 100);
            Assert.assertTrue(random >= 0);
        }
    }
}