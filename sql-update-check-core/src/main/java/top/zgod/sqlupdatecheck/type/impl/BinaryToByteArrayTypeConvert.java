package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;


/**
 * @author ZGOD
 */
public class BinaryToByteArrayTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "binary";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return "byte[]";
    }
}
