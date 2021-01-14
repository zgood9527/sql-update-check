package top.zgod.sqlupdatecheck.demo.entity;

import lombok.Data;
import top.zgod.sqlupdatecheck.annotation.TableName;

/**
 * @author ZGOD
 */
@Data
@TableName("test_user")
public class TestUser {
    private int id;
    private int userName;
    private String version;
}
