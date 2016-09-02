/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alternacraft.aclib.commands;

import org.bukkit.command.CommandSender;

public interface PluginCommand {
    
    /**
     * Method for execute the command
     * @param cs CommandSender
     * @param args List of arguments
     * @return Command result
     */
    public abstract boolean execute(CommandSender cs, String[] args);
}
