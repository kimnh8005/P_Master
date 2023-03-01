package kr.co.pulmuone.batch;

import kr.co.pulmuone.batch.common.util.BeanUtils;
import kr.co.pulmuone.batch.domain.service.system.BatchJobManagementService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(
        basePackages = {
                "kr.co.pulmuone.batch"
                , "kr.co.pulmuone.v1.batch"
                , "kr.co.pulmuone.v1.comm.util"
                // service
                , "kr.co.pulmuone.v1.api.cjlogistics"
                , "kr.co.pulmuone.v1.api.lotteglogis"
                , "kr.co.pulmuone.v1.api.ncp"
                , "kr.co.pulmuone.v1.api.hitok"
                , "kr.co.pulmuone.v1.approval.auth"
                , "kr.co.pulmuone.v1.base"
                , "kr.co.pulmuone.v1.comm.api"
                , "kr.co.pulmuone.v1.display.dictionary"
                , "kr.co.pulmuone.v1.goods.discount"
                , "kr.co.pulmuone.v1.goods.goods"
                , "kr.co.pulmuone.v1.goods.item"
                , "kr.co.pulmuone.v1.goods.itemprice"
                , "kr.co.pulmuone.v1.goods.price"
                , "kr.co.pulmuone.v1.goods.search"
                , "kr.co.pulmuone.v1.goods.stock"
                , "kr.co.pulmuone.v1.order.claim"
                , "kr.co.pulmuone.v1.order.date"
                , "kr.co.pulmuone.v1.order.delivery"
                , "kr.co.pulmuone.v1.order.front"
                , "kr.co.pulmuone.v1.order.order"
                , "kr.co.pulmuone.v1.order.registration"
                , "kr.co.pulmuone.v1.order.regular"
                , "kr.co.pulmuone.v1.order.shipping"
                , "kr.co.pulmuone.v1.order.status"
                , "kr.co.pulmuone.v1.order.present"
                , "kr.co.pulmuone.v1.pg"
                , "kr.co.pulmuone.v1.policy.benefit"
                , "kr.co.pulmuone.v1.policy.config"
                , "kr.co.pulmuone.v1.policy.excel"
                , "kr.co.pulmuone.v1.policy.holiday"
                , "kr.co.pulmuone.v1.policy.payment"
                , "kr.co.pulmuone.v1.policy.clause"
                , "kr.co.pulmuone.v1.promotion.coupon"
                , "kr.co.pulmuone.v1.promotion.exhibit"
                , "kr.co.pulmuone.v1.promotion.point"
                , "kr.co.pulmuone.v1.promotion.serialnumber"
                , "kr.co.pulmuone.v1.search.indexer"
                , "kr.co.pulmuone.v1.send.template"
                , "kr.co.pulmuone.v1.shopping.cart"
                , "kr.co.pulmuone.v1.shopping.favorites"
                , "kr.co.pulmuone.v1.shopping.recently"
                , "kr.co.pulmuone.v1.store.delivery"
                , "kr.co.pulmuone.v1.store.warehouse"
                , "kr.co.pulmuone.v1.system.basic"
                , "kr.co.pulmuone.v1.system.code"
                , "kr.co.pulmuone.v1.system.log"
                , "kr.co.pulmuone.v1.system.monitoring"
                , "kr.co.pulmuone.v1.user.buyer"
                , "kr.co.pulmuone.v1.user.certification"
                , "kr.co.pulmuone.v1.user.device"
                , "kr.co.pulmuone.v1.user.dormancy"
                , "kr.co.pulmuone.v1.user.environment"
                , "kr.co.pulmuone.v1.user.group"
                , "kr.co.pulmuone.v1.user.join"
                , "kr.co.pulmuone.v1.user.login"
                , "kr.co.pulmuone.v1.user.noti"
                , "kr.co.pulmuone.v1.outmall.ezadmin"
                , "kr.co.pulmuone.v1.order.schedule"
                , "kr.co.pulmuone.v1.api.babymeal"
                , "kr.co.pulmuone.v1.api.eatsslim"
                , "kr.co.pulmuone.v1.comm.excel.util"
                , "kr.co.pulmuone.v1.outmall.order"
                , "kr.co.pulmuone.v1.comm.excel"
                , "kr.co.pulmuone.v1.order.email"
                , "kr.co.pulmuone.v1.policy.claim"
                , "kr.co.pulmuone.v1.order.create"
                , "kr.co.pulmuone.v1.order.ifday"
                , "kr.co.pulmuone.v1.api.sns"
                , "kr.co.pulmuone.v1.api.storedelivery"
                , "kr.co.pulmuone.v1.system.code.service"
                , "kr.co.pulmuone.v1.policy.shiparea"
                , "kr.co.pulmuone.v1.user.store"
                , "kr.co.pulmuone.v1.user.company"
                , "kr.co.pulmuone.v1.outmall.sellers"
                , "kr.co.pulmuone.v1.policy.fee"
                , "kr.co.pulmuone.v1.shopping.restock"
                , "kr.co.pulmuone.v1.promotion.linkprice"
                , "kr.co.pulmuone.v1.calculate.employee"
                , "kr.co.pulmuone.v1.api.api2pdf"
        }
)
public class BatchApplication implements CommandLineRunner {

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
                new SpringApplicationBuilder(BatchApplication.class)
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
