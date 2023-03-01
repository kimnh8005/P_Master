package kr.co.pulmuone.v1.comm;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import kr.co.pulmuone.v1.comm.api.factory.HttpRequestFactory;
import kr.co.pulmuone.v1.comm.api.factory.RestTemplateFactory;
import kr.co.pulmuone.v1.comm.api.service.impl.ErpApiExchangeServiceImpl;
import kr.co.pulmuone.v1.comm.util.SystemUtil;

@ExtendWith(SpringExtension.class) // JUnit 5 Jupiter 적용, JUnit 4 의 @RunWith 대체
@ActiveProfiles("local")
@TestPropertySource( //
        properties = { //
                SystemUtil.SPRING_CONFIG_LOCATION + "=" // YML 설정 파일 경로 설정
                        + SystemUtil.COMMON_PROFILE_YML_PATH // 모든 profile 이 공유하는 공통 설정 yml 경로
                        + SystemUtil.YML_PATH_SEPARATOR //
                        + SystemUtil.LOCAL_PROFILE_YML_PATH // LOCAL Profile yml 경로
        } //
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = { HttpRequestFactory.class, RestTemplateFactory.class })
@SpringBootTest(classes = { ErpApiExchangeServiceImpl.class })
public class ErpApiServiceTestConfig {

    @Autowired
    protected ErpApiExchangeServiceImpl erpApiExchangeService;

}
