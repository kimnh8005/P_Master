package kr.co.pulmuone.v1.comm.aop.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UserMaskingRun {
    String system() default "BOS";  //BOS, MALL, MUST_MASKING
}