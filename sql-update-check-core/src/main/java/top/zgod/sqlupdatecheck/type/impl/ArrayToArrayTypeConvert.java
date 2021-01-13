package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.lang.reflect.Array;
import java.sql.JDBCType;


/**
 * @author ZGOD
 */
public class ArrayToArrayTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return JDBCType.ARRAY.getName();
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return Array.class.getName();
    }
}
