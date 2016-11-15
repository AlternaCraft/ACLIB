/*
 * Copyright (C) 2016 AlternaCraft
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.alternacraft.aclib;

import java.lang.reflect.Field;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.when;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author AlternaCraft
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class, PluginBase.class})
public class MessageManagerTest {

    static String error = null, info = null;
    String lastmessage = "";
    ConsoleCommandSender cs = null;
    Player player = null;

    public MessageManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        new MessageManager();
        
        for (Field f : MessageManager.class.getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (f.getName().equals("ERROR")) {
                    error = (String) f.get(null);
                } else if (f.getName().equals("INFO")) {
                    info = (String) f.get(null);
                }

            } catch (IllegalArgumentException | IllegalAccessException ex) {
                fail();
            }
        }
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        PowerMockito.mockStatic(Bukkit.class);
        PowerMockito.mockStatic(PluginBase.class);

        cs = mock(ConsoleCommandSender.class);
        when(Bukkit.getConsoleSender()).thenReturn(cs);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                lastmessage = (String) args[0];
                return null;
            }
        }).when(cs).sendMessage(anyString());

        player = mock(Player.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                lastmessage = (String) args[0];
                return null;
            }
        }).when(player).sendMessage(anyString());
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of log method, of class MessageManager.
     */
    @Test
    public void testLog() {
        String message = "test1";
        MessageManager.log(message);
        String expResult = MessageManager.prepareString(message);
        assertEquals(expResult, lastmessage);
    }

    /**
     * Test of logInfo method, of class MessageManager.
     */
    @Test
    public void testLogInfo() {
        String message = "test2";
        MessageManager.logInfo(message);
        String expResult = MessageManager.prepareString(info + message);
        assertEquals(expResult, lastmessage);
    }

    /**
     * Test of logError method, of class MessageManager.
     */
    @Test
    public void testLogError() {
        String message = "test3";
        MessageManager.logError(message);
        String expResult = MessageManager.prepareString(error + message);
        assertEquals(expResult, lastmessage);
    }

    /**
     * Test of sendPlayer method, of class MessageManager.
     */
    @Test
    public void testSendPlayer() {
        String message = "test4";
        MessageManager.sendPlayer(player, message);
        String expResult = MessageManager.prepareString(message);
        assertEquals(expResult, lastmessage);
    }

    /**
     * Test of sendCommandSender method, of class MessageManager.
     */
    @Test
    public void testSendCommandSender() {
        String message = "test5";
        MessageManager.sendCommandSender(cs, message);
        String expResult = MessageManager.prepareString(message);
        assertEquals(expResult, lastmessage);
    }

    /**
     * Test of prepareString method, of class MessageManager.
     */
    @Test
    public void testPrepareString() {
        String message = "Hola";
        String result = MessageManager.prepareString(message);
        String expResult = "null " + ChatColor.RESET + "Hola";
        assertEquals(expResult, result);
    }
}
