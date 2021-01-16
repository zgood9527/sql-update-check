package top.zgod.sqlupdatecheck.type.impl;



import top.zgod.sqlupdatecheck.bean.JdbcType;
import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;


import java.time.LocalDate;

/**
 * @author ZGOD
 */
public class DateToLocalDateTypeConvert extends AbstractTypeConvert {


    @Override
    public JdbcType getInitJdbcColumnTypeName() {
        return JdbcType.DATE;
    }

    @Override
    public Class<?> getInitJavaColumnTypeName() {
        return LocalDate.class;
    }
}
