package com.alternacraft.aclib.extras.gui;

import com.alternacraft.aclib.PluginBase;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author AlternaCraft
 */
public abstract class GUI {

    protected static final String DEF_NAME = "Chest";
    
    protected static final int MAX_COLS = 9;
    
    protected String title;
    protected final Map<Integer, GUIItem> options;

    public GUI() {
        this(DEF_NAME);
    }
    
    public GUI(String title) {
        this.title = title;
        this.options = new HashMap<>();
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        String meta_title = this.title + HiddenStringUtils.encodeString(GUIUtils.CI_META);
        Inventory inventory = PluginBase.INSTANCE.plugin().getServer()
                .createInventory(null, this.getSlots(), meta_title);
        options.entrySet().forEach(i -> inventory.setItem(i.getKey(),
                i.getValue().getCompleteItem()));
        return inventory;
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
