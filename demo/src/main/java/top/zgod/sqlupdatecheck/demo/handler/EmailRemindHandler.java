package top.zgod.sqlupdatecheck.demo.handler;

import top.zgod.sqlupdatecheck.bean.ColumnBean;
import top.zgod.sqlupdatecheck.handler.RemindHandler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author ZGOD
 */
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
