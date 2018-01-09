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
package com.alternacraft.aclib.langs;

import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.utils.StringsUtils;
import java.util.Map;

/**
 * Languages behavior
 * 
 * @author AlternaCraft
 */
public interface LangInterface {
    
    /**
     * Returns a message with translated colors.
     *
     * @param lang Languages
     * 
     * @return String
     */
    public default String getText(Lang lang) {
        return StringsUtils.translateColors(getDefaultText(lang));
    }

    /**
     * Returns a message without translated colors.
     *
     * @param lang Languages
     * 
     * @return String
     */
    public default String getDefaultText(Lang lang) {
        Lang main = PluginBase.INSTANCE.getMainLanguage();
        String v = LangManager.getValueFromFile(lang, this.getEnum());
        v = (v == null) ? this.getLocales().get(lang) : v;
        v = (v == null) ? LangManager.getValueFromFile(main, this.getEnum()) : v;
        v = (v == null) ? this.getLocales().get(main) : v;
        return v;
    }
    
    /**
     * Returns locales.
     * 
     * @return Map with locales
     */
    public Map<Lang, String> getLocales();
    
    /**
     * Returns Enum class.
     * 
     * @return Enum
     */
    public Enum getEnum();
}
