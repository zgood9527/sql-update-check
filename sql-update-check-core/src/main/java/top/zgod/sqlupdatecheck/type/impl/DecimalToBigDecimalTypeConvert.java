package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.bean.JdbcType;
import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.math.BigDecimal;


/**
 * @author ZGOD
 */
public class DecimalToBigDecimalTypeConvert extends AbstractTypeConvert {


    @Override
    public JdbcType getInitJdbcColumnTypeName() {
        return JdbcType.DECIMAL;
    }

    @Override
    public Class<?> getInitJavaColumnTypeName() {
        return BigDecimal.class;
    }

}
