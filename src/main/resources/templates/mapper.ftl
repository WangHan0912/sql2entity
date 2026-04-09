<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.mapper.${tableInfo.mapperName}">

    <resultMap id="BaseResultMap" type="${packageName}.${tableInfo.className}">
<#list tableInfo.columns as column>
        <result column="${column.columnName}" property="${column.fieldName}" jdbcType="${column.jdbcType}"/>
</#list>
    </resultMap>

    <sql id="Base_Column_List">
<#list tableInfo.columns as column>${column.columnName}<#sep>,</#sep></#list>
    </sql>

    <select id="selectAll" resultMap="BaseResultMap">
        SELECT <include refid="Base_Column_List"/> FROM ${tableInfo.tableName}
    </select>

    <select id="selectById" resultMap="BaseResultMap">
        SELECT <include refid="Base_Column_List"/> FROM ${tableInfo.tableName}
        WHERE <#list tableInfo.columns as column><#if column.primaryKey>${column.columnName} = <#noparse>#</#noparse>{${column.fieldName}}</#if></#list>
    </select>

    <insert id="insert" parameterType="${packageName}.${tableInfo.className}">
        INSERT INTO ${tableInfo.tableName} (<#list tableInfo.columns as column>${column.columnName}<#sep>,</#sep></#list>)
        VALUES (<#list tableInfo.columns as column><#noparse>#</#noparse>{${column.fieldName}}<#sep>,</#sep></#list>)
    </insert>

    <update id="updateById" parameterType="${packageName}.${tableInfo.className}">
        UPDATE ${tableInfo.tableName}
        <set>
<#list tableInfo.columns as column><#if !column.primaryKey>
            <if test="${column.fieldName} != null">${column.columnName} = <#noparse>#</#noparse>{${column.fieldName}},</if>
</#if></#list>
        </set>
        WHERE <#list tableInfo.columns as column><#if column.primaryKey>${column.columnName} = <#noparse>#</#noparse>{${column.fieldName}}</#if></#list>
    </update>

    <delete id="deleteById">
        DELETE FROM ${tableInfo.tableName}
        WHERE <#list tableInfo.columns as column><#if column.primaryKey>${column.columnName} = <#noparse>#</#noparse>{${column.fieldName}}</#if></#list>
    </delete>

</mapper>
