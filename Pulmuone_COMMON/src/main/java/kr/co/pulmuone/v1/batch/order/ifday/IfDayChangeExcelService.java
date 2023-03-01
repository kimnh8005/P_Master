package kr.co.pulmuone.v1.batch.order.ifday;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OutmallEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.ifday.IfDayChangeExcelOrderMapper;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayChangeDto;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelFailVo;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelInfoVo;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelSuccessVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IfDayChangeExcelService {

    private final IfDayChangeExcelOrderMapper ifDayChangeExcelOrderMapper;

    @Autowired
    private GoodsGoodsBiz goodsGoodsBiz;

    @Autowired
    private IfDayChangeExcelProcessService ifDayChangeExcelProcessService;

    protected void putIfDayChange() throws Exception {
        List<IfDayChangeDto> list = ifDayChangeExcelOrderMapper.getIfDayChangeExcelTargetList(OutmallEnums.OutmallBatchStatusCd.READY.getCode());

        int updateCount = 0;
        String orderStatusCd = "";
        String orderChangeTp = "";
        IfDayExcelInfoVo ifDayExcelInfoVo = new IfDayExcelInfoVo();
        for(IfDayChangeDto item : list) {

            log.info("---------------------------------------- I/F 일자 변경 상태 업데이트 처리 [" + item.getIfIfDayExcelInfoId() + "] ING");
            ifDayExcelInfoVo.setIfIfDayExcelInfoId(item.getIfIfDayExcelInfoId());
            ifDayExcelInfoVo.setBatchStatusCd(OutmallEnums.OutmallBatchStatusCd.ING.getCode());
            LocalDateTime startTime = LocalDateTime.now();
            ifDayExcelInfoVo.setBatchStartDateTime(startTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));

            ifDayChangeExcelOrderMapper.putIfDayChangeExcelInfo(ifDayExcelInfoVo);


            //updateCount = 0;
            List<IfDayExcelSuccessVo> ifDayChangeSuccExcelTargetList = ifDayChangeExcelOrderMapper.getIfDayChangeSuccExcelTargetList(item.getIfIfDayExcelInfoId());
            for(IfDayExcelSuccessVo succItem : ifDayChangeSuccExcelTargetList) {
                // I/F 일자 조회 및 체크로직

				succItem.setOrderIfDt(null);
				succItem.setShippingDt(null);
				succItem.setDeliveryDt(null);
				succItem.setFailMessage("");
				succItem.setSuccess(true);
				if (succItem.getIlGoodsId() != null){
					// 주문상태 체크 결제완료 만 변경 가능
					// 배송준비중이지만 산지직송(주문변경타입이 "배송준비중 변경","사용안함")인 경우 변경 가능
					orderStatusCd = succItem.getOrderStatusCd();
					orderChangeTp = succItem.getOrderChangeTp();
					if (OrderEnums.OrderStatus.INCOM_COMPLETE.getCode().equals(orderStatusCd) ||
							(OrderEnums.OrderStatus.DELIVERY_READY.getCode().equals(orderStatusCd) &&
									orderChangeTp.matches(ErpApiEnums.OrderChangeTp.ORDER_CHANGE.getCode()+"|"
											+ErpApiEnums.OrderChangeTp.NOT_USE.getCode()))){

						// 개별 상품별(주문상세 상품별) 도착예정일 리스트 조회
						List<ArrivalScheduledDateDto> scheduledList = goodsGoodsBiz.getArrivalScheduledDateDtoList(
								succItem.getUrWarehouseId(),			// 출고처ID(출고처PK)
								succItem.getIlGoodsId(),				// 상품ID(상품PK)
								succItem.getIsDawnDelivery(),			// 새벽배송여부 (true/false)
								succItem.getOrderCnt(),					// 주문수량
								succItem.getGoodsCycleTp());



						ArrivalScheduledDateDto scheduledDateDto = goodsGoodsBiz.getArrivalScheduledDateDtoByOrderDate(scheduledList, LocalDate.parse(succItem.getIfDay(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
						if (scheduledDateDto != null) {
							LocalDate orderIfDt		= scheduledDateDto.getOrderDate();
							LocalDate shippingDt	= scheduledDateDto.getForwardingScheduledDate();
							LocalDate deliveryDt	= scheduledDateDto.getArrivalScheduledDate();

							succItem.setOrderIfDt(orderIfDt);
							succItem.setShippingDt(shippingDt);
							succItem.setDeliveryDt(deliveryDt);
							/*
							try {
								// 기존 도착일 재고 수정
								ApiResult<?> originStockRes = goodsStockOrderBiz.putOrderStockByOdOrderDetlId(succItem.getOdOrderDetlId(), "N");
								if (!originStockRes.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
									//기존 도착일 재고 실패시
								}

								// I/F 일자 업데이트
								ifDayChangeExcelOrderMapper.putIfDayChange(succItem.getOdid(), succItem.getOdOrderDetlSeq(), orderIfDt, shippingDt, deliveryDt);

								// 변경 도착일 재고 수정
								ApiResult<?> changeStockRes = goodsStockOrderBiz.putOrderStockByOdOrderDetlId(succItem.getOdOrderDetlId(), "Y");
								if (!changeStockRes.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
									// 변경 도착일 재고 실패시
								}

								updateCount++;
							} catch (Exception e){
								log.error(e.getMessage());
							}
							*/
						} else {
							// 엑셀에 등록한 데이터로 배송 가능일자 없음
							succItem.setSuccess(false);
							succItem.setFailMessage(ExcelUploadValidateEnums.ValidateType.IF_DAY_NOT_DELIVERY.getMessage());
						}
					} else {
						// 실패 : 결제완료 상태만 변경가능합니다.
						succItem.setSuccess(false);
						succItem.setFailMessage(ExcelUploadValidateEnums.ValidateType.IF_CHANGE_ORDER_IC.getMessage());
					}
            	} else {
            		// 실패 :주문건 없음
					succItem.setSuccess(false);
					succItem.setFailMessage(ExcelUploadValidateEnums.ValidateType.GOODS_ID_ERROR.getMessage());
            	}
            }


			List<IfDayExcelSuccessVo> successVoList = new ArrayList<>();
			List<IfDayExcelFailVo> failVoList = new ArrayList<>();
			Map<String, String> checkFailMap = new HashMap<>();
			for (IfDayExcelSuccessVo dto : ifDayChangeSuccExcelTargetList) {
				if (!dto.isSuccess()) {
					if(!checkFailMap.containsKey(dto.getOdid())){
						checkFailMap.put(dto.getOdid(), dto.getOdid());
					}
				}
			}

			for (IfDayExcelSuccessVo dto : ifDayChangeSuccExcelTargetList) {
				if (checkFailMap.containsKey(dto.getOdid())) {
					failVoList.add(new IfDayExcelFailVo(dto));
				} else {
					successVoList.add(dto);
				}
			}

			// ODID기준 그룹핑
			Map<String, List<IfDayExcelSuccessVo>> resultMap = successVoList.stream().collect(Collectors.groupingBy(IfDayExcelSuccessVo::getOdid, LinkedHashMap::new,Collectors.toList()));

			for(String odid : resultMap.keySet()){
				List<IfDayExcelSuccessVo> succItemList = resultMap.get(odid);
				try {
					// 성공내역 저장
					ifDayChangeExcelProcessService.putIfDay(succItemList);
					updateCount += succItemList.size();

				}catch(BaseException be){
					be.printStackTrace();
					// 실패내역 저장
					for(IfDayExcelSuccessVo vo : succItemList){
						vo.setFailMessage(be.getMessageEnum().getMessage());
						failVoList.add(new IfDayExcelFailVo(vo));
					}
				} catch (Exception e){
					e.printStackTrace();
					// 실패내역 저장
					for(IfDayExcelSuccessVo vo : succItemList){
						vo.setFailMessage(e.getMessage());
						failVoList.add(new IfDayExcelFailVo(vo));
					}
				}

			}

			// 실패내역 저장
			for (IfDayExcelFailVo failItem : failVoList){
				//outmallOrderService.addOutMallExcelFailSelectInsert(ifOutmallExcelInfoId, StringUtil.nvlLong(failItem.get("succId")), StringUtil.nvl(failItem.get("failMessage")));
				//outmallOrderService.deleteOutMallExcelSuccess(StringUtil.nvlLong(failItem.get("succId")));
				failItem.setIfIfDayExcelInfoId(ifDayExcelInfoVo.getIfIfDayExcelInfoId());
				failItem.setFailType(OutmallEnums.OutmallDownloadType.BATCH.getCode());
				ifDayChangeExcelOrderMapper.addIfDayExcelFail(failItem);
			}





            log.info("---------------------------------------- I/F 일자 변경 상태 업데이트 처리 [" + item.getIfIfDayExcelInfoId() + "] END");

            LocalDateTime endTime = LocalDateTime.now();
            ifDayExcelInfoVo.setBatchEndDateTime(endTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
            ifDayExcelInfoVo.setBatchExecutionTime(String.valueOf(ChronoUnit.SECONDS.between(startTime, endTime)));
            ifDayExcelInfoVo.setBatchStatusCd(OutmallEnums.OutmallBatchStatusCd.END.getCode());
            ifDayExcelInfoVo.setUpdateCount(updateCount);
            ifDayChangeExcelOrderMapper.putIfDayChangeExcelInfo(ifDayExcelInfoVo);
        }
    }
}