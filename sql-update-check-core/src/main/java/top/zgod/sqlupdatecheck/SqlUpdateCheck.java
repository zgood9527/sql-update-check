package top.zgod.sqlupdatecheck;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.zgod.sqlupdatecheck.annotation.EnableSqlUpdateCheck;
import top.zgod.sqlupdatecheck.annotation.TableName;
import top.zgod.sqlupdatecheck.bean.ColumnBean;
import top.zgod.sqlupdatecheck.config.SqlUpdateCheckConfig;
import top.zgod.sqlupdatecheck.handler.JdbcDataHandler;
import top.zgod.sqlupdatecheck.handler.RemindHandler;
import top.zgod.sqlupdatecheck.type.TypeConvert;
import top.zgod.sqlupdatecheck.type.AbstractTypeConvert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * 扫描检查核心类
 * @author ZGOD
 */
@Slf4j
@Data
public class SqlUpdateCheck implements ImportBeanDefinitionRegistrar, ApplicationRunner {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SqlUpdateCheckConfig sqlUpdateCheckConfig;
    /**
     * 是否监测出问题
     */
    private boolean hasError;
    /**
     * 类型集合
     */
    Map<String, List<TypeConvert>> typeParseMap;
    private static final int BEAN_TYPE_JAVA = 1;
    private static final int BEAN_TYPE_JDBC = 2;

    /**
     * 系统是否退出
     */
    private Boolean isSystemExit;
    /**
     * 排除检查的表
     */
    private List<String> excludeTableNames;
    /**
     * 是否检查实体类和相关字段对于数据库没有创建
     */
    private Boolean isCheckEntityByDataBase;
    /**
     * 是否检查实体类与数据库字段是否符合映射规定
     */
    private Boolean isCheckColumnType;
    /**
     * 扫描的包
     */
    private List<String> basePackages;
    /**
     * 额外数据库实体类扫描映射关系的路径
     */
    private List<String> extraTypeParsePackages;
    /**
     * 处理提醒的实现类
     */
    private Class<? extends RemindHandler> remindHandler;
    /**
     * 获取数据库数据的实现类
     */
    private Class<? extends JdbcDataHandler> jdbcDataHandler;
    /**
     * 使用TableName标签获取实体类表名,默认使用自带或者mybatis-plus的TableName
     */
    private Boolean enableTableName;

    private static final String REMIND_STR1 = "[表创建sql未提交异常]该{}表没有创建，请提交创建表sql";
    private static final String REMIND_STR2 = "[字段创建sql未提交异常]该{}表的{}字段没有创建，请提交创建字段sql";
    private static final String REMIND_STR3 = "[实体类未创建异常]该{}实体类没有创建，请根据数据库表创建好相应实体类";
    private static final String REMIND_STR4 = "[实体类字段未创建异常]该{}实体类的{}字段没有创建，请根据数据库表创建好相应实体类字段";
    private static final String REMIND_STR5 = "[字段不匹配异常]该{}实体类与{}表的字段：{}对应关系不匹配，请提交sql或检查好对应关系";

    private List<String> remindStrList = new ArrayList<>();

    private SqlUpdateCheck(){

    }

    /**
     * @param importingClassMetadata
     * @param registry
     */
    @SneakyThrows
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes mapperScanAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(EnableSqlUpdateCheck.class.getName()));
        if (mapperScanAttrs != null) {
            List<String> basePackages = new ArrayList<>();
            List<String> excludeTableNames = new ArrayList<>();
            List<String> extraTypeParsePackages = new ArrayList<>();
            basePackages.addAll(
                    Arrays.stream(mapperScanAttrs.getStringArray("value"))
                            .filter(StringUtils::hasText)
                            .collect(Collectors.toList()));
            basePackages.addAll(
                    Arrays.stream(mapperScanAttrs.getStringArray("basePackages"))
                            .filter(StringUtils::hasText)
                            .collect(Collectors.toList()));
            extraTypeParsePackages.addAll(
                    Arrays.stream(mapperScanAttrs.getStringArray("extraTypeParsePackages"))
                            .filter(StringUtils::hasText)
                            .collect(Collectors.toList()));
            if (CollectionUtils.isEmpty(basePackages)) {
                throw new NullPointerException("basePackages must not be null!");
            }
            excludeTableNames.addAll(
                    Arrays.stream(mapperScanAttrs.getStringArray("excludeTableNames"))
                            .filter(StringUtils::hasText)
                            .collect(Collectors.toList()));
            Boolean isCheckEntityByDataBase = mapperScanAttrs.getBoolean("isCheckEntityByDB");
            Boolean isCheckColumnType = mapperScanAttrs.getBoolean("isCheckColumnType");
            Boolean isSystemExit = mapperScanAttrs.getBoolean("isSystemExit");
            Boolean enableTableName = mapperScanAttrs.getBoolean("enableTableName");
            Class<? extends RemindHandler> remindHandler = mapperScanAttrs.getClass("remindHandler");
            Class<? extends JdbcDataHandler> jdbcDataHandler = mapperScanAttrs.getClass("jdbcDataHandler");

            //封装BeanDefinition
            BeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClassName(SqlUpdateCheck.class.getName());
            MutablePropertyValues values = beanDefinition.getPropertyValues();
            values.addPropertyValue("basePackages", basePackages);
            values.addPropertyValue("excludeTableNames", excludeTableNames);
            values.addPropertyValue("isCheckEntityByDataBase", isCheckEntityByDataBase);
            values.addPropertyValue("isCheckColumnType", isCheckColumnType);
            values.addPropertyValue("isSystemExit", isSystemExit);
            values.addPropertyValue("enableTableName", enableTableName);
            values.addPropertyValue("extraTypeParsePackages", extraTypeParsePackages);
            values.addPropertyValue("remindHandler", remindHandler);
            values.addPropertyValue("jdbcDataHandler", jdbcDataHandler);
            //注册bean
            registry.registerBeanDefinition("sqlUpdateCheck", beanDefinition);
        }

    }

    /**
     * 扫描所有父类是AbstractTypeConvert的类并实例化
     *
     * @return
     */
    public Map<String, List<TypeConvert>> registerTypeParseMap() throws IllegalAccessException, InstantiationException {
        String name = AbstractTypeConvert.class.getPackage().getName();
        List<Class<?>> classes = getClasses(name + ".impl", 2);
        if (!CollectionUtils.isEmpty(extraTypeParsePackages)) {
            for (String extraTypeParsePackage : extraTypeParsePackages) {
                classes.addAll(getClasses(extraTypeParsePackage, 2));
            }
        }
        ArrayList<TypeConvert> typePars = new ArrayList<>();
        for (Class<?> aClass : classes) {
            AbstractTypeConvert abstractTypeConvert = (AbstractTypeConvert) aClass.newInstance();
            typePars.add(abstractTypeConvert);
        }
        //相同数据库类型为一个键，java类型存到集合中
        return typePars
                .stream().collect(Collectors.groupingBy(typeConvert -> typeConvert.getInitJdbcColumnTypeName().getName()));

    }

    public void check() throws InstantiationException, IllegalAccessException {
        Boolean enabledScan = sqlUpdateCheckConfig.getEnabledScan();
        if (!enabledScan) {
            return;
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        typeParseMap = registerTypeParseMap();
        HashMap<String, List<ColumnBean>> dbBeanMap = getJdbcBeanMap();
        HashMap<String, List<ColumnBean>> entityBeanMap = getEntityBeanMap();
        //创建收集列名相同但是类型不符合规定的集合（字段类型不匹配）
        HashMap<String, List<ColumnBean>> columnTypeDifferentMap = new HashMap<>(8);
        //获取实体类对于数据库缺少和不匹配的表和字段集合（没有表或者字段）
        HashMap<String, List<ColumnBean>> withoutTableOrColumnMap = getVacancyBeanMap(dbBeanMap, entityBeanMap, columnTypeDifferentMap);
        //提醒实体类对于数据库缺少和不匹配的表和字段集合（没有实体类或者字段）
        remind(true, withoutTableOrColumnMap, REMIND_STR1, REMIND_STR2);
        HashMap<String, List<ColumnBean>> withoutEntityOrPropertyMap = new HashMap<>(8);
        if (isCheckEntityByDataBase) {
            //获取数据库对于实体类缺少和不匹配的表和字段集合
            withoutEntityOrPropertyMap = getVacancyBeanMap(entityBeanMap, dbBeanMap, columnTypeDifferentMap);
            //提醒数据库对于实体类缺少和不匹配的表和字段集合
            remind(false, withoutEntityOrPropertyMap, REMIND_STR3, REMIND_STR4);
        }
        //提醒实体类与数据库字段类型不匹配
        remindTypeNotFit(columnTypeDifferentMap);
        stopWatch.stop();
        log.info("SqlUpdateCheck扫描时间毫秒值：{}", stopWatch.getTotalTimeMillis());
        //有问题就退出系统不让开启
        if (hasError) {
            log.error("监测到您的数据库实体类和数据库不匹配，请及时处理！");
            notifyUser(withoutTableOrColumnMap, withoutEntityOrPropertyMap, columnTypeDifferentMap);
            if (isSystemExit) {
                log.error("即将关闭系统。。。。。。。。。");
                System.exit(-1);
            }
        }
    }

    /**
     * 提醒实体类与数据库字段类型不匹配
     *
     * @param columnTypeDifferentMap
     */
    private void remindTypeNotFit(HashMap<String, List<ColumnBean>> columnTypeDifferentMap) {
        if (!columnTypeDifferentMap.isEmpty()) {
            hasError = true;
            columnTypeDifferentMap.forEach((tableName, columnBeans) -> {
                StringBuilder reminds = new StringBuilder();
                String className = "";
                for (ColumnBean columnBean : columnBeans) {
                    reminds.append(columnBean.getJavaColumnName())
                            .append("(")
                            .append(columnBean.getJavaColumnType())
                            .append(")")
                            .append("与")
                            .append(columnBean.getJdbcColumnName())
                            .append("(")
                            .append(columnBean.getJdbcColumnType())
                            .append(")")
                            .append(",");
                    className = columnBean.getClassName();
                }
                String replace1 = REMIND_STR5.replaceFirst("\\{}", className);
                String replace2 = replace1.replaceFirst("\\{}", tableName);
                String replace3 = replace2.replace("{}", reminds.toString());
                remindStrList.add(replace3);
                log.error(replace3);
            });
        }
    }

    private void notifyUser(HashMap<String, List<ColumnBean>> withoutTableOrColumnMap,
                            HashMap<String, List<ColumnBean>> withoutEntityOrPropertyMap,
                            HashMap<String, List<ColumnBean>> columnTypeDifferentMap) {
        ObjectProvider<? extends RemindHandler> beanProvider = applicationContext.getBeanProvider(remindHandler);
        RemindHandler remindHandlerBean = beanProvider.getIfAvailable();
        if (remindHandlerBean == null) {
            String name = remindHandler.getName();
            throw new RuntimeException("提醒类错误！请将名称为" + name + "的该类添加上@Component或放到spring容器中!");
        }
        remindHandlerBean.remindContainer(withoutTableOrColumnMap, withoutEntityOrPropertyMap, columnTypeDifferentMap);
        remindHandlerBean.remindMessage(remindStrList);
    }

    /**
     * 1获取实体类对于数据库缺少和不匹配的表和字段集合,beanMap1:dbBeanMap,beanMap2:entityBeanMap
     * 2获取数据库对于实体类缺少和不匹配的表和字段集合,beanMap1:entityBeanMap,beanMap2:dbBeanMap
     *
     * @param beanMap1
     * @param beanMap2
     * @return
     */
    private HashMap<String, List<ColumnBean>> getVacancyBeanMap(HashMap<String, List<ColumnBean>> beanMap1, HashMap<String, List<ColumnBean>> beanMap2, HashMap<String, List<ColumnBean>> columnTypeDifferentMap) {
        HashMap<String, List<ColumnBean>> vacancyEntityBeanMap = new HashMap<>(8);
        beanMap2.forEach((tableName1, columnBeans1) -> {
            AtomicBoolean hasTable = new AtomicBoolean(false);
            beanMap1.forEach((tableName2, columnBeans2) -> {
                //有该表则比较每个表的字段
                if (tableName1.equals(tableName2)) {
                    hasTable.set(true);
                    for (ColumnBean columnBean1 : columnBeans1) {
                        boolean hasColumn = false;
                        String columnName1 = columnBean1.getJavaColumnName();

                        for (ColumnBean columnBean2 : columnBeans2) {
                            String columnName2 = columnBean2.getJavaColumnName();
                            //只要列名匹配到了就结束
                            if (columnName1.equals(columnName2)) {
                                hasColumn = true;
                                //匹配类型,getVacancyBeanMap调用两次但只取一次匹配,beanMap1:dbBeanMap,beanMap2:entityBeanMap
                                matchColumnType(columnTypeDifferentMap, tableName1, columnBean1, columnBean2);
                                break;
                            }
                        }
                        //1,如果最外层循环的是实体类map,说明：实体类中该表的字段没有存在数据库中，数据库没更新
                        //2,如果最外层循环的是数据库map,说明：数据库中该表的字段没有存在实体类中，实体类没更新
                        if (!hasColumn) {
                            ColumnBean targetColumnBean1 = new ColumnBean();
                            BeanUtils.copyProperties(columnBean1, targetColumnBean1);
                            if (vacancyEntityBeanMap.containsKey(tableName1)) {
                                List<ColumnBean> columnBeans = vacancyEntityBeanMap.get(tableName1);
                                columnBeans.add(targetColumnBean1);
                            } else {
                                List<ColumnBean> columnBeans = new ArrayList<>();
                                columnBeans.add(targetColumnBean1);
                                vacancyEntityBeanMap.put(tableName1, columnBeans);
                            }

                        }
                    }
                }
            });
            //1,如果最外层循环的是实体类map,说明：实体类中该表没有存在于数据库中，数据库没更新
            //2,如果最外层循环的是数据库map,说明：数据库中该表没有存在于实体类中，实体类没更新
            if (!hasTable.get()) {
                vacancyEntityBeanMap.put(tableName1, new ArrayList<>());
            }
        });
        return vacancyEntityBeanMap;
    }

    /**
     * 匹配类型，将不符合的放到集合中
     *
     * @param columnTypeDifferentMap
     * @param tableName1
     * @param columnBean1
     * @param columnBean2
     */
    private void matchColumnType(HashMap<String, List<ColumnBean>> columnTypeDifferentMap, String tableName1, ColumnBean columnBean1, ColumnBean columnBean2) {
        if (isCheckColumnType) {
            int beanType1 = columnBean1.getBeanType();
            int beanType2 = columnBean2.getBeanType();
            if (beanType1 == BEAN_TYPE_JAVA && beanType2 == BEAN_TYPE_JDBC) {
                String javaColumnType = columnBean1.getJavaColumnType();
                String jdbcColumnType = columnBean2.getJdbcColumnType();
                //判断java字段的类型和数据库的类型是否符合规定，不符合加入到集合中
                boolean notFit = compareFit(javaColumnType, jdbcColumnType);
                if (notFit) {
                    ColumnBean columnBean = ColumnBean.builder()
                            .className(columnBean1.getClassName())
                            .tableName(columnBean1.getTableName())
                            .javaColumnType(columnBean1.getJavaColumnType())
                            .javaColumnName(columnBean1.getJavaColumnName())
                            .jdbcColumnType(columnBean2.getJdbcColumnType())
                            .jdbcColumnName(columnBean2.getJdbcColumnName())
                            .build();
                    if (columnTypeDifferentMap.containsKey(tableName1)) {
                        List<ColumnBean> columnBeans = columnTypeDifferentMap.get(tableName1);
                        columnBeans.add(columnBean);
                    } else {
                        List<ColumnBean> columnBeans = new ArrayList<>();
                        columnBeans.add(columnBean);
                        columnTypeDifferentMap.put(tableName1, columnBeans);
                    }
                }
            }
        }
    }

    /**
     * 比较jdbc类型是否符合java类型
     *
     * @param javaColumnType
     * @param jdbcColumnType
     */
    private boolean compareFit(String javaColumnType, String jdbcColumnType) {
        AtomicBoolean notFit = new AtomicBoolean(true);
        typeParseMap.forEach((jdbcTypeName, typeParses) -> {
            if (!StringUtils.isEmpty(jdbcColumnType) && jdbcColumnType.equals(jdbcTypeName.toLowerCase())) {
                for (TypeConvert typePars : typeParses) {
                    String initJavaColumnType = typePars.getInitJavaColumnTypeName().getName();
                    if (javaColumnType.equals(initJavaColumnType)) {
                        notFit.set(false);
                        break;
                    }
                }

            }
        });
        return notFit.get();
    }

    /**
     * 扫描包信息封装成map
     *
     * @return
     */
    private HashMap<String, List<ColumnBean>> getEntityBeanMap() {
        HashMap<String, List<ColumnBean>> columnBeanMap = new HashMap<>(32);
        for (String basePackage : basePackages) {
            List<Class<?>> typesAnnotatedWith = getClasses(basePackage, 1);
            //扫描包获取包下所有带TableName的class
            for (Class<?> aClass : typesAnnotatedWith) {
                List<ColumnBean> columnBeans = new ArrayList<>();
                ColumnBean columnBean = getColumnBean(aClass);
                String tableName = columnBean.getTableName();
                String className = columnBean.getClassName();
                //解析父类的属性
                List<Class<?>> superClasses = getSuperClass(aClass);
                for (Class<?> superClass : superClasses) {
                    getProperty(columnBeans, tableName, className, superClass);
                }
                //解析本类的属性
                getProperty(columnBeans, tableName, className, aClass);
                columnBeanMap.put(tableName, columnBeans);
            }
        }
        return columnBeanMap;
    }

    /**
     * 获取数据库所有表信息封装成map
     *
     * @return
     */
    private HashMap<String, List<ColumnBean>> getJdbcBeanMap() {
        ObjectProvider<? extends JdbcDataHandler> beanProvider = applicationContext.getBeanProvider(jdbcDataHandler);
        JdbcDataHandler jdbcDataHandlerBean = beanProvider.getIfAvailable();
        if (jdbcDataHandlerBean == null) {
            String name = remindHandler.getName();
            throw new RuntimeException("数据库数据类错误！请将名称为" + name + "的该类添加上@Component或放到spring容器中,或者使用默认实现类!");
        }
        List<Map<String, Object>> dbInfo = jdbcDataHandlerBean.getJdbcData();
        if (dbInfo.isEmpty()) {
            throw new RuntimeException("获取数据库数据为空！");
        }
        HashMap<String, List<ColumnBean>> dbColumnBeanMap = new HashMap<>(32);
        sqlFor:
        for (Map<String, Object> stringObjectMap : dbInfo) {
            String tableName = stringObjectMap.get("TABLE_NAME").toString();
            String columnName = stringObjectMap.get("COLUMN_NAME").toString();
            String dataType = stringObjectMap.get("DATA_TYPE").toString();

            //排除指定的表
            for (String excludeTableName : excludeTableNames) {
                if (excludeTableName.equals(tableName)) {
                    continue sqlFor;
                }
            }
            ColumnBean columnBean = ColumnBean.builder()
                    .beanType(2)
                    .javaColumnName(underlineToHump(columnName))
                    .jdbcColumnType(dataType)
                    .tableName(tableName)
                    .jdbcColumnName(columnName)
                    .build();
            if (!dbColumnBeanMap.containsKey(tableName)) {
                List<ColumnBean> columnBeans = new ArrayList<>();
                columnBeans.add(columnBean);
                dbColumnBeanMap.put(tableName, columnBeans);
            } else {
                List<ColumnBean> columnBeans = dbColumnBeanMap.get(tableName);
                columnBeans.add(columnBean);
            }
        }
        return dbColumnBeanMap;
    }

    /**
     * 封装属性信息
     *
     * @param columnBeans
     * @param tableName
     * @param className
     * @param superClass
     */
    private void getProperty(List<ColumnBean> columnBeans, String tableName, String className, Class<?> superClass) {
        Field[] declaredFields = superClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String columnName = declaredField.getName();
            if ("serialVersionUID".equals(columnName)) {
                continue;
            }
            Class<?> type = declaredField.getType();
            String columnType = type.getName();
            //添加前判断有没有重复名，有要删除
            for (int i = columnBeans.size() - 1; i > -1; i--) {
                ColumnBean columnBean = columnBeans.get(i);
                if (columnBean.getJavaColumnName().equals(columnName)) {
                    columnBeans.remove(i);
                }
            }
            columnBeans.add(
                    ColumnBean.builder()
                            .beanType(1)
                            .javaColumnName(columnName)
                            .javaColumnType(columnType)
                            .tableName(tableName)
                            .className(className).build());
        }
    }

    /**
     * 将下划线风格替换为驼峰风格
     *
     * @param inputString
     * @return
     */
    private String underlineToHump(String inputString) {
        StringBuilder sb = new StringBuilder();

        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if (c == '_') {
                if (sb.length() > 0) {
                    nextUpperCase = true;
                }
            } else {
                if (nextUpperCase) {
                    sb.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取类的TableName信息
     *
     * @param clazz
     * @return
     */
    public ColumnBean getColumnBean(Class<?> clazz) {
        //如果没有指定的标注表名的注解则获取类名转换成‘_’模式
        String tableName;
        String className = clazz.getSimpleName();
        if (enableTableName){
            com.baomidou.mybatisplus.annotation.TableName annotation1 = clazz.getAnnotation(com.baomidou.mybatisplus.annotation.TableName.class);
            if (annotation1 !=null){
                tableName = annotation1.value();
            }else{
                TableName annotation2 = clazz.getAnnotation(TableName.class);
                tableName = annotation2.value();
            }
        }else{
            StringBuilder stringBuilder = new StringBuilder();
            char[] ch = className.toCharArray();
            // 得到大写字母
            for(int i = 0; i < ch.length ; i++){
                if(ch[i] >= 'A' && ch[i] <= 'Z'){
                    String ch1 = String.valueOf(ch[i]).toLowerCase();
                    if (i == 0){
                        stringBuilder.append(ch1);
                    }else{
                        stringBuilder.append("_").append(ch1);
                    }
                }else{
                    stringBuilder.append(ch[i]);
                }
            }
            tableName = stringBuilder.toString();
        }
        return ColumnBean.builder()
                .beanType(1)
                .tableName(tableName)
                .className(className)
                .build();
    }

    /**
     * 获取这个类的所有父类
     *
     * @param clazz
     * @return
     */
    public List<Class<?>> getSuperClass(Class<?> clazz) {
        List<Class<?>> clazzs = new ArrayList<>();
        Class<?> suCl = clazz.getSuperclass();
        while (suCl != null) {
            clazzs.add(suCl);
            suCl = suCl.getSuperclass();
        }
        return clazzs;
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String upperCaseFirst(String str) {
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    /**
     * 提醒
     *
     * @param vacancyEntityBeanMap
     * @param remind1
     * @param remind2
     */
    private void remind(boolean isCheckJdbc, HashMap<String, List<ColumnBean>> vacancyEntityBeanMap, String remind1, String remind2) {
        vacancyEntityBeanMap.forEach((tableName, columnBeans) -> {
            String tableOrEntityName = isCheckJdbc ? tableName : upperCaseFirst(underlineToHump(tableName));
            if (CollectionUtils.isEmpty(columnBeans)) {
                hasError = true;
                String replace1 = remind1.replace("{}", tableOrEntityName);
                remindStrList.add(replace1);
                log.error(replace1);
            } else {
                StringBuilder columnNames = new StringBuilder();
                for (ColumnBean columnBean : columnBeans) {
                    String columnName = columnBean.getJavaColumnName();
                    columnNames.append(columnName).append(",");
                    hasError = true;
                }
                String replace2 = (remind2.replaceFirst("\\{}", tableOrEntityName)).replace("{}", columnNames.substring(0, columnNames.length() - 1));
                remindStrList.add(replace2);
                log.error(replace2);
            }
        });
    }

    @Override
    public void run(ApplicationArguments args) throws IllegalAccessException, InstantiationException {
        check();
    }

    /**
     * 条件扫描获取类
     *
     * @param packageName 要扫描的包名
     * @param type        1 只要含有TableName（如果） 2只要父类是TypeParseAbstract
     * @return
     */
    public List<Class<?>> getClasses(String packageName, int type) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        //是否循环迭代
        boolean recursive = true;
        //获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        //定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            //循环迭代下去
            while (dirs.hasMoreElements()) {
                //获取下一个元素
                URL url = dirs.nextElement();
                //得到协议的名称
                String protocol = url.getProtocol();
                //如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    //获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    //以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes, type);
                } else if ("jar".equals(protocol)) {
                    //如果是jar包文件
                    //定义一个JarFile
                    JarFile jar;
                    try {
                        //获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        //从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        //同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            //获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            //如果是以/开头的
                            if (name.charAt(0) == '/') {
                                //获取后面的字符串
                                name = name.substring(1);
                            }
                            //如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                //如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    //获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                //如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    //如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        //去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            //添加到classes
                                            getTargetClasses(packageName, classes, className, type);
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    public void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes, int type) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        //如果存在 就获取包下的所有文件 包括目录
        //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
        File[] dirFiles = dir.listFiles(file -> (recursive && file.isDirectory()) || (file.getName().endsWith(".class")));
        //循环所有文件
        assert dirFiles != null;
        for (File file : dirFiles) {
            //如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
                        file.getAbsolutePath(),
                        recursive,
                        classes,
                        type);
            } else {
                //如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    //添加到集合中去
                    getTargetClasses(packageName, classes, className, type);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getTargetClasses(String packageName, List<Class<?>> classes, String className, int type) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(packageName + '.' + className);
        if (type == BEAN_TYPE_JAVA) {
            if (enableTableName){
                com.baomidou.mybatisplus.annotation.TableName annotation = aClass.getAnnotation(com.baomidou.mybatisplus.annotation.TableName.class);
                if (annotation != null) {
                    classes.add(aClass);
                }else{
                    TableName annotation1 = aClass.getAnnotation(TableName.class);
                    if (annotation1 != null) {
                        classes.add(aClass);
                    }
                }
            }else{
                classes.add(aClass);
            }

        } else if (type == BEAN_TYPE_JDBC) {
            Class<?> superclass = aClass.getSuperclass();
            Deprecated annotation = aClass.getAnnotation(Deprecated.class);
            if (superclass.getName().equals(AbstractTypeConvert.class.getName()) && annotation == null) {
                classes.add(aClass);
            }
        }
    }
}
