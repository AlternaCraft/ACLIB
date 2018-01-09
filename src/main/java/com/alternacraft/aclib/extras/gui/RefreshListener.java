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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 *
 * @author AlternaCraft
 */
public class RefreshListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onOpen(InventoryOpenEvent event) {
        String title = event.getInventory().getTitle();
        Player pl = (Player) event.getPlayer();

        if (GUIUtils.isCustom(title)) {
            int v = GUIUtils.getUpdateInterval(title);
            if (v > 0) {
                MessageManager.logDebug("Update task thrown for " + pl.getName());
                new RefreshTask(pl.getUniqueId().toString(), v).registerUpdate();
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onClose(InventoryCloseEvent event) {
        String title = event.getInventory().getTitle();
        Player pl = (Player) event.getPlayer();

        if (GUIUtils.isCustom(title)) {
            int v = GUIUtils.getUpdateInterval(title);
            if (v > 0) {
                MessageManager.logDebug("Update task canceled for " + pl.getName());
                RefreshTask.cancel(pl.getUniqueId().toString());
            }
        }
    }
}
