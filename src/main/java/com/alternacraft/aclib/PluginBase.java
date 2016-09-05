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

import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.StrUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginBase {
    /**
     * @since 0.0.6
     */
    public static final PluginBase INSTANCE = new PluginBase();

    private JavaPlugin pluginInstance = null;
    private String prefix = null;

    private PluginDescription pluginDescription = null;
    private ConfigurationFile configurationFile = null;

    /**
     * Default language for translations
     *
     * @since 0.0.6
     */
    private Langs messages = Langs.EN;

    /**
     * Default language for translations
     *
     * @since 0.0.6
     */
    private short errorFormat = 2;
    
    private PluginBase() {
    }

    /**
     * Initializing the essentials
     *
     * @param plugin
     */
    public void init(JavaPlugin plugin) {
        this.pluginInstance = plugin;
        pluginDescription = new PluginDescription();
        configurationFile = new ConfigurationFile(plugin);
    }

    // <editor-fold defaultstate="collapsed" desc="Setters">
    public void definePluginPrefix(String prefix) {
        this.prefix = StrUtils.translateColors(prefix);
    }

    /**
     * @param lang Langs
     * @since 0.0.6
     */
    public void defineMainLanguage(Langs lang) {
        this.messages = lang;
    }

    /**
     * @param n Error format
     * @since 0.0.6
     */
    public void defineErrorFormat(short n) {
        this.errorFormat = n;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters">
    public JavaPlugin plugin() {
        return pluginInstance;
    }

    public String pluginPrefix() {
        return prefix;
    }

    public ConfigurationFile config() {
        return configurationFile;
    }

    public PluginDescription getPluginDescription() {
        return pluginDescription;
    }

    /**
     * @return Langs
     * @since 0.0.6
     */
    public Langs getMainLanguage() {
        return this.messages;
    }

    /**
     * @return short
     * @since 0.0.6
     */
    public short getErrorFormat() {
        return this.errorFormat;
    }
    //</editor-fold>
}
