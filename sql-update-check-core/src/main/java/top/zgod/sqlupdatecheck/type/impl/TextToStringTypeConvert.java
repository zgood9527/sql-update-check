package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

/**
 * @author ZGOD
 */
public class TextToStringTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "text";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return String.class.getName();
    }
}
