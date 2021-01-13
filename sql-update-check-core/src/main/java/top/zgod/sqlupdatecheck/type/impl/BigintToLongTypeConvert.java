package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.sql.JDBCType;


/**
 * @author ZGOD
 */
public class BigintToLongTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return JDBCType.BIGINT.getName();
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return "long";
    }
}
