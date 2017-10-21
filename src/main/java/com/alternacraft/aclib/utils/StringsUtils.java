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
package com.alternacraft.aclib.utils;

import com.alternacraft.aclib.MessageManager;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Color;

/**
 * This class contains some utils for Strings.
 *
 * @author AlternaCraft
 */
public class StringsUtils {

    /**
     * Translates all ChatColors from a String.
     *
     * @param s String to format
     *
     * @return String formatted
     */
    public static String translateColors(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Removes all ChatColors from a String.
     *
     * @param s String to format
     *
     * @return String formatted
     */
    public static String stripColors(String s) {
        return ChatColor.stripColor(s);
    }

    /**
     * Formattes a number in time components.
     *
     * @param s Time in seconds
     *
     * @return String formatted as, for example, "4d 5h 3m 2s" without quotes
     * @since 0.0.9
     */
    public static String splitToComponentTimes(long s) {
        String resul = "";
        long days, hours, mins, secs, remainder;

        // Lógica        
        days = s / 3600 / 24;
        remainder = s - days * 3600 * 24;
        hours = remainder / 3600;
        remainder = remainder - hours * 3600;
        mins = remainder / 60;
        remainder = remainder - mins * 60;
        secs = remainder;

        // Representación
        resul += (days > 0) ? (days + "d ") : "";
        if (days > 0) {
            resul += hours + "h ";
            resul += mins + "m ";
        } else {
            resul += (hours > 0) ? (hours + "h ") : "";
            if (hours > 0) {
                resul += mins + "m ";
            } else {
                resul += (mins > 0) ? (mins + "m ") : "";
            }
        }
        resul += secs + "s";

        return resul;
    }

    /**
     * Method for getting the higher length of multiple Strings.
     *
     * @param strs String...
     *
     * @return the higher length
     */
    public static int getHigherLength(String... strs) {
        int max = 0;
        for (String str : strs) {
            str = stripColors(str);

            if (stripColors(str).length() > max) {
                max = str.length();
            }
        }
        return max;
    }

    /**
     * @since 0.0.9
     */
    public static enum POSITION {
        LEFT, CENTER, RIGHT;
    }

    /**
     * Sets the same length into multiple Strings by using a Char.
     * <p>
     * So far, this is a bit useless because letters in Minecraft does not
     * occupy the same space</p>
     *
     * @param size Maximum size
     * @param e Char to add
     * @param p Position in the 'block'
     * @param strs String...
     *
     * @return String[] in the same order
     *
     * @since 0.0.9
     */
    public static String[] copyLength(int size, char e, POSITION p, String... strs) {
        boolean left;

        for (int i = 0; i < strs.length; i++) {
            int length = stripColors(strs[i]).length();
            left = true;

            for (int j = length; j < size; j++) {
                switch (p) {
                    case LEFT:
                        strs[i] = e + strs[i];
                        break;
                    case CENTER:
                        strs[i] = (left) ? (e + strs[i]) : (strs[i] + e);
                        left = !left;
                        break;
                    case RIGHT:
                        strs[i] += e;
                        break;
                }
            }
        }

        return strs;
    }

    /**
     * Return the Color by name.
     *
     * @param color Color name
     *
     * @return Bukkit Color
     */
    public static Color getColorByName(String color) {
        Field[] fields = Color.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase(color)) {
                try {
                    return (Color) field.get(Color.class);
                } catch (IllegalAccessException ex) {
                    MessageManager.logError("Color " + color + " doesn't exist...");
                }
            }
        }
        return null;
    }

    /**
     * Returns an interactive text.
     *
     * Template: %click:(cmd|info):value | [hover:(text|item):value] |
     * displayed_text%
     *
     * @param str String
     *
     * @return Awesome text
     */
    public static TextComponent parseString(String str) {
        TextComponent result = new TextComponent();

        List<String> components = RegExp.getGroupsWithElements(
                "%((?:\\w+:\\w+:[^%]+\\|?){1,2}\\|[^%]+)%", str, 1
        ).stream().map(arr -> arr[0]).collect(Collectors.toList());

        TextComponent[] values = components.stream().map(c -> {
            TextComponent v = new TextComponent();
            String[] elements = c.split("\\|");
            for (int i = 0; i < elements.length; i++) {
                String e = elements[i];
                if (i == elements.length - 1) {
                    Arrays.stream(TextComponent.fromLegacyText(e))
                            .forEach(v::addExtra);
                    break;
                }
                String[] data = e.split(":");
                if (data.length == 3) {
                    String stripped_message = StringsUtils.stripColors(data[2]);
                    switch (data[0]) {
                        case "click":
                            ClickEvent ce;
                            if (data[1].equals("cmd")) {
                                ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, 
                                        stripped_message);
                            } else {
                                ce = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
                                        stripped_message);
                            }
                            v.setClickEvent(ce);
                            break;
                        case "hover":
                            HoverEvent he;
                            if (data[1].equals("text")) {
                                he = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        TextComponent.fromLegacyText(data[2]));
                            } else {
                                he = new HoverEvent(HoverEvent.Action.SHOW_ITEM, null);
                                //ce = new ClickEvent(HoverEvent.Action.SHOW_ITEM,);
                            }
                            v.setHoverEvent(he);
                    }
                }
            }
            return v;
        }).toArray(TextComponent[]::new);

        if (components.isEmpty()) {
            result = new TextComponent(TextComponent.fromLegacyText(str));
        } else {
            String[] texts = str.split(
                    String.join("|", components
                            .stream()
                            .map(cp -> "%" + Pattern.quote(cp) + "%")
                            .toArray(String[]::new)));
            for (int i = 0; i < texts.length; i++) {
                Arrays.stream(TextComponent.fromLegacyText(texts[i]))
                        .forEach(result::addExtra);
                if (values.length > i) {
                    result.addExtra(values[i]);
                }
            }
        }

        return result;
    }
}
