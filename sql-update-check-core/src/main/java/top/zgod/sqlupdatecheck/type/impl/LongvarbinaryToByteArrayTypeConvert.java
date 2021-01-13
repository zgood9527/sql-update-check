package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.sql.JDBCType;

/**
 * @author ZGOD
 */
public class LongvarbinaryToByteArrayTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return JDBCType.LONGVARBINARY.getName();
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return "byte[]";
    }
}
