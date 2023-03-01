package kr.co.pulmuone.batch.cj.job;

import lombok.Getter;

@Getter
public class NoMatchBeanNameException extends RuntimeException {

    private String beanName;

    public NoMatchBeanNameException(String beanName) {
        this.beanName = beanName;
    }

    public NoMatchBeanNameException(String message, String beanName) {
        super(message);
        this.beanName = beanName;
    }

    public NoMatchBeanNameException(String message, Throwable cause, String beanName) {
        super(message, cause);
        this.beanName = beanName;
    }

    public NoMatchBeanNameException(Throwable cause, String beanName) {
        super(cause);
        this.beanName = beanName;
    }

    public NoMatchBeanNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String beanName) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.beanName = beanName;
    }
}
