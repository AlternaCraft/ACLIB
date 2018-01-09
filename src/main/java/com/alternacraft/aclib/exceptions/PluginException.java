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
package com.alternacraft.aclib.exceptions;

import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.utils.exceptions.ErrorManager;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Custom exception.
 * This add some extras:
 * <ul>
 *  <li>Structured messages
 *      <ul>
 *          <li>There are three types:
 *              <ul>
 *                  <li><b>Simplified</b>. Just for indicating an error</li>
 *                  <li><b>Essential</b>. Essential information to find a reason</li>
 *                  <li><b>Full</b>. All the data</li>
 *              </ul>
 *          </li>
 *      </ul>
 *  </li>
 *  <li>How to report</li>
 *  <li>Functionality for finding a possible reason of the error</li>
 * </ul>
 *
 * @author AlternaCraft
 */
public class PluginException extends Exception {

    protected static final PluginBase PLUGIN = PluginBase.INSTANCE;

    protected static final String NAME = PLUGIN.plugin().getDescription().getName();
    protected static final String VERSION = PLUGIN.plugin().getDescription().getVersion();

    protected static final ChatColor V = ChatColor.GREEN;
    protected static final ChatColor G = ChatColor.GRAY;
    protected static final ChatColor R = ChatColor.RED;
    protected static final ChatColor L = ChatColor.RESET;

    protected final String REPORT = "If you don't know the error cause please, report it.";

    protected static final short SIMPLIFIED = 0;
    protected static final short ESSENTIAL = 1;
    protected static final short FULL = 2;  
    
    protected Map<String, Object> data = new LinkedHashMap();
    protected String custom_error = null;

    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTORS">    
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
    
    /* With previous stacktrace */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public PluginException(String message, StackTraceElement[] ste) {
        super(message);
        this.setStackTrace(ste);
    }

    public PluginException(String message, StackTraceElement[] ste, Map<String, Object> data) {
        this(message, ste);
        this.data = data;
    }

    public PluginException(String message, StackTraceElement[] ste, String custom_error) {
        this(message, ste);
        this.custom_error = custom_error;
    }

    /* Even easier */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public PluginException(Exception ex) {
        super(ex.getMessage());
        this.setStackTrace(ex.getStackTrace());
    }

    public PluginException(Exception ex, Map<String, Object> data) {
        this(ex.getMessage(), ex.getStackTrace());
        this.data = data;
    }

    public PluginException(Exception ex, String custom_error) {
        this(ex.getMessage(), ex.getStackTrace());
        this.custom_error = custom_error;
    }
    // </editor-fold>

    public Object[] getCustomStacktrace() {
        int n = PluginBase.INSTANCE.getErrorFormat();
        List result = new ArrayList();
        
        ErrorManager.analyzePossibleReasons(this.getMessage(), this.data, 
                this.custom_error); // Keep data
        
        switch (n) {
            case SIMPLIFIED:
                result.addAll(getHeader());
                break;
            case ESSENTIAL:
                result.addAll(getHeader());
                result.addAll(getBody());
                break;
            case FULL:
                result.add("-------------------------------------------------------------");
                result.addAll(getHeader());
                result.add("-------------------------------------------------------------");
                result.addAll(getExtraData());
                result.addAll(getBody());
                result.addAll(getPossibleReasons());
                result.addAll(getReportMessage());
        }

        return result.toArray();
    }

    // <editor-fold defaultstate="collapsed" desc="DEFAULT TEMPLATES">
    protected List getHeader() {
        return new ArrayList<String>() {{ 
            this.add(new StringBuilder(getMessage())
                    .append(ErrorManager.getLastCodes()).toString()); 
        }};
    }

    protected List getPossibleReasons() {
        return new ArrayList() {{ 
            this.add("          " + G + "====== " + V + "POSSIBLE REASONS" + G + " ======");
            this.addAll(ErrorManager.getLastMessages());
        }};
    }

    protected List getBody() {
        return new ArrayList<String>() {
            {
                this.add("          " + G + "====== " + V + "STACK TRACE" + G + " ======");
                this.addAll(getSource());
                this.add("          " + G + "====== " + V + "DUMP" + G + " ======");
                this.addAll(getPluginInformation());
            }
        };
    }

    protected List getExtraData() {
        return new ArrayList<String>() {
            {
                this.add("          " + G + "====== " + V + "MORE INFORMATION" + G + " ======");
                if (!data.isEmpty()) {
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        this.add(new StringBuilder().append("- ").append(key)
                                .append(": ").append(value).toString());
                    }
                } else if (custom_error != null) {
                    this.add(custom_error);
                }
            }
        };
    }

    protected List getReportMessage() {
        return new ArrayList<String>() {
            {
                this.add("-------------------------------------------------------------");
                this.add(R + REPORT);
                this.add("-------------------------------------------------------------");
            }
        };
    }
    // </editor-fold>

    /* UTILS */
    protected List getSource() {
        return new ArrayList() {
            {
                for (StackTraceElement stackTrace : getStackTrace()) {
                    String str = stackTrace.toString();
                    if (str.contains(NAME.toLowerCase())) {                        
                        this.add(
                          new StringBuilder()
                                .append(stackTrace.getClassName()
                                  .replaceAll(".*\\." + NAME.toLowerCase() + "\\.", ""))
                                .append("(")
                                  .append(stackTrace.getMethodName())
                                  .append(" -> ")
                                  .append(stackTrace.getLineNumber())
                                .append(")").toString()
                        );
                    }
                }
            }
        };
    }

    protected List getPluginInformation() {
        return new ArrayList<String>() {
            {
                this.add(G + "Plugin name: " + L + NAME);
                this.add(G + "Plugin version: " + L + VERSION);
                this.add(G + "Bukkit version: " + L + Bukkit.getBukkitVersion());
            }
        };
    }
}
