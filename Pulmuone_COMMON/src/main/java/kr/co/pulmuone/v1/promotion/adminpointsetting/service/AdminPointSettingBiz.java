package kr.co.pulmuone.v1.promotion.adminpointsetting.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.AdminPointSettingRequestDto;

public interface AdminPointSettingBiz {
	/**
	 * 관리자 적립금 한도 설정 조회
	 */
	ApiResult<?> getAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception;

	/**
	 * 역할그룹 조회
	 */
	ApiResult<?> getRoleGroupList(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception;

	/**
	 * 관리자 적립금 한도 설정 이력 조회
	 */
	ApiResult<?> getAdminPointSettingList(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception;


	/**
	 * 관리자 적립금 지급 한도 설정 상세 조회
	 */
	ApiResult<?> getAdminPointSettingDetail(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception;

	/**
	 * 관리자 적립금 지급 한도 설정 등록
	 */
	ApiResult<?> addAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception;

	/**
	 * 관리자 적립금 지급 한도 설정 수정
	 */
	ApiResult<?> putAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception;

	/**
	 * 관리자 적립금 지급 한도 설정 삭제
	 */
	ApiResult<?> removeAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception;
}
