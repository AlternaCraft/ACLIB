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
import com.alternacraft.aclib.utils.PluginLog;
import com.alternacraft.aclib.utils.StringsUtils;
import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Singleton.
 *
 * @author AlternaCraft
 */
public class PluginBase {

    /**
     * Gets plugin's directory
     *
     * @since 1.0.2
     */
    public static String DIRECTORY;

    /**
     * Ticks Per Second
     *
     * @since 0.0.9
     */
    public static final int TPS = 20;

    /**
     * Gets singleton.
     * 
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
    public final void init(JavaPlugin plugin) {
        this.pluginInstance = plugin;

        DIRECTORY = new StringBuilder().append(
                plugin.getDataFolder()).append(
                File.separator).toString();

        pluginDescription = new PluginDescription();
        configurationFile = new ConfigurationFile(plugin);

        // Plugin logs
        PluginLog.setDefaultPath(plugin);
    }

    /**
     * Initializing the essentials and setting the main config data.
     *
     * @param plugin JavaPlugin
     * @param cdi ConfigDataInterface
     *
     * @since 0.0.9
     */
    public final void init(JavaPlugin plugin, ConfigDataInterface cdi) {
        this.init(plugin);
        this.configurationFile.loadParams(cdi);
    }

    // <editor-fold defaultstate="collapsed" desc="Setters">
    /**
     * Sets plugin prefix.
     * 
     * @param prefix Prefix
     */
    public void definePluginPrefix(String prefix) {
        this.prefix = StringsUtils.translateColors(prefix + "&r");
    }

    /**
     * Sets the main language of the plugin.
     * 
     * @param lang Language
     * @since 0.0.6
     */
    public void defineMainLanguage(Langs lang) {
        this.messages = lang;
    }

    /**
     * Sets error format.
     * 
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

    public ConfigurationFile config() {
        return configurationFile;
    }

    public PluginDescription pluginDescription() {
        return pluginDescription;
    }

    /**
     * Returns plugin prefix.
     * 
     * @return Plugin prefix
     * @since 0.0.6
     */
    public String pluginPrefix() {
        return prefix;
    }

    /**
     * Return main language.
     * 
     * @return Language
     * @since 0.0.6
     */
    public Langs getMainLanguage() {
        return this.messages;
    }

    /**
     * Return error format.
     * 
     * @return Error format number
     * @since 0.0.6
     */
    public short getErrorFormat() {
        return this.errorFormat;
    }
    //</editor-fold>
}
