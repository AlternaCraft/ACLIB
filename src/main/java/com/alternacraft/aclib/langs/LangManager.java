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
package com.alternacraft.aclib.langs;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.utils.MapUtils;
import com.alternacraft.aclib.utils.PluginFile;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Language manager
 *
 * @author AlternaCraft
 */
public class LangManager {

    public static final String LANG_DIRECTORY
            = new StringBuilder().append(
                    "langs").append(
                            File.separator).toString();

    private static final Map<String, List<Class>> MESSAGES = new HashMap<>();

    private static PluginFile backupFile = null;
    private static Langs[] keys = {Langs.EN}; // Default value

    private LangManager() {
    }

    /**
     * Registers an Enum with a custom path.
     *
     * @param e Enum class
     * @param filename File name without extension.
     * @param path File path.
     */
    public static void saveMessages(String path, String filename, Class... e) {
        saveMessages(path + "/" + filename);
    }

    /**
     * Registers an Enum with a custom path.
     *
     * @param e Enum class
     * @param path Filepath + filename without extension.
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
     * Registers an Enum with the default path.
     *
     * @param e Enum class
     */
    public static void saveMessages(Class... e) {
        LangManager.saveMessages(new StringBuilder(LANG_DIRECTORY)
                .append("messages").toString(), e);
    }

    /**
     * Loads message files.
     */
    public static void loadMessages() {
        for (Langs langType : keys) {
            for (Map.Entry<String, List<Class>> entry : MESSAGES.entrySet()) {
                String key = entry.getKey();
                List<Class> value = entry.getValue();

                PluginFile langFile = new PluginFile(key + "_"
                        + langType.name() + ".yml", false);

                if (!langFile.exists()) {
                    createConfig(langFile, langType, value, false); // Not restore
                } else {
                    if (!checkLocales(langFile, langType, value)) {
                        MessageManager.logError("Couldn't load " + langType.name() + " locales, "
                                + "a new one has been created.");
                    }
                }
            }
        }
    }

    /**
     * Creates a new language file.
     *
     * @param <T> Enum type
     * @param langfile PluginFile
     * @param lang Langs
     * @param messages List
     * @param restore boolean
     *
     * @see LangInterface
     */
    private static <T extends Enum & LangInterface> void createConfig(
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
                    // Ignore empty lines, new translations?
                    if (backupFile.hasNode(name) && !backupFile.getNode(name).equals("")) {
                        value = (String) backupFile.getNode(name);
                    }
                }

                langfile.setNode(name, value);
            }
        }

        langfile.saveConfiguration();
    }

    /**
     * Checks the inner values.
     *
     * @param langFile PluginFile
     * @param langConf YamlConfiguration
     * @param langType Langs
     * @return true or false
     *
     * @see LangInterface
     */
    private static <T extends Enum> boolean checkLocales(
            PluginFile langFile, Langs langType, List<Class> messages) {
        backupFile = new PluginFile(langFile.getParent(),
                new StringBuilder()
                        .append(File.separator)
                        .append(langFile.getNameWithoutExtension())
                        .append("_backup.yml").toString(),
                false // Disable auto creation
        );

        langFile.loadYamlConfiguration();

        Boolean resul = true;

        for (Class<T> e : messages) {
            for (T msgs : e.getEnumConstants()) {
                if (!langFile.hasNode(msgs.name())) {
                    backupFile.copyYamlConfiguration(langFile.yamlFile); // Save the original file      
                    backupFile.saveConfiguration();
                    createConfig(langFile, langType, messages, true); // Restore
                    return false;
                }
            }
        }

        return resul;
    }

    /**
     * Sets the main languages.
     *
     * @param locales Map
     */
    public static void setKeys(Langs... locales) {
        LangManager.keys = locales;
    }

    /**
     * Cleans all registered messages.
     */
    public static void clearMessages() {
        LangManager.MESSAGES.clear();
    }

    /**
     * Returns a language value from locales files.
     *
     * @param <T> Enum type
     * @param lang Langs
     * @param e Enum value
     * @return Value or null if it does not exist
     *
     * @see LangInterface
     */
    public static <T extends Enum> String getValueFromFile(
            Langs lang, T e) {

        // File access to get custom message (if exists)
        PluginFile pluginFile = new PluginFile(MapUtils.getKeyFromList(MESSAGES,
                e.getDeclaringClass()) + "_" + lang.name() + ".yml", false);
        pluginFile.loadYamlConfiguration();

        // Value from the file (externally)
        if (pluginFile.yamlFile != null && pluginFile.hasNode(e.name())) {
            return (String) pluginFile.getNode(e.name());
        }

        return null;
    }

    /**
     * Returns a list with the variables of the translation
     * 
     * @param text Locale translated
     * @return Variables
     */
    public static List<String> getVariables(String text) {
        List<String> variables = new ArrayList<>();
        Pattern vars = Pattern.compile("(%[\\w_]+%)");
        Matcher matcher = vars.matcher(text);
        while (matcher.find()) {
            variables.add(matcher.group());
        }
        return variables;
    }
}
