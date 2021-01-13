package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.time.LocalDateTime;


/**
 * @author ZGOD
 */
public class TimeStampToLocalDateTimeTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "timestamp";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return LocalDateTime.class.getName();
    }
}
