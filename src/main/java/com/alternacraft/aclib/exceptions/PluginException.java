/*
 * Copyright (C) 2016 AlternaCraft
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

import com.alternacraft.aclib.PluginBase;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Custom exception.
 * <ul>This add some extras:
 *  <li>Structured messages
 *      <ul>There are three type:
 *          <li>Simplified. Just for indicating an error</li>
 *          <li>Essential. Essential information for finding a reason</li>
 *          <li>Full. Complete error</li>
 *      </ul>
 *  </li>
 *  <li>How to report</li>
 *  <li>Functionality for finding a possible reason of the error</li>
 * </ul>
 * 
 * @author AlternaCraft
 */
public abstract class PluginException extends Exception {

    protected final String REPORT = "If you don't know the error cause, please, report it.";

    protected static final short SIMPLIFIED = 0;
    protected static final short ESSENTIAL = 1;
    protected static final short FULL = 2;

    protected Map<String, Object> data = new LinkedHashMap();
    protected String custom_error = null;

    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTS">    
    public PluginException(String message) {
        super(message);
    }

    public PluginException(String message, Map<String, Object> data) {
        this(message);
        this.data = data;
    }

    public PluginException(String message, String custom_error) {
        this(message);
        this.custom_error = custom_error;
    }
    // </editor-fold>

    public String getCustomMessage() {
        int n = PluginBase.INSTANCE.getErrorFormat();

        switch (n) {
            case SIMPLIFIED:
                return getHeader();
            case ESSENTIAL:
                return new StringBuilder(getHeader())
                        .append(getBody())
                        .append("\n").toString();
            case FULL:
                return new StringBuilder(getHeader())
                        .append(getExtraData())
                        .append(getBody())
                        .append(getPossibleReasons())
                        .append(getReportMessage()).toString();
            default:
                return "";
        }
    }

    // <editor-fold defaultstate="collapsed" desc="ABSTRACT METHODS">
    protected abstract String getHeader();
    protected abstract String getPossibleReasons();
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="DEFAULT TEMPLATES">
    protected String getBody() {
        return new StringBuilder()
                .append("\n\nStackTrace:")
                .append("\n-----------")
                .append(getSource()).toString();
    }

    protected String getExtraData() {
        String extradata = "";

        if (!this.data.isEmpty()) {
            extradata = "\n\nMore information:";
            extradata += "\n-----------------";
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                extradata += new StringBuilder().append("\n- ").append(key)
                        .append(": ").append(value).toString();
            }
        } else if (this.custom_error != null) {
            extradata = "\nMore information: " + this.custom_error;
        }

        return extradata;
    }

    protected String getReportMessage() {
        return new StringBuilder()
                .append("\n\n-------------------------------------------------------------\n")
                .append(this.REPORT)
                .append("\n-------------------------------------------------------------").toString();
    }
    // </editor-fold>

    /* UTILS */
    protected String getSource() {
        String source = "";

        for (StackTraceElement stackTrace : this.getStackTrace()) {
            String str = stackTrace.toString();
            if (str.contains(PluginBase.INSTANCE.plugin().getDescription()
                    .getName().toLowerCase())) {
                source += "\n" + str;
            } else {
                break;
            }
        }

        return source;
    }

}
