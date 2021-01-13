package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.math.BigDecimal;
import java.sql.JDBCType;

/**
 * @author ZGOD
 */
public class DecimalToBigDecimalTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return JDBCType.DECIMAL.getName();
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return BigDecimal.class.getName();
    }

}
