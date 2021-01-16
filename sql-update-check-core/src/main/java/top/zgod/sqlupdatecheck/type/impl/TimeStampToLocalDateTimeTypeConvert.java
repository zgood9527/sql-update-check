package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.bean.JdbcType;
import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.time.LocalDateTime;


/**
 * @author ZGOD
 */
public class TimeStampToLocalDateTimeTypeConvert extends AbstractTypeConvert {


    @Override
    public JdbcType getInitJdbcColumnTypeName() {
        return JdbcType.TIMESTAMP;
    }

    @Override
    public Class<?> getInitJavaColumnTypeName() {
        return LocalDateTime.class;
    }
}
