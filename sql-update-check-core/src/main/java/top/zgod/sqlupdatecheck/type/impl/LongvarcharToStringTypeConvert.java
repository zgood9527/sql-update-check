package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.sql.JDBCType;

/**
 * @author ZGOD
 */
public class LongvarcharToStringTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return JDBCType.LONGVARCHAR.getName();
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return String.class.getName();
    }
}
