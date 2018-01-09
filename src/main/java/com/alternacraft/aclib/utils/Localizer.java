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
package com.alternacraft.aclib.utils;

import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.langs.Lang;
import java.lang.reflect.Field;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This class contains some utils for locales.
 * 
 * @author AlternaCraft
 * @see NMS 
 */
public enum Localizer {
    ENGLISH(Lang.EN, "en_US"),    
    SPANISH(Lang.ES, "es_ES"),
    ARGENTINEAN_SPANISH(Lang.ES, "es_AR"),
    MEXICO_SPANISH(Lang.ES, "es_MX"),
    URUGUAY_SPANISH(Lang.ES, "es_UY"),
    VENEZUELA_SPANISH(Lang.ES, "es_VE"),    
    CZECH(Lang.CS, "cs_CZ"),
    EUSKARA(Lang.EU, "eu_ES"),
    GALICIAN(Lang.GL, "gl_ES"),
    CATALAN(Lang.CA, "ca_ES"),
    CROATIAN(Lang.HR, "hr_HR"),
    KOREAN(Lang.KO, "ko_KR"),
    UKRAINIAN(Lang.UK, "uk_UA"),
    POLISH(Lang.PL, "pl_PL"),
    SLOVENIAN(Lang.SL, "sl_SI"),
    SERBIAN(Lang.SR, "sr_SP"),
    ROMANIAN(Lang.RO, "ro_RO"),    
    SWEDISH(Lang.SV, "sv_SE"),
    PORTUGUESE_BR(Lang.PT, "pt_BR"),
    PORTUGUESE_PT(Lang.PT, "pt_PT"),
    DEUTSCH(Lang.DE, "de_DE"),
    GREEK(Lang.GR, "el_GR"),
    FRENCH_CA(Lang.FR, "fr_CA"), 
    FRENCH(Lang.FR, "fr_FR"),
    JAPANESE(Lang.JP, "ja_JP"),
    SIMPLIFIED_CHINESE(Lang.CN, "zh_CN"),    
    TRADITIONAL_CHINESE(Lang.CH, "zh_TW"),    
    RUSSIAN(Lang.RU, "ru_RU");

    private final Lang type;
    private final String code;

    private Localizer(Lang type, String code) {
        this.type = type;
        this.code = code;
    }

    public Lang getType() {
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
    public static Lang getLocale(CommandSender cs) {
        return (cs instanceof Player) ? Localizer.getLocale((Player) cs)
                : PluginBase.INSTANCE.getMainLanguage();
    }
    
    public static Lang getLocale(Player inPlayer) {
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
