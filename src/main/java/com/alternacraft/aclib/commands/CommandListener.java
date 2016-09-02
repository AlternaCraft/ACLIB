/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alternacraft.aclib.commands;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginDescription;
import com.alternacraft.aclib.PluginManager;
import java.util.HashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandListener implements CommandExecutor {

    private HashMap<String, PluginCommand> arguments = new HashMap<>();

    private final String command;
    private final JavaPlugin plugin;

    public CommandListener(String command, JavaPlugin plugin) {
        this.command = command;
        this.plugin = plugin;
        
        this.plugin.getCommand(command).setExecutor(this);
    }

    public void addArgument(String arg, PluginCommand argClass) {
        arguments.put(arg, argClass);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String str, String[] args) {
        if (args.length == 0) {
            PluginDescription pluginDescription = PluginManager.instance
                    .getPluginDescription();

            for (String line : pluginDescription.getLines()) {
                MessageManager.sendCommandSender(
                        cs,
                        line
                );
            }
        } else {
            if (this.arguments.containsKey(args[0])) {
                PluginCommand command = this.arguments.get(args[0]);
                return command.execute(cs, args);
            }
            MessageManager.sendCommandSender(cs, "&4Invalid argument");
        }

        return true;
    }

    public String getCommand() {
        return command;
    }
}
