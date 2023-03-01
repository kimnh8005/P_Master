package kr.co.pulmuone.bos;

import java.nio.file.Paths;

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

import kr.co.pulmuone.v1.comm.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)  // SpringBoot CGLIB AOP 활성화
@ComponentScan(
        basePackages = {
                "kr.co.pulmuone.bos"
                , "kr.co.pulmuone.v1"}
        , excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "kr.co.pulmuone.v1.batch.*")
}
)
public class SpringBootBosApplication implements ApplicationContextAware, CommandLineRunner {

    public static void main(String[] args) {

        /*
         * Profile 별 YML 파일 Load
         */
        SystemUtil.setApplicationProfile();

        /*
         * BOS 어플리케이션 실행
         */
        SpringApplication.run(SpringBootBosApplication.class, args);

    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {

        log.info("----------  Pulmuone : BOS Application Start  ----------  ");
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
