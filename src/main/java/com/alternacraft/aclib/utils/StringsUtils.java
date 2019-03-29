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
package com.alternacraft.aclib.utils;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.exceptions.InvalidColorNameException;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
     * @return String formatted
     */
    public static String translateColors(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Removes all ChatColors from a String.
     *
     * @param s String to format
     * @return String formatted
     */
    public static String stripColors(String s) {
        return ChatColor.stripColor(s);
    }

    /**
     * Formattes a number in time components.
     *
     * @param s Time in seconds
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
    public enum Position {
        LEFT, CENTER, RIGHT
    }

    /**
     * Sets the same length into multiple Strings by using a Char.
     * <p>
     * So far, this is a bit useless because letters in Minecraft does not
     * occupy the same space</p>
     *
     * @param size Maximum size
     * @param e    Char to add
     * @param p    Position in the 'block'
     * @param strs String...
     * @return String[] in the same order
     * @since 0.0.9
     */
    public static String[] copyLength(int size, char e, Position p, String... strs) {
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
     * Returns the Color by name.
     *
     * @param color Color name
     * @return Bukkit Color
     * @throws com.alternacraft.aclib.exceptions.InvalidColorNameException
     */
    public static Color getColorByName(String color) throws InvalidColorNameException {
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
        throw new InvalidColorNameException();
    }

    private static final Map<String, Long> PAIRS = new HashMap();
    private static final Pattern FORMAT;

    static {
        PAIRS.put("d", 1000L * 3600L * 24L);
        PAIRS.put("h", 1000L * 3600L);
        PAIRS.put("m", 1000L * 60L);
        PAIRS.put("s", 1000L);
        FORMAT = Pattern.compile("(\\d+)([dhms])", Pattern.CASE_INSENSITIVE);
    }

    /**
     * Returns time String in millis.
     * Format example: [-]5d 3m 4s
     *
     * @param time String
     * @return time in millis
     */
    public static long parseStrTimeToMillis(String time) {
        long millis = 0;
        boolean minus = false;
        if (!time.trim().isEmpty()) {
            minus = time.charAt(0) == '-';
            Matcher m = FORMAT.matcher(time);
            while (m.find())
                millis += Integer.valueOf(m.group(1)) * PAIRS.get(m.group(2));
        }
        return (minus) ? -millis : millis;
    }

    /**
     * Returns an interactive text.
     * <p>
     * Template: %click:(cmd|info):value | [hover:(text|item):value] |
     * displayed_text%
     * <p>
     * Use "//" to write multiple lore lines
     *
     * @param str String
     * @return Awesome text
     */
    public static TextComponent interactiveText(String str) {
        TextComponent result = new TextComponent();

        List<String> components = RegExp.getGroupsWithElements(
                "%((?:\\w+:\\w+:[^%]+" + RegExp.ESCAPE_STRING + "\\|?){1,2}"
                        + RegExp.ESCAPE_STRING + "\\|[^%]+)%", str, 1
        ).stream().map(arr -> arr[0]).collect(Collectors.toList());

        TextComponent[] values = components.stream().map(c -> {
            TextComponent v = new TextComponent();
            String[] elements = c.split(RegExp.ESCAPE_STRING + "\\|");
            for (int i = 0; i < elements.length; i++) {
                String e = elements[i].replace("\\|", "|");
                if (i == elements.length - 1) {
                    // Display text
                    Arrays.stream(TextComponent.fromLegacyText(e))
                            .forEach(v::addExtra);
                    break;
                }
                String[] data = e.split(RegExp.ESCAPE_STRING + ":");
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
                                BaseComponent[] aux = null;
                                try {
                                    new JSONParser().parse(data[2]);
                                    aux = ComponentSerializer.parse(data[2]);
                                } catch (ParseException ex) {
                                    aux = TextComponent.fromLegacyText(data[2]
                                            .replace("//", "\n")
                                            .replace("\\:", ":")
                                    );
                                }
                                he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, aux);
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
