/*
 * Copyright (C) 2016 AlternaCraft
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
package com.alternacraft.aclib;

import com.alternacraft.aclib.utils.StringsUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageManager {
    public static void log(String message) {
        String[] messages = message.split("\n");
        for (String _message : messages) {
            Bukkit.getConsoleSender().sendMessage(
                    prepareString(_message)
            );
        }
    }

    public static void logInfo(String message) {
        String[] messages = message.split("\n");
        for (String _message : messages) {
            Bukkit.getConsoleSender().sendMessage(
                    prepareString("&eINFO: &f" + _message)
            );
        }
    }
    
    public static void logError(String message) {
        String[] messages = message.split("\n");
        for (String _message : messages) {
            Bukkit.getConsoleSender().sendMessage(
                    prepareString("&cERROR: &f" + _message)
            );
        }
    }

    public static void sendPlayer(Player player, String message) {
        String[] messages = message.split("\n");
        for (String _message : messages) {
            player.sendMessage(
                    prepareString(_message)
            );
        }
    }

    public static void sendCommandSender(CommandSender cs, String message) {
        String[] messages = message.split("\n");
        for (String _message : messages) {
            cs.sendMessage(
                    prepareString(_message)
            );
        }
    }

    public static String prepareString(String message) {
        return StringsUtils.translateColors(PluginBase.INSTANCE.pluginPrefix() + message);
    }
}
