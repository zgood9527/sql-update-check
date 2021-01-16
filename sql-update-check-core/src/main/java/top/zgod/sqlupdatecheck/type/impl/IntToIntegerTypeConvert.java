package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.bean.JdbcType;
import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

/**
 * @author ZGOD
 */
public class IntToIntegerTypeConvert extends AbstractTypeConvert {

    @Override
    public JdbcType getInitJdbcColumnTypeName() {
        return JdbcType.INT;
    }

    @Override
    public Class<?> getInitJavaColumnTypeName() {
        return Integer.class;
    }
}
