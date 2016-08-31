/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alternacraft.aclib.commands;

import org.bukkit.command.CommandSender;

public abstract class PluginCommand {
    protected String error = null;

    public abstract boolean execute(CommandSender cs, String[] args);
    
    public final String error() {
        return error;
    }
}
