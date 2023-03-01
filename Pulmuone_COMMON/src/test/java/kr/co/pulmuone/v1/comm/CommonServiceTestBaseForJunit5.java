package kr.co.pulmuone.v1.comm;

import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
//@ActiveProfiles("local")
@Transactional
public class CommonServiceTestBaseForJunit5 {

    private TestContextManager testContextManager;

    @BeforeAll
    static void beforeAll() throws Exception {
        SystemUtil.setApplicationProfile();
    }

    @BeforeEach
    void setUp() throws Exception {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
    }

    public void preLogin() {
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLoginName("포비즈");
        userVO.setUserType(null);
        userVO.setStatusType("EMPLOYEE_STATUS.NORMAL");
        userVO.setCompanyName("(주)풀무원");
        userVO.setRoleId("1");
        userVO.setPasswordChangeYn("N");
        userVO.setLastLoginElapsedDay(0);
        userVO.setConnectionId(53308);
        userVO.setTemporaryYn("N");
        userVO.setPersonalInformationAccessYn("N");
        userVO.setLangCode("1");
        userVO.setListAuthSupplierId(new ArrayList<>());
        userVO.setListAuthWarehouseId(new ArrayList<>());
        userVO.setListAuthStoreId(new ArrayList<>());
        userVO.setListAuthSellersId(new ArrayList<>());
        SessionUtil.setUserVO(userVO);
    }

    public void buyerLogin() {
        BuyerVo buyerVo = new BuyerVo();
        buyerVo.setSnsAuthorizationState("");
        buyerVo.setUrUserId("1646773");
        buyerVo.setUrErpEmployeeCode("");
        buyerVo.setSnsProvider("");
        buyerVo.setSnsSocialId("");
        buyerVo.setPersonalCertificationUserName("테스터");
        buyerVo.setPersonalCertificationMobile("01072721234");
        buyerVo.setPersonalCertificationGender("M");
        buyerVo.setPersonalCertificationCiCd("123qweasd");
        buyerVo.setPersonalCertificationBirthday("19771215");
        SessionUtil.setUserVO(buyerVo);
    }
}
