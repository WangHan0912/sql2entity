package ${packageName};

<#if useLombok>
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
</#if>
<#if generateValidation>
import javax.validation.constraints.*;
</#if>
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity class for table: ${tableInfo.tableName}
 * Database type: ${dbType}
 */
@Entity
@Table(name = "${tableInfo.tableName}")
<#if useLombok>
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
</#if>
public class ${tableInfo.className} <#if !useLombok>implements Serializable</#if> {
<#if !useLombok>
    private static final long serialVersionUID = 1L;
</#if>

<#list tableInfo.columns as column>
<#if column.comment?? && column.comment?has_content>
    /**
     * ${column.comment}
     */
</#if>
<#if generateValidation && !column.nullable && !column.primaryKey>
    @NotNull(message = "${column.columnName}不能为空")
</#if>
<#if generateValidation && column.fieldType == "String" && column.length?? && column.length gt 0>
    @Size(max = ${column.length}, message = "${column.columnName}长度不能超过${column.length}")
</#if>
<#if generateValidation && column.columnName?lower_case?contains("email")>
    @Email(message = "${column.columnName}格式不正确")
</#if>
<#if generateValidation && column.columnName?lower_case?contains("phone") || column.columnName?lower_case?contains("mobile")>
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "${column.columnName}手机号格式不正确")
</#if>
<#if column.primaryKey>
    @Id
    <#if column.autoIncrement || dbType == "postgresql" || dbType == "oracle">
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    </#if>
</#if>
    @Column(name = "${column.columnName}"<#if !column.nullable>, nullable = false</#if><#if column.length?? && column.length gt 0>, length = ${column.length}</#if>)
    private ${column.fieldType} ${column.fieldName};
</#list>
}
