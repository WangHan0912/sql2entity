package com.sql2entity.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "批量生成响应")
public class BatchResponse {
    
    @Schema(description = "是否全部成功")
    private boolean success;
    
    @Schema(description = "生成结果列表")
    private List<GenerateResponse> results;
    
    @Schema(description = "错误信息")
    private String message;
    
    @Schema(description = "成功数量")
    private int successCount;
    
    @Schema(description = "失败数量")
    private int failCount;

    public BatchResponse() {}

    public static BatchResponse ok(List<GenerateResponse> results) {
        BatchResponse resp = new BatchResponse();
        resp.success = true;
        resp.results = results;
        resp.successCount = results.size();
        resp.failCount = 0;
        return resp;
    }

    public static BatchResponse error(String message) {
        BatchResponse resp = new BatchResponse();
        resp.success = false;
        resp.message = message;
        return resp;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public List<GenerateResponse> getResults() { return results; }
    public void setResults(List<GenerateResponse> results) { this.results = results; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public int getSuccessCount() { return successCount; }
    public void setSuccessCount(int successCount) { this.successCount = successCount; }
    public int getFailCount() { return failCount; }
    public void setFailCount(int failCount) { this.failCount = failCount; }
}
