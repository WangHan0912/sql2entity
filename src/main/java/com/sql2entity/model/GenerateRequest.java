package com.sql2entity.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Entity生成请求")
public class GenerateRequest {
    
    @Schema(description = "SQL建表语句", example = "CREATE TABLE user_info (id BIGINT PRIMARY KEY, user_name VARCHAR(50) NOT NULL)")
    private String sql;
    
    @Schema(description = "包名", example = "com.example.entity")
    private String packageName = "com.example.entity";
    
    @Schema(description = "自定义类名（可选，默认从表名转换）", example = "UserInfo")
    private String className;
    
    @Schema(description = "是否使用Lombok注解", example = "true")
    private Boolean useLombok = false;
    
    @Schema(description = "数据库类型: mysql/postgresql/oracle", example = "mysql")
    private String dbType = "mysql";
    
    @Schema(description = "是否生成校验注解（@NotNull, @Size等）", example = "false")
    private Boolean generateValidation = false;
    
    @Schema(description = "是否生成MyBatis Mapper XML", example = "false")
    private Boolean generateMapper = false;
    
    // Getters and Setters
    public String getSql() { return sql; }
    public void setSql(String sql) { this.sql = sql; }
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public Boolean getUseLombok() { return useLombok; }
    public void setUseLombok(Boolean useLombok) { this.useLombok = useLombok; }
    public String getDbType() { return dbType; }
    public void setDbType(String dbType) { this.dbType = dbType; }
    public Boolean getGenerateValidation() { return generateValidation; }
    public void setGenerateValidation(Boolean generateValidation) { this.generateValidation = generateValidation; }
    public Boolean getGenerateMapper() { return generateMapper; }
    public void setGenerateMapper(Boolean generateMapper) { this.generateMapper = generateMapper; }
}
