# SQL2Entity API 🚀

> 一键将 SQL 建表语句转换为 Java Entity 类

**在线体验：** https://sql2entity.onrender.com/swagger-ui.html

## ✨ 功能特性

- 🔄 **SQL 转 Entity** - 支持 MySQL / PostgreSQL / Oracle
- 📦 **批量生成** - 一次提交多个表，批量生成
- 🎯 **Lombok 支持** - @Data / @Builder / @NoArgsConstructor 等
- ✅ **校验注解** - @NotNull / @Size / @Email / @Pattern
- 📝 **MyBatis Mapper** - 自动生成 Mapper XML 文件
- 📖 **Swagger 文档** - 在线 API 文档，开箱即用

## 🚀 快速开始

### 在线调用

```bash
curl -X POST https://sql2entity.onrender.com/api/generate \
  -H "Content-Type: application/json" \
  -d '{
    "sql": "CREATE TABLE tb_user (id BIGINT PRIMARY KEY, username VARCHAR(50) NOT NULL COMMENT '\''用户名'\'', email VARCHAR(100))",
    "packageName": "com.example.entity",
    "className": "User",
    "useLombok": true,
    "generateMapper": true
  }'
```

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| sql | String | ✅ | SQL 建表语句 |
| packageName | String | ❌ | 包名，默认 com.example.entity |
| className | String | ❌ | 类名，默认从表名转换 |
| useLombok | Boolean | ❌ | 是否使用 Lombok，默认 false |
| dbType | String | ❌ | 数据库类型：mysql/postgresql/oracle |
| generateValidation | Boolean | ❌ | 是否生成校验注解 |
| generateMapper | Boolean | ❌ | 是否生成 MyBatis Mapper XML |

### 返回示例

```json
{
  "success": true,
  "entityCode": "package com.example.entity;\n\nimport lombok.Data;\n...",
  "mapperXml": "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n...",
  "tableName": "tb_user",
  "className": "User"
}
```

## 📡 API 接口

| 接口 | 方法 | 说明 |
|-----|------|------|
| `/api/generate` | POST | 生成单个 Entity |
| `/api/batch` | POST | 批量生成多个 Entity |
| `/api/example` | GET | 获取示例 SQL |
| `/api/health` | GET | 健康检查 |
| `/swagger-ui.html` | GET | API 文档 |

## 🔧 本地运行

```bash
# 克隆项目
git clone https://github.com/WangHan0912/sql2entity.git
cd sql2entity

# 运行
java -jar sql2entity-1.0.0.jar

# 访问
http://localhost:8080/swagger-ui.html
```

## 📋 使用场景

- 🏗️ 快速生成项目基础代码
- 📊 数据库表结构变更后快速同步 Entity
- 🔧 CI/CD 集成，自动化代码生成
- 📚 学习 JPA 注解和 MyBatis 配置

## 🤝 联系方式

- GitHub Issues: [提交问题](https://github.com/WangHan0912/sql2entity/issues)
- Email: 1576094737@qq.com

---

⭐ 如果觉得有用，欢迎 Star 支持！
