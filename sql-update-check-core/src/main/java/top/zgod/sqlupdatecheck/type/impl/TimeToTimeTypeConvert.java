package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.sql.Time;


/**
 * @author ZGOD
 */
public class TimeToTimeTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "time";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return Time.class.getName();
    }
}
