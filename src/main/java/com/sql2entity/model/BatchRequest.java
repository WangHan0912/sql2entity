package com.sql2entity.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "批量生成请求")
public class BatchRequest {
    
    @Schema(description = "SQL建表语句列表")
    private List<String> sqlList;
    
    @Schema(description = "包名", example = "com.example.entity")
    private String packageName = "com.example.entity";
    
    @Schema(description = "是否使用Lombok注解", example = "true")
    private Boolean useLombok = false;
    
    @Schema(description = "数据库类型: mysql/postgresql/oracle", example = "mysql")
    private String dbType = "mysql";
    
    @Schema(description = "是否生成校验注解", example = "false")
    private Boolean generateValidation = false;
    
    @Schema(description = "是否生成MyBatis Mapper XML", example = "false")
    private Boolean generateMapper = false;

    // Getters and Setters
    public List<String> getSqlList() { return sqlList; }
    public void setSqlList(List<String> sqlList) { this.sqlList = sqlList; }
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public Boolean getUseLombok() { return useLombok; }
    public void setUseLombok(Boolean useLombok) { this.useLombok = useLombok; }
    public String getDbType() { return dbType; }
    public void setDbType(String dbType) { this.dbType = dbType; }
    public Boolean getGenerateValidation() { return generateValidation; }
    public void setGenerateValidation(Boolean generateValidation) { this.generateValidation = generateValidation; }
    public Boolean getGenerateMapper() { return generateMapper; }
    public void setGenerateMapper(Boolean generateMapper) { this.generateMapper = generateMapper; }
}
