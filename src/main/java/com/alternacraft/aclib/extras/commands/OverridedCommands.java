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

package com.alternacraft.aclib.extras.commands;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 *
 * @author AlternaCraft
 */
public class OverridedCommands implements Listener {
   
    private static final String ARGUMENTS = "(\\s(.+))*";    
    private static final String COMMAND_SLASH = "\\/";
    
    private static final Map<String, PreparedCommand> OVERRIDED_COMMANDS = new HashMap();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage().toLowerCase();
        Player pl = event.getPlayer();
        
        for (Map.Entry<String, PreparedCommand> entry : OVERRIDED_COMMANDS.entrySet()) {
            if (msg.matches(entry.getKey())) {
                event.setCancelled(true);                
                entry.getValue().execute(pl, entry.getValue().getName(), new String[0]);
                return;
            }
        }
    }
    
    public static void register(PreparedCommand dc, boolean with_args) {
        String reduced_aliases = (dc.getAliases().size() > 0) ? 
                new StringBuilder("|").append(String.join("|", dc.getAliases())).toString() : "";
        OVERRIDED_COMMANDS.put(
                new StringBuilder(COMMAND_SLASH)
                        .append("(")
                        .append(dc.getName())
                        .append(reduced_aliases)
                        .append(")")
                        .append((with_args) ? ARGUMENTS : "")
                        .toString(),
                dc
        );
    }
}
