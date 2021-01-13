package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

/**
 * @author ZGOD
 */
public class SmallintToShortTypeConvert extends AbstractTypeConvert {

    @Override
    public String getInitJdbcColumnTypeName() {
        return "smallint";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return Short.class.getName();
    }
}
