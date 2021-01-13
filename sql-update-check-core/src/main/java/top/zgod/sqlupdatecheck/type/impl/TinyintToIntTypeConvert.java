package top.zgod.sqlupdatecheck.type.impl;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

/**
 * @author ZGOD
 */
public class TinyintToIntTypeConvert extends AbstractTypeConvert {

    @Override
    public String getInitJdbcColumnTypeName() {
        return "tinyint";
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return "int";
    }
}
