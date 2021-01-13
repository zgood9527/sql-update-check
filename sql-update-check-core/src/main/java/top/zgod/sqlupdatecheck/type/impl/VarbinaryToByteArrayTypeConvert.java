package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.sql.JDBCType;

/**
 * @author ZGOD
 */
public class VarbinaryToByteArrayTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return JDBCType.VARBINARY.getName();
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return "byte[]";
    }
}
