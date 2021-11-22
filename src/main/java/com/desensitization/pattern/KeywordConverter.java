package com.desensitization.pattern;

import com.desensitization.utils.ConverterUtil;

/**
 * KeywordConverter
 *
 * @author sijia.zhang
 */
public class KeywordConverter extends BaseConverter {

    @Override
    public String invokeMsg(final String oriMsg) {
        return ConverterUtil.keyInvokeMsg(oriMsg, converterCanRun, keywordMap, depth);
    }
}
