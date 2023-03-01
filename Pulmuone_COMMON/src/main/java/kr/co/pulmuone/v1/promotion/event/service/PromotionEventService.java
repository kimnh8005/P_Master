package kr.co.pulmuone.v1.promotion.event.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.constants.PromotionConstants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.mapper.promotion.event.PromotionEventMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.order.front.dto.vo.OrderInfoFromStampPurchaseVo;
import kr.co.pulmuone.v1.order.front.service.OrderFrontBiz;
import kr.co.pulmuone.v1.policy.bbs.service.PolicyBbsBannedWordBiz;
import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.event.dto.*;
import kr.co.pulmuone.v1.promotion.event.dto.vo.*;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionEventService {
    private final PromotionEventMapper promotionEventMapper;
    private final PromotionCouponBiz promotionCouponBiz;
    private final PointBiz pointBiz;
    private final PromotionEventConcurrencyBiz promotionEventConcurrencyBiz;
    private final PolicyConfigBiz policyConfigBiz;

    @Autowired
    private PolicyBbsBannedWordBiz policyBbsBannedWordBiz;

    @Autowired
    private OrderFrontBiz orderFrontBiz;

    @Autowired
    GoodsSearchBiz goodsSearchBiz;

    /**
     * 이벤트 목록조회
     *
     * @param dto EventListByUserRequestDto
     * @return EventListByUserResponseDto
     * @throws Exception Exception
     */
    protected EventListByUserResponseDto getEventListByUser(EventListByUserRequestDto dto) throws Exception {
        List<EventListByUserVo> eventList = promotionEventMapper.getEventListByUser(dto);

        // SPEC-OUT - SPMO-487
//        List<EventListByUserVo> eventList = new ArrayList<>();
//        for (EventListByUserVo vo : getEventList) {
//            // 제외처리
//            // 혜택완료여부 확인
//            if (EventEnums.EventTp.NORMAL.getCode().equals(vo.getEventType())) {   // 일반이벤트
//                if (EventEnums.EventDrawType.AUTO.getCode().equals(vo.getEventDrawType()) && "N".equals(vo.getEndYn())) {    // 당첨자설정 : 즉시당첨 이면서 진행중
//                    if ((EventEnums.EventBenefitType.COUPON.getCode().equals(vo.getEventBenefitType())
//                            || EventEnums.EventBenefitType.POINT.getCode().equals(vo.getEventBenefitType())
//                            || EventEnums.EventBenefitType.GIFT.getCode().equals(vo.getEventBenefitType()))) {    //쿠폰, 적립금, 경품
//                        if (0 >= vo.getRemainBenefitCount()) { // 혜택 소진
//                            continue;
//                        }
//                    }
//                }
//            }
//            eventList.add(vo);
//        }

        // response Dto
        EventListByUserResponseDto responseDto = new EventListByUserResponseDto();
        if (eventList.size() > 0) {
            responseDto.setTotal(eventList.size());

            // page, limit 처리
            responseDto.setEvent(eventList.stream()
                    .skip(dto.getSkipPage())
                    .limit(dto.getLimit())
                    .collect(Collectors.toList()));
        }

        return responseDto;
    }

    /**
     * 이벤트 목록조회 from mypage
     *
     * @param dto EventListFromMyPageRequestDto
     * @return EventListFromMyPageResponseDto
     * @throws Exception Exception
     */
    protected EventListFromMyPageResponseDto getEventListFromMyPage(EventListFromMyPageRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<EventListFromMyPageVo> eventListByUserVos = promotionEventMapper.getEventListFromMyPage(dto);

        return EventListFromMyPageResponseDto.builder()
                .total((int) eventListByUserVos.getTotal())
                .list(eventListByUserVos.getResult())
                .build();
    }

    /**
     * 일반 이벤트 조회
     *
     * @param dto EventRequestDto
     * @return ApiResult<?>
     * @throws Exception Exception
     */
    protected ApiResult<?> getNormalByUser(EventRequestDto dto) throws Exception {
        // Get Event Information
        NormalByUserVo eventInfo = promotionEventMapper.getNormalByUser(dto.getEvEventId(), dto.getUrUserId(), dto.getDeviceType());
        if (eventInfo == null) {
            return ApiResult.result(EventEnums.EventValidation.NO_EVENT);
        } else {
            eventInfo.setUserGroupList(promotionEventMapper.getUserGroup(dto.getEvEventId()));
        }

        //미리보기 여부에 따라 validation 처리
        if (!"Y".equals(dto.getPreviewYn())) {
            EventValidationRequestDto validationRequestDto = new EventValidationRequestDto(dto, eventInfo);
            ApiResult validResult = validPromotionEvent(dto, validationRequestDto);
            if (!validResult.getMessageEnum().equals(EventEnums.EventValidation.SERVICEABLE)) return validResult;
        }

        // Set return data
        NormalByUserResponseDto result = new NormalByUserResponseDto(eventInfo);
        result.setCommentCode(getEventCommentCodeByUser(dto.getEvEventId()));   // Comment Code 조회
        result.setDday(DateUtil.getBetweenDays(eventInfo.getEndDate()));    // dDay 계산
        result.setNotDeviceAppOnlyEventYn(getNotDeviceAppOnlyEventYn(eventInfo.getAppOnlyEventYn(), dto.getDeviceType())); // 앱전용 이벤트 디바이스 아님 : Y

        //상품 정보
        if ("Y".equals(dto.getPreviewYn())) {
            List<EventGroupByUserVo> groupList = getGoodsGroupList(dto.getEvEventId());
            result.setGroup(groupList);
        }

        return ApiResult.success(result);
    }

    /**
     * 일반 이벤트 댓글구분 조회
     *
     * @param evEventId Long
     * @return List<CommentCodeByUserVo>
     * @throws Exception Exception
     */
    protected List<CommentCodeByUserVo> getEventCommentCodeByUser(Long evEventId) throws Exception {
        return promotionEventMapper.getEventCommentCodeByUser(evEventId);
    }


    /**
     * 일반 이벤트 Validation 정보 조회
     *
     * @param evEventId Long
     * @param urUserId  Long
     * @return NormalValidationVo
     * @throws Exception Exception
     */
    protected NormalValidationVo getNormalJoinValidation(Long evEventId, Long urUserId) throws Exception {
        return getNormalJoinValidation(evEventId, urUserId, null);
    }

    protected NormalValidationVo getNormalJoinValidation(Long evEventId, Long urUserId, String selectCouponId) throws Exception {
        if (evEventId == null) return null;

        NormalValidationVo result = promotionEventMapper.getNormalJoinValidation(evEventId, urUserId);
        if (result != null) {
            result.setUserGroupList(promotionEventMapper.getUserGroup(evEventId));
        }

        if (result != null && EventEnums.EventBenefitType.COUPON.getCode().equals(result.getEventBenefitType())) {
            result.setCoupon(promotionEventMapper.getEventSelectCoupon(evEventId, null, selectCouponId));
        }

        return result;
    }

    /**
     * 일반이벤트 등록 Validation
     *
     * @param eventInfo NormalValidationVo
     * @param dto       NormalJoinRequestDto
     * @return EventEnums.AddJoinValidation
     */
    protected MessageCommEnum addNormalJoinValidation(NormalValidationVo eventInfo, NormalJoinRequestDto dto) throws Exception {
        if (eventInfo == null) return EventEnums.EventValidation.NO_EVENT;

        //금칙어 - 마스킹 하여 저장
        dto.setComment(policyBbsBannedWordBiz.filterSpamWord(dto.getComment(), BaseEnums.EnumSiteType.MALL));

        //댓글 구분 값
        if (EventEnums.NormalEventType.COMMENT.getCode().equals(eventInfo.getNormalEventType()) && "Y".equals(eventInfo.getCommentCodeYn())) {
            if (dto.getCommentValue() == null || StringUtil.isEmpty(dto.getCommentValue())) {
                return EventEnums.EventValidation.EMPTY_COMMENT_VALUE;
            }
        }

        //참여조건 - 주문고객
        if (EventEnums.JoinConditionType.ORDER.getCode().equals(eventInfo.getJoinCondition())) {
            // 기존 소스 OrderInfoFromStampPurchaseVo responseOrder = orderFrontBiz.getOrderInfoFromStampPurchase(dto.getUrUserId(), eventInfo.getStartDateTime(), eventInfo.getEndDateTime(), eventInfo.getOrderPrice());
            // 주문에 적용범위 적용
            OrderInfoFromStampPurchaseVo responseOrder = orderFrontBiz.getOrderCountFromNormalEvent(dto.getUrUserId(), dto.getEvEventId(), eventInfo.getStartDateTime(), eventInfo.getEndDateTime(), eventInfo.getOrderPrice(), eventInfo.getGoodsDeliveryTp());
            if (responseOrder.getOrderCount() < eventInfo.getOrderCount()) {
                return EventEnums.EventValidation.NEED_ORDER;
            }
        }

        return BaseEnums.Default.SUCCESS;
    }

    /**
     * 일반 이벤트 혜택발생
     *
     * @param eventInfo NormalValidationVo
     * @param dto       NormalJoinRequestDto
     * @return BenefitResponseDto
     * @throws Exception Exception
     */
    protected BenefitResponseDto addNormalBenefit(NormalValidationVo eventInfo, NormalJoinRequestDto dto) throws Exception {
        BenefitResponseDto result = new BenefitResponseDto();
        result.setEventBenefitType(eventInfo.getEventBenefitType());
        result.setWinnerYn("N");
        if (!eventInfo.getEventDrawType().equals(EventEnums.EventDrawType.AUTO.getCode())) return result;

        // 즉시 당첨 케이스
        if (eventInfo.getEventDrawType().equals(EventEnums.EventDrawType.AUTO.getCode())) {
            // 확률계산
            if (isNormalWinner(eventInfo)) {
                setEventBenefitNew(result, dto.getUrUserId(), dto.getEvEventId(), eventInfo.getEventBenefitType(), eventInfo.getPmPointId(), eventInfo.getBenefitName(), eventInfo.getCoupon());
            }
        }

        return result;
    }


    /**
     * 이벤트 참여정보 저장
     *
     * @param dto EventJoinVo
     * @throws Exception Exception
     */
    protected void addEventJoin(EventJoinVo dto) throws Exception {
        promotionEventMapper.addEventJoin(dto);
    }

    /**
     * 이벤트 참여정보 수정
     *
     * @param dto NormalJoinRequestDto
     * @throws Exception Exception
     */
    protected void putEventJoin(EventJoinVo dto) throws Exception {
        // 참여정보 저장
        promotionEventMapper.putEventJoin(dto);

        // 쿠폰정보 저장
        if (dto.getCoupon() == null) return;
        for (int i = 0; i < dto.getCoupon().size(); i++) {
            EventJoinCouponVo eventJoinCouponVo = EventJoinCouponVo.builder()
                    .evEventJoinId(dto.getEvEventJoinId())
                    .pmCouponId(dto.getCoupon().get(i).getPmCouponId())
                    .couponCount(dto.getCoupon().get(i).getCouponCount())
                    .sort(i + 1)
                    .build();
            promotionEventMapper.addEventJoinCoupon(eventJoinCouponVo);
        }
    }

    /**
     * 이벤트 댓글 목록조회
     *
     * @param dto EventJoinByUserRequestDto
     * @return EventJoinByUserResponseDto
     * @throws Exception Exception
     */
    protected EventJoinByUserResponseDto getNormalJoinByUser(EventJoinByUserRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<EventJoinByUserVo> vos = promotionEventMapper.getNormalJoinByUser(dto);

        return EventJoinByUserResponseDto.builder()
                .total((int) vos.getTotal())
                .comment(vos.getResult())
                .build();
    }

    /**
     * 스탬프 이벤트 조회
     *
     * @param evEventId  Long
     * @param urUserId   Long
     * @param deviceType String
     * @return StampByUserVo
     * @throws Exception Exception
     */
    protected StampByUserVo getStampByUser(Long evEventId, Long urUserId, String deviceType) throws Exception {
        StampByUserVo result = promotionEventMapper.getStampByUser(evEventId, urUserId, deviceType);
        if (result != null) {
            result.setUserGroupList(promotionEventMapper.getUserGroup(evEventId));
        }
        return result;
    }

    /**
     * 스탬프 상세 조회
     *
     * @param evEventStampId Long
     * @return List<StampDetailByUserVo>
     * @throws Exception Exception
     */
    protected List<StampDetailByUserVo> getStampDetailByUser(Long evEventStampId) throws Exception {
        return promotionEventMapper.getStampDetailByUser(evEventStampId);
    }

    /**
     * 스탬프 참여 정보 조회
     *
     * @param evEventId Long
     * @param urUserId  Long
     * @return List<Integer>
     * @throws Exception Exception
     */
    protected List<Integer> getStampJoinByUser(Long evEventId, Long urUserId) throws Exception {
        return promotionEventMapper.getStampJoinByUser(evEventId, urUserId);
    }

    /**
     * 스탬프 이벤트 Validation 정보 조회
     *
     * @param evEventId Long
     * @param urUserId  Long
     * @return StampValidationVo
     * @throws Exception Exception
     */
    protected StampValidationVo getStampValidation(Long evEventId, Long urUserId) throws Exception {
        if (evEventId == null) return null;

        StampValidationVo result = promotionEventMapper.getStampValidation(evEventId, urUserId);    // 스탬프 이벤트 정보
        if (result != null) {
            result.setUserGroupList(promotionEventMapper.getUserGroup(evEventId));    //접근권한 - 등급 기준

            //스탬프 상세 정보
            List<StampDetailValidation> stampDetailList = promotionEventMapper.getStampDetailValidation(result.getEvEventStampId());
            for (StampDetailValidation detailVo : stampDetailList) {
                // 쿠폰정보
                if (detailVo.getEventBenefitType().equals(EventEnums.EventBenefitType.COUPON.getCode())) {
                    detailVo.setCoupon(promotionEventMapper.getEventCoupon(evEventId, detailVo.getEvEventStampDetlId()));
                }
            }
            result.setStamp(stampDetailList);

            //스탬프 참여 정보
            result.setJoin(promotionEventMapper.getStampJoinValidationByUser(evEventId, urUserId));
        }

        return result;
    }

    /**
     * 스탬프 이벤트 Validation 참여 정보 조회
     *
     * @param evEventId Long
     * @param urUserId  Long
     * @return List<StampJoinByUserVo>
     * @throws Exception Exception
     */
    protected List<StampJoinByUserVo> getStampJoinValidationByUser(Long evEventId, Long urUserId) throws Exception {
        return promotionEventMapper.getStampJoinValidationByUser(evEventId, urUserId);
    }

    /**
     * 스탬프(출석) - 스탬프계산
     *
     * @param eventInfo StampValidationVo
     * @param dto       StampJoinRequestDto
     * @return int
     * @throws Exception Exception
     */
    protected int calcStampCount(StampValidationVo eventInfo, StampJoinRequestDto dto) throws Exception {
        int stampCount = 1;

        // 스탬프(출석) - 스탬프 번호 계산
        if (EventEnums.EventType.ATTEND.getCode().equals(eventInfo.getEventType())) {
            List<StampJoinByUserVo> joinList = eventInfo.getJoin();
            if (joinList != null && !joinList.isEmpty()) {
                stampCount = joinList.get(0).getStampCount() + 1;
            }
            return stampCount;
        }

        // 스탬프(미션) - 단순 참가 이력 저장, 단순 참가는 StampCount 0번
        if (EventEnums.EventType.MISSION.getCode().equals(eventInfo.getEventType())) {
            if (dto.getUrlVisitYn() == null || dto.getUrlVisitYn().equals("N")) {
                stampCount = 0;
            } else {
                stampCount = dto.getStampCount();
            }
            return stampCount;
        }

        // 스탬프(주문)
        if (EventEnums.EventType.PURCHASE.getCode().equals(eventInfo.getEventType())) {
            // 주문정보
            OrderInfoFromStampPurchaseVo orderInfo = orderFrontBiz.getOrderInfoFromStampPurchase(dto.getUrUserId(), eventInfo.getStartDateTime(), eventInfo.getEndDateTime(), eventInfo.getOrderPrice());

            // 스탬프 갯수 계산
            stampCount = 0;
            if (orderInfo != null) {
                stampCount = orderInfo.getOrderCount();
                if (stampCount > eventInfo.getStampCount2()) {
                    stampCount = eventInfo.getStampCount2();
                }
            }

            return stampCount;
        }

        return stampCount;
    }

    /**
     * 스탬프(출석) 혜택발생
     *
     * @param eventInfo StampValidationVo
     * @param dto       StampJoinRequestDto
     * @return BenefitResponseDto
     * @throws Exception Exception
     */
    protected BenefitResponseDto addStampAttendBenefit(StampValidationVo eventInfo, StampJoinRequestDto dto) throws Exception {
        BenefitResponseDto result = new BenefitResponseDto();
        result.setWinnerYn("N");

        //스탬프(출석) - 해당 구간 진입시 혜택 발행
        List<StampDetailValidation> stamp = eventInfo.getStamp();
        for (StampDetailValidation stampDetail : stamp) {
            if (stampDetail.getStampCount() == dto.getStampCount()) {
                // 스탬프 혜택 설정
                setEventBenefit(result, dto.getUrUserId(), dto.getEvEventId(), stampDetail.getEventBenefitType(), stampDetail.getPmPointId(), stampDetail.getBenefitName(), stampDetail.getCoupon());
                break;
            }
        }

        return result;
    }

    /**
     * 스탬프(미션) 혜택발생
     *
     * @param eventInfo StampValidationVo
     * @param dto       StampJoinRequestDto
     * @return BenefitResponseDto
     * @throws Exception Exception
     */
    protected BenefitResponseDto addStampMissionBenefit(StampValidationVo eventInfo, StampJoinRequestDto dto) throws Exception {
        BenefitResponseDto result = new BenefitResponseDto();
        result.setWinnerYn("N");

        // 스탬프(미션) - 각각의 미션별로 혜택 제공
        result.setStampCount(dto.getStampCount());
        List<StampDetailValidation> stampList = eventInfo.getStamp();
        for (StampDetailValidation stamp : stampList) {
            if (dto.getStampCount() == stamp.getStampCount()) {
                setEventBenefit(result, dto.getUrUserId(), dto.getEvEventId(), stamp.getEventBenefitType(), stamp.getPmPointId(), stamp.getBenefitName(), stamp.getCoupon());
                break;
            }
        }

        return result;
    }

    /**
     * 이벤트 혜택 설정
     *
     * @param result           BenefitResponseDto
     * @param urUserId         Long
     * @param evEventId        Long
     * @param eventBenefitType String
     * @param benefitId        Long
     * @param benefitName      String
     * @throws Exception Exception
     */
    private void setEventBenefit(BenefitResponseDto result, Long urUserId, Long evEventId, String eventBenefitType, Long benefitId, String benefitName, List<EventCouponVo> coupon) throws Exception {
        result.setEventBenefitType(eventBenefitType);
        result.setWinnerYn("Y");

        // 쿠폰
        if (eventBenefitType.equals(EventEnums.EventBenefitType.COUPON.getCode())) {
            List<EventCouponVo> resultCouponList = new ArrayList<>();
            for (EventCouponVo vo : coupon) {
                int count = 0;
                for (int i = 0; i < vo.getCouponCount(); i++) {
                    CouponEnums.AddCouponValidation couponResult = promotionCouponBiz.addCouponByOne(vo.getPmCouponId(), urUserId);
                    if (couponResult.equals(CouponEnums.AddCouponValidation.PASS_VALIDATION)) {
                        count++;
                    }
                }
                //쿠폰 결과 저장
                if (count > 0) {
                    EventCouponVo resultCoupon = new EventCouponVo();
                    resultCoupon.setPmCouponId(vo.getPmCouponId());
                    resultCoupon.setDisplayCouponName(vo.getDisplayCouponName());
                    resultCoupon.setCouponCount(count);
                    resultCouponList.add(resultCoupon);
                }
            }
            // 쿠폰 발급 실패 처리
            if (resultCouponList.size() == 0) {
                result.setWinnerYn("N");
            } else {
                String couponName = String.join(", ",
                        resultCouponList.stream()
                                .collect(Collectors.groupingBy(EventCouponVo::getDisplayCouponName))
                                .keySet());

                result.setBenefitName(couponName);
                result.setCoupon(resultCouponList);
            }
        }

        // 적립금
        if (eventBenefitType.equals(EventEnums.EventBenefitType.POINT.getCode())) {
            ApiResult<?> pointResult = pointBiz.depositEventPoint(urUserId, benefitId, evEventId, "");
            if (BaseEnums.Default.SUCCESS.equals(pointResult.getMessageEnum()) || PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT.equals(pointResult.getMessageEnum())) {
                result.setPmPointId(benefitId);
                result.setBenefitName(benefitName);
            } else {
                // 적립금 발급 실패 처리
                result.setWinnerYn("N");
            }
        }

        // 경품 OR 자동응모
        if (eventBenefitType.equals(EventEnums.EventBenefitType.GIFT.getCode())
                || eventBenefitType.equals(EventEnums.EventBenefitType.AUTO.getCode())) {
            result.setBenefitName(benefitName);
        }

        // 제공안함
        if (eventBenefitType.equals(EventEnums.EventBenefitType.NONE.getCode())) {
            result.setWinnerYn("N");
        }
    }

    /**
     * 이벤트 혜택 설정
     *
     * @param result           BenefitResponseDto
     * @param urUserId         Long
     * @param evEventId        Long
     * @param eventBenefitType String
     * @param benefitId        Long
     * @param benefitName      String
     * @throws Exception Exception
     */
    private void setEventBenefitNew(BenefitResponseDto result, Long urUserId, Long evEventId, String eventBenefitType, Long benefitId, String benefitName, List<EventCouponVo> coupon) throws Exception {
        result.setEventBenefitType(eventBenefitType);
        result.setWinnerYn("Y");

        // 쿠폰
        if (eventBenefitType.equals(EventEnums.EventBenefitType.COUPON.getCode())) {
            List<EventCouponVo> resultCouponList = new ArrayList<>();
            for (EventCouponVo vo : coupon) {
                // 총 인원수 체크 - 동시성 적용
                MessageCommEnum responseConcurrency = promotionEventConcurrencyBiz.concurrencyEventCoupon(vo.getEvEventCouponId());
                if (BaseEnums.Default.FAIL.equals(responseConcurrency)) {
                    continue;
                }

                int count = 0;
                for (int i = 0; i < vo.getCouponCount(); i++) {
                    CouponEnums.AddCouponValidation couponResult = promotionCouponBiz.addCouponByOne(vo.getPmCouponId(), urUserId);
                    if (couponResult.equals(CouponEnums.AddCouponValidation.PASS_VALIDATION)) {
                        count++;
                    }
                }
                //쿠폰 결과 저장
                if (count > 0) {
                    EventCouponVo resultCoupon = new EventCouponVo();
                    resultCoupon.setPmCouponId(vo.getPmCouponId());
                    resultCoupon.setDisplayCouponName(vo.getDisplayCouponName());
                    resultCoupon.setCouponCount(count);
                    resultCouponList.add(resultCoupon);
                }
            }
            // 쿠폰 발급 실패 처리
            if (resultCouponList.size() == 0) {
                result.setWinnerYn("N");
            } else {
                String couponName = String.join(", ",
                        resultCouponList.stream()
                                .collect(Collectors.groupingBy(EventCouponVo::getDisplayCouponName))
                                .keySet());

                result.setBenefitName(couponName);
                result.setCoupon(resultCouponList);
            }
        }

        // 적립금
        if (eventBenefitType.equals(EventEnums.EventBenefitType.POINT.getCode())) {
            // 총 인원수 체크 - 동시성 적용
            MessageCommEnum responseConcurrency = promotionEventConcurrencyBiz.concurrencyNormalBenefit(evEventId);
            if (BaseEnums.Default.FAIL.equals(responseConcurrency)) {
                result.setWinnerYn("N");
                return;
            }

            ApiResult<?> pointResult = pointBiz.depositEventPoint(urUserId, benefitId, evEventId, "");
            if (BaseEnums.Default.SUCCESS.equals(pointResult.getMessageEnum()) || PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT.equals(pointResult.getMessageEnum())) {
                result.setPmPointId(benefitId);
                result.setBenefitName(benefitName);
            } else {
                // 적립금 발급 실패 처리
                result.setWinnerYn("N");
            }
        }

        // 경품
        if (eventBenefitType.equals(EventEnums.EventBenefitType.GIFT.getCode())) {
            // 총 인원수 체크 - 동시성 적용
            MessageCommEnum responseConcurrency = promotionEventConcurrencyBiz.concurrencyNormalBenefit(evEventId);
            if (BaseEnums.Default.FAIL.equals(responseConcurrency)) {
                result.setWinnerYn("N");
                return;
            }
            result.setBenefitName(benefitName);
        }

        // 자동응모
        if (eventBenefitType.equals(EventEnums.EventBenefitType.AUTO.getCode())) {
            result.setBenefitName(benefitName);
        }

        // 제공안함
        if (eventBenefitType.equals(EventEnums.EventBenefitType.NONE.getCode())) {
            result.setWinnerYn("N");
        }
    }

    /**
     * 스탬프(구매) 이벤트 참여 저장
     *
     * @param dto StampJoinRequestDto
     * @throws Exception Exception
     */
    protected void putStampJoin(StampJoinRequestDto dto) throws Exception {
        promotionEventMapper.putStampJoin(dto);
    }

    /**
     * 룰렛 이벤트 조회
     *
     * @param evEventId  Long
     * @param urUserId   Long
     * @param deviceType String
     * @return RouletteByUserVo
     * @throws Exception Exception
     */
    protected RouletteByUserVo getRouletteByUser(Long evEventId, Long urUserId, String deviceType) throws Exception {
        RouletteByUserVo result = promotionEventMapper.getRouletteByUser(evEventId, urUserId, deviceType);
        if (result != null) {
            result.setUserGroupList(promotionEventMapper.getUserGroup(evEventId));
        }
        return result;
    }

    /**
     * 룰렛 이벤트 상세 조회
     *
     * @param evEventRouletteId Long
     * @return List<RouletteItemByUserVo>
     * @throws Exception Exception
     */
    protected List<RouletteItemByUserVo> getRouletteDetailByUser(Long evEventId, Long evEventRouletteId) throws Exception {
        List<RouletteItemByUserVo> responseList = promotionEventMapper.getRouletteDetailByUser(evEventRouletteId);
        for (RouletteItemByUserVo vo : responseList) {
            if (vo.getEventBenefitType().equals(EventEnums.EventBenefitType.COUPON.getCode())) {
                List<EventCouponVo> eventCouponVoList = promotionEventMapper.getEventCoupon(evEventId, vo.getEvEventRouletteItemId());
                if (eventCouponVoList != null && !eventCouponVoList.isEmpty()) {
                    vo.setCoupon(eventCouponVoList);
                    vo.setBenefitName(eventCouponVoList.get(0).getDisplayCouponName());
                    vo.setCouponDiscountType(eventCouponVoList.get(0).getDiscountType());
                    vo.setBenefitPrice(eventCouponVoList.get(0).getDiscountValue());
                }
            }
        }
        return responseList;
    }

    /**
     * 룰렛 이벤트 Validation 정보 조회
     *
     * @param evEventId Long
     * @param urUserId  Long
     * @return RouletteValidationVo
     * @throws Exception Exception
     */
    protected RouletteValidationVo getRouletteValidation(Long evEventId, Long urUserId) throws Exception {
        if (evEventId == null) return null;

        RouletteValidationVo result = promotionEventMapper.getRouletteValidation(evEventId, urUserId);
        if (result == null) return null;
        result.setUserGroupList(promotionEventMapper.getUserGroup(evEventId));

        List<RouletteItemValidation> itemList = promotionEventMapper.getRouletteDetailValidation(result.getEvEventRouletteId());
        for (RouletteItemValidation vo : itemList) {
            if (vo.getEventBenefitType().equals(EventEnums.EventBenefitType.COUPON.getCode())) {
                List<EventCouponVo> eventCouponVoList = promotionEventMapper.getEventCoupon(evEventId, vo.getEvEventRouletteItemId());
                if (eventCouponVoList != null && !eventCouponVoList.isEmpty()) {
                    vo.setCoupon(eventCouponVoList);
                }
            }
        }
        result.setItem(itemList);

        return result;
    }

    /**
     * 룰렛 이벤트 혜택발생
     *
     * @param eventInfo RouletteValidationVo
     * @param dto       RouletteJoinRequestDto
     * @return BenefitResponseDto
     * @throws Exception Exception
     */
    protected BenefitResponseDto processRouletteBenefit(RouletteValidationVo eventInfo, RouletteJoinRequestDto dto) throws Exception {
        BenefitResponseDto result = new BenefitResponseDto();
        result.setWinnerYn("N");

        // 룰렛 계산 대상 아이템 선정
        List<RouletteItemValidation> itemList = eventInfo.getItem().stream()
                .filter(v -> v.getAwardMaxCount() > v.getWinnerCount())
                .collect(Collectors.toList());

        // 참여제한 회원 룰렛 처리
        if (eventInfo.getEventNotAvailableYn().equals("Y")) {
            List<RouletteItemValidation> eventNotItem = itemList.stream()
                    .filter(vo -> vo.getItemNumber().equals(eventInfo.getExceptionUserRouletteCount()))
                    .collect(Collectors.toList());

            if (eventNotItem.size() > 0) {
                // 참여제한용 당첨정보 인원수 확인
                int eventNotItemCount = promotionEventMapper.putEventBenefitInfoConcurrency(EventBenefitInfoRequestDto.builder()
                        .evEventId(dto.getEvEventId())
                        .evEventRouletteItemId(eventNotItem.get(0).getEvEventRouletteItemId())
                        .build());

                if (eventNotItemCount != 0) {
                    result.setEvEventRouletteItemId(eventNotItem.get(0).getEvEventRouletteItemId());
                    setEventBenefit(result, dto.getUrUserId(), dto.getEvEventId(), eventNotItem.get(0).getEventBenefitType(), eventNotItem.get(0).getPmPointId(), eventNotItem.get(0).getBenefitName(), eventNotItem.get(0).getCoupon());

                    // 당첨정보가 있을경우 Return
                    if (EventEnums.EventBenefitType.NONE.getCode().equals(result.getEventBenefitType()) ||
                            (!EventEnums.EventBenefitType.NONE.getCode().equals(result.getEventBenefitType()) && "Y".equals(result.getWinnerYn()))) {

                        return result;
                    }
                }
            }
        }

        // 회원 룰렛 처리
        if (itemList.size() > 0 && eventInfo.getEventNotAvailableYn().equals("N")) {
            // 룰렛 계산
            RouletteItemValidation rouletteResult = calculationRouletteWinner(itemList);

            // 동시성 처리
            if (rouletteResult != null) {
                MessageCommEnum eventBenefitInfoVo = promotionEventConcurrencyBiz.concurrencyEventBenefit(EventBenefitInfoRequestDto.builder()
                        .evEventId(dto.getEvEventId())
                        .evEventRouletteItemId(rouletteResult.getEvEventRouletteItemId())
                        .build());
                if (BaseEnums.Default.FAIL.equals(eventBenefitInfoVo)) {
                    rouletteResult = null;
                }
            }

            if (rouletteResult != null) {
                // 당첨 일 경우
                result.setEvEventRouletteItemId(rouletteResult.getEvEventRouletteItemId());
                setEventBenefit(result, dto.getUrUserId(), dto.getEvEventId(), rouletteResult.getEventBenefitType(), rouletteResult.getPmPointId(), rouletteResult.getBenefitName(), rouletteResult.getCoupon());

                // 당첨정보가 있을경우 Return
                if (EventEnums.EventBenefitType.NONE.getCode().equals(result.getEventBenefitType()) ||
                        (!EventEnums.EventBenefitType.NONE.getCode().equals(result.getEventBenefitType()) && "Y".equals(result.getWinnerYn()))) {
                    return result;
                }
            }
        }

        // 당첨 정보가 없을 경우 아이템중 확률 높고 확률 동일 할 경우 당첨수 많은 아이템 - 주의 : 제한인원수 체크 없음(요구사항 : 무조건 아이템 하나 선택되어야 하는 구조)
        Comparator<RouletteItemValidation> sort = (o1, o2) -> {
            if (o1.getAwardRate().equals(o2.getAwardRate())) {
                return o1.getAwardMaxCount() > o2.getAwardMaxCount() ? -1 : 0;
            } else {
                return o1.getAwardRate() > o2.getAwardRate() ? -1 : 1;
            }
        };

        List<RouletteItemValidation> zeroItem = eventInfo.getItem().stream()
                .filter(vo -> vo.getAwardMaxCount() > 1)   // 방어로직 : 당첨인원수 1 초과만 사용
                .sorted(sort)
                .collect(Collectors.toList());

        if (!zeroItem.isEmpty()) {
            int putResult = promotionEventMapper.putEventBenefitInfoConcurrency(EventBenefitInfoRequestDto.builder()
                    .evEventId(dto.getEvEventId())
                    .evEventRouletteItemId(zeroItem.get(0).getEvEventRouletteItemId())
                    .build());

            if (putResult != 0) {
                result.setEvEventRouletteItemId(zeroItem.get(0).getEvEventRouletteItemId());
                result.setEventBenefitType(zeroItem.get(0).getEventBenefitType());
                setEventBenefit(result, dto.getUrUserId(), dto.getEvEventId(), zeroItem.get(0).getEventBenefitType(), zeroItem.get(0).getPmPointId(), zeroItem.get(0).getBenefitName(), zeroItem.get(0).getCoupon());
            }
        }

        return result;
    }

    /**
     * 일반이벤트 혜택 제공여부
     *
     * @param eventInfo NormalValidationVo
     * @return boolean
     */
    private boolean isNormalWinner(NormalValidationVo eventInfo) {
        if (eventInfo.getAwardRate() == null) return true;
        if (eventInfo.getAwardRate() >= 100) return true;
        if (eventInfo.getAwardRate() <= 0) return false;

        int ratioCorrectionValue = calculationRatioCorrectionValue(eventInfo.getExpectJoinUserCount());
        long trueRate = (long) ((eventInfo.getAwardRate() / 100) * eventInfo.getExpectJoinUserCount() * ratioCorrectionValue);
        long falseRate = ((long) eventInfo.getExpectJoinUserCount() * ratioCorrectionValue) - trueRate;

        // request 설정
        List<CalcBenefitRequestDto> requestList = new ArrayList<>();
        requestList.add(CalcBenefitRequestDto.builder().id(0).rate(trueRate).build());
        requestList.add(CalcBenefitRequestDto.builder().id(1).rate(falseRate).build());

        return calculationRatio(requestList, eventInfo.getExpectJoinUserCount()) == 0;
    }

    private int calculationRatioCorrectionValue(int expectJoinUserCount) {
        int ratioCorrectionValue = 1;
        // 소숫점이하 자리수 보정
        if (expectJoinUserCount < PromotionConstants.EVENT_RATIO_DECIMAL) {
            ratioCorrectionValue = PromotionConstants.EVENT_RATIO_DECIMAL;
        }
        // 퍼센트(100) 기준 보정
        if (expectJoinUserCount < 100) {
            ratioCorrectionValue = ratioCorrectionValue * 100;
        }
        return ratioCorrectionValue;
    }

    /**
     * 룰렛 이벤트 아이템 선정
     *
     * @param itemList List<RouletteItemValidation>
     * @return RouletteItemValidation
     */
    private RouletteItemValidation calculationRouletteWinner(List<RouletteItemValidation> itemList) {
        if (itemList.isEmpty()) return new RouletteItemValidation();

        // request 설정
        List<CalcBenefitRequestDto> requestList = new ArrayList<>();
        int ratioCorrectionValue = calculationRatioCorrectionValue(itemList.get(0).getExpectJoinUserCount());
        for (int i = 0; i < itemList.size(); i++) {
            long rate = (long) ((itemList.get(i).getAwardRate() / 100) * itemList.get(i).getExpectJoinUserCount() * ratioCorrectionValue); // 예상참여인원 대비 비중 계산
            requestList.add(CalcBenefitRequestDto.builder().id(i).rate(rate).build());
        }

        int result = calculationRatio(requestList, itemList.get(0).getExpectJoinUserCount());
        if (result == -1) {
            return null;
        } else {
            return itemList.get(result);
        }
    }

    /**
     * 확률 계산
     *
     * @param requestDto          List<CalcBenefitRequestDto>
     * @param expectJoinUserCount int
     * @return int
     */
    private int calculationRatio(List<CalcBenefitRequestDto> requestDto, int expectJoinUserCount) {
        int ratioCorrectionValue = calculationRatioCorrectionValue(expectJoinUserCount);
        long targetUserCount = (long) expectJoinUserCount * ratioCorrectionValue;    // 당첨확률 소수점이하 자리수 보정

        // List 설정
        List<CalcBenefitRequestDto> targetList = new ArrayList<>();
        for (long i = 0; i < targetUserCount; i++) {
            for (CalcBenefitRequestDto dto : requestDto) {
                targetList.add(CalcBenefitRequestDto.builder().id(dto.getId()).rate(dto.getRate()).count(i + 1).build());
            }
        }

        // List Random
        Collections.shuffle(targetList);

        // 당첨정보 return
        for (CalcBenefitRequestDto item : targetList) {
            if (item.getRate() >= item.getCount()) {
                return item.getId();
            }
        }

        return -1;
    }

    /**
     * 설문 이벤트 조회
     *
     * @param evEventId  Long
     * @param urUserId   Long
     * @param deviceType String
     * @return SurveyByUserVo
     * @throws Exception Exception
     */
    protected SurveyByUserVo getSurveyByUser(Long evEventId, Long urUserId, String deviceType) throws Exception {
        SurveyByUserVo result = promotionEventMapper.getSurveyByUser(evEventId, urUserId, deviceType);
        if (result != null) {
            result.setUserGroupList(promotionEventMapper.getUserGroup(evEventId));
        }
        return result;
    }

    /**
     * 설문 문항 조회
     *
     * @param evEventId Long
     * @return List<SurveyQuestionByUserVo>
     * @throws Exception Exception
     */
    protected List<SurveyQuestionByUserVo> getSurveyQuestionByUser(Long evEventId) throws Exception {
        return promotionEventMapper.getSurveyQuestionByUser(evEventId);
    }

    /**
     * 설문 문항 보기 조회
     *
     * @param evEventSurveyQuestionId Long
     * @return List<SurveyItemByUserVo>
     * @throws Exception Exception
     */
    protected List<SurveyItemByUserVo> getSurveyItemByUser(Long evEventSurveyQuestionId) throws Exception {
        return promotionEventMapper.getSurveyItemByUser(evEventSurveyQuestionId);
    }

    /**
     * 설문 이벤트 Validation 정보 조회
     *
     * @param evEventId Long
     * @param urUserId  Long
     * @return SurveyValidationVo
     * @throws Exception Exception
     */
    protected SurveyValidationVo getSurveyJoinValidation(Long evEventId, Long urUserId) throws Exception {
        if (evEventId == null) return null;

        SurveyValidationVo result = promotionEventMapper.getSurveyValidation(evEventId, urUserId);
        if (result != null) {
            result.setUserGroupList(promotionEventMapper.getUserGroup(evEventId));
        }
        if (result != null && result.getEventBenefitType().equals(EventEnums.EventBenefitType.COUPON.getCode())) {
            result.setCoupon(promotionEventMapper.getEventCoupon(evEventId, null));
        }

        return result;
    }

    /**
     * 설문 이벤트 혜택발생
     *
     * @param eventInfo SurveyValidationVo
     * @param dto       SurveyJoinRequestDto
     * @return BenefitResponseDto
     * @throws Exception Exception
     */
    protected BenefitResponseDto addSurveyBenefit(SurveyValidationVo eventInfo, SurveyJoinRequestDto dto) throws Exception {
        BenefitResponseDto result = new BenefitResponseDto();
        result.setEventBenefitType(eventInfo.getEventBenefitType());
        result.setWinnerYn("N");
        if (!eventInfo.getEventDrawType().equals(EventEnums.EventDrawType.AUTO.getCode())) return result;

        // 즉시 당첨 케이스
        if (eventInfo.getEventDrawType().equals(EventEnums.EventDrawType.AUTO.getCode())) {
            setEventBenefit(result, dto.getUrUserId(), dto.getEvEventId(), eventInfo.getEventBenefitType(), eventInfo.getPmPointId(), eventInfo.getBenefitName(), eventInfo.getCoupon());
        }

        return result;
    }

    /**
     * 설문 이벤트 참여 저장
     *
     * @param dto SurveyJoinRequestDto
     * @throws Exception Exception
     */
    protected void addSurveyJoinDetail(Long evEventJoinId, List<SurveyJoinDetailRequestDto> list) throws Exception {
        promotionEventMapper.addSurveyJoinDetail(evEventJoinId, list);
    }

    /**
     * 체험단 이벤트 조회
     *
     * @param evEventId  Long
     * @param urUserId   Long
     * @param deviceType String
     * @return ExperienceByUserVo
     * @throws Exception Exception
     */
    protected ExperienceByUserVo getExperienceByUser(Long evEventId, Long urUserId, String deviceType) throws Exception {
        ExperienceByUserVo result = promotionEventMapper.getExperienceByUser(evEventId, urUserId, deviceType);
        if (result != null) {
            result.setUserGroupList(promotionEventMapper.getUserGroup(evEventId));
        }
        return result;
    }

    /**
     * 체험단 댓글 목록조회
     *
     * @param dto EventJoinByUserRequestDto
     * @return EventJoinByUserResponseDto
     * @throws Exception Exception
     */
    protected EventJoinByUserResponseDto getExperienceJoinByUser(EventJoinByUserRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<EventJoinByUserVo> vos = promotionEventMapper.getExperienceJoinByUser(dto);

        return EventJoinByUserResponseDto.builder()
                .total((int) vos.getTotal())
                .comment(vos.getResult())
                .build();
    }

    /**
     * 체험단 이벤트 Validation 정보 조회
     *
     * @param evEventId Long
     * @param urUserId  Long
     * @return ExperienceValidationVo
     * @throws Exception Exception
     */
    protected ExperienceValidationVo getExperienceJoinValidation(Long evEventId, Long urUserId) throws Exception {
        if (evEventId == null) return null;
        ExperienceValidationVo result = promotionEventMapper.getExperienceValidation(evEventId, urUserId);
        if (result != null) {
            result.setUserGroupList(promotionEventMapper.getUserGroup(evEventId));
        }
        if (result != null && result.getEventDrawType().equals(EventEnums.EventDrawType.FIRST_COME.getCode())) {
            String couponName = promotionCouponBiz.getCouponNameById(result.getPmCouponId());

            List<EventCouponVo> eventCouponVoList = new ArrayList<>();
            EventCouponVo eventCouponVo = new EventCouponVo();
            eventCouponVo.setPmCouponId(result.getPmCouponId());
            eventCouponVo.setDisplayCouponName(couponName);
            eventCouponVo.setCouponCount(1);
            eventCouponVoList.add(eventCouponVo);

            result.setCoupon(eventCouponVoList);
        }


        return result;
    }

    /**
     * 체험단 이벤트 등록 Validation
     *
     * @param eventInfo ExperienceValidationVo
     * @param dto       ExperienceJoinRequestDto
     * @return EventEnums.AddJoinValidation
     */
    protected MessageCommEnum addExperienceJoinValidation(ExperienceValidationVo eventInfo, ExperienceJoinRequestDto dto) {
        if (eventInfo == null) return EventEnums.EventValidation.NO_EVENT;

        //선착순 마감 여부
        if (eventInfo.getEventDrawType().equals(EventEnums.EventDrawType.FIRST_COME.getCode())) {
            if (eventInfo.getRecruitCloseYn().equals("Y")) {
                return EventEnums.EventValidation.FIRST_COME_CLOSE;
            }
        }

        //관리자 추첨의 경우만 댓글 있음
        if (eventInfo.getEventDrawType().equals(EventEnums.EventDrawType.ADMIN.getCode())) {
            //금칙어 - 마스킹 하여 저장
            dto.setComment(policyBbsBannedWordBiz.filterSpamWord(dto.getComment(), BaseEnums.EnumSiteType.MALL));

            if (eventInfo.getCommentCodeYn().equals("Y")) {
                //댓글 구분 값
                if (dto.getCommentValue() == null || dto.getCommentValue().equals("")) {
                    return EventEnums.EventValidation.EMPTY_COMMENT_VALUE;
                }
            }
        }

        return BaseEnums.Default.SUCCESS;
    }

    /**
     * 체험단 이벤트 혜택발생
     *
     * @param eventInfo SurveyValidationVo
     * @param dto       SurveyJoinRequestDto
     * @return BenefitResponseDto
     * @throws Exception Exception
     */
    protected BenefitResponseDto addExperienceBenefit(ExperienceValidationVo eventInfo, ExperienceJoinRequestDto dto) throws Exception {
        BenefitResponseDto result = new BenefitResponseDto();
        result.setWinnerYn("N");

        // 선착순 당첨 케이스
        if (!eventInfo.getEventDrawType().equals(EventEnums.EventDrawType.FIRST_COME.getCode())) return result;

        result.setEventBenefitType(EventEnums.EventBenefitType.COUPON.getCode());
        setEventBenefit(result, dto.getUrUserId(), dto.getEvEventId(), EventEnums.EventBenefitType.COUPON.getCode(), null, null, eventInfo.getCoupon());

        return result;
    }

    /**
     * 체험단 이벤트 선착순 마감 처리
     *
     * @param evEventId Long
     * @throws Exception Exception
     */
    protected void putEventExperienceRecruitClose(Long evEventId) throws Exception {
        promotionEventMapper.putEventExperienceRecruitClose(evEventId);
    }

    /**
     * 이벤트 조회 Validation
     *
     * @param dto EventValidationRequestDto
     * @return EventEnums.GetEventValidation
     */
    protected MessageCommEnum eventValidation(EventValidationRequestDto dto, String validationType) throws Exception {
        if (dto == null) return EventEnums.EventValidation.NO_EVENT;

        //이벤트 기간
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        LocalDate eventStartDate = LocalDate.parse(dto.getStartDate(), dateFormatter);
        if (now.isBefore(eventStartDate)) {                   // 시작일 이전
            return EventEnums.EventValidation.NOT_DATE;
        }

        if (validationType.equals(EventEnums.EventValidationType.ADD.getCode())) {
            if (dto.getEndYn().equals("Y")) {               // 종료일 지남
                return EventEnums.EventValidation.NOT_DATE;
            }
        }

        // 앱전용 이벤트 확인
        boolean isDeviceAppOnlyEvent = isAppOnlyEvent(dto.getDisplayWebPcYn(), dto.getDisplayWebMobileYn(), dto.getDisplayAppYn());

        // 앱전용 이벤트 APP 디바이스 아닐때
        if (validationType.equals(EventEnums.EventValidationType.ADD.getCode()) && isDeviceAppOnlyEvent && !GoodsEnums.DeviceType.APP.getCode().equals(dto.getDeviceType())) {
            return EventEnums.EventValidation.NOT_DEVICE_APP_ONLY_EVENT;
        }

        // 로그인 확인
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        if (validationType.equals(EventEnums.EventValidationType.ADD.getCode()) && (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId()))) {
            return UserEnums.Buyer.NEED_LOGIN;
        }

        if (validationType.equals(EventEnums.EventValidationType.GET.getCode())) {
            if (dto.getUserGroupList() != null && dto.getUserGroupList().size() > 0) {  // 접근제한 : 회원 등급 있을경우 비회원 체크
                //비회원 확인
                if (dto.getUrUserId() == 0L) {
                    return EventEnums.EventValidation.NOT_GROUP_NONE;
                }
            }
        }

        // 신규 회원가입 확인
        if ("Y".equals(dto.getCheckNewUserYn())) {
            if (!"Y".equals(dto.getNewUserYn())) {
                return EventEnums.EventValidation.NOT_NEW_USER;
            }
        }

        if (validationType.equals(EventEnums.EventValidationType.ADD.getCode())) {
            //이벤트 참여 제한 회원 - 룰렛이벤트 참여가능 -- SPEC OUT 2021.03.11
//            if (dto.getEventType() == null || !dto.getEventType().equals(EventEnums.EventType.ROULETTE.getCode())) {
//                if (dto.getEventNotAvailableYn().equals("Y")) {
//                    return EventEnums.EventValidation.NOT_AVAILABLE;
//                }
//            }

            //참여정보
            if (dto.getEventJoinType().equals(EventEnums.EventJoinType.DAY_1.getCode())) {
                if (dto.getUserJoinCount() > 0) {
                    return EventEnums.EventValidation.ALREADY_JOIN_DATE;
                }
                //CICD 참여여부 확인
                List<String> eventCicdList = Arrays.stream(policyConfigBiz.getConfigValue(Constants.EVENT_CICD_KEY).split(",")).collect(Collectors.toList());
                if (EventEnums.EventTp.NORMAL.getCode().equals(dto.getEventType()) ||
                        EventEnums.EventTp.MISSION.getCode().equals(dto.getEventType()) ||
                        EventEnums.EventTp.ATTEND.getCode().equals(dto.getEventType()) ||
                        EventEnums.EventTp.ROULETTE.getCode().equals(dto.getEventType())) { // 대상 이벤트 선정
                    if (eventCicdList.contains(String.valueOf(dto.getEvEventId()))) {
                        if (dto.getCicdCount() > 0) {
                            return EventEnums.EventValidation.ALREADY_JOIN_DATE;
                        }
                    }
                }
            }
            if (dto.getEventJoinType().equals(EventEnums.EventJoinType.RANGE_1.getCode())) {
                if (dto.getUserJoinCount() > 0) {
                    return EventEnums.EventValidation.ALREADY_JOIN_EVENT;
                }
                //CICD 참여여부 확인
                List<String> eventCicdList = Arrays.stream(policyConfigBiz.getConfigValue(Constants.EVENT_CICD_KEY).split(",")).collect(Collectors.toList());
                if (EventEnums.EventTp.NORMAL.getCode().equals(dto.getEventType()) ||
                        EventEnums.EventTp.MISSION.getCode().equals(dto.getEventType()) ||
                        EventEnums.EventTp.ATTEND.getCode().equals(dto.getEventType()) ||
                        EventEnums.EventTp.ROULETTE.getCode().equals(dto.getEventType())) { // 대상 이벤트 선정
                    if (eventCicdList.contains(String.valueOf(dto.getEvEventId()))) {
                        if (dto.getCicdCount() > 0) {
                            return EventEnums.EventValidation.ALREADY_JOIN_EVENT;
                        }
                    }
                }
            }
        }

        //노출범위 : 임직원 전용
        if (dto.getEvEmployeeType().equals(EventEnums.EvEmployeeType.EMPLOYEE_ONLY.getCode())) {
            if (!dto.getUserStatus().equals(UserEnums.UserStatusType.EMPLOYEE.getCode())) {
                return EventEnums.EventValidation.ONLY_EMPLOYEE;
            }
        }

        //노출범위 : 임직원 제외 (회원전용) - 비회원, 회원 상세진입 가능
        if (dto.getEvEmployeeType().equals(EventEnums.EvEmployeeType.EMPLOYEE_EXCEPT.getCode())) {
            if (dto.getUserStatus().equals(UserEnums.UserStatusType.EMPLOYEE.getCode())) {
                return EventEnums.EventValidation.NOT_EMPLOYEE;
            }
        }

        //임직원 참여 여부
        if (validationType.equals(EventEnums.EventValidationType.ADD.getCode())) {
            //노출범위 : 임직원 제외 (회원전용) - 회원만 참여 가능
            if (dto.getEvEmployeeType().equals(EventEnums.EvEmployeeType.EMPLOYEE_EXCEPT.getCode())) {
                if (!dto.getUserStatus().equals(UserEnums.UserStatusType.MEMBER.getCode())) {
                    return EventEnums.EventValidation.NOT_EMPLOYEE;
                }
            }
            if (dto.getEmployeeJoinYn().equals("N")) {
                if (dto.getUserStatus().equals(UserEnums.UserStatusType.EMPLOYEE.getCode())) {
                    return EventEnums.EventValidation.NOT_EMPLOYEE;
                }
            }
        }

        //디바이스
        if (dto.getDisplayWebPcYn().equals("N")) {
            if (dto.getDeviceType().equals(GoodsEnums.DeviceType.PC.getCode()) && !isDeviceAppOnlyEvent) {
                return EventEnums.EventValidation.NOT_DEVICE;
            }
        }
        if (dto.getDisplayWebMobileYn().equals("N")) {
            if (dto.getDeviceType().equals(GoodsEnums.DeviceType.MOBILE.getCode()) && !isDeviceAppOnlyEvent) {
                return EventEnums.EventValidation.NOT_DEVICE;
            }
        }
        if (dto.getDisplayAppYn().equals("N")) {
            if (dto.getDeviceType().equals(GoodsEnums.DeviceType.APP.getCode())) {
                return EventEnums.EventValidation.NOT_DEVICE;
            }
        }

        //등급
        if (dto.getUserGroupList() != null && dto.getUserGroupList().size() > 0) {
            List<Long> userGroupIdList = dto.getUserGroupList().stream()
                    .map(EventUserGroupByUserVo::getUrGroupId)
                    .collect(Collectors.toList());
            if (userGroupIdList.size() > 0 && !userGroupIdList.contains(dto.getUrGroupId())) {
                return EventEnums.EventValidation.NOT_GROUP;
            }
        }

        // 혜택완료여부 확인
        if (validationType.equals(EventEnums.EventValidationType.ADD.getCode())) {  //참여
            if (EventEnums.EventTp.NORMAL.getCode().equals(dto.getEventType())) {   // 일반이벤트
                if (EventEnums.EventDrawType.AUTO.getCode().equals(dto.getEventDrawType()) && "N".equals(dto.getEndYn())) {    // 당첨자설정 : 즉시당첨 이면서 진행중
                    if ((EventEnums.EventBenefitType.COUPON.getCode().equals(dto.getEventBenefitType())
                            || EventEnums.EventBenefitType.POINT.getCode().equals(dto.getEventBenefitType())
                            || EventEnums.EventBenefitType.GIFT.getCode().equals(dto.getEventBenefitType()))) {    //쿠폰, 적립금, 경품
                        if (0 >= dto.getRemainBenefitCount()) { // 혜택 소진
                            if(dto.getSelectCouponId() != null) { // 쿠폰을 선택한 경우는 모든 쿠폰이 소진되었다고 별도로 표시
                                return EventEnums.EventValidation.FULL_ALL_COUPON;
                            } else {
                                return EventEnums.EventValidation.NOT_REMAIN_BENEFIT;
                            }
                        }
                    }
                    // 발급 가능한 쿠폰 갯수가 한개이면서 발급가능 쿠폰 수보다 참여쿠폰이 같거나 큰 경우
                    // 발급 가능한 쿠폰이 여러개인 경우에는 최대치를 초과하는 쿠폰을 제외하고 다운로드가 가능함
                    if (EventEnums.EventBenefitType.COUPON.getCode().equals(dto.getEventBenefitType())
                            && dto.getCouponList() != null
                            && dto.getCouponList().size() == 1) {
                        for (EventCouponVo eventCouponVo : dto.getCouponList()) {
                            if (eventCouponVo.getCouponJoinCount() >= eventCouponVo.getCouponTotalCount()) {
                                return EventEnums.EventValidation.FULL_COUPON;
                            }
                        }
                    }
                }
            }
        }

        return BaseEnums.Default.SUCCESS;
    }

    /**
     * APP 전용 이벤트
     *
     * @param displayWebPcYn
     * @param displayWebMobileYn
     * @param displayAppYn
     * @return
     */
    protected boolean isAppOnlyEvent(String displayWebPcYn, String displayWebMobileYn, String displayAppYn) {

        return ("N".equals(displayWebPcYn) && "N".equals(displayWebMobileYn) && "Y".equals(displayAppYn)) ? true : false;
    }

    /**
     * APP 전용 이벤트 디바이스 아닌 경우
     *
     * @param appOnlyEventYn
     * @param deviceType
     * @return
     */
    protected String getNotDeviceAppOnlyEventYn(String appOnlyEventYn, String deviceType) {

        return ("Y".equals(appOnlyEventYn) && !GoodsEnums.DeviceType.APP.getCode().equals(deviceType)) ? "Y" : "N";
    }

    /**
     * 스탬프 미션 이벤트 정보 조회 - common
     *
     * @param urUserId   Long
     * @param deviceType String
     * @return List<MissionStampByUserVo>
     * @throws Exception Exception
     */
    protected List<MissionStampByUserResponseDto> getMissionStampByUser(Long urUserId, String deviceType, CodeCommEnum userStatus, long userGroup) throws Exception {
        List<MissionStampByUserVo> eventInfo = promotionEventMapper.getMissionStampByUser(urUserId, deviceType, userStatus);
        List<MissionStampByUserResponseDto> result = new ArrayList<>();

        for (MissionStampByUserVo vo : eventInfo) {
            //접근 권한 제외처리
            List<EventUserGroupByUserVo> userGroupList = promotionEventMapper.getUserGroup(vo.getEvEventId());
            if (userGroupList != null && userGroupList.size() > 0) {
                List<Long> userGroupIdList = userGroupList.stream()
                        .map(EventUserGroupByUserVo::getUrGroupId)
                        .collect(Collectors.toList());
                if (userGroupIdList.size() > 0 && !userGroupIdList.contains(userGroup)) {
                    continue;
                }

                // 임직원 회원 - 임직원 제외 처리
                if (userStatus.equals(UserEnums.UserStatusType.EMPLOYEE) && vo.getEvEmployeeType().equals(EventEnums.EvEmployeeType.EMPLOYEE_EXCEPT.getCode())) {
                    continue;
                }
            }

            // 1일1회 인경우 미션스탬프 모두 제외처리
            if (vo.getEventJoinType().equals((EventEnums.EventJoinType.DAY_1.getCode()))) {
                if (vo.getUserJoinTodayCount() > 0) continue;
            }
            // 기존에 참여한 번호의 경우 제외처리
            if (vo.getUserJoinItemCount() > 0) continue;

            // URL 나누기
            String stampUrl = vo.getStampUrl();
            if (stampUrl.contains(",") || stampUrl.contains("\n")) {
                stampUrlSplit(result, vo, stampUrl);
            } else {
                result.add(new MissionStampByUserResponseDto(vo));
            }
        }

        return result;
    }

    /**
     * 스탬프 이벤트 URL 분리
     *
     * @param result   List<MissionStampByUserResponseDto>
     * @param vo       MissionStampByUserVo
     * @param stampUrl String
     */
    private void stampUrlSplit(List<MissionStampByUserResponseDto> result, MissionStampByUserVo vo, String stampUrl) {
        String[] sample = stampUrl.split(",|\n");
        for (String url : sample) {
            if (url.length() > 0) {
                MissionStampByUserVo urlVo = new MissionStampByUserVo();
                urlVo.setEvEventId(vo.getEvEventId());
                urlVo.setStampCount(vo.getStampCount());
                urlVo.setTotalStampCount(vo.getTotalStampCount());
                urlVo.setJoinStampCount(vo.getJoinStampCount());
                urlVo.setIconPath(vo.getIconPath());
                urlVo.setStampUrl(url);
                result.add(new MissionStampByUserResponseDto(urlVo));
            }
        }
    }

    /**
     * 이벤트명 조회
     *
     * @param evEventId  Long
     * @param deviceType String
     * @return String
     */
    protected EventInfoFromMetaVo getEventInfoFromMeta(Long evEventId, String deviceType) throws Exception {
        return promotionEventMapper.getEventInfoFromMeta(evEventId, deviceType);
    }

    /**
     * 룰렛 이벤트 Validation
     *
     * @param evEventId Long
     * @return EventEnums.EventValidation
     */
    protected MessageCommEnum eventRouletteValidation(Long evEventId) throws Exception {
        // 잔여 참여가능 횟수 확인
        int remainBenefitCount = promotionEventMapper.getEventBenefitCount(evEventId);
        if (remainBenefitCount == 0) {
            return EventEnums.EventValidation.FULL_BENEFIT;
        }

        return BaseEnums.Default.SUCCESS;
    }

    /**
     * 이벤트 결과값 제외처리
     *
     * @param validation           MessageCommEnum
     * @param validationRequestDto EventValidationRequestDto
     * @return String
     */
    protected String getEventReturnData(MessageCommEnum validation, EventValidationRequestDto validationRequestDto) {
        if (validation.getCode().equals(EventEnums.EventValidation.NOT_GROUP.getCode())) {
            return validationRequestDto.getUserGroupList().stream()
                    .map(vo -> vo.getGroupMasterName() + "_" + vo.getGroupName())
                    .collect(Collectors.joining(", "));
        }

        if (validation.getCode().equals(EventEnums.EventValidation.NOT_DEVICE.getCode())) {
            Set<String> deviceSet = new HashSet<>(Arrays.asList("PC", "MOBILE", "APP"));
            if (validationRequestDto.getDisplayWebPcYn().equals("N")) {
                deviceSet.remove("PC");
            }
            if (validationRequestDto.getDisplayWebMobileYn().equals("N")) {
                deviceSet.remove("MOBILE");
            }
            if (validationRequestDto.getDisplayAppYn().equals("N")) {
                deviceSet.remove("APP");
            }
            return deviceSet.toString();
        }
        return "";
    }

    /**
     * 이벤트 validation 처리 공통 함수
     */
    protected ApiResult validPromotionEvent(EventRequestDto dto, EventValidationRequestDto validationRequestDto) throws Exception {
        // Event Validation Check
        MessageCommEnum validation = eventValidation(validationRequestDto, EventEnums.EventValidationType.GET.getCode());
        if (!validation.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
            if (validation.getCode().equals(EventEnums.EventValidation.NOT_GROUP_NONE.getCode()) ||
                    validation.getCode().equals(EventEnums.EventValidation.NOT_EMPLOYEE.getCode()) ||
                    validation.getCode().equals(EventEnums.EventValidation.NOT_DEVICE_APP_ONLY_EVENT.getCode()) ||
                    validation.getCode().equals(EventEnums.EventValidation.ONLY_EMPLOYEE.getCode())) {
                return ApiResult.result(validation);
            }
            if (validation.getCode().equals(EventEnums.EventValidation.NOT_GROUP.getCode()) ||
                    validation.getCode().equals(EventEnums.EventValidation.NOT_DEVICE.getCode())) {
                return ApiResult.result(getEventReturnData(validation, validationRequestDto), validation);
            }
            return ApiResult.result(EventEnums.EventValidation.NO_EVENT);
        }

        return ApiResult.result(EventEnums.EventValidation.SERVICEABLE);
    }

    /**
     * 이벤트 상품그룹 조회
     */
    protected List<EventGroupByUserVo> getGoodsGroupList(Long evEventId) throws Exception {
        //이벤트 매핑 상품 정보
        List<EventGroupByUserVo> groupList = promotionEventMapper.getGroupByUser(evEventId);
        for (EventGroupByUserVo groupVo : groupList) {
            List<Long> groupGoodsIdList = promotionEventMapper.getGroupDetailByUser(groupVo.getEvEventGroupId());
            //상품 ID로 상품검색
            GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
                    .goodsIdList(groupGoodsIdList)
                    .build();
            List<GoodsSearchResultDto> goodsList = goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto).stream()
                    .limit(groupVo.getDisplayCount())
                    .collect(Collectors.toList());

            groupVo.setGoods(goodsList);
        }

        return groupList;
    }

    /**
     * 이벤트 상품그룹 조회 - Controller 조회버전
     */
    protected List<EventGroupByUserVo> getGoodsGroupList(Long evEventId, String deviceType, String userType) throws Exception {
        //이벤트 매핑 상품 정보
        List<EventGroupByUserVo> groupList = promotionEventMapper.getGroupByUser(evEventId);
        for (EventGroupByUserVo groupVo : groupList) {
            List<Long> groupGoodsIdList = promotionEventMapper.getGroupDetailByUser(groupVo.getEvEventGroupId());
            //상품 ID로 상품검색
            GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
                    .deviceInfo(deviceType)
                    .isMember(userType != null && (userType.equals(DisplayEnums.UserType.NORMAL.getCode()) || userType.equals(DisplayEnums.UserType.EMPLOYEE.getCode())))
                    .isEmployee(userType != null && (userType.equals(DisplayEnums.UserType.EMPLOYEE.getCode())))
                    .goodsIdList(groupGoodsIdList)
                    .build();

            List<GoodsSearchResultDto> goodsList = goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto).stream()
                    .limit(groupVo.getDisplayCount())
                    .collect(Collectors.toList());

            groupVo.setGoods(goodsList);
        }

        return groupList;
    }

    /**
     * SPMO-1301 이벤트 리다이렉션 이벤트 아이디 조회
     *
     * @return Long
     */
    protected Long getReDirectEventId(Long evEventId) throws Exception {
        Long reEventId = promotionEventMapper.getReDirectEventId(evEventId);
        return reEventId;
    }
}