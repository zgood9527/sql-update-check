package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

/**
 * @author ZGOD
 */
public class IntToIntegerTypeConvert extends AbstractTypeConvert {

    @Override
    public String getInitJdbcColumnTypeName() {
        return "int";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return Integer.class.getName();
    }
}
