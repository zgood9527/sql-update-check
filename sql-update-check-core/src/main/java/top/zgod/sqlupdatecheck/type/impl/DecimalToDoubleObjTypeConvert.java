package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.sql.JDBCType;

/**
 * @author ZGOD
 */
public class DecimalToDoubleObjTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return JDBCType.DECIMAL.getName();
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return Double.class.getName();
    }
}
