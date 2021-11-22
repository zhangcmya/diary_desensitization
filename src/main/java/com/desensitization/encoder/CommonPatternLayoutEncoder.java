package com.desensitization.encoder;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;

import java.text.MessageFormat;

/**
 * CommonPatternLayoutEncoder.
 *
 * @author sijia.zhang
 */
public class CommonPatternLayoutEncoder extends PatternLayoutEncoder {

    protected static final String PATTERN_D1 = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread]";
    protected static final String PATTERN_D2 = " [{0}:%X'{'{1}'}']";
    protected static final String PATTERN_D3_S1 = " [%level] %logger{10}-%L ";
    /**
     * 0:日志脱敏开关,1:查找深度(超过深度后停止正则匹配),2:日志最大长度,3:待脱敏关键字
     */
    protected static final String PATTERN_D3_S2 = "%msg'{'{0},{1},{2},{3}'}'%n";

    /**
     * 日志脱敏开关
     */
    protected String converterCanRun = "false";

    /**
     * 待脱敏关键字
     */
    protected String sensitiveData;

    /**
     * 自定义MDC的key,多个key用逗号分隔。
     */
    protected String mdcKeys;

    /**
     * 匹配深度(若深度值过大影响性能)
     */
    protected int depth = 12;

    /**
     * 单条消息的最大长度
     */
    protected int maxLength = 2048;

    @Override
    public void start() {
        if (getPattern() == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(PATTERN_D1);
            //拼接MDC参数
            if (mdcKeys != null) {
                String[] keys = mdcKeys.split(",");
                for (String key : keys) {
                    sb.append(MessageFormat.format(PATTERN_D2, key, key));
                }
            }
            sb.append(PATTERN_D3_S1);
            if (maxLength < 0 || maxLength > 10240) {
                maxLength = 2048;
            }
            sb.append(MessageFormat.format(PATTERN_D3_S2, converterCanRun,String.valueOf(depth),String.valueOf(maxLength),sensitiveData));
            System.out.println(sb);
            setPattern(sb.toString());
        }
        super.start();
    }

    public String getSensitiveData() {
        return sensitiveData;
    }

    public void setSensitiveData(String sensitiveData) {
        this.sensitiveData = sensitiveData;
    }

    public String getConverterCanRun() {
        return converterCanRun;
    }

    public void setConverterCanRun(String converterCanRun) {
        this.converterCanRun = converterCanRun;
    }

    public String getMdcKeys() {
        return mdcKeys;
    }

    public void setMdcKeys(String mdcKeys) {
        this.mdcKeys = mdcKeys;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String getPattern() {
        return super.getPattern();
    }

    @Override
    public void setPattern(String pattern) {
        super.setPattern(pattern);
    }

}