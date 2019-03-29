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
import com.alternacraft.aclib.exceptions.PlayerNotFoundException;
import com.alternacraft.aclib.exceptions.SkinNotLoadedException;
import com.alternacraft.headconverter.HeadConverter;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
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

    public List<String> getInfo() {
        return info;
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

    public Object getMetaBy(String key) {
        return meta.get(key);
    }

    public JSONObject getMeta() {
        return meta;
    }

    public ItemStack getCompleteItem(boolean removeAttributes, ItemFlag... flags)
            throws SkinNotLoadedException {
        final ItemStack aux = new ItemStack(this.item);
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
        if (removeAttributes) GUIUtils.removeAttributes(aux, flags);
        if (this.isPlayerHead()) {
            if (Bukkit.getPluginManager().isPluginEnabled("HeadConverter")) {
                if (HeadConverter.containsUUID(this.player_head)) {
                    setSkin(aux, HeadConverter.getB64(this.player_head));
                } else {
                    throw new SkinNotLoadedException(aux);
                }
            } else {
                if (this.player_head.matches(PluginBase.UUID_FORMAT)) {
                    try {
                        setSkullOwner(aux, UUID.fromString(this.player_head));
                    } catch (PlayerNotFoundException ex) {
                    }
                } else {
                    setSkullOwner(aux, this.player_head);
                }
            }
        }
        return aux;
    }

    public ItemStack getCompleteItem() throws SkinNotLoadedException {
        return this.getCompleteItem(true, ItemFlag.values());
    }

    public ItemStack getItem() {
        return new ItemStack(this.item);
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

    public void setPlayerHead(UUID uuid) {
        if (Bukkit.getPluginManager().isPluginEnabled("HeadConverter"))
            HeadConverter.addHead(uuid);
        this.setPlayerHead(uuid.toString());
    }

    public void setPlayerHead(String value) {
        this.player_head = value;
    }

    public boolean isPlayerHead() {
        return this.item.getType().equals(Material.PLAYER_HEAD)
                && this.player_head != null;
    }

    public static String getID() {
        return ID;
    }

    /**
     * Set a potion effect.
     *
     * @param is     ItemStack
     * @param effect Potion type
     */
    public static void setPotionEffect(ItemStack is, PotionType effect) {
        PotionMeta pmeta = (PotionMeta) is.getItemMeta();
        pmeta.setBasePotionData(new PotionData(effect));
        is.setItemMeta(pmeta);
    }

    public static boolean isGUIItem(ItemStack is) {
        if (is == null || is.getItemMeta() == null) return false;
        String str = GUIUtils.getHiddenString(is.getItemMeta().getDisplayName());
        if (str != null) {
            try {
                JSONObject data = (JSONObject) new JSONParser().parse(str);
                return data.get(GUIUtils.CIT_KEY) != null;
            } catch (ParseException ex) {
            }
        }
        return false;
    }

    /**
     * Set skull owner.
     *
     * @param aux
     * @param uuid Player uuid
     * @throws com.alternacraft.aclib.exceptions.PlayerNotFoundException If player
     *                                                                   doesn't exist
     */
    public void setSkullOwner(ItemStack aux, UUID uuid) throws PlayerNotFoundException {
        if (!(aux.getItemMeta() instanceof SkullMeta)) return;
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
        if (op == null || op.getName() == null) {
            throw new PlayerNotFoundException("Invalid player " + op);
        }
        SkullMeta smeta = (SkullMeta) aux.getItemMeta();
        smeta.setOwningPlayer(op);
        aux.setItemMeta(smeta);
    }

    /**
     * Set skull owner
     *
     * @param aux
     * @param ow  Player name
     * @deprecated Use offline player instead of name.
     */
    @Deprecated
    public void setSkullOwner(ItemStack aux, String ow) {
        if (!(aux.getItemMeta() instanceof SkullMeta)) return;
        SkullMeta smeta = (SkullMeta) aux.getItemMeta();
        smeta.setOwner(ow);
        aux.setItemMeta(smeta);
    }

    public static void setSkin(ItemStack head, String b64) {
        SkullMeta localSkullMeta = (SkullMeta) head.getItemMeta();
        GameProfile localGameProfile = new GameProfile(UUID.randomUUID(), null);
        localGameProfile.getProperties().put("textures", new Property("textures", b64));
        Field localField;
        try {
            localField = localSkullMeta.getClass().getDeclaredField("profile");
            localField.setAccessible(true);
            localField.set(localSkullMeta, localGameProfile);
            localField.setAccessible(false);
        } catch (IllegalAccessException | IllegalArgumentException
                | NoSuchFieldException | SecurityException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        head.setItemMeta(localSkullMeta);
    }
}
