/*
 * Copyright (C) 2018 AlternaCraft
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
package com.alternacraft.aclib.extras.hook;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginBase;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ExternalPluginRegisterer {

    public static final String DEFAULT_TEMPLATE = new StringBuilder()
            .append(ChatColor.YELLOW)
            .append("%p%")
            .append(ChatColor.AQUA)
            .append(" integrated correctly")
            .toString();

    private final boolean shouldDisplay;
    private final boolean delimiters;

    private final Map<String, HookerInterface> plugins = new HashMap();
    private final Map<String, Boolean> enabled = new HashMap();

    public ExternalPluginRegisterer() {
        this(true, true);
    }

    public ExternalPluginRegisterer(boolean shouldDisplay) {
        this(shouldDisplay, true);
    }

    public ExternalPluginRegisterer(boolean shouldDisplay, boolean delimiters) {
        this.shouldDisplay = shouldDisplay;
        this.delimiters = delimiters;
    }

    public void registerPlugin(HookerInterface hooker) {
        plugins.put(hooker.name(), hooker);
        enabled.put(hooker.name(), Boolean.FALSE);
    }

    public void loadPlugin(String str) {
        if (PluginBase.INSTANCE.plugin().getServer().getPluginManager()
                .isPluginEnabled(str)) {
            this.enabled.put(str, this.plugins.get(str).hook());
        }
    }

    public boolean isRegistered(String str) {
        return this.plugins.containsKey(str);
    }

    public HookerInterface getHooker(String pluginName) {
        return plugins.get(pluginName);
    }

    public void loadPlugins() {
        loadPlugins(5L, DEFAULT_TEMPLATE);
    }

    /**
     * Loads added plugins.
     *
     * @param tps    TPS to execute the task; If 0 then it will be executed
     *               instantly.
     * @param format String with the text to display. %p% will be replaced with
     *               the plugin name.
     */
    public void loadPlugins(long tps, String format) {
        if (tps == 0) {
            common(format);
        } else {
            final JavaPlugin plugin = PluginBase.INSTANCE.plugin();
            // Tareas posteriores
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                common(format);
            }, tps);
        }
    }

    private void common(String format) {
        if (this.shouldDisplay && this.delimiters) {
            MessageManager.log(ChatColor.GRAY + "# STARTING INTEGRATION MODULE #");
        }
        plugins.keySet().forEach(key -> loadPlugin(key));
        if (this.shouldDisplay) {
            this.enabled.entrySet()
                    .stream()
                    .filter(e -> e.getValue())
                    .map(e -> format.replace("%p%", e.getKey()))
                    .forEach(MessageManager::log);
            if (this.delimiters) {
                MessageManager.log(ChatColor.GRAY + "# ENDING INTEGRATION MODULE #");
            }
        }
    }

    public boolean isPluginHooked(String pl) {
        return this.enabled.containsKey(pl) && this.enabled.get(pl);
    }
}
