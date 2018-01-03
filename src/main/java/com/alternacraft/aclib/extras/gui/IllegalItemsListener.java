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

import com.alternacraft.aclib.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author AlternaCraft
 */
public class IllegalItemsListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player pl = event.getPlayer();
        for (ItemStack item : pl.getInventory().getContents()) {
            if (item == null) continue;
            if (GUIItem.isGUIItem(item)) {
                MessageManager.logDebug("Removed " + item.getType().name() 
                        + " from " + pl.getName() + "'s inventory");
                pl.getInventory().remove(item);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onInteract(InventoryClickEvent event) {
        ItemStack is = event.getCurrentItem();
        if (is == null || is.getItemMeta() == null 
                || event.getClickedInventory() == null) {
            return;
        }
        if (GUIItem.isGUIItem(is) && !GUIUtils.isCustom(event.getClickedInventory().getName())) {
            MessageManager.logDebug("Removed " + is.getType().name() + " from inventory!");
            event.setCancelled(true);
            event.getClickedInventory().remove(is);
        }
    }
}
