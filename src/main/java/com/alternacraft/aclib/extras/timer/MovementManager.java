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
package com.alternacraft.aclib.extras.timer;

import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.listeners.HandlersRegisterer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementManager implements Listener {

    private static final int AFK_DEF_TIME = 5;
    private static final int DISABLED_AFK = 0;
    
    private static int timeThreshold = AFK_DEF_TIME;
    
    private final TimedPlayer tp;    
    private long lastMovement; 
        
    public MovementManager(TimedPlayer tp) {
        this.tp = tp;
        this.lastMovement = 0;
    }
    
    public void registerListener() {
        // Register listener
        HandlersRegisterer.load(this);
        Bukkit.getServer().getPluginManager().registerEvents(
                this, PluginBase.INSTANCE.plugin()
        );
    }

    public boolean isAFK() {
        // Returns 'DISABLED_AFK' if AFKChecker is disabled
        return (!hasLastMovement()) ? false : getAFKTime() > DISABLED_AFK;
    }

    /**
     * Time from last movement to now minus minimum time to consider a player as AFK.
     *
     * @return Time
     */
    public int getAFKTime() {
        long currTime = System.currentTimeMillis();
        int timeDiff = (int) ((currTime - this.lastMovement) / 1000L);
        return (timeThreshold > DISABLED_AFK) ? timeDiff - timeThreshold : DISABLED_AFK;
    }

    /**
     * 
     * @return 
     */
    public boolean hasLastMovement() {
        return this.lastMovement > 0;
    }

    /**
     * Get last movement.
     *
     * @return Time in millis
     */
    public long getLastMovement() {
        return this.lastMovement;
    }

    /**
     * Save last movement.
     */
    public void addLastMovement() {
        if (isAFK()) {
            this.tp.setAFKTime(this.tp.getAFKTime() + getAFKTime());
        }
        this.lastMovement = System.currentTimeMillis();
    }

    /**
     * 
     */
    public void removeLastMovement() {
        this.lastMovement = 0;
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerMoves(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();        
        if (event.getPlayer().getUniqueId().equals(this.tp.getUniqueId())) {
            if (from.getX() != to.getX() || from.getY() != to.getY() 
                    || from.getZ() != to.getZ()) {
                this.addLastMovement();
            }
        }
    }    
    
    /**
     * 
     * @param time Minutes
     */
    public static void setAFKTime(int time) {
        timeThreshold = time * 60;
    }
}
