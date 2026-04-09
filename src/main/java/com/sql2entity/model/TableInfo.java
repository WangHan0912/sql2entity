package com.sql2entity.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "表信息")
public class TableInfo {
    
    @Schema(description = "数据库类型")
    private String dbType;
    
    @Schema(description = "表名")
    private String tableName;
    
    @Schema(description = "类名")
    private String className;
    
    @Schema(description = "Mapper名称")
    private String mapperName;
    
    @Schema(description = "列信息列表")
    private List<ColumnInfo> columns;

    // Getters and Setters
    public String getDbType() { return dbType; }
    public void setDbType(String dbType) { this.dbType = dbType; }
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getMapperName() { return mapperName; }
    public void setMapperName(String mapperName) { this.mapperName = mapperName; }
    public List<ColumnInfo> getColumns() { return columns; }
    public void setColumns(List<ColumnInfo> columns) { this.columns = columns; }

    @Schema(description = "列信息")
    public static class ColumnInfo {
        @Schema(description = "列名")
        private String columnName;
        
        @Schema(description = "字段名（驼峰）")
        private String fieldName;
        
        @Schema(description = "Java类型")
        private String fieldType;
        
        @Schema(description = "JDBC类型")
        private String jdbcType;
        
        @Schema(description = "列类型（varchar, int等）")
        private String columnType;
        
        @Schema(description = "长度")
        private Integer length;
        
        @Schema(description = "小数位")
        private Integer decimalDigits;
        
        @Schema(description = "是否主键")
        private boolean primaryKey;
        
        @Schema(description = "是否自增")
        private boolean autoIncrement;
        
        @Schema(description = "是否可为空")
        private boolean nullable = true;
        
        @Schema(description = "默认值")
        private String defaultValue;
        
        @Schema(description = "注释")
        private String comment;

        // Getters and Setters
        public String getColumnName() { return columnName; }
        public void setColumnName(String columnName) { this.columnName = columnName; }
        public String getFieldName() { return fieldName; }
        public void setFieldName(String fieldName) { this.fieldName = fieldName; }
        public String getFieldType() { return fieldType; }
        public void setFieldType(String fieldType) { this.fieldType = fieldType; }
        public String getJdbcType() { return jdbcType; }
        public void setJdbcType(String jdbcType) { this.jdbcType = jdbcType; }
        public String getColumnType() { return columnType; }
        public void setColumnType(String columnType) { this.columnType = columnType; }
        public Integer getLength() { return length; }
        public void setLength(Integer length) { this.length = length; }
        public Integer getDecimalDigits() { return decimalDigits; }
        public void setDecimalDigits(Integer decimalDigits) { this.decimalDigits = decimalDigits; }
        public boolean isPrimaryKey() { return primaryKey; }
        public void setPrimaryKey(boolean primaryKey) { this.primaryKey = primaryKey; }
        public boolean isAutoIncrement() { return autoIncrement; }
        public void setAutoIncrement(boolean autoIncrement) { this.autoIncrement = autoIncrement; }
        public boolean isNullable() { return nullable; }
        public void setNullable(boolean nullable) { this.nullable = nullable; }
        public String getDefaultValue() { return defaultValue; }
        public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }
}
