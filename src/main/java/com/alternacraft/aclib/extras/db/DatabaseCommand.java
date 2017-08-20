package com.alternacraft.aclib.extras.db;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.commands.SubCommandExecutor;
import com.alternacraft.aclib.extras.db.CustomStatement;
import com.alternacraft.aclib.extras.db.CustomStatement.COMPARATOR;
import com.alternacraft.aclib.extras.db.CustomStatement.Field;
import com.alternacraft.aclib.extras.db.CustomStatement.GroupedCondition;
import com.alternacraft.aclib.extras.db.CustomStatement.LOGIC;
import com.alternacraft.aclib.extras.db.CustomStatement.METHOD;
import com.alternacraft.aclib.extras.db.CustomStatement.TYPES;
import com.alternacraft.aclib.extras.db.CustomStatement.Table;
import com.alternacraft.aclib.extras.db.SQLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

/**
 *
 * @author AlternaCraft
 */
public class DatabaseCommand implements SubCommandExecutor {

    private final SQLConnection connection;

    public DatabaseCommand(SQLConnection connection) {
        this.connection = connection;
    }
    
    @Override
    public boolean execute(CommandSender cs, String[] args) {
        if (args.length < 3) {
            return false;
        }
        
        String method = args[0];
        String[] tables = args[1].split("\\|");        
        String[] fields = args[2].split("\\|");
        String[] conditions = (args.length > 3) ? 
                args[3].split("\\|") : null;
        Object[] values = (args.length > 4) ? 
                Arrays.stream(args[4].split("\\|")).map(e -> 
                        parseValue(e)).toArray() : new Object[0];

        METHOD m = METHOD.valueOf(method.toUpperCase());

        CustomStatement statement = new CustomStatement(m) {
            {
                this.addTables(
                        Arrays.stream(tables).map(f -> {
                            String[] items = f.split(":");
                            Table t = new Table(items[0]);
                            if (items.length > 1) {
                                t.setRelation_key(items[1]);
                            }
                            return t;
                        }).toArray(Table[]::new)
                );
                this.addFields(
                        Arrays.stream(fields).map(f -> {
                            String[] items = f.split(":");
                            return new Field(
                                    items[0],
                                    TYPES.valueOf(items[1].toUpperCase())
                            );
                        }).toArray(Field[]::new)
                );
                if (conditions != null) {
                    List<Condition> aux_conds = new ArrayList();
                    List<String> aux_ops = new ArrayList();
                    Arrays.stream(conditions).forEach(f -> {
                        String[] items = f.split(":");
                        Condition c = new Condition(
                                items[0],
                                TYPES.valueOf(items[1].toUpperCase())                                
                        );
                        if (items.length > 2) {
                            c.setComparator(COMPARATOR.valueOf(items[2].toUpperCase()));
                        }
                        aux_conds.add(c);
                        if (items.length > 3) {
                            aux_ops.add(items[3]);
                        }
                    });
                    this.addConditions(new GroupedCondition() {{
                        this.addConditions(aux_conds
                                .stream()
                                .toArray(Condition[]::new));
                        this.addOperators(aux_ops
                                .stream()
                                .map(o -> LOGIC.valueOf(o))
                                .toArray(LOGIC[]::new)
                        );
                    }});
                }
            }
        };
        
        if (m.equals(METHOD.SELECT)) {
            try {
                MessageManager.sendCommandSender(cs, 
                        "Result: " + connection.executeQuery(
                            statement, values
                        ).toString()
                );
            } catch (SQLException ex) {
                MessageManager.logError(ex.getMessage());
            }
        } else {
            try {
                MessageManager.sendCommandSender(cs, 
                        String.valueOf(connection.executeUpdate(
                            statement, values
                        ))
                );
            } catch (SQLException ex) {
                MessageManager.logError(ex.getMessage());
            }
        }

        return true;
    }
    
    public static Object parseValue(String str) {
        return (StringUtils.isNumeric(str)) ? Integer.valueOf(str) : str;
    }
}
