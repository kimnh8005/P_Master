package kr.co.pulmuone.v1.batch.order.order;

import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 ERP API 배치 Biz
 * </PRE>
 */

public interface OrderErpBiz {

	/**
	 * 주문|취소 입력 ERP API 송신
	 * @param erpServiceType
	 * @return void
	 * @throws BaseException
	 */
	void addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType erpServiceType) throws BaseException;

	/**
	 * 주문|취소 입력 ERP API 송신 (외부몰 분리)
	 * @param erpServiceType
	 * @param omSellersId
	 * @return void
	 * @throws BaseException
	 */
	void addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType erpServiceType, String omSellersId) throws BaseException;

	/**
	 * 주문|취소 입력 ERP API 송신 (외부몰 분리)
	 * @param erpServiceType
	 * @param omSellersId
	 * @return void
	 * @throws BaseException
	 */
	void addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType erpServiceType, List<String> omSellersId) throws BaseException;

	/**
	 * 기타주문 입력 ERP API 송신
	 * @param erpServiceType
	 * @return void
	 * @throws BaseException
	 */
	void addEtcOrderIfInpByErp(ErpApiEnums.ErpServiceType erpServiceType) throws BaseException;

	/**
	 * 기타주문 입력 ERP API 송신 (외부몰 분리)
	 * @param erpServiceType
	 * @return void
	 * @throws BaseException
	 */
	void addEtcOrderIfInpByErp(ErpApiEnums.ErpServiceType erpServiceType, String omSellersId) throws BaseException;

	/**
	 * 기타주문 입력 ERP API 송신 (외부몰 분리)
	 * @param erpServiceType
	 * @return void
	 * @throws BaseException
	 */
	void addEtcOrderIfInpByErp(ErpApiEnums.ErpServiceType erpServiceType, List<String> omSellersId) throws BaseException;

	/**
	 * 연동 셀러 리스트
	 * @return String
	 * @throws BaseException
	 */
	List<String> getErpOutMallIfSellerList(String erpInterFaceYn) throws BaseException;
}
