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

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author AlternaCraft
 */
public class SubCommandArgument {

    private String value;
    private Enum description;
    private boolean optional;
    private String[] aliases;

    public SubCommandArgument(String value, Enum description, boolean optional, String... aliases) {
        this.value = value;
        this.description = description;
        this.optional = optional;
        this.aliases = aliases;
    }

    public SubCommandArgument(String value, Enum description) {
        this(value, description, false);
    }

    public SubCommandArgument(String value, String... aliases) {
        this(value, null, false, aliases);
    }

    public SubCommandArgument(String simplex) {
        Pattern p = Pattern.compile("(\\[)?<(\\w+)>(\\])?");
        Matcher m = p.matcher(simplex);
        if (m.find()) {
            this.optional = m.group(1) != null && m.group(3) != null;
            this.value = m.group(2);
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Enum getDescription() {
        return description;
    }

    public void setDescription(Enum description) {
        this.description = description;
    }

    public boolean hasDescription() {
        return this.description != null;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public boolean is(String value) {
        return this.value.equals(value) || Arrays.asList(aliases).contains(value);
    }

    @Override
    public String toString() {
        String result = "<" + this.value + ">";
        return (this.optional) ? "[" + result + "]" : result;
    }
}
