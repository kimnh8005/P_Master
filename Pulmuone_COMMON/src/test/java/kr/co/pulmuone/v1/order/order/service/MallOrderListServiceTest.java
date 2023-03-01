package kr.co.pulmuone.v1.order.order.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderListDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderListRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
class MallOrderListServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    MallOrderListService mallOrderListService;

    @Test
    void 주문_배송_목록_조회() {

    	// Request
    	MallOrderListRequestDto mallOrderListRequestDto = new MallOrderListRequestDto();
    	mallOrderListRequestDto.setUrUserId(1647338L);
		mallOrderListRequestDto.setStartDate("2021-07-01");
		mallOrderListRequestDto.setEndDate("2021-08-20");

    	// result
    	Page<MallOrderListDto> mallOrderResult = mallOrderListService.getOrderList(mallOrderListRequestDto);
    	List<MallOrderListDto> mallOrderList = mallOrderResult.getResult();

    	mallOrderList.forEach(
            i -> log.info("주문_배송_목록_조회  result : {}",  i)
        );

    	// equals
    	Assertions.assertTrue(mallOrderList.size() > 0);
    }

    @Test
    void 주문_취소_반품_목록_조회() {

    	// Request
    	MallOrderListRequestDto mallOrderListRequestDto = new MallOrderListRequestDto();
    	mallOrderListRequestDto.setUrUserId(1647338L);
		mallOrderListRequestDto.setStartDate("2021-07-01");
		mallOrderListRequestDto.setEndDate("2021-08-20");

    	// result
    	Page<MallOrderListDto> mallOrderResult = mallOrderListService.getOrderClaimList(mallOrderListRequestDto);
    	List<MallOrderListDto> mallOrderCancelList = mallOrderResult.getResult();

    	mallOrderCancelList.forEach(
            i -> log.info("주문_취소_반품_목록_조회  result : {}",  i)
        );

    	// equals
    	Assertions.assertTrue(mallOrderCancelList.size() > 0);
    }

}