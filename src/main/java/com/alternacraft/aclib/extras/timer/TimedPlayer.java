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
package com.alternacraft.aclib.extras.timer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class TimedPlayer {

    private final MovementManager mm;
    
    private UUID uuid;
    private Set<Session> sessions;
    private Session activeSession;
    private long afkTime;

    public TimedPlayer(UUID uuid) {
        this.mm = new MovementManager(this);
        this.uuid = uuid;
        this.sessions = new HashSet();
        this.afkTime = 0;
    }
    
    public void registerMMListener() {
        this.mm.registerListener();
    }

    public boolean startSession() {
        Session session = new Session(System.currentTimeMillis());
        setActiveSession(session);
        return addSession(session);
    }

    public boolean addSession(Session session) {
        return this.sessions.add(session);
    }

    public boolean stopSession() {
        if (!hasSession()) {
            return false;
        }

        Session session = getSession();

        if (this.mm.isAFK()) {
            // Suma de todos los tiempos AFK más el actual
            setAFKTime(getAFKTime() + this.mm.getAFKTime());
        }

        session.setStopTime(System.currentTimeMillis() - getAFKTime() * 1000L);
        setActiveSession(null);
        setAFKTime(0);

        return true;
    }

    public long getTotalOnline() {
        long timeOnline = 0;

        for (Session s : this.sessions) {
            // If it is active session
            if (s.equals(getSession())) {
                timeOnline += (long) ((System.currentTimeMillis() - s.getStartTime()) / 1000L);
                timeOnline -= getAFKTime();

                if (this.mm.isAFK()) {
                    timeOnline -= this.mm.getAFKTime();
                }
            } else {
                timeOnline += (long) ((s.getStopTime() - s.getStartTime()) / 1000L);
            }
        }

        // Avoid negative values
        return (timeOnline < 0) ? 0 : timeOnline;
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getServer().getOfflinePlayer(getUniqueId());
    }

    public boolean hasSession() {
        return getSession() != null;
    }

    public boolean isCurrentlyAFK() {
        return this.mm.isAFK();
    }

    public Session getSession() {
        return activeSession;
    }

    public void setActiveSession(Session activeSession) {
        this.activeSession = activeSession;
    }

    public boolean removeSession(Session session) {
        return this.sessions.remove(session);
    }

    public void removeSessions() {
        this.sessions.clear();
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public void setUniqueId(UUID uuid) {
        this.uuid = uuid;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }

    public long getAFKTime() {
        return afkTime;
    }

    public void setAFKTime(long afkTime) {
        this.afkTime = afkTime;
    }

    public MovementManager MM() {
        return mm;
    }

    public class Session {

        private long startTime;
        private long stopTime;

        public Session(long startTime) {
            this(startTime, 0L);
        }

        public Session(long startTime, long stopTime) {
            this.startTime = startTime;
            this.stopTime = stopTime;
        }

        public long getStartTime() {
            return this.startTime;
        }

        public long getStopTime() {
            return this.stopTime;
        }

        public void setStartTime(long time) {
            this.startTime = time;
        }

        public void setStopTime(long time) {
            this.stopTime = time;
        }
    }
}
