package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

/**
 * @author ZGOD
 */
public class BlobToByteArrayTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "blob";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return "byte[]";
    }
}
