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

import com.alternacraft.aclib.config.ConfigDataInterface;
import com.alternacraft.aclib.config.ConfigurationFile;
import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.aclib.utils.PluginLog;
import com.alternacraft.aclib.utils.Recorder;
import com.alternacraft.aclib.utils.StringsUtils;
import java.io.File;
import java.util.Arrays;
import java.util.logging.LogRecord;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Singleton.
 *
 * @author AlternaCraft
 */
public class PluginBase {
    
    //<editor-fold defaultstate="collapsed" desc="Vars + Constructor">
    /**
     * Gets plugin's directory.
     *
     * @since 1.0.2
     */
    public static String DIRECTORY;

    /**
     * Ticks Per Second.
     *
     * @since 0.0.9
     */
    public static final int TPS = 20;
    
    /**
     * Player maximum health.
     *
     * @since 1.5.1
     */
    public static final double MAX_HEALTH = 20.0;
    
    /**
     * Player maximum food.
     *
     * @since 1.5.1
     */
    public static final int MAX_FOOD = 20;
    
    /**
     * Gets singleton.
     * 
     * @since 0.0.6
     */
    public static final PluginBase INSTANCE = new PluginBase();

    private JavaPlugin pluginInstance = null;
    private String prefix = null;
    private boolean debug = false;

    private PluginDescription pluginDescription = null;
    private ConfigurationFile configurationFile = null;

    /**
     * Default language for translations
     *
     * @since 0.0.6
     */
    private Lang messages = Lang.EN;

    /**
     * Default language for translations
     *
     * @since 0.0.6
     */
    private short errorFormat = 2;

    /**
     * Custom nodes of the configuration file to keep previous data.
     * 
     * @since 1.2.1
     */
    private String[] nodes = {};
    
    /**
     * Static variable to record times.
     */
    public static final Recorder METER = new Recorder();
    
    // Singleton
    private PluginBase() {    
    }
    //</editor-fold>

    /**
     * Initializing the essentials
     *
     * @param plugin Java plugin
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
     * Sets custom nodes.
     * 
     * @param nodes Array of nodes
     */
    public void defineCustomNodes(String... nodes) {
        this.nodes = nodes;
    }
    
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
    public void defineMainLanguage(Lang lang) {
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
    
    /**
     * Define custom filters for logger.
     * 
     * @param cmds Regular expressions 
     * @since 1.4.1
     */
    public void defineFilters(String... cmds) {
        this.pluginInstance.getServer().getLogger().setFilter((LogRecord record) -> {
            return !Arrays.stream(cmds).anyMatch(cmd -> 
                    record.getMessage().matches(cmd));
        });
    }
    
    /**
     * Define debug mode.
     * 
     * @param debug Should enable debug?
     * @since 1.4.1
     */
    public void defineDebugMode(boolean debug) {
        this.debug = debug;
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
     * Returns custom nodes.
     * 
     * @return Array of strings
     * @since 1.2.1
     */
    public String[] getCustomNodes() {
        return nodes;
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
     * Returns main language.
     * 
     * @return Language
     * @since 0.0.6
     */
    public Lang getMainLanguage() {
        return this.messages;
    }

    /**
     * Returns error format.
     * 
     * @return Error format number
     * @since 0.0.6
     */
    public short getErrorFormat() {
        return this.errorFormat;
    }
    
    /**
     * Returns debug mode
     * 
     * @return True if it is enabled; False if not
     * @since 1.4.1
     */
    public boolean isDebug() {
        return this.debug;
    }
    //</editor-fold>
}
