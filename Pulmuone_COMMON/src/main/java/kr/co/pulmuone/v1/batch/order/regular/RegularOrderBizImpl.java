package kr.co.pulmuone.v1.batch.order.regular;

import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.comm.util.SystemUtil;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultCreateOrderListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultPaymentListDto;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularResultVo;
import kr.co.pulmuone.v1.order.regular.service.OrderRegularOrderCreateBiz;
import kr.co.pulmuone.v1.order.regular.service.OrderRegularPaymentBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 배치 BizImpl
 * </PRE>
 */
@Slf4j
@Service
public class RegularOrderBizImpl implements RegularOrderBiz {

	@Autowired
	private OrderRegularOrderCreateBiz orderRegularOrderCreateBiz;

	@Autowired
	private RegularOrderService regularOrderService;

	@Autowired
	private OrderRegularPaymentBiz orderRegularPaymentBiz;

	/**
	 * 정기배송 주문 생성
	 */
	@Override
	public void putRegularOrderResult() throws Exception {

		// 1. 정기배송 신청 테이블 상태 APPLY 인것 중
		//    정기배송 결과 테이블 상태 APPLY 인것 중에 주문서 생성 예정일자가 배치생성일자와 동일한 건, 회차완료 여부가 N 인건, 주문생성여부가 N 인건 중
		//    정기배송 결과 상세 테이블 상태 APPLY 인것 목록 조회
		List<RegularResultCreateOrderListDto> regularOrderResultCreateGoodsList = orderRegularOrderCreateBiz.getRegularOrderResultCreateGoodsList(0);

		// 정기배송주문결과PK, 출고처 별 grouping
		Map<Long, Map<String, List<RegularResultCreateOrderListDto>>> resultMap = regularOrderResultCreateGoodsList.stream()
				.filter(data -> data.getParentIlGoodsId() == 0)
				.collect(Collectors.groupingBy(RegularResultCreateOrderListDto::getOdRegularResultId, LinkedHashMap::new,
						Collectors.groupingBy(RegularResultCreateOrderListDto::getGrpWarehouseShippingTmplId, LinkedHashMap::new, Collectors.toList())));

		// 정기배송결과PK 주문상세 forEach
		resultMap.entrySet().forEach(entry -> {

			List<RegularResultCreateOrderListDto> regularGoodsList = regularOrderResultCreateGoodsList.stream().filter(data -> data.getOdRegularResultId() == entry.getKey()).collect(Collectors.toList());

			// 정기배송 주문 생성 처리
			try {
				orderRegularOrderCreateBiz.createOrderRegular(regularGoodsList, entry.getKey(), entry.getValue());
			}
			catch(Exception e) {
				e.printStackTrace();
				log.debug("putRegularOrderResult ERROR :: <{}>", e.getMessage());
			}
		});

		// 해당 일자의 정기배송 결과 상세 테이블의 상품이 모두 SKIP 인 경우 회차 완료 처리
		regularOrderService.putOrderRegularResultReqRoundYn(OrderEnums.RegularDetlStatusCd.APPLY.getCode());
	}

	/**
	 * 정기배송 주문 결제
	 */
	@Override
	public void putRegularOrderResultPayment() throws Exception {

		// 1. 정기배송 신청 테이블 상태 해지가 아닌 것 중 인것 중
		//	    정기배송 결과 테이블 상태 해지가 아닌 것 중에 결제 예정일자가 배치 실행 일자 -1 과 배치 실행일자 사이인 건, 회차완료 여부가 N 인건, 주문생성여부가 Y 인건 조회
		//    결제 카드정보 조회
		List<RegularResultPaymentListDto> regularResultPaymentList = orderRegularPaymentBiz.getRegularOrderResultPaymentList();
		if(!regularResultPaymentList.isEmpty()) {
			for(RegularResultPaymentListDto regularResultPaymentItem : regularResultPaymentList) {

				try {
					OrderRegularResultVo orderRegularResultVo = orderRegularPaymentBiz.procOdRegularPayment(regularResultPaymentItem);
					OrderPaymentMasterVo orderPaymentMasterVo = orderRegularResultVo.getOrderPaymentMasterVo();
					if(ObjectUtils.isNotEmpty(orderPaymentMasterVo)) {
						regularOrderService.putOrderPaymentMasterInfo(orderPaymentMasterVo);
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				log.debug("putRegularOrderResultPayment ERROR :: <{}>", e.getMessage());
				}
			}
		}
	}
}