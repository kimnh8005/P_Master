package kr.co.pulmuone.v1.comm.mapper.promotion.event;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.enums.CodeCommEnum;
import kr.co.pulmuone.v1.promotion.event.dto.*;
import kr.co.pulmuone.v1.promotion.event.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PromotionEventMapper {

    List<EventListByUserVo> getEventListByUser(EventListByUserRequestDto dto) throws Exception;

    Page<EventListFromMyPageVo> getEventListFromMyPage(EventListFromMyPageRequestDto dto) throws Exception;

    NormalByUserVo getNormalByUser(@Param("evEventId") Long evEventId, @Param("urUserId") Long urUserId, @Param("deviceType") String deviceType) throws Exception;

    List<CommentCodeByUserVo> getEventCommentCodeByUser(@Param("evEventId") Long evEventId) throws Exception;

    NormalValidationVo getNormalJoinValidation(@Param("evEventId") Long evEventId, @Param("urUserId") Long urUserId) throws Exception;

    List<EventCouponVo> getEventCoupon(@Param("evEventId") Long evEventId, @Param("evEventDetailId") Long evEventDetailId) throws Exception;

    List<EventCouponVo> getEventSelectCoupon(@Param("evEventId") Long evEventId, @Param("evEventDetailId") Long evEventDetailId, @Param("selectCouponId") String selectCouponId) throws Exception;

    void addEventJoin(EventJoinVo vo) throws Exception;

    void putEventJoin(EventJoinVo vo) throws Exception;

    void addEventJoinCoupon(EventJoinCouponVo vo) throws Exception;

    Page<EventJoinByUserVo> getNormalJoinByUser(EventJoinByUserRequestDto dto) throws Exception;

    StampByUserVo getStampByUser(@Param("evEventId") Long evEventId, @Param("urUserId") Long urUserId, @Param("deviceType") String deviceType) throws Exception;

    List<StampDetailByUserVo> getStampDetailByUser(Long evEventStampId) throws Exception;

    List<Integer> getStampJoinByUser(@Param("evEventId") Long evEventId, @Param("urUserId") Long urUserId) throws Exception;

    StampValidationVo getStampValidation(@Param("evEventId") Long evEventId, @Param("urUserId") Long urUserId) throws Exception;

    List<StampDetailValidation> getStampDetailValidation(@Param("evEventStampId") Long evEventStampId) throws Exception;

    List<StampJoinByUserVo> getStampJoinValidationByUser(@Param("evEventId") Long evEventId, @Param("urUserId") Long urUserId) throws Exception;

    void putStampJoin(StampJoinRequestDto dto) throws Exception;

    RouletteByUserVo getRouletteByUser(@Param("evEventId") Long evEventId, @Param("urUserId") Long urUserId, @Param("deviceType") String deviceType) throws Exception;

    List<RouletteItemByUserVo> getRouletteDetailByUser(Long evEventRouletteId) throws Exception;

    RouletteValidationVo getRouletteValidation(@Param("evEventId") Long evEventId, @Param("urUserId") Long urUserId) throws Exception;

    List<RouletteItemValidation> getRouletteDetailValidation(@Param("evEventRouletteId") Long evEventRouletteId) throws Exception;

    SurveyByUserVo getSurveyByUser(@Param("evEventId") Long evEventId, @Param("urUserId") Long urUserId, @Param("deviceType") String deviceType) throws Exception;

    List<SurveyQuestionByUserVo> getSurveyQuestionByUser(Long evEventId) throws Exception;

    List<SurveyItemByUserVo> getSurveyItemByUser(Long evEventSurveyQuestionId) throws Exception;

    SurveyValidationVo getSurveyValidation(@Param("evEventId") Long evEventId, @Param("urUserId") Long urUserId) throws Exception;

    void addSurveyJoinDetail(@Param("evEventJoinId") Long evEventJoinId, @Param("list") List<SurveyJoinDetailRequestDto> list) throws Exception;

    ExperienceByUserVo getExperienceByUser(@Param("evEventId") Long evEventId, @Param("urUserId") Long urUserId, @Param("deviceType") String deviceType) throws Exception;

    Page<EventJoinByUserVo> getExperienceJoinByUser(EventJoinByUserRequestDto dto) throws Exception;

    ExperienceValidationVo getExperienceValidation(@Param("evEventId") Long evEventId, @Param("urUserId") Long urUserId) throws Exception;

    void putEventExperienceRecruitClose(Long evEventId) throws Exception;

    List<EventUserGroupByUserVo> getUserGroup(@Param("evEventId") Long evEventId) throws Exception;

    List<MissionStampByUserVo> getMissionStampByUser(@Param("urUserId") Long urUserId, @Param("deviceType") String deviceType, @Param("userStatus") CodeCommEnum userStatus) throws Exception;

    EventInfoFromMetaVo getEventInfoFromMeta(@Param("evEventId") Long evEventId, @Param("deviceType") String deviceType) throws Exception;

    EventBenefitInfoVo getEventBenefitInfo(EventBenefitInfoRequestDto dto) throws Exception;

    int putEventBenefitInfoConcurrency(EventBenefitInfoRequestDto dto) throws Exception;

    int getEventBenefitCount(Long evEventId) throws Exception;

    String getEventCouponConcurrency(Long evEventCouponId) throws Exception;

    int putEventCouponConcurrency(Long evEventCouponId) throws Exception;

    String getNormalBenefitConcurrency(Long evEventId) throws Exception;

    int putNormalBenefitConcurrency(Long evEventId) throws Exception;

    List<EventGroupByUserVo> getGroupByUser(Long evEventId) throws Exception;

    List<Long> getGroupDetailByUser(Long evEventGroupId) throws Exception;

    Long getReDirectEventId(Long evEventId) throws Exception;
}
