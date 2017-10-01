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
package com.alternacraft.aclib;

import com.alternacraft.aclib.langs.LangInterface;
import com.alternacraft.aclib.langs.LangManager;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.aclib.utils.StringsUtils;
import java.util.List;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Class for sending messages.
 *
 * @author AlternaCraft
 */
public class MessageManager {

    private static final String BREAK_LINE = "\\r\\n|\\n|\\r";
    
    private static final String ERROR = "&cERROR: &f";
    private static final String INFO = "&eINFO: &f";
    private static final String DEBUG = "&6DEBUG: &f";

    /**
     * Logs a message into CONSOLE with a prefix.
     *
     * @param message The message
     * @param prefix Prefix message
     */
    public static void log(String message, String prefix) {
        String[] messages = message.split(BREAK_LINE);
        for (String _message : messages) {
            Bukkit.getConsoleSender().sendMessage(
                    prepareString(prefix + _message)
            );
        }
    }

    /**
     * Logs a message into CONSOLE.
     *
     * @param message The message
     */
    public static void log(String message) {
        log(message, "");
    }

    /**
     * Logs a message into CONSOLE with 'INFO' prefix.
     *
     * @param message The message
     */
    public static void logInfo(String message) {
        log(message, INFO);
    }

    /**
     * Logs a message into CONSOLE with 'DEBUG' prefix if debug mode is enabled.
     *
     * @param message The message
     */
    public static void logDebug(String message) {
        if (PluginBase.INSTANCE.isDebug()) {
            log(message, DEBUG);
        }
    }

    /**
     * Logs a message into CONSOLE with 'ERROR' prefix.
     *
     * @param message The message
     */
    public static void logError(String message) {
        log(message, ERROR);
    }

    /**
     * Logs the message from an array into CONSOLE
     *
     * @param messages Array of messages
     */
    public static void logArrayError(Object[] messages) {
        for (Object msg : messages) {
            logError(msg.toString());
        }
    }

    /**
     * Send a message to all the players.
     *
     * @param message Message to translate
     * @param replace items in order to replace variables
     */
    public static void sendServer(LangInterface message, String... replace) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            String msg = message.getText(Localizer.getLocale(p));
            List<String> variables = LangManager.getVariables(msg);
            for (int i = 0; i < replace.length; i++) {
                msg = msg.replace(variables.get(i), replace[i]);
            }
            sendPlayer(p, msg);
        });
    }

    /**
     * Sends a message to a player.
     *
     * @param player The player
     * @param message The message
     */
    public static void sendPlayer(Player player, String message) {
        String[] messages = message.split(BREAK_LINE);
        for (String _message : messages) {
            player.sendMessage(prepareString(_message));
        }
    }

    /**
     * Sends a message to a command sender.
     *
     * @param cs The command sender
     * @param message The message
     */
    public static void sendCommandSender(CommandSender cs, String message) {
        String[] messages = message.split(BREAK_LINE);
        for (String _message : messages) {
            cs.sendMessage(
                    prepareString(_message)
            );
        }
    }
    
    public static void sendTextComponent(Player pl, TextComponent text) {
        pl.spigot().sendMessage(text);
    }
    
    /**
     * Sends a clean message to a command sender
     * 
     * @param cs The command sender
     * @param message The message
     */
    public static void sendMessage(CommandSender cs, String message) {
        String[] messages = message.split(BREAK_LINE);
        for (String _message : messages) {
            cs.sendMessage(StringsUtils.translateColors(_message));
        }
    }

    /**
     * Prepares a message, adding the plugin prefix and the space after it, all
     * before the message.
     *
     * @param message The message
     * @return The prepared message
     */
    public static String prepareString(String message) {
        return StringsUtils.translateColors(
                PluginBase.INSTANCE.pluginPrefix() + " &r" + message);
    }
}
