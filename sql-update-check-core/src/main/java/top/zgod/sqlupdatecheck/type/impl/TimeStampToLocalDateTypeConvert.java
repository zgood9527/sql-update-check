package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.time.LocalDate;


/**
 * @author ZGOD
 */
public class TimeStampToLocalDateTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "timestamp";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return LocalDate.class.getName();
    }
}
