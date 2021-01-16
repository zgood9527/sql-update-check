package top.zgod.sqlupdatecheck.demo.type;

import top.zgod.sqlupdatecheck.bean.JdbcType;
import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;



/**
 * @author ZGOD
 */
public class VarcharToLongTypeConvert extends AbstractTypeConvert {


    @Override
    public JdbcType getInitJdbcColumnTypeName() {
        return JdbcType.VARCHAR;
    }

    @Override
    public Class<?> getInitJavaColumnTypeName() {
        return int.class;
    }
}
