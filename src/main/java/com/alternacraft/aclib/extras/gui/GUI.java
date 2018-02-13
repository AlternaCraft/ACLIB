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

import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.exceptions.SkinNotLoadedException;
import com.alternacraft.headconverter.HeadConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

/**
 *
 * @author AlternaCraft
 */
public class GUI {

    private static String ID = PluginBase.INSTANCE.plugin().getName();
    
    protected static final String DEF_NAME = "Chest";
    protected static final int MAX_COLS = 9;
    protected static final int DEF_ROWS = 6;
    
    protected String title;
    protected int update_interval;

    protected final JSONObject meta;
    protected final Map<Integer, GUIItem> options;

    public GUI() {
        this(DEF_NAME, 0, new JSONObject(), new HashMap());
    }

    public GUI(String title) {
        this(title, 0, new JSONObject(), new HashMap());
    }

    public GUI(GUI gui) {
        this(gui.getTitle(), gui.getUpdate_interval(), gui.getMeta(), gui.getOptions());
    }

    public GUI(String title, int ui, JSONObject meta, Map<Integer, GUIItem> options) {
        this.title = title;
        this.update_interval = ui;
        this.meta = new JSONObject(meta);
        this.options = options.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new GUIItem(e.getValue())
                ));
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
        this.addItem(slot, item, true);
    }

    public void addItem(int slot, GUIItem item, boolean max_slots) {
        if (slot < 0 || (slot >= this.getMaxSlots() && max_slots)) {
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
        Inventory inventory = Bukkit.createInventory(null, this.getSlots(), meta_title);
        int v = (int) (4L * HeadConverter.getRateLimit() + 1L);
        options.entrySet().forEach(i -> {
            ItemStack complete = null;
            final String uuid = i.getValue().getPlayerHead();
            try {
                complete = i.getValue().getCompleteItem();
            } catch (SkinNotLoadedException ex) {
                complete = ex.getItemStack();
                new RefreshTask(UUID.randomUUID().toString(), 5).registerUpdate(v, (d) -> {
                    boolean contains = HeadConverter.containsUUID(uuid);
                    if (contains) {
                        GUIItem.setSkin(inventory.getItem(i.getKey()), HeadConverter.getB64(uuid));
                    }
                    return !contains;
                });
            } finally {
                inventory.setItem(i.getKey(), complete);
            }
        });
        return inventory;
    }

    protected String addMetaToTitle() {
        StringBuilder sb = new StringBuilder(this.title);
        JSONObject aux_meta = this.getMeta();
        aux_meta.put(GUIUtils.CI_KEY, ID);
        aux_meta.put(GUIUtils.UPDATE_INTERVAL, this.update_interval);
        sb.append(HiddenStringUtils.encodeString(aux_meta.toString()));
        return sb.toString();
    }

    public void fillWith(GUIItem item) {
        for (int i = 0; i < this.getMaxSlots(); i++) {
            if (!this.options.containsKey(i)) {
                this.options.put(i, new GUIItem(item));
            }
        }
    }

    public void cleanInventory() {
        this.options.clear();
    }

    public final int getSlots() {
        return this.getRows() * this.getCols();
    }

    public final int getCols() {
        return MAX_COLS;
    }

    public int getRows() {
        return DEF_ROWS;
    }

    public int getMaxSlots() {
        return this.getSlots();
    }

    public static void setID(String id) {
        ID = id;
    }

    public static String getID() {
        return ID;
    }    
}
