package com.ef;

import com.ef.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class Parser {

    static Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    public static void main(String[] args){
        ApplicationContext context =
                new AnnotationConfigApplicationContext(Parser.class);
        Helper helper = context.getBean(Helper.class);
        Params params;
        try {
            params = helper.readParams(args);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return;
        }
        ServiceProvider service = context.getBean(ServiceProvider.class);
        service.setParams(params);
        service.start();
    }
}
