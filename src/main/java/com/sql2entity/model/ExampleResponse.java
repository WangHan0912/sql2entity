package com.sql2entity.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "示例响应")
public class ExampleResponse {
    
    @Schema(description = "示例SQL")
    private String exampleSql;
    
    @Schema(description = "示例生成结果")
    private String generatedCode;
    
    @Schema(description = "示例说明")
    private String description;

    public ExampleResponse() {}

    public ExampleResponse(String exampleSql, String generatedCode, String description) {
        this.exampleSql = exampleSql;
        this.generatedCode = generatedCode;
        this.description = description;
    }

    // Getters and Setters
    public String getExampleSql() { return exampleSql; }
    public void setExampleSql(String exampleSql) { this.exampleSql = exampleSql; }
    public String getGeneratedCode() { return generatedCode; }
    public void setGeneratedCode(String generatedCode) { this.generatedCode = generatedCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
