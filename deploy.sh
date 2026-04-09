#!/bin/bash
# SQL2Entity 部署脚本

# 后台运行服务
nohup java -jar sql2entity-1.0.0.jar --server.port=8080 > app.log 2>&1 &

echo "服务已启动，PID: $!"
echo "日志文件: app.log"
echo "访问地址: http://localhost:8080"
echo "API文档: http://localhost:8080/swagger-ui.html"
