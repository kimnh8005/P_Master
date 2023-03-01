package kr.co.pulmuone.v1.comm.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
    	System.out.println("set ctx ::: " + ctx);
        applicationContext = ctx;
    }

    public static ApplicationContext getApplicationContext() {
    	System.out.println("get applicationContext ::: " + applicationContext);
        return applicationContext;
    }

}
