package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

/**
 * @author ZGOD
 */
public class DoubleToDoubleTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "double";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return "double";
    }
}
