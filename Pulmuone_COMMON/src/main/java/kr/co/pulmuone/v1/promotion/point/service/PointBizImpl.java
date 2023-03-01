package kr.co.pulmuone.v1.promotion.point.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.FeedbackEnums;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.promotion.point.dto.DepositPointDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointRefundRequestDto;
import kr.co.pulmuone.v1.promotion.serialnumber.service.SerialNumberBiz;

@Service
public class PointBizImpl implements PointBiz {

    @Autowired
    private PointUseService pointUseService;

    @Autowired
    private SerialNumberBiz serialNumberBiz;


    /**
     * 특정회원 사용 가능 적립금
     * @param urUserId
     * @return
     */
    @Override
    public ApiResult<?> getUserAvailablePoints(Long urUserId){
        return pointUseService.getUserAvailablePoints(urUserId);
    }

    /**
     * 기존 올가 회원 적립금 적립
     * @param urUserId
     * @param amount
     * @param previusOrgaUserId
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> depositOrgaTransferPoints(Long urUserId, Long amount, String previusOrgaUserId) throws Exception{
        return pointUseService.depositOrgaTransferPoints(urUserId, amount, previusOrgaUserId);
    }

    /**
     * 기존 풀무원 회원 적립금 적립
     * @param urUserId
     * @param amount
     * @param previusPmoUserId
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> depositPreviousPulmuoneMemberPoints(Long urUserId, Long amount, String previusPmoUserId) throws Exception{
        return pointUseService.depositPreviousPulmuoneMemberPoints(urUserId, amount, previusPmoUserId);
    }

    /**
     * 상품 후기 적립금 적립
     * @param urUserId
     * @param urGroupId
     * @param feedbackType
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> goodsFeedbackPointReward(Long urUserId, Long urGroupId, FeedbackEnums.FeedbackType feedbackType) throws Exception{
        return pointUseService.goodsFeedbackPointReward(urUserId,urGroupId,feedbackType);
    }

    /**
     * 적립 기준일로 예약된 후기 적립금 적립
     * @param depositDate
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> depositReservationGoodsFeedbackPoint(String depositDate) throws Exception {

        if(StringUtil.isEmpty(depositDate)){
            depositDate = DateUtil.getCurrentDate();
        }
        return pointUseService.depositReservationGoodsFeedbackPoint(depositDate);

    }

    /**
     * 관리자 포인트 지급/차감 적용
     * @param pmPointId
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> applyAdminPoint(Long pmPointId) throws Exception{
        return pointUseService.applyAdminPoint(pmPointId);
    }

    /**
     * 이벤트 적립금 적립
     * @param urUserId
     * @param pmPointId
     * @param evEventId
     * @param refNo2
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> depositEventPoint(Long urUserId, Long pmPointId, Long evEventId, String refNo2) throws Exception {
        return pointUseService.depositEventPoint(urUserId, pmPointId, evEventId, refNo2);
    }

    /**
     * 추천인 적립금 적립
     * @param urUserId
     * @param pmPointId
     * @param recommenderId
     * @param refNo2
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> depositRecommendationPoint(Long urUserId, Long pmPointId, Long recommenderId, String refNo2) throws Exception{
        return pointUseService.depositRecommendationPoint(urUserId, pmPointId, recommenderId, refNo2);
    }

    /**
     * 적립금 적립
     * @param depositPointDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> depositPoints(DepositPointDto depositPointDto) {
        return pointUseService.depositPoints(depositPointDto);
    }


    /**
     * 시리얼 번호로 적립금 적립
     * @param urUserId
     * @param pointSerialNumber
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> depositPointsBySerialNumber(Long urUserId, String pointSerialNumber) throws Exception {

        ApiResult depositPointResult = pointUseService.depositPointsBySerialNumber(urUserId, pointSerialNumber);

        if (PointEnums.PointUseMessage.ERROR_REDEEM_SERIAL_NUMBER.getCode() == depositPointResult.getCode()) {
            throw new BaseException(PointEnums.PointUseMessage.ERROR_REDEEM_SERIAL_NUMBER);
        }

        return depositPointResult;

    }


    /**
     * 적립금 사용
     * @param depositPointDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> redeemPoint(DepositPointDto depositPointDto) throws Exception {

        ApiResult validationResult = pointUseService.validateMinusPointRequest(depositPointDto);

        if (BaseEnums.Default.SUCCESS.getCode() != validationResult.getCode())
            return ApiResult.result(false, validationResult.getMessageEnum());

        return pointUseService.minusPoint(depositPointDto);

    }

    /**
     * 적립금 환불
     * @param pointRefundRequestDto
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> refundPoint(PointRefundRequestDto pointRefundRequestDto) throws Exception {
        return pointUseService.refundPoint(pointRefundRequestDto);
    }

    /**
     * 적립금 만료
     * @param expiredDate
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> expirePoint(String expiredDate) throws Exception {

        if(StringUtil.isEmpty(expiredDate)){
            expiredDate = DateUtil.getCurrentDate();
        }
        return pointUseService.expirePoint(expiredDate);

    }

    /**
     * 탈퇴회원 적립금 만료
     * @param urUserId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> expireWithdrawalMemberPoint(Long urUserId) throws Exception {

        return pointUseService.expireWithdrawalMemberPoint(urUserId);
    }

    /**
     * 환불 적립금 만료로 즉시 소멸
     * @param pointRefundRequestDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> expireImmediateRefundPoint(PointRefundRequestDto pointRefundRequestDto) {
        return pointUseService.expireImmediateRefundPoint(pointRefundRequestDto);
    }


    /**
     * 단건 미지급건 재적립 처리
     * @param pmPointNotIssueId
     * @param urUserId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> depositNotIssuePoints(Long pmPointNotIssueId, Long urUserId) throws Exception{
        return pointUseService.depositNotIssuePoints(pmPointNotIssueId, urUserId);
    }

    /**
     * 적립금 만료
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> depositRefundOrderNotIssuePoints() throws Exception {

        return (ApiResult<Boolean>) pointUseService.depositRefundOrderNotIssuePoints();

    }

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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> depositCsRefundOrderPoint(Long urUserId, String orderNo, Long amount, Boolean isCsRoleManager, String finOrganizationCode, Integer csPointValidityDay) throws Exception {
        return pointUseService.depositCsRefundOrderPoint(urUserId, orderNo, amount, isCsRoleManager, finOrganizationCode, csPointValidityDay);
    }
}
