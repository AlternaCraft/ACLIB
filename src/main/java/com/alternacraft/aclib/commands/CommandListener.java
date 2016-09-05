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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandListener implements CommandExecutor {

    private final Map<CommandArgument, ArgumentExecutor> arguments = new LinkedHashMap<>();

    private final String command;
    private final JavaPlugin plugin;
    private final String alias;

    @SuppressWarnings("LeakingThisInConstructor")
    public CommandListener(String command, String alias, JavaPlugin plugin) {
        this.command = command;
        this.alias = alias;
        this.plugin = plugin;

        this.plugin.getCommand(command).setExecutor(this);
    }

    public void addArgument(CommandArgument arg, ArgumentExecutor argClass) {
        arguments.put(arg, argClass);
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
            Langs l = ((cs instanceof Player) ? Localizer.getLocale((Player)cs)
                    :PluginBase.INSTANCE.getMainLanguage());
            
            CommandArgument cmdArgument = MapUtils.findArgument(arguments, args[0]);
            if (cmdArgument != null) {
                if (cs instanceof Player) {
                    String permission = this.alias + "." + cmdArgument.getArgument();
                    if (((Player) cs).hasPermission(permission)) {
                        if (!arguments.get(cmdArgument).execute(cs, args)) {
                            MessageManager.sendCommandSender(cs, cmdArgument.getUsage());
                        }
                    } else {
                        MessageManager.sendCommandSender(cs, DefaultMessages.NO_PERMISSION.getText(l));
                    }
                }
            } else {
                MessageManager.sendCommandSender(cs, DefaultMessages.INVALID_ARGUMENTS.getText(l));
            }
        }

        return true;
    }

    public String getCommand() {
        return command;
    }
}
