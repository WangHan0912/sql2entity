# SQL2Entity API

将SQL建表语句转换为Java Entity类的API服务。

## 功能特性

- ✅ 单个/批量SQL生成Entity
- ✅ 支持MySQL/PostgreSQL/Oracle
- ✅ Lombok注解支持
- ✅ 字段校验注解（@NotNull/@Size/@Email）
- ✅ MyBatis Mapper XML生成
- ✅ Swagger API文档

## API接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/generate` | POST | 生成单个Entity |
| `/api/batch` | POST | 批量生成多个Entity |
| `/api/example` | GET | 获取示例SQL |
| `/api/health` | GET | 健康检查 |
| `/swagger-ui.html` | GET | API文档 |

## 本地运行

```bash
java -jar sql2entity-1.0.0.jar
```

## 请求示例

```bash
curl -X POST http://localhost:8080/api/generate \
  -H "Content-Type: application/json" \
  -d '{
    "sql": "CREATE TABLE tb_user (id BIGINT PRIMARY KEY, name VARCHAR(50))",
    "packageName": "com.example.entity",
    "useLombok": true,
    "generateMapper": true
  }'
```

## Railway部署

1. 将此项目上传到GitHub
2. 登录 [Railway.app](https://railway.app)
3. 点击 "New Project" → "Deploy from GitHub repo"
4. 选择此仓库
5. Railway会自动检测Dockerfile并部署

