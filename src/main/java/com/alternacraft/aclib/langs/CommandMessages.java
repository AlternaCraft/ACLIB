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

import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.utils.StringsUtils;
import java.util.HashMap;

public enum CommandMessages implements LangInterface {
    // <editor-fold defaultstate="collapsed" desc="MESSAGES">
    NO_PERMISSION(
            "&c¡No tienes suficiente permiso para hacer eso!",
            "&cYou don’t have enough permission to do this!"
    ),
    INVALID_ARGUMENTS(
            "&cEse subcomando no es válido",
            "&cInvalid subcommand"
    ),
    COMMAND_USAGE(
            "&cUso: /%USAGE%",
            "&cUsage: /%USAGE%"
    ),
    ONLY_PLAYERS(
            "&c¡No puedes ejecutar ese comando!",
            "&cYou can't execute that command!"
    );
    // </editor-fold>

    public final HashMap<Lang, String> locales = new HashMap();

    /**
     * Define the default languages to load
     *
     * @param es Spanish
     * @param en English
     */
    private CommandMessages(String es, String en) {
        this.locales.put(Lang.ES, es);
        this.locales.put(Lang.EN, en);
    }

    @Override
    public String getText(Lang lang) {
        return StringsUtils.translateColors(getDefaultText(lang));
    }

    @Override
    public String getDefaultText(Lang lang) {
        Lang main = PluginBase.INSTANCE.getMainLanguage();
        String v = LangManager.getValueFromFile(lang, this);
        v = (v == null) ? this.locales.get(lang) : v;
        v = (v == null) ? LangManager.getValueFromFile(main, this) : v;
        v = (v == null) ? this.locales.get(main) : v;
        return v;
    }
}
