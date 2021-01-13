package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

/**
 * @author ZGOD
 */
public class RealToFloatObjTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return "real";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return Float.class.getName();
    }
}
