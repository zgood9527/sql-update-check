package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.bean.JdbcType;
import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;



/**
 * @author ZGOD
 */
public class VarbinaryToByteArrayTypeConvert extends AbstractTypeConvert {


    @Override
    public JdbcType getInitJdbcColumnTypeName() {
        return JdbcType.VARBINARY;
    }

    @Override
    public Class<?> getInitJavaColumnTypeName() {
        return byte[].class;
    }
}
