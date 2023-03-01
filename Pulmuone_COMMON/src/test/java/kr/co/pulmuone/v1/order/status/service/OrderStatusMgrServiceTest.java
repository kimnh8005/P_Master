package kr.co.pulmuone.v1.order.status.service;

import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.order.status.dto.*;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusActionVo;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusGoodsTypeVo;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusTypeActionVo;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

@Slf4j
class OrderStatusMgrServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    OrderStatusMgrService orderStatusMgrService;

    @Test
    void 주문_상태_조회() throws Exception {

    	// Request
    	OrderStatusSearchRequestDto orderStatusSearchRequestDto = OrderStatusSearchRequestDto.builder()
    																						.useYn("Y")
    																						.build();

    	// result
    	OrderStatusSearchResponseDto orderStatusSearchResponseDto = orderStatusMgrService.getSearchCode(orderStatusSearchRequestDto);
    	List<OrderStatusVo> orderStatusList = orderStatusSearchResponseDto.getRows();

    	orderStatusList.stream().forEach(
            i -> log.info("주문_상태_조회 result : {}",  i)
        );

    	// equals
    	Assertions.assertTrue(orderStatusList.size() > 0);
    }

    @Test
    void 주문_상태_목록_조회() throws Exception {

    	// Request

    	// result
    	OrderStatusListResponseDto orderStatusSearchResponseDto = orderStatusMgrService.getOrderStatusList();
    	List<OrderStatusVo> orderStatusSearchList = orderStatusSearchResponseDto.getRows();

    	orderStatusSearchList.stream().forEach(
			i -> log.info("주문_상태_목록_조회 result : {}",  i)
		);

    	// equals
    	Assertions.assertTrue(orderStatusSearchList.size() > 0);
    }

    @Test
    void 주문_상태_등록() throws Exception {

    	// Request
    	OrderStatusVo orderStatusVo = new OrderStatusVo();
    	orderStatusVo.setStatusCd("TT");
    	orderStatusVo.setStatusNm("테스트상태");
    	orderStatusVo.setStatusSort(99);
    	orderStatusVo.setIfDayChangeYn("N");
    	orderStatusVo.setDeliverySearchYn("N");
    	orderStatusVo.setUseYn("Y");
    	orderStatusVo.setSearchGrp("test");

    	// result
    	int insertCnt = orderStatusMgrService.addOrderStatus(orderStatusVo);

		log.info("주문_상태_등록 insertCnt : {}",  insertCnt);

    	// equals
    	Assertions.assertTrue(insertCnt > 0);
    }

    @Test
    void 주문_상태_수정() {

    	// Request
    	OrderStatusRequestDto orderStatusRequestDto = new OrderStatusRequestDto();
    	orderStatusRequestDto.setStatusCd("IC");
    	orderStatusRequestDto.setStatusNm("테스트상태");
    	orderStatusRequestDto.setStatusSort(99);
    	orderStatusRequestDto.setIfDayChangeYn("N");
    	orderStatusRequestDto.setDeliverySearchYn("N");
    	orderStatusRequestDto.setUseYn("Y");
    	orderStatusRequestDto.setSearchGrp("TEST");

    	// result
    	int updateCnt = orderStatusMgrService.putOrderStatus(orderStatusRequestDto);

    	log.info("주문_상태_수정 updateCnt : {}",  updateCnt);

    	// equals
    	Assertions.assertTrue(updateCnt > 0);
    }

    @Test
    void 주문_상태_상세_조회() throws Exception {

    	// Request
    	String statusCd = OrderEnums.OrderStatus.CANCEL_APPLY.getCode();

    	// result
    	OrderStatusResponseDto orderStatusResponseDto = orderStatusMgrService.getOrderStatus(statusCd);

    	log.info("주문_상태_상세_조회 result : {}",  orderStatusResponseDto);

    	// equals
    	Assertions.assertTrue(!Objects.isNull(orderStatusResponseDto));
    }

    @Test
    void 주문_상태유형_목록_조회() throws Exception {

    	// Request

    	// result
    	OrderStatusGoodsTypeListResponseDto orderStatusGoodsTypeResult = orderStatusMgrService.getOrderStatusGoodsTypeList();
    	List<OrderStatusGoodsTypeVo> orderStatusGoodsTypeList = orderStatusGoodsTypeResult.getRows();

    	orderStatusGoodsTypeList.stream().forEach(
			i -> log.info("주문_상태_목록_조회 result : {}",  i)
		);

    	// equals
    	Assertions.assertTrue(orderStatusGoodsTypeList.size() > 0);
    }

    @Test
    void 주문_상태유형_상세_조회() throws Exception {

    	// Request
    	String typeCd = "BOS";

    	// result
    	OrderStatusGoodsTypeResponseDto orderStatusGoodstypeResponse = orderStatusMgrService.getOrderStatusGoodsType(typeCd);
    	OrderStatusGoodsTypeVo orderStatusGoodsType = orderStatusGoodstypeResponse.getRows();

    	log.info("주문_상태유형_상세_조회 result : {}",  orderStatusGoodsType);

    	// equals
    	Assertions.assertTrue(!Objects.isNull(orderStatusGoodsType));
    }

    @Test
    void 주문_상태유형_등록() throws Exception {

    	// Request
    	OrderStatusGoodsTypeVo orderStatusGoodsTypeVo = new OrderStatusGoodsTypeVo();
    	orderStatusGoodsTypeVo.setUseType("TT");
    	orderStatusGoodsTypeVo.setTypeCd("BOS");
    	orderStatusGoodsTypeVo.setTypeNm("BOS");

    	// result
    	int insertCnt = orderStatusMgrService.addOrderStatusGoodsType(orderStatusGoodsTypeVo);

    	log.info("주문_상태유형_등록 result : {}",  insertCnt);

    	// equals
    	Assertions.assertTrue(insertCnt > 0);
    }

    @Test
    void 주문_상태유형_수정() throws Exception {

    	// Request
    	String typeNm = "BOS";
    	String typeCd = "BOS";
    	String useType = "BOS";

    	// result
    	int updateCnt = orderStatusMgrService.putOrderStatusGoodsType(typeNm, typeCd, useType);

    	log.info("주문_상태유형_수정 result : {}",  updateCnt);

    	// equals
    	Assertions.assertTrue(updateCnt > 0);
    }

    @Test
    void 주문_상태_중복_조회() throws Exception {

    	// Request
    	String statusCd = OrderEnums.OrderStatus.CANCEL_APPLY.getCode();

    	// result
    	int dupCnt = orderStatusMgrService.hasOrderStatusDuplicate(statusCd);

    	log.info("주문_상태_중복_조회 result : {}",  dupCnt);

    	// equals
    	Assertions.assertTrue(dupCnt > 0);
    }

    @Test
    void 주문_상태_유형_중복_조회() throws Exception {

    	// Request
    	String typeCd = "BOS";

    	// result
    	int dupCnt = orderStatusMgrService.hasOrderStatusGoodsTypeDuplicate(typeCd);

    	log.info("주문_상태_유형_중복_조회 result : {}",  dupCnt);

    	// equals
    	Assertions.assertTrue(dupCnt > 0);
    }

    @Test
    void 주문_상태_실행_목록_조회() throws Exception {

    	// Request
    	BaseRequestPageDto baseRequestPageDto = new BaseRequestPageDto();

    	// result
    	OrderStatusActionListResponseDto orderStatusActionListResponse = orderStatusMgrService.getOrderStatusActionList(baseRequestPageDto);
    	List<OrderStatusActionVo> orderStatusActionList = orderStatusActionListResponse.getRows();

    	orderStatusActionList.stream().forEach(
			i -> log.info("주문_상태_실행_목록_조회 result : {}",  i)
		);

    	// equals
    	Assertions.assertTrue(orderStatusActionList.size() > 0);
    }

    @Test
    void 주문_상태_실행_상세_조회() throws Exception {

    	// Request
    	long actionId = 1;

    	// result
    	OrderStatusActionResponseDto orderStatusActionResponse = orderStatusMgrService.getOrderStatusAction(actionId);
    	OrderStatusActionVo orderStatusAction = orderStatusActionResponse.getRows();

    	log.info("주문_상태_실행_상세_조회 result : {}",  orderStatusAction);

    	// equals
    	Assertions.assertTrue(!Objects.isNull(orderStatusAction));
    }

    @Test
    void 주문_상태_실행_등록() throws Exception {

    	// Request
    	OrderStatusActionVo orderStatusActionVo = new OrderStatusActionVo();
    	orderStatusActionVo.setActionType("BOS");
    	orderStatusActionVo.setActionExecId("test");
    	orderStatusActionVo.setActionExecNm("테스트");
    	orderStatusActionVo.setActionTarget("selfLink");
    	orderStatusActionVo.setActionTargetUrl("/test");
    	orderStatusActionVo.setActionConfirm("테스트입니다");

    	// result
    	int insertCnt = orderStatusMgrService.addOrderStatusAction(orderStatusActionVo);

    	log.info("주문_상태_실행_등록 result : {}",  insertCnt);

    	// equals
    	Assertions.assertTrue(insertCnt > 0);
    }

    @Test
    void 주문_상태_실행_중복_조회() throws Exception {

    	// Request
    	String actionExecId = "orderCancel";

    	// result
    	int dupCnt = orderStatusMgrService.hasOrderStatusActionDuplicate(actionExecId);

    	log.info("주문_상태_실행_중복_조회 result : {}",  dupCnt);

    	// equals
    	Assertions.assertTrue(dupCnt > 0);
    }

    @Test
    void 주문_상태_실행_수정() throws Exception {

    	// Request
    	OrderStatusActionVo orderStatusActionVo = new OrderStatusActionVo();
    	orderStatusActionVo.setActionId(1L);
    	orderStatusActionVo.setActionType("BOS");
    	orderStatusActionVo.setActionExecId("test");
    	orderStatusActionVo.setActionExecNm("테스트");
    	orderStatusActionVo.setActionTarget("selfLink");
    	orderStatusActionVo.setActionTargetUrl("/test");
    	orderStatusActionVo.setActionConfirm("테스트입니다");

    	// result
    	int updateCnt = orderStatusMgrService.putOrderStatusAction(orderStatusActionVo);

    	log.info("주문_상태_실행_수정 result : {}",  updateCnt);

    	// equals
    	Assertions.assertTrue(updateCnt > 0);
    }

    @Test
    void 주문_유형별_상태_실행관리_목록_조회() throws Exception {

    	// Request
    	OrderStatusTypeActionSearchRequestDto orderStatusTypeActionSearchRequestDto = new OrderStatusTypeActionSearchRequestDto();

    	// result
    	OrderStatusTypeActionListResponseDto orderStatusTypeActionResponse = orderStatusMgrService.getOrderStatusTypeActionList(orderStatusTypeActionSearchRequestDto);
    	List<OrderStatusTypeActionVo> orderStatusTypeActionList = orderStatusTypeActionResponse.getRows();

    	orderStatusTypeActionList.stream().forEach(
			i -> log.info("주문_유형별_상태_실행관리_목록_조회 result : {}",  i)
		);

    	// equals
    	Assertions.assertTrue(orderStatusTypeActionList.size() > 0);
    }

    @Test
    void 주문_유형별_상태_실행관리_삭제() throws Exception {

    	// Request
    	long actionSeq = 185;
    	String useType = "BOS";

    	// result
    	int deleteCnt = orderStatusMgrService.delOrderStatusTypeAction(actionSeq, useType);

    	log.info("주문_유형별_상태_실행관리_삭제 result : {}",  deleteCnt);

    	// equals
    	Assertions.assertTrue(deleteCnt > 0);
    }

    @Test
    void 주문_상태_실행ID_정보_조회() throws Exception {

    	// Request

    	// result
    	GetCodeListResponseDto getCodeListResponse = orderStatusMgrService.getOrderStatusActionIdList();
    	List<GetCodeListResultVo> getCodeList = getCodeListResponse.getRows();

    	getCodeList.stream().forEach(
			i -> log.info("주문_상태_실행ID_정보_조회 result : {}",  i)
		);

    	// equals
    	Assertions.assertTrue(getCodeList.size() > 0);
    }

    @Test
    void 주문_상태_유형_노출영역_조회() throws Exception {

    	// Request

    	// result
    	GetCodeListResponseDto getCodeListResponse = orderStatusMgrService.getOrderStatusGoodsTypeUseTypeList();
    	List<GetCodeListResultVo> getCodeList = getCodeListResponse.getRows();

    	getCodeList.stream().forEach(
			i -> log.info("주문_상태_유형_노출영역_조회 result : {}",  i)
		);

    	// equals
    	Assertions.assertTrue(getCodeList.size() > 0);
    }

    @Test
    void 주문_상태_유형ID_정보_조회() throws Exception {

    	// Request

    	// result
    	GetCodeListResponseDto getCodeListResponse = orderStatusMgrService.getOrderStatusGoodsTypeTypeCdList();
    	List<GetCodeListResultVo> getCodeList = getCodeListResponse.getRows();

    	getCodeList.stream().forEach(
			i -> log.info("주문_상태_유형ID_정보_조회 result : {}",  i)
		);

    	// equals
    	Assertions.assertTrue(getCodeList.size() > 0);
    }

    @Test
    void 주문_유형별_상태일행관리_등록() {

    	// Request
    	OrderStatusTypeActionRequestDto orderStatusTypeActionRequestDto = new OrderStatusTypeActionRequestDto();
    	orderStatusTypeActionRequestDto.setStatusCd("TT");
    	orderStatusTypeActionRequestDto.setUseType("BOS");
    	orderStatusTypeActionRequestDto.setActionNm("테스트");
		orderStatusTypeActionRequestDto.setTypeCd("TEST");

    	// result
    	int insertCnt = orderStatusMgrService.addOrderStatusTypeAction(orderStatusTypeActionRequestDto);

    	log.info("주문_유형별_상태일행관리_등록 result : {}",  insertCnt);

    	// equals
    	Assertions.assertTrue(insertCnt > 0);
    }

    @Test
    void 주문_유형별_상태실행관리_중복_조회() throws Exception {

    	// Request
    	String statusCd = "BF";
    	String useType = "BOS";
    	String typeCd = "BOS";
    	long actionId = 17;
		String actionNm = "반품";

    	// result
    	int dupCnt = orderStatusMgrService.hasOrderStatusTypeActionDuplicate(statusCd, useType, typeCd, actionId, actionNm);

    	log.info("주문_유형별_상태실행관리_중복_조회 result : {}",  dupCnt);

    	// equals
    	Assertions.assertTrue(dupCnt > 0);
    }

    @Test
    void 주문_유형별_상태실행관리_노출상태_수정() throws Exception {

    	// Request
    	OrderStatusDisplayRequestDto orderStatusDisplayRequestDto = new OrderStatusDisplayRequestDto();
    	orderStatusDisplayRequestDto.setTypeCd("BOS");
    	orderStatusDisplayRequestDto.setStatusCd("BF");
    	orderStatusDisplayRequestDto.setUseType("BOS");
    	orderStatusDisplayRequestDto.setActionStatusNm("테스트");

    	// result
    	int updateCnt = orderStatusMgrService.putStatusNmOrderStatusDisplay(orderStatusDisplayRequestDto);

    	log.info("주문_유형별_상태실행관리_노출상태_수정 result : {}",  updateCnt);

    	// equals
    	Assertions.assertTrue(updateCnt > 0);
    }

    @Test
    void 주문_상태_FRONT_BOS_ACTION_JSON_수정() throws Exception {

    	// Request
    	String useType = "FRONT";

    	// result
    	int updateCnt = orderStatusMgrService.putJsonOrderStatus(useType);

    	log.info("주문_상태_FRONT_BOS_ACTION_JSON_수정 result : {}",  updateCnt);

    	// equals
    	Assertions.assertTrue(updateCnt > 0);
    }
}