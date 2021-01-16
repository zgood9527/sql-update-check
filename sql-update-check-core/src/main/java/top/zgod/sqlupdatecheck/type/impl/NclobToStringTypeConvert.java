package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.bean.JdbcType;
import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

/**
 * @author ZGOD
 */
public class NclobToStringTypeConvert extends AbstractTypeConvert {


    @Override
    public JdbcType getInitJdbcColumnTypeName() {
        return JdbcType.NCLOB;
    }

    @Override
    public Class<?> getInitJavaColumnTypeName() {
        return String.class;
    }
}
