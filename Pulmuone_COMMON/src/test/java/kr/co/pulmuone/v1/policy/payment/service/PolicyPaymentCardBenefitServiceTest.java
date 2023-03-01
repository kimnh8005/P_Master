package kr.co.pulmuone.v1.policy.payment.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.PolicyEnums;
import kr.co.pulmuone.v1.comm.mapper.policy.payment.PolicyPaymentCardBenefitMapper;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentCardBenefitDto;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentCardBenefitVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
class PolicyPaymentCardBenefitServiceTest extends CommonServiceTestBaseForJunit5 {


	@Autowired
    private PolicyPaymentCardBenefitService policyPaymentCardBenefitService;

	@InjectMocks
	private PolicyPaymentCardBenefitService mockPolicyPaymentCardBenefitService;

	@Mock
	private PolicyPaymentCardBenefitMapper mockPolicyPaymentCardBenefitMapper;

	@BeforeEach
	void setUp() {
		mockPolicyPaymentCardBenefitService = new PolicyPaymentCardBenefitService(mockPolicyPaymentCardBenefitMapper);
	}

	@Test
	public void test_등록된혜택_결과있음()  {

		// given
    	Long psCardBenefitId = (long) 2;
    	log.info("psCardBenefitId: {}", psCardBenefitId);

    	// when
    	PolicyPaymentCardBenefitVo result = policyPaymentCardBenefitService.getPolicyPaymentCardBenefitInfo(psCardBenefitId);

		// then
		assertNotNull(result);
	}
    @Test
    public void test_등록된혜택목록_결과있음()  {

    	// given
    	String psPgCd = "";
    	String cardBenefitTp = "";

    	PolicyPaymentCardBenefitDto dto = new PolicyPaymentCardBenefitDto();
    	dto.setPsPgCd(psPgCd);
    	dto.setCardBenefitTp(cardBenefitTp);

        // when
    	PolicyPaymentCardBenefitDto result = policyPaymentCardBenefitService.getPolicyPaymentCardBenefitList(dto);

    	// then
        assertNotNull(result.getRows());
    }

    @Test
	public void test_신용카드혜택_신규등록() {
		PolicyPaymentCardBenefitVo returnVo = new PolicyPaymentCardBenefitVo();
		returnVo.setPsCardBenefitId("1");

		given(mockPolicyPaymentCardBenefitMapper.checkDuplicateTermPolicyPaymentCardBenefit(any())).willReturn(returnVo);

        // when
        ApiResult<?> result = mockPolicyPaymentCardBenefitService.addPolicyPaymentCardBenefit(null);

        // then
        assertTrue(PolicyEnums.Payment.DUPLICATE_TERM.equals(result.getMessageEnum()));
	}

	@Test
	public void test_신용카드혜택_신규등록_오류_중복된기간() {
		PolicyPaymentCardBenefitVo returnVo = new PolicyPaymentCardBenefitVo();
		returnVo.setPsCardBenefitId("");

		given(mockPolicyPaymentCardBenefitMapper.checkDuplicateTermPolicyPaymentCardBenefit(any())).willReturn(returnVo);

		// when
		ApiResult<?> result = mockPolicyPaymentCardBenefitService.addPolicyPaymentCardBenefit(null);

		// then
		assertFalse(result.getCode().equals(ApiResult.fail().getCode()));
	}

    @Test
    public void test_신용카드혜택_수정() {
    	String psCardBenefitId = "58";
    	String psPgCd = "PG_SERVICE.KCP";
    	String cardBenefitTp = "CARD_BENEFIT_INFO_TP.BILLING_DISCNT";
    	String startDt = "2020-09-21";
    	String endDt = "2020-12-31";
    	String title = "test_신용카드혜택_수정";
    	String information = "test_신용카드혜택_수정 본문";

    	 // given
    	PolicyPaymentCardBenefitDto dto = new PolicyPaymentCardBenefitDto();
    	dto.setPsCardBenefitId(psCardBenefitId);
    	dto.setPsPgCd(psPgCd);
    	dto.setCardBenefitTp(cardBenefitTp);
    	dto.setStartDt(startDt);
    	dto.setEndDt(endDt);
    	dto.setTitle(title);
    	dto.setInformation(information);
        log.info("dto: {}", dto);

        // when
        MessageCommEnum msgEnum = policyPaymentCardBenefitService.putPolicyPaymentCardBenefit(dto);

        // then
        assertTrue(msgEnum.getCode().equals(ApiResult.success().getCode()));
    }
    @Test
    public void test_신용카드혜택_수정_오류() {
    	// given
    	PolicyPaymentCardBenefitDto dto = new PolicyPaymentCardBenefitDto();
    	dto.setPsCardBenefitId("11111");
    	dto.setPsPgCd("PG_SERVICE.KCP");
    	dto.setCardBenefitTp("CARD_BENEFIT_INFO_TP.BILLING_DISCNT");
    	dto.setStartDt("2020-10-01");
    	dto.setEndDt("2020-10-02");
    	dto.setTitle("test_신용카드혜택_수정");
    	dto.setInformation("test_신용카드혜택_수정 본문");

    	// when
    	MessageCommEnum msgEnum = policyPaymentCardBenefitService.putPolicyPaymentCardBenefit(dto);

    	// then
		assertEquals(PolicyEnums.Payment.UPDATE_FAIL, msgEnum);
    }

    @Test
    public void test_신용카드혜택_수정_오류_중복된기간() {
    	String psCardBenefitId = "3";
    	String psPgCd = "PG_SERVICE.KCP";
    	String cardBenefitTp = "CARD_BENEFIT_INFO_TP.BILLING_DISCNT";
    	String startDt = "2020-10-21";
    	String endDt = "2020-12-31";
    	String title = "test_신용카드혜택_수정";
    	String information = "test_신용카드혜택_수정 본문";

    	// given
    	PolicyPaymentCardBenefitDto dto = new PolicyPaymentCardBenefitDto();
    	dto.setPsCardBenefitId(psCardBenefitId);
    	dto.setPsPgCd(psPgCd);
    	dto.setCardBenefitTp(cardBenefitTp);
    	dto.setStartDt(startDt);
    	dto.setEndDt(endDt);
    	dto.setTitle(title);
    	dto.setInformation(information);
    	log.info("dto: {}", dto);

    	// when
    	MessageCommEnum msgEnum = policyPaymentCardBenefitService.putPolicyPaymentCardBenefit(dto);

    	// then
    	assertTrue(PolicyEnums.Payment.DUPLICATE_TERM.equals(msgEnum));
    }
    @Test
    public void test_신용카드혜택_삭제() {

    	// given
    	Long psCardBenefitId = (long) 2;
    	log.info("psCardBenefitId: {}", psCardBenefitId);

    	// when
    	int count = policyPaymentCardBenefitService.delPolicyPaymentCardBenefit(psCardBenefitId);

    	// then
    	assertEquals(1, count);
    }
}
