package kr.co.pulmuone.v1.promotion.point.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.FeedbackEnums;
import kr.co.pulmuone.v1.promotion.point.dto.DepositPointDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointRefundRequestDto;

public interface PointBiz {

	/**
	 * 회원 가용 적립금 조회
	 * @param urUserId
	 * @return
	 */
	ApiResult<?> getUserAvailablePoints(Long urUserId);

    /**
     * 기존 올가 회원 적립금 적립
     * @param urUserId
     * @param amount
     * @param previusOrgaUserId
     * @return
     * @throws Exception
     */
    ApiResult<?> depositOrgaTransferPoints(Long urUserId, Long amount, String previusOrgaUserId) throws Exception;

    /**
     * 기존 풀무원 회원 적립금 적립
     * @param urUserId
     * @param amount
     * @param previusPmoUserId
     * @return
     * @throws Exception
     */
	ApiResult<?> depositPreviousPulmuoneMemberPoints(Long urUserId, Long amount, String previusPmoUserId) throws Exception;

    /**
     * 상품 후기 적립금 적립
     * @param urUserId
     * @param urGroupId
     * @param feedbackType
     * @return
     */
	ApiResult<?> goodsFeedbackPointReward(Long urUserId, Long urGroupId, FeedbackEnums.FeedbackType feedbackType) throws Exception;

    /**
     * 적립 기준일로 예약된 후기 적립금 적립
     * @param depositDate
     * @return
     * @throws Exception
     */
    ApiResult<Boolean> depositReservationGoodsFeedbackPoint(String depositDate) throws Exception;

    /**
     * 관리자 포인트 지급/차감 적용
     * @param pmPointId
     * @return
     * @throws Exception
     */
    ApiResult<Boolean> applyAdminPoint(Long pmPointId) throws Exception;

    /**
     * 이벤트 적립금 적립
     * @param urUserId
     * @param pmPointId
     * @param evEventId
     * @param refNo2
     * @return
     * @throws Exception
     */
    ApiResult<?> depositEventPoint(Long urUserId, Long pmPointId, Long evEventId, String refNo2) throws Exception;

    /**
     * 추천인 적립금 적립
     * @param urUserId
     * @param pmPointId
     * @param recommenderId
     * @param refNo2
     * @return
     * @throws Exception
     */
    ApiResult<?> depositRecommendationPoint(Long urUserId, Long pmPointId, Long recommenderId, String refNo2) throws Exception;

	/**
     * 적립금 적립 처리
     * @param depositPointDto
     * @return
     */
    ApiResult<?> depositPoints(DepositPointDto depositPointDto) throws Exception;


    /**
     * 적립금 적립 처리 - 시리얼코드
     * @param urUserId
     * @param pointSerialNumber
     * @return
     */
    ApiResult<?> depositPointsBySerialNumber(Long urUserId, String pointSerialNumber) throws Exception;

    /**
     * 적립금 사용
     * */
    ApiResult<Boolean> redeemPoint(DepositPointDto depositPointDto) throws Exception;


    /**
     * 적립금 환불
     * */
    ApiResult<?> refundPoint(PointRefundRequestDto pointRefundRequestDto) throws Exception;


    /**
     * 적립금 소멸
     * @param expiredDate
     * @return
     * @throws Exception
     */
    ApiResult<Boolean> expirePoint(String expiredDate) throws Exception;

    /**
     * 탈퇴회원 적립금 만료
     * @param urUserId
     * @return
     */
    ApiResult<Boolean> expireWithdrawalMemberPoint(Long urUserId) throws Exception;

    /**
     * 환불 적립금 만료로 즉시 소멸
     * @param pointRefundRequestDto
     * @return
     */
    ApiResult<?> expireImmediateRefundPoint(PointRefundRequestDto pointRefundRequestDto);

    /**
     * 단건 미지급 적립금 지급처리
     * @param pmPointNotIssueId
     * @param urUserId
     * @return
     * @throws Exception
     */
    ApiResult<?> depositNotIssuePoints(Long pmPointNotIssueId, Long urUserId) throws Exception;

    /**
     * 미적립된 환불 적립금 재적립 처리
     * @return
     * @throws Exception
     */
    ApiResult<Boolean> depositRefundOrderNotIssuePoints() throws Exception;

    /**
     * CS 환불 적립금 적립
     * @param urUserId
     * @param orderNo
     * @param amount
     * @param isCsRoleManager
     * @param finOrganizationCode
     * @param csPointValidityDay
     * @return
     * @throws Exception
     */
    ApiResult<?> depositCsRefundOrderPoint(Long urUserId, String orderNo, Long amount, Boolean isCsRoleManager, String finOrganizationCode, Integer csPointValidityDay) throws Exception;
}
