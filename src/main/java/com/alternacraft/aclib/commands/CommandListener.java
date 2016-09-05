/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alternacraft.aclib.commands;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.utils.MapUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandListener implements CommandExecutor {

    private final Map<CommandArgument, ArgumentExecutor> arguments = new LinkedHashMap<>();

    private final String command;
    private final JavaPlugin plugin;

    public CommandListener(String command, JavaPlugin plugin) {
        this.command = command;
        this.plugin = plugin;

        this.plugin.getCommand(command).setExecutor(this);
    }

    public void addArgument(CommandArgument arg, ArgumentExecutor argClass) {
        arguments.put(arg, argClass);
    }

    @Deprecated
    public CommandArgument getArgument(ArgumentExecutor argClass) {
        for (Map.Entry<CommandArgument, ArgumentExecutor> entry : arguments.entrySet()) {
            CommandArgument key = entry.getKey();
            ArgumentExecutor value = entry.getValue();

            if (value.equals(argClass)) {
                return key;
            }
        }
        return null;
    }

    /**
     *
     * @param argExecutor ArgumentExecutor
     * @return CommandArgument
     * @since 0.0.6
     */
    public CommandArgument getCmdArgument(ArgumentExecutor argExecutor) {
        return MapUtils.getKeyFrom(arguments, argExecutor);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String str, String[] args) {
        if (args.length == 0) {
            List<String> lines = new ArrayList<>();

            for (Map.Entry<CommandArgument, ArgumentExecutor> entry : arguments.entrySet()) {
                CommandArgument key = entry.getKey();

                String line = "&6" + key.getUsage() + "&r - " + key.getDescription();
                lines.add(line);
            }

            MessageManager.sendCommandSender(cs, "Plugin command list:");
            for (String line : lines) {
                MessageManager.sendCommandSender(cs, line);
            }
        } else {
            CommandArgument cmdArgument = MapUtils.findArgument(arguments, args[0]);
            if (cmdArgument != null) {
                if (!arguments.get(cmdArgument).execute(cs, args)) {
                    MessageManager.sendCommandSender(cs, cmdArgument.getUsage());
                }
            } else {
                MessageManager.sendCommandSender(cs, "&4Invalid argument");
            }
        }

        return true;
    }

    public String getCommand() {
        return command;
    }
}
