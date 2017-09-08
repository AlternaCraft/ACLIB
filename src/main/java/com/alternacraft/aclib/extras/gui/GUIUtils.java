package com.alternacraft.aclib.extras.gui;

import com.alternacraft.aclib.utils.StringsUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author AlternaCraft
 */
public class GUIUtils {

    private static int MAX_LORE_LENGTH = 45;

    // Inventory identificator
    public static final String CI_KEY = "ci_key";
    public static final String CI_META = UUID.randomUUID().toString();

    // Meta key
    public static final String KEY_META = "key";

    // Default options
    public static final String CLOSE_ACTION_META = "cam";
    public static final String NEXT_PAGE_META = "npm";
    public static final String PREVIOUS_PAGE_META = "ppm";
    public static final String CI_PAGINATION_META = "cip";

    // Refresh options
    public static final String UPDATE_INTERVAL = "ui";

    public static final int calculateSlot(int i, int j, int ROWS, int COLS) {
        if (i > 0 && i <= ROWS && j > 0 && j <= COLS) {
            return i * COLS - (COLS - j) - 1;
        } else {
            return -1;
        }
    }

    public static final <T> List<T> paginate(List<T> list, int page, int max_slots) {
        int from = page * (max_slots - 1);
        int to = (page + 1) * (max_slots - 1);
        if (to > list.size()) {
            to = list.size();
        }
        return list.subList(from, to);
    }

    /**
     * Returns if an inventory is custom.
     *
     * @param title Inventory title
     *
     * @return True if it is an inventory custom; False if not
     */
    public static final boolean isCustom(String title) {
        String str = getHiddenString(title);
        if (str != null) {
            try {
                JSONObject data = (JSONObject) new JSONParser().parse(str);
                return (data.get(CI_KEY) != null)
                        ? data.get(CI_KEY).equals(CI_META) : false;
            } catch (ParseException ex) {
            }
        }
        return false;
    }

    /**
     * Get update interval value.
     *
     * @param title Inventory title
     *
     * @return Update interval value
     */
    public static final int getUpdateInterval(String title) {
        int def = 0;

        String str = getHiddenString(title);
        if (str != null) {
            try {
                JSONObject data = (JSONObject) new JSONParser().parse(str);
                def = (data.get(UPDATE_INTERVAL) != null)
                        ? Integer.valueOf(data.get(UPDATE_INTERVAL).toString()) : def;
            } catch (ParseException ex) {
            }
        }

        return def;
    }

    /**
     * Returns the hidden string.
     *
     * @param str Complete string
     *
     * @return Hidden string or null.
     */
    public static final String getHiddenString(String str) {
        boolean has_a_gift = HiddenStringUtils.hasHiddenString(str);
        return (has_a_gift) ? HiddenStringUtils.extractHiddenString(str) : null;
    }

    /**
     * Closes all the custom active inventories.
     */
    public static final void closeInventories() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            InventoryView inv = p.getOpenInventory();
            if (inv != null && isCustom(inv.getTopInventory().getName())) {
                inv.close();
            }
        });
    }

    /**
     * Creates a new instance of GUI
     *
     * @param gui GUI to clone
     *
     * @return GUI cloned
     */
    public static final GUI cloneGUI(GUI gui) {
        GUI r_gui = new GUI(gui.getTitle()) {
            @Override
            public int getRows() {
                return gui.getRows();
            }

            @Override
            public int getCols() {
                return gui.getCols();
            }

            @Override
            public int getMaxSlots() {
                return gui.getMaxSlots();
            }
        };

        r_gui.setUpdate_interval(gui.getUpdate_interval());
        gui.getMeta().forEach((k, v)
                -> r_gui.addMeta(k.toString(), v));

        gui.getOptions().entrySet().forEach(option -> {
            GUIItem item = new GUIItem(option.getValue().getItem());

            item.setTitle(option.getValue().getTitle());
            item.setGlow(option.getValue().isGlow());
            item.setPlayerHead(option.getValue().getPlayerHead());

            option.getValue().getMeta().forEach((k, v)
                    -> item.addMeta(k.toString(), v));

            item.setInfo(new ArrayList<>(option.getValue().getInfo()));

            r_gui.addItem(option.getKey(), item);
        });
        return r_gui;
    }
    
    /**
     * Returns the not null items, mapped by position
     * 
     * @param items Inventory items
     * 
     * @return Map with the not null items
     */
    public static final Map<Integer, ItemStack> convertInventory(ItemStack[] items) {
        Map<Integer, ItemStack> aux = new HashMap<>();
        IntStream.range(0, items.length)
                .filter(idx -> items[idx] != null)
                .forEach(idx -> aux.put(idx, items[idx]));
        return aux;
    }

    /**
     * Returns the not null items which are also steve skull, mapped by position
     * 
     * @param items Inventory items
     * 
     * @return Map with the not null items
     */
    public static final Map<Integer, ItemStack> findSteveSkulls(ItemStack[] items) {
        Map<Integer, ItemStack> aux = new HashMap<>();
        IntStream.range(0, items.length)
                .filter(idx -> items[idx] != null
                && items[idx].getType().equals(Material.SKULL_ITEM)
                && items[idx].getDurability() == 3)
                .forEach(idx -> aux.put(idx, new ItemStack(items[idx])));
        return aux;
    }

    /**
     * Removes the item attributes.
     *
     * @param item ItemStack
     *
     * @return Parsed ItemStack
     */
    public static final ItemStack removeAttributes(ItemStack item) {
        ItemMeta iM = item.getItemMeta();
        Arrays.stream(ItemFlag.values()).forEach(iM::addItemFlags);
        item.setItemMeta(iM);
        return item;
    }

    /**
     * Format the messages to limit the text box size
     * 
     * @param lore Lore lines
     * 
     * @return Lore lines formatted
     */
    public static final List<String> parseLoreLines(List<String> lore) {
        List<String> aux = new ArrayList<>();
        lore.forEach(line -> {
            aux.addAll(recurrentParser(line));
        });
        return aux;
    }

    private static List<String> recurrentParser(String msg) {
        List<String> aux = new ArrayList<>();
        if (StringsUtils.stripColors(msg).length() > MAX_LORE_LENGTH) {
            // Cut before
            String sub = msg.substring(0, MAX_LORE_LENGTH);
            int cut = sub.lastIndexOf(" ");
            sub = sub.substring(0, cut);
            aux.add(sub);
            // Get previous color
            String last_color = "";
            Pattern pattern = Pattern.compile(".*(&\\w|§\\w)");
            Matcher m = pattern.matcher(sub);
            if (m.find()) {
                last_color = m.group(1);
                int start = m.start(1);
                if (start > 1 && sub.substring(start - 2, start).matches("&\\w|§\\w")) {
                    last_color = sub.substring(start - 2, start) + last_color;
                }
            }
            // Analyze new text line
            aux.addAll(recurrentParser(last_color + msg.substring(cut + 1)));
        } else {
            aux.add(msg);
        }
        return aux;
    }

    public static final void setMaxLoreLength(int length) {
        MAX_LORE_LENGTH = length;
    }
}
