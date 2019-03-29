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

import com.alternacraft.aclib.listeners.HandlersRegisterer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;

public class TimeManager implements Listener {

    private Set<TimedPlayer> players;

    public TimeManager() {
        this.players = new HashSet();
    }

    /**
     * Register a handler for player join/move/leave events.
     */
    public void registerHandler() {
        // Register listener
        HandlersRegisterer.load(this);
    }

    public boolean addPlayer(TimedPlayer tPlayer) {
        return this.players.add(tPlayer);
    }

    public TimedPlayer getPlayer(OfflinePlayer player) {
        return this.players
                .stream()
                .filter(p -> p.getUniqueId().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    public Set<TimedPlayer> getTimedPlayers() {
        return this.players;
    }

    public boolean hasPlayer(OfflinePlayer player) {
        return getPlayer(player) != null;
    }

    public void setTimedPlayers(Set<TimedPlayer> players) {
        this.players = players;
    }

    public void stopSessions() {
        this.players.forEach(TimedPlayer::stopSession);
    }

    public void startPlayerSession(Player pl) {
        TimedPlayer tPlayer;
        if (!this.hasPlayer(pl)) {
            tPlayer = new TimedPlayer(pl.getUniqueId());
            tPlayer.registerMMListener();
            this.addPlayer(tPlayer);
        } else {
            tPlayer = this.getPlayer(pl);
        }
        tPlayer.startSession();
        // First movement
        tPlayer.MM().addLastMovement();
    }

    public void stopPlayerSession(Player pl) {
        if (this.hasPlayer(pl)) {
            TimedPlayer tp = this.getPlayer(pl);
            tp.stopSession();
            // Avoid add afk time when player is disconnected
            tp.MM().removeLastMovement();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.startPlayerSession(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        this.stopPlayerSession(event.getPlayer());
    }
}
