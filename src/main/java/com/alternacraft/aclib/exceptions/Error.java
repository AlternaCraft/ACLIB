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

/**
 * Error.
 * 
 * @author AlternaCraft
 */
public class Error {

    private final String message;
    private final ErrorCode code;    
    // For checking if this error matches.
    private ErrorExecutor executor;
    
    public Error(String message, ErrorCode ec) {
        this.message = message;
        this.code = ec;
    }
    
    public Error(String message, ErrorCode ec, ErrorExecutor executor) {
        this(message, ec);
        this.executor = executor;
    }
    
    public String getError() {
        return this.message + " (" + this.code.errorCode() + ")";
    }

    public String getMessage() {
        return message;
    }

    public ErrorCode getCode() {
        return code;
    }

    public ErrorExecutor getExecutor() {
        return executor;
    }
}
