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

