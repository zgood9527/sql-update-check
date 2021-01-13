package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.sql.JDBCType;


/**
 * @author ZGOD
 */
public class LongnvarcharToStringTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return JDBCType.LONGNVARCHAR.getName();
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return String.class.getName();
    }
}
