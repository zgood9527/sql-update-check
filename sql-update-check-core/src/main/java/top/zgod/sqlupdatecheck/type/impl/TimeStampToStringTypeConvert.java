package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;


/**
 * @author ZGOD
 */
public class TimeStampToStringTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "timestamp";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return String.class.getName();
    }
}
