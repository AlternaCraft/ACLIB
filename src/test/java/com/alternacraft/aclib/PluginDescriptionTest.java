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

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author AlternaCraft
 */
public class PluginDescriptionTest {
    
    public PluginDescriptionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addLine method, of class PluginDescription.
     */
    @Test
    public void testAddLine() {
        String expected1 = "test1";
        String expected2 = "test2";
        
        PluginDescription instance = new PluginDescription();
        
        instance.addLine(expected1);
        instance.addLine(expected2);
        
        List<String> lines = instance.getLines();
        
        String result1 = lines.get(0);
        String result2 = lines.get(1);
        
        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
    }
}
