package top.zgod.sqlupdatecheck.type.impl;



import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.time.LocalDate;

/**
 * @author ZGOD
 */
public class DateToLocalDateTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "date";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return LocalDate.class.getName();
    }
}
