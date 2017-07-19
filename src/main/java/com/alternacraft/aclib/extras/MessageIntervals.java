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
package com.alternacraft.aclib.extras;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.langs.LangInterface;
import com.alternacraft.aclib.langs.Langs;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;

/**
 * Class to reduce the spam of repeated messages.
 * 
 * @author AlternaCraft
 */
public class MessageIntervals {
    
    private static long MUTE_TIME = 3 * 1000; // Seconds
    
    /** const LAST to check the time when it was sent */    
    private static final Map<UUID, Map<LangInterface, Long>> LAST = new HashMap<>();    
    
    public static void sendMessage(Player pl, LangInterface li, Langs lang) {
        UUID playerUUID = pl.getUniqueId();
        
        if (!LAST.containsKey(playerUUID)) {
            LAST.put(playerUUID, new HashMap<LangInterface, Long>());
        }
                
        if (LAST.get(playerUUID).containsKey(li)) {
            long last = LAST.get(playerUUID).get(li);
            if (new Date(last + MUTE_TIME).after(new Date())) {
                return;
            }
        }        
        
        MessageManager.sendPlayer(pl, li.getText(lang));        
        LAST.get(playerUUID).put(li, new Date().getTime());
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
