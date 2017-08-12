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
package com.alternacraft.aclib.commands.registerer;

import com.alternacraft.aclib.commands.SubCommandExecutor;
import com.alternacraft.aclib.commands.Condition;

/**
 * Subcommand structure.
 * 
 * @author AlternaCraft
 */
public interface SubCommandsInterface {

    /**
     * Returns subcommand.
     * 
     * @return String with the subcommand
     */
    public String getSubCommand();

    /**
     * Returns alias.
     * 
     * @return Array with the alias
     */
    public String[] getAliases();
    
    /**
     * Returns usage.
     * 
     * @return String with the usage
     */
    public String getUsage();

    /**
     * Returns description.
     * 
     * @return Enum with the description
     */
    public Enum getDescription();

    /**
     * Returns the custom condition.
     * 
     * @return Condition
     */
    public Condition getCustomCondition();
    
    /**
     * Returns executor instance.
     * 
     * @return Subcommand
     * 
     * @since 1.0.1
     */
    public SubCommandExecutor getInstance();

}
