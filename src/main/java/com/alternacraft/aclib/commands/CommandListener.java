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
package com.alternacraft.aclib.commands;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.exceptions.KeyNotFoundException;
import com.alternacraft.aclib.langs.CommandMessages;
import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.aclib.utils.MapUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Define command/subcommand listener.
 *
 * @author AlternaCraft
 */
public class CommandListener implements CommandExecutor, TabCompleter {

    private final Map<SubCommand, SubCommandExecutor> arguments = new LinkedHashMap<>();

    private final String command;
    private final String perm_prefix;
    private final JavaPlugin plugin;

    /**
     * Main constructor.
     *
     * @param command Main command
     * @param prefix Permissions prefix
     * @param plugin JavaPlugin
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public CommandListener(String command, String prefix, JavaPlugin plugin) {
        this.command = command;
        this.perm_prefix = prefix;
        this.plugin = plugin;

        PluginCommand cmd = this.plugin.getCommand(command);
        cmd.setExecutor(this);
        cmd.setTabCompleter(this);
    }

    /**
     * Adds a subcommand.
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

        Lang l = Localizer.getLocale(cs);

        try {
            SubCommand cmdArgument = MapUtils.findArgument(arguments, args[0]);
            // Removing first argument
            if (args.length == 1) {
                args = new String[0];
            } else {
                args = Arrays.copyOfRange(args, 1, args.length);
            }
            // Checking sender permissions            
            if (cs instanceof Player && !cmdArgument.getCommand().isEmpty()) {
                Player pl = (Player) cs;
                String permission = this.getPermission(cmdArgument.getCommand());
                if (!pl.hasPermission(permission) && 
                        (cmdArgument.getCondition() == null || !cmdArgument.getCondition().testCondition(pl, args))) {
                    MessageManager.sendPluginMessage(cs, CommandMessages.NO_PERMISSION.getText(l));
                    return true;
                }
            }
            if (!arguments.get(cmdArgument).execute(cs, args)) {
                MessageManager.sendPluginMessage(cs, CommandMessages.COMMAND_USAGE
                        .getText(l).replace("%USAGE%", cmdArgument.getUsage(this.command, l)));
            }
        } catch (KeyNotFoundException ex) {
            MessageManager.sendPluginMessage(cs, CommandMessages.INVALID_ARGUMENTS.getText(l));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String string, String[] params) {
        return getOptions(cs, arguments.keySet().toArray(new SubCommand[arguments.size()]), 0, params);
    }
    
    private List<String> getOptions(CommandSender cs, SubCommand[] cmds, int idx, String[] params) {
        List<String> options = new ArrayList<>();
        for (SubCommand cmd : cmds) {
            String value = cmd.getCommand();
            if (value.startsWith(params[idx])) {
                if (idx == params.length - 1 && !cmd.areArguments() 
                        && cs.hasPermission(this.getPermission(value))) {
                    options.add(value.split(" ")[0]); // Exclude arguments <*>
                } else if (idx + 1 < params.length) {
                    return getOptions(cs, cmd.getArguments(), idx + 1, params);
                }
            }
        }
        return options;
    }
    
    // <editor-fold defaultstate="collapsed" desc="GETTERS">
    public String getCommand() {
        return command;
    }
    
    public String getPermission(String subcmd) {
        return this.perm_prefix + "." + subcmd;
    }

    public String prefix() {
        return perm_prefix;
    }

    public Map<SubCommand, SubCommandExecutor> arguments() {
        return arguments;
    }

    public JavaPlugin plugin() {
        return plugin;
    }
    // </editor-fold>
}
