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
package com.alternacraft.aclib.hook;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginBase;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class ExternalPluginRegister {

    public static final ExternalPluginRegister INSTANCE = new ExternalPluginRegister();

    private final Map<String, HookerInterface> plugins = new HashMap();
    private final Map<String, Boolean> enabled = new HashMap();

    private ExternalPluginRegister() {
    }

    public void registerPlugin(String str, HookerInterface hooker) {
        plugins.put(str, hooker);
        enabled.put(str, Boolean.FALSE);
    }

    public void loadPlugin(String str) {
        if (PluginBase.INSTANCE.plugin().getServer().getPluginManager()
                .isPluginEnabled(str)) {
            this.enabled.put(str, this.plugins.get(str).hook());
        }
    }

    public HookerInterface getHooker(String pluginName) {
        return plugins.get(pluginName);
    }

    public void loadPlugins() {
        final JavaPlugin plugin = PluginBase.INSTANCE.plugin();

        // Tareas posteriores
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                MessageManager.log(ChatColor.GRAY + "# STARTING INTEGRATION MODULE #");

                for (String key : plugins.keySet()) {
                    loadPlugin(key);
                }

                MessageManager.log(ChatColor.GRAY + "# ENDING INTEGRATION MODULE #");
            }
        }, 5L);
    }

    public boolean isPluginEnabled(String pl) {
        return this.enabled.get(pl);
    }
}
