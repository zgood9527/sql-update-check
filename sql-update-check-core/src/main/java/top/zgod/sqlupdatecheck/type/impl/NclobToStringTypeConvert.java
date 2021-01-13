package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

/**
 * @author ZGOD
 */
public class NclobToStringTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "nclob";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return String.class.getName();
    }
}
