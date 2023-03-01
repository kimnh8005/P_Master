package kr.co.pulmuone.v1.order.claim.service;

import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderStatus;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimStatusInfoDto;

import java.util.ArrayList;
import java.util.List;

public class ClaimUtilStatusService {

	/**
	 * 주문상태 코드에 따라 클레임 상태 코드 및 명 구하기
	 * @param orderStatus
	 * @param returnsYn
	 * @return
	 * @throws Exception
	 */
	public List<OrderClaimStatusInfoDto> getClaimStatusList(String orderStatus) {
		List<OrderClaimStatusInfoDto> claimStatusList = new ArrayList<>();


		// 주문상태가 결재완료 일 때
		if (OrderStatus.INCOM_COMPLETE.getCode().equals(orderStatus)) {
			//취소완료
			OrderClaimStatusInfoDto claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.CANCEL_COMPLETE.getCode())
					.claimStatusNm(OrderStatus.CANCEL_COMPLETE.getCodeName())
					.build();
			claimStatusList.add(claimDto);

			//재배송
			claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.EXCHANGE_COMPLETE.getCode())
					.claimStatusNm(OrderStatus.EXCHANGE_COMPLETE.getCodeName())
					.build();
			claimStatusList.add(claimDto);
		}
		// 주문상태가 배송준비중 일 때
		else if (OrderStatus.DELIVERY_READY.getCode().equals(orderStatus)) {
			//취소요청
			OrderClaimStatusInfoDto claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.CANCEL_APPLY.getCode())
					.claimStatusNm(OrderStatus.CANCEL_APPLY.getCodeName())
					.build();
			claimStatusList.add(claimDto);

			//재배송
			claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.EXCHANGE_COMPLETE.getCode())
					.claimStatusNm(OrderStatus.EXCHANGE_COMPLETE.getCodeName())
					.build();
			claimStatusList.add(claimDto);
		}
		// 주문상태가 배송중 일 때
		// 주문상태가 배송완료 일 때
		// 주문상태가 구매확정 일 때
		else if (OrderStatus.DELIVERY_ING.getCode().equals(orderStatus)
				|| OrderStatus.DELIVERY_COMPLETE.getCode().equals(orderStatus)
				|| OrderStatus.BUY_FINALIZED.getCode().equals(orderStatus)) {
			//반품승인
			OrderClaimStatusInfoDto claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.RETURN_ING.getCode())
					.claimStatusNm(OrderStatus.RETURN_ING.getCodeName())
					.build();
			claimStatusList.add(claimDto);

			//반품완료
			claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.RETURN_COMPLETE.getCode())
					.claimStatusNm(OrderStatus.RETURN_COMPLETE.getCodeName())
					.build();
			claimStatusList.add(claimDto);

			//재배송
			claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.EXCHANGE_COMPLETE.getCode())
					.claimStatusNm(OrderStatus.EXCHANGE_COMPLETE.getCodeName())
					.build();
			claimStatusList.add(claimDto);
		}
		// 주문상태가 취소요청 일 때
		else if (OrderStatus.CANCEL_APPLY.getCode().equals(orderStatus)) {
			//취소승인 = 취소완료
			OrderClaimStatusInfoDto claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.CANCEL_COMPLETE.getCode())
					.claimStatusNm(OrderStatus.CANCEL_COMPLETE.getCodeName())
					.build();
			claimStatusList.add(claimDto);

			//취소거부 = 재배송
			claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.CANCEL_DENY_DEFE.getCode())
					.claimStatusNm(OrderStatus.CANCEL_DENY_DEFE.getCodeName())
					.build();
			claimStatusList.add(claimDto);
		}
		// 주문상태가 반품요청 일 때
		else if (OrderStatus.RETURN_APPLY.getCode().equals(orderStatus)) {
			//반품승인
			OrderClaimStatusInfoDto claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.RETURN_ING.getCode())
					.claimStatusNm(OrderStatus.RETURN_ING.getCodeName())
					.build();
			claimStatusList.add(claimDto);

			//반품완료
			claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.RETURN_COMPLETE.getCode())
					.claimStatusNm(OrderStatus.RETURN_COMPLETE.getCodeName())
					.build();
			claimStatusList.add(claimDto);

			//반품거부
			claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.RETURN_DENY_DEFER.getCode())
					.claimStatusNm(OrderStatus.RETURN_DENY_DEFER.getCodeName())
					.build();
			claimStatusList.add(claimDto);
		}
		// 주문상태가 반품승인 일 때
		else if (OrderStatus.RETURN_ING.getCode().equals(orderStatus)) {
			//반품완료
			OrderClaimStatusInfoDto claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.RETURN_COMPLETE.getCode())
					.claimStatusNm(OrderStatus.RETURN_COMPLETE.getCodeName())
					.build();
			claimStatusList.add(claimDto);

			//반품보류
			claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.RETURN_DEFER.getCode())
					.claimStatusNm(OrderStatus.RETURN_DEFER.getCodeName())
					.build();
			claimStatusList.add(claimDto);
		}
		// 주문상태가 반품보류 일 때
		else if (OrderStatus.RETURN_DEFER.getCode().equals(orderStatus)) {
			//반품완료
			OrderClaimStatusInfoDto claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.RETURN_COMPLETE.getCode())
					.claimStatusNm(OrderStatus.RETURN_COMPLETE.getCodeName())
					.build();
			claimStatusList.add(claimDto);

			//배송완료
			claimDto = OrderClaimStatusInfoDto.builder()
					.claimStatusCd(OrderStatus.DELIVERY_COMPLETE.getCode())
					.claimStatusNm(OrderStatus.DELIVERY_COMPLETE.getCodeName())
					.build();
			claimStatusList.add(claimDto);
		}

		return claimStatusList;
	}
}
