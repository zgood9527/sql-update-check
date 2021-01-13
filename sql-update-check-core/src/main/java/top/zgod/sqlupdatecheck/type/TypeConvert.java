package top.zgod.sqlupdatecheck.type;

/**
 * 分别提供获取数据库字段类型名和实体类类型名的方法,提供核心类关联
 * @author ZGOD
 */
public interface TypeConvert {
    /**
     * 获取数据库字段名
     * @return
     */
    String getInitJdbcColumnTypeName();

    /**
     * 获取实体类字段名
     * @return
     */
    String getInitJavaColumnTypeName();
}
