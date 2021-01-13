package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.math.BigDecimal;

/**
 * @author ZGOD
 */
public class NumericToBigDecimalTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "numeric";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return BigDecimal.class.getName();
    }
}
