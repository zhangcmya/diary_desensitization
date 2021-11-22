package com.desensitization.pattern;

import com.desensitization.utils.ConverterUtil;

/**
 * RegularExpressionConverter.
 *
 * @author sijia.zhang
 */
public class RegularExpressionConverter extends BaseConverter {

    @Override
    public String invokeMsg(String oriMsg) {
        return ConverterUtil.regularExpressionInvokeMsg(oriMsg, converterCanRun, keywordMap, depth);
    }
}
