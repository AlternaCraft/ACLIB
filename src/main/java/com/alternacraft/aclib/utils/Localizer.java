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
package com.alternacraft.aclib.utils;

import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.PluginBase;
import java.lang.reflect.Field;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @see NMS
 */
public enum Localizer {

    ENGLISH(Langs.EN, "en_US"),    
    SPANISH(Langs.ES, "es_ES"),
    ARGENTINEAN_SPANISH(Langs.ES, "es_AR"),
    MEXICO_SPANISH(Langs.ES, "es_MX"),
    URUGUAY_SPANISH(Langs.ES, "es_UY"),
    VENEZUELA_SPANISH(Langs.ES, "es_VE"),    
    CZECH(Langs.CS, "cs_CZ"),
    EUSKARA(Langs.EU, "eu_ES"),
    GALICIAN(Langs.GL, "gl_ES"),
    CATALAN(Langs.CA, "ca_ES"),
    CROATIAN(Langs.HR, "hr_HR"),
    KOREAN(Langs.KO, "ko_KR"),
    UKRAINIAN(Langs.UK, "uk_UA"),
    POLISH(Langs.PL, "pl_PL"),
    SLOVENIAN(Langs.SL, "sl_SI"),
    SERBIAN(Langs.SR, "sr_SP"),
    ROMANIAN(Langs.RO, "ro_RO"),    
    SWEDISH(Langs.SV, "sv_SE"),
    PORTUGUESE_BR(Langs.PT, "pt_BR"),
    PORTUGUESE_PT(Langs.PT, "pt_PT"),
    DEUTSCH(Langs.DE, "de_DE"),
    GREEK(Langs.GR, "el_GR"),
    FRENCH_CA(Langs.FR, "fr_CA"), 
    FRENCH(Langs.FR, "fr_FR"),
    JAPANESE(Langs.JP, "ja_JP"),
    SIMPLIFIED_CHINESE(Langs.CN, "zh_CN"),    
    TRADITIONAL_CHINESE(Langs.CH, "zh_TW"),    
    RUSSIAN(Langs.RU, "ru_RU");

    private final Langs type;
    private final String code;

    private Localizer(Langs type, String code) {
        this.type = type;
        this.code = code;
    }

    public Langs getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    private static Field field = null;

    /**
     * Method for getting a locale by CommandSender
     * 
     * @param cs CommandSender
     * @return Langs
     * 
     * @since 0.0.9
     */
    public static Langs getLocale(CommandSender cs) {
        return (cs instanceof Player) ? Localizer.getLocale((Player) cs)
                : PluginBase.INSTANCE.getMainLanguage();
    }
    
    public static Langs getLocale(Player inPlayer) {
        try {
            Object nms = NMS.castToNMS(inPlayer);

            if (field == null) {
                field = nms.getClass().getDeclaredField("locale");
                field.setAccessible(true);
            }

            Localizer code = getByCode((String) field.get(nms));

            return code.getType();
        } catch (NoSuchFieldException | SecurityException |
                IllegalArgumentException | IllegalAccessException exc) {
            return PluginBase.INSTANCE.getMainLanguage();
        }
    }

    public static Localizer getByCode(String code) {
        for (Localizer l : values()) {
            if (l.getCode().equalsIgnoreCase(code)) {
                return l;
            }
        }
        return Localizer.ENGLISH;
    }
}
