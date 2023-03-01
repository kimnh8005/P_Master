package kr.co.pulmuone.v1.outmall.ezadmin.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import kr.co.pulmuone.v1.comm.enums.EZAdminEnums;
import kr.co.pulmuone.v1.comm.enums.OutmallEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingPriceVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderVo;
import kr.co.pulmuone.v1.order.registration.dto.*;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.stock.dto.StockOrderRequestDto;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockOrderBiz;
import kr.co.pulmuone.v1.order.order.dto.StockCheckOrderDetailDto;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.registration.service.OrderBindBiz;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationBiz;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusUpdateRequestDto;
import kr.co.pulmuone.v1.order.status.service.OrderStatusBiz;
import kr.co.pulmuone.v1.outmall.ezadmin.dto.EZAdminOrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class EZAdminOrderRegistrationBizImpl implements EZAdminOrderRegistrationBiz {

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
	private EZAdminOrderCreateBiz ezAdminOrderCreateBiz;

	private static final int BATCH_END_MINUTE = 28;	// 배치강제종료 분

    /**
     * 배치 대상 조회
     * @param ifEasyadminInfoId
     * @return
     */
    @Override
    public Map<String,Integer> setBindOrderOrder(long ifEasyadminInfoId, LocalDateTime startTime) throws Exception {
		Map<String,Integer> orderCreateResult = new HashMap<>();

		// IF_EASYADMIN_INFO_REQ_DATA_ID 리스트 조회
		List<Long> reqDataIdList = eazdminOrderRegistrasionService.getIfEasyadminInfoReqDataIdList(ifEasyadminInfoId);
		int orderCreateCnt = 0;

		if(CollectionUtils.isNotEmpty(reqDataIdList)){
			for(Long reqDataId : reqDataIdList){

				// 이지어드민 주문 성공 정보테이블에서 주문생성여부 N만 조회
				List<EZAdminOrderDto> list = eazdminOrderRegistrasionService.getEZAdminOrderCreateTargetList(ifEasyadminInfoId, reqDataId);
				if(!list.isEmpty()) {

					// COLLECTION_MALL_ID별 그룹핑
					Map<String, Map<String, List<EZAdminOrderDto>>> shippingZoneMap = list.stream()
							.collect(
									groupingBy(EZAdminOrderDto::getCollectionMallId, LinkedHashMap::new,     		// 1. 합포번호별 그룹
											groupingBy(EZAdminOrderDto::getGrpShippingPrice, LinkedHashMap::new,    // 2. 배송정책별 BUNDLE_GRP
													toList()
											))
							);

					for(String key : shippingZoneMap.keySet()){
						Map<String, List<EZAdminOrderDto>> shippingPriceMap = shippingZoneMap.get(key);

						try{
							// 주문데이터 바인딩 및 주문생성
							orderCreateCnt += ezAdminOrderCreateBiz.orderDataBindAndCreateOrder(shippingPriceMap, ifEasyadminInfoId, reqDataId);

						}catch(BaseException baseException){
							// 주문생성실패시 내역 저장
							log.error(" EZAdminOrderRegistrationBizImpl setBindOrderOrder error ==== ::{}", shippingPriceMap);
							failCreateOrder((List<OrderBindDto>)baseException.getReturnObj(), ifEasyadminInfoId, baseException.getMessage());
						}catch(Exception e){
							// 주문성공PK로 실패내역 저장
							log.error(" OutmallOrderRegistrationBizImpl setBindOrderOrder error ==== ::{}", shippingPriceMap);
							String failMessage = OutmallEnums.orderCreateMsg.ORDER_BIND_FAIL.getMessage();
							failCreateOrderByEZAdminOrderDtoList(shippingPriceMap,failMessage);
						}

						// 다음 배치시간에 맞춰 강제종료 강제종료 로직 제거
//						LocalDateTime nowTime = LocalDateTime.now();
//						long batchExecTime = ChronoUnit.SECONDS.between(startTime, nowTime);
//						if(batchExecTime >= 60 * BATCH_END_MINUTE ){ // 배치강제종료 28분
//							orderCreateResult.put(EZAdminEnums.EZAdminSyncCd.SYNC_CD_ING.getCode(),orderCreateCnt);
//							return orderCreateResult;
//						}
					}
				}
			}
		}

		orderCreateResult.put(EZAdminEnums.EZAdminSyncCd.SYNC_CD_END.getCode(),orderCreateCnt);
		return orderCreateResult;
    }

    @Override
    public List<OrderBindDto> orderDataBind(Map<String, List<EZAdminOrderDto>> shippingPriceMap) throws Exception{
		List<OrderBindDto> orderBindList = orderBindEZAdminCreateBizImpl.orderDataBind(shippingPriceMap);
		return orderBindList;
	}

	@Override
	public void failCreateOrder(List<OrderBindDto> orderBindList, long ifEasyadminInfoId, String failMessage) throws Exception{
		List<Map<String, Object>> resultFailList = new ArrayList<>();

		if(CollectionUtils.isNotEmpty(orderBindList)){
			for(OrderBindDto item: orderBindList){

				if(CollectionUtils.isNotEmpty(item.getCollectionMalllFailList())){
					for(Map<String, Object> failItem: item.getCollectionMalllFailList()){

						Map<String, Object> failMap = new HashMap<>();
						failMap.put("succId", failItem.get("succId"));
						failMap.put("failMessage", failItem.get("failMessage"));
						resultFailList.add(failMap);
					}
				}


				List<OrderBindShippingZoneDto> orderShippingZoneList = item.getOrderShippingZoneList().stream().collect(Collectors.toList());
				for (OrderBindShippingZoneDto shippingZoneItem : orderShippingZoneList) {
					OrderShippingZoneVo orderShippingZoneVo = shippingZoneItem.getOrderShippingZoneVo();
					List<OrderBindShippingPriceDto> shippingPriceList = shippingZoneItem.getShippingPriceList().stream().collect(Collectors.toList());

					for (OrderBindShippingPriceDto shippingPriceItem : shippingPriceList) {

						OrderShippingPriceVo orderShippingPriceVo = shippingPriceItem.getOrderShippingPriceVo();
						List<OrderBindOrderDetlDto> orderDetlList = (shippingPriceItem.getOrderDetlList().stream().collect(Collectors.toList()));

						for (OrderBindOrderDetlDto orderDetlItem : orderDetlList) {
							OrderDetlVo orderDetlVo = orderDetlItem.getOrderDetlVo();
							boolean addFailFlag = true;

							for (Map<String, Object> failMap : resultFailList){
								if (StringUtil.nvl(failMap.get("succId"), "").equals(orderDetlVo.getIfOutmallExcelSuccId())){
									addFailFlag = false;
									break;
								}
							}

							if (addFailFlag == true){

								Map<String, Object> failMap = new HashMap<>();
								failMap.put("succId", orderDetlVo.getIfOutmallExcelSuccId());
								failMap.put("failMessage", failMessage);
								resultFailList.add(failMap);
							}
						}
					}
				}

			}

			resultFailList = resultFailList.stream().distinct().sorted((o1, o2) -> o1.get("succId").toString().compareTo(o2.get("succId").toString())).collect(Collectors.toList());

			// 실패데이터  추가
			eazdminOrderRegistrasionService.addEzadminCreateOrderFailSelectInsert(resultFailList);
			// 성공데이터 삭제
			long ifEasyadminOrderSuccDetlId = StringUtil.nvlLong(resultFailList.get(0).get("succId"));
			eazdminOrderRegistrasionService.delEzadminCreateOrderSuccess(ifEasyadminOrderSuccDetlId);
		}

	}

	private void failCreateOrderByEZAdminOrderDtoList(Map<String, List<EZAdminOrderDto>> shippingPriceMap,String failMessage){

    	try{
			List<EZAdminOrderDto> ezAdminOrderDtoList = shippingPriceMap.entrySet().iterator().next().getValue();

			if(CollectionUtils.isNotEmpty(ezAdminOrderDtoList)){
				eazdminOrderRegistrasionService.failCreateOrderByEZAdminOrderDtoList(ezAdminOrderDtoList, failMessage);
			}
		}catch(Exception e){
			log.error(" OutmallOrderRegistrationBizImpl failCreateOrderByEZAdminOrderDtoList error ==== ::{}", shippingPriceMap);
			e.printStackTrace();
		}
	}

	@Override
	public void failCreateOrderByEZAdminOrderDtoList(List<EZAdminOrderDto> ezAdminOrderDtoList,String failMessage){
		eazdminOrderRegistrasionService.failCreateOrderByEZAdminOrderDtoList(ezAdminOrderDtoList,failMessage);
	}

}
