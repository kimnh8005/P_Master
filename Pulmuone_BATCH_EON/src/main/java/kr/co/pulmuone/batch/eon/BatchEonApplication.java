package kr.co.pulmuone.batch.eon;

import kr.co.pulmuone.batch.eon.common.util.BeanUtils;
import kr.co.pulmuone.batch.eon.domain.service.system.BatchJobManagementService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@SpringBootApplication
public class BatchEonApplication implements CommandLineRunner {

    /**
     * [[ 실행방법 ]]
     * 1. java -jar -Dspring.profiles.active={env} build/libs/pmo-batch.jar {batchNo} {userId}
     * 2. 환경변수 세팅 후(실행 명령어 간소화)
     * 환경변수: export SPRING_PROFILES_ACTIVE={local,dev,prod}
     * 실행
     * java -jar build/libs/pmo-batch.jar {batchNo} {userId}
     * <p>
     * {env}
     * : 환경변수, local/dev/prod
     * {batchNo}
     * : ST_BATCH_JOB에 등록된 배치번호
     * {userId}
     * : Jenkins BUILD_USER_ID
     */
    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext =
                new SpringApplicationBuilder(BatchEonApplication.class)
                        .web(WebApplicationType.NONE)
                        .run(args);
        System.exit(SpringApplication.exit(applicationContext));
    }

    private static long getBatchNo(String num) {

        return validateBatchNo(num);
    }

    private static long validateBatchNo(String num) {

        long batchNo;
        try {
            batchNo = Long.parseLong(num);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("배치번호는 숫자로 입력하세요.");
        }

        if (batchNo <= 0) {
            throw new IllegalArgumentException("잘못된 배치번호 입니다.");
        }
        return batchNo;
    }

    private static BatchJobManagementService getBatchJobManagementService() {
        return BeanUtils.getBeanByClass(BatchJobManagementService.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private static String[] getParams(String[] args) {
        if (args == null) {
            return new String[0];
        }
        return Arrays.copyOfRange(args, 2, args.length);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("배치번호와 사용자아이디를 입력하세요.");
        }

        if (args.length > 2) {
            getBatchJobManagementService().run(getBatchNo(args[0]), args[1], args);
        } else {
            getBatchJobManagementService().run(getBatchNo(args[0]), args[1]);
        }
    }
}
