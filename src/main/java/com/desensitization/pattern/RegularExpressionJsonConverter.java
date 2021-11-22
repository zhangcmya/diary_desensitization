package com.desensitization.pattern;

import com.desensitization.utils.ConverterUtil;

/**
 * @author sijia.zhang
 * @date 2021-09-16 14:31:03
 */
public class RegularExpressionJsonConverter extends BaseJsonConverter {

    @Override
    public String invokeMsg(String oriMsg) {
        return ConverterUtil.regularExpressionInvokeMsg(oriMsg, converterCanRun, keywordMap, depth);
    }
}
