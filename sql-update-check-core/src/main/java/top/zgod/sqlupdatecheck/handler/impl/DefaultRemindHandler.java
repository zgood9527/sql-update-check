package top.zgod.sqlupdatecheck.handler.impl;

import top.zgod.sqlupdatecheck.bean.ColumnBean;
import top.zgod.sqlupdatecheck.handler.RemindHandler;

import java.util.HashMap;
import java.util.List;

/**
 * @author ZGOD
 */
public class DefaultRemindHandler implements RemindHandler {
    @Override
    public void remindContainer(HashMap<String, List<ColumnBean>> withoutTableOrColumnMap, HashMap<String, List<ColumnBean>> withoutEntityOrPropertyMap, HashMap<String, List<ColumnBean>> columnTypeDifferentMap) {

    }

    @Override
    public void remindMessage(List<String> remindStrList) {

    }
}
