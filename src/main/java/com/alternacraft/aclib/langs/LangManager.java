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
import com.alternacraft.aclib.PluginManager;
import com.alternacraft.aclib.files.PluginFile;
import java.io.File;

public class LangManager {

    public static final String DIRECTORY = new StringBuilder().append(
            PluginManager.INSTANCE.plugin().getDataFolder()).append(
                    File.separator).append(
                    "Langs").append(
                    File.separator).toString();

    private static PluginFile backupFile = null;

    private static Langs[] keys = null;

    private LangManager() {
    }

    /**
     * Method for loading locales
     *
     * @param <T>
     * @param enumClass
     * @param loadLangs
     */
    public static <T extends Enum<T> & LangInterface> void load(Class<T> enumClass,
            Langs... loadLangs) {

        for (Langs langType : keys) {
            // Language file
            PluginFile langFile = new PluginFile(DIRECTORY + "messages_" + langType.name() + ".yml");

            if (!langFile.exists()) {
                createConfig(langFile, langType, false, enumClass); // Not restore
            }

            if (!checkLocales(langFile, langType, enumClass)) {
                MessageManager.logError("Error loading " + langType.name() + " locales, "
                        + "a new one has been created.");
            }
        }
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
            PluginFile langFile, Langs langType, Class<T> enumClass) {
        backupFile = new PluginFile(DIRECTORY + "messages_" + langType.name() + "_Backup.yml");
        langFile.loadYamlConfiguration();

        Boolean resul = true;

        // Check if it is complete
        for (T lang : enumClass.getEnumConstants()) {
            if (!langFile.hasNode(lang.name())) {
                backupFile.copyYamlConfiguration(langFile.yamlFile); // Save the original file                   
                createConfig(langFile, langType, true, enumClass); // Restore
                return false;
            }
        }

        return resul;
    }

    /**
     * Method for creating the missing language files
     *
     * @param langFile PluginFile
     * @param lang Langs
     * @param restore boolean for keeping old values
     */
    private static <T extends Enum<T> & LangInterface> void createConfig(
            PluginFile langFile, Langs lang, boolean restore, Class<T> enumClass) {

        langFile.resetYamlConfiguration();

        if (restore) {
            backupFile.loadYamlConfiguration();
        }

        langFile.yamlFile.options().header(
                "######################################\n"
                + "## [LOCALES]Do not edit %variables% ##\n"
                + "######################################"
        );
        langFile.yamlFile.options().copyHeader(true);

        for (T idioma : enumClass.getEnumConstants()) {
            String name = idioma.name();
            String value = idioma.getDefaultText(lang);;

            // Set previous value
            if (restore) {
                if (backupFile.hasNode(name)) {
                    value = (String) backupFile.getNode(name);
                }
            }

            langFile.setNode(name, value);
        }

        langFile.saveConfiguration();
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
