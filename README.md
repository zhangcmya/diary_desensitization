# diary_desensitization
## logback日志脱敏工具
### 功能：
#### 通过logback底层开发,在日志打印之前进行脱敏处理
##### 1、统一日志pattern格式:

##### %d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] [{0}:%X'{'{1}'}'] [%level] %logger{10}-%L %msg'{'{0},{1},{2},{3}'}'%n

##### 2、日志敏感数据脱敏处理

##### 3、日志超长截取

### 脱敏规则：

###### a、[trueName]真实姓名脱敏(展示姓名第一个字,其余用*替换)
###### b、[idCardNo]身份证号码脱敏(展示身份证号码前3位和最后4位,其余用*替换)
###### c、[phoneNo]手机号码脱敏(展示手机号码前3位和最后4位,其余用*替换)
###### d、[bankcardNo]银行卡号脱敏(展示银行卡号前6位和最后4位,其余用*替换)
###### e、[other]其它数据脱敏(全部用*替换)

### 使用方法：

##### logback.xml添加配置项：

##### 1、配置 通用pattern格式Encoder

````xml
<!--配置 通用pattern格式Encoder-->
<!--sensitiveData:脱敏处理的规则-->
<!--mdcKeys:自定义的MDC名称-->
<!--converterCanRun:脱敏功能开关:true打开,false关闭-->
<!--maxLength:日志最大长度-->
<!--depth:脱敏深度-->
<encoder class="com.desensitization.encoder.CommonPatternLayoutEncoder">
    <converterCanRun>true</converterCanRun>
    <sensitiveData>
        <!-- 以下只能出现一种 -->
        <!--sensitiveData配置规则,关键字类型:trueName,idCardNo,bankcardNo,phoneNo,other -->
        <!--示例 关键字类型1:关键字或正则表达式(多个用;隔开),关键字类型2:关键字或正则表达式...-->
        idCardNo:cardId;idNo,trueName:realName <!--按关键字脱敏处理-->
        "idCardNo:\d{18}","phoneNo:1[0-9]{10}" <!--按正则表达式脱敏处理(注意双引号)-->
    </sensitiveData>
    <mdcKeys>sessionId</mdcKeys>
    <depth>128</depth>
    <maxLength>2048</maxLength>
    <charset>UTF-8</charset>
</encoder>
````

##### 2、配置 脱敏处理Converter

##### a、通过关键字脱敏

```xml
<!-- 配置 关键字脱敏处理Converter-->
<conversionRule conversionWord="msg" converterClass="com.desensitization.pattern.KeywordConverter"/>
```

##### b、通过正则表达式脱敏

```xml
<!-- 配置 正则表达式脱敏处理Converter-->
<conversionRule conversionWord="msg" converterClass="com.gitee.cqdevops.desensitization.patternRegularExpressionConverter"/>
```

### logback.xml配置样例(通过关键字脱敏)

````xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="60 seconds" debug="true">
    <!-- 配置 关键字脱敏处理Converter-->
    <conversionRule conversionWord="msg"
                    converterClass="com.desensitization.pattern.KeywordConverter"/>

    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <file>日志文件名</file>
        <!-- 配置 通用pattern格式Encoder-->
        <encoder class="com.desensitization.encoder.CommonPatternLayoutEncoder">
            <converterCanRun>true</converterCanRun>
            <sensitiveData>idCardNo:cardId;idNo,trueName:realName</sensitiveData>
            <mdcKeys>sessionId</mdcKeys>
            <depth>128</depth>
            <maxLength>2048</maxLength>
            <charset>UTF-8</charset>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <FileNamePattern>日志文件名-%d{yyyy-MM-dd}-%i.log</FileNamePattern>
            <maxFileSize>64MB</maxFileSize>
            <maxHistory>7</maxHistory>
        </rollingPolicy>
    </appender>

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>
</configuration>  
````
