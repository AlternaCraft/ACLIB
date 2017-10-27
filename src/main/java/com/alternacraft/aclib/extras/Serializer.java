package com.alternacraft.aclib.extras;

import com.alternacraft.aclib.utils.RegExp;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author AlternaCraft
 */
public class Serializer {

    private static final String ITEMSTACK_REGEX = "\\{\\{([\\w\\d=,]+)\\}=([\\w\\d=,]+)\\}";
    private static final String LIST_REGEX = "\\[(.*)\\]";
    
    /**
     * Deserialize a list from toString value of a List
     * 
     * @param toString toString value
     * 
     * @return String List
     */
    public List<String> deserializeList(String toString) {
        String s = cleanString(toString);
        String content = RegExp.getGroups(LIST_REGEX, Pattern.quote(s)).get(0);
        return Arrays.stream(content.split(",")).collect(Collectors.toList());
    }
    
    public static String serializeLocation(Location l) {
        return new StringBuilder()
                .append(l.getWorld().getName())
                .append(";")
                .append(l.getX())
                .append(";")
                .append(l.getY())
                .append(";")
                .append(l.getZ())
                .append(";")
                .append(l.getYaw())
                .append(";")
                .append(l.getPitch()).toString();
    }

    public static Location deserializeLocation(String l) {
        String[] values = l.split(";");
        return new Location(Bukkit.getWorld(values[0]), Double.valueOf(values[1]),
                Double.valueOf(values[2]), Double.valueOf(values[3]), 
                Float.valueOf(values[4]), Float.valueOf(values[5]));
    }

    public final static String serializeItemStackList(final ItemStack[] itemStackList) {
        final List<Map.Entry<Map<String, Object>, Map<String, Object>>> 
                serializedItemStackList = new ArrayList<>();

        for (ItemStack itemStack : itemStackList) {
            Map<String, Object> serializedItemStack, serializedItemMeta;

            if (itemStack == null) {
                itemStack = new ItemStack(Material.AIR);
            }
            serializedItemMeta = (itemStack.hasItemMeta())
                    ? itemStack.getItemMeta().serialize()
                    : null;
            itemStack.setItemMeta(null);
            serializedItemStack = itemStack.serialize();

            serializedItemStackList.add(new AbstractMap.SimpleEntry<>(
                    serializedItemStack,
                    serializedItemMeta
            ));
        }

        return serializedItemStackList.toString();
    }

    public final static ItemStack[] deserializeItemStackList(final String str) {
        List<Map.Entry<Map<String, Object>, Map<String, Object>>> serializedItemStackList = itemStackToList(str);
        
        final ItemStack[] itemStackList = new ItemStack[serializedItemStackList.size()];
        int i = 0;
        for (Map.Entry<Map<String, Object>, Map<String, Object>> serializedItemStack : serializedItemStackList) {
            ItemStack itemStack = ItemStack.deserialize(serializedItemStack.getKey());
            if (serializedItemStack.getValue() != null) {
                ItemMeta itemMeta = (ItemMeta) ConfigurationSerialization.deserializeObject(serializedItemStack.getValue(),
                        ConfigurationSerialization.getClassByAlias("ItemMeta"));
                itemStack.setItemMeta(itemMeta);
            }
            itemStackList[i++] = itemStack;
        }
        
        return itemStackList;
    }

    //<editor-fold defaultstate="collapsed" desc="INNER CODE">
    private static List<Map.Entry<Map<String, Object>, Map<String, Object>>> itemStackToList(String str) {
        return RegExp.getGroupsWithElements(ITEMSTACK_REGEX, str, 1, 2)
                .stream()
                .map(e -> {
                    return new AbstractMap.SimpleEntry<>(
                        parseString(e[0]), 
                        parseString(e[1])
                    );
                })
                .collect(Collectors.toList());
    }
    
    private static Map<String, Object> parseString(String str) {
        if (str.equals("null")) {
            return null;
        }            
        String[] arr = str.split(",");
        Map<String, Object> data = new HashMap<>();
        for (String val : arr) {
            String[] aux = val.split("=");
            data.put(aux[0], parseValue(aux[1]));
        }
        return data;
    }    
    //</editor-fold>
    
    private static String cleanString(String str) {
        return str.replaceAll(" ", "");
    }    
    
    public static Object parseValue(String str) {
        return (StringUtils.isNumeric(str)) ? Integer.valueOf(str) : str;
    }    
}
