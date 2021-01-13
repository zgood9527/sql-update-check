package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.util.Date;


/**
 * @author ZGOD
 */
public class DatetimeToDateTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "datetime";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return Date.class.getName();
    }
}
