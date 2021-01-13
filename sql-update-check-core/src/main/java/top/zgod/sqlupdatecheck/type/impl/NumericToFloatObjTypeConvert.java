package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

/**
 * @author ZGOD
 */
public class NumericToFloatObjTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "numeric";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return Float.class.getName();
    }
}
