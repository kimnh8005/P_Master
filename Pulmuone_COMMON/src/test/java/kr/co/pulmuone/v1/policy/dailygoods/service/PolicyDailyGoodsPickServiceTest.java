package kr.co.pulmuone.v1.policy.dailygoods.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.policy.dailygoods.dto.PolicyDailyGoodsPickDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class PolicyDailyGoodsPickServiceTest extends CommonServiceTestBaseForJunit5 {


	@Autowired
    private PolicyDailyGoodsPickService policyDailyGoodsPickService;

    @Test
    public void test_일일상품_골라담기_허용여부_목록_조회_결과있음()  {
    	// given
    	String searchType = "goodsName";//goodsId
    	String conditionValue = "일일상품";
    	String pickableYn = "";

    	// given
    	PolicyDailyGoodsPickDto dto = new PolicyDailyGoodsPickDto();
    	dto.setSearchType(searchType);
    	dto.setConditionValue(conditionValue);
    	dto.setPickableYn(pickableYn);
        // when
    	PolicyDailyGoodsPickDto result = policyDailyGoodsPickService.getPolicyDailyGoodsPickList(dto);
    	// then
        assertNotNull(result.getRows());
    }


    @Test
    public void test_일일상품_골라담기_허용여부_수정() {
    	List<Long> goodsIdList = new ArrayList<>();
		goodsIdList.add(175L);
		goodsIdList.add(177L);
		goodsIdList.add(179L);
    	String pickableYn = "";
    	 // given
    	PolicyDailyGoodsPickDto dto = new PolicyDailyGoodsPickDto();
    	dto.setGoodsIdList(goodsIdList);
    	dto.setPickableYn(pickableYn);
        log.info("dto: {}", dto);
        // when
        int result = policyDailyGoodsPickService.putPolicyDailyGoodsPick(dto);

        // then
        assertNotEquals(-1, result);

    }
    @Test
    public void test_일일상품_골라담기_허용여부_목록_조회_엑셀다운로드_결과있음() {
    	// given
    	String searchType = "goodsName";//goodsId
    	String conditionValue = "일일상품";
    	String pickableYn = "";

    	// given
    	PolicyDailyGoodsPickDto dto = new PolicyDailyGoodsPickDto();
    	dto.setSearchType(searchType);
    	dto.setConditionValue(conditionValue);
    	dto.setPickableYn(pickableYn);
        // when
    	ExcelDownloadDto result = policyDailyGoodsPickService.getPolicyDailyGoodsPickListExportExcel(dto);
    	// then
        assertNotNull(result);
    }
}
