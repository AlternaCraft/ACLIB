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
package com.alternacraft.aclib.hook;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginBase;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class ExternalPluginRegisterer {

    private final boolean shouldDisplay;

    private final Map<String, HookerInterface> plugins = new HashMap();
    private final Map<String, Boolean> enabled = new HashMap();

    public ExternalPluginRegisterer() {
        this.shouldDisplay = true;
    }

    public ExternalPluginRegisterer(boolean shouldDisplay) {
        this.shouldDisplay = shouldDisplay;
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
        loadPlugins(5L, new StringBuilder()
                .append(ChatColor.YELLOW)
                .append("%p%")
                .append(ChatColor.AQUA)
                .append(" integrated correctly")
                .toString()
        );
    }

    /**
     * Loads added plugins.
     *
     * @param time Time to execute the task.
     * @param format String with the text to display. %p% will be replaced with
     * the plugin name.
     */
    public void loadPlugins(long time, String format) {
        final JavaPlugin plugin = PluginBase.INSTANCE.plugin();

        // Tareas posteriores
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            plugins.keySet().forEach(key -> loadPlugin(key));
            if (this.shouldDisplay) {
                MessageManager.log(ChatColor.GRAY + "# STARTING INTEGRATION MODULE #");
                this.enabled.entrySet()
                        .stream()
                        .filter(e -> e.getValue())
                        .map(e -> format.replace("%p%", e.getKey()))
                        .forEach(MessageManager::log);
                MessageManager.log(ChatColor.GRAY + "# ENDING INTEGRATION MODULE #");
            }
        }, time);
    }

    public boolean isPluginEnabled(String pl) {
        return this.enabled.get(pl);
    }
}
