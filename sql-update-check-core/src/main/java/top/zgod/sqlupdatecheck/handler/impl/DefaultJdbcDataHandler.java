package top.zgod.sqlupdatecheck.handler.impl;

import top.zgod.sqlupdatecheck.handler.JdbcDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author ZGOD
 */
public class DefaultJdbcDataHandler implements JdbcDataHandler {

    private static final String SQL_DATA = "select TABLE_NAME,COLUMN_NAME,DATA_TYPE from information_schema.COLUMNS where TABLE_SCHEMA = (select database());";

    @Autowired
    private DataSource dataSource;

    @Override
    public List<Map<String, Object>> getJdbcData() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.queryForList(SQL_DATA);
    }
}
