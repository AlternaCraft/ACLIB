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

import java.util.HashMap;
import java.util.Map;

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
            "&cComando desconocido. El uso correcto es /%USAGE%",
            "&cUnknown command. Correct usage is /%USAGE%"
    ),
    ONLY_PLAYERS(
            "&c¡No puedes ejecutar ese comando!",
            "&cYou can't execute that command!"
    ),
    COMMAND_OPTION(
            "<opción>",
            "<option>"
    ),
    COMMAND_OPTION_LIST(
            "&eLista de opciones:",
            "&eOptions list:"
    );
    // </editor-fold>

    public final Map<Lang, String> locales = new HashMap();

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
    public Map<Lang, String> getLocales() {
        return this.locales;
    }
    
    @Override
    public Enum getEnum() {
        return this;
    }
}
