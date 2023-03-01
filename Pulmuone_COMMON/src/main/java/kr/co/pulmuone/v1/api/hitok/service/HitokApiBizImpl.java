package kr.co.pulmuone.v1.api.hitok.service;

import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 하이톡 API BizImpl
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HitokApiBizImpl implements HitokApiBiz {

	@Autowired
	private final HitokApiService hitokApiService;

	/**
	 * 하이톡 반품 주문|취소 입력 ERP API 송신
	 * @param erpApiEnums
	 * @return boolean
	 * @throws BaseException
	 */
	@Override
	public boolean addHitokReturnOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType erpApiEnums, long odClaimId) throws BaseException {

		// 하이톡 일일배송 반품 API 송신
		boolean isSuccess = hitokApiService.sendApiHitokDailyReturnOrder(odClaimId);

		// 재배송이고 반품API연동 성공일때
		if(ErpApiEnums.ErpServiceType.ERP_HITOK_DAILY_DELIVERY_REDELIVERY_ORDER.equals(erpApiEnums) && isSuccess) {

			// 하이톡 일일배송 원 배송일자로 재배송 시 처리
			isSuccess = hitokApiService.sendApiHitokDailyOriginalRedeliveryOrder(odClaimId);

			if(isSuccess)
				// 하이톡 일일배송 재배송 API 송신
				isSuccess = hitokApiService.sendApiHitokDailyRedeliveryOrder(odClaimId);

		}

		return isSuccess;

	}
	
	
	/**
	 * 하이톡 반품 주문|취소 입력 ERP API 송신 막음 (클레임 완료 처리만)
	 * 추후 하이톡 취소/반품 인터페이스 오픈시 addHitokReturnOrderIfCustordInpByErp 로 변경
	 */
	@Override
	public boolean addHitokReturnOrderClaimComplete(long odClaimId) throws BaseException {
		
		// 하이톡 일일배송 클레임 완료처리
		boolean isSuccess = hitokApiService.saveHitokDailyDeliveryReturnOrderCompleteUpdate(odClaimId);
				
		return isSuccess;
		
	}
}