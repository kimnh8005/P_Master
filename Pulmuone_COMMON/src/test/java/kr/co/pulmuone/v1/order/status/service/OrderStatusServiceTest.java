package kr.co.pulmuone.v1.order.status.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlHistVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlVo;
import kr.co.pulmuone.v1.order.history.util.HistoryMsgUtil;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.v1.order.status.dto.OrderClaimInfoListDto;
import kr.co.pulmuone.v1.order.status.dto.OrderClaimStatusRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusUpdateResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class OrderStatusServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    OrderStatusService orderStatusService;

    @Test
    void 주문상세_상태_변경_이력_등록() throws Exception {

    	// Request
    	OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
    													.statusCd(OrderEnums.OrderStatus.DELIVERY_ING.getCode())
    													.histMsg(HistoryMsgUtil.getHistoryMsg("HIST_001"))
    													.createId(1647264)
    													.odOrderDetlId(1401)
    													.build();

    	// result
    	int insertCnt = orderStatusService.putOrderDetailStatusHist(orderDetlHistVo);

		log.info("주문상세_상태_변경_이력_등록 insertCnt : {}",  insertCnt);

    	// equals
    	Assertions.assertEquals(1, insertCnt);
    }

/*
    @Test
    void 주문_상태_수정() throws Exception {

    	// Request
    	OrderStatusUpdateDto orderDetlVo = new OrderStatusUpdateDto();

    	orderDetlVo.setOdOrderDetlId(1401);
    	orderDetlVo.setOrderStatusCd(OrderEnums.OrderStatus.DELIVERY_ING.getCode());

    	// result
    	int updateCnt = orderStatusService.putOrderDetailStatus(orderDetlVo);

		log.info("주문_상태_수정 updateCnt : {}",  updateCnt);

    	// equals
    	Assertions.assertEquals(1, updateCnt);
    }
*/

    @Test
    void 주문클레임상세_상태_변경_이력_등록() throws Exception {

    	// Request
    	ClaimDetlHistVo claimDetlHistVo = ClaimDetlHistVo.builder()
														.statusCd(OrderEnums.OrderStatus.CANCEL_APPLY.getCode())
														.histMsg(HistoryMsgUtil.getHistoryMsg("HIST_001"))
														.createId(1647264)
														.odOrderDetlId(1401)
														.build();

    	// result
    	int insertCnt = orderStatusService.putClaimDetailStatusHist(claimDetlHistVo);

		log.info("주문클레임상세_상태_변경_이력_등록 insertCnt : {}",  insertCnt);

    	// equals
    	Assertions.assertEquals(1, insertCnt);
    }

    @Test
    void 주문클레임상세_상태_수정() throws Exception {

    	// Request
    	ClaimDetlVo claimDetlVo = ClaimDetlVo.builder()
											.odOrderDetlId(1401)
											.claimStatusCd(OrderEnums.OrderStatus.CANCEL_APPLY.getCode())
											.build();

    	// result
    	int updateCnt = orderStatusService.putClaimDetailStatus(claimDetlVo);

		log.info("주문클레임상세_상태_수정 updateCnt : {}",  updateCnt);

    	// equals
    	Assertions.assertEquals(1, updateCnt);
    }

    @Test
	void 주문_취소요청리스트_일괄_취소완료처리() throws Exception {

		OrderClaimStatusRequestDto orderClaimStatusRequestDto = new OrderClaimStatusRequestDto();
		List<OrderClaimInfoListDto> odClaimInfoList = new ArrayList<>();

		OrderClaimInfoListDto odClaimInfo = new OrderClaimInfoListDto();
		odClaimInfo.setOdOrderId(45163);
		odClaimInfo.setOdOrderDetlId(58946);
		odClaimInfo.setOdClaimId(1176375);
		odClaimInfo.setOdClaimDetlId(16097);
		odClaimInfo.setOdid("21051100045163");
		odClaimInfo.setIlGoodsId(8773);
		odClaimInfo.setTargetTp("B");
		odClaimInfo.setReturnsYn("");
		odClaimInfo.setClaimCnt(2);
		odClaimInfo.setOrderCnt(2);
		odClaimInfo.setCancelCnt(2);
		odClaimInfo.setUrWarehouseId(140);
		odClaimInfoList.add(odClaimInfo);

		odClaimInfo = new OrderClaimInfoListDto();
		odClaimInfo.setOdOrderId(45163);
		odClaimInfo.setOdOrderDetlId(58947);
		odClaimInfo.setOdClaimId(1176375);
		odClaimInfo.setOdClaimDetlId(16098);
		odClaimInfo.setOdid("21051100045163");
		odClaimInfo.setIlGoodsId(900259);
		odClaimInfo.setTargetTp("B");
		odClaimInfo.setReturnsYn("");
		odClaimInfo.setClaimCnt(2);
		odClaimInfo.setOrderCnt(2);
		odClaimInfo.setCancelCnt(2);
		odClaimInfo.setUrWarehouseId(140);
		odClaimInfoList.add(odClaimInfo);

		odClaimInfo = new OrderClaimInfoListDto();
		odClaimInfo.setOdOrderId(45163);
		odClaimInfo.setOdOrderDetlId(58943);
		odClaimInfo.setOdClaimId(1176375);
		odClaimInfo.setOdClaimDetlId(16099);
		odClaimInfo.setOdid("21051100045163");
		odClaimInfo.setIlGoodsId(90018111);
		odClaimInfo.setTargetTp("B");
		odClaimInfo.setReturnsYn("");
		odClaimInfo.setClaimCnt(2);
		odClaimInfo.setOrderCnt(2);
		odClaimInfo.setCancelCnt(2);
		odClaimInfo.setUrWarehouseId(86);
		odClaimInfoList.add(odClaimInfo);

		orderClaimStatusRequestDto.setOdClaimInfoList(odClaimInfoList);

		OrderStatusUpdateResponseDto responseDto = orderStatusService.putClaimCancelReqeustToCancelComplete(orderClaimStatusRequestDto);

		// 기 처리건
		assertTrue(responseDto.getSuccessCount() > 0);
	}
}