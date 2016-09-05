/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alternacraft.aclib.commands;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.langs.DefaultMessages;
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

public class CommandListener implements CommandExecutor {

    /**
     * @since 0.0.7
     */
    public static final String DEFAULT_CMD = "info";

    private final Map<CommandArgument, ArgumentExecutor> arguments = new LinkedHashMap<>();

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

    public void addArgument(CommandArgument arg, ArgumentExecutor argClass) {
        arguments.put(arg, argClass);
    }

    /**
     * Get a command argument by an argument executor
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
            args = new String[]{DEFAULT_CMD}; // Custom template
        }

        Langs l = ((cs instanceof Player) ? Localizer.getLocale((Player) cs)
                : PluginBase.INSTANCE.getMainLanguage());

        CommandArgument cmdArgument = MapUtils.findArgument(arguments, args[0]);
        if (cmdArgument != null) {
            if (cs instanceof Player) { // Checking if it has permission
                String permission = this.prefix + "." + cmdArgument.getArgument();
                if (!((Player) cs).hasPermission(permission)) {
                    MessageManager.sendCommandSender(cs, DefaultMessages.NO_PERMISSION.getText(l));
                }
            }
            if (!arguments.get(cmdArgument).execute(cs, args)) {
                MessageManager.sendCommandSender(cs, cmdArgument.getUsage());
            }
        } else {
            MessageManager.sendCommandSender(cs, DefaultMessages.INVALID_ARGUMENTS.getText(l));
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

    public Map<CommandArgument, ArgumentExecutor> arguments() {
        return arguments;
    }

    public JavaPlugin plugin() {
        return plugin;
    }
    // </editor-fold>
}
