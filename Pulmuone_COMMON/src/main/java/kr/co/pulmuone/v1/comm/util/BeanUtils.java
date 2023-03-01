package kr.co.pulmuone.v1.comm.util;

import kr.co.pulmuone.v1.comm.config.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;


public class BeanUtils {
    public static <T> T getBeanByClass(Class<T> beanClass) {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        return context.getBean(beanClass);
    }
}
