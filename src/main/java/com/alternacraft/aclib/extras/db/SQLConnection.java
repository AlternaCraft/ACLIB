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
package com.alternacraft.aclib.extras.db;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.exceptions.PluginException;
import com.alternacraft.aclib.exceptions.UndefinedMethodException;
import com.alternacraft.aclib.extras.db.CustomStatement.Condition;
import com.alternacraft.aclib.extras.db.CustomStatement.Field;
import com.alternacraft.aclib.extras.db.CustomStatement.Method;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.alternacraft.aclib.PluginBase.METER;

/**
 * @author AlternaCraft
 */
public abstract class SQLConnection {

    //<editor-fold defaultstate="collapsed" desc="VARS + CONSTRUCTOR">    
    public enum Drivers {
        MYSQL("com.mysql.jdbc.Driver", "jdbc:mysql://"),
        SQLITE("org.sqlite.JDBC", "jdbc:sqlite:");

        private final String driver;
        private final String prefix;

        Drivers(String driver, String prefix) {
            this.driver = driver;
            this.prefix = prefix;
        }

        public String getDriver() {
            return driver;
        }

        public String getCompleteURL(String to) {
            return this.prefix + to;
        }
    }

    public enum Status {
        CONNECTED,
        NOT_CONNECTED
    }

    private final static String DEFAULT_USER = "root";
    private final static String DEFAULT_PASS = "";

    public Status status = Status.NOT_CONNECTED;

    protected java.sql.Connection connection;

    protected Drivers driver;
    protected String url, user, password;

    public SQLConnection(Drivers driver, String url) {
        this(driver, url, DEFAULT_USER, DEFAULT_PASS);
    }

    public SQLConnection(Drivers driver, String url, String user, String password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONNECTION">
    public void connectDB() throws PluginException {
        HikariDataSource ds = new HikariDataSource();

        ds.setDriverClassName(this.driver.getDriver());
        ds.setJdbcUrl(this.driver.getCompleteURL(this.url));

        switch (this.driver) {
            case MYSQL:
                ds.setUsername(this.user);
                ds.setPassword(this.password);
                break;
            case SQLITE:
                ds.setConnectionInitSql("PRAGMA foreign_keys = ON");
                break;
        }
        try {
            connection = ds.getConnection();
            status = Status.CONNECTED;
        } catch (SQLException ex) {
            status = Status.NOT_CONNECTED;
            throw new PluginException("SQL Exception: " + ex.getErrorCode());
        }
        this.load();
    }

    public boolean isConnected() throws PluginException {
        boolean valida = false;

        if (connection != null) {
            try {
                valida = connection.isValid(3) && !connection.isClosed();
                if (valida) {
                    status = Status.CONNECTED;
                } else {
                    status = Status.NOT_CONNECTED;
                }
            } catch (SQLException ex) {
                status = Status.NOT_CONNECTED;
            } catch (AbstractMethodError ex) {
                valida = true; // Should continue?
            }
        }

        return valida;
    }

    public void closeConnection() {
        try {
            if (!connection.isClosed()) {
                connection.close();
                status = Status.NOT_CONNECTED;
            }
        } catch (SQLException | AbstractMethodError ex) {
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="QUERIES">    
    public List<Map<String, Object>> executeQuery(CustomStatement query,
                                                  Object... values) throws SQLException {
        List<Map<String, Object>> data = new ArrayList();

        PreparedStatement ps = this.basics(query, values);

        String id = query.getMethod().name() + " ON " + query.getTables()[0].getName().toUpperCase();
        METER.start(id);
        try (ResultSet results = ps.executeQuery()) {
            while (results.next()) {
                HashMap<String, Object> round = new HashMap<>();
                for (Field field : query.getFields()) {
                    switch (field.getType()) {
                        case BLOB:
                            round.put(field.getName(), results.getBlob(field.getName()));
                            break;
                        case BOOLEAN:
                            round.put(field.getName(), results.getBoolean(field.getName()));
                            break;
                        case INTEGER:
                            round.put(field.getName(), results.getInt(field.getName()));
                            break;
                        case LONG:
                            round.put(field.getName(), results.getLong(field.getName()));
                            break;
                        case REAL:
                            round.put(field.getName(), results.getFloat(field.getName()));
                            break;
                        case TEXT:
                            round.put(field.getName(), results.getString(field.getName()));
                            break;
                    }
                }
                data.add(round);
            }
        }
        METER.recordTime(id);
        return data;
    }

    public int executeUpdate(CustomStatement query, Object... values)
            throws SQLException {
        int result;
        String id = query.getMethod().name() + " ON " + query.getTables()[0].getName().toUpperCase();
        METER.start(id);
        try (PreparedStatement ps = this.basics(query, values)) {
            result = ps.executeUpdate();
        }
        METER.recordTime(id);
        return result;
    }

    public int executeUpdateAndGet(CustomStatement query, Object... values)
            throws SQLException {
        int key = -1;
        String id = query.getMethod().name() + " ON " + query.getTables()[0].getName().toUpperCase();
        METER.start(id);
        try (PreparedStatement ps = this.basics(query, values)) {
            if (ps.executeUpdate() > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        key = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creation failed, no ID was obtained.");
                    }
                }
            }
        }
        METER.recordTime(id);
        return key;
    }

    public PreparedStatement basics(CustomStatement query, Object... values)
            throws SQLException {
        boolean extended = query.getFields().length > 0 && query.getGConditions().hasConditions()
                && (query.isThisMethod(Method.INSERT)
                || query.isThisMethod(Method.UPDATE));
        try {
            String qr = query.getByMethod();
            MessageManager.logDebug(qr + " - extended: " + extended + " | "
                    + Arrays.asList(values).toString());
            PreparedStatement ps = this.connection.prepareStatement(qr);
            if (query.isThisMethod(Method.INSERT)
                    || query.isThisMethod(Method.UPDATE)) {
                for (int i = 0; i < query.getFields().length; i++) {
                    Field field = query.getFields()[i];
                    switch (field.getType()) {
                        case BLOB:
                            ps.setBlob(i + 1, (Blob) values[i]);
                            break;
                        case BOOLEAN:
                            ps.setInt(i + 1, ((boolean) values[i]) ? 1 : 0);
                            break;
                        case INTEGER:
                            ps.setInt(i + 1, (int) values[i]);
                            break;
                        case LONG:
                            ps.setLong(i + 1, (long) values[i]);
                            break;
                        case REAL:
                            ps.setFloat(i + 1, (float) values[i]);
                            break;
                        case TEXT:
                            ps.setString(i + 1, (String) values[i]);
                            break;
                    }
                }
            }
            int from = (extended) ? query.getFields().length : 0;
            for (int i = 0; i < query.getGConditions().getPreparedConditions().length; i++) {
                Condition condition = query.getGConditions().getPreparedConditions()[i];
                if (!condition.shouldSet()) {
                    continue;
                }
                switch (condition.getType()) {
                    case BLOB:
                        ps.setBlob(from + i + 1, (Blob) values[from + i]);
                        break;
                    case BOOLEAN:
                        ps.setInt(from + i + 1, ((boolean) values[from + i]) ? 1 : 0);
                        break;
                    case INTEGER:
                        ps.setInt(from + i + 1, (int) values[from + i]);
                        break;
                    case LONG:
                        ps.setLong(from + i + 1, (long) values[from + i]);
                        break;
                    case REAL:
                        ps.setFloat(from + i + 1, (float) values[from + i]);
                        break;
                    case TEXT:
                        ps.setString(from + i + 1, (String) values[from + i]);
                        break;
                }
            }
            return ps;
        } catch (UndefinedMethodException ex) {
            MessageManager.logError("Invalid method name: " + query.getMethod().name());
            return null;
        }
    }
    //</editor-fold>   

    public abstract void load() throws PluginException;
}
