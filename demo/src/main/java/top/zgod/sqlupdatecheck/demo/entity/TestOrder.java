package top.zgod.sqlupdatecheck.demo.entity;

import lombok.Data;
import top.zgod.sqlupdatecheck.annotation.TableName;

/**
 * @author ZGOD
 */
@Data
@TableName("test_order")
public class TestOrder {
    private int id;
    private int orderName;
}
