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
package com.alternacraft.aclib.commands;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.langs.CommandMessages;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.aclib.utils.MapUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Define command/subcommand listener
 * 
 * @author AlternaCraft
 */
public class CommandListener implements CommandExecutor {

    private final Map<SubCommand, SubCommandExecutor> arguments = new LinkedHashMap<>();

    private final String command;
    private final String prefix;
    private final JavaPlugin plugin;

    /**
     * Main constructor
     *
     * @param command Main command
     * @param prefix Permissions prefix
     * @param plugin JavaPlugin
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public CommandListener(String command, String prefix, JavaPlugin plugin) {
        this.command = command;
        this.prefix = prefix;
        this.plugin = plugin;

        this.plugin.getCommand(command).setExecutor(this);
    }

    /**
     * Add a subcommand
     *
     * @param argument SubCommand
     * @param executor Executor
     */
    public void addSubCommand(SubCommand argument, SubCommandExecutor executor) {
        arguments.put(argument, executor);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String str, String[] args) {
        if (args.length == 0) {
            args = new String[]{""}; // Custom template
        }

        Langs l = Localizer.getLocale(cs);

        SubCommand cmdArgument = MapUtils.findArgument(arguments, args[0]);
        if (cmdArgument != null) {
            if (cs instanceof Player && !cmdArgument.getCommand().isEmpty()) {
                String permission = this.prefix + "." + cmdArgument.getCommand();
                if (!((Player) cs).hasPermission(permission)) {
                    MessageManager.sendCommandSender(cs, CommandMessages.NO_PERMISSION.getText(l));
                }
            }
            if (!arguments.get(cmdArgument).execute(cs, args)) {
                MessageManager.sendCommandSender(cs, CommandMessages.COMMAND_USAGE
                        .getText(l).replace("%USAGE%", cmdArgument.getUsage()));
            }
        } else {
            MessageManager.sendCommandSender(cs, CommandMessages.INVALID_ARGUMENTS.getText(l));
        }

        return true;
    }

    // <editor-fold defaultstate="collapsed" desc="GETTERS">
    public String getCommand() {
        return command;
    }

    public String prefix() {
        return prefix;
    }

    public Map<SubCommand, SubCommandExecutor> arguments() {
        return arguments;
    }

    public JavaPlugin plugin() {
        return plugin;
    }
    // </editor-fold>
}
