package kr.co.pulmuone.v1.order.delivery.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberDetlDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberDetlListRequestDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberListRequestDto;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderBulkTrackingNumberFailVo;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderBulkTrackingNumberSuccVo;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderBulkTrackingNumberVo;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderBulkTrackingNumberServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
	OrderBulkTrackingNumberService orderBulkTrackingNumberService;

	@Test
	void 주문상세_번호_존재여부_조회() {

    	// Request
		String odid = "21011600001026";
    	int odOrderDetlId = 1;

    	// result
    	int odOrderDetlCnt = orderBulkTrackingNumberService.getOdOrderDetlIdCount(odid, odOrderDetlId);

    	log.info("주문상세_번호_존재여부_조회 odOrderDetlCnt : {}",  odOrderDetlCnt);

    	// equals
    	Assertions.assertTrue(odOrderDetlCnt > 0);
	}

	@Test
	void 택배사_존재여부_조회() {

		// Request
		long logisticsCd = 88;

		// result
		int logisticsCnt = orderBulkTrackingNumberService.getPsShippingCompIdCount(logisticsCd);

		log.info("택배사_존재여부_조회 logisticsCnt : {}",  logisticsCnt);

		// equals
		Assertions.assertTrue(logisticsCnt > 0);
	}

	@Test
	void 일괄_송장정보_등록() {

		// Request
		OrderBulkTrackingNumberVo orderBulkTrackingNumberVo = new OrderBulkTrackingNumberVo();
		orderBulkTrackingNumberVo.setSuccessCnt(10);
		orderBulkTrackingNumberVo.setFailureCnt(0);
		orderBulkTrackingNumberVo.setCreateId(1646893L);
		orderBulkTrackingNumberVo.setOriginNm("원본파일명");

		// result
		int insertCnt = orderBulkTrackingNumberService.addOrderBulkTrackingNumber(orderBulkTrackingNumberVo);

		log.info("일괄_송장정보_등록 insertCnt : {}",  insertCnt);

		// equals
		Assertions.assertTrue(insertCnt > 0);
	}

	@Test
	void 주문상세_송장번호_등록여부_조회() {

		// Request
		long odOrderDetlId = 17;

		// result
		int registCnt = orderBulkTrackingNumberService.getOrderTrackingNumberCnt(odOrderDetlId);

		log.info("주문상세_송장번호_등록여부_조회 registCnt : {}",  registCnt);

		// equals
		Assertions.assertTrue(registCnt > 0);
	}

	@Test
	void 주문상세_송장번호_등록() {

		// Request
		OrderTrackingNumberVo odTrackingNumberVo = OrderTrackingNumberVo.builder()
																		.odOrderDetlId(17L)
																		.psShippingCompId(25L)
																		.trackingNo("123456")
																		.createId(1646893L)
																		.build();

		// result
		int insertCnt = orderBulkTrackingNumberService.addOrderTrackingNumber(odTrackingNumberVo);

		log.info("주문상세_송장번호_등록 insertCnt : {}",  insertCnt);

		// equals
		Assertions.assertTrue(insertCnt > 0);
	}

	@Test
	void 주문상세_송장번호_수정() {

		// Request
		OrderTrackingNumberVo odTrackingNumberVo = OrderTrackingNumberVo.builder()
																		.odOrderDetlId(17L)
																		.psShippingCompId(25L)
																		.trackingNo("123456")
																		.createId(1646893L)
																		.build();

		// result
		int updateCnt = orderBulkTrackingNumberService.putOrderTrackingNumber(odTrackingNumberVo);

		log.info("주문상세_송장번호_수정 updateCnt : {}",  updateCnt);

		// equals
		Assertions.assertTrue(updateCnt > 0);
	}

	@Test
	void 일괄송장_성공내역_테이블_등록() {

		// Request
		OrderBulkTrackingNumberSuccVo odBulkTrackingNumberSuccVo = new OrderBulkTrackingNumberSuccVo();
		odBulkTrackingNumberSuccVo.setOdBulkTrackingNumberId(999L);
		odBulkTrackingNumberSuccVo.setOdOrderDetlId(17L);
		odBulkTrackingNumberSuccVo.setPsShippingCompId(25L);
		odBulkTrackingNumberSuccVo.setTrackingNo("123456");

		// result
		int insertCnt = orderBulkTrackingNumberService.addOrderBulkTrackingNumberSucc(odBulkTrackingNumberSuccVo);

		log.info("일괄송장_성공내역_테이블_등록 insertCnt : {}",  insertCnt);

		// equals
		Assertions.assertTrue(insertCnt > 0);
	}

	@Test
	void 일괄송장_실패내역_테이블_등록() {

		// Request
		OrderBulkTrackingNumberFailVo odBulkTrackingNumberFailVo = new OrderBulkTrackingNumberFailVo();
		odBulkTrackingNumberFailVo.setOdBulkTrackingNumberId(999L);
		odBulkTrackingNumberFailVo.setOdOrderDetlId(17L);
		odBulkTrackingNumberFailVo.setPsShippingCompId(25L);
		odBulkTrackingNumberFailVo.setTrackingNo("123456");

		// result
		int insertCnt = orderBulkTrackingNumberService.addOrderBulkTrackingNumberFail(odBulkTrackingNumberFailVo);

		log.info("일괄송장_실패내역_테이블_등록 insertCnt : {}",  insertCnt);

		// equals
		Assertions.assertTrue(insertCnt > 0);
	}

	@Test
	void 일괄송장_정보_수정() {

		// Request
		OrderBulkTrackingNumberVo odBulkTrackingNumberVo = new OrderBulkTrackingNumberVo();
		odBulkTrackingNumberVo.setSuccessCnt(10);
		odBulkTrackingNumberVo.setFailureCnt(0);
		odBulkTrackingNumberVo.setCreateId(1646893L);
		odBulkTrackingNumberVo.setOriginNm("원본파일명");
		odBulkTrackingNumberVo.setOdBulkTrackingNumberId(12L);

		// result
		int updateCnt = orderBulkTrackingNumberService.putOrderBulkTrackingNumber(odBulkTrackingNumberVo);

		log.info("일괄송장_정보_수정 updateCnt : {}",  updateCnt);

		// equals
		Assertions.assertTrue(updateCnt > 0);
	}

	@Test
	void 일괄송장_입력내역_목록_조회() {

		// Request
		OrderBulkTrackingNumberListRequestDto orderBulkTrackingNumberRequest = new OrderBulkTrackingNumberListRequestDto();

    	// result
		Page<OrderBulkTrackingNumberVo> orderBulkTrackingNumberResult = orderBulkTrackingNumberService.getOrderBulkTrackingNumberList(orderBulkTrackingNumberRequest);
		List<OrderBulkTrackingNumberVo> orderBulkTrackingNumberList = orderBulkTrackingNumberResult.getResult();

		orderBulkTrackingNumberList.stream().forEach(
            i -> log.info("일괄송장_입력내역_목록_조회 result : {}",  i)
        );

        // equals
        Assertions.assertTrue(orderBulkTrackingNumberList.size() > 0);
	}

	@Test
	void 일괄송장_실패내역_목록_조회() {

		// Request
		OrderBulkTrackingNumberListRequestDto orderBulkTrackingNumberResult = new OrderBulkTrackingNumberListRequestDto();
		orderBulkTrackingNumberResult.setOdBulkTrackingNumberId(13L);

		// result
		List<OrderBulkTrackingNumberFailVo> orderBulkTrackingNumberFailResult = orderBulkTrackingNumberService.getOrderBulkTrackingNumberFailList(orderBulkTrackingNumberResult);

		orderBulkTrackingNumberFailResult.forEach(
			i -> log.info("일괄송장_실패내역_목록_조회 result : {}",  i)
		);

		// equals
		Assertions.assertTrue(orderBulkTrackingNumberFailResult.size() > 0);
	}

	@Test
	void 일괄송장_입력내역_상세_목록_조회() {

		// Request
		OrderBulkTrackingNumberDetlListRequestDto orderBulkTrackingNumberDetlListRequest = new OrderBulkTrackingNumberDetlListRequestDto();
		orderBulkTrackingNumberDetlListRequest.setOdBulkTrackingNumberId("12");

		// result
		Page<OrderBulkTrackingNumberDetlDto> orderBulkTrackingNumberDetlResult = orderBulkTrackingNumberService.getOrderBulkTrackingNumberDetlList(orderBulkTrackingNumberDetlListRequest);
		List<OrderBulkTrackingNumberDetlDto> orderBulkTrackingNumberDetlList = orderBulkTrackingNumberDetlResult.getResult();

		orderBulkTrackingNumberDetlList.forEach(
			i -> log.info("일괄송장_입력내역_상세_목록_조회 result : {}",  i)
		);

		// equals
		Assertions.assertTrue(orderBulkTrackingNumberDetlList.size() > 0);
	}

	@Test
	void 일괄송장_입력내역_상세_목록_조회_엑셀다운로드() {

		// Request
		OrderBulkTrackingNumberDetlListRequestDto orderBulkTrackingNumberDetlListRequest = new OrderBulkTrackingNumberDetlListRequestDto();
		orderBulkTrackingNumberDetlListRequest.setOdBulkTrackingNumberId("12");

		// result
		List<OrderBulkTrackingNumberDetlDto> orderBulkTrackingNumberDetlList = orderBulkTrackingNumberService.getOrderBulkTrackingNumberDetlList(orderBulkTrackingNumberDetlListRequest);

		orderBulkTrackingNumberDetlList.forEach(
				i -> log.info("일괄송장_입력내역_상세_목록_조회_엑셀다운로드result : {}",  i)
		);

		// equals
		Assertions.assertTrue(orderBulkTrackingNumberDetlList.size() > 0);
	}
}
