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
package com.alternacraft.aclib.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Possible errors.
 * 
 * @author AlternaCraft
 */
public class ErrorManager {
    
    private static final List<Error> VAULT = new ArrayList();  
    
    private static final List<String> LAST_MESSAGES = new ArrayList();
    private static final List<String> LAST_CODES = new ArrayList();
           
    public static <T extends Enum & ErrorFormat> void registerTestsBenchs(Class<T>... classes) {
        for (Class<T> errors : classes) {            
            for (T error : errors.getEnumConstants()) {
                Error e = new Error(error.getMessage(), new ErrorCode(
                        error.getErrorCode()[0], error.getErrorCode()[1]), 
                        error.getErrorExecutor());
                VAULT.add(e);
            }
        }        
    }
    
    public static void registerTest(Error e) {
        VAULT.add(e);
    }
    
    /**
     * Errors analysis.
     * <b>This method saves the result into this vars: LAST_MESSAGES & LAST_CODES</b>
     * 
     * @param msg Exception message.
     * @param data Custom data.
     * @param c_error Another exception message.
     */
    public static void analyzePossibleReasons(String msg, Map<String, Object> data, 
            String c_error) {
        LAST_MESSAGES.clear();
        LAST_CODES.clear();
        
        for (Error e : VAULT) {
            if (e.getExecutor().matches(msg, data, c_error)) {
                LAST_MESSAGES.add(new StringBuilder("- ").append(e.getMessage()).toString());
                LAST_CODES.add(e.getCode().errorCode());
            }
        }
    }
    
    /**
     * Returns the messages inside of a list.
     * 
     * @return Latest analyzed messages
     */
    public static List<String> getLastMessages() {
        return LAST_MESSAGES;
    }

    /**
     * Returns codes inside parentheses separated by spaces.
     * 
     * @return Latest analyzed codes
     */
    public static String getLastCodes() {
        String result = "";
        for (String lastCode : LAST_CODES) {
            result = new StringBuilder(result).append(" (").append(lastCode).append(")").toString();
        }
        return result;
    }
}
