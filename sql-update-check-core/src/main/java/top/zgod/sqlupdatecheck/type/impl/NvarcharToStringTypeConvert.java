package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.sql.JDBCType;

/**
 * @author ZGOD
 */
public class NvarcharToStringTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return JDBCType.NVARCHAR.getName();
    }

    @Override
    public String getInitJavaColumnTypeName() {

        return String.class.getName();
    }
}
