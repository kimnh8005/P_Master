package kr.co.pulmuone.v1.promotion.event.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.CodeCommEnum;
import kr.co.pulmuone.v1.promotion.event.dto.*;
import kr.co.pulmuone.v1.promotion.event.dto.vo.EventGroupByUserVo;
import kr.co.pulmuone.v1.promotion.event.dto.vo.EventInfoFromMetaVo;

import java.util.List;

public interface PromotionEventBiz {

    EventListByUserResponseDto getEventListByUser(EventListByUserRequestDto dto) throws Exception;

    ApiResult<?> getNormalByUser(EventRequestDto dto) throws Exception;

    ApiResult<?> addNormalJoin(NormalJoinRequestDto dto) throws Exception;

    EventJoinByUserResponseDto getNormalJoinByUser(EventJoinByUserRequestDto dto) throws Exception;

    ApiResult<?> getStampByUser(EventRequestDto dto) throws Exception;

    ApiResult<?> addStampJoin(StampJoinRequestDto dto) throws Exception;

    ApiResult<?> getRouletteByUser(EventRequestDto dto) throws Exception;

    ApiResult<?> addRouletteJoin(RouletteJoinRequestDto dto) throws Exception;

    ApiResult<?> getSurveyByUser(EventRequestDto dto) throws Exception;

    ApiResult<?> addSurveyJoin(SurveyJoinRequestDto dto) throws Exception;

    ApiResult<?> getExperienceByUser(EventRequestDto dto) throws Exception;

    EventJoinByUserResponseDto getExperienceJoinByUser(EventJoinByUserRequestDto dto) throws Exception;

    ApiResult<?> addExperienceJoin(ExperienceJoinRequestDto dto) throws Exception;

    EventListFromMyPageResponseDto getEventListFromMyPage(EventListFromMyPageRequestDto dto) throws Exception;

    List<MissionStampByUserResponseDto> getMissionStampByUser(Long urUserId, String deviceType, CodeCommEnum userStatus, long userGroup) throws Exception;

    EventInfoFromMetaVo getEventInfoFromMeta(Long evEventId, String deviceType) throws Exception;

    List<EventGroupByUserVo> getGroupList(Long evEventId, String deviceType, String userType) throws Exception;

    Long getReDirectEventId(Long evEventId) throws Exception;
}