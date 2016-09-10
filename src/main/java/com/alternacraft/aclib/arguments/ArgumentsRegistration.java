/*
 * Copyright (C) 2016 AlternaCraft
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
package com.alternacraft.aclib.arguments;

import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.commands.CommandArgument;
import com.alternacraft.aclib.commands.CommandListener;

public class ArgumentsRegistration {

    private final CommandListener cmd;

    /**
     * Constructor
     *
     * @param cmd Argument
     * @param alias Abbreviation
     */
    public ArgumentsRegistration(String cmd, String alias) {
        this.cmd = new CommandListener(cmd, alias, PluginBase.INSTANCE.plugin());
    }

    /**
     * Method for registering the arguments in an enum class
     *
     * @param <T> Enum type
     * @param e Enum class
     *
     * @see ArgumentsInterface
     */
    public <T extends ArgumentsInterface> void register(Class<T> e) {
        for (T arg : e.getEnumConstants()) {
            this.cmd.addArgument(
                    new CommandArgument(arg.getArgument(), arg.getUsage(),
                            arg.getDescription()), arg.getClazz());
        }
    }

    /**
     * Get the Command Listener
     *
     * @return CommandListener
     */
    public CommandListener cmdListener() {
        return cmd;
    }

}
