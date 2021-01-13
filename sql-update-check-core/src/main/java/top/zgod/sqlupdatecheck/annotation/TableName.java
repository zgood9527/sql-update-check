package top.zgod.sqlupdatecheck.annotation;

import java.lang.annotation.*;

/**
 * @author ZGOD
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface TableName {
    /**
     * 表名
     */
    String value();
}
