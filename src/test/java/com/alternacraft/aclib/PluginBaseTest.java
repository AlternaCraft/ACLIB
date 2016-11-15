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

import com.alternacraft.aclib.config.ConfigDataInterface;
import com.alternacraft.aclib.config.ConfigurationFile;
import com.alternacraft.aclib.langs.Langs;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author AlternaCraft
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JavaPlugin.class, ConfigurationFile.class, PluginDescription.class})
public class PluginBaseTest {

    PluginBase base;
    JavaPlugin plugin;
    ConfigurationFile cfile;
    PluginDescription pdesc;

    public PluginBaseTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {        
        plugin = PowerMockito.mock(JavaPlugin.class);
        PowerMockito.when(plugin.getDataFolder()).thenReturn(null);
        cfile = mock(ConfigurationFile.class);
        PowerMockito.whenNew(ConfigurationFile.class).withAnyArguments().thenReturn(cfile);
        pdesc = mock(PluginDescription.class);
        PowerMockito.whenNew(PluginDescription.class).withAnyArguments().thenReturn(pdesc);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of init method, of class PluginBase.
     */
    @Test
    public void testInit_JavaPlugin() {
        PluginBase.INSTANCE.init(plugin);
        assertNotNull(PluginBase.INSTANCE.plugin());
        assertNotNull(PluginBase.INSTANCE.config());
        assertNotNull(PluginBase.INSTANCE.pluginDescription());
    }

    /**
     * Test of init method, of class PluginBase.
     */
    @Test
    public void testInit_JavaPlugin_ConfigDataInterface() {
        ConfigDataInterface cdi = mock(ConfigDataInterface.class);
        PluginBase.INSTANCE.init(plugin, cdi);
        assertNotNull(PluginBase.INSTANCE.plugin());
        assertNotNull(PluginBase.INSTANCE.config());
        assertNotNull(PluginBase.INSTANCE.pluginDescription());
    }

    /**
     * Test of definePluginPrefix method, of class PluginBase.
     */
    @Test
    public void testDefinePluginPrefix() {
        String prefix = "";
        PluginBase.INSTANCE.definePluginPrefix(prefix);
        assertEquals(prefix + ChatColor.RESET, PluginBase.INSTANCE.pluginPrefix());
    }

    /**
     * Test of defineMainLanguage method, of class PluginBase.
     */
    @Test
    public void testDefineMainLanguage() {
        Langs lang = null;
        PluginBase.INSTANCE.defineMainLanguage(lang);
        assertEquals(lang, PluginBase.INSTANCE.getMainLanguage());
    }

    /**
     * Test of defineErrorFormat method, of class PluginBase.
     */
    @Test
    public void testDefineErrorFormat() {
        short n = 0;
        PluginBase.INSTANCE.defineErrorFormat(n);
        assertEquals(n, PluginBase.INSTANCE.getErrorFormat());
    }
}
