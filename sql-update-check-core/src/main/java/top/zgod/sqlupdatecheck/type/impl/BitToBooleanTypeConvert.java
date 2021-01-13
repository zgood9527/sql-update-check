package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.sql.JDBCType;

/**
 * @author ZGOD
 */
public class BitToBooleanTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return JDBCType.BIT.getName();
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return "boolean";
    }
}
