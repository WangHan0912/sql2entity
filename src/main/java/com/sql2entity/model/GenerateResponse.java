package com.sql2entity.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Entity生成响应")
public class GenerateResponse {
    
    @Schema(description = "是否成功")
    private boolean success;
    
    @Schema(description = "生成的Entity代码")
    private String entityCode;
    
    @Schema(description = "生成的Mapper XML（如果启用）")
    private String mapperXml;
    
    @Schema(description = "错误信息")
    private String message;
    
    @Schema(description = "表名")
    private String tableName;
    
    @Schema(description = "类名")
    private String className;

    public GenerateResponse() {}

    public static GenerateResponse ok(String entityCode) {
        GenerateResponse resp = new GenerateResponse();
        resp.success = true;
        resp.entityCode = entityCode;
        return resp;
    }
    
    public static GenerateResponse ok(String entityCode, String mapperXml, String tableName, String className) {
        GenerateResponse resp = new GenerateResponse();
        resp.success = true;
        resp.entityCode = entityCode;
        resp.mapperXml = mapperXml;
        resp.tableName = tableName;
        resp.className = className;
        return resp;
    }

    public static GenerateResponse error(String message) {
        GenerateResponse resp = new GenerateResponse();
        resp.success = false;
        resp.message = message;
        return resp;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getEntityCode() { return entityCode; }
    public void setEntityCode(String entityCode) { this.entityCode = entityCode; }
    public String getMapperXml() { return mapperXml; }
    public void setMapperXml(String mapperXml) { this.mapperXml = mapperXml; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
}
