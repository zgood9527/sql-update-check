package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.bean.JdbcType;
import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;


/**
 * @author ZGOD
 */
public class BinaryToByteArrayTypeConvert extends AbstractTypeConvert {


    @Override
    public JdbcType getInitJdbcColumnTypeName() {
        return JdbcType.BINARY;
    }

    @Override
    public Class<?> getInitJavaColumnTypeName() {
        return byte[].class;
    }
}
