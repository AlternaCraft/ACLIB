package com.alternacraft.aclib.extras.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.json.simple.JSONObject;

/**
 *
 * @author AlternaCraft
 */
public class GUIItem {

    private ItemStack item;

    private String title;
    private boolean glow;
    private String player_head;

    private List<String> info;
    private JSONObject meta;

    public GUIItem(ItemStack item) {
        this(item, null, false, null);
    }

    public GUIItem(ItemStack item, String title) {
        this(item, title, false, null);
    }

    public GUIItem(ItemStack item, String title, boolean glow, String player_head) {
        this.item = GUIUtils.removeAttributes(item);

        this.title = title;
        this.glow = glow;
        this.player_head = player_head;

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

    /**
     * Set skull owner.
     *
     * @param ow Player name
     */
    public void setSkullOwner(String ow) {
        SkullMeta smeta = (SkullMeta) this.item.getItemMeta();
        smeta.setOwner(ow);
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

    public ItemStack getCompleteItem() {
        ItemStack aux = new ItemStack(this.item);
        ItemMeta metaaux = aux.getItemMeta();
        String aux_title = this.title;
        if (this.title == null) {
            aux_title = " ";
        }
        metaaux.setDisplayName(aux_title + ((this.meta.size() > 0)
                ? HiddenStringUtils.encodeString(this.meta.toString()) : ""));
        if (this.glow) {
            metaaux.addEnchant(Enchantment.DURABILITY, 1, true);
        }
        metaaux.setLore(GUIUtils.parseLoreLines(this.info));
        aux.setItemMeta(metaaux);
        return aux;
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

    public JSONObject getMeta() {
        return meta;
    }
}
