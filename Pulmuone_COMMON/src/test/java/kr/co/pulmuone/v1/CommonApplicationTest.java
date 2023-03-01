package kr.co.pulmuone.v1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
//@MapperScan(basePackages = { "kr.co.pulmuone.common.persistence.mapper.*", "kr.co.pulmuone.v1.comm.mapper.*" })
@ComponentScan(
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "kr.co.pulmuone.v1.batch.*")
        }
)
public class CommonApplicationTest implements ApplicationContextAware, CommandLineRunner {

    public static void main(String[] args) {
        /*
         * Profile 별 YML 파일 Load
         */
//        SystemUtil.setApplicationProfile();

        /*
         * MALL 어플리케이션 실행
         */
//        SpringApplication.run(CommonApplicationTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
