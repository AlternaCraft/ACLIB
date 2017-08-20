package com.alternacraft.aclib.extras;

import java.util.Arrays;
import java.util.Collections;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author AlternaCraft
 */
public class CustomStatement {

    public static enum METHOD {
        SELECT,
        INSERT,
        UPDATE,
        DELETE
    }

    public static enum TYPES {
        INTEGER,
        LONG,
        BOOLEAN,
        TEXT,
        BLOB,
        REAL
    }

    public static enum ORDER {
        ASC,
        DESC
    }
    
    public static enum LOGIC {
        AND,
        OR
    }

    public static enum COMPARATOR {
        EQ,
        GRT,
        GRTE,
        LOW,
        LOWE
    }

    private final METHOD method;

    private Table[] tables;
    private Field[] fields;
    private GroupedCondition conditions;

    private String orderby;
    private ORDER order;

    private int limit;

    public CustomStatement(METHOD method) {
        this.method = method;
        this.tables = new Table[0];
        this.fields = new Field[0];
        this.conditions = new GroupedCondition();
    }

    public void addTables(Table... tables) {
        this.tables = tables;
    }

    public void addFields(Field... fields) {
        this.fields = fields;
    }

    public void addConditions(GroupedCondition conditions) {
        this.conditions = conditions;
    }

    public void setOrder(String field, ORDER order) {
        this.orderby = field;
        this.order = order;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSort() {
        return (this.orderby != null) ? " order by " + this.orderby + " "
                + this.order.name() : "";
    }

    public String getLimit() {
        return (this.limit > 0) ? " limit " + this.limit : "";
    }

    public String createRelationsBetweenTables() {
        String result = "";
        if (tables.length > 0) {
            result = tables[0].getName();
            for (int i = 0; i < tables.length - 1; i++) {
                result = new StringBuilder(result)
                        .append(" inner join ")
                        .append(tables[i + 1].getName())
                        .append(" on ")
                        .append(tables[i].getName())
                        .append(".")
                        .append(tables[i].getRelationKey())
                        .append(" = ")
                        .append(tables[i + 1].getName())
                        .append(".")
                        .append(tables[i + 1].getRelationKey())
                        .toString();
            }
        }
        return result;
    }

    public String createUpdateStatements() {
        String result = "";
        for (Field field : fields) {
            result = new StringBuilder(result)
                    .append(field.getName())
                    .append("=?,")
                    .toString();
        }
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public String getByMethod() {
        switch (this.method) {
            case SELECT:
                return this.getQuery();
            case UPDATE:
                return this.getUpdate();
            case DELETE:
                return this.getDelete();
            case INSERT:
                return this.getInsert();
            default:
                return null;
        }
    }

    public String getQuery() {
        return "select " + StringUtils.join(this.fields, ",") + " from "
                + this.createRelationsBetweenTables()
                + ((this.conditions.hasConditions()) ? " where " + this.conditions.toString() : "")
                + this.getSort() + this.getLimit();
    }

    public String getUpdate() {
        return "update " + this.tables[0].getName() + " set "
                + this.createUpdateStatements()
                + ((this.conditions.hasConditions()) ? " where " + this.conditions.toString() : "");
    }

    public String getDelete() {
        return "delete from " + this.tables[0].getName()
                + ((this.conditions.hasConditions()) ? " where " + this.conditions.toString() : "");
    }

    public String getInsert() {
        return "insert into " + this.tables[0].getName() + "("
                + StringUtils.join(this.fields, ",") + ") values ("
                + StringUtils.join(Collections.nCopies(this.fields.length, "?")
                        .toArray(new String[this.fields.length]), ",")
                + ")";
    }

    public Table[] getTables() {
        return tables;
    }

    public Field[] getFields() {
        return fields;
    }

    public GroupedCondition getGConditions() {
        return conditions;
    }

    public boolean isThisMethod(METHOD method) {
        return this.method.equals(method);
    }

    public METHOD getMethod() {
        return method;
    }

    public class GroupedCondition {

        private Condition[] conditions;
        private LOGIC[] operators;

        public GroupedCondition(Condition c) {
            this.conditions = new Condition[]{c};
            this.operators = new LOGIC[0];
        }

        public GroupedCondition(LOGIC operator, Condition... conditions) {
            this.conditions = conditions;
            this.operators = new LOGIC[]{operator};
        }

        public GroupedCondition() {
            this.conditions = new Condition[0];
            this.operators = new LOGIC[0];
        }

        public void addConditions(Condition... conditions) {
            this.conditions = conditions;
        }

        public void addOperators(LOGIC... operators) {
            this.operators = operators;
        }        
        
        public Condition[] getPreparedConditions() {
            return Arrays.stream(this.conditions).filter(p -> {
                return p.shouldSet();
            }).toArray(Condition[]::new);
        }

        public boolean hasConditions() {
            return this.conditions.length > 0;
        }

        @Override
        public String toString() {
            String result = "";
            if (conditions.length > 1 && Math.round(conditions.length / 2F) == this.operators.length) {
                for (int i = 0; i < conditions.length - 1; i++) {
                    result += this.conditions[i].toString() + " " + this.operators[i].name()
                            + " " + this.conditions[i + 1].toString();
                }
            } else if (conditions.length > 0) {
                result = this.conditions[0].toString();
            }
            return result;
        }
    }

    public class Condition {

        private String name;
        private String condition;
        private COMPARATOR comparator;
        private TYPES type;

        public Condition(String name, String condition, COMPARATOR comparator, TYPES type) {
            this.name = name;
            this.condition = condition;
            this.comparator = comparator;
            this.type = type;
        }

        public Condition(String name, String condition) {
            this(name, condition, COMPARATOR.EQ, null);
        }

        public Condition(String name, String condition, COMPARATOR comparator) {
            this(name, condition, comparator, null);
        }

        public Condition(String name, TYPES type) {
            this(name, "?", COMPARATOR.EQ, type);
        }

        public Condition(String name, TYPES type, COMPARATOR comparator) {
            this(name, "?", comparator, type);
        }

        public String getName() {
            return name;
        }

        public String getCondition() {
            return condition;
        }

        public COMPARATOR getComparator() {
            return comparator;
        }

        public TYPES getType() {
            return type;
        }

        public void setComparator(COMPARATOR comparator) {
            this.comparator = comparator;
        }

        public boolean shouldSet() {
            return this.condition.equals("?");
        }

        @Override
        public String toString() {
            String comp = "";
            switch (this.comparator) {
                case EQ:
                    comp = "=";
                    break;
                case GRTE:
                    comp = ">=";
                    break;
                case GRT:
                    comp = ">";
                    break;
                case LOWE:
                    comp = "<=";
                    break;
                case LOW:
                    comp = "<";
                    break;
            }
            return new StringBuilder()
                    .append(this.getName())
                    .append(comp)
                    .append(this.getCondition())
                    .toString();
        }
    }

    public class Table {

        private final String name;
        private String relation_key;

        public Table(String name) {
            this(name, null);
        }

        public Table(String name, String relation_key) {
            this.name = name;
            this.relation_key = relation_key;
        }

        public String getName() {
            return name;
        }

        public String getRelationKey() {
            return relation_key;
        }

        public void setRelation_key(String relation_key) {
            this.relation_key = relation_key;
        }
    }

    public class Field {

        private final String name;
        private final TYPES type;

        public Field(String name, TYPES type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            String[] keys = name.split("\\.");
            return keys[keys.length - 1];
        }

        public TYPES getType() {
            return type;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
