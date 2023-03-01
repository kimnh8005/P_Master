package kr.co.pulmuone.v1.batch.order.calculate;

import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * 정산 배치 Biz
 * </PRE>
 */

public interface CalculateBiz {

	/**
	 * 임직원 정산 일마감
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	void employeeCalculateDayDeadline() throws BaseException;

	/**
	 * 정산을 위한 임직원 정보 저장
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	void addEmployeeCalculateInfo() throws BaseException;

	/**
	 * 매출확정 조회
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	void salesConfirmSearch() throws BaseException;

	/**
	 * 하이톡 일배 매출확정 조회
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	void hitokDaliySalesConfirmSearch() throws BaseException;

	/**
	 * 하이톡 택배 매출확정 조회
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	void hitokSalesConfirmSearch() throws BaseException;

	/**
	 * 임직원 정산 월마감 erp 전송
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	void employeeCalculateMonthDeadlineErpSend() throws Exception;

}
