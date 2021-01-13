package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;


/**
 * @author ZGOD
 */
public class DatetimeToStringTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "datetime";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return String.class.getName();
    }
}
