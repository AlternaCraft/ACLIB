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
import com.alternacraft.aclib.commands.SubCommandExecutor;
import com.alternacraft.aclib.extras.db.CustomStatement.Comparator;
import com.alternacraft.aclib.extras.db.CustomStatement.Field;
import com.alternacraft.aclib.extras.db.CustomStatement.GroupedCondition;
import com.alternacraft.aclib.extras.db.CustomStatement.Logic;
import com.alternacraft.aclib.extras.db.CustomStatement.Method;
import com.alternacraft.aclib.extras.db.CustomStatement.Table;
import com.alternacraft.aclib.extras.db.CustomStatement.Types;
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
        if (args.length < 2) {
            return false;
        }
        
        String method = args[0];
        String[] tables = args[1].split("\\|");
        String[] fields = (args.length > 2) ? 
                args[2].split("\\|") : null;
        String[] conditions = (args.length > 3) ? 
                args[3].split("\\|") : null;
        Object[] values = (args.length > 4) ? 
                Arrays.stream(args[4].split("\\|")).map(e -> 
                        parseValue(e)).toArray() : new Object[0];

        Method m = Method.valueOf(method.toUpperCase());

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
                if (fields != null) {
                    this.addFields(Arrays.stream(fields).map(f -> {
                                String[] items = f.split(":");
                                return new Field(
                                        items[0],
                                        Types.valueOf(items[1].toUpperCase())
                                );
                            }).toArray(Field[]::new)
                    );
                }
                if (conditions != null) {
                    List<Condition> aux_conds = new ArrayList();
                    List<String> aux_ops = new ArrayList();
                    Arrays.stream(conditions).forEach(f -> {
                        String[] items = f.split(":");
                        Condition c = new Condition(
                                items[0],
                                Types.valueOf(items[1].toUpperCase())                                
                        );
                        if (items.length > 2) {
                            c.setComparator(Comparator.valueOf(items[2].toUpperCase()));
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
                                .map(o -> Logic.valueOf(o))
                                .toArray(Logic[]::new)
                        );
                    }});
                }
            }
        };
        
        if (m.equals(Method.SELECT)) {
            try {
                MessageManager.sendPluginMessage(cs, 
                        "Result: " + connection.executeQuery(
                            statement, values
                        ).toString()
                );
            } catch (SQLException ex) {
                MessageManager.logError(ex.getMessage());
            }
        } else {
            try {
                MessageManager.sendPluginMessage(cs, 
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
