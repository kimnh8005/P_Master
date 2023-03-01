package kr.co.pulmuone.mall.promotion.event.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.promotion.event.dto.*;

import javax.servlet.http.HttpServletRequest;

public interface PromotionEventMallService {

    ApiResult<?> getEventListByUser(EventListByUserRequestDto dto) throws Exception;

    ApiResult<?> getNormalByUser(HttpServletRequest request, Long evEventId) throws Exception;

    ApiResult<?> addNormalJoin(NormalJoinRequestDto dto) throws Exception;

    ApiResult<?> getNormalJoinByUser(EventJoinByUserRequestDto dto) throws Exception;

    ApiResult<?> getStampByUser(HttpServletRequest request, Long evEventId) throws Exception;

    ApiResult<?> addStampJoin(StampJoinRequestDto dto) throws Exception;

    ApiResult<?> getRouletteByUser(HttpServletRequest request, Long evEventId) throws Exception;

    ApiResult<?> addRouletteJoin(RouletteJoinRequestDto dto) throws Exception;

    ApiResult<?> getSurveyByUser(HttpServletRequest request, Long evEventId) throws Exception;

    ApiResult<?> addSurveyJoin(SurveyJoinRequestDto dto) throws Exception;

    ApiResult<?> getExperienceByUser(HttpServletRequest request, Long evEventId) throws Exception;

    ApiResult<?> getExperienceJoinByUser(EventJoinByUserRequestDto dto) throws Exception;

    ApiResult<?> addExperienceJoin(ExperienceJoinRequestDto dto) throws Exception;

    ApiResult<?> getGroupList(Long evEventId, String deviceType, String userType) throws Exception;

}
