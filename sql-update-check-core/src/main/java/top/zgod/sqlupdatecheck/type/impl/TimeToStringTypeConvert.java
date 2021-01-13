package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;


/**
 * @author ZGOD
 */
public class TimeToStringTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "time";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return String.class.getName();
    }
}
