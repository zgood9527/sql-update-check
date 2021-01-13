package top.zgod.sqlupdatecheck.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZGOD
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColumnBean {

    /**
     * 1from javaBean
     * 2from jdbcBean
     */
    private int beanType;
    private String javaColumnName;
    private String jdbcColumnName;
    private String javaColumnType;
    private String jdbcColumnType;
    private String tableName;
    private String className;

}