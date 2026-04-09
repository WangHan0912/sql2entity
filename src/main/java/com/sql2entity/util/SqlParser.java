package com.sql2entity.util;

import com.sql2entity.model.TableInfo;
import com.sql2entity.model.TableInfo.ColumnInfo;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlParser {

    private static final Pattern COMMENT_PATTERN = Pattern.compile("COMMENT\\s+'([^']*)'", Pattern.CASE_INSENSITIVE);
    private static final Pattern LENGTH_PATTERN = Pattern.compile("\\(([0-9,]+)\\)");
    private static final Pattern AUTO_INCREMENT_PATTERN = Pattern.compile("AUTO_INCREMENT|IDENTITY|SERIAL", Pattern.CASE_INSENSITIVE);
    private static final Pattern DEFAULT_PATTERN = Pattern.compile("DEFAULT\\s+([^\\s,]+)", Pattern.CASE_INSENSITIVE);

    public static TableInfo parse(String sql, String customClassName, String dbType) throws JSQLParserException {
        CreateTable createTable = (CreateTable) CCJSqlParserUtil.parse(sql);
        Table table = createTable.getTable();
        
        TableInfo tableInfo = new TableInfo();
        tableInfo.setDbType(dbType);
        tableInfo.setTableName(table.getName());
        
        String className = customClassName;
        if (className == null || className.trim().isEmpty()) {
            className = toClassName(table.getName());
        }
        tableInfo.setClassName(className);
        tableInfo.setMapperName(className + "Mapper");
        
        // 获取主键列名
        List<String> primaryKeyColumns = new ArrayList<>();
        if (createTable.getTableOptionsStrings() != null) {
            String options = String.join(" ", createTable.getTableOptionsStrings()).toUpperCase();
            if (options.contains("PRIMARY KEY")) {
                Matcher m = Pattern.compile("PRIMARY\\s+KEY\\s*\\(([^)]+)\\)", Pattern.CASE_INSENSITIVE).matcher(sql);
                if (m.find()) {
                    String pkCols = m.group(1);
                    for (String col : pkCols.split(",")) {
                        primaryKeyColumns.add(col.trim().replace("`", "").replace("\"", ""));
                    }
                }
            }
        }
        
        List<ColumnInfo> columns = new ArrayList<>();
        for (ColumnDefinition def : createTable.getColumnDefinitions()) {
            ColumnInfo col = new ColumnInfo();
            String columnName = def.getColumnName();
            col.setColumnName(columnName);
            col.setFieldName(toFieldName(columnName));
            
            String colSpec = def.getColDataType().toString();
            col.setColumnType(extractColumnType(colSpec));
            col.setJdbcType(mapToJdbcType(colSpec, dbType));
            col.setFieldType(mapToJavaType(colSpec, dbType));
            
            // 检查是否自增
            col.setAutoIncrement(AUTO_INCREMENT_PATTERN.matcher(colSpec).find());
            
            // 解析长度
            Matcher lengthMatcher = LENGTH_PATTERN.matcher(colSpec);
            if (lengthMatcher.find()) {
                String lengthStr = lengthMatcher.group(1);
                if (lengthStr.contains(",")) {
                    String[] parts = lengthStr.split(",");
                    col.setLength(Integer.parseInt(parts[0].trim()));
                    col.setDecimalDigits(Integer.parseInt(parts[1].trim()));
                } else {
                    col.setLength(Integer.parseInt(lengthStr.trim()));
                }
            }
            
            // 检查是否主键
            col.setPrimaryKey(primaryKeyColumns.stream()
                .anyMatch(pk -> pk.equalsIgnoreCase(columnName)));
            
            // 从原SQL中分析约束
            String upperSql = sql.toUpperCase();
            String upperColName = columnName.toUpperCase();
            
            // 检查NOT NULL - 简单检查
            if (colSpec.toUpperCase().contains("NOT NULL")) {
                col.setNullable(false);
            }
            
            // 检查注释
            int colIndex = sql.toUpperCase().indexOf(upperColName);
            if (colIndex >= 0) {
                String remaining = sql.substring(colIndex, Math.min(colIndex + 200, sql.length()));
                Matcher m = COMMENT_PATTERN.matcher(remaining);
                if (m.find() && m.start() < 100) {
                    col.setComment(m.group(1));
                }
            }
            
            columns.add(col);
        }
        
        tableInfo.setColumns(columns);
        return tableInfo;
    }

    private static String extractColumnType(String spec) {
        int parenIndex = spec.indexOf('(');
        if (parenIndex > 0) {
            return spec.substring(0, parenIndex).toUpperCase();
        }
        return spec.toUpperCase().split("\\s+")[0];
    }

    private static String toClassName(String name) {
        StringBuilder sb = new StringBuilder();
        boolean capitalize = true;
        for (char c : name.toCharArray()) {
            if (c == '_' || c == '-') {
                capitalize = true;
            } else if (capitalize) {
                sb.append(Character.toUpperCase(c));
                capitalize = false;
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }
        if (sb.length() > 0 && Character.isLowerCase(sb.charAt(0))) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }
        return sb.toString();
    }

    private static String toFieldName(String name) {
        StringBuilder sb = new StringBuilder();
        boolean capitalize = false;
        for (char c : name.toCharArray()) {
            if (c == '_' || c == '-') {
                capitalize = true;
            } else if (capitalize) {
                sb.append(Character.toUpperCase(c));
                capitalize = false;
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }

    public static String mapToJavaType(String columnSpec, String dbType) {
        if (columnSpec == null) return "String";
        columnSpec = columnSpec.toUpperCase();
        
        // PostgreSQL SERIAL/BIGSERIAL
        if ("postgresql".equalsIgnoreCase(dbType)) {
            if (columnSpec.contains("SERIAL") || columnSpec.contains("IDENTITY")) {
                return columnSpec.contains("BIG") ? "Long" : "Integer";
            }
        }
        
        // Oracle NUMBER
        if ("oracle".equalsIgnoreCase(dbType)) {
            if (columnSpec.contains("NUMBER") && !columnSpec.contains(".")) {
                return "Long";
            }
        }
        
        // 通用类型映射
        if (columnSpec.matches(".*INT\\s*\\(.*|.*INTEGER.*") && !columnSpec.contains("BIGINT")) {
            return "Integer";
        } else if (columnSpec.contains("BIGINT")) {
            return "Long";
        } else if (columnSpec.contains("FLOAT") || columnSpec.contains("DOUBLE") || columnSpec.contains("DECIMAL") || columnSpec.contains("NUMERIC")) {
            return "Double";
        } else if (columnSpec.contains("BOOLEAN") || columnSpec.contains("BIT")) {
            return "Boolean";
        } else if (columnSpec.contains("DATE") && columnSpec.contains("TIME")) {
            return "LocalDateTime";
        } else if (columnSpec.contains("DATE")) {
            return "LocalDate";
        } else if (columnSpec.contains("TIME") && !columnSpec.contains("DATE")) {
            return "LocalTime";
        } else if (columnSpec.contains("BLOB") || columnSpec.contains("BINARY") || columnSpec.contains("BYTEA")) {
            return "byte[]";
        } else if (columnSpec.contains("JSON") || columnSpec.contains("JSONB")) {
            return "String";
        } else if (columnSpec.contains("TEXT") || columnSpec.contains("CHAR") || columnSpec.contains("VARCHAR") || columnSpec.contains("ENUM") || columnSpec.contains("SET") || columnSpec.contains("CLOB")) {
            return "String";
        }
        return "String";
    }

    public static String mapToJdbcType(String columnSpec, String dbType) {
        if (columnSpec == null) return "VARCHAR";
        columnSpec = columnSpec.toUpperCase();
        
        if (columnSpec.contains("INT") && !columnSpec.contains("BIGINT")) {
            return "INTEGER";
        } else if (columnSpec.contains("BIGINT")) {
            return "BIGINT";
        } else if (columnSpec.contains("FLOAT") && !columnSpec.contains("DOUBLE")) {
            return "FLOAT";
        } else if (columnSpec.contains("DOUBLE") || columnSpec.contains("DECIMAL") || columnSpec.contains("NUMERIC")) {
            return "DECIMAL";
        } else if (columnSpec.contains("BOOLEAN") || columnSpec.contains("BIT")) {
            return "BOOLEAN";
        } else if (columnSpec.contains("DATE") && columnSpec.contains("TIME")) {
            return "TIMESTAMP";
        } else if (columnSpec.contains("DATE")) {
            return "DATE";
        } else if (columnSpec.contains("TIME")) {
            return "TIME";
        } else if (columnSpec.contains("BLOB") || columnSpec.contains("BINARY") || columnSpec.contains("BYTEA")) {
            return "BLOB";
        } else if (columnSpec.contains("CLOB")) {
            return "CLOB";
        }
        return "VARCHAR";
    }
}
