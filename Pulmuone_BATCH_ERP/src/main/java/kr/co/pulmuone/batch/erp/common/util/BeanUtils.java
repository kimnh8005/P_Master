package kr.co.pulmuone.batch.erp.common.util;

import kr.co.pulmuone.batch.erp.common.Constants;
import kr.co.pulmuone.batch.erp.common.config.ApplicationContextProvider;
import kr.co.pulmuone.batch.erp.job.BaseJob;
import kr.co.pulmuone.batch.erp.job.NoMatchBeanNameException;
import org.springframework.context.ApplicationContext;

public class BeanUtils {

    public static BaseJob getBaseJob(String className) throws ClassNotFoundException {
        if (!className.startsWith(Constants.JOB_APP_BASE_PACKAGE)) {
            throw new NoMatchBeanNameException(className);
        }
        Class<?> clazz = Class.forName(className);
        return (BaseJob) getBeanByClass(clazz);
    }

    public static <T> T getBeanByClass(Class<T> beanClass) {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        return context.getBean(beanClass);
    }
}
