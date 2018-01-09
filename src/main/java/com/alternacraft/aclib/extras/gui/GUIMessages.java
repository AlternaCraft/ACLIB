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
package com.alternacraft.aclib.extras.gui;

import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.aclib.langs.LangInterface;
import java.util.HashMap;
import java.util.Map;

/**
 * Messages about general information.
 * 
 * @author AlternaCraft
 */
public enum GUIMessages implements LangInterface {
    // <editor-fold defaultstate="collapsed" desc="MESSAGES">
    EXIT("Exit", "Salir"),
    EXIT_ACTION("Click to exit", "Clic para salir"),
    NEXT("Next", "Siguiente"),
    NEXT_ACTION("Click to go to the next page.", "Clic para pasar de página"),
    PREVIOUS("Previous", "Anterior"),
    PREVIOUS_ACTION("Click to go to the previous page", "Clic para volver a la anterior");
    // </editor-fold>

    private final HashMap<Lang, String> locales = new HashMap();

    /**
     * Define the default languages to load
     *
     * @param en English
     * @param es Spanish
     */
    private GUIMessages(String en, String es) {
        this.locales.put(Lang.EN, en);
        this.locales.put(Lang.ES, es);
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

