package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

/**
 * @author ZGOD
 */
public class SmallintToIntegerTypeConvert extends AbstractTypeConvert {

    @Override
    public String getInitJdbcColumnTypeName() {
        return "smallint";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return Integer.class.getName();
    }
}
