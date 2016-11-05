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
package com.alternacraft.aclib.commands.registerer;

import com.alternacraft.aclib.commands.SubCommandExecutor;

/**
 * Subcommand structure
 * 
 * @author AlternaCraft
 */
public interface SubCommandsInterface {

    /**
     * Gets subcommand
     * 
     * @return String with the subcommand
     */
    public String getSubCommand();

    /**
     * Gets usage
     * 
     * @return String with the usage
     */
    public String getUsage();

    /**
     * Gets description
     * 
     * @return Enum with the description
     */
    public Enum getDescription();

    /**
     * Gets executor instance
     * 
     * @return Subcommand
     * 
     * @since 1.0.1
     */
    public SubCommandExecutor getInstance();

}
