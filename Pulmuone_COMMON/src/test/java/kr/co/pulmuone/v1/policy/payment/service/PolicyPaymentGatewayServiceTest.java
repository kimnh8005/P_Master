package kr.co.pulmuone.v1.policy.payment.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentGatewayDto;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentGatewayMethodVo;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentGatewayVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class PolicyPaymentGatewayServiceTest extends CommonServiceTestBaseForJunit5 {


	@Autowired
    private PolicyPaymentGatewayService policyPaymentGatewayService;

	@Test
	public void test_PG설정_기본설정정보_결과있음()  {
		// given

    	// when
		PolicyPaymentGatewayDto result = policyPaymentGatewayService.getPolicyPaymentGatewayList();

		// then
        assertNotNull(result.getRows());
	}
    @Test
    public void test_PG설정_PG사별_이중화_비율_결과있음()  {
    	// given

        // when
    	PolicyPaymentGatewayDto result = policyPaymentGatewayService.getPolicyPaymentGatewayRatioList();

    	// then
        assertNotNull(result.getRows());
    }

    @Test
    public void test_PG설정_저장() {
    	// given
    	PolicyPaymentGatewayDto dto = new PolicyPaymentGatewayDto();
    	List<PolicyPaymentGatewayVo> policyPaymentGatewayList = new ArrayList<PolicyPaymentGatewayVo>();
    	dto.setPolicyPaymentGatewayList(policyPaymentGatewayList);
    	ApiResult<?> result = policyPaymentGatewayService.putPolicyPaymentGateway(dto);

    	assertFalse(result.getCode().equals(ApiResult.fail().getCode()));

    	PolicyPaymentGatewayVo vo = new PolicyPaymentGatewayVo();
		vo.setPsPgCd("PG_SERVICE.KCP");
		vo.setAutomaticDepositUrl("www.KCP.com");
		vo.setDepositScheduled("5");
		policyPaymentGatewayList.add(vo);

		vo = new PolicyPaymentGatewayVo();
		vo.setPsPgCd("PG_SERVICE.INICIS");
		vo.setAutomaticDepositUrl("www.INICIS.com");
		vo.setDepositScheduled("6");
		policyPaymentGatewayList.add(vo);

		List<PolicyPaymentGatewayVo> rows = new ArrayList<PolicyPaymentGatewayVo>();
		dto.setRows(rows);

		PolicyPaymentGatewayVo row = new PolicyPaymentGatewayVo();
    	row.setPsPayCd("PAY_TP.CARD");
    	row.setUseRatioKcp("70");
    	row.setUseRatioInicis("30");
    	rows.add(row);

    	row = new PolicyPaymentGatewayVo();
    	row.setPsPayCd("PAY_TP.BANK");
    	row.setUseRatioKcp("75");
    	row.setUseRatioInicis("25");
    	rows.add(row);

    	row = new PolicyPaymentGatewayVo();
    	row.setPsPayCd("PAY_TP.VIRTUAL_BANK");
    	row.setUseRatioKcp("80");
    	row.setUseRatioInicis("20");
    	rows.add(row);

    	dto.setPolicyPaymentGatewayList(policyPaymentGatewayList);

    	result = policyPaymentGatewayService.putPolicyPaymentGateway(dto);
    	assertFalse(result.getCode().equals(ApiResult.fail().getCode()));

    	dto.setRows(rows);
    	log.info("dto: {}", dto);
        // when
    	result = policyPaymentGatewayService.putPolicyPaymentGateway(dto);

        // then
    	assertTrue(result.getCode().equals(ApiResult.success().getCode()));
    }


    @Test
    public void test_PG_결제수단별_목록조회() {
    	 // given
    	String psPayCd = "PAY_TP.CARD";
        log.info("psPayCd: {}", psPayCd);

        // when
        PolicyPaymentGatewayDto result = policyPaymentGatewayService.getPolicyPaymentGatewayMethodList(psPayCd);

        // then
        assertNotNull(result.getPolicyPaymentGatewayMethodList());
    }
    @Test
    public void test_PG_결제수단별_사용여부수정() {

    	// given
    	PolicyPaymentGatewayDto dto = new PolicyPaymentGatewayDto();
    	List<PolicyPaymentGatewayMethodVo> policyPaymentGatewayMethodList = new ArrayList<PolicyPaymentGatewayMethodVo>();
    	String[] psPaymentMethodIdArr = {"45","78","54","86"};
    	String[] useYnArr = {"Y","N","N","Y"};
    	for(int i=0;i<psPaymentMethodIdArr.length;i++) {
    		PolicyPaymentGatewayMethodVo vo = new PolicyPaymentGatewayMethodVo();
    		vo.setPsPaymentMethodId(psPaymentMethodIdArr[i]);
    		vo.setUseYn(useYnArr[i]);
    		policyPaymentGatewayMethodList.add(vo);
    	}
    	dto.setPolicyPaymentGatewayMethodList(policyPaymentGatewayMethodList);
    	log.info("dto: {}", dto);

    	// when
    	int count = policyPaymentGatewayService.putPolicyPaymentGatewayMethod(dto);
    	// then
    	assertTrue(count > 0);
    }
}
