package com.desensitization.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import com.alibaba.fastjson.JSONObject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sijia.zhang
 * @date 2021-09-22 13:03:26
 */
public class BaseJsonConverter extends BaseConverter{

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static final int LEVEL = 40000;

    @Override
    public String convert(ILoggingEvent event) {
        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
        Map<String, Object> map = new HashMap<>(7);
        map.put("@timestamp", dateTime.format(DATE_TIME_FORMATTER));
        map.put("message", invokeMsg(event.getFormattedMessage()));
        map.put("logger_name", event.getLoggerName());
        map.put("thread_name", event.getThreadName());
        map.put("level", event.getLevel().levelStr);
        map.put("level_value", event.getLevel().levelInt);
        if (LEVEL == event.getLevel().levelInt) {
            StackTraceElementProxy[] arr = event.getThrowableProxy().getStackTraceElementProxyArray();

            List<String> stackList = new ArrayList<>();
            for (StackTraceElementProxy line : arr) {
                stackList.add(line.toString());
            }
            map.put("stack_trace", stackList);
        }
        return JSONObject.toJSONString(map);
    }

    @Override
    public String invokeMsg(String oriMsg) {
        return null;
    }
}
