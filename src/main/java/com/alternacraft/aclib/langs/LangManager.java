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
package com.alternacraft.aclib.langs;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.files.PluginFile;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LangManager {

    public static final String DIRECTORY = new StringBuilder().append(
            PluginBase.INSTANCE.plugin().getDataFolder()).append(
                    File.separator).append(
                        "Langs").append(
                            File.separator).toString();

    private static final Map<String, List<Class>> MESSAGES = new HashMap<>();

    private static PluginFile backupFile = null;
    private static Langs[] keys = {Langs.EN}; // Default value

    private LangManager() {
    }

    /**
     * Method to register an Enum with a custom path
     *
     * @param e Enum class
     * @param path Path to save the file
     */
    public static void saveMessages(String path, Class... e) {        
        if (MESSAGES.get(path) == null) {
            MESSAGES.put(path, new ArrayList());
        }        
        for (Class clazz : e) {
            if (!MESSAGES.get(path).contains(clazz)) {
                MESSAGES.get(path).add(clazz);
            }
        }
    }

    /**
     * Method to register an Enum with the default path
     *
     * @param e Enum class
     */
    public static void saveMessages(Class... e) {
        LangManager.saveMessages(DIRECTORY, e);
    }

    /**
     * Method for loading locales
     */
    public static void loadMessages() {
        for (Langs langType : keys) {
            for (Map.Entry<String, List<Class>> entry : MESSAGES.entrySet()) {
                String key = entry.getKey();
                List<Class> value = entry.getValue();

                PluginFile langFile = new PluginFile(key + "messages_"
                        + langType.name() + ".yml");

                if (!langFile.exists()) {
                    createConfig(langFile, langType, value, false); // Not restore
                }

                if (!checkLocales(langFile, langType, value)) {
                    MessageManager.logError("Couldn't load " + langType.name() + " locales, "
                            + "a new one has been created.");
                }
            }
        }
    }

    /**
     * Method to create a new file
     *
     * @param <T> Enum type
     * @param langfile PluginFile
     * @param lang Langs
     * @param messages List
     * @param restore boolean
     */
    private static <T extends Enum<T> & LangInterface> void createConfig(
            PluginFile langfile, Langs lang, List<Class> messages, boolean restore) {

        langfile.resetYamlConfiguration();

        if (restore) {
            backupFile.loadYamlConfiguration();
        }

        langfile.yamlFile.options().header(
                "######################################\n"
                + "## [LOCALES]Do not edit %variables% ##\n"
                + "######################################"
        );
        langfile.yamlFile.options().copyHeader(true);

        for (Class<T> e : messages) {
            for (T msgs : e.getEnumConstants()) {
                String name = msgs.name();
                String value = msgs.getDefaultText(lang);

                // Set previous value
                if (restore) {
                    if (backupFile.hasNode(name)) {
                        value = (String) backupFile.getNode(name);
                    }
                }

                langfile.setNode(name, value);
            }
        }

        langfile.saveConfiguration();
    }

    /**
     * Method for checking the values
     *
     * @param langFile PluginFile
     * @param langConf YamlConfiguration
     * @param langType Langs
     * @return true or false
     */
    private static <T extends Enum<T> & LangInterface> boolean checkLocales(
            PluginFile langFile, Langs langType, List<Class> messages) {
        backupFile = new PluginFile(langFile.getParent() + "messages_" + langType.name() + "_Backup.yml");
        langFile.loadYamlConfiguration();

        Boolean resul = true;

        for (Class<T> e : messages) {
            for (T msgs : e.getEnumConstants()) {
                if (!langFile.hasNode(msgs.name())) {
                    backupFile.copyYamlConfiguration(langFile.yamlFile); // Save the original file                   
                    createConfig(langFile, langType, messages, true); // Restore
                    return false;
                }
            }
        }

        return resul;
    }

    /**
     * Method for setting the default languages
     *
     * @param locales Map
     */
    public static void setKeys(Langs... locales) {
        LangManager.keys = locales;
    }
}