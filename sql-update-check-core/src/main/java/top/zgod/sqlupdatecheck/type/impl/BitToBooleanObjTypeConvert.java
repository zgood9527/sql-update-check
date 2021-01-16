package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.bean.JdbcType;
import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;



/**
 * @author ZGOD
 */
public class BitToBooleanObjTypeConvert extends AbstractTypeConvert {


    @Override
    public JdbcType getInitJdbcColumnTypeName() {
        return JdbcType.BIT;
    }

    @Override
    public Class<?> getInitJavaColumnTypeName() {
        return Boolean.class;
    }
}
