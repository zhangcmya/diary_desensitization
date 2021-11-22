package com.desensitization.pattern;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.desensitization.enums.KeywordTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseConverter extends MessageConverter {

    // 日志脱敏开关
    protected String converterCanRun = "false";
    // 匹配深度
    protected Integer depth = 12;
    //单条消息的最大长度，主要是message
    protected Integer maxLength = 2048;
    // 日志脱敏关键字
    protected static Map<String,String> keywordMap = new HashMap<>();

    @Override
    public void start() {
        List<String> options = getOptionList();
        //如果存在参数选项，则提取
        if (options != null && options.size() >= 3) {
            converterCanRun = String.valueOf(options.get(0));
            depth = Integer.valueOf(options.get(1));
            maxLength = Integer.valueOf(options.get(2));
            if (options.size() > 3){
                for (int i = 3;i < options.size();i++) {
                    String sensitiveData = String.valueOf(options.get(i));
                    if (!StringUtils.isEmpty(sensitiveData)) {
                        String[] sensitiveArray = sensitiveData.split(":");
                        if (sensitiveArray.length == 2) {
                            String keywordType = sensitiveArray[0];
                            KeywordTypeEnum keywordTypeEnum = KeywordTypeEnum.getMessageType(keywordType);
                            if (keywordTypeEnum != null && !StringUtils.isEmpty(sensitiveArray[1])) {
                                String[] keywordArray = sensitiveArray[1].split(";");
                                if (keywordArray.length > 0) {
                                    for (String keyword : keywordArray) {
                                        keywordMap.put(keyword, keywordType);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        super.start();
    }

    @Override
    public String convert(ILoggingEvent event){
        String oriLogMsg = event.getFormattedMessage();
        if (oriLogMsg == null || oriLogMsg.isEmpty()) {
            return oriLogMsg;
        }
        //如果超长截取
        if (oriLogMsg.length() > maxLength) {
            //后面增加三个终止符
            oriLogMsg = oriLogMsg.substring(0, maxLength) + "<<<";
        }
        return invokeMsg(oriLogMsg);
    }

    public abstract String invokeMsg(final String oriMsg);
}
