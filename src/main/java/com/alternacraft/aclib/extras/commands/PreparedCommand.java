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

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.langs.CommandMessages;
import com.alternacraft.aclib.utils.Localizer;
import java.lang.reflect.Field;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 *
 * @author AlternaCraft
 */
public abstract class PreparedCommand extends Command implements Listener {

    private final int arguments;
    private final boolean only_player;

    public PreparedCommand(String cmd, String desc, String usage, String perm, short args,
            boolean only_player, String... aliases) {
        super(cmd, desc, usage, Arrays.asList(aliases));
        this.arguments = args;
        this.only_player = only_player;
    }

    public PreparedCommand(String cmd, String... aliases) {
        this(cmd, cmd, "/" + cmd, "", (short) -1, false, aliases);
    }

    public PreparedCommand(String cmd, String perm, String... aliases) {
        this(cmd, cmd, "/" + cmd, perm, (short) -1, false, aliases);
    }
    
    public PreparedCommand(PluginCommand pc, short args, boolean only_player) {
        this(pc.getName(), pc.getDescription(), pc.getUsage(), pc.getPermission(), 
                args, only_player, pc.getAliases().toArray(new String[pc.getAliases().size()]));
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] data) {
        if (this.only_player && !(sender instanceof Player)) {
            MessageManager.sendPluginMessage(sender, CommandMessages
                    .ONLY_PLAYERS.getText(Localizer.getLocale(sender)));
            return true;
        }
        if (this.getPermission() != null && !sender.hasPermission(this.getPermission())) {
            MessageManager.sendPluginMessage(sender, CommandMessages
                    .NO_PERMISSION.getText(Localizer.getLocale(sender)));
            return true;
        }
        if (this.arguments != -1 && data.length != this.arguments) {
            MessageManager.sendPluginMessage(sender, CommandMessages
                    .INVALID_ARGUMENTS.getText(Localizer.getLocale(sender)));
            return false;
        }
        return this.run(sender, cmd, data);
    }
    
    public abstract boolean run(CommandSender sender, String cmd, String[] args);

    public CommandExecutor getCommandExecutor() {
        return ((sender, command, label, args) -> {
            return this.execute(sender, label, args);
        });
    }
    
    public static void registerDynamicCommand(PreparedCommand dc, boolean or) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            if (commandMap.getCommand(dc.getName()) != null
                    && commandMap.getCommand(dc.getName()).isRegistered()) {
                OverridedCommands.register(dc, or);
            } else {
                commandMap.register(dc.getName(), dc);
            }
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex) {
            MessageManager.logError("Command " + dc.getName() + " hasn't been registered"
                    + "\nError: " + ex.getMessage());
        }
    }
}
