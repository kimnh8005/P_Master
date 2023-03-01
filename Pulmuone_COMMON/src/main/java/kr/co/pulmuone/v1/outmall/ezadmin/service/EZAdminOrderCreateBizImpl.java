package kr.co.pulmuone.v1.outmall.ezadmin.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.EZAdminEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OutmallEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.stock.dto.StockOrderRequestDto;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockOrderBiz;
import kr.co.pulmuone.v1.order.order.dto.StockCheckOrderDetailDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingPriceVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.registration.dto.*;
import kr.co.pulmuone.v1.order.registration.service.OrderBindBiz;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationBiz;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusUpdateRequestDto;
import kr.co.pulmuone.v1.order.status.service.OrderStatusBiz;
import kr.co.pulmuone.v1.outmall.ezadmin.dto.EZAdminOrderDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.outmall.order.service.OutmallOrderRegistrationBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class EZAdminOrderCreateBizImpl implements EZAdminOrderCreateBiz {

	@Autowired
	private OrderBindBiz orderBindEZAdminCreateBizImpl;

    @Autowired
    private EZAdminOrderRegistrasionService eazdminOrderRegistrasionService;

    @Autowired
    private OrderRegistrationBiz orderRegistrationBiz;

    @Autowired
    private OrderOrderBiz orderOrderBiz;

    @Autowired
    private GoodsStockOrderBiz goodsStockOrderBiz;

    @Autowired
    private OrderStatusBiz orderStatusBiz;

    @Autowired
	private OutmallOrderRegistrationBiz outmallOrderRegistrationBiz;

    @Autowired
	private EZAdminOrderRegistrationBiz ezAdminOrderRegistrationBiz;


	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class}, readOnly = false)
	public int orderDataBindAndCreateOrder(Map<String, List<EZAdminOrderDto>> shippingPriceMap , Long ifEasyadminInfoId, Long reqDataId) throws Exception{
		int orderCreateCnt = 0;

		// 주문데이터 바인딩
		List<OrderBindDto> orderDataBindResult = ezAdminOrderRegistrationBiz.orderDataBind(shippingPriceMap);

		try{
			if(CollectionUtils.isEmpty(orderDataBindResult)){
				log.error("주문 생성 대상 미존재");
			}else{
				List<OrderBindDto> failCreateOrderBindList = orderDataBindResult.stream().filter(obj -> obj.getOrder().getCreateYn().equals("N")).collect(Collectors.toList());

				if(CollectionUtils.isNotEmpty(failCreateOrderBindList)){
					// 주문데이터 바인딩 실패 -> 실패내역 저장
					ezAdminOrderRegistrationBiz.failCreateOrder(orderDataBindResult, ifEasyadminInfoId, "");
				}else{
					// 성공 -> 주문생성
					orderCreateCnt += createOrder(orderDataBindResult, ifEasyadminInfoId, reqDataId);
				}
			}

		}catch(BaseException baseException){
			// 에러메세지, 바인딩된 주문데이터 객체 return
			throw new BaseException(baseException.getMessageEnum().getMessage(), orderDataBindResult);
		}catch(Exception e){
			// 바인딩된 주문데이터 객체, 에러메세지 return
			throw new BaseException(OutmallEnums.orderCreateMsg.ORDER_CREATE_FAIL.getMessage(), orderDataBindResult);
		}

		return orderCreateCnt;
	}

    private int createOrder(List<OrderBindDto> orderBindList, Long ifEasyadminInfoId, Long reqDataId) throws Exception{
		int orderCreateSuccessCnt = 0;

		List<OrderBindDto> createOrderBindList = orderBindList.stream()
				.filter(obj -> obj.getOrder().getCreateYn().equals("Y"))
				.collect(Collectors.toList());

		orderCreateSuccessCnt += createOrderBindList.size();

		OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(createOrderBindList, "Y");

		if(!OrderEnums.OrderRegistrationResult.SUCCESS.getCode().equals(orderRegistrationResponseDto.getOrderRegistrationResult().getCode())){
			System.out.println("=========== [createOrder] order fail!! 주문생성 오류!! : 뭔가의 이유로 강제 오류진행..  롤백처리!!!");
			throw new Exception();
		}

		String[] odOrderIds = orderRegistrationResponseDto.getOdOrderIds().split(",");

		// 주문 ID 목록 으로 주문 상세 목록 조회
		List<Long> odOrderDetlIds = eazdminOrderRegistrasionService.getOdOrderDetlIds(Arrays.asList(odOrderIds));
		OrderStatusUpdateRequestDto orderStatusUpdateRequest = OrderStatusUpdateRequestDto.builder()
				.detlIdList(odOrderDetlIds)
				.userId(Constants.BATCH_CREATE_USER_ID)
				.statusCd(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode())
				.build();
		// 주문처리이력 등록
		orderStatusBiz.putOrderDetailListStatus(orderStatusUpdateRequest);

		for(String odOrderid:odOrderIds){

			List<StockOrderRequestDto> stockOrderReqDtoList = new ArrayList<StockOrderRequestDto>();
			StockOrderRequestDto stockOrderRequestDto = new StockOrderRequestDto();
			List<StockCheckOrderDetailDto> orderGoodsList = orderOrderBiz.getStockCheckOrderDetailList(StringUtil.nvlLong(odOrderid));
			for (StockCheckOrderDetailDto goods : orderGoodsList) {
				StockOrderRequestDto stockOrderReqDto = new StockOrderRequestDto();
				stockOrderReqDto.setIlGoodsId(goods.getIlGoodsId());
				stockOrderReqDto.setOrderQty(goods.getOrderCnt());
				stockOrderReqDto.setScheduleDt(StringUtil.nvl(goods.getShippingDt(), "2000-01-01"));
				stockOrderReqDto.setOrderYn("Y");
				stockOrderReqDto.setStoreYn("N");
				stockOrderReqDto.setMemo(String.valueOf(goods.getOdOrderDetlId()));
				stockOrderReqDtoList.add(stockOrderReqDto);
			}
			stockOrderRequestDto.setOrderList(stockOrderReqDtoList);
			ApiResult<?> stockRes = goodsStockOrderBiz.stockOrderHandle(stockOrderRequestDto);

			if (!stockRes.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
				throw new BaseException(OutmallEnums.orderCreateMsg.STOCK_FAIL);
			}
		}

        // 출고처 일자별 출고수량 업데이트
        orderOrderBiz.putWarehouseDailyShippingCount(orderRegistrationResponseDto.getOdOrderIds());

		// 이지어드민 주문 성공 정보테이블에 주문생성여부 Y로 업데이트
		// OD_ORDER.COLLECTION_MALL_ID = IF_EASYADMIN_ORDER_SUC.PACK
		eazdminOrderRegistrasionService.putEZAdminOrderSuccOrderCreateYn(Long.parseLong(createOrderBindList.get(0).getOrder().getCollectionMallId()),ifEasyadminInfoId,reqDataId);

		return orderCreateSuccessCnt;
	}

}
