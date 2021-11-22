package com.desensitization.pattern;

import com.desensitization.utils.ConverterUtil;

/**
 * KeywordJsonConverter.
 *
 * @author sijia.zhang
 * @date 2021-09-22 11:25:51
 */
public class KeywordJsonConverter extends BaseJsonConverter {

    @Override
    public String invokeMsg(final String oriMsg) {
        return ConverterUtil.keyInvokeMsg(oriMsg, converterCanRun, keywordMap, depth);
    }
}
