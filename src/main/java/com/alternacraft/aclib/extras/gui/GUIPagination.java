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
            this.addItem(GUIUtils.calculateSlot(6, 4, this.getRows()), item, false);
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
            this.addItem(GUIUtils.calculateSlot(6, 6, this.getRows()), item, false);
        }
    }
    
    public void addExitItem(Lang lang) {
        GUIItem item = new GUIItem(new ItemStack(Material.DOUBLE_PLANT),
                GUIMessages.EXIT.getText(lang));
        item.addInfo(GUIMessages.EXIT_ACTION.getText(lang));
        item.addMeta(GUIUtils.KEY_META, GUIUtils.CLOSE_ACTION_META);
        this.addItem(GUIUtils.calculateSlot(6, 5, this.getRows()), item, false);
    }
    
    @Override
    public int getRows() {
        return 6;
    }
    
    @Override
    public int getMaxSlots() {
        return 4 * this.getCols();
    }    
}
