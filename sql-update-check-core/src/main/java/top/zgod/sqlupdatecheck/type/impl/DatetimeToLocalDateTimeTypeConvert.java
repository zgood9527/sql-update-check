package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.time.LocalDateTime;


/**
 * @author ZGOD
 */
public class DatetimeToLocalDateTimeTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "datetime";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return LocalDateTime.class.getName();
    }
}
