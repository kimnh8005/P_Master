package kr.co.pulmuone.mall;

import kr.co.pulmuone.v1.comm.config.SlaveCjFrontDatabaseConfig;
import kr.co.pulmuone.v1.comm.config.SlaveErpDatabaseConfig;
import kr.co.pulmuone.v1.comm.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.core.annotation.Order;

import java.nio.file.Paths;

@Slf4j
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)  // SpringBoot CGLIB AOP 활성화
@ComponentScan(
        basePackages = {
                "kr.co.pulmuone.mall"
                , "kr.co.pulmuone.v1"
        }
        , excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = {"kr.co.pulmuone.v1.batch.*"})
}
)
public class SpringBootMallApplication implements ApplicationContextAware, CommandLineRunner {
    public static void main(String[] args) {

        /*
         * Profile 별 YML 파일 Load
         */
        SystemUtil.setApplicationProfile();

        /*
         * MALL 어플리케이션 실행
         */
        SpringApplication.run(SpringBootMallApplication.class, args);

    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {

        log.info("----------  Pulmuone : MALL Application Start  ----------  ");
        log.info("    server.port : " + ctx.getEnvironment().getProperty("server.port"));
        log.info("    spring.profiles.active : " + ctx.getEnvironment().getProperty(SystemUtil.SPRING_PROFILES_ACTIVE));
        log.info("    spring.config.location : " + ctx.getEnvironment().getProperty(SystemUtil.SPRING_CONFIG_LOCATION));
        log.info("    baseDir : " + Paths.get("").toAbsolutePath().toString());
        log.info("    rootDir : " + Paths.get(Paths.get("").toFile().getAbsolutePath()).getRoot().toString());

    }

    @Order(1)
    @Override
    public void run(String... args) throws Exception {

    }

}
