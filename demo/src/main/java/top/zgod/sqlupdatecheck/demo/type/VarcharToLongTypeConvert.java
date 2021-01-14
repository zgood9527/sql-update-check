package top.zgod.sqlupdatecheck.demo.type;

import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;

import java.sql.JDBCType;


/**
 * @author ZGOD
 */
public class VarcharToLongTypeConvert extends AbstractTypeConvert {


    @Override
    public String getInitJdbcColumnTypeName() {
        return JDBCType.VARCHAR.getName();
    }

    @Override
    public String getInitJavaColumnTypeName() {
        return "int";
    }
}
