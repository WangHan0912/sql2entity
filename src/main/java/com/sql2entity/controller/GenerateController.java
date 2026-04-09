package com.sql2entity.controller;

import com.sql2entity.model.*;
import com.sql2entity.service.EntityGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@Tag(name = "SQL转Entity", description = "SQL建表语句转换为Java Entity类")
public class GenerateController {

    @Autowired
    private EntityGeneratorService entityGeneratorService;
    
    // 存储临时生成的代码，key为token
    private static final java.util.Map<String, GenerateResponse> codeCache = new java.util.concurrent.ConcurrentHashMap<>();

    @PostMapping("/generate")
    @Operation(summary = "生成Entity", description = "将SQL建表语句转换为Java Entity类代码")
    public GenerateResponse generate(@RequestBody GenerateRequest request) {
        return entityGeneratorService.generate(request);
    }

    @PostMapping("/batch")
    @Operation(summary = "批量生成Entity", description = "一次传入多个SQL语句，批量生成多个Entity类")
    public BatchResponse batchGenerate(@RequestBody BatchRequest request) {
        return entityGeneratorService.batchGenerate(request);
    }

    @GetMapping("/example")
    @Operation(summary = "获取示例", description = "返回一个示例SQL和生成结果，方便测试API")
    public ExampleResponse getExample(
            @Parameter(description = "数据库类型: mysql/postgresql/oracle", example = "mysql")
            @RequestParam(defaultValue = "mysql") String dbType) {
        return entityGeneratorService.getExample(dbType);
    }

    @GetMapping("/download")
    @Operation(summary = "下载代码", description = "下载生成的Entity代码")
    public ResponseEntity<String> download(
            @Parameter(description = "临时令牌")
            @RequestParam String token) {
        GenerateResponse cached = codeCache.get(token);
        if (cached == null) {
            return ResponseEntity.notFound().build();
        }
        
        String content = cached.getEntityCode();
        if (cached.getMapperXml() != null) {
            content = "=== Entity ===\n\n" + content + "\n\n=== Mapper ===\n\n" + cached.getMapperXml();
        }
        
        String filename = cached.getClassName() + ".java.txt";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(content);
    }

    @PostMapping("/generate-with-download")
    @Operation(summary = "生成并返回下载token", description = "生成代码并返回临时下载令牌")
    public java.util.Map<String, Object> generateWithDownload(@RequestBody GenerateRequest request) {
        GenerateResponse resp = entityGeneratorService.generate(request);
        
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", resp.isSuccess());
        
        if (resp.isSuccess()) {
            String token = UUID.randomUUID().toString();
            codeCache.put(token, resp);
            result.put("token", token);
            result.put("downloadUrl", "/api/download?token=" + token);
            result.put("entityCode", resp.getEntityCode());
            if (resp.getMapperXml() != null) {
                result.put("mapperXml", resp.getMapperXml());
            }
        } else {
            result.put("message", resp.getMessage());
        }
        
        return result;
    }

    @GetMapping("/health")
    @Operation(summary = "健康检查")
    public String health() {
        return "{\"status\":\"UP\"}";
    }
}
