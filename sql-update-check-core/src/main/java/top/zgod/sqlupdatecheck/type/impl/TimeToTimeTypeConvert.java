package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.bean.JdbcType;
import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.sql.Time;


/**
 * @author ZGOD
 */
public class TimeToTimeTypeConvert extends AbstractTypeConvert {


    @Override
    public JdbcType getInitJdbcColumnTypeName() {
        return JdbcType.TIME;
    }

    @Override
    public Class<?> getInitJavaColumnTypeName() {
        return Time.class;
    }
}
