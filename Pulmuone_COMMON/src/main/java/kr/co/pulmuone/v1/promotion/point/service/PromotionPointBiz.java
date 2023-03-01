package kr.co.pulmuone.v1.promotion.point.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.promotion.point.dto.CommonCheckAddPointValidationByUserResponseDto;
import kr.co.pulmuone.v1.promotion.point.dto.CommonGetPointListByUserRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.GetPointInfoResponseDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointApprovalRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointSettingListRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointSettingMgmRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.vo.GoodsFeedbackPointRewardSettingVo;
import kr.co.pulmuone.v1.promotion.point.dto.vo.PointExpiredForEmailVo;
import kr.co.pulmuone.v1.promotion.point.dto.vo.PointExpiredListForEmailVo;

public interface PromotionPointBiz {

	/**
	 * 적립금 설정 목록 조회
	 */
	ApiResult<?> getPointSettingList(PointSettingListRequestDto pointSettingListRequestDto) throws Exception;

	/**
	 * 적립금 설정 등록
	 */
	ApiResult<?> addPointSetting(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	/**
	 * 적립금 설정 수정
	 */
	ApiResult<?> putPointSetting(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	/**
	 * 적립금 설정 수정
	 */
	ApiResult<?> getPointDetail(PointRequestDto pointRequestDto) throws Exception;


	/**
	 * 적립금 승인상태 변경
	 */
	ApiResult<?> putPointStatus(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;



	/**
	 * 적립금 명 수정
	 * @param pointSettingMgmRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> updatePointName(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	/**
	 * 적립금 관리자지급/차감 발급사유 수정
	 * @param pointSettingMgmRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> updatePointIssueReason(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	/**
	 * 적립금 설정 삭제
	 * @param pointRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> removePoint(PointRequestDto pointRequestDto) throws Exception;


    /**
     * 포인트 조회
     */
	GetPointInfoResponseDto getPointInfo(Long urUserId) throws Exception;

    /**
     * 적립금 정보 목록 조회
     */
    ApiResult<?> getPointListByUser(CommonGetPointListByUserRequestDto dto) throws Exception;

    /**
     * 적립금 소멸예정 목록 조회
     */
    ApiResult<?> getPointExpectExpireList() throws Exception;

    /**
     * 적립금 사용자 적립 검증
     */
    CommonCheckAddPointValidationByUserResponseDto checkPointValidationByUser(Long urUserId, Long pmPointId) throws Exception;

    /**
     * 포인트 조회 - 사용가능
     */
    int getPointUsable(Long urUserId) throws Exception;

    ApiResult<?> getApprovalPointList(PointApprovalRequestDto requestDto);

	ApiResult<?> putCancelRequestApprovalPoint(PointApprovalRequestDto requestDto) throws Exception;

	ApiResult<?> putDisposalApprovalPoint(PointApprovalRequestDto requestDto) throws Exception;

	ApiResult<?> putApprovalProcessPoint(PointApprovalRequestDto requestDto) throws Exception;

    /**
     * 적립금 조회 - 소멸예정 - 적립금 소멸예정 자동메일 발송용
     */
	PointExpiredForEmailVo getPointExpectExpiredForEmail(Long urUserId) throws Exception;

    /**
     * 적립금 소멸예정 목록 조회 - 적립금 소멸예정 자동메일 발송용
     */
	List<PointExpiredListForEmailVo> getPointExpectExpireListForEmail(Long urUserId) throws Exception;

	ApiResult<?> getEventCallPointInfo(PointRequestDto pointRequestDto);

	ApiResult<?> getPointSearchStatus(PointRequestDto pointRequestDto);

	ApiResult<?> putTicketCollectStatus(PointRequestDto pointRequestDto) throws Exception;

	/* 관리자 지급 (지급한도 정보) */
	ApiResult<?> getAdminAmountCheck(PointRequestDto pointRequestDto) throws Exception;

	List<GoodsFeedbackPointRewardSettingVo> getGoodsFeedbackPointRewardSettingList(Long urGroupId);

	// 적립금 지급 목록 엑셀 다운로드
	ExcelDownloadDto getPointPayListExportExcel(PointRequestDto pointRequestDto);

}
