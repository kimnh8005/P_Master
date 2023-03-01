package kr.co.pulmuone.v1.promotion.point.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.FeedbackEnums;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.promotion.point.PointUseMapper;
import kr.co.pulmuone.v1.comm.util.EnumUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.promotion.point.dto.*;
import kr.co.pulmuone.v1.promotion.point.dto.vo.*;
import kr.co.pulmuone.v1.promotion.serialnumber.service.SerialNumberBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointUseService {

    @Autowired
    private SerialNumberBiz serialNumberBiz;

    private final PointUseMapper pointUseMapper;
    /**
     * 회원 가용 적립금 조회
     * @param urUserId
     * @return
     */
    protected ApiResult<?> getUserAvailablePoints(Long urUserId){
        //회원번호 번호 확인
        if(urUserId == null || urUserId < 1){
            return ApiResult.result(BaseEnums.CommBase.NEED_LOGIN);
        }

        return ApiResult.success(pointUseMapper.getUserAvailablePoints(urUserId));
    }

    /**
     * 기존 올가 회원 적립금 적립
     * @param urUserId
     * @param amount
     * @param previusPmoUserId
     * @return
     * @throws Exception
     */
    protected ApiResult<?> depositOrgaTransferPoints(Long urUserId, Long amount, String previusPmoUserId) throws Exception{

        //회원번호 번호 확인
        if(Objects.isNull(urUserId) || urUserId < 1){
            return ApiResult.result(false, BaseEnums.CommBase.NEED_LOGIN);
        }

        //적립금 1보다 작고  미존재 시
        if(Objects.isNull(amount) || amount < 1){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //기존 회원아이디 미존재 시
        if(StringUtil.isEmpty(previusPmoUserId)){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //최대 적립 가능액 확인
        Long maximumAccrualAmount = getMaximumAccrualAmount(amount, urUserId);

        //최대 적립 가능 적립금 초과
        if(maximumAccrualAmount == 0L){
            return ApiResult.result(false, PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED);
        }

        DepositPointDto depositPointDto = DepositPointDto.builder()
                .urUserId(urUserId)
                .amount(maximumAccrualAmount)
                .refNo2(previusPmoUserId)
                .pointProcessType(PointEnums.PointProcessType.DEPOSIT_POINT_ORGA_TRANSFER)
                .pointSettlementType(PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .cmnt(Constants.ORGA_TRANSFER_POINTS_COMMENT)
                .build();

        //적립금 적립 유효기간 설정
        PointVo pointInfo = new PointVo();
        pointInfo.setValidityTp(PointEnums.ValidityType.VALIDITY);
        pointInfo.setValidityDay(Constants.PRIVIUS_ORGA_MEMBER_POINT_EXPIRE_DAY);
        pointInfo.setIssueDeptCd(Constants.ORGA_SETTLEMENT_CORPORATION_CODE);

        //적립금 사용 정보 등록
        addPointUsedInfo(depositPointDto, pointInfo);

        //보유 가능 적립금 초과로 부분 적립처리 후 return: 부분 적립 금액, PointUseMessage Enum
        if (amount > maximumAccrualAmount){

            PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto = PointPartialDepositOverLimitDto.builder()
                    .urUserId(urUserId)
                    .maximumAccrualAmount(maximumAccrualAmount)
                    .requestAmount(amount)
                    .pointProcessType(depositPointDto.getPointProcessType())
                    .pointSettlementType(depositPointDto.getPointSettlementType())
                    .expirationDate(getExpirationDate(pointInfo))
                    .issueDeptCd(pointInfo.getIssueDeptCd())
                    .refNo2(depositPointDto.getRefNo2())
                    .build();

            addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);

            return ApiResult.result(maximumAccrualAmount, PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT);
        }

        return ApiResult.success(true);
    }

    /**
     * 기존 풀무원 회원 적립금 적립
     * @param urUserId
     * @param amount
     * @param previusPmoUserId
     * @return
     * @throws Exception
     */
    protected ApiResult<?> depositPreviousPulmuoneMemberPoints(Long urUserId, Long amount, String previusPmoUserId) throws Exception{

        //회원번호 번호 확인
        if(Objects.isNull(urUserId) || urUserId < 1){
            return ApiResult.result(false, BaseEnums.CommBase.NEED_LOGIN);
        }

        //적립금 1보다 작고  미존재 시
        if(Objects.isNull(amount) || amount < 1){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //기존 회원아이디 미존재 시
        if(StringUtil.isEmpty(previusPmoUserId)){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //최대 적립 가능액 확인
        Long maximumAccrualAmount = getMaximumAccrualAmount(amount, urUserId);

        //최대 적립 가능 적립금 초과
        if(maximumAccrualAmount == 0L){
            return ApiResult.result(false, PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED);
        }

        DepositPointDto depositPointDto = DepositPointDto.builder()
                .urUserId(urUserId)
                .amount(maximumAccrualAmount)
                .refNo2(previusPmoUserId)
                .pointProcessType(PointEnums.PointProcessType.DEPOSIT_POINT_PULMUONE_TRANSFER)
                .pointSettlementType(PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .cmnt(Constants.PULMUONE_TRANSFER_POINTS_COMMENT)
                .build();

        //적립금 적립 유효기간 설정
        PointVo pointInfo = new PointVo();
        pointInfo.setValidityTp(PointEnums.ValidityType.VALIDITY);
        pointInfo.setValidityDay(Constants.PRIVIUS_PULMUONE_MEMBER_POINT_EXPIRE_DAY);
        pointInfo.setIssueDeptCd(Constants.PULMUONE_SETTLEMENT_CORPORATION_CODE);

        //적립금 사용 정보 등록
        addPointUsedInfo(depositPointDto, pointInfo);

        //보유 가능 적립금 초과로 부분 적립처리 후 return: 부분 적립 금액, PointUseMessage Enum
        if (amount > maximumAccrualAmount){

            PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto = PointPartialDepositOverLimitDto.builder()
                    .urUserId(urUserId)
                    .maximumAccrualAmount(maximumAccrualAmount)
                    .requestAmount(amount)
                    .pointProcessType(depositPointDto.getPointProcessType())
                    .pointSettlementType(depositPointDto.getPointSettlementType())
                    .expirationDate(getExpirationDate(pointInfo))
                    .issueDeptCd(pointInfo.getIssueDeptCd())
                    .refNo2(depositPointDto.getRefNo2())
                    .build();

            addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);

            return ApiResult.result(maximumAccrualAmount, PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT);
        }

        return ApiResult.success(true);
    }

    /**
     * 한도 초과 부분 적립, 미적립 내역 생성
     * @param pointPartialDepositOverLimitDto
     */
    protected void addPartialDepositOverLimitHistory(PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto) {
        log.info("------------ 보유 가능 적립금 초과로 부분 적립, 미적립 처리 내역 저장");

        pointUseMapper.insertPointNotIssue(pointPartialDepositOverLimitDto);
    }

    /**
     * 상품 후기 적립금 적립
     * @param urUserId
     * @param urGroupId
     * @param feedbackType
     * @return
     */
    protected ApiResult<?> goodsFeedbackPointReward(Long urUserId, Long urGroupId, FeedbackEnums.FeedbackType feedbackType) throws Exception{

        //회원번호 번호 확인
        if(Objects.isNull(urUserId) || urUserId < 1){
            return ApiResult.result(false, BaseEnums.CommBase.NEED_LOGIN);
        }

        //등급, 후기 유형 확인
        if(Objects.isNull(urGroupId) || urGroupId < 1 || feedbackType == null){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //적립금 등급별 리스트 조회 및 최대 적립금 정렬
        List<GoodsFeedbackPointRewardSettingVo> rewardSettingList = pointUseMapper.getGoodsFeedbackPointRewardSettingList(urGroupId);

        if(rewardSettingList.isEmpty()){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        rewardSettingList = sortingRewardSettingList(rewardSettingList, feedbackType);

        //후기 적립금 적립 dto 셋팅
        GoodsFeedbackRewardPointDto goodsFeedbackRewardPointDto = GoodsFeedbackRewardPointDto.builder()
                .goodsFeedbackPointRewardSettingVo(rewardSettingList.get(0))
                .feedbackType(feedbackType)
                .build();

        if(goodsFeedbackRewardPointDto.isDepositPointsImmediately()){
            //최대 적립 가능액 확인
            Long maximumAccrualAmount = getMaximumAccrualAmount(goodsFeedbackRewardPointDto.getAmount(), urUserId);

            //즉시 지급
            DepositPointDto depositPointDto = DepositPointDto.builder()
                    .urUserId(urUserId)
                    .pmPointId(goodsFeedbackRewardPointDto.getPmPointId())
                    .amount(maximumAccrualAmount)
                    .pointPaymentType(PointEnums.PointPayment.PROVISION)
                    .pointSettlementType(goodsFeedbackRewardPointDto.getPointSettlementTp())
                    .pointProcessType(goodsFeedbackRewardPointDto.getPointProcessTp())
                    .refNo1(goodsFeedbackRewardPointDto.getPmPointUserGradeId()+"")
                    .build();

            PointVo pointVo = new PointVo();
            pointVo.setPmPointId(goodsFeedbackRewardPointDto.getPmPointId());
            pointVo.setIssueDeptCd(goodsFeedbackRewardPointDto.getIssueDeptCd());
            pointVo.setValidityTp(PointEnums.ValidityType.PERIOD);
            pointVo.setValidityEndDt(goodsFeedbackRewardPointDto.getExpirationDt());

            PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto = PointPartialDepositOverLimitDto.builder()
                    .urUserId(urUserId)
                    .maximumAccrualAmount(maximumAccrualAmount)
                    .requestAmount(goodsFeedbackRewardPointDto.getAmount())
                    .pointProcessType(depositPointDto.getPointProcessType())
                    .pointSettlementType(depositPointDto.getPointSettlementType())
                    .expirationDate(pointVo.getValidityEndDt())
                    .issueDeptCd(pointVo.getIssueDeptCd())
                    .pmPointId(pointVo.getPmPointId())
                    .refNo1(depositPointDto.getRefNo1())
                    .build();

            //최대 적립 가능 적립금 초과
            if(maximumAccrualAmount == 0L){

                //보유 가능 적립금 초과로 미적립 내역 저장
                addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);

                return ApiResult.result(false, PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED);
            }

            //적립금 적립 처리
            addPointUsedInfo(depositPointDto, pointVo);

            //보유 가능 적립금 초과로 부분 적립처리 후 return: 부분 적립 금액, PointUseMessage Enum
            if (goodsFeedbackRewardPointDto.getAmount() > maximumAccrualAmount){

                addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);

                return ApiResult.result(maximumAccrualAmount, PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT);
            }

            //포인트 적립금액을 반환함 (상품리뷰 등록 이후 적립된 금액을 노출하려는 목적으로 사용)
            return ApiResult.result(maximumAccrualAmount, PointEnums.PointUseMessage.SUCCESS_DEPOSIT_POINT);
        } else {
            //적립금 예약 적립 등록
            PointDepositReservationDto pointDepositReservationDto = PointDepositReservationDto.builder()
                    .pmPointId(goodsFeedbackRewardPointDto.getPmPointId())
                    .pmPointUserGradeId(goodsFeedbackRewardPointDto.getPmPointUserGradeId())
                    .urUserId(urUserId)
                    .amount(goodsFeedbackRewardPointDto.getAmount())
                    .depositDt(goodsFeedbackRewardPointDto.getDepositDt())
                    .expirationDt(goodsFeedbackRewardPointDto.getExpirationDt())
                    .build();

            addPointDepositReservation(pointDepositReservationDto);
        }

        return ApiResult.success(true);
    }

    /**
     * 적립금 예약 적립 등록
     * @param pointDepositReservationDto
     */
    protected void addPointDepositReservation(PointDepositReservationDto pointDepositReservationDto){
        pointUseMapper.addPointDepositReservation(pointDepositReservationDto);
    }


    /**
     * 후기 설정 리스트 정렬
     * @param rewardSettingList
     * @param feedbackType
     * @return
     */
    protected List<GoodsFeedbackPointRewardSettingVo> sortingRewardSettingList(List<GoodsFeedbackPointRewardSettingVo> rewardSettingList, FeedbackEnums.FeedbackType feedbackType){

        if (FeedbackEnums.FeedbackType.NORMAL == feedbackType){
            return rewardSettingList.stream().sorted(Comparator.comparing(GoodsFeedbackPointRewardSettingVo::getNomalAmount).reversed()).collect(toList());
        }

        if (FeedbackEnums.FeedbackType.PHOTO == feedbackType){
            return rewardSettingList.stream().sorted(Comparator.comparing(GoodsFeedbackPointRewardSettingVo::getPhotoAmount).reversed()).collect(toList());
        }

        if (FeedbackEnums.FeedbackType.PREMIUM == feedbackType){
            return rewardSettingList.stream().sorted(Comparator.comparing(GoodsFeedbackPointRewardSettingVo::getPremiumAmount).reversed()).collect(toList());
        }

        return null;
    }

    /**
     * 적립 기준일로 예약된 후기 적립금 적립
     * @param depositDate
     * @return
     */
    protected ApiResult<Boolean> depositReservationGoodsFeedbackPoint(String depositDate){

        //적립금 적립 예약 리스트 조회
        List<PointDepositReservationVo> pointDepositReservationList = pointUseMapper.getPointDepositReservationList(depositDate);

        for (PointDepositReservationVo pointDepositReservationVo : pointDepositReservationList){
            if(!"Y".equals(pointDepositReservationVo.getUseYn())){
                reservationGoodsFeedbackPointStatusChange(pointDepositReservationVo.getDepositReservationId(), PointEnums.PointUseMessage.STOP_DEPOSIT_POINT);
                continue;
            }

            //최대 적립 가능액 확인
            Long maximumAccrualAmount = getMaximumAccrualAmount(pointDepositReservationVo.getAmount(), pointDepositReservationVo.getUrUserId());

            DepositPointDto depositPointDto = DepositPointDto.builder()
                    .urUserId(pointDepositReservationVo.getUrUserId())
                    .pmPointId(pointDepositReservationVo.getPmPointId())
                    .amount(maximumAccrualAmount)
                    .pointPaymentType(PointEnums.PointPayment.PROVISION)
                    .pointSettlementType(pointDepositReservationVo.getPointSettlementTp())
                    .pointProcessType(pointDepositReservationVo.getPointProcessTp())
                    .refNo1(pointDepositReservationVo.getPmPointUserGradeId()+"")
                    .refNo2(pointDepositReservationVo.getDepositReservationId()+"")
                    .build();

            PointVo pointVo = new PointVo();
            pointVo.setPmPointId(pointDepositReservationVo.getPmPointId());
            pointVo.setIssueDeptCd(pointDepositReservationVo.getIssueDeptCd());
            pointVo.setValidityTp(PointEnums.ValidityType.PERIOD);
            pointVo.setValidityEndDt(pointDepositReservationVo.getExpirationDt());

            PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto = PointPartialDepositOverLimitDto.builder()
                    .urUserId(pointDepositReservationVo.getUrUserId())
                    .maximumAccrualAmount(maximumAccrualAmount)
                    .requestAmount(pointDepositReservationVo.getAmount())
                    .pointProcessType(depositPointDto.getPointProcessType())
                    .pointSettlementType(depositPointDto.getPointSettlementType())
                    .expirationDate(pointVo.getValidityEndDt())
                    .issueDeptCd(pointVo.getIssueDeptCd())
                    .pmPointId(pointVo.getPmPointId())
                    .refNo1(depositPointDto.getRefNo1())
                    .refNo2(depositPointDto.getRefNo2())
                    .build();

            //최대 적립 가능 적립금 초과
            if(maximumAccrualAmount == 0L){
                //보유 가능 적립금 초과로 미적립 내역 저장
                addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);

                reservationGoodsFeedbackPointStatusChange(pointDepositReservationVo.getDepositReservationId(), PointEnums.PointUseMessage.EXCEEDS_ISSUE_LIMIT_PER_ACCOUNT);
                continue;
            }

            //적립금 적립 처리
            addPointUsedInfo(depositPointDto, pointVo);

            //보유 가능 적립금 초과로 부분 적립처리 후 return: 부분 적립 금액, PointUseMessage Enum
            if (pointDepositReservationVo.getAmount() > maximumAccrualAmount){
                reservationGoodsFeedbackPointStatusChange(maximumAccrualAmount, PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT);

                addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);
                continue;
            }

            pointUseMapper.putReservationGoodsFeedbackPointStatus(pointDepositReservationVo.getDepositReservationId(), PointEnums.PointUseMessage.SUCCESS_DEPOSIT_POINT);
        }


        return ApiResult.success(true);

    }

    /**
     * 후기 적립금 적립 예약 상태값 변경
     * @param depositReservationId
     * @param pointUseMessage
     */
    protected void reservationGoodsFeedbackPointStatusChange(Long depositReservationId, PointEnums.PointUseMessage pointUseMessage){
        pointUseMapper.putReservationGoodsFeedbackPointStatus(depositReservationId, pointUseMessage);
    }

    /**
     * 관리자 포인트 지급/차감 적용
     * @param pmPointId
     * @return
     * @throws Exception
     */
    protected ApiResult<Boolean> applyAdminPoint(Long pmPointId) throws Exception{
        //적립금 설정 번호 확인
        if(Objects.isNull(pmPointId) || pmPointId < 1){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //관리자 지급/차감 적립금 설정 조회
        PointVo pointInfo = pointUseMapper.getManagerReflectionPointInfo(pmPointId);
        boolean maximumAccrualAmountCheck = false;

        if(pointInfo == null){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        ManagerReflectionPointDto managerReflectionPointDto = ManagerReflectionPointDto.builder()
                .pointVo(pointInfo)
                .build();

        //발급 리스트 조회
        List<PointIssueVo> pointIssueList = pointUseMapper.getPointIssueList(pmPointId);

        if(pointIssueList.isEmpty()){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        if(pointInfo.getPaymentTp() == PointEnums.PointPayment.PROVISION){

            for (PointIssueVo pointIssueVo : pointIssueList){

                //최대 적립 가능액 확인
                Long maximumAccrualAmount = getMaximumAccrualAmount(managerReflectionPointDto.getAmount(), pointIssueVo.getUrUserId());

                //즉시 지급
                DepositPointDto depositPointDto = DepositPointDto.builder()
                        .urUserId(pointIssueVo.getUrUserId())
                        .pmPointId(managerReflectionPointDto.getPmPointId())
                        .amount(maximumAccrualAmount)
                        .pointPaymentType(PointEnums.PointPayment.PROVISION)
                        .pointSettlementType(managerReflectionPointDto.getPointSettlementTp())
                        .pointProcessType(managerReflectionPointDto.getPointProcessTp())
                        .build();

                PointVo pointVo = new PointVo();
                pointVo.setPmPointId(managerReflectionPointDto.getPmPointId());
                pointVo.setIssueDeptCd(managerReflectionPointDto.getIssueDeptCd());
                pointVo.setValidityTp(pointInfo.getValidityTp());
                pointVo.setValidityEndDt(managerReflectionPointDto.getExpirationDt());
                pointVo.setValidityDay(pointInfo.getValidityDay());

                PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto = PointPartialDepositOverLimitDto.builder()
                        .urUserId(pointIssueVo.getUrUserId())
                        .maximumAccrualAmount(maximumAccrualAmount)
                        .requestAmount(managerReflectionPointDto.getAmount())
                        .pointProcessType(depositPointDto.getPointProcessType())
                        .pointSettlementType(depositPointDto.getPointSettlementType())
                        .expirationDate(getExpirationDate(pointVo))
                        .issueDeptCd(pointVo.getIssueDeptCd())
                        .pmPointId(pointVo.getPmPointId())
                        .build();

                //최대 적립 가능 적립금 초과
                if(maximumAccrualAmount == 0L){
                    //보유 가능 적립금 초과로 미적립 내역 저장
                    addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);
                    maximumAccrualAmountCheck = true;
                    continue;
                }

                //적립금 적립 처리
                addPointUsedInfo(depositPointDto, pointVo);

                //보유 가능 적립금 초과로 부분 적립처리 후 return: 부분 적립 금액, PointUseMessage Enum
                if (managerReflectionPointDto.getAmount() > maximumAccrualAmount){

                    addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);
                    maximumAccrualAmountCheck = true;
                    continue;
                }
            }
        } else {

            //적립금 차감 처리
            for (PointIssueVo pointIssueVo : pointIssueList) {
                DepositPointDto depositPointDto = DepositPointDto.builder()
                        .pointPaymentType(PointEnums.PointPayment.DEDUCTION)
                        .pointProcessType(managerReflectionPointDto.getPointProcessTp())
                        .pointSettlementType(managerReflectionPointDto.getPointSettlementTp())
                        .urUserId(pointIssueVo.getUrUserId())
                        .pmPointId(managerReflectionPointDto.getPmPointId())
                        .amount(managerReflectionPointDto.getAmount())
                        .build();

                minusPoint(depositPointDto);
            }
        }

        if(maximumAccrualAmountCheck) {
            return ApiResult.result(true,PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED);
        } else {
            return ApiResult.success(true);
        }
    }

    protected String getExpirationDate(PointVo pointVo) {
        String expirationDate = "";
        if(PointEnums.ValidityType.PERIOD.equals(pointVo.getValidityTp())){
            expirationDate = pointVo.getValidityEndDt();
        }else{
            expirationDate = LocalDate.now().plusDays(pointVo.getValidityDay()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        return expirationDate;
    }

    /**
     * 이벤트 적립금 적립
     * @param urUserId
     * @param pmPointId
     * @param evEventId
     * @param refNo2
     * @return
     */
    protected ApiResult<?> depositEventPoint(Long urUserId, Long pmPointId, Long evEventId, String refNo2) throws Exception{

        //회원번호 번호 확인
        if(Objects.isNull(urUserId) || urUserId < 1){
            return ApiResult.result(false, BaseEnums.CommBase.NEED_LOGIN);
        }

        //적립금 설정 번호 확인
        if(Objects.isNull(pmPointId) || pmPointId < 1){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //이벤트 설정 고유 번호 확인
        if(Objects.isNull(evEventId) || evEventId < 1){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //적립금 설정 조회
        PointVo pointInfo = pointUseMapper.getPmPoint(pmPointId);

        if(pointInfo == null){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //최대 적립 가능액 확인
        Long maximumAccrualAmount = getMaximumAccrualAmount(pointInfo.getIssueVal(), urUserId);

        DepositPointDto depositPointDto = DepositPointDto.builder()
                .urUserId(urUserId)
                .pmPointId(pointInfo.getPmPointId())
                .amount(maximumAccrualAmount)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .pointSettlementType(pointInfo.getPointSettlementTp())
                .pointProcessType(pointInfo.getPointProcessTp())
                .refNo1(evEventId+"")
                .refNo2(refNo2+"")
                .build();

        PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto = PointPartialDepositOverLimitDto.builder()
                .urUserId(urUserId)
                .maximumAccrualAmount(maximumAccrualAmount)
                .requestAmount(pointInfo.getIssueVal())
                .pointProcessType(depositPointDto.getPointProcessType())
                .pointSettlementType(depositPointDto.getPointSettlementType())
                .expirationDate(getExpirationDate(pointInfo))
                .issueDeptCd(pointInfo.getIssueDeptCd())
                .pmPointId(pointInfo.getPmPointId())
                .refNo1(depositPointDto.getRefNo1())
                .refNo2(depositPointDto.getRefNo2())
                .build();

        //최대 적립 가능 적립금 초과
        if(maximumAccrualAmount == 0L){
            //보유 가능 적립금 초과로 미적립 내역 저장
            addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);

            return ApiResult.result(false, PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED);
        }

        //적립금 사용 정보 등록
        addPointUsedInfo(depositPointDto, pointInfo);

        //보유 가능 적립금 초과로 부분 적립처리 후 return: 부분 적립 금액, PointUseMessage Enum
        if (pointInfo.getIssueVal() > maximumAccrualAmount){

            addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);

            return ApiResult.result(maximumAccrualAmount, PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT);
        }

        return ApiResult.success(true);

    }

    /**
     * 추천인 적립금 적립
     * @param urUserId
     * @param pmPointId
     * @param recommenderId
     * @param refNo2
     * @return
     */
    protected ApiResult<?> depositRecommendationPoint(Long urUserId, Long pmPointId, Long recommenderId, String refNo2) throws Exception{

        //회원번호 번호 확인
        if(Objects.isNull(urUserId) || urUserId < 1){
            return ApiResult.result(false, BaseEnums.CommBase.NEED_LOGIN);
        }

        //적립금 설정 번호 확인
        if(Objects.isNull(pmPointId) || pmPointId < 1){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //추천인 회원번호 확인
        if(Objects.isNull(recommenderId) || recommenderId < 1){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //적립금 설정 조회
        PointVo pointInfo = pointUseMapper.getPmPoint(pmPointId);

        if(pointInfo == null){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //최대 적립 가능액 확인
        Long maximumAccrualAmount = getMaximumAccrualAmount(pointInfo.getIssueVal(), urUserId);

        DepositPointDto depositPointDto = DepositPointDto.builder()
                .urUserId(urUserId)
                .pmPointId(pointInfo.getPmPointId())
                .amount(maximumAccrualAmount)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .pointSettlementType(pointInfo.getPointSettlementTp())
                .pointProcessType(pointInfo.getPointProcessTp())
                .refNo1(recommenderId+"")
                .refNo2(refNo2+"")
                .build();

        PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto = PointPartialDepositOverLimitDto.builder()
                .urUserId(urUserId)
                .maximumAccrualAmount(maximumAccrualAmount)
                .requestAmount(pointInfo.getIssueVal())
                .pointProcessType(depositPointDto.getPointProcessType())
                .pointSettlementType(depositPointDto.getPointSettlementType())
                .expirationDate(getExpirationDate(pointInfo))
                .issueDeptCd(pointInfo.getIssueDeptCd())
                .pmPointId(pointInfo.getPmPointId())
                .refNo1(depositPointDto.getRefNo1())
                .refNo2(depositPointDto.getRefNo2())
                .build();

        //최대 적립 가능 적립금 초과
        if(maximumAccrualAmount == 0L){
            //보유 가능 적립금 초과로 미적립 내역 저장
            addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);

            return ApiResult.result(false, PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED);
        }

        //적립금 사용 정보 등록
        addPointUsedInfo(depositPointDto, pointInfo);

        //보유 가능 적립금 초과로 부분 적립처리 후 return: 부분 적립 금액, PointUseMessage Enum
        if (pointInfo.getIssueVal() > maximumAccrualAmount){

            addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);

            return ApiResult.result(maximumAccrualAmount, PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT);
        }

        return ApiResult.success(true);

    }

    /**
     * 적립금 적립 처리
     * @param depositPointDto
     * @return
     * @throws Exception
     */
    protected ApiResult<?> depositPoints(DepositPointDto depositPointDto) {

        //파라미터 필수값 체크
        ApiResult<Boolean> resultValidation = depositPointValidation(depositPointDto);
        if(resultValidation.getCode() != BaseEnums.Default.SUCCESS.getCode()){
            return resultValidation;
        }

        //최대 적립 가능액 확인
        Long maximumAccrualAmount = getMaximumAccrualAmount(depositPointDto.getAmount(), depositPointDto.getUrUserId());

        //적립금 설정 조회
        PointVo pointInfo = pointUseMapper.getPmPoint(depositPointDto.getPmPointId());

        if(pointInfo == null){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        DepositPointDto depositPointInfo = DepositPointDto.builder()
                .urUserId(depositPointDto.getUrUserId())
                .pmPointId(depositPointDto.getPmPointId())
                .amount(maximumAccrualAmount)
                .pointPaymentType(depositPointDto.getPointPaymentType())
                .pointSettlementType(depositPointDto.getPointSettlementType())
                .pointProcessType(depositPointDto.getPointProcessType())
                .refNo2(depositPointDto.getRefNo1())
                .refNo2(depositPointDto.getRefNo2())
                .cmnt(depositPointDto.getCmnt())
                .build();

        PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto = PointPartialDepositOverLimitDto.builder()
                .urUserId(depositPointDto.getUrUserId())
                .maximumAccrualAmount(maximumAccrualAmount)
                .requestAmount(depositPointDto.getPmPointId())
                .pointProcessType(depositPointDto.getPointProcessType())
                .pointSettlementType(depositPointDto.getPointSettlementType())
                .expirationDate(getExpirationDate(pointInfo))
                .issueDeptCd(pointInfo.getIssueDeptCd())
                .pmPointId(pointInfo.getPmPointId())
                .refNo1(depositPointDto.getRefNo1())
                .refNo1(depositPointDto.getRefNo1())
                .build();

        //최대 적립 가능 적립금 초과
        if(maximumAccrualAmount == 0L){
            //보유 가능 적립금 초과로 미적립 내역 저장
            addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);

            return ApiResult.result(false, PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED);
        }

        //적립금 사용 정보 등록
        addPointUsedInfo(depositPointInfo, pointInfo);

        //보유 가능 적립금 초과로 부분 적립처리 후 return: 부분 적립 금액, PointUseMessage Enum
        if (pointInfo.getIssueVal() > maximumAccrualAmount){

            addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);

            return ApiResult.result(maximumAccrualAmount, PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT);
        }

        return ApiResult.success(true);
    }


    /**
     * 시리얼번호로 적립금 적립
     * @param urUserId
     * @param pointSerialNumber
     * @return
     */
    protected ApiResult<?> depositPointsBySerialNumber(Long urUserId, String pointSerialNumber) {

        PointVo pointVo = pointUseMapper.selectPointInfoBySerialNumber(pointSerialNumber);

        if (pointVo == null) return ApiResult.result(false, PointEnums.PointUseMessage.INVALID_POINT_SERIAL_NUMBER);

        //계정당 수량 제한 확인
        int issueQtyLimit = Integer.parseInt(pointVo.getIssueQtyLimit());
        if (issueQtyLimit > 0) {
            int issuedCount = pointUseMapper.selectCountSerialNumberIssuedPerAccount(urUserId, pointVo.getPmPointId());
            if (issueQtyLimit <= issuedCount) return ApiResult.result(false, PointEnums.PointUseMessage.EXCEEDS_ISSUE_LIMIT_PER_ACCOUNT);
        }

        //이용권 단일코드 시 생성개수 확인
        if(PointEnums.SerialNumberType.FIXED_VALUE.getCode().equals(pointVo.getSerialNumberTp())){
            int issuedTotalCount = pointUseMapper.selectCountFixedSerialNumberIssuedPerAccount(pointSerialNumber);
            int issueQty = Integer.parseInt(pointVo.getIssueQty());

            if (issueQty <= issuedTotalCount) return ApiResult.result(false, PointEnums.PointUseMessage.EXCEEDS_ISSUE_LIMIT_ACCOUNT);
        }

        //최대 적립 가능액 확인
        Long maximumAccrualAmount = getMaximumAccrualAmount(pointVo.getIssueVal(), urUserId);

        DepositPointDto depositPointDto = DepositPointDto.builder()
                .urUserId(urUserId)
                .pmPointId(pointVo.getPmPointId())
                .amount(maximumAccrualAmount)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .pointSettlementType(pointVo.getPointSettlementTp())
                .pointProcessType(pointVo.getPointProcessTp())
                .refNo2(pointSerialNumber)
                .cmnt(pointSerialNumber)
                .build();

        PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto = PointPartialDepositOverLimitDto.builder()
                .urUserId(urUserId)
                .maximumAccrualAmount(maximumAccrualAmount)
                .requestAmount(pointVo.getIssueVal())
                .pointProcessType(depositPointDto.getPointProcessType())
                .pointSettlementType(depositPointDto.getPointSettlementType())
                .expirationDate(getExpirationDate(pointVo))
                .issueDeptCd(pointVo.getIssueDeptCd())
                .pmPointId(pointVo.getPmPointId())
                .refNo2(depositPointDto.getRefNo2())
                .build();

        //최대 적립 가능 적립금 초과
        if(maximumAccrualAmount == 0L){
            //보유 가능 적립금 초과로 미적립 내역 저장
            addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);

            return ApiResult.result(false, PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED);
        }

        addPointUsedInfo(depositPointDto, pointVo);

        //보유 가능 적립금 초과로 부분 적립처리 후 return: 부분 적립 금액, PointUseMessage Enum
        if (pointVo.getIssueVal() > maximumAccrualAmount){

            addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);

            return ApiResult.result(maximumAccrualAmount, PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT);
        }

        if(!PointEnums.SerialNumberType.FIXED_VALUE.getCode().equals(pointVo.getSerialNumberTp())){
            boolean isRedeemSerialNumber = serialNumberBiz.redeemSerialNumber(urUserId, pointSerialNumber);

            if (!isRedeemSerialNumber) {
                return ApiResult.result(false, PointEnums.PointUseMessage.ERROR_REDEEM_SERIAL_NUMBER);
            }
        }

        return ApiResult.success(true);

    }

    /**
     * 적립금 적립 처리 시 필수값 체크 메소드
     * @param depositPointDto
     * @return
     */
    protected ApiResult<Boolean> depositPointValidation(DepositPointDto depositPointDto){
        //회원번호 번호 확인
        if(depositPointDto.getUrUserId() < 1){
            return ApiResult.result(false, BaseEnums.CommBase.NEED_LOGIN);
        }

        //적립금 지불유형 미존재 시
        if(depositPointDto.getPointPaymentType() == null){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //적립금 지불 유형 적립 일때 1보다 작고 미존재 시
        if(depositPointDto.getPointPaymentType() == PointEnums.PointPayment.PROVISION && depositPointDto.getAmount() < 1){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //적립금 지불 유형 차감 일때 1보다 크고 미존재 시
        if(depositPointDto.getPointPaymentType() == PointEnums.PointPayment.DEDUCTION && depositPointDto.getAmount() > 1){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //적립금 설정 번호 확인
        if(depositPointDto.getPmPointId() < 1){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        return ApiResult.success(true);
    }

    /**
     * 적립금 사용 정보 등록
     * @param depositPointDto
     * @param pointInfo
     */
    protected void addPointUsedInfo(DepositPointDto depositPointDto, PointVo pointInfo) {
        //적립금 내역 등록
        PointUsedDto pointUsedDto = PointUsedDto.builder()
                .depositPointDto(depositPointDto)
                .pointVo(pointInfo)
                .build();
        pointUseMapper.addDepositPointsPmPointUsed(pointUsedDto);

        //적립금 상세 내역 등록
        PointUsedDetailDto pointUsedDetailDto = PointUsedDetailDto.builder()
                .pointUsedDto(pointUsedDto)
                .pointVo(pointInfo)
                .closeYn("N")
                .build();
        pointUseMapper.addDepositPointsPmPointUsedDetl(pointUsedDetailDto);
    }

    /**
     * 최대 적립 가능액
     * 보유 적립금을 초과 시 부분 적립을 위한 용도
     * @param amount
     * @param urUserId
     * @return
     */
    protected Long getMaximumAccrualAmount(Long amount, Long urUserId){
        int availablePoint = pointUseMapper.getUserAvailablePoints(urUserId);

        //최대적립금과 보유적립금이 같을 때 0 리턴
        if(Constants.DEPOSIT_MAXIMUM_POSSIBLE_POINT == availablePoint){
            return 0L;
        }

        //최대보유가능금액 보다 작을때 적립가능액 리턴
        if(Constants.DEPOSIT_MAXIMUM_POSSIBLE_POINT > availablePoint+amount){
            return amount;
        }

        return new Long(Constants.DEPOSIT_MAXIMUM_POSSIBLE_POINT-availablePoint);

    }

    /**
     * 적립 후 예상 적립금이 최대 적립 가능 적립금을 초과 확인
     * @param amount
     * @return
     */
    protected boolean exceededMaximumDepositPoints(Long amount, Long urUserId) {
        int availablePoint = pointUseMapper.getUserAvailablePoints(urUserId);

        return availablePoint+amount > Constants.DEPOSIT_MAXIMUM_POSSIBLE_POINT ? true : false;
    }

    /**
     * 적립금 차감
     * @param depositPointDto
     * @return
     */
    protected ApiResult<Boolean> minusPoint(DepositPointDto depositPointDto) throws Exception {

        List<Long> closePointRefList = new ArrayList<>();

        pointUseMapper.insertPointUsed(depositPointDto);

        long masterPointUsedId = depositPointDto.getPmPointUsedId();
        long amount = depositPointDto.getAmount();

        List<PointUsedDetailVo> usablePointList = pointUseMapper.selectUsablePointListOrderByExpiryDate(depositPointDto.getUrUserId());
        for (PointUsedDetailVo usablePoint : usablePointList) {

            if (amount >= 0) break;

            long increment = amount;
            amount += usablePoint.getAmount();

            if (amount <= 0) {
                increment = -usablePoint.getAmount();
                closePointRefList.add(usablePoint.getRefPointUsedDetlId());
            }

            PointEnums.PointProcessType minusPointProcessType = getMinusPointProcessType(depositPointDto.getPointProcessType(),usablePoint.getPointSettlementTp());

            PointUsedDetailAddRequestDto pointDetailAddDto = PointUsedDetailAddRequestDto.builder()
                    .amount(increment)
                    .urUserId(usablePoint.getUrUserId())
                    .pmPointUsedId(masterPointUsedId)
                    .pmPointId(usablePoint.getPmPointId())
                    .refPointUsedDetailId(usablePoint.getRefPointUsedDetlId())
                    .availableDate(usablePoint.getAvailableDt())
                    .expirationDate(usablePoint.getExpirationDt())
                    .pointPaymentType(depositPointDto.getPointPaymentType())
                    .refNo1(depositPointDto.getRefNo1())
                    .refNo2(depositPointDto.getRefNo2())
                    .deptCd(usablePoint.getDeptCd())
                    .pointProcessType(minusPointProcessType)
                    .pointSettlementType(EnumUtil.getEnum(PointEnums.PointSettlementType.class,minusPointProcessType.getSettlementCode()))
                    .build();

            pointUseMapper.insertPointUsedDetail(pointDetailAddDto);

            // 중복 차감 검증 확인
            if(!pointUseMapper.isValidationOverUsedPoint(usablePoint.getRefPointUsedDetlId())) {
            	throw new Exception("isValidationOverUsedPoint - 쿠폰 차감 검증 실패");
            }
        }

        //update closeYn
        if (CollectionUtils.isNotEmpty(closePointRefList)) {
            pointUseMapper.closeUsedPoint(depositPointDto.getUrUserId(), closePointRefList);
        }

        return ApiResult.success(true);
    }

    /**
     * 적립금 차감 요청에 대한 유효성 체크
     * @param depositPointDto
     * @return
     * @throws Exception
     */
    protected ApiResult<?> validateMinusPointRequest(DepositPointDto depositPointDto) {

        if (depositPointDto.getPointProcessType() == null) {
            return ApiResult.result(PointEnums.PointUseMessage.INVALID_MINUS_REQUEST_PARAM);
        }

        if (depositPointDto.getPointProcessType().isPlus()) {
            return ApiResult.result(PointEnums.PointUseMessage.INVALID_POINT_PROCESS_TYPE);
        }

        long amount = depositPointDto.getAmount();
        if (amount >= 0) {
            return ApiResult.result(PointEnums.PointUseMessage.INVALID_POINT_AMOUNT);
        }

        long pointBalance = pointUseMapper.getUserAvailablePoints(depositPointDto.getUrUserId());
        if (pointBalance + amount < 0) {
            return ApiResult.result(PointEnums.PointUseMessage.USER_POINT_LACK);
        }
        return ApiResult.success();
    }


    /**
     * 적립금 환불
     * @param pointRefundRequestDto
     * @return
     */
    protected ApiResult<?> refundPoint(PointRefundRequestDto pointRefundRequestDto) throws ParseException {

        //미지급 환불금액 리스트
        List<PointUnpaidIssueVo> unpaidIssueList = pointUseMapper.selectUnpaidRefundList(pointRefundRequestDto.getUrUserId(), pointRefundRequestDto.getOrderNo());

        //주문번호, 적립금 학인
        List<PointUsedDetailVo> pointDetailListUsedOrder = pointUseMapper.selectPointDetailListUsedOrder(pointRefundRequestDto.getUrUserId(), pointRefundRequestDto.getOrderNo());
        if (CollectionUtils.isEmpty(pointDetailListUsedOrder)) {
            return ApiResult.result(false, PointEnums.PointUseMessage.INVALID_REFUND_REQUEST_ORDER_NO);
        }

        //미지급 환불적립금 환불 가능한 적립금 리스트에서 차감
        for (PointUsedDetailVo pointUsedDetailVo : pointDetailListUsedOrder) {
            for (PointUnpaidIssueVo pointUnpaidIssueVo : unpaidIssueList) {
                //미지급 환불적립금에 환불 가능한 사용 적립금 이력 고유값이 같을 때 환불가능적립금 차감
                if(pointUsedDetailVo.getAmount()<0 && pointUsedDetailVo.getPmPointUsedDetlId().equals(pointUnpaidIssueVo.getRefDproPointUsedDetlId())){
                    pointUsedDetailVo.setAmount(pointUsedDetailVo.getAmount()+pointUnpaidIssueVo.getIssueVal());
                }
            }
        }

        long refundablePoint = pointDetailListUsedOrder.stream().mapToLong(PointUsedDetailVo::getAmount).sum();
        long refundRequestAmount = pointRefundRequestDto.getAmount();
        if (refundRequestAmount + refundablePoint > 0) {
            return ApiResult.result(false, PointEnums.PointUseMessage.EXCEEDS_REFUNDABLE_POINT);
        }

        //최대 적립 가능액 확인
        long maximumAccrualAmount = getMaximumAccrualAmount(refundRequestAmount, pointRefundRequestDto.getUrUserId());

        boolean isUnpaidRefund = (maximumAccrualAmount == 0 || refundRequestAmount > maximumAccrualAmount) ? true : false;


        long masterPointUsedId = 0;

        //insert into PM_POINT_USED
        if(maximumAccrualAmount > 0) {
            pointRefundRequestDto.initDepositPointDto(maximumAccrualAmount);
            pointUseMapper.insertPointUsed(pointRefundRequestDto.getDepositPointDto());

            masterPointUsedId = pointRefundRequestDto.masterPointUsedId();
        }

        //insert into PM_POINT_USED_DETL
        pointDetailListUsedOrder = pointDetailListUsedOrder.stream().filter(l -> l.getAmount() < 0).collect(toList());

        //환불 가능 금액
        long refundAmount = maximumAccrualAmount;

        //미지급 금액
        long unpaidRefundAmount = refundRequestAmount-maximumAccrualAmount;

        PointUsedDetailAddRequestDto pointDetailAddDto = null;

        for (PointUsedDetailVo usedPoint : pointDetailListUsedOrder) {
            //환불 가능 금액 존재 시 적립처리
            boolean isDeposit = refundAmount > 0 ? true : false;

            if (refundAmount <= 0 && unpaidRefundAmount <= 0) break;

            long increment = isDeposit ? refundAmount : unpaidRefundAmount;

            if (isDeposit) {
                refundAmount += usedPoint.getAmount();
            } else {
                unpaidRefundAmount += usedPoint.getAmount();
            }

            if ((isDeposit && refundAmount > 0) || (!isDeposit && unpaidRefundAmount > 0)) {
                increment = -usedPoint.getAmount();
            }

            pointDetailAddDto = PointUsedDetailAddRequestDto.builder()
                    .amount(increment)
                    .urUserId(usedPoint.getUrUserId())
                    .pmPointUsedId(masterPointUsedId)
                    .pmPointId(usedPoint.getPmPointId())
                    .refDproPointUsedDetlId(usedPoint.getPmPointUsedDetlId())
                    .availableDate(usedPoint.getAvailableDt())
                    .expirationDate(resetRefundPointExpirationDate(usedPoint.getExpirationDt(), usedPoint.getCreateDt(), pointRefundRequestDto.getReasonAttributableType()))
                    .pointPaymentType(PointEnums.PointPayment.PROVISION)
                    .refNo1(pointRefundRequestDto.getOrderNo())
                    .refNo2(pointRefundRequestDto.getOdClaimId())
                    .deptCd(usedPoint.getDeptCd())
                    .pointProcessType(PointEnums.PointProcessType.getRefundPointProcessType(usedPoint.getPointProcessTp()))
                    .pointSettlementType(PointEnums.PointSettlementType.getRefundPointSettlementType(usedPoint.getPointSettlementTp()))
                    .build();

            //적립 상세 내역 저장
            if(isDeposit) {
                pointUseMapper.insertPointUsedDetail(pointDetailAddDto);
            }

            //미적립 환불 내역 저장
            if(!isDeposit){
                PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto = PointPartialDepositOverLimitDto.builder()
                        .urUserId(pointRefundRequestDto.getUrUserId())
                        .requestAmount(increment)
                        .pointProcessType(pointDetailAddDto.getPointProcessType())
                        .pointSettlementType(pointDetailAddDto.getPointSettlementType())
                        .expirationDate(pointDetailAddDto.getExpirationDate())
                        .issueDeptCd(pointDetailAddDto.getDeptCd())
                        .pmPointId(pointDetailAddDto.getPmPointId())
                        .refNo1(pointDetailAddDto.getRefNo1())
                        .refNo2(pointDetailAddDto.getRefNo2())
                        .refDproPointUsedDetlId(pointDetailAddDto.getRefDproPointUsedDetlId())
                        .availableDate(pointDetailAddDto.getAvailableDate())
                        .reasonAttributableType(pointRefundRequestDto.getReasonAttributableType())
                        .build();

                addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);
            }
        }

        //미지급 내역이 존재할때
        if(unpaidRefundAmount>0 && pointDetailAddDto != null){
            PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto = PointPartialDepositOverLimitDto.builder()
                    .urUserId(pointRefundRequestDto.getUrUserId())
                    .requestAmount(unpaidRefundAmount)
                    .pointProcessType(pointDetailAddDto.getPointProcessType())
                    .pointSettlementType(pointDetailAddDto.getPointSettlementType())
                    .expirationDate(pointDetailAddDto.getExpirationDate())
                    .issueDeptCd(pointDetailAddDto.getDeptCd())
                    .pmPointId(pointDetailAddDto.getPmPointId())
                    .refNo1(pointDetailAddDto.getRefNo1())
                    .refNo2(pointDetailAddDto.getRefNo2())
                    .refDproPointUsedDetlId(pointDetailAddDto.getRefDproPointUsedDetlId())
                    .availableDate(pointDetailAddDto.getAvailableDate())
                    .reasonAttributableType(pointRefundRequestDto.getReasonAttributableType())
                    .build();
            addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);
        }

        //최대 적립 가능 적립금 초과로 미적립 환불
        if(maximumAccrualAmount == 0){
            return ApiResult.result(false, PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED);
        }

        //보유 가능 적립금 초과로 부분 적립처리 후 return: 부분 적립 금액, PointUseMessage Enum
        if (refundRequestAmount > maximumAccrualAmount){
            return ApiResult.result(maximumAccrualAmount, PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT);
        }

        return ApiResult.success(true);

    }

    private String resetRefundPointExpirationDate(String expirationDate, String createDate, ClaimEnums.ReasonAttributableType reasonAttributableType) throws ParseException {

        //구매자 귀책일 경우 기존 만료일
        if (ClaimEnums.ReasonAttributableType.BUYER == reasonAttributableType) return expirationDate;

        //판매자 귀책일 경우, 환불요청일 + (만료일-주문발생 잔여일)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int remainingDays = (int) ((dateFormat.parse(expirationDate).getTime() - dateFormat.parse(createDate).getTime()) / (24*60*60*1000));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, remainingDays);

        return dateFormat.format(cal.getTime());

    }

    /**
     * 적립된 적립금 정산 유형(적립금/이용권)으로 차감 적립금 처리 유형 변환
     * @param pointProcessType
     * @param pointSettlementType
     * @return
     */
    protected PointEnums.PointProcessType getMinusPointProcessType(PointEnums.PointProcessType pointProcessType, PointEnums.PointSettlementType pointSettlementType){

        if(pointProcessType == null || pointSettlementType == null){
            return null;
        }

        //차감 적립금 처리 유형
        String[] depositePtProcTpArr = pointProcessType.getCode().split(Constants.STRING_ARRAY_SEPARATORS_DOT);

        if(depositePtProcTpArr.length != 2){
            return null;
        }

        /**
         * 적립금 정산 유형 반영된 차감 적립금 처리 유형
         * depositePtProcTpArr[0] master code
         * depositePtProcTpArr[1].substring(0,1) 적립금 차감 처리 첫번째 코드값(적립/차감)
         * pointSettlementType.getPointType().substring(0,1) 적립된 적립금 정산 유형(적립금/이용권)
         * depositePtProcTpArr[1].substring(2) 적립금 차감 처리 3,4번째 코드값(처리)
         */

        String withdrawPointProcessTp = depositePtProcTpArr[0]+"."+depositePtProcTpArr[1].substring(0,1)+pointSettlementType.getPointType().substring(0,1)+depositePtProcTpArr[1].substring(2);

        return EnumUtil.getEnum(PointEnums.PointProcessType.class, withdrawPointProcessTp);
    }

    /**
     * 만료된 적립금 소멸
     * @param expireDate
     * @return
     */
    protected ApiResult<Boolean> expirePoint(String expireDate) throws Exception {
        List<PointUsedDetailVo> expiredPointList = pointUseMapper.selectExpiredPointList(expireDate);

        for (PointUsedDetailVo expiredPoint : expiredPointList){
            DepositPointDto depositPointDto = DepositPointDto.builder()
                    .pointPaymentType(PointEnums.PointPayment.DEDUCTION)
                    .pointProcessType(PointEnums.PointProcessType.WITHDRAW_POINT_PERIOD_EXPIRE)
                    .urUserId(expiredPoint.getUrUserId())
                    .amount(expiredPoint.getAmount()*-1)
                    .build();

            minusPoint(depositPointDto);
        }

        return ApiResult.success(true);
    }


    /**
     * 탈퇴회원 적립급 소멸
     * @param urUserId
     * @return
     */
    protected ApiResult<Boolean> expireWithdrawalMemberPoint(Long urUserId) throws Exception {

        List<PointUsedDetailVo> pointList = pointUseMapper.selectWithdrawalMemberPointList(urUserId);

        for (PointUsedDetailVo expiredPoint : pointList){
            DepositPointDto depositPointDto = DepositPointDto.builder()
                    .pointPaymentType(PointEnums.PointPayment.DEDUCTION)
                    .pointProcessType(PointEnums.PointProcessType.WITHDRAW_POINT_EXTINCTION)
                    .urUserId(urUserId)
                    .amount(expiredPoint.getAmount() * -1)
                    .build();

            minusPoint(depositPointDto);
        }

        return ApiResult.success(true);
    }

    /**
     * 환불 적립금 만료로 즉시 소멸
     * @param pointRefundRequestDto
     * @return
     */
    protected ApiResult<?> expireImmediateRefundPoint(PointRefundRequestDto pointRefundRequestDto) {

        //필수값 체크
        ApiResult<?> validation = expireImmediateRefundPointValidation(pointRefundRequestDto);

        if(BaseEnums.Default.SUCCESS.getCode() != validation.getCode()){
            return validation;
        }

        //구매자 귀책 사유가 아닐 경우 미처리
        if(ClaimEnums.ReasonAttributableType.BUYER != pointRefundRequestDto.getReasonAttributableType()){
            return ApiResult.result(false, PointEnums.PointUseMessage.INVALID_EXPIRE_IMMEDIATE_REFUND_REASON);
        }

        //즉시 소멸 가능한 주문 환불 적립금 학인
        List<PointUsedDetailVo> orderRefundPointThatCanExpireImmediateList = pointUseMapper.selectOrderRefundPointThatCanExpireImmediate(pointRefundRequestDto.getUrUserId(), pointRefundRequestDto.getOrderNo());

        if (CollectionUtils.isEmpty(orderRefundPointThatCanExpireImmediateList)) {
            return ApiResult.result(false, PointEnums.PointUseMessage.INVALID_EXPIRE_IMMEDIATE_REFUND_REQUEST_ORDER);
        }

        for (PointUsedDetailVo expiredPoint : orderRefundPointThatCanExpireImmediateList){
            DepositPointDto depositPointDto = DepositPointDto.builder()
                    .pointPaymentType(PointEnums.PointPayment.DEDUCTION)
                    .pointProcessType(PointEnums.PointProcessType.WITHDRAW_POINT_PERIOD_EXPIRE)
                    .urUserId(expiredPoint.getUrUserId())
                    .amount(expiredPoint.getAmount()*-1)
                    .refNo1(expiredPoint.getRefNo1())
                    .build();

            expireImmediateRefundMinusPoint(depositPointDto, expiredPoint);
        }

        return ApiResult.success(true);
    }

    /**
     * 환불 적립금 만료로 즉시 소멸 필수값 체크
     * @param pointRefundRequestDto
     * @return
     */
    protected ApiResult<?> expireImmediateRefundPointValidation(PointRefundRequestDto pointRefundRequestDto){
        //회원번호 번호 확인
        if(pointRefundRequestDto.getUrUserId() == null){
            return ApiResult.result(false, BaseEnums.CommBase.NEED_LOGIN);
        }

        if(pointRefundRequestDto.getUrUserId() < 1){
            return ApiResult.result(false, BaseEnums.CommBase.NEED_LOGIN);
        }

        //귀책 사유 미존재 시
        if(pointRefundRequestDto.getReasonAttributableType() == null){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //주문번호 미존재 시
        if(pointRefundRequestDto.getOrderNo() == null){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        return ApiResult.success(true);
    }

    /**
     * 환불 적립금 만료로 즉시 차감(소멸) 처리
     * @param depositPointDto
     * @param expiredPoint
     * @return
     */
    protected ApiResult<Boolean> expireImmediateRefundMinusPoint(DepositPointDto depositPointDto, PointUsedDetailVo expiredPoint) {

        List<Long> closePointRefList = new ArrayList<>();

        pointUseMapper.insertPointUsed(depositPointDto);

        long masterPointUsedId = depositPointDto.getPmPointUsedId();

        closePointRefList.add(expiredPoint.getRefPointUsedDetlId());

        PointEnums.PointProcessType minusPointProcessType = getMinusPointProcessType(depositPointDto.getPointProcessType(),expiredPoint.getPointSettlementTp());

        PointUsedDetailAddRequestDto pointDetailAddDto = PointUsedDetailAddRequestDto.builder()
                .amount(depositPointDto.getAmount())
                .urUserId(expiredPoint.getUrUserId())
                .pmPointUsedId(masterPointUsedId)
                .pmPointId(expiredPoint.getPmPointId())
                .refPointUsedDetailId(expiredPoint.getRefPointUsedDetlId())
                .availableDate(expiredPoint.getAvailableDt())
                .expirationDate(expiredPoint.getExpirationDt())
                .pointPaymentType(depositPointDto.getPointPaymentType())
                .refNo1(depositPointDto.getRefNo1())
                .refNo2(depositPointDto.getRefNo2())
                .deptCd(expiredPoint.getDeptCd())
                .pointProcessType(minusPointProcessType)
                .pointSettlementType(EnumUtil.getEnum(PointEnums.PointSettlementType.class,minusPointProcessType.getSettlementCode()))
                .build();

        pointUseMapper.insertPointUsedDetail(pointDetailAddDto);


        //update closeYn
        if (CollectionUtils.isNotEmpty(closePointRefList)) {
            pointUseMapper.closeUsedPoint(depositPointDto.getUrUserId(), closePointRefList);
        }

        return ApiResult.success(true);
    }


    /**
     * 단건 미지급건 재적립 처리
     * @param pmPointNotIssueId
     * @param urUserId
     * @return
     * @throws Exception
     */
    protected ApiResult<?> depositNotIssuePoints(Long pmPointNotIssueId, Long urUserId) throws Exception{

    	//적립금 매지급 내역 고유값 미존재 시
        if(pmPointNotIssueId == null){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //회원번호  확인
        if(Objects.isNull(urUserId) || urUserId < 1){
            return ApiResult.result(false, BaseEnums.CommBase.NEED_LOGIN);
        }


        PointUsedDetailVo notIssuePointVo = new PointUsedDetailVo();
        notIssuePointVo = pointUseMapper.getNotIssuePointDetail(pmPointNotIssueId);

        UserVo userVo     = SessionUtil.getBosUserVO();

        //최대 미적립 지급 가능액 확인
        Long maximumAccrualAmount = getMaximumAccrualAmount(notIssuePointVo.getRedepositPointVal(), notIssuePointVo.getUrUserId());

        //최대 적립 가능 적립금 초과
        if(maximumAccrualAmount == 0L){
            return ApiResult.result(false, PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED);
        }

        //적립금 적립 유효기간 설정
        PointVo pointInfo = new PointVo();
        pointInfo.setValidityTp(PointEnums.ValidityType.PERIOD);
        pointInfo.setValidityEndDt(notIssuePointVo.getExpirationDt());
        pointInfo.setIssueDeptCd(notIssuePointVo.getDeptCd());

        DepositPointDto depositPointDto = DepositPointDto.builder()
                .urUserId(notIssuePointVo.getUrUserId())
                .amount(maximumAccrualAmount)
                .refNo1(notIssuePointVo.getRefNo1())
                .refNo2(notIssuePointVo.getRefNo2())
                .pointProcessType(notIssuePointVo.getPointProcessTp())
                .pointSettlementType(notIssuePointVo.getPointSettlementTp())
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .cmnt(Constants.REDEPOSIT_NOT_ISSUE_POINT_COMMENT + " " + notIssuePointVo.getCmnt())
                .build();

        //적립금 사용 정보 등록
        addPointUsedInfo(depositPointDto, pointInfo);

        //보유 가능 적립금 초과로 부분 적립처리 후 return: 부분 적립 금액, PointUseMessage Enum
        if ( maximumAccrualAmount > 0){

            PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto = PointPartialDepositOverLimitDto.builder()
                    .urUserId(notIssuePointVo.getUrUserId())
                    .maximumAccrualAmount(notIssuePointVo.getPartPointVal() + maximumAccrualAmount)
                    .requestAmount(notIssuePointVo.getIssueVal())
                    .pmPointNotIssueId(pmPointNotIssueId)
                    .modifyId(Long.parseLong(userVo.getUserId()))
                    .build();

            //미지급 적립금 정보 Update
            putDepositNotIssuePoints(pointPartialDepositOverLimitDto);

//            return ApiResult.result(maximumAccrualAmount, PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT);
        }

        return ApiResult.success(true);
    }

    /**
     * 단건 미지급 적립금 업데이트 처리
     * @param pointPartialDepositOverLimitDto
     */
    protected void putDepositNotIssuePoints(PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto) {
        log.info("------------ 미적립 적립금 내역 수정");

        pointUseMapper.updatePointNotIssue(pointPartialDepositOverLimitDto);
    }


    /**
     * 미적립된 환불 적립금 재적립 시 만료일 계산
     * @param expirationDate
     * @param depositDate
     * @return
     * @throws ParseException
     */
    protected String resetRefundNotIssuePointExpirationDate(String expirationDate, String depositDate) throws ParseException {

        //판매자 귀책일 경우, 환불요청일 + (만료일-주문발생 잔여일)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int remainingDays = (int) ((dateFormat.parse(expirationDate).getTime() - dateFormat.parse(depositDate).getTime()) / (24*60*60*1000));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, remainingDays);

        return dateFormat.format(cal.getTime());

    }


    /**
     * 미적립된 환불 적립금 재적립 처리
     * @return
     * @throws Exception
     */
    protected ApiResult<?> depositRefundOrderNotIssuePoints() throws Exception{

    	//미적립된 환불 적립금 리스트
    	//회원번호, 주문번호, 클레임번호 Group By 리스트 조회
    	List <PointUsedDetailVo> refundNotIssuedList =  pointUseMapper.getRefundNotIssuedList();

    	for (PointUsedDetailVo refundPointVo : refundNotIssuedList){


    		//회원번호, 주문번호, 클레임번호 재적립 처리
    		List <PointUsedDetailVo> addNotIssuedList =  pointUseMapper.getAddNotIssuedList(refundPointVo);

    		long refundablePoint = addNotIssuedList.stream().mapToLong(PointUsedDetailVo::getRedepositPointVal).sum();

    		//최대 적립 가능액 확인
    		Long maximumAccrualAmount = getMaximumAccrualAmount(refundablePoint, refundPointVo.getUrUserId());

    		//최대 적립 가능 적립금 초과
            if(maximumAccrualAmount == 0L){
            	log.info(PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED.getMessage());
            	continue;
            }


    		long masterPointUsedId = 0;

    		PointRefundRequestDto pointRefundRequestDto = PointRefundRequestDto.builder()
 					.urUserId(refundPointVo.getUrUserId())	//회원 ID
 					.orderNo(refundPointVo.getRefNo1()) 	//주문번호
 					.odClaimId(refundPointVo.getRefNo2()) 	//클레임 PK
 					.amount(refundPointVo.getAmount())		//환불 금액
 					.build();

	        if(maximumAccrualAmount > 0) {
	            pointRefundRequestDto.initDepositPointDto(maximumAccrualAmount);
	            pointUseMapper.insertPointUsed(pointRefundRequestDto.getDepositPointDto());

	            masterPointUsedId = pointRefundRequestDto.masterPointUsedId();
	        }

	        // 미지급 금액
	        long unpaidRefundAmount = maximumAccrualAmount;

	        PointRequestDto pointRequestDto = new PointRequestDto();
	        pointRequestDto.setPointUsedDetailVoList(addNotIssuedList);

	        // 사용상세 및 미지급 적립금 정보 수정
	        this.putPointUsedDetail(pointRequestDto, masterPointUsedId, unpaidRefundAmount, refundPointVo.getRefNo1(), refundPointVo.getRefNo2()) ;

    	}

        return ApiResult.success(true);
    }



    /**
     * 미적립된 환불 적립금 재적립 사용상세 미지급 적립금 정보 수정
     * @param pointRequestDto
     * @param masterPointUsedId
     * @param unpaidRefundAmount
     * @param orderNo
     * @param claimId
     * @return
     * @throws ParseException
     */
    protected ApiResult<?> putPointUsedDetail(PointRequestDto pointRequestDto, Long masterPointUsedId, Long unpaidRefundAmount, String orderNo, String claimId) throws ParseException {

		for (PointUsedDetailVo addPointVo : pointRequestDto.getPointUsedDetailVoList()) {

            if (unpaidRefundAmount <= 0) break;

            long increment = unpaidRefundAmount;

            unpaidRefundAmount -= addPointVo.getRedepositPointVal();

            if (unpaidRefundAmount > 0) {
                increment = addPointVo.getRedepositPointVal();
            }

            PointUsedDetailAddRequestDto pointDetailAddDto = PointUsedDetailAddRequestDto.builder()
                    .amount(increment)
                    .urUserId(addPointVo.getUrUserId())
                    .pmPointUsedId(masterPointUsedId)
                    .pmPointId(addPointVo.getPmPointId())
                    .refDproPointUsedDetlId(addPointVo.getRefDproPointUsedDetlId())
                    .availableDate(addPointVo.getAvailableDt())
                    .expirationDate(resetRefundNotIssuePointExpirationDate(addPointVo.getExpirationDt(), addPointVo.getDepositDt()))
                    .pointPaymentType(PointEnums.PointPayment.PROVISION)
                    .refNo1(orderNo)
                    .refNo2(claimId)
                    .deptCd(addPointVo.getDeptCd())
                    .pointProcessType(addPointVo.getPointProcessTp())
                    .pointSettlementType(addPointVo.getPointSettlementTp())
                    .build();

            //적립 상세 내역 저장
            pointUseMapper.insertPointUsedDetail(pointDetailAddDto);

            // 단건 지급 Update  처리 추가
            if ( increment > 0){

                PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto = PointPartialDepositOverLimitDto.builder()
                        .urUserId(addPointVo.getUrUserId())
                        .maximumAccrualAmount(addPointVo.getPartPointVal() + increment)
                        .requestAmount(addPointVo.getIssueVal())
                        .pointProcessType(PointEnums.PointProcessType.getRefundPointProcessType(addPointVo.getPointProcessTp()))
                        .pointSettlementType(PointEnums.PointSettlementType.getRefundPointSettlementType(addPointVo.getPointSettlementTp()))
                        .pmPointNotIssueId(addPointVo.getPmPointNotIssueId())
                        .modifyId(Constants.BATCH_CREATE_ID)
                        .expirationDate(resetRefundNotIssuePointExpirationDate(addPointVo.getExpirationDt(), addPointVo.getDepositDt()))
                        .build();
                //미지급 적립금 정보 Update
                putDepositNotIssuePoints(pointPartialDepositOverLimitDto);

            }
		}

        return ApiResult.success(true);
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
    protected ApiResult<?> depositCsRefundOrderPoint(Long urUserId, String orderNo, Long amount, Boolean isCsRoleManager, String finOrganizationCode, Integer csPointValidityDay) throws Exception {

        //회원번호 번호 확인
        if(Objects.isNull(urUserId) || urUserId < 1){
            return ApiResult.result(false, BaseEnums.CommBase.NEED_LOGIN);
        }

        //주문번호 미존재 시
        if(StringUtil.isEmpty(orderNo)){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //적립금 1보다 작고  미존재 시
        if(Objects.isNull(amount) || amount < 1){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //CS 역할관리자 여부 값 미존재 시
        if(isCsRoleManager == null){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //CS 역할관리자인데 회계조직코드 미존재 시
        if(isCsRoleManager && StringUtil.isEmpty(finOrganizationCode)){
            return ApiResult.result(false, BaseEnums.CommBase.MANDATORY_MISSING);
        }

        //최대 적립 가능액 확인
        Long maximumAccrualAmount = getMaximumAccrualAmount(amount, urUserId);

        DepositPointDto depositPointDto = DepositPointDto.builder()
                .urUserId(urUserId)
                .amount(maximumAccrualAmount)
                .pointProcessType(PointEnums.PointProcessType.DEPOSIT_POINT_CUSTOMER_SERVICE_REFUND_ORDER)
                .pointSettlementType(PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .refNo1(orderNo)
                .cmnt("관리자 CS환불 " + orderNo)
                .build();

        //적립금 적립 유효기간 설정
        PointVo pointInfo = new PointVo();
        pointInfo.setValidityTp(PointEnums.ValidityType.VALIDITY);
        pointInfo.setValidityDay(csPointValidityDay == null ? Constants.CUSTOMER_SERVICE_REFUND_ORDER_POINT_EXPIRE_DAY : csPointValidityDay);
        pointInfo.setIssueDeptCd(getCsRefundOrderSettlementCorporationCode(isCsRoleManager, finOrganizationCode));

        PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto = PointPartialDepositOverLimitDto.builder()
                .urUserId(urUserId)
                .maximumAccrualAmount(maximumAccrualAmount)
                .requestAmount(amount)
                .pointProcessType(depositPointDto.getPointProcessType())
                .pointSettlementType(depositPointDto.getPointSettlementType())
                .expirationDate(getExpirationDate(pointInfo))
                .issueDeptCd(pointInfo.getIssueDeptCd())
                .cmnt(depositPointDto.getCmnt())
                .build();

        //최대 적립 가능 적립금 초과
        if(maximumAccrualAmount == 0L){
            //보유 가능 적립금 초과로 미적립 내역 저장
            addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);

            return ApiResult.result(false, PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED);
        }

        //적립금 사용 정보 등록
        addPointUsedInfo(depositPointDto, pointInfo);

        //보유 가능 적립금 초과로 부분 적립처리 후 return: 부분 적립 금액, PointUseMessage Enum
        if (amount > maximumAccrualAmount){

            addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);

            return ApiResult.result(maximumAccrualAmount, PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT);
        }

        return ApiResult.success(true);
    }

    /**
     * cs 환불 정산 회계조직코드
     * @param isCsRoleManager
     * @param finOrganizationCode
     * @return
     */
    protected String getCsRefundOrderSettlementCorporationCode(Boolean isCsRoleManager, String finOrganizationCode) {

        return isCsRoleManager ? finOrganizationCode : Constants.ONLINE_MARKETING_SETTLEMENT_CORPORATION_CODE;
    }

}
