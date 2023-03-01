package kr.co.pulmuone.v1.promotion.pointhistory.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.PointHistoryListRequestDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.vo.PointHistoryVo;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class PointHistoryServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
	PointHistoryService pointHistoryService;

    @Test
    void getPointHistoryList_정상() throws Exception {
    	//given
    	PointHistoryListRequestDto dto = new PointHistoryListRequestDto();

    	//when
    	Page<PointHistoryVo> result = pointHistoryService.getPointHistoryList(dto);

        //then
        Assertions.assertTrue(result.getTotal()> 0);
    }

    @Test
    void getPointHistoryList_조회내역정상() throws Exception {
    	//given
    	PointHistoryListRequestDto dto = new PointHistoryListRequestDto();
    	dto.setSearchPaymentType("POINT_PAYMENT_TP");

    	//when
    	Page<PointHistoryVo> result = pointHistoryService.getPointHistoryList(dto);

        //then
        Assertions.assertFalse(result.getTotal()> 0);
    }

    @Test
    void getPointHistoryList_조회내역실패() throws Exception {
    	//given
    	PointHistoryListRequestDto dto = new PointHistoryListRequestDto();
    	dto.setSearchPaymentType("POINT_PAYMENT_TP");
    	dto.setPointUsedMsg("-1");
    	//when
    	Page<PointHistoryVo> result = pointHistoryService.getPointHistoryList(dto);

        //then
    	assertFalse( CollectionUtils.isNotEmpty(result.getResult()) );
    }
}
