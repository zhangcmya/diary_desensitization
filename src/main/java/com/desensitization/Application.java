package com.desensitization;

import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sijia.zhang
 * @date 2021-09-15 12:46:59
 */
@SpringBootApplication
public class Application {

//    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
//    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", "186412850523");
        String s = JSON.toJSONString(map);
        Logger logger = LoggerFactory.getLogger(Application.class);
        logger.info(s);
        logger.info("11111111111111");
        map.put("phone", "186412850523");
    }
}
