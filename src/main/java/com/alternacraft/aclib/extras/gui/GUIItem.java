package com.alternacraft.aclib.extras.gui;

import java.util.ArrayList;
import java.util.List;
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
    private List<String> info;
    private JSONObject meta;

    public GUIItem(ItemStack item) {
        this(item, null);
    }

    public GUIItem(ItemStack item, String title) {
        this.item = removeAttributes(item);
        this.title = title;
        this.info = new ArrayList<>();
        this.meta = new JSONObject();
    }

    private ItemStack removeAttributes(ItemStack item) {
        ItemMeta iM = item.getItemMeta();
        iM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(iM);
        return item;
    }

    public void addInfo(String msg) {
        this.info.add(msg);
    }

    public void addMeta(String id, String meta) {
        this.meta.put(id, meta);
    }

    public ItemStack getCompleteItem() {
        ItemStack aux = new ItemStack(this.item);
        ItemMeta metaaux = aux.getItemMeta();
        if (this.title != null) {
            metaaux.setDisplayName(this.title + ((this.meta.size() > 0) ? 
                    HiddenStringUtils.encodeString(this.meta.toString()) : ""));
        }
        metaaux.setLore(this.info);
        aux.setItemMeta(metaaux);
        return aux;
    }
    
    public ItemStack getItem() {
        return this.item;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getInfo() {
        return info;
    }

    public JSONObject getMeta() {
        return meta;
    }    
}
