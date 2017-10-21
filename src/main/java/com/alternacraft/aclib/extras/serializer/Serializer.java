package com.alternacraft.aclib.extras.serializer;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        List<Map.Entry<Map<String, Object>, Map<String, Object>>> serializedItemStackList = Parser.stringToList(str);
        
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
}
