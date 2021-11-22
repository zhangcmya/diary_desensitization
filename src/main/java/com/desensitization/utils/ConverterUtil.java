package com.desensitization.utils;

import com.desensitization.enums.KeywordTypeEnum;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sijia.zhang
 * @date 2021-09-22 13:07:05
 */
public class ConverterUtil {

    private static final Pattern PATTERN = Pattern.compile("[0-9a-zA-Z]");

    public static String keyInvokeMsg(final String oriMsg, String converterCanRun, Map<String,String> keywordMap, Integer depth) {
        String tempMsg = oriMsg;
        try {
            if ("true".equals(converterCanRun)) {
                if (!keywordMap.isEmpty()) {
                    Set<String> keysArray = keywordMap.keySet();
                    for (String key : keysArray) {
                        int index = -1;
                        int i = 0;
                        do {
                            index = tempMsg.indexOf(key, index + 1);
                            if (index != -1) {
                                if (isWordChar(tempMsg, key, index)) {
                                    continue;
                                }
                                int valueStart = getValueStartIndex(tempMsg, index + key.length());
                                int valueEnd = getValueEndIndex(tempMsg, valueStart);
                                // 对获取的值进行脱敏
                                String subStr = tempMsg.substring(valueStart, valueEnd);
                                subStr = facade(subStr, keywordMap.get(key));
                                tempMsg = tempMsg.substring(0, valueStart) + subStr + tempMsg.substring(valueEnd);
                                i++;
                            }
                        } while (index != -1 && i < depth);
                    }
                }
            }
        } catch (Exception e) {
            return tempMsg;
        }
        return tempMsg;
    }

    public static String regularExpressionInvokeMsg(final String oriMsg, String converterCanRun, Map<String,String> keywordMap, Integer depth) {
        String tempMsg = oriMsg;
        Pattern pattern;
        try {
            if ("true".equals(converterCanRun)) {
                if (!keywordMap.isEmpty()) {
                    Set<String> keysArray = keywordMap.keySet();
                    for (String key : keysArray) {
                        pattern = Pattern.compile(key);
                        Matcher matcher = pattern.matcher(tempMsg);
                        int i = 0;
                        while (matcher.find() && (i < depth)) {
                            i++;
                            int valueStart = matcher.start();
                            int valueEnd = matcher.end();
                            if (valueStart < 0 || valueEnd < 0) {
                                break;
                            }
                            String subStr = matcher.group();
                            subStr = facade(subStr, keywordMap.get(key));
                            tempMsg = tempMsg.substring(0, valueStart) + subStr + tempMsg.substring(valueEnd);
                        }
                    }
                }
            }
        } catch (Exception e) {
            return tempMsg;
        }
        return tempMsg;
    }

    /**
     * 判断key是否为单词内字符
     *
     * @param msg   待检查字符串
     * @param key   关键字
     * @param index 起始位置
     * @return 判断结果
     */
    private static boolean isWordChar(String msg, String key, int index) {
        if (index != 0) {
            // 判断key前面一个字符
            char preCh = msg.charAt(index - 1);
            Matcher match = PATTERN.matcher(preCh + "");
            if (match.matches()) {
                return true;
            }
        }
        // 判断key后面一个字符
        char nextCh = msg.charAt(index + key.length());
        Matcher match = PATTERN.matcher(nextCh + "");
        if (match.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 寻找key对应值的开始位置
     *
     * @param msg        待检查字符串
     * @param valueStart 开始寻找位置
     * @return key对应值的开始位置
     */
    private static int getValueStartIndex(String msg, int valueStart) {
        do {
            char ch = msg.charAt(valueStart);
            if (ch == ':' || ch == '=') {
                valueStart++;
                ch = msg.charAt(valueStart);
                if (ch == '"') {
                    valueStart++;
                }
                break;
            } else {
                valueStart++;
            }
        } while (true);

        return valueStart;
    }

    /**
     * 寻找key对应值的结束位置
     *
     * @param msg      待检查字符串
     * @param valueEnd 开始寻找位置
     * @return key对应值的结束位置
     */
    private static int getValueEndIndex(String msg, int valueEnd) {
        do {
            if (valueEnd == msg.length()) {
                break;
            }
            char ch = msg.charAt(valueEnd);

            if (ch == '"') {
                if (valueEnd + 1 == msg.length()) {
                    break;
                }
                char nextCh = msg.charAt(valueEnd + 1);
                if (nextCh == ';' || nextCh == ',' || nextCh == '}') {
                    while (valueEnd > 0) {
                        char preCh = msg.charAt(valueEnd - 1);
                        if (preCh != '\\') {
                            break;
                        }
                        valueEnd--;
                    }
                    break;
                } else {
                    valueEnd++;
                }
            } else if (ch == ';' || ch == ',' || ch == '}') {
                break;
            } else {
                valueEnd++;
            }
        } while (true);

        return valueEnd;
    }

    protected static String facade(String msg, String key){

        String result = msg;
        KeywordTypeEnum keywordTypeEnum = KeywordTypeEnum.getMessageType(key);
        if (keywordTypeEnum == null) {
            keywordTypeEnum = KeywordTypeEnum.OTHER;
        }
        switch (keywordTypeEnum){
            case TRUE_NAME:
                result = SensitiveInfoUtil.handleTrueName(msg);
                break;
            case ID_CARD_NO:
                result = SensitiveInfoUtil.handleIdCardNo(msg);
                break;
            case BANKCARD_NO:
                result = SensitiveInfoUtil.handleBankcardNo(msg);
                break;
            case PHONE_NO:
                result = SensitiveInfoUtil.handlePhoneNo(msg);
                break;
            case OTHER:
                result = SensitiveInfoUtil.handleOther(msg);
                break;
            default:
                break;
        }
        return result;
    }
}
