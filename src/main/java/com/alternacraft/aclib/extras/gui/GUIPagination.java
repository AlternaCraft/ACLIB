package com.alternacraft.aclib.extras.gui;

import com.alternacraft.aclib.langs.Lang;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author AlternaCraft
 */
public class GUIPagination extends GUI {
    
    public GUIPagination(String title) {
        super(title);
    }
    
    public void addControls(int page, int max_size, Lang lang, String... subkeys) {
        if (page > 0) {
            GUIItem item = new GUIItem(new ItemStack(Material.ARROW),
                    GUIMessages.PREVIOUS.getText(lang));
            item.addInfo(GUIMessages.PREVIOUS_ACTION.getText(lang));
            item.addMeta(GUIUtils.KEY_META, GUIUtils.PREVIOUS_PAGE_META);
            item.addMeta("page", String.valueOf(page));
            for (int i = 0; i < subkeys.length; i++) {
                item.addMeta("key_" + i, subkeys[i]);
            }
            this.options.put(GUIUtils.calculateSlot(6, 4, this.getRows(), this.getCols()), item);
        }
        if (max_size / this.getMaxSlots() > (page + 1)) {
            GUIItem item = new GUIItem(new ItemStack(Material.ARROW),
                    GUIMessages.NEXT.getText(lang));
            item.addInfo(GUIMessages.NEXT_ACTION.getText(lang));
            item.addMeta(GUIUtils.KEY_META, GUIUtils.NEXT_PAGE_META);
            item.addMeta("page", String.valueOf(page));
            for (int i = 0; i < subkeys.length; i++) {
                item.addMeta("key_" + i, subkeys[i]);
            }
            this.options.put(GUIUtils.calculateSlot(6, 6, this.getRows(), this.getCols()), item);
        }
    }
    
    public void addExitItem(Lang lang) {
        GUIItem item = new GUIItem(new ItemStack(Material.DOUBLE_PLANT),
                GUIMessages.EXIT.getText(lang));
        item.addInfo(GUIMessages.EXIT_ACTION.getText(lang));
        item.addMeta(GUIUtils.KEY_META, GUIUtils.CLOSE_ACTION_META);
        this.options.put(GUIUtils.calculateSlot(6, 5, this.getRows(), this.getCols()), item);
    }
    
    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public int getCols() {
        return 9;
    }

    @Override
    public int getMaxSlots() {
        return 4 * this.getCols();
    }    
}
