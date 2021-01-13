package top.zgod.sqlupdatecheck.annotation;

import top.zgod.sqlupdatecheck.handler.JdbcDataHandler;
import top.zgod.sqlupdatecheck.handler.impl.DefaultJdbcDataHandler;
import top.zgod.sqlupdatecheck.handler.impl.DefaultRemindHandler;
import top.zgod.sqlupdatecheck.handler.RemindHandler;
import top.zgod.sqlupdatecheck.SqlUpdateCheck;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ZGOD
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = SqlUpdateCheck.class)
public @interface EnableSqlUpdateCheck {
    /** 实体类所在的包 */
    String[] value() default {};
    /** 实体类所在的包数组 */
    String[] basePackages() default {};
    /** 排除检查的表 */
    String[] excludeTableNames() default {};
    /** 是否检查数据库有的但是没有生成实体类或字段 */
    boolean isCheckEntityByDB() default false;
    /** 是否检查实体类与数据库字段是否符合映射规定(实体类与数据库字段是否匹配) */
    boolean isCheckColumnType() default true;
    /** 检查出现问题是否退出系统 */
    boolean isSystemExit() default true;
    /** 使用TableName标签获取实体类表名，默认使用自带或者mybatis-plus的TableName */
    boolean enableTableName() default true;
    /** 额外数据库实体类扫描映射关系的路径 */
    String[] extraTypeParsePackages() default {};
    /** 处理提醒的实现类 */
    Class<? extends RemindHandler> remindHandler() default DefaultRemindHandler.class;
    /** 获取数据库数据的实现类 */
    Class<? extends JdbcDataHandler> jdbcDataHandler() default DefaultJdbcDataHandler.class;
}