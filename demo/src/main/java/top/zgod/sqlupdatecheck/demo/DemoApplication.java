package top.zgod.sqlupdatecheck.demo;

import top.zgod.sqlupdatecheck.annotation.EnableSqlUpdateCheck;
import top.zgod.sqlupdatecheck.demo.handler.EmailRemindHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ZGOD
 */
@SpringBootApplication
//基本使用
//@EnableSqlUpdateCheck("top.zgod.sqlupdatecheck.demo.entity")
//增强提醒
@EnableSqlUpdateCheck(
        value = "top.zgod.sqlupdatecheck.demo.entity",
        remindHandler = EmailRemindHandler.class,
        isCheckEntityByDB = true)
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
