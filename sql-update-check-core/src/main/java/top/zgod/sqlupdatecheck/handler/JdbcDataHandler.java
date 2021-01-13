package top.zgod.sqlupdatecheck.handler;

import java.util.List;
import java.util.Map;

/**
 * @author ZGOD
 */
public interface JdbcDataHandler {
    /**
     * 获取数据库数据信息
     *
     * @return
     */
    List<Map<String, Object>> getJdbcData();
}
