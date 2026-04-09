package com.sql2entity.service;

import com.sql2entity.model.*;
import com.sql2entity.util.SqlParser;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.*;

@Service
public class EntityGeneratorService {

    private final Configuration freemarkerConfig;

    public EntityGeneratorService() {
        this.freemarkerConfig = new Configuration(new Version("2.3.31"));
        this.freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
    }

    public GenerateResponse generate(GenerateRequest request) {
        if (request.getSql() == null || request.getSql().trim().isEmpty()) {
            return GenerateResponse.error("SQL语句不能为空");
        }

        try {
            String dbType = request.getDbType();
            if (dbType == null) dbType = "mysql";
            
            TableInfo tableInfo = SqlParser.parse(request.getSql(), request.getClassName(), dbType);
            
            Map<String, Object> data = new HashMap<>();
            data.put("packageName", request.getPackageName());
            data.put("tableInfo", tableInfo);
            data.put("useLombok", Boolean.TRUE.equals(request.getUseLombok()));
            data.put("generateValidation", Boolean.TRUE.equals(request.getGenerateValidation()));
            data.put("dbType", dbType);
            
            String entityCode = processTemplate("entity.ftl", data);
            
            // 如果需要生成Mapper
            if (Boolean.TRUE.equals(request.getGenerateMapper())) {
                Map<String, Object> mapperData = new HashMap<>();
                mapperData.put("tableInfo", tableInfo);
                mapperData.put("packageName", request.getPackageName());
                mapperData.put("dbType", dbType);
                String mapperXml = processTemplate("mapper.ftl", mapperData);
                return GenerateResponse.ok(entityCode, mapperXml, tableInfo.getTableName(), tableInfo.getClassName());
            }
            
            return GenerateResponse.ok(entityCode);
        } catch (JSQLParserException e) {
            return GenerateResponse.error("SQL解析失败: " + e.getMessage());
        } catch (Exception e) {
            return GenerateResponse.error("生成失败: " + e.getMessage());
        }
    }

    public BatchResponse batchGenerate(BatchRequest request) {
        if (request.getSqlList() == null || request.getSqlList().isEmpty()) {
            return BatchResponse.error("SQL列表不能为空");
        }

        List<GenerateResponse> results = new ArrayList<>();
        for (String sql : request.getSqlList()) {
            GenerateRequest req = new GenerateRequest();
            req.setSql(sql);
            req.setPackageName(request.getPackageName());
            req.setUseLombok(request.getUseLombok());
            req.setDbType(request.getDbType());
            req.setGenerateValidation(request.getGenerateValidation());
            req.setGenerateMapper(request.getGenerateMapper());
            
            GenerateResponse resp = generate(req);
            results.add(resp);
        }

        long successCount = results.stream().filter(GenerateResponse::isSuccess).count();
        long failCount = results.stream().filter(r -> !r.isSuccess()).count();
        
        BatchResponse batchResp = BatchResponse.ok(results);
        batchResp.setSuccessCount((int) successCount);
        batchResp.setFailCount((int) failCount);
        return batchResp;
    }

    public ExampleResponse getExample(String dbType) {
        String sql;
        String generatedCode;
        String description;
        
        if ("postgresql".equalsIgnoreCase(dbType)) {
            sql = "CREATE TABLE tb_user (\n" +
                  "    user_id BIGSERIAL PRIMARY KEY,\n" +
                  "    user_name VARCHAR(50) NOT NULL,\n" +
                  "    user_email VARCHAR(100),\n" +
                  "    user_age INTEGER DEFAULT 0,\n" +
                  "    user_status SMALLINT DEFAULT 1,\n" +
                  "    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                  "    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                  "    remark TEXT\n" +
                  ")";
            description = "PostgreSQL示例：包含自增主键、多种数据类型、默认值等";
        } else if ("oracle".equalsIgnoreCase(dbType)) {
            sql = "CREATE TABLE TB_USER (\n" +
                  "    USER_ID NUMBER(20) PRIMARY KEY,\n" +
                  "    USER_NAME VARCHAR2(50) NOT NULL,\n" +
                  "    USER_EMAIL VARCHAR2(100),\n" +
                  "    USER_AGE NUMBER(10) DEFAULT 0,\n" +
                  "    CREATE_TIME TIMESTAMP DEFAULT SYSTIMESTAMP,\n" +
                  "    REMARK CLOB\n" +
                  ")";
            description = "Oracle示例：包含NUMBER类型、VARCHAR2、CLOB等";
        } else {
            sql = "CREATE TABLE tb_user (\n" +
                  "    id BIGINT PRIMARY KEY AUTO_INCREMENT,\n" +
                  "    user_name VARCHAR(50) NOT NULL COMMENT '用户名',\n" +
                  "    user_email VARCHAR(100) COMMENT '邮箱',\n" +
                  "    user_age INT DEFAULT 0 COMMENT '年龄',\n" +
                  "    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                  "    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'\n" +
                  ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表'";
            description = "MySQL示例：包含自增主键、多种数据类型、注释等";
        }
        
        GenerateRequest req = new GenerateRequest();
        req.setSql(sql);
        req.setPackageName("com.example.entity");
        req.setUseLombok(true);
        req.setDbType(dbType);
        req.setGenerateValidation(true);
        req.setGenerateMapper(true);
        
        GenerateResponse resp = generate(req);
        generatedCode = "=== Entity代码 ===\n\n" + resp.getEntityCode();
        if (resp.getMapperXml() != null) {
            generatedCode += "\n\n=== Mapper XML ===\n\n" + resp.getMapperXml();
        }
        
        return new ExampleResponse(sql, generatedCode, description);
    }

    public String downloadCode(GenerateRequest request) {
        GenerateResponse resp = generate(request);
        if (!resp.isSuccess()) {
            return "生成失败: " + resp.getMessage();
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(resp.getClassName()).append(".java\n\n");
        sb.append(resp.getEntityCode());
        
        if (resp.getMapperXml() != null) {
            sb.append("\n\n# ").append(resp.getClassName()).append("Mapper.xml\n\n");
            sb.append(resp.getMapperXml());
        }
        
        return sb.toString();
    }

    private String processTemplate(String templateName, Map<String, Object> data) throws Exception {
        Template template = freemarkerConfig.getTemplate(templateName);
        StringWriter writer = new StringWriter();
        template.process(data, writer);
        return writer.toString();
    }
}
