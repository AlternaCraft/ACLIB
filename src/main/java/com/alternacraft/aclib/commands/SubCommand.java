/*
 * Copyright (C) 2018 AlternaCraft
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
package com.alternacraft.aclib.commands;

import com.alternacraft.aclib.langs.CommandMessages;
import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.aclib.langs.LangInterface;
import java.util.Arrays;

/**
 * Class for defining a command argument.
 *
 * @author AlternaCraft
 */
public class SubCommand {

    private final String command;
    private final SubCommandArgument[] arguments;
    
    private final Enum description;
    private final Condition condition;
    private final String[] aliases;
    
    private final SubCommand[] subcommands;
    private final SubCommandTabExecutor tabExecutor;

    public SubCommand(String cmd, Enum desc, Condition condition, String[] aliases, 
            SubCommand[] subcommands, SubCommandTabExecutor tabExecutor, 
            SubCommandArgument... arguments) {
        this.command = cmd;
        this.description = desc;
        this.condition = condition;
        this.aliases = aliases;
        this.subcommands = subcommands;
        this.tabExecutor = tabExecutor;
        this.arguments = arguments;
    }

    public SubCommand(String command, Enum description, SubCommandArgument... arguments) {
        this(command, description, null, new String[0], new SubCommand[0], null, arguments);
    }

    public SubCommand(String command) {
        this(command, null);
    }

    /**
     * Returns subcommand.
     *
     * @return SubCommand
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns arguments.
     * 
     * @return Argument[]
     */
    public SubCommandArgument[] getArguments() {
        return arguments;
    }
    
    /**
     * Returns full command.
     * 
     * @param prefix Parent command
     * 
     * @return Full command
     */
    public String getFullCommand(String prefix) {
        return this.getPartialCommand(prefix) + String.join(" ", Arrays.stream(this.arguments)
                .map(SubCommandArgument::toString).toArray(String[]::new));
    }
    
    /**
     * Returns command without arguments
     * 
     * @param prefix Parent command
     * 
     * @return Partial command
     */
    public String getPartialCommand(String prefix) {
        // Avoid "" empty command (help)
        return prefix + ((!this.command.isEmpty()) ? 
                ((!prefix.isEmpty()) ? " " : "") + this.command 
                + ((this.hasArguments()) ? " " : "") : "");
    }
    
    /**
     * Returns usage.
     * 
     * @param prefix Main command
     * @param lang Language
     * 
     * @return String with the usage
     */
    public String getUsage(String prefix, Lang lang) {
        String usage = this.getFullCommand(prefix);
        if (this.hasSubCommands()) {
            if (this.subcommands.length == 1) {
                usage += " " + this.subcommands[0].getFullCommand(prefix);
            } else {
                usage += " " + CommandMessages.COMMAND_OPTION.getText(lang);
            }
        }
        return usage;
    }
    
    /**
     * Returns if command has description.
     * 
     * @return True if description is not equals to null
     */
    public boolean hasDescription() {
        return this.description != null;
    }

    /**
     * Returns if command has arguments.
     * 
     * @return True if command has arguments
     */
    public boolean hasArguments() {
        return this.arguments != null && this.arguments.length > 0;
    }
    
    /**
     * Returns if command has subcommands.
     * 
     * @return True if command has subcommands
     */
    public boolean hasSubCommands() {
        return this.subcommands != null && this.subcommands.length > 0;
    }
    
    /**
     * Returns command description by language.
     *
     * @param <T> Lang enum
     * @param lang Language
     *
     * @return SubCommand description or empty string
     */
    public <T extends Enum & LangInterface> String getDescription(Lang lang) {
        return !this.hasDescription() ? "" : ((T) description).getText(lang);
    }

    /**
     * Returns custom condition
     *
     * @return Condition condition
     */
    public Condition getCondition() {
        return condition;
    }
    
    /**
     * Returns the aliases
     *
     * @return Array of aliases
     */
    public String[] getAliases() {
        return aliases;
    }

    /**
     * Returns all arguments as subcommands
     * 
     * @return Array of subcommands
     */
    public SubCommand[] getSubcommands() {
        return subcommands;
    }    

    /**
     * Returns tabExecutor
     * 
     * @return SubCommandTabExecutor
     */
    public SubCommandTabExecutor getTabExecutor() {
        return tabExecutor;
    }
    
    /**
     * Does it have tabExecutor?
     * 
     * @return True if SubCommandTabExecutor is not null; False if not
     */
    public boolean hasTabExecutor() {
        return this.tabExecutor != null;
    }
}
