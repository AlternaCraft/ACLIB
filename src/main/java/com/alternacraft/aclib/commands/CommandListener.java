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
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Define command/subcommand listener.
 *
 * @author AlternaCraft
 */
public class CommandListener implements CommandExecutor, TabCompleter {

    private final Map<SubCommand, SubCommandExecutor> arguments = new LinkedHashMap<>();

    //<editor-fold defaultstate="collapsed" desc="TAB UTILS">
    private final SubCommandTabExecutor DEFAULT_TAB = (String input, SubCommandArgument arg) -> {
        switch (arg.getValue()) {
            case "ign":
            case "player_name":
            case "player":
                return Bukkit.getOnlinePlayers().stream()
                        .filter(p -> p.getName().startsWith(input))
                        .map(p -> p.getName())
                        .collect(Collectors.toList());
        }
        return new ArrayList();
    };
    //</editor-fold>

    private final String command;
    private final String perm_prefix;
    private final JavaPlugin plugin;

    /**
     * Main constructor.
     *
     * @param command Main command
     * @param prefix  Permissions prefix
     * @param plugin  JavaPlugin
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
     * @param subcommand SubCommand
     * @param executor   Executor
     */
    public void addSubCommand(SubCommand subcommand, SubCommandExecutor executor) {
        arguments.put(subcommand, executor);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String str, String[] args) {
        if (args.length == 0) {
            args = new String[]{""}; // Custom template
        }

        Lang l = Localizer.getLocale(cs);

        try {
            SubCommand subcommand = MapUtils.findArgument(arguments, args[0]);
            // Removing first argument
            if (args.length == 1) {
                args = new String[0];
            } else {
                args = Arrays.copyOfRange(args, 1, args.length);
            }
            // Checking sender permissions            
            if (cs instanceof Player && !subcommand.getCommand().isEmpty()) {
                Player pl = (Player) cs;
                String permission = this.getPermission(subcommand.getCommand());
                if (!pl.hasPermission(permission) &&
                        (subcommand.getCondition() == null || !subcommand.getCondition().testCondition(pl, args))) {
                    MessageManager.sendPluginMessage(cs, CommandMessages.NO_PERMISSION.getText(l));
                    return true;
                }
            }
            if (!arguments.get(subcommand).execute(cs, args)) {
                MessageManager.sendPluginMessage(cs, CommandMessages.COMMAND_USAGE
                        .getText(l).replace("%USAGE%", subcommand.getUsage(this.command, l)));
            }
        } catch (KeyNotFoundException ex) {
            MessageManager.sendPluginMessage(cs, CommandMessages.INVALID_ARGUMENTS.getText(l));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String string, String[] params) {
        List<String> options = new ArrayList<>();
        for (Iterator<SubCommand> iterator = this.arguments.keySet().iterator(); iterator.hasNext(); ) {
            SubCommand next = iterator.next();
            if (next.getCommand().startsWith(params[0])
                    || Arrays.stream(next.getAliases())
                    .anyMatch(als -> als.startsWith(params[0]))) {
                options.addAll(this.getOptions(cs, next, next, 1, params));
            }
        }
        return options;
    }

    private List<String> getOptions(CommandSender cs, SubCommand base,
                                    SubCommand next, int idx, String[] params) {
        List<String> options = new ArrayList<>();

        if (next.hasSubCommands() && params.length > idx) {
            for (SubCommand subcommand : next.getSubcommands()) {
                if (subcommand.getCommand().startsWith(params[idx])
                        || Arrays.stream(subcommand.getAliases())
                        .anyMatch(als -> als.startsWith(params[idx]))) {
                    options.addAll(this.getOptions(cs, base, subcommand, idx + 1, params));
                }
            }
        } else {
            int left;
            if (idx >= params.length) {
                options.add(next.getCommand());
            } else if (next.hasArguments() && (left = params.length - idx) > 0) {
                SubCommandArgument arg = next.getArguments()[left - 1];
                String input = params[params.length - 1];
                options.addAll(this.DEFAULT_TAB.parseArgument(input, arg));
                if (base.hasTabExecutor()) {
                    options.addAll(base.getTabExecutor().parseArgument(input, arg));
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
