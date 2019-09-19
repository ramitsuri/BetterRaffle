package com.betterraffle.main.db;

import com.betterraffle.main.entities.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SQLHelperTest {

    @Before
    public void setUp() throws Exception {
        SQLHelper.getInstance();
    }

    @Test
    public void testGetUser() {
        User user = SQLHelper.getUser(1001);
        assertEquals(1001, user.getId());
        assertEquals("ramitsuri", user.getName());
        assertEquals("3527459856", user.getPhone());
        assertEquals("9944108", user.getToken());

        user = SQLHelper.getUser(1002);
        assertEquals(1002, user.getId());
        assertEquals("test2", user.getName());
        assertEquals("1234567895", user.getPhone());
        assertEquals("9944109", user.getToken());
    }

    @Test
    public void testWinnerPick() {
        User user = SQLHelper.pickWinner();
        assertNotNull(user);
        assertNotNull(user.getToken());
        System.out.println(user.getToken());
    }

    @Test
    public void testFillToken() {
        String token = SQLHelper.fillToken(1001);
        User user = SQLHelper.getUser(1001);
        assertEquals(token, user.getToken());

        token = SQLHelper.fillToken(1002);
        user = SQLHelper.getUser(1002);
        assertEquals(token, user.getToken());

        token = SQLHelper.fillToken(1004);
        user = SQLHelper.getUser(1004);
        assertEquals(token, user.getToken());

        token = SQLHelper.fillToken(1006);
        user = SQLHelper.getUser(1006);
        assertEquals(token, user.getToken());
    }

    @Test
    public void testResetTokens() {
        SQLHelper.resetTokens();
        User user = SQLHelper.getUser(1001);
        assertNull(user.getToken());

        user = SQLHelper.getUser(1002);
        assertNull(user.getToken());

        user = SQLHelper.getUser(1004);
        assertNull(user.getToken());

        user = SQLHelper.getUser(1006);
        assertNull(user.getToken());
    }
}