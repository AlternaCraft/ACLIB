package com.alternacraft.aclib.extras.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

/**
 *
 * @author AlternaCraft
 */
public class GUIItem {

    private ItemStack item;
    private String title;
    private boolean glow;
    private List<String> info;
    private JSONObject meta;

    public GUIItem(ItemStack item) {
        this(item, null, false);
    }

    public GUIItem(ItemStack item, String title) {
        this(item, title, false);
    }
    
    public GUIItem(ItemStack item, String title, boolean glow) {
        this.item = GUIUtils.removeAttributes(item);
        this.title = title;
        this.info = new ArrayList<>();
        this.meta = new JSONObject();
    }

    public void addInfo(String msg) {
        this.info.add(msg);
    }
    
    /**
     * Clear current values and add the new ones.
     * 
     * @param info List with the values
     */
    public void setInfo(List<String> info) {
        this.info.clear();
        this.info.addAll(info);
    }

    public void addMeta(String id, Object meta) {
        this.meta.put(id, meta);
    }
    
    /**
     * Clear current values and add the new ones.
     * 
     * @param meta Map with the values
     */
    public void setMeta(Map<String, Object> meta) {
        this.meta.clear();
        this.meta.putAll(meta);
    }

    public ItemStack getCompleteItem() {
        ItemStack aux = new ItemStack(this.item);
        ItemMeta metaaux = aux.getItemMeta();
        if (this.title != null) {
            metaaux.setDisplayName(this.title + ((this.meta.size() > 0) ? 
                    HiddenStringUtils.encodeString(this.meta.toString()) : ""));
        }
        if (this.glow) {
            metaaux.addEnchant(Enchantment.DURABILITY, 1, true);
        }
        metaaux.setLore(this.info);
        aux.setItemMeta(metaaux);
        return aux;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGlow(boolean glow) {
        this.glow = glow;
    }
    
    public ItemStack getItem() {
        return this.item;
    }

    public String getTitle() {
        return title;
    }

    public boolean isGlow() {
        return glow;
    }

    public List<String> getInfo() {
        return info;
    }

    public JSONObject getMeta() {
        return meta;
    }
}
