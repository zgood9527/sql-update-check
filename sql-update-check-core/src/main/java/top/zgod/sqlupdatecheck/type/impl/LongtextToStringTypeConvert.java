package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;


/**
 * @author ZGOD
 */
public class LongtextToStringTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "longtext";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return String.class.getName();
    }
}
