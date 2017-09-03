package com.alternacraft.aclib.extras.gui;

import com.alternacraft.aclib.PluginBase;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.Inventory;
import org.json.simple.JSONObject;

/**
 *
 * @author AlternaCraft
 */
public abstract class GUI {

    protected static final String DEF_NAME = "Chest";
    
    protected static final int MAX_COLS = 9;
    
    protected String title;
    protected int update_interval;
    
    protected final JSONObject meta;
    protected final Map<Integer, GUIItem> options;

    public GUI() {
        this(DEF_NAME);
    }
    
    public GUI(String title) {
        this.title = title;
        this.update_interval = 0;
        this.meta = new JSONObject();
        this.options = new HashMap<>();
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUpdate_interval() {
        return update_interval;
    }

    public void setUpdate_interval(int update_interval) {
        this.update_interval = update_interval;
    }

    public JSONObject getMeta() {
        return meta;
    }

    public void addMeta(String k, Object v) {
        this.meta.put(k, v);
    }
    
    public Map<Integer, GUIItem> getOptions() {
        return options;
    }
    
    public void addItem(int slot, GUIItem item) {
        if (slot < 0 || slot >= this.getMaxSlots()) {
            return;
        }
        this.options.put(slot, item);
    }

    public void addNextItem(GUIItem item) {
        int i = 0;
        while (this.options.containsKey(i)) {
            i++;
        }
        this.addItem(i, item);
    }

    public Inventory getInventory() {
        String meta_title = this.addMetaToTitle();
        Inventory inventory = PluginBase.INSTANCE.plugin().getServer()
                .createInventory(null, this.getSlots(), meta_title);
        options.entrySet().forEach(i -> inventory.setItem(i.getKey(),
                i.getValue().getCompleteItem()));
        return inventory;
    }
    
    protected String addMetaToTitle() {
        StringBuilder sb = new StringBuilder(this.title);
        JSONObject aux_meta = this.getMeta();
        aux_meta.put(GUIUtils.CI_KEY, GUIUtils.CI_META);
        aux_meta.put(GUIUtils.UPDATE_INTERVAL, this.update_interval);
        sb.append(HiddenStringUtils.encodeString(aux_meta.toString()));
        return sb.toString();
    }

    public void fillWith(GUIItem item) {
        for (int i = 0; i < this.getMaxSlots(); i++) {
            if (!this.options.containsKey(i)) {
                this.options.put(i, item);
            }
        }
    }

    public void cleanInventory() {
        this.options.clear();
    }

    public int getSlots() {
        return this.getRows() * this.getCols();
    }

    public int getCols() {
        return MAX_COLS;
    }

    public abstract int getRows();
    public abstract int getMaxSlots();
}
