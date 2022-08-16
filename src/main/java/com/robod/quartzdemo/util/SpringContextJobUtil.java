package com.robod.quartzdemo.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author L.D.
 * @date 2022/8/15 11:18
 * @description
 */
@Component
public class SpringContextJobUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    @SuppressWarnings("static-access")
    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        this.context = context;
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

}
