package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.bean.JdbcType;
import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;



/**
 * @author ZGOD
 */
public class LongvarcharToStringTypeConvert extends AbstractTypeConvert {


    @Override
    public JdbcType getInitJdbcColumnTypeName() {
        return JdbcType.LONGVARCHAR;
    }

    @Override
    public Class<?> getInitJavaColumnTypeName() {
        return String.class;
    }
}
