package com.alternacraft.aclib.extras.serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public static List<HashMap<Map<String, Object>, Map<String, Object>>> stringToList(String str) {
        List<HashMap<Map<String, Object>, Map<String, Object>>> result = new ArrayList<>();

        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            HashMap<Map<String, Object>, Map<String, Object>> items = new LinkedHashMap<>();
            items.put(parseString(matcher.group(1)), parseString(matcher.group(2)));
            result.add(items);
        }

        return result;
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
