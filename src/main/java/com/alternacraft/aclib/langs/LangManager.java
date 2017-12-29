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
import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.exceptions.KeyNotFoundException;
import com.alternacraft.aclib.utils.MapUtils;
import com.alternacraft.aclib.utils.PluginFile;
import com.alternacraft.aclib.utils.RegExp;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Language manager
 *
 * @author AlternaCraft
 */
public class LangManager {

    public static final String LANG_DIRECTORY = new StringBuilder(PluginBase.DIRECTORY)            
            .append("langs")
            .append(File.separator)
            .toString();
    public static final String ACTUAL_DIR = "." + File.separator;

    private static final Map<String, List<Class>> MESSAGES = new HashMap<>();
    private static final Map<String, PluginFile> LOADED_FILES = new HashMap<>();

    private static PluginFile backupFile = null;
    private static Lang[] keys = {Lang.EN}; // Default value

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
     * Registers an Enum with the default path.
     *
     * @param e Enum class
     */
    public static void saveMessages(Class... e) {
        saveMessages(new StringBuilder(LANG_DIRECTORY)
                .append("messages").toString(), e);
    }

    /**
     * Registers an Enum.
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
     * Loads message files.
     */
    public static void loadMessages() {
        for (Lang langType : keys) {
            MESSAGES.entrySet().forEach(entry -> {
                String key = entry.getKey();
                List<Class> value = entry.getValue();

                PluginFile langFile = new PluginFile(ACTUAL_DIR, key + "_"
                        + langType.name() + ".yml", false);

                if (!langFile.exists()) {
                    createConfig(langFile, langType, value, false); // Not restore
                } else {
                    if (!checkLocales(langFile, langType, value)) {
                        MessageManager.logError("Couldn't load " + langType.name() + " locales, "
                                + "a new one has been created.");
                    }
                }
                LOADED_FILES.put(key + "_" + langType.name() + ".yml", langFile);
            });
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
            PluginFile langfile, Lang lang, List<Class> messages, boolean restore) {

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
            PluginFile langFile, Lang langType, List<Class> messages) {
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
    public static void setKeys(Lang... locales) {
        LangManager.keys = locales;
    }

    /**
     * Returns main languages.
     * 
     * @return Array of languages
     */
    public static Lang[] getKeys() {
        return keys;
    }

    /**
     * Cleans all registered messages.
     */
    public static void clearMessages() {
        LangManager.MESSAGES.clear();
        LangManager.LOADED_FILES.clear();
    }

    private static String findMessageByKey(String path, String lang, String key) {
        PluginFile pluginFile = LOADED_FILES.get(path + "_" + lang + ".yml");
        // Value from the file (externally)
        if (pluginFile != null && pluginFile.yamlFile != null && !key.equals("")
                && pluginFile.hasNode(key)) {
            return (String) pluginFile.getNode(key);
        }
        return null;
    }

    /**
     * Find a language value in the registered files
     *
     * @param lang Language
     * @param key Key
     * @param match Array of matches
     *
     * @return Message or null
     */
    public static String findValueInAllFiles(Lang lang, String key, String... match) {
        return Arrays.stream(MapUtils.getKeys(MESSAGES))
                .map(m -> findMessageByKey((String) m, lang.name(), key))
                .filter(m -> m != null)
                .filter(p -> match.length == 0 || Arrays.stream(match).anyMatch(m -> p.contains(m)))
                .findFirst()
                .orElse(null);
    }

    public static String findValueInFile(String fname, String lang, String key, String... match) {
        return findValueInFile(fname, Lang.valueOf(lang), key, match);
    }
    
    /**
     * Find a language value in a file. (if exists)
     *
     * @param fname File name without extension
     * @param lang Language name
     * @param key Language key
     * @param match Array of matches
     *
     * @return Translated value or null
     */
    public static String findValueInFile(String fname, Lang lang, String key, String... match) {
        String aux = Arrays.stream(MapUtils.getKeys(MESSAGES))
                .filter(p -> p.matches(".*\\" + File.separator 
                        + Matcher.quoteReplacement(fname)))
                .filter(p -> match.length == 0 || Arrays.stream(match).anyMatch(m -> p.contains(m)))
                .findFirst()
                .orElse(null);
        if (aux != null) {
            aux = findMessageByKey(aux, lang.name(), key);
        }
        return aux;
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
    public static <T extends Enum> String getValueFromFile(Lang lang, T e) {
        try {
            String path = MapUtils.getKeyFromList(MESSAGES, e.getDeclaringClass());
            return findMessageByKey(path, lang.name(), e.name());
        } catch (KeyNotFoundException ex) {
            return null;
        }
    }

    /**
     * Returns a list with the variables of the translation
     *
     * @param text Locale translated
     * @return Variables
     */
    public static List<String> getVariables(String text) {
        return RegExp.getGroups("(%[\\w_]+%)", text);
    }
}
