package kr.co.pulmuone.v1.api.hitok.service;

import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * 하이톡 API Biz
 * </PRE>
 */
public interface HitokApiBiz {

	/**
	 * 하이톡 반품 주문|취소 입력 ERP API 송신
	 * @param erpApiEnums
	 * @param odClaimId
	 * @return boolean
	 * @throws BaseException
	 */
	boolean addHitokReturnOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType erpApiEnums, long odClaimId) throws BaseException;
	
	
	/**
	 * 하이톡 반품 주문|취소 입력 ERP API 송신 막음 (클레임 완료 처리만)
	 * 추후 하이톡 취소/반품 인터페이스 오픈시 addHitokReturnOrderIfCustordInpByErp 로 변경
	 */
	boolean addHitokReturnOrderClaimComplete(long odClaimId) throws BaseException;
}
