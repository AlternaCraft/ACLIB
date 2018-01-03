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
package com.alternacraft.aclib.extras.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author AlternaCraft
 */
public class GUIItem {

    private static String ID = "ct_value";
    
    private ItemStack item;

    private String title;
    private boolean glow;
    private String player_head;

    private List<String> info;
    private JSONObject meta;

    public GUIItem(ItemStack item) {
        this(item, null, false, null, new ArrayList<>(), new JSONObject());
    }

    public GUIItem(ItemStack item, String title) {
        this(item, title, false, null, new ArrayList<>(), new JSONObject());
    }

    public GUIItem(GUIItem item) {
        this(item.getItem(), item.getTitle(), item.isGlow(), item.getPlayerHead(),
                item.getInfo(), item.getMeta());
    }

    public GUIItem(ItemStack item, String title, boolean glow, String player_head,
            List<String> info, JSONObject meta) {
        this.item = new ItemStack(item);

        this.title = title;
        this.glow = glow;
        this.player_head = player_head;

        this.info = new ArrayList<>(info);
        this.meta = new JSONObject(meta);
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

    /**
     * Set skull owner.
     *
     * @param ow Player name
     */
    @Deprecated
    public void setSkullOwner(String ow) {
        SkullMeta smeta = (SkullMeta) this.item.getItemMeta();
        smeta.setOwner(ow);
        this.item.setItemMeta(smeta);
    }

    /**
     * Set skull owner
     *
     * @param ow Player
     */
    public void setSkullOwner(OfflinePlayer ow) {
        SkullMeta smeta = (SkullMeta) this.item.getItemMeta();
        smeta.setOwningPlayer(ow);
        this.item.setItemMeta(smeta);
    }

    /**
     * Set a potion effect.
     *
     * @param effect Potion type
     */
    public void setPotionEffect(PotionType effect) {
        PotionMeta pmeta = (PotionMeta) this.item.getItemMeta();
        pmeta.setBasePotionData(new PotionData(effect));
        this.item.setItemMeta(pmeta);
    }

    public ItemStack getCompleteItem(boolean removeAttributes, ItemFlag... flags) {
        ItemStack aux = new ItemStack(this.item);
        ItemMeta metaaux = aux.getItemMeta();
        String aux_title = this.title;
        if (this.title == null) {
            aux_title = " ";
        }
        this.meta.put(GUIUtils.CIT_KEY, ID);
        metaaux.setDisplayName(aux_title + ((this.meta.size() > 0)
                ? HiddenStringUtils.encodeString(this.meta.toString()) : ""));
        if (this.glow) {
            metaaux.addEnchant(Enchantment.DURABILITY, 1, true);
        }
        metaaux.setLore(GUIUtils.parseLoreLines(this.info));
        aux.setItemMeta(metaaux);
        return (removeAttributes) ? GUIUtils.removeAttributes(aux, flags) : aux;
    }
    
    public ItemStack getCompleteItem() {
        return this.getCompleteItem(true, ItemFlag.values());
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isGlow() {
        return glow;
    }

    public void setGlow(boolean glow) {
        this.glow = glow;
    }

    public String getPlayerHead() {
        return player_head;
    }

    public void setPlayerHead(String player_head) {
        this.player_head = player_head;
    }

    public List<String> getInfo() {
        return info;
    }

    public Object getMetaBy(String key) {
        return meta.get(key);
    }

    public JSONObject getMeta() {
        return meta;
    }
    
    public static String getID() {
        return ID;
    }

    public static boolean isGUIItem(ItemStack is) {
        if (is == null || is.getItemMeta() == null) return false;
        String str = GUIUtils.getHiddenString(is.getItemMeta().getDisplayName());
        if (str != null) {
            try {
                JSONObject data = (JSONObject) new JSONParser().parse(str);
                return (data.get(GUIUtils.CIT_KEY) != null)
                        ? data.get(GUIUtils.CIT_KEY).equals(ID) : false;
            } catch (ParseException ex) {}
        }
        return false;
    }
}
