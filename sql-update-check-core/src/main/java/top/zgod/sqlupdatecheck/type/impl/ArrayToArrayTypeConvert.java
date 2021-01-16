package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.bean.JdbcType;
import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.lang.reflect.Array;


/**
 * @author ZGOD
 */
public class ArrayToArrayTypeConvert extends AbstractTypeConvert {


    @Override
    public JdbcType getInitJdbcColumnTypeName() {
        return JdbcType.ARRAY;
    }

    @Override
    public Class<?> getInitJavaColumnTypeName() {
        return Array.class;
    }
}
