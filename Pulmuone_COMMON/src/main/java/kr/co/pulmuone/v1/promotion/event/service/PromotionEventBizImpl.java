package kr.co.pulmuone.v1.promotion.event.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.CustomerConstants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackTargetListByUserResultVo;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.order.front.dto.vo.OrderInfoFromFeedbackVo;
import kr.co.pulmuone.v1.order.front.dto.vo.OrderInfoFromStampPurchaseVo;
import kr.co.pulmuone.v1.order.front.service.OrderFrontBiz;
import kr.co.pulmuone.v1.promotion.event.dto.*;
import kr.co.pulmuone.v1.promotion.event.dto.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionEventBizImpl implements PromotionEventBiz {

    @Autowired
    private PromotionEventService promotionEventService;

    @Autowired
    private PromotionEventConcurrencyBiz promotionEventConcurrencyBiz;

    @Autowired
    private GoodsSearchBiz goodsSearchBiz;

    @Autowired
    private OrderFrontBiz orderFrontBiz;

    @Override
    public EventListByUserResponseDto getEventListByUser(EventListByUserRequestDto dto) throws Exception {
        return promotionEventService.getEventListByUser(dto);
    }

    @Override
    public ApiResult<?> getNormalByUser(EventRequestDto dto) throws Exception {
        return promotionEventService.getNormalByUser(dto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ApiResult<?> addNormalJoin(NormalJoinRequestDto dto) throws Exception {
        // get Event Info
        NormalValidationVo eventInfo = promotionEventService.getNormalJoinValidation(dto.getEvEventId(), dto.getUrUserId(), dto.getSelectCouponId());
        if (eventInfo == null) return ApiResult.result(EventEnums.EventValidation.NO_EVENT);

        // Event Validation Check
        EventValidationRequestDto validationRequestDto = new EventValidationRequestDto(eventInfo, dto);
        validationRequestDto.setEvEventId(dto.getEvEventId());
        MessageCommEnum validation = promotionEventService.eventValidation(validationRequestDto, EventEnums.EventValidationType.ADD.getCode());
        if (!BaseEnums.Default.SUCCESS.getCode().equals(validation.getCode())) {
            return ApiResult.result(validation);
        }
        // Normal Validation Check
        validation = promotionEventService.addNormalJoinValidation(eventInfo, dto);
        if (!BaseEnums.Default.SUCCESS.getCode().equals(validation.getCode())) {
            return ApiResult.result(validation);
        }

        // 참여정보 저장 - 1차
        EventJoinVo eventJoinVo = EventJoinVo.builder()
                .evEventId(dto.getEvEventId())
                .urUserId(dto.getUrUserId())
                .commentValue(dto.getCommentValue())
                .comment(dto.getComment())
                .build();
        promotionEventService.addEventJoin(eventJoinVo);

        // 혜택발생여부 - 즉시당첨 케이스
        BenefitResponseDto benefitResponseDto = promotionEventService.addNormalBenefit(eventInfo, dto);

        // 참여정보 저장 - 2차
        eventJoinVo.setBenefit(benefitResponseDto);
        promotionEventService.putEventJoin(eventJoinVo);

        // Return 값 설정
        NormalJoinResponseDto result = new NormalJoinResponseDto(benefitResponseDto);
        // 댓글 정보 조회
        EventJoinByUserRequestDto joinDto = new EventJoinByUserRequestDto();
        joinDto.setPage(1);
        joinDto.setLimit(20);
        joinDto.setEvEventId(dto.getEvEventId());
        EventJoinByUserResponseDto joinResponseDto = promotionEventService.getNormalJoinByUser(joinDto);
        result.setTotal(joinResponseDto.getTotal());
        result.setComment(joinResponseDto.getComment());

        return ApiResult.success(result);
    }

    @Override
    public EventJoinByUserResponseDto getNormalJoinByUser(EventJoinByUserRequestDto dto) throws Exception {
        return promotionEventService.getNormalJoinByUser(dto);
    }

    @Override
    public ApiResult<?> getStampByUser(EventRequestDto dto) throws Exception {
        StampByUserVo eventInfo = promotionEventService.getStampByUser(dto.getEvEventId(), dto.getUrUserId(), dto.getDeviceType());
        if (eventInfo == null) return ApiResult.result(EventEnums.EventValidation.NO_EVENT);

        //미리보기 여부에 따라 validation 처리
        if (!"Y".equals(dto.getPreviewYn())) {
            EventValidationRequestDto validationRequestDto = new EventValidationRequestDto(dto, eventInfo);
            ApiResult validResult = promotionEventService.validPromotionEvent(dto, validationRequestDto);
            if (!validResult.getMessageEnum().equals(EventEnums.EventValidation.SERVICEABLE)) return validResult;
        }

        StampByUserResponseDto result = new StampByUserResponseDto(eventInfo);

        // 스탬프 상세 정보 조회
        result.setStamp(promotionEventService.getStampDetailByUser(eventInfo.getEvEventStampId()));

        // dDay 계산
        result.setDday(DateUtil.getBetweenDays(eventInfo.getEndDate()));

        result.setNotDeviceAppOnlyEventYn(promotionEventService.getNotDeviceAppOnlyEventYn(eventInfo.getAppOnlyEventYn(), dto.getDeviceType())); // 앱전용 이벤트 디바이스 아님 : Y

        // 유저 스탬프 모두 참여여부 계산
        if (eventInfo.getStampCount2().equals(eventInfo.getJoinCount())) {
            result.setJoinAllYn("Y");
        } else {
            result.setJoinAllYn("N");
        }

        // 스탬프(구매) 이벤트의 경우 스탬프 참여정보 반영
        if (EventEnums.EventType.PURCHASE.getCode().equals(eventInfo.getEventType()) && eventInfo.getUserJoinCount() > 0) {
            // 참여 정보 조회
            List<StampJoinByUserVo> join = promotionEventService.getStampJoinValidationByUser(dto.getEvEventId(), dto.getUrUserId());
            if (join.size() > 0) {
                // 주문정보
                OrderInfoFromStampPurchaseVo orderInfo = orderFrontBiz.getOrderInfoFromStampPurchase(dto.getUrUserId(), eventInfo.getStartDateTime(), eventInfo.getEndDateTime(), eventInfo.getOrderPrice());

                // 스탬프 갯수 계산
                int stampCount = 0;
                if (orderInfo != null) {
                    stampCount = orderInfo.getOrderCount();
                    if (stampCount > Integer.parseInt(eventInfo.getStampCount2())) {
                        stampCount = Integer.parseInt(eventInfo.getStampCount2());
                    }
                }

                //반영
                StampJoinRequestDto joinRequestDto = new StampJoinRequestDto();
                joinRequestDto.setEvEventJoinId(join.get(0).getEvEventStampJoinId());
                joinRequestDto.setStampCount(stampCount);
                promotionEventService.putStampJoin(joinRequestDto);
            }
        }

        // 스탬프 참여 정보 조회
        List<Integer> joinList = promotionEventService.getStampJoinByUser(dto.getEvEventId(), dto.getUrUserId());
        if (EventEnums.EventType.PURCHASE.getCode().equals(eventInfo.getEventType()) && !joinList.isEmpty()) {
            int joinCount = joinList.get(0);
            joinList = new ArrayList<>();
            for (int i = joinCount; i > 0; i--) {
                joinList.add(i);
            }
        }

        result.setStampCount(joinList);

        //상품 정보
        if ("Y".equals(dto.getPreviewYn())) {
            List<EventGroupByUserVo> groupList = promotionEventService.getGoodsGroupList(dto.getEvEventId());
            result.setGroup(groupList);
        }

        return ApiResult.success(result);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ApiResult<?> addStampJoin(StampJoinRequestDto dto) throws Exception {
        // get Event Info
        StampValidationVo eventInfo = promotionEventService.getStampValidation(dto.getEvEventId(), dto.getUrUserId());
        if (eventInfo == null) return ApiResult.result(EventEnums.EventValidation.NO_EVENT);

        // Event Validation Check
        EventValidationRequestDto validationRequestDto = new EventValidationRequestDto(eventInfo, dto);
        validationRequestDto.setEvEventId(dto.getEvEventId());
        MessageCommEnum validation = promotionEventService.eventValidation(validationRequestDto, EventEnums.EventValidationType.ADD.getCode());
        if (!validation.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
            return ApiResult.result(validation);
        }

        // 스탬프 혜택 발행
        BenefitResponseDto benefitResponseDto = null;
        EventJoinVo eventJoinVo = EventJoinVo.builder()
                .evEventId(dto.getEvEventId())
                .urUserId(dto.getUrUserId())
                .build();
        int stampCount = promotionEventService.calcStampCount(eventInfo, dto);
        eventJoinVo.setStampCount(stampCount);
        dto.setStampCount(stampCount);
        if (eventInfo.getEventType().equals(EventEnums.EventType.ATTEND.getCode())) {
            // 참여정보 저장 - 1차
            promotionEventService.addEventJoin(eventJoinVo);
            // 혜택 발행
            benefitResponseDto = promotionEventService.addStampAttendBenefit(eventInfo, dto);
        }
        if (eventInfo.getEventType().equals(EventEnums.EventType.MISSION.getCode())) {
            // 참여정보 저장 - 1차
            promotionEventService.addEventJoin(eventJoinVo);
            //혜택 발행
            benefitResponseDto = promotionEventService.addStampMissionBenefit(eventInfo, dto);
        }
        if (eventInfo.getEventType().equals(EventEnums.EventType.PURCHASE.getCode())) {
            // 스탬프 정보 반영
            List<StampJoinByUserVo> join = eventInfo.getJoin();
            if (join.size() == 0) {
                promotionEventService.addEventJoin(eventJoinVo);
            } else {
                dto.setStampCount(eventJoinVo.getStampCount());
                dto.setEvEventJoinId(join.get(0).getEvEventStampJoinId());
                promotionEventService.putStampJoin(dto);
            }

            // 결과 값
            benefitResponseDto = new BenefitResponseDto();
            benefitResponseDto.setStampCount(eventJoinVo.getStampCount());
            benefitResponseDto.setWinnerYn("N");
            return ApiResult.success(new StampJoinResponseDto(benefitResponseDto));
        }

        // 저장
        if (benefitResponseDto == null) {
            return ApiResult.result(EventEnums.EventValidation.NO_EVENT);
        }

        // 참여정보 저장 - 2차
        eventJoinVo.setBenefit(benefitResponseDto);
        promotionEventService.putEventJoin(eventJoinVo);

        // 결과 값
        // 유저 스탬프 모두 참여여부 계산
        benefitResponseDto.setStampCount(dto.getStampCount());
        if (eventInfo.getStampCount2() == benefitResponseDto.getStampCount()) {
            benefitResponseDto.setJoinAllYn("Y");
        } else {
            benefitResponseDto.setJoinAllYn("N");
        }
        return ApiResult.success(new StampJoinResponseDto(benefitResponseDto));
    }

    @Override
    public ApiResult<?> getRouletteByUser(EventRequestDto dto) throws Exception {
        RouletteByUserVo eventInfo = promotionEventService.getRouletteByUser(dto.getEvEventId(), dto.getUrUserId(), dto.getDeviceType());
        if (eventInfo == null) return ApiResult.result(EventEnums.EventValidation.NO_EVENT);

        //미리보기 여부에 따라 validation 처리
        if (!"Y".equals(dto.getPreviewYn())) {
            EventValidationRequestDto validationRequestDto = new EventValidationRequestDto(dto, eventInfo);
            ApiResult validResult = promotionEventService.validPromotionEvent(dto, validationRequestDto);
            if (!validResult.getMessageEnum().equals(EventEnums.EventValidation.SERVICEABLE)) return validResult;
        }

        RouletteByUserResponseDto result = new RouletteByUserResponseDto(eventInfo);

        // 룰렛 상세 정보 조회
        result.setItem(promotionEventService.getRouletteDetailByUser(dto.getEvEventId(), eventInfo.getEvEventRouletteId()));

        // dDay 계산
        result.setDday(DateUtil.getBetweenDays(eventInfo.getEndDate()));

        //상품 정보
        if ("Y".equals(dto.getPreviewYn())) {
            List<EventGroupByUserVo> groupList = promotionEventService.getGoodsGroupList(dto.getEvEventId());
            result.setGroup(groupList);
        }

        return ApiResult.success(result);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ApiResult<?> addRouletteJoin(RouletteJoinRequestDto dto) throws Exception {
        // get Event Info
        RouletteValidationVo eventInfo = promotionEventService.getRouletteValidation(dto.getEvEventId(), dto.getUrUserId());
        if (eventInfo == null) return ApiResult.result(EventEnums.EventValidation.NO_EVENT);

        // Event Validation Check
        EventValidationRequestDto validationRequestDto = new EventValidationRequestDto(eventInfo, dto);
        validationRequestDto.setEvEventId(dto.getEvEventId());
        MessageCommEnum validation = promotionEventService.eventValidation(validationRequestDto, EventEnums.EventValidationType.ADD.getCode());
        if (!BaseEnums.Default.SUCCESS.getCode().equals(validation.getCode())) {
            return ApiResult.result(validation);
        }

        // Validation Event Roulette Check
        MessageCommEnum validationRoulette = promotionEventService.eventRouletteValidation(dto.getEvEventId());
        if (!BaseEnums.Default.SUCCESS.getCode().equals(validationRoulette.getCode())) {
            return ApiResult.result(validationRoulette);
        }

        // 참여정보 저장 - 1차
        EventJoinVo eventJoinVo = EventJoinVo.builder()
                .evEventId(dto.getEvEventId())
                .urUserId(dto.getUrUserId())
                .stampCount(eventInfo.getJoinCount() + 1)
                .build();
        promotionEventService.addEventJoin(eventJoinVo);

        // 혜택 발행
        BenefitResponseDto benefitResponseDto = promotionEventService.processRouletteBenefit(eventInfo, dto);
        if (benefitResponseDto == null || benefitResponseDto.getEvEventRouletteItemId() == null) {
            return ApiResult.result(EventEnums.EventValidation.FULL_BENEFIT);
        }

        // 참여정보 저장 - 2차
        eventJoinVo.setBenefit(benefitResponseDto);
        eventJoinVo.setEvEventRouletteItemId(benefitResponseDto.getEvEventRouletteItemId());
        promotionEventService.putEventJoin(eventJoinVo);

        // 결과 값
        return ApiResult.success(new RouletteJoinResponseDto(benefitResponseDto));
    }

    @Override
    public ApiResult<?> getSurveyByUser(EventRequestDto dto) throws Exception {
        SurveyByUserVo eventInfo = promotionEventService.getSurveyByUser(dto.getEvEventId(), dto.getUrUserId(), dto.getDeviceType());
        if (eventInfo == null) return ApiResult.result(EventEnums.EventValidation.NO_EVENT);

        //미리보기 여부에 따라 validation 처리
        if (!"Y".equals(dto.getPreviewYn())) {
            EventValidationRequestDto validationRequestDto = new EventValidationRequestDto(dto, eventInfo);
            ApiResult validResult = promotionEventService.validPromotionEvent(dto, validationRequestDto);
            if (!validResult.getMessageEnum().equals(EventEnums.EventValidation.SERVICEABLE)) return validResult;
        }

        SurveyByUserResponseDto result = new SurveyByUserResponseDto(eventInfo);

        // 설문 문항 정보 조회
        List<SurveyQuestionByUserVo> questionList = promotionEventService.getSurveyQuestionByUser(dto.getEvEventId());
        for (SurveyQuestionByUserVo question : questionList) {
            question.setItem(promotionEventService.getSurveyItemByUser(question.getEvEventSurveyQuestionId()));
        }
        result.setQuestion(questionList);

        // dDay 계산
        result.setDday(DateUtil.getBetweenDays(eventInfo.getEndDate()));
        result.setNotDeviceAppOnlyEventYn(promotionEventService.getNotDeviceAppOnlyEventYn(eventInfo.getAppOnlyEventYn(), dto.getDeviceType())); // 앱전용 이벤트 디바이스 아님 : Y

        //상품 정보
        if ("Y".equals(dto.getPreviewYn())) {
            List<EventGroupByUserVo> groupList = promotionEventService.getGoodsGroupList(dto.getEvEventId());
            result.setGroup(groupList);
        }

        return ApiResult.success(result);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ApiResult<?> addSurveyJoin(SurveyJoinRequestDto dto) throws Exception {
        // get Event Info
        SurveyValidationVo eventInfo = promotionEventService.getSurveyJoinValidation(dto.getEvEventId(), dto.getUrUserId());
        if (eventInfo == null) return ApiResult.result(EventEnums.EventValidation.NO_EVENT);

        // Event Validation Check
        EventValidationRequestDto validationRequestDto = new EventValidationRequestDto(eventInfo, dto);
        MessageCommEnum validation = promotionEventService.eventValidation(validationRequestDto, EventEnums.EventValidationType.ADD.getCode());
        if (!validation.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
            return ApiResult.result(validation);
        }

        // 참여정보 저장 - 1차
        EventJoinVo eventJoinVo = EventJoinVo.builder()
                .evEventId(dto.getEvEventId())
                .urUserId(dto.getUrUserId())
                .build();
        promotionEventService.addEventJoin(eventJoinVo);
        promotionEventService.addSurveyJoinDetail(eventJoinVo.getEvEventJoinId(), dto.getList());

        // 혜택발생여부 - 즉시당첨 케이스
        BenefitResponseDto benefitResponseDto = promotionEventService.addSurveyBenefit(eventInfo, dto);

        // 참여정보 저장 - 2차
        eventJoinVo.setBenefit(benefitResponseDto);
        promotionEventService.putEventJoin(eventJoinVo);

        return ApiResult.success(new SurveyJoinResponseDto(benefitResponseDto));
    }

    @Override
    public ApiResult<?> getExperienceByUser(EventRequestDto dto) throws Exception {
        ExperienceByUserVo eventInfo = promotionEventService.getExperienceByUser(dto.getEvEventId(), dto.getUrUserId(), dto.getDeviceType());
        if (eventInfo == null) return ApiResult.result(EventEnums.EventValidation.NO_EVENT);

        //미리보기 여부에 따라 validation 처리
        if (!"Y".equals(dto.getPreviewYn())) {
            EventValidationRequestDto validationRequestDto = new EventValidationRequestDto(dto, eventInfo);
            ApiResult validResult = promotionEventService.validPromotionEvent(dto, validationRequestDto);
            if (!validResult.getMessageEnum().equals(EventEnums.EventValidation.SERVICEABLE)) return validResult;
        }

        ExperienceByUserResponseDto result = new ExperienceByUserResponseDto(eventInfo);

        // Comment Code 조회
        result.setCommentCode(promotionEventService.getEventCommentCodeByUser(dto.getEvEventId()));

        // 체험단 상품 정보 조회
        if (eventInfo.getIlGoodsId() > 0) {
            result.setGoods(goodsSearchBiz.getGoodsSearchExperience(eventInfo.getIlGoodsId()));
        }

        // dDay 계산
        result.setDday(DateUtil.getBetweenDays(eventInfo.getEndDate()));

        //상품 정보
        if ("Y".equals(dto.getPreviewYn())) {
            List<EventGroupByUserVo> groupList = promotionEventService.getGoodsGroupList(dto.getEvEventId());
            result.setGroup(groupList);
        }

        return ApiResult.success(result);
    }

    @Override
    public EventJoinByUserResponseDto getExperienceJoinByUser(EventJoinByUserRequestDto dto) throws Exception {
        return promotionEventService.getExperienceJoinByUser(dto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ApiResult<?> addExperienceJoin(ExperienceJoinRequestDto dto) throws Exception {
        // get Event Info
        ExperienceValidationVo eventInfo = promotionEventService.getExperienceJoinValidation(dto.getEvEventId(), dto.getUrUserId());
        if (eventInfo == null) return ApiResult.result(EventEnums.EventValidation.NO_EVENT);

        // Event Validation Check
        EventValidationRequestDto validationRequestDto = new EventValidationRequestDto(eventInfo, dto);
        MessageCommEnum validation = promotionEventService.eventValidation(validationRequestDto, EventEnums.EventValidationType.ADD.getCode());
        if (!validation.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
            return ApiResult.result(validation);
        }
        // Experience Validation Check
        validation = promotionEventService.addExperienceJoinValidation(eventInfo, dto);
        if (!validation.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
            return ApiResult.result(validation);
        }

        // 선착순 케이스
        if (eventInfo.getEventDrawType().equals(EventEnums.EventDrawType.FIRST_COME.getCode())) {
            // 동시성 처리
            MessageCommEnum eventBenefitInfoVo = promotionEventConcurrencyBiz.concurrencyEventBenefit(EventBenefitInfoRequestDto.builder()
                    .evEventId(dto.getEvEventId())
                    .build());

            if (BaseEnums.Default.FAIL.equals(eventBenefitInfoVo)) {
                return ApiResult.result(EventEnums.EventValidation.FIRST_COME_CLOSE);
            }
            // 선착순 인원 마감처리
            if (eventInfo.getUserCount() + 1 >= eventInfo.getFirstComeCount()) {
                promotionEventService.putEventExperienceRecruitClose(dto.getEvEventId());
            }
            // 참여정보 저장 - 1차
            EventJoinVo eventJoinVo = EventJoinVo.builder()
                    .evEventId(dto.getEvEventId())
                    .urUserId(dto.getUrUserId())
                    .commentValue(dto.getCommentValue())
                    .comment(dto.getComment())
                    .build();
            promotionEventService.addEventJoin(eventJoinVo);

            // 혜택발생여부 - 선착순 케이스
            BenefitResponseDto benefitResponseDto = promotionEventService.addExperienceBenefit(eventInfo, dto);

            // 참여정보 저장 - 2차
            eventJoinVo.setBenefit(benefitResponseDto);
            promotionEventService.putEventJoin(eventJoinVo);
        } else {
            // 참여정보 저장
            EventJoinVo eventJoinVo = EventJoinVo.builder()
                    .evEventId(dto.getEvEventId())
                    .urUserId(dto.getUrUserId())
                    .commentValue(dto.getCommentValue())
                    .comment(dto.getComment())
                    .build();
            promotionEventService.addEventJoin(eventJoinVo);
        }

        // 댓글 조회 결과 값
        EventJoinByUserRequestDto joinDto = new EventJoinByUserRequestDto();
        joinDto.setPage(1);
        joinDto.setLimit(20);
        joinDto.setEvEventId(dto.getEvEventId());
        return ApiResult.success(promotionEventService.getExperienceJoinByUser(joinDto));
    }

    @Override
    public EventListFromMyPageResponseDto getEventListFromMyPage(EventListFromMyPageRequestDto dto) throws Exception {
        EventListFromMyPageResponseDto result = promotionEventService.getEventListFromMyPage(dto);
        for (EventListFromMyPageVo vo : result.getList()) {
            if (vo.getEventType().equals(EventEnums.EventType.EXPERIENCE.getCode())) {
                String orderStatusCode = orderFrontBiz.getOrderStatusFromEvent(vo.getEvEventId(), dto.getUrUserId());
                if (orderStatusCode == null) continue;
                if (orderStatusCode.equals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode())) {
                    vo.setExperienceOrderCode(EventEnums.ExperienceOrderCode.INCOM_COMPLETE.getCode());
                } else if (orderStatusCode.equals(OrderEnums.OrderStatus.DELIVERY_READY.getCode()) || orderStatusCode.equals(OrderEnums.OrderStatus.DELIVERY_ING.getCode())) {
                    vo.setExperienceOrderCode(EventEnums.ExperienceOrderCode.DELIVERY_DOING.getCode());
                } else if (orderStatusCode.equals(OrderEnums.OrderStatus.DELIVERY_COMPLETE.getCode()) || orderStatusCode.equals(OrderEnums.OrderStatus.BUY_FINALIZED.getCode())) {
                    vo.setExperienceOrderCode(EventEnums.ExperienceOrderCode.DELIVERY_COMPLETE.getCode());
                }

                OrderInfoFromFeedbackVo fromFeedbackVo = orderFrontBiz.getOrderInfoFromExperienceFeedback(vo.getEvEventId(), dto.getUrUserId(), CustomerConstants.FEEDBACK_DAY);
                if (fromFeedbackVo != null) {
                    FeedbackTargetListByUserResultVo feedbackInfo = new FeedbackTargetListByUserResultVo(fromFeedbackVo);
                    feedbackInfo.setExperienceYn("Y");
                    vo.setFeedback(feedbackInfo);
                }
            }
        }

        return result;
    }

    @Override
    public List<MissionStampByUserResponseDto> getMissionStampByUser(Long urUserId, String deviceType, CodeCommEnum userStatus, long userGroup) throws Exception {
        return promotionEventService.getMissionStampByUser(urUserId, deviceType, userStatus, userGroup);
    }

    @Override
    public EventInfoFromMetaVo getEventInfoFromMeta(Long evEventId, String deviceType) throws Exception {
        return promotionEventService.getEventInfoFromMeta(evEventId, deviceType);
    }

    @Override
    public List<EventGroupByUserVo> getGroupList(Long evEventId, String deviceType, String userType) throws Exception {
        return promotionEventService.getGoodsGroupList(evEventId, deviceType, userType);
    }

    @Override
    public Long getReDirectEventId(Long evEventId) throws Exception {
        return promotionEventService.getReDirectEventId(evEventId);
    }
}