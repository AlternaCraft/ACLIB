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
package com.alternacraft.aclib.extras;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.aclib.langs.LangInterface;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class to reduce the spam of repeated messages.
 *
 * @author AlternaCraft
 */
public class MessageIntervals {

    private static long MUTE_TIME = 3 * 1000; // Seconds

    /**
     * const LAST to check the time when it was sent
     */
    private static final Map<UUID, Map<String, Long>> LAST = new HashMap<>();

    public static void sendMessage(Player pl, LangInterface li, Lang lang) {
        sendMessage(pl, li.getText(lang));
    }

    /**
     * Sends a message avoiding spam.
     *
     * @param pl      Player
     * @param message Message
     */
    public static void sendMessage(Player pl, String message) {
        UUID playerUUID = pl.getUniqueId();

        if (!LAST.containsKey(playerUUID)) {
            LAST.put(playerUUID, new HashMap<>());
        }

        if (LAST.get(playerUUID).containsKey(message)) {
            long last = LAST.get(playerUUID).get(message);
            if (new Date(last + MUTE_TIME).after(new Date())) {
                return;
            }
        }

        MessageManager.sendPluginMessage(pl, message);
        LAST.get(playerUUID).put(message, new Date().getTime());
    }

    /**
     * Redefines the mute time in seconds
     *
     * @param time Seconds
     */
    public static void redefineMuteTime(long time) {
        MUTE_TIME = time * 1000;
    }
}
