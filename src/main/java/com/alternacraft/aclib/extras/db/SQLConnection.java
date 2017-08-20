package com.alternacraft.aclib.extras.db;

import com.alternacraft.aclib.MessageManager;
import static com.alternacraft.aclib.PluginBase.METER;
import com.alternacraft.aclib.exceptions.PluginException;
import com.alternacraft.aclib.extras.db.CustomStatement.Condition;
import com.alternacraft.aclib.extras.db.CustomStatement.Field;
import com.alternacraft.aclib.extras.db.CustomStatement.METHOD;
import static com.alternacraft.aclib.extras.db.CustomStatement.TYPES.BLOB;
import static com.alternacraft.aclib.extras.db.CustomStatement.TYPES.BOOLEAN;
import static com.alternacraft.aclib.extras.db.CustomStatement.TYPES.INTEGER;
import static com.alternacraft.aclib.extras.db.CustomStatement.TYPES.LONG;
import static com.alternacraft.aclib.extras.db.CustomStatement.TYPES.REAL;
import static com.alternacraft.aclib.extras.db.CustomStatement.TYPES.TEXT;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author AlternaCraft
 */
public abstract class SQLConnection {

    /**
     * constante DRIVER_MYSQL para definir el driver de MySQL
     */
    protected static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";

    /**
     * constante DRIVER_SQLITE para definir el driver de SQLite
     */
    protected static final String DRIVER_SQLITE = "org.sqlite.JDBC";

    public static enum STATUS_AVAILABLE {
        CONNECTED,
        NOT_CONNECTED;
    }
    
    public STATUS_AVAILABLE status = STATUS_AVAILABLE.NOT_CONNECTED;
    
    protected java.sql.Connection connection;
    
    public boolean isConnected() throws PluginException {
        boolean valida = false;

        if (connection != null) {
            try {
                valida = connection.isValid(3) && !connection.isClosed();
                if (valida) {
                    status = STATUS_AVAILABLE.CONNECTED;
                } else {
                    status = STATUS_AVAILABLE.NOT_CONNECTED;
                }
            } catch (SQLException ex) {
                status = STATUS_AVAILABLE.NOT_CONNECTED;
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
                status = STATUS_AVAILABLE.NOT_CONNECTED;
            }
        } catch (SQLException | AbstractMethodError ex) {
        }
    }
    
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
                && (query.isThisMethod(METHOD.INSERT)
                    || query.isThisMethod(METHOD.UPDATE));
        String qr = query.getByMethod();
        MessageManager.logDebug(qr + " - extended: " + extended + " | " 
                + Arrays.asList(values).toString());
        PreparedStatement ps = this.connection.prepareStatement(qr);
        if (query.isThisMethod(METHOD.INSERT)
                || query.isThisMethod(METHOD.UPDATE)) {
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
    }

    public abstract void connectDB(String... args) throws PluginException;
    public abstract void load() throws PluginException;
}
