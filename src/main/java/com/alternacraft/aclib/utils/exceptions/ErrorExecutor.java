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
package com.alternacraft.aclib.utils.exceptions;

import java.util.Map;

/**
 *
 * @author AlternaCraft
 */
public interface ErrorExecutor {
    
    /**
     * Checks what could be the cause of the error.
     * 
     * @param msg Exception message.
     * @param data (Optional) Added data to the exception.
     * @param c_error (Optional) Another exception message.
     * 
     * @return True if it matches; False if not
     */
    public boolean matches(String msg, Map<String, Object> data, String c_error);
}
