/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alternacraft.aclib.commands;

import com.alternacraft.aclib.MessageManager;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandListener implements CommandExecutor {

    private Map<CommandArgument, ArgumentExecutor> arguments = new LinkedHashMap<>();

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

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String str, String[] args) {
        if (args.length == 0) {
            for (Map.Entry<CommandArgument, ArgumentExecutor> entry : arguments.entrySet()) {
                CommandArgument key = entry.getKey();
                MessageManager.sendCommandSender(cs, key.getArgument());
            }
        } else {
            Set<CommandArgument> cmd_args = this.arguments.keySet();
            for (CommandArgument argument : cmd_args) {
                if (argument.getArgument().equals(args[0])) {
                    ArgumentExecutor arg_exe = this.arguments.get(argument);
                    if (!arg_exe.execute(cs, args)) {
                        MessageManager.sendCommandSender(cs, argument.getUsage());                        
                    }
                    return true;
                }
            }
            MessageManager.sendCommandSender(cs, "&4Invalid argument");
        }

        return true;
    }

    public String getCommand() {
        return command;
    }
}
