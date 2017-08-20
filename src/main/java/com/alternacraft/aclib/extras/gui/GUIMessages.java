package com.alternacraft.aclib.extras.gui;

import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.langs.LangInterface;
import com.alternacraft.aclib.langs.LangManager;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.StringsUtils;
import java.util.HashMap;

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

    private final HashMap<Langs, String> locales = new HashMap();

    /**
     * Define the default languages to load
     *
     * @param en English
     * @param es Spanish
     */
    private GUIMessages(String en, String es) {
        this.locales.put(Langs.EN, en);
        this.locales.put(Langs.ES, es);
    }

    @Override
    public String getText(Langs lang) {
        return StringsUtils.translateColors(getDefaultText(lang));
    }

    @Override
    public String getDefaultText(Langs lang) {    
        Langs main = PluginBase.INSTANCE.getMainLanguage();
        String v = LangManager.getValueFromFile(lang, this);
        v = (v == null) ? this.locales.get(lang) : v;
        v = (v == null) ? LangManager.getValueFromFile(main, this) : v;
        v = (v == null) ? this.locales.get(main) : v;
        return v;
    }
}

