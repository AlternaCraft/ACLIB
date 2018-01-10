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

import com.alternacraft.aclib.MessageManager;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author AlternaCraft
 */
public class IllegalItemsListener implements Listener {

    private static final List<Function<ItemStack, Boolean>> VALIDATIONS = new ArrayList();
    
    static {
        VALIDATIONS.add((Function<ItemStack, Boolean>) (ItemStack t) -> {
            return GUIItem.isGUIItem(t);
        });
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player pl = event.getPlayer();
        for (ItemStack is : pl.getInventory().getContents()) {
            if (is == null) continue;
            if (VALIDATIONS.stream().anyMatch(v -> v.apply(is))) {
                MessageManager.logDebug("Removed " + is.getItemMeta().getDisplayName()
                        + " from " + pl.getName() + "'s inventory");
                pl.getInventory().remove(is);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPickUp(EntityPickupItemEvent event) {
        Item item = event.getItem();
        ItemStack is = item.getItemStack();
        if (is != null && is.getItemMeta() != null 
                    && VALIDATIONS.stream().anyMatch(v -> v.apply(is))) {
            MessageManager.logDebug("Removed " + is.getItemMeta().getDisplayName() 
                    + " from the ground");
            event.setCancelled(true);
            item.remove();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onHold(PlayerItemHeldEvent event) {
        Player pl = event.getPlayer();
        this.verify(event.getPreviousSlot(), pl);
        this.verify(event.getNewSlot(), pl);
    }
    
    private void verify(int slot, Player pl) {
        if (slot >= 0 && slot < pl.getInventory().getSize()) {
            ItemStack is = pl.getInventory().getItem(slot);
            if (is != null && is.getItemMeta() != null 
                    && VALIDATIONS.stream().anyMatch(v -> v.apply(is))) {
                MessageManager.logDebug("Removed " + is.getItemMeta().getDisplayName()
                        + " from " + pl.getName() + "'s inventory");
                pl.getInventory().remove(is);
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
        if (VALIDATIONS.stream().anyMatch(v -> v.apply(is)) 
                && !GUIUtils.isCustom(event.getClickedInventory().getName())) {
            MessageManager.logDebug("Removed " + is.getItemMeta().getDisplayName() 
                    + " from " + event.getClickedInventory().getHolder() + " inventory!");
            event.setCancelled(true);
            event.getClickedInventory().remove(is);
        }
    }
    
    public static void registerValidation(Function<ItemStack, Boolean> validation) {
        VALIDATIONS.add(validation);
    }
}
