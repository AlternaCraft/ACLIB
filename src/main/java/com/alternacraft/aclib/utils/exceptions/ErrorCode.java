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

/**
 * Error code.
 * The final structure should be something like this: <i>00x00</i>
 * <ul>
 *  <li>The first two digits refer to the "application".</li>
 *  <li>The last two digits refer to the "component" of the "application".</li>
 * </ul>
 * 
 * @author AlternaCraft
 */
public class ErrorCode {

    private final int error_prefix;
    private final int error_suffix;

    public ErrorCode(int pfx, int sfx) {
        this.error_prefix = pfx;
        this.error_suffix = sfx;
    }

    public String errorCode() {
        String result_prefix = "";
        String result_suffix = "";
        
        if (this.error_prefix <= 9) {
            result_prefix = "0";
        }
        if (this.error_suffix <= 9) {
            result_suffix = "0";
        }
        
        return new StringBuilder(result_prefix).append(this.error_prefix)
                .append("x").append(result_suffix).append(this.error_suffix).toString();
    }
}
