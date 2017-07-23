/*
 * Copyright (C) 2017 AlternaCraft
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
package com.alternacraft.aclib.extras;

import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.commands.CommandListener;
import com.alternacraft.aclib.commands.SubCommand;
import com.alternacraft.aclib.commands.SubCommandExecutor;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.Localizer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commands list.
 *
 * @author AlternaCraft
 */
public class PluginCommands implements SubCommandExecutor {

    private String title, footer;
    private CommandListener cl;

    public PluginCommands(CommandListener cl) {
        this(
                cl,
                new StringBuilder()
                        .append(PluginBase.INSTANCE.pluginPrefix())
                        .append(ChatColor.AQUA)
                        .append(" v")
                        .append(cl.plugin().getDescription().getVersion())
                        .append(ChatColor.RESET)
                        .toString(),
                new StringBuilder()
                        .append("■ ")
                        .append(ChatColor.AQUA)
                        .append("Created By ")
                        .append(cl.plugin().getDescription().getAuthors().get(0))
                        .append(ChatColor.RESET)
                        .append(" ■")
                        .toString()
        );
    }

    public PluginCommands(CommandListener cl, String title, String footer) {
        this.cl = cl;
        this.title = title;
        this.footer = footer;
    }

    @Override
    public boolean execute(CommandSender cs, String[] args) {
        Langs lang = Localizer.getLocale(cs);

        cs.sendMessage("");
        cs.sendMessage(this.title);

        this.cl.arguments().entrySet().stream().filter(entry -> {
            SubCommand key = entry.getKey();
            // Don't show if he does not have permission
            if (cs instanceof Player && !key.getCommand().isEmpty()) {
                String permission = this.cl.prefix() + "." + key.getCommand();
                if (!((Player) cs).hasPermission(permission)) {
                    return false;
                }
            }
            return true;
        }).forEachOrdered(map -> {
            cs.sendMessage("  /" + ChatColor.BLUE + map.getKey().getUsage() + ChatColor.RESET
                    + " - " + ChatColor.GRAY + map.getKey().getDescription(lang));
        });

        cs.sendMessage(this.footer);

        return true;
    }
}
