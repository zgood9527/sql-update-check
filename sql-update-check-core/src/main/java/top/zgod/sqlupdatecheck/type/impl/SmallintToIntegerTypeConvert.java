package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.bean.JdbcType;
import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

/**
 * @author ZGOD
 */
public class SmallintToIntegerTypeConvert extends AbstractTypeConvert {

    @Override
    public JdbcType getInitJdbcColumnTypeName() {
        return JdbcType.SMALLINT;
    }

    @Override
    public Class<?> getInitJavaColumnTypeName() {
        return Integer.class;
    }
}
