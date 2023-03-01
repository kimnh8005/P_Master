package kr.co.pulmuone.v1.user.company.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.CompanyEnums;
import kr.co.pulmuone.v1.user.company.dto.vo.BusinessInformationVo;
import kr.co.pulmuone.v1.user.company.dto.vo.CommmonHeadQuartersCompanyVo;
import kr.co.pulmuone.v1.user.company.dto.vo.CompanyVo;
import kr.co.pulmuone.v1.user.company.dto.vo.HeadQuartersVo;

class UserCompanyServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private UserCompanyService userCompanyService;

    @Test
    void 본사_회사정보_조회_성공() throws Exception {
        // given, when
        CommmonHeadQuartersCompanyVo result = userCompanyService.getHeadquartersCompany();

        // then
        assertEquals("(주)풀 무 원",result.getCompanyName().trim());

    }

    @Test
    void 사업자정보관리_조회(){
        BusinessInformationVo businessInformationVo = userCompanyService.getBizInfo();

        assertTrue( !ObjectUtils.isEmpty(businessInformationVo) );
    }

    @Test
    void 회사정보_등록() throws Exception {
        CompanyVo companyVo = new CompanyVo();
        companyVo.setCompanyType(CompanyEnums.CompanyType.HEADQUARTERS.getCode());
        companyVo.setCompanyName("(주)풀 무 원 ");
        companyVo.setCompanyCeoName("이효율");
        companyVo.setBusinessNumber("315-81-01744");
        companyVo.setCompanyMail("Pulmuone@Pulmuone.com");
        companyVo.setZipCode("06367");
        companyVo.setAddress("서울 강남구 광평로 280 (수서동)");
        companyVo.setDetailAddress("(주) 풀무원");
        companyVo.setCreateId("1");

        int count = userCompanyService.addCompany(companyVo);

        assertEquals(1, count);
    }

    @Test
    void 회사정보_수정() throws Exception {
        CompanyVo companyVo = new CompanyVo();
        companyVo.setCompanyId(1L);
        companyVo.setCompanyName("(주)풀 무 원 ");
        companyVo.setCreateId("1");

        int count = userCompanyService.putCompany(companyVo);

        assertEquals(1, count);
    }

    @Test
    void 본사정보_등록() throws Exception {
        HeadQuartersVo headQuartersVo = new HeadQuartersVo();
        headQuartersVo.setCompanyId(1L);
        headQuartersVo.setCorporationNumber("110111-0375439-1234");
        headQuartersVo.setMailOrderNumber("제2019-서울강남-02379호");
        headQuartersVo.setHostingProvider("Pulmuone  풀무원");
        headQuartersVo.setHealthFunctFoodReport("제2019-016813호");
        headQuartersVo.setServiceCenterPhoneNumber("080-022-0085");
        headQuartersVo.setServiceCenterOperatorOpenTime("21:00:00");
        headQuartersVo.setServiceCenterOperatorCloseTime("22:30:00");
        headQuartersVo.setServiceCenterLunchTimeStart("12:00:00");
        headQuartersVo.setServiceCenterLunchTimeEnd("13:00:00");
        headQuartersVo.setLunchTimeYn("N");
        headQuartersVo.setEscrowOriginFileName("나이스페이에스크로.png");
        headQuartersVo.setEscrowFileName("B5085CE4001E47659633.png");
        headQuartersVo.setEscrowFilePath("BOS/ur/test/2020/09/08/");
        headQuartersVo.setEscrowDescription("LG데이콤 구매안전에스크로서비스");
        headQuartersVo.setEscrowSubscriptionUrl("http://slb-4329475.ncloudslb.com/layout.html#/bizInfo");
        headQuartersVo.setCreateId("1");
        headQuartersVo.setSecurityDirector("080-022-0085");

        int count = userCompanyService.addHeadQuarters(headQuartersVo);

        assertEquals(1, count);
    }

    @Test
    void 본사정보_수정() throws Exception {
        HeadQuartersVo headQuartersVo = new HeadQuartersVo();
        headQuartersVo.setHeadquartersId(3L);
        headQuartersVo.setCorporationNumber("110111-0375439-1234");
        headQuartersVo.setCreateId("1");

        int count = userCompanyService.putHeadQuarters(headQuartersVo);

        assertEquals(1, count);
    }
}