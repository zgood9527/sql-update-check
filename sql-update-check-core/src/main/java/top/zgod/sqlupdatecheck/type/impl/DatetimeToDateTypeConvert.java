package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.bean.JdbcType;
import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.util.Date;


/**
 * @author ZGOD
 */
public class DatetimeToDateTypeConvert extends AbstractTypeConvert {


    @Override
    public JdbcType getInitJdbcColumnTypeName() {
        return JdbcType.DATETIME;
    }

    @Override
    public Class<?> getInitJavaColumnTypeName() {
        return Date.class;
    }
}
