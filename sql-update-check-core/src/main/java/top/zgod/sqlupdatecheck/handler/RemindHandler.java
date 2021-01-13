package top.zgod.sqlupdatecheck.handler;


import top.zgod.sqlupdatecheck.bean.ColumnBean;

import java.util.HashMap;
import java.util.List;

/**
 * 检查不合格提醒回调
 *
 * @author ZGOD
 */
public interface RemindHandler {
    /**
     * 回调不符合检查的实体
     *
     * @param withoutTableOrColumnMap
     * @param withoutEntityOrPropertyMap
     * @param columnTypeDifferentMap
     */
    void remindContainer(HashMap<String, List<ColumnBean>> withoutTableOrColumnMap,
                         HashMap<String, List<ColumnBean>> withoutEntityOrPropertyMap,
                         HashMap<String, List<ColumnBean>> columnTypeDifferentMap);

    /**
     * 回调检查信息
     *
     * @param remindStrList
     */
    void remindMessage(List<String> remindStrList);
}
