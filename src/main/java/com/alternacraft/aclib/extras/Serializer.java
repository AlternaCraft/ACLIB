package com.alternacraft.aclib.extras;

import com.alternacraft.aclib.utils.RegExp;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
        final List<Map.Entry<Map<String, Object>, Map<String, Object>>> serializedItemStackList = new ArrayList<>();

        for (ItemStack itemStack : itemStackList) {
            Map<String, Object> serializedItemStack, serializedItemMeta;

            ItemStack aux;
            if (itemStack == null) {
                aux = new ItemStack(Material.AIR);
            } else {
                aux = new ItemStack(itemStack);
            }
            
            serializedItemMeta = (aux.hasItemMeta()) ? aux.getItemMeta().serialize() : null;
            aux.setItemMeta(null);
            serializedItemStack = aux.serialize();

            serializedItemStackList.add(new AbstractMap.SimpleEntry<>(
                    serializedItemStack,
                    serializedItemMeta
            ));
        }

        return serializedItemStackList.toString();
    }

    public final static ItemStack[] deserializeItemStackList(final String str) {
        List<Map.Entry<Map<String, Object>, Map<String, Object>>> serializedItemStackList = stringToList(str);

        final ItemStack[] itemStackList = new ItemStack[serializedItemStackList.size()];
        int i = 0;
        for (Map.Entry<Map<String, Object>, Map<String, Object>> serializedItemStack : serializedItemStackList) {
            ItemStack itemStack = ItemStack.deserialize(serializedItemStack.getKey());
            if (serializedItemStack.getValue() != null) {
                ItemMeta itemMeta = (ItemMeta) ConfigurationSerialization
                        .deserializeObject(
                                serializedItemStack.getValue(),
                                ConfigurationSerialization.getClassByAlias("ItemMeta")
                        );
                itemStack.setItemMeta(itemMeta);
            }
            itemStackList[i++] = itemStack;
        }

        return itemStackList;
    }

    //<editor-fold defaultstate="collapsed" desc="INNER CODE">    
    private static final String INNER_LIST_REGEX = "\\[(.*)\\]";
    private static final String LIST_ITEM_REGEX = ",(?=\\{)";
    private static final String ITEM_REGEX = "\\{(.*)\\}=(?:\\{(.*)\\}|null)";
    private static final String ITEM_ATTR_REGEX = ",(?!(\\w+=[\\w\\d]+,?)+\\})(?!([\\w\\d]+,?)+\\])";
    
    private static final String ITEM_INNER_MAP = "\\{(.*)\\}";
    private static final String ITEM_INNER_ARRAY = "\\[(.*)\\]";
    
    private static List<Map.Entry<Map<String, Object>, Map<String, Object>>> stringToList(String str) {
        List<Map.Entry<Map<String, Object>, Map<String, Object>>> serializedItemStackList = new ArrayList<>();
        
        Arrays.asList(RegExp.getGroups(INNER_LIST_REGEX, cleanString(str))
                .get(0).split(LIST_ITEM_REGEX)).forEach(item -> {        
            RegExp.getGroupsWithElements(ITEM_REGEX, item, 1, 2).forEach(nodes -> {
                Map<String, Object> keys = new LinkedHashMap();
                Arrays.asList(nodes[0].split(",")).forEach(kv -> {
                    String[] splitter = kv.split("=");
                    keys.put(splitter[0], parseValue(splitter[1]));
                });
                Map<String, Object> values = null;
                if (nodes[1] != null) {
                    values = new LinkedHashMap();
                    for (String kv : Arrays.asList(nodes[1].split(ITEM_ATTR_REGEX))) {
                        String[] splitter = kv.split("=", 2);

                        String key = splitter[0];
                        Object value;

                        // {a,b}
                        if (splitter[1].matches(ITEM_INNER_MAP)) {
                            Map<String, Object> aux = new LinkedHashMap<>();
                            Arrays.asList(RegExp.getGroups(ITEM_INNER_MAP, splitter[1])
                                    .get(0).split(",")).forEach(enchantment -> {
                                String[] auxsplitter = enchantment.split("=", 2);
                                aux.put(auxsplitter[0], parseValue(auxsplitter[1]));
                            });       
                            value = aux;
                        } 
                        // [a,b]
                        else if (splitter[1].matches(ITEM_INNER_ARRAY)) {  
                            List<String> aux = new ArrayList<>();
                            Arrays.asList(RegExp.getGroups(ITEM_INNER_ARRAY, splitter[1])
                                    .get(0).split(",")).forEach(text -> {
                                aux.add(text);
                            });       
                            value = aux;
                        } 
                        // a
                        else {
                            value = parseValue(splitter[1]);
                        }

                        values.put(key, value);
                    }
                }
                serializedItemStackList.add(new AbstractMap.SimpleEntry<>(keys, values));
            });
        });      
        
        return serializedItemStackList;
    }

    private static String cleanString(String str) {
        return str.replaceAll(" ", "");
    }

    private static Object parseValue(String str) {
        return (StringUtils.isNumeric(str)) ? Integer.valueOf(str) : str;
    }
    //</editor-fold>    
}
