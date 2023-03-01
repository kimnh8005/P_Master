package kr.co.pulmuone.v1.batch.order.ifday;

import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import kr.co.pulmuone.v1.comm.constants.ApiConstants;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.service.OrderDetailDeliveryBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.ifday.IfDayChangeExcelOrderMapper;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockOrderBiz;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelSuccessVo;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IfDayChangeExcelProcessService {

    private final IfDayChangeExcelOrderMapper ifDayChangeExcelOrderMapper;

	@Autowired
	private GoodsStockOrderBiz goodsStockOrderBiz;

	@Autowired
	private OrderOrderBiz orderOrderBiz;

	@Autowired
	private OrderDetailDeliveryBiz orderDetailDeliveryBiz;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
	public void putIfDay(List<IfDayExcelSuccessVo> succItemList) throws Exception {
    	Long odOrderId = succItemList.stream().findAny().get().getOdOrderId();

    	for(IfDayExcelSuccessVo succItem : succItemList){

			// 처리이력 메세지
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String histMsg = "";
			OrderDetlVo histMsgVo = orderDetailDeliveryBiz.getHistMsgOdOrderDetlId(succItem.getOdOrderDetlId(), ApiConstants.IF_BATCH);
			String prevOrderStatusDeliTpNm = histMsgVo.getOrderStatusDeliTp();
			String prevOrderIfDt = histMsgVo.getOrderIfDt().format(dateFormatter);
			String prevDeliveryDt = histMsgVo.getDeliveryDt().format(dateFormatter);

			// 기존 도착일 재고 수정
			ApiResult<?> originStockRes = goodsStockOrderBiz.putOrderStockByOdOrderDetlId(succItem.getOdOrderDetlId(), "N");
			if (!originStockRes.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
				//기존 도착일 재고 실패시
				throw new BaseException(originStockRes.getMessageEnum());
			}

			// I/F 일자 업데이트
			ifDayChangeExcelOrderMapper.putIfDayChange(succItem.getOdid(), succItem.getOdOrderDetlSeq(), succItem.getOrderIfDt(), succItem.getShippingDt(), succItem.getDeliveryDt());

			// 변경 도착일 재고 수정
			ApiResult<?> changeStockRes = goodsStockOrderBiz.putOrderStockByOdOrderDetlId(succItem.getOdOrderDetlId(), "Y");
			if (!changeStockRes.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
				// 변경 도착일 재고 실패시
				throw new BaseException(changeStockRes.getMessageEnum());
			}

			// 처리 이력 등록
			String orderStatusDetailTypeNm = OrderEnums.OrderStatusDetailType.NORMAL.getCodeName();
			if(succItem.getIsDawnDelivery()){
				orderStatusDetailTypeNm= OrderEnums.OrderStatusDetailType.DAWN.getCodeName();
			}

			// 처리이력 메세지 > {배송방법} 주문I/F : {주문I/F} / 도착예정일 : {도착예정일} → {배송방법} 주문I/F : {주문I/F} / 도착예정일 : {도착예정일}
			histMsg = MessageFormat.format(OrderEnums.OrderDetailStatusHistMsg.DELIVERY_DT_CHANGE_MSG.getMessage(),
					prevOrderStatusDeliTpNm, prevOrderIfDt, prevDeliveryDt,
					orderStatusDetailTypeNm, succItem.getOrderIfDt(), succItem.getDeliveryDt());

			/* 이력 등록 */
			OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
					.odOrderDetlId(succItem.getOdOrderDetlId())
					.statusCd("")
					.histMsg(histMsg)
					.createId(Constants.BATCH_CREATE_USER_ID)
					.build();

			orderDetailDeliveryBiz.putOrderDetailStatusHist(orderDetlHistVo);
		}

		// 일자별 출고처별 출고예정수량 업데이트
		orderOrderBiz.putWarehouseDailyShippingCount(Arrays.asList(odOrderId));

	}

}