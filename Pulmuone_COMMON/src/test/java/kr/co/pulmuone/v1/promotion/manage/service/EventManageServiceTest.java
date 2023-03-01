package kr.co.pulmuone.v1.promotion.manage.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.EventEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.promotion.manage.dto.EventManageRequestDto;
import kr.co.pulmuone.v1.promotion.manage.dto.EventManageResponseDto;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventManageServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private EventManageService eventManageService;

    @BeforeEach
    void setUp() {
        preLogin();
    }

    /**
     * 이벤트 리스트조회
     *
     * @throws BaseException
     */
    @Test
    public void test_이벤트리스트조회() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        EventManageRequestDto eventManageRequestDto = new EventManageRequestDto();

        List<String> eventTpList = new ArrayList<>();
        eventTpList.add(EventEnums.EventTp.NORMAL.getCode());
        eventTpList.add(EventEnums.EventTp.SURVEY.getCode());
        eventTpList.add(EventEnums.EventTp.ATTEND.getCode());
        eventTpList.add(EventEnums.EventTp.MISSION.getCode());
        eventTpList.add(EventEnums.EventTp.PURCHASE.getCode());
        eventTpList.add(EventEnums.EventTp.ROULETTE.getCode());
        eventTpList.add(EventEnums.EventTp.EXPERIENCE.getCode());

        eventManageRequestDto.setEventTpList(eventTpList);
        // 페이지조건
        eventManageRequestDto.setPageSize(20);
        eventManageRequestDto.setPage(1);

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        Page<EventVo> result = eventManageService.selectEventList(eventManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 이벤트 상세조회
     *
     * @throws BaseException
     */
    @Test
    public void test_이벤트상세조회() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        EventManageRequestDto eventManageRequestDto = new EventManageRequestDto();

        eventManageRequestDto.setEvEventId("1");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        EventManageResponseDto result = eventManageService.selectEventInfo(eventManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    @Test
    public void test_이벤트참여리스트조회() throws BaseException {

        //given
        EventManageRequestDto eventManageRequestDto = new EventManageRequestDto();
        eventManageRequestDto.setEvEventId("5");

        eventManageRequestDto.setWinnerYn("");
        // 페이지조건
        eventManageRequestDto.setPageSize(20);
        eventManageRequestDto.setPage(1);

        //when
        Page<EventJoinVo> result = eventManageService.selectEventJoinList(eventManageRequestDto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    public void test_이벤트참여직접입력리스트조회() throws BaseException {
        //given
        EventManageRequestDto eventManageRequestDto = new EventManageRequestDto();
        eventManageRequestDto.setEvEventSurveyQuestionId("1");
        eventManageRequestDto.setPageSize(20);
        eventManageRequestDto.setPage(1);

        //when
        Page<EventJoinVo> result = eventManageService.selectEventJoinDirectJoinList(eventManageRequestDto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    public void test_이벤트참여설문항목리스트조회() throws BaseException {

        // given
        EventManageRequestDto eventManageRequestDto = new EventManageRequestDto();

        eventManageRequestDto.setEvEventId("5");
        // 페이지조건
        //eventManageRequestDto.setPageSize(20);
        //eventManageRequestDto.setPage(1);

        // when
        List<EventSurveyVo> result = eventManageService.selectEventJoinSurveyList(eventManageRequestDto);

        // then
        assertTrue(result.size() > 0);
    }

    /**
     * 이벤트참여 설문항목참여 리스트조회
     *
     * @throws BaseException
     */
    @Test
    public void test_이벤트참여설문항목참여리스트조회() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        EventManageRequestDto eventManageRequestDto = new EventManageRequestDto();

        eventManageRequestDto.setEvEventId("5");
        // 페이지조건
        //eventManageRequestDto.setPageSize(20);
        //eventManageRequestDto.setPage(1);

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        List<EventSurveyVo> result = eventManageService.selectEventJoinSurveyItemJoinList(eventManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.size() > 0);
    }

    /**
     * 이벤트 삭제
     *
     * @throws BaseException
     */
    @Test
    public void test_이벤트삭제() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        List<String> eventList = new ArrayList<>();
        eventList.add("1");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        EventManageResponseDto result = eventManageService.delEvent(eventList);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 이벤트 등록
     *
     * @throws BaseException
     */
    @Test
    public void test_이벤트등록() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        EventManageRequestDto eventManageRequestDto = new EventManageRequestDto();

        // 이벤트기본정보
        EventVo eventInfo = new EventVo();
        eventInfo.setEventTp("EVENT_TP.MISSION");
        eventInfo.setMallDiv("MALL_DIV.PULMUONE");
        eventInfo.setUseYn("Y");
        eventInfo.setDelYn("N");
        eventInfo.setTitle("미션테스트1");
        eventInfo.setDescription("미션설명");
        eventInfo.setDispWebPcYn("Y");
        eventInfo.setDispWebMobileYn("N");
        eventInfo.setDispAppYn("Y");
        eventInfo.setEvEmployeeTp("EV_EMPLOYEE_TP.EMPLOYEE_EXCEPT");
        eventInfo.setStartDt("20210223010200");
        eventInfo.setEndDt("29991223030459");
        eventInfo.setTimeOverCloseYn("Y");
        eventInfo.setBnrImgPathPc("BOS/dp/2021/02/22/86138CDCF3AE48438CE8.jpg");
        eventInfo.setBnrImgOriginNmPc("AcadianDay_ROW6460737821_1920x1200.jpg");
        eventInfo.setBnrImgPathMo("BOS/dp/2021/02/22/05F139D7E0DA4594BB70.jpg");
        eventInfo.setBnrImgOriginNmMo("Almabtrieb_ROW4328676685_1920x1200.jpg");
        eventInfo.setDetlHtmlPc("상세1");
        eventInfo.setDetlHtmlMo("상세2");
        eventInfo.setEmployeeJoinYn("N");
        eventInfo.setEventJoinTp("EVENT_JOIN_TP.NO_LIMIT");
        eventInfo.setEventDrawTp("EVENT_DRAW_TP.AUTO");
        eventInfo.setWinnerInfor("당첨자안내");
        eventInfo.setDispYn("Y");
        eventManageRequestDto.setEventInfo(eventInfo);

        // 그룹정보
        List<EvUserGroupVo> userGroupList = new ArrayList<>();
        EvUserGroupVo evUserGroupVo = new EvUserGroupVo();
        evUserGroupVo.setUrGroupId(22L);
        userGroupList.add(evUserGroupVo);
        evUserGroupVo = new EvUserGroupVo();
        evUserGroupVo.setUrGroupId(8L);
        userGroupList.add(evUserGroupVo);
        evUserGroupVo = new EvUserGroupVo();
        evUserGroupVo.setUrGroupId(13L);
        userGroupList.add(evUserGroupVo);
        eventManageRequestDto.setUserGroupList(userGroupList);

        // 이벤트상세정보
        EventStampVo eventStampInfo = new EventStampVo();
        eventStampInfo.setBtnColorCd("#111111");
        eventStampInfo.setDefaultPath("BOS/dp/2021/02/22/6FF9BD7896E04650ACE6.jpg");
        eventStampInfo.setDefaultOriginNm("BavariaFossil_ROW7020921185_1920x1200.jpg");
        eventStampInfo.setCheckPath("BOS/dp/2021/02/22/3DFF7EB3ECFF4868B973.jpg");
        eventStampInfo.setCheckOriginNm("CastleriggStone_ROW9324790345_1920x1200.jpg");
        eventStampInfo.setBgPath("BOS/dp/2021/02/22/494A2CD881B04F7E8040.jpg");
        eventStampInfo.setBgOriginNm("HeartAustralia_ROW0887383281_1920x1200.jpg");
        eventStampInfo.setStampCnt1(3);
        eventStampInfo.setStampCnt2(4);
        eventManageRequestDto.setEventStampInfo(eventStampInfo);
        // 이벤트 스탬프상세리스트
        List<EventStampVo> eventStampDetlList = new ArrayList<>();
        EventStampVo eventStampVo = new EventStampVo();
        eventStampVo.setDefaultPath("BOS/dp/2021/02/22/987542E06E2C4AB9BE91.png");
        eventStampVo.setDefaultOriginNm("bp.png");
        eventStampVo.setCheckPath("BOS/dp/2021/02/22/6CC9C79396B64046B707.png");
        eventStampVo.setCheckOriginNm("brand.png");
        eventStampVo.setIconPath("BOS/dp/2021/02/22/9D3DA3121569449AA9F2.png");
        eventStampVo.setIconOriginNm("brand2.png");
        eventStampVo.setEventBenefitTp("EVENT_BENEFIT_TP.ENTER");
        eventStampVo.setBenefitNm("응모");
        eventStampVo.setStampUrl("www.naver.com");
        eventStampDetlList.add(eventStampVo);
        eventStampVo = new EventStampVo();
        eventStampVo.setDefaultPath("BOS/dp/2021/02/22/FB6D77F7538A4A1CA120.png");
        eventStampVo.setDefaultOriginNm("brand3.png");
        eventStampVo.setCheckPath("BOS/dp/2021/02/22/7D3390E2991141F7BFA2.png");
        eventStampVo.setCheckOriginNm("brand4.png");
        eventStampVo.setIconPath("BOS/dp/2021/02/22/AEF9C39C35804BE09F4F.png");
        eventStampVo.setIconOriginNm("brand5.png");
        eventStampVo.setEventBenefitTp("EVENT_BENEFIT_TP.COUPON");
        eventStampVo.setStampUrl("www.daum.net");
        List<EventVo> eventCouponList = new ArrayList<>();
        EventVo couponVo = new EventVo();
        couponVo.setPmCouponId("346");
        couponVo.setCouponCnt(5);
        eventCouponList.add(couponVo);
        couponVo = new EventVo();
        couponVo.setPmCouponId("345");
        couponVo.setCouponCnt(4);
        eventCouponList.add(couponVo);
        couponVo = new EventVo();
        couponVo.setPmCouponId("344");
        couponVo.setCouponCnt(3);
        eventCouponList.add(couponVo);
        eventStampVo.setEventCouponList(eventCouponList);
        eventStampDetlList.add(eventStampVo);
        eventStampVo = new EventStampVo();
        eventStampVo.setDefaultPath("BOS/dp/2021/02/22/56CDA0753CA944B4ABC1.png");
        eventStampVo.setDefaultOriginNm("brand6.png");
        eventStampVo.setCheckPath("BOS/dp/2021/02/22/4892DBA80BB64B50A198.png");
        eventStampVo.setCheckOriginNm("brand7.png");
        eventStampVo.setIconPath("BOS/dp/2021/02/22/C866C22AC7FD45EFB757.png");
        eventStampVo.setIconOriginNm("brand8.png");
        eventStampVo.setEventBenefitTp("EVENT_BENEFIT_TP.POINT");
        eventStampVo.setBenefitId("42");
        eventStampVo.setStampUrl("www.kakao.co.kr");
        eventStampDetlList.add(eventStampVo);
        eventStampVo = new EventStampVo();
        eventStampVo.setDefaultPath("BOS/dp/2021/02/22/7CF46296DB5C4D72997C.png");
        eventStampVo.setDefaultOriginNm("card.png");
        eventStampVo.setCheckPath("BOS/dp/2021/02/22/77E6517A8B6E4A49B1E6.png");
        eventStampVo.setCheckOriginNm("icon-join-sns-naver.png");
        eventStampVo.setIconPath("BOS/dp/2021/02/22/4F196EF2FC5C472E9D58.png");
        eventStampVo.setIconOriginNm("imglist.png");
        eventStampVo.setEventBenefitTp("EVENT_BENEFIT_TP.GIFT");
        eventStampVo.setBenefitNm("경품");
        eventStampVo.setStampUrl("www.nate.com");
        eventStampDetlList.add(eventStampVo);
        eventManageRequestDto.setEventStampDetlList(eventStampDetlList);

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        EventManageResponseDto result = eventManageService.addEvent(eventManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getEvEventId().length() > 0);
    }

    /**
     * 이벤트 수정
     *
     * @throws BaseException
     */
    @Test
    public void test_이벤트수정() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        EventManageRequestDto eventManageRequestDto = new EventManageRequestDto();

        // 이벤트기본정보
        EventVo eventInfo = new EventVo();
        eventInfo.setEvEventId("37");
        eventInfo.setStatusSe("BEF");
        eventInfo.setEventTp("EVENT_TP.MISSION");
        eventInfo.setMallDiv("MALL_DIV.PULMUONE");
        eventInfo.setUseYn("Y");
        eventInfo.setDelYn("N");
        eventInfo.setTitle("미션테스트1");
        eventInfo.setDescription("미션설명");
        eventInfo.setDispWebPcYn("Y");
        eventInfo.setDispWebMobileYn("N");
        eventInfo.setDispAppYn("Y");
        eventInfo.setEvEmployeeTp("EV_EMPLOYEE_TP.EMPLOYEE_EXCEPT");
        eventInfo.setStartDt("20210223010200");
        eventInfo.setEndDt("29991223030459");
        eventInfo.setTimeOverCloseYn("Y");
        eventInfo.setBnrImgPathPc("BOS/dp/2021/02/22/86138CDCF3AE48438CE8.jpg");
        eventInfo.setBnrImgOriginNmPc("AcadianDay_ROW6460737821_1920x1200.jpg");
        eventInfo.setBnrImgPathMo("BOS/dp/2021/02/22/05F139D7E0DA4594BB70.jpg");
        eventInfo.setBnrImgOriginNmMo("Almabtrieb_ROW4328676685_1920x1200.jpg");
        eventInfo.setDetlHtmlPc("상세1");
        eventInfo.setDetlHtmlMo("상세2");
        eventInfo.setEmployeeJoinYn("N");
        eventInfo.setEventJoinTp("EVENT_JOIN_TP.NO_LIMIT");
        eventInfo.setEventDrawTp("EVENT_DRAW_TP.AUTO");
        eventInfo.setWinnerInfor("당첨자안내");
        eventInfo.setDispYn("Y");
        eventManageRequestDto.setEventInfo(eventInfo);

        // 그룹정보
        List<EvUserGroupVo> userGroupList = new ArrayList<>();
        EvUserGroupVo evUserGroupVo = new EvUserGroupVo();
        evUserGroupVo.setUrGroupId(22L);
        userGroupList.add(evUserGroupVo);
        evUserGroupVo = new EvUserGroupVo();
        evUserGroupVo.setUrGroupId(8L);
        userGroupList.add(evUserGroupVo);
        evUserGroupVo = new EvUserGroupVo();
        evUserGroupVo.setUrGroupId(13L);
        userGroupList.add(evUserGroupVo);
        eventManageRequestDto.setUserGroupList(userGroupList);

        // 이벤트상세정보
        EventStampVo eventStampInfo = new EventStampVo();
        eventStampInfo.setBtnColorCd("#111111");
        eventStampInfo.setDefaultPath("BOS/dp/2021/02/22/6FF9BD7896E04650ACE6.jpg");
        eventStampInfo.setDefaultOriginNm("BavariaFossil_ROW7020921185_1920x1200.jpg");
        eventStampInfo.setCheckPath("BOS/dp/2021/02/22/3DFF7EB3ECFF4868B973.jpg");
        eventStampInfo.setCheckOriginNm("CastleriggStone_ROW9324790345_1920x1200.jpg");
        eventStampInfo.setBgPath("BOS/dp/2021/02/22/494A2CD881B04F7E8040.jpg");
        eventStampInfo.setBgOriginNm("HeartAustralia_ROW0887383281_1920x1200.jpg");
        eventStampInfo.setStampCnt1(3);
        eventStampInfo.setStampCnt2(4);
        eventManageRequestDto.setEventStampInfo(eventStampInfo);
        // 이벤트 스탬프상세리스트
        List<EventStampVo> eventStampDetlList = new ArrayList<>();
        EventStampVo eventStampVo = new EventStampVo();
        eventStampVo.setDefaultPath("BOS/dp/2021/02/22/987542E06E2C4AB9BE91.png");
        eventStampVo.setDefaultOriginNm("bp.png");
        eventStampVo.setCheckPath("BOS/dp/2021/02/22/6CC9C79396B64046B707.png");
        eventStampVo.setCheckOriginNm("brand.png");
        eventStampVo.setIconPath("BOS/dp/2021/02/22/9D3DA3121569449AA9F2.png");
        eventStampVo.setIconOriginNm("brand2.png");
        eventStampVo.setEventBenefitTp("EVENT_BENEFIT_TP.ENTER");
        eventStampVo.setBenefitNm("응모");
        eventStampVo.setStampUrl("www.naver.com");
        eventStampDetlList.add(eventStampVo);
        eventStampVo = new EventStampVo();
        eventStampVo.setDefaultPath("BOS/dp/2021/02/22/FB6D77F7538A4A1CA120.png");
        eventStampVo.setDefaultOriginNm("brand3.png");
        eventStampVo.setCheckPath("BOS/dp/2021/02/22/7D3390E2991141F7BFA2.png");
        eventStampVo.setCheckOriginNm("brand4.png");
        eventStampVo.setIconPath("BOS/dp/2021/02/22/AEF9C39C35804BE09F4F.png");
        eventStampVo.setIconOriginNm("brand5.png");
        eventStampVo.setEventBenefitTp("EVENT_BENEFIT_TP.COUPON");
        eventStampVo.setStampUrl("www.daum.net");
        List<EventVo> eventCouponList = new ArrayList<>();
        EventVo couponVo = new EventVo();
        couponVo.setPmCouponId("346");
        couponVo.setCouponCnt(5);
        eventCouponList.add(couponVo);
        couponVo = new EventVo();
        couponVo.setPmCouponId("345");
        couponVo.setCouponCnt(4);
        eventCouponList.add(couponVo);
        couponVo = new EventVo();
        couponVo.setPmCouponId("344");
        couponVo.setCouponCnt(3);
        eventCouponList.add(couponVo);
        eventStampVo.setEventCouponList(eventCouponList);
        eventStampDetlList.add(eventStampVo);
        eventStampVo = new EventStampVo();
        eventStampVo.setDefaultPath("BOS/dp/2021/02/22/56CDA0753CA944B4ABC1.png");
        eventStampVo.setDefaultOriginNm("brand6.png");
        eventStampVo.setCheckPath("BOS/dp/2021/02/22/4892DBA80BB64B50A198.png");
        eventStampVo.setCheckOriginNm("brand7.png");
        eventStampVo.setIconPath("BOS/dp/2021/02/22/C866C22AC7FD45EFB757.png");
        eventStampVo.setIconOriginNm("brand8.png");
        eventStampVo.setEventBenefitTp("EVENT_BENEFIT_TP.POINT");
        eventStampVo.setBenefitId("42");
        eventStampVo.setStampUrl("www.kakao.co.kr");
        eventStampDetlList.add(eventStampVo);
        eventStampVo = new EventStampVo();
        eventStampVo.setDefaultPath("BOS/dp/2021/02/22/7CF46296DB5C4D72997C.png");
        eventStampVo.setDefaultOriginNm("card.png");
        eventStampVo.setCheckPath("BOS/dp/2021/02/22/77E6517A8B6E4A49B1E6.png");
        eventStampVo.setCheckOriginNm("icon-join-sns-naver.png");
        eventStampVo.setIconPath("BOS/dp/2021/02/22/4F196EF2FC5C472E9D58.png");
        eventStampVo.setIconOriginNm("imglist.png");
        eventStampVo.setEventBenefitTp("EVENT_BENEFIT_TP.GIFT");
        eventStampVo.setBenefitNm("경품");
        eventStampVo.setStampUrl("www.nate.com");
        eventStampDetlList.add(eventStampVo);
        eventManageRequestDto.setEventStampDetlList(eventStampDetlList);

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        EventManageResponseDto result = eventManageService.putEvent(eventManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 이벤트 당첨자공지사항 수정
     *
     * @throws BaseException
     */
    @Test
    public void test_이벤트당첨자공지사항수정() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        EventManageRequestDto eventManageRequestDto = new EventManageRequestDto();

        // 이벤트기본정보
        EventVo eventInfo = new EventVo();
        eventInfo.setEvEventId("1");
        eventInfo.setWinnerNotice("www.naver.com");
        eventManageRequestDto.setEventInfo(eventInfo);


        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        EventManageResponseDto result = eventManageService.putEventWinnerNotice(eventManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }


    /**
     * 이벤트참여 당첨자 설정
     *
     * @throws BaseException
     */
    @Test
    public void test_이벤트참여당첨자설정() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        EventManageRequestDto eventManageRequestDto = new EventManageRequestDto();

        eventManageRequestDto.setEvEventId("692");
        eventManageRequestDto.setWinnerSelectTp("RANDOM");
        eventManageRequestDto.setWinnerCnt(3);

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        EventManageResponseDto result = eventManageService.putWinnerLottery(eventManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 이벤트참여 댓글 차단/차단해제
     *
     * @throws BaseException
     */
    @Test
    public void test_이벤트참여댓글차단_차단해제() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        EventManageRequestDto eventManageRequestDto = new EventManageRequestDto();

        eventManageRequestDto.setEvEventId("1");
        eventManageRequestDto.setEvEventJoinId("1");
        eventManageRequestDto.setAdminSecretYn("Y");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        EventManageResponseDto result = eventManageService.putAdminSecretYn(eventManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }


}
