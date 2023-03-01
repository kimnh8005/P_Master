package kr.co.pulmuone.v1.policy.shiparea.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.policy.shiparea.dto.PolicyShipareaDto;
import kr.co.pulmuone.v1.policy.shiparea.dto.vo.NonDeliveryAreaInfoVo;

public interface PolicyShipareaBiz {

	/**
	 * 도서산관 목록 조회
	 * @param	PoTypeListRequestDto
	 * @return	GetBackCountryListResponseDto
	 * @throws Exception
	 */
	ApiResult<?> getBackCountryList(PolicyShipareaDto dto);

	/**
	 * 도서산관 등록
	 * @param AddBackCountryRequestDto
	 * @return AddBackCountryResponseDto
	 * @throws Exception
	 */
	ApiResult<?> addBackCountry(PolicyShipareaDto dto);

	/**
	 * 도서산관 수정
	 * @param PoTypeRequestDto
	 * @return PutBackCountryResponseDto
	 * @throws Exception
	 */
	ApiResult<?> putBackCountry(PolicyShipareaDto dto);

	/**
	 * 도서산관 삭제
	 * @param DelBackCountryRequestDto
	 * @return DelBackCountryResponseDto
	 * @throws Exception
	 */
	ApiResult<?> delBackCountry(PolicyShipareaDto dto);

	ApiResult<?> getBackCountry(PolicyShipareaDto dto);

	ExcelDownloadDto getBackCountryExcelList(String zipCodes[]);


	/**
	 * 배송불가 지역 여부
	 *
	 * @param undeliverableType
	 * @param zipCode
	 * @return
	 * @throws Exception
	 */
	boolean isUndeliverableArea(String undeliverableType, String zipCode) throws Exception;

	/**
	 * 배송불가권역 여부
	 *
	 * @param undeliverableTypes
	 * @param zipCode
	 * @return
	 * @throws Exception
	 */
	boolean isNonDeliveryArea(String[] undeliverableTypes, String zipCode) throws Exception;

	/**
	 * 배송불가권역 안내 메시지
	 *
	 * @param undeliverableTypes
	 * @param zipCode
	 * @return
	 * @throws Exception
	 */
	NonDeliveryAreaInfoVo getNonDeliveryAreaInfo(String[] undeliverableTypes, String zipCode) throws Exception;
}
