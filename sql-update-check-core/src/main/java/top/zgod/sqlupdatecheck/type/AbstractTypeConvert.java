package top.zgod.sqlupdatecheck.type;

import org.springframework.util.StringUtils;

/**
 * 带有检查功能
 * @author ZGOD
 */
public abstract class AbstractTypeConvert implements TypeConvert {

    public void checkJdbcColumnTypeName() {
        String jdbcColumnType = getInitJdbcColumnTypeName();
        if (StringUtils.isEmpty(jdbcColumnType)) {
            throw new NullPointerException(this.getClass().getName() +":返回值不能为空或者空字符串!");
        }
    }
    public void checkJavaColumnTypeName() {
        String javaColumnType = getInitJavaColumnTypeName();
        if (StringUtils.isEmpty(javaColumnType)) {
            throw new NullPointerException(this.getClass().getName() +":返回值不能为空或者空字符串!");
        }
    }

}
