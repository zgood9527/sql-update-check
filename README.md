# SqlUpdateCheck
用于上线前或上线中，对实体类是否符合规定和数据库修改情况是否反映到实体类中进行检查，检查后的结果根据配置情况通知或记录后反馈给用户
## 作用
##### 1检查创建表sql是否提交
##### 2检查创建字段或者修改字段sql是否提交
##### 3检查实体类是否创建
##### 4检查实体类与数据库字段是否匹配

如不匹配控制台会输出如下信息,通过配置可以进行邮件,短信等通知给用户
```
2021-01-14 11:40:42.689 ERROR 20812 --- [           main] top.zgod.sqlupdatecheck.SqlUpdateCheck   : [字段创建sql未提交异常]该test_user表的version字段没有创建，请提交创建字段sql
2021-01-14 11:40:42.689 ERROR 20812 --- [           main] top.zgod.sqlupdatecheck.SqlUpdateCheck   : [表创建sql未提交异常]该test_order表没有创建，请提交创建表sql
2021-01-14 11:40:42.689 ERROR 20812 --- [           main] top.zgod.sqlupdatecheck.SqlUpdateCheck   : [实体类未创建异常]该TestGood实体类没有创建，请根据数据库表创建好相应实体类
2021-01-14 11:40:42.689 ERROR 20812 --- [           main] top.zgod.sqlupdatecheck.SqlUpdateCheck   : [实体类字段未创建异常]该TestUser实体类的password字段没有创建，请根据数据库表创建好相应实体类字段
2021-01-14 11:40:42.690 ERROR 20812 --- [           main] top.zgod.sqlupdatecheck.SqlUpdateCheck   : [字段不匹配异常]该TestUser实体类与test_user表的字段：userName(int)与user_name(varchar),对应关系不匹配，请提交sql或检查好对应关系
2021-01-14 11:40:42.690 ERROR 20812 --- [           main] top.zgod.sqlupdatecheck.SqlUpdateCheck   : [字段不匹配异常]该TestShop实体类与test_shop表的字段：name(java.lang.Boolean)与name(varchar),对应关系不匹配，请提交sql或检查好对应关系

```
## 原理
程序通过扫描包获取实体类对应的表名和字段信息,通过访问数据库获取相应的表和表字段信息,通过映射关系进行匹配,对不符合的信息进行相应处理
## 集成
maven方式
```
<dependency>
  <groupId>top.zgod.sqlupdatecheck</groupId>
  <artifactId>sql-update-check-core</artifactId>
  <version>1.0.0.RELEASE</version>
</dependency>
```
## 使用
加上@EnableSqlUpdateCheck注解,传入扫描的实体类包名就可以使用了,注意:实体类需要有带有表名的@TableName("table_name")注解(实体类可以继承父类,父类不需要注解).如果想要没有注解的形式,则传入参数enableTableName = false让程序自动处理(驼峰转成'_''的命名方式)
```
    @SpringBootApplication
    @EnableSqlUpdateCheck("top.zgod.sqlupdatecheck.demo.entity")
    public class DemoApplication {
        public static void main(String[] args) {
            SpringApplication.run(DemoApplication.class, args);
        }
    }
```
最佳配置如下,其中EmailRemindHandler为用户自定义实现RemindHandler后处理提醒消息的实现类,您可以邮件或者短信通知自己
```
@EnableSqlUpdateCheck(
        value = "top.zgod.sqlupdatecheck.demo.entity",
        remindHandler = EmailRemindHandler.class,
        isCheckEntityByDB = true)

@Component
public class EmailRemindHandler implements RemindHandler {
    @Override
    public void remindContainer(HashMap<String, List<ColumnBean>> withoutTableOrColumnMap, HashMap<String, List<ColumnBean>> withoutEntityOrPropertyMap, HashMap<String, List<ColumnBean>> columnTypeDifferentMap) {

    }

    @Override
    public void remindMessage(List<String> remindStrList) {
        System.out.println("发送提醒消息:" + remindStrList.toString());
    }
}
```
@EnableSqlUpdateCheck中的扩展属性解释如下
```
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
```
## 扩展映射类
当您想要数据库字段与实体类字段特殊的映射时(映射关系可以通过检查),继承AbstractTypeConvert并重写以下方法,分别表示数据库与实体类的映射,并在@EnableSqlUpdateCheck()中的extraTypeParsePackages字段里传入扫描路径
```
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

```
## 配置开关闭功能
通过配置文件application.proprerties中修改字段控制是否开启此功能,可以不配置,默认开启
```
sql-update-check.scan.enabled = false
```