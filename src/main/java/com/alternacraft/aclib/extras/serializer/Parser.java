package com.alternacraft.aclib.extras.serializer;

import com.alternacraft.aclib.utils.RegExp;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author AlternaCraft
 */
public class Parser {

    private static final String REGEX = "\\{\\{([\\w\\d=,]+)\\}=([\\w\\d=,]+)\\}";

    public static String cleanString(String str) {
        return str.replaceAll(" ", "");
    }

    public static List<Map.Entry<Map<String, Object>, Map<String, Object>>> stringToList(String str) {
        return RegExp.getGroupsWithElements(REGEX, str, 1, 2)
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
    
    public static Object parseValue(String str) {
        return (StringUtils.isNumeric(str)) ? Integer.valueOf(str) : str;
    }
}
