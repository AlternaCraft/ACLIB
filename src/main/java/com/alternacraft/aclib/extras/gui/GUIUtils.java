package com.alternacraft.aclib.extras.gui;

import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.inventory.InventoryView;

/**
 *
 * @author AlternaCraft
 */
public class GUIUtils {

    // Inventory identificator
    public static final String CI_META = UUID.randomUUID().toString();    

    // Meta key
    public static final String KEY_META = "key";
    
    // Default options
    public static final String CLOSE_ACTION_META = "cam";
    public static final String NEXT_PAGE_META = "npm";
    public static final String PREVIOUS_PAGE_META = "ppm";
    public static final String CI_PAGINATION_META = "cip";

    public static int calculateSlot(int i, int j, int ROWS, int COLS) {
        if (i > 0 && i <= ROWS && j > 0 && j <= COLS) {
            return i * COLS - (COLS - j) - 1;
        } else {
            return -1;
        }
    }

    public static <T> List<T> paginate(List<T> list, int page, int max_slots) {
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
    public static boolean isCustom(String title) {
        String str = getHiddenString(title);
        return str != null && str.equals(CI_META);
    }
    
    /**
     * Returns the hidden string.
     * 
     * @param str Complete string
     * 
     * @return Hidden string or null.
     */
    public static String getHiddenString(String str) {
        boolean has_a_gift = HiddenStringUtils.hasHiddenString(str);
        return (has_a_gift) ? HiddenStringUtils.extractHiddenString(str) : null;
    }
    
    /**
     * Closes all the custom active inventories.
     */
    public static void closeInventories() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            InventoryView inv = p.getOpenInventory();
            if (inv != null && isCustom(inv.getTopInventory().getName())) {
                inv.close();
            }
        });
    }
}
