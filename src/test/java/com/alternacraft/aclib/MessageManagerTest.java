/*
 * Copyright (C) 2017 AlternaCraft
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

import com.alternacraft.aclib.utils.StringsUtils;
import java.lang.reflect.Field;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender.Spigot;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
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

    static String error = null, info = null, debug = null;
    String lastmessage = "";
    ConsoleCommandSender cs = null;
    Player player = null;

    public MessageManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        for (Field f : MessageManager.class.getDeclaredFields()) {
            f.setAccessible(true);
            try {
                switch (f.getName()) {
                    case "ERROR":
                        error = (String) f.get(new String());
                        break;
                    case "INFO":
                        info = (String) f.get(new String());
                        break;
                    case "DEBUG":
                        debug = (String) f.get(new String());
                        break;
                    default:
                        break;
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
        Spigot spigot = mock(Spigot.class);
        when(Bukkit.getConsoleSender()).thenReturn(cs);
        when(cs.spigot()).thenReturn(spigot);

        doAnswer((Answer<Void>) (InvocationOnMock invocation) -> {
            Object[] args = invocation.getArguments();
            lastmessage = ((String) args[0]);
            return null;
        }).when(cs).sendMessage(Matchers.anyString());

        doAnswer((Answer<Void>) (InvocationOnMock invocation) -> {
            Object[] args = invocation.getArguments();
            lastmessage = ((TextComponent) args[0]).toLegacyText();
            return null;
        }).when(spigot).sendMessage(Matchers.isA(TextComponent.class));
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
        String message = "test4";
        MessageManager.logError(message);
        String expResult = MessageManager.prepareString(error + message);
        assertEquals(expResult, lastmessage);
    }

    /**
     * Test of sendCommandSender method, of class MessageManager.
     */
    @Test
    public void testSendCommandSender() {
        String message = "test6";
        MessageManager.sendCommandSender(cs, message);
        String expResult = new TextComponent(TextComponent.fromLegacyText(
                MessageManager.prepareString(message))).toLegacyText();
        assertEquals(expResult, lastmessage);
    }

    /**
     * Test of sendMessage method, of class MessageManager.
     */
    @Test
    public void testSendMessage() {
        String message = "&6test6";
        MessageManager.sendMessage(cs, message);
        String expResult = new TextComponent(TextComponent.fromLegacyText(
                StringsUtils.translateColors(message))).toLegacyText();
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
