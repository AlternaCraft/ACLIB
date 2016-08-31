/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alternacraft.aclib.commands;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginDescription;
import com.alternacraft.aclib.PluginManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String str, String[] args) {
        if (args.length == 0) {
            PluginDescription pluginDescription = PluginManager.instance
                    .getPluginDescription();
            
            pluginDescription.getLines().stream().forEach((line) -> {
                MessageManager.sendCommandSender(
                        cs,
                        line
                );
            });
        } else {
            PluginCommand command = PluginCommands.instance.getCommand(args[0]);
            if (command != null) {
                if (!command.execute(cs, args)) {
                    MessageManager.sendCommandSender(cs, command.error());
                }
            }
        }
        return true;
    }
}
