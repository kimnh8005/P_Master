package kr.co.pulmuone.v1.company.notice.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.mapper.company.notice.CompanyNoticeMapper;
import kr.co.pulmuone.v1.company.notice.dto.*;
import kr.co.pulmuone.v1.company.notice.dto.vo.AddNoticeResultVo;
import kr.co.pulmuone.v1.company.notice.dto.vo.GetNoticePopupListResultVo;
import kr.co.pulmuone.v1.company.notice.dto.vo.GetNoticeResultVo;
import kr.co.pulmuone.v1.company.notice.dto.vo.PutNoticeResultVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class CompanyNoticeServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private CompanyNoticeService companyNoticeService;

    @InjectMocks
    private CompanyNoticeService mockCompanyNoticeService;

    @Mock
    CompanyNoticeMapper mockCompanyNoticeMapper;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void 공지사항_등록_필수값_오류_false() throws Exception {

        AddNoticeRequestDto dto = AddNoticeRequestDto.builder()
                .companyBbsType("BOS_BBS_TYPE.COMPANY")
                .notificationYn("N")
                .popupYn("Y")
                .title("테스트 케이스 등록 제목")
                .content(" ")
                .popupCoordinateX("50")
                .popupCoordinateY(" ")
                .popupDisplayStartDate("2020-06-17")
                .popupDisplayEndDate("2020-06-23")
                .build();

        assertFalse(companyNoticeService.addNotice(dto).getCode().equals(ApiResult.fail().getCode()));
    }

    @Test
    void 공지사항_수정_필수값_오류_false() throws Exception {

        PutNoticeRequestDto dto = PutNoticeRequestDto.builder()
                .companyBbsType("BOS_BBS_TYPE.COMPANY")
                .notificationYn("N")
                .popupYn("Y")
                .title("테스트 케이스 수 제목")
                .content(" ")
                .popupCoordinateX("50")
                .popupCoordinateY("49")
                .popupDisplayStartDate(" ")
                .popupDisplayEndDate("2020-06-23")
                .build();

        assertFalse(companyNoticeService.putNotice(dto).getCode().equals(ApiResult.fail().getCode()));
    }

    @Test
    void 전체_등록_조회_수정_삭제_한싸이클_true() throws Exception {

        String REQ_BBS_ID_01 = "";
        String REQ_BBS_ID_02 = "";

        //************************************************************
        // void 공지사항_등록_첨부파일_없을경우_true()
        //************************************************************

        AddNoticeRequestDto AddDto = AddNoticeRequestDto.builder()
                .companyBbsType("BOS_BBS_TYPE.COMPANY")
                .notificationYn("N")
                .popupDisplayTodayYn("Y")
                .useYn("Y")
                .popupYn("Y")
                .title("테스트 케이스 등록 제목")
                .content("테스트 케이스 등록 상세")
                .popupCoordinateX("50")
                .popupCoordinateY("49")
                .popupDisplayStartDate("2020-06-17")
                .popupDisplayEndDate("2020-06-23")
                .addFileList(new ArrayList<>())
                .build();

        ApiResult<?> result = companyNoticeService.addNotice(AddDto);

        String csCompanyBbsId = Optional.ofNullable(result)
                .map(m -> (AddNoticeResponseDto)m.getData())
                .map(AddNoticeResponseDto::getRows)
                .map(AddNoticeResultVo::getCsCompanyBbsId)
                .orElse("");

        REQ_BBS_ID_01 = csCompanyBbsId;

        assertTrue(REQ_BBS_ID_01.length() > 0);

        //************************************************************
        // void 공지사항_등록_첨부파일_있을경우_true()
        //************************************************************

        List<FileVo> addFileVoList = new ArrayList<>();
        for(int i=0; i<2; i++) {
            FileVo fileVo = new FileVo();
            fileVo.setOriginalFileName("첨부파일오리지널" + i);
            fileVo.setPhysicalFileName("첨부파일피지컬" + i);
            fileVo.setServerSubPath("TEST");
            addFileVoList.add(fileVo);
        }

        AddDto.setAddFileList(addFileVoList);

        result = companyNoticeService.addNotice(AddDto);

        csCompanyBbsId = Optional.ofNullable(result)
                .map(m -> (AddNoticeResponseDto)m.getData())
                .map(AddNoticeResponseDto::getRows)
                .map(AddNoticeResultVo::getCsCompanyBbsId)
                .orElse("");

        REQ_BBS_ID_02 = csCompanyBbsId;

        assertTrue(REQ_BBS_ID_02.length() > 0);

        System.out.println("REQ_BBS_ID_01 : " + REQ_BBS_ID_01);
        System.out.println("REQ_BBS_ID_02 : " + REQ_BBS_ID_02);

        //************************************************************
        // void 공지사항_수정_첨부파일_없을경우_true
        //************************************************************

        PutNoticeRequestDto putDto = PutNoticeRequestDto.builder()
                .companyBbsType("BOS_BBS_TYPE.COMPANY")
                .notificationYn("N")
                .popupDisplayTodayYn("Y")
                .useYn("Y")
                .popupYn("Y")
                .csCompanyBbsId(REQ_BBS_ID_01)
                .title("테스트 케이스 수정 제목")
                .content("테스트 케이스 수정 상세")
                .popupCoordinateX("50")
                .popupCoordinateY("49")
                .popupDisplayStartDate("2020-06-17")
                .popupDisplayEndDate("2020-06-23")
                .addFileList(new ArrayList<>())
                .build();

        result = companyNoticeService.putNotice(putDto);

        csCompanyBbsId = Optional.ofNullable(result)
                .map(m -> (PutNoticeResponseDto)m.getData())
                .map(PutNoticeResponseDto::getRows)
                .map(PutNoticeResultVo::getCsCompanyBbsId)
                .orElse("");

        String UP_BBS_ID_01 = csCompanyBbsId;

        assertEquals(UP_BBS_ID_01, REQ_BBS_ID_01);

        //************************************************************
        // void 공지사항_수정_첨부파일_있을경우_true()
        //************************************************************

        List<FileVo> putFileVoList = new ArrayList<>();
        for(int i=0; i<2; i++) {
            FileVo fileVo = new FileVo();
            fileVo.setOriginalFileName("첨부파일오리지널" + i);
            fileVo.setPhysicalFileName("첨부파일피지컬" + i);
            fileVo.setServerSubPath("TEST");
            putFileVoList.add(fileVo);
        }

        putDto.setCsCompanyBbsId(REQ_BBS_ID_02);
        putDto.setAddFileList(putFileVoList);

        result = companyNoticeService.putNotice(putDto);

        csCompanyBbsId = Optional.ofNullable(result)
                .map(m -> (PutNoticeResponseDto)m.getData())
                .map(PutNoticeResponseDto::getRows)
                .map(PutNoticeResultVo::getCsCompanyBbsId)
                .orElse("");

        String UP_BBS_ID_02 = csCompanyBbsId;

        assertEquals(UP_BBS_ID_02, REQ_BBS_ID_02);

        //************************************************************
        // void 공지사항_상세_조회_true()
        //************************************************************

        GetNoticeRequestDto dto = GetNoticeRequestDto.builder()
                .csCompanyBbsId(REQ_BBS_ID_01)
                .viewMode("Y")
                .build();

        csCompanyBbsId = Optional.ofNullable(companyNoticeService.getNotice(dto))
                .map(GetNoticeResponseDto::getRows)
                .map(GetNoticeResultVo::getCsCompanyBbsId)
                .orElse("");

        assertEquals(REQ_BBS_ID_01, csCompanyBbsId);

        //************************************************************
        // void 공지사항_목록_조회_true()
        //************************************************************

        GetNoticeListRequestDto listDto = GetNoticeListRequestDto.builder()
                .useYn("Y")
                .companyBbsType("BOS_BBS_TYPE.COMPANY")
                .build();

        int cnt = companyNoticeService.getNoticeList(listDto).getTotal();

        assertTrue(cnt > 0);

        //************************************************************
        // void 공지사항_공지여부_업데이트_true()
        //************************************************************

        List<PutNoticeSetRequestSaveDto> list = new ArrayList<>();
        PutNoticeSetRequestSaveDto tmpDto1 = PutNoticeSetRequestSaveDto.builder()
                .csCompanyBbsId(REQ_BBS_ID_01)
                .build();
        list.add(tmpDto1);
        PutNoticeSetRequestSaveDto tmpDto2 = PutNoticeSetRequestSaveDto.builder()
                .csCompanyBbsId(REQ_BBS_ID_02)
                .build();
        list.add(tmpDto2);

        PutNoticeSetRequestDto putSetDto = PutNoticeSetRequestDto.builder()
                .updateRequestDtoList(list)
                .notificationYn("Y")
                .build();

        cnt = companyNoticeService.putNoticeSet(putSetDto);

        assertTrue(cnt > 0);

        //************************************************************
        // void 다음_공지사항_조회_true()
        //************************************************************

        GetNoticePreNextListRequestDto reqNextDto = GetNoticePreNextListRequestDto.builder()
                .csCompanyBbsId(REQ_BBS_ID_01)
                .build();

        GetNoticePreNextListResponseDto getNextdto = companyNoticeService.getNoticePreNextList(reqNextDto);

        cnt = getNextdto.getRows().size();

        assertTrue(cnt > 0);

        //************************************************************
        // void 공지사항_첨부파일_삭제_true()
        //************************************************************

        NoticeAttachParamDto attDto = NoticeAttachParamDto.builder()
                .csCompanyBbsId(REQ_BBS_ID_02)
                .build();

        cnt = companyNoticeService.delAttc(attDto);

        assertTrue(cnt > 0);

        //************************************************************
        // void 공지사항_삭제_true()
        //************************************************************

        List<DelNoticeRequestSaveDto> delList = new ArrayList<>();
        DelNoticeRequestSaveDto dtlTmpDto1 = DelNoticeRequestSaveDto.builder()
                .csCompanyBbsId(REQ_BBS_ID_01)
                .build();
        delList.add(dtlTmpDto1);
        DelNoticeRequestSaveDto dtlTmpDto2 = DelNoticeRequestSaveDto.builder()
                .csCompanyBbsId(REQ_BBS_ID_02)
                .build();
        delList.add(dtlTmpDto2);

        DelNoticeRequestDto delListDto = DelNoticeRequestDto.builder()
                .deleteRequestDtoList(delList)
                .build();

        cnt = companyNoticeService.delNotice(delListDto);

        assertTrue(cnt > 0);
    }

    @Test
    void addNotice() throws Exception {
        //given
        AddNoticeRequestDto addNoticeRequestDto = new AddNoticeRequestDto();
        addNoticeRequestDto.setCompanyBbsType("test");
        addNoticeRequestDto.setTitle("testTitle");
        addNoticeRequestDto.setContent("testContent");
        addNoticeRequestDto.setPopupYn("N");
        addNoticeRequestDto.setNotificationYn("N");
        addNoticeRequestDto.setPopupDisplayTodayYn("N");
        addNoticeRequestDto.setUseYn("N");
        addNoticeRequestDto.setAddFileList(new ArrayList<>());

        //when
        ApiResult result = companyNoticeService.addNotice(addNoticeRequestDto);

        //then
        assertEquals(ApiResult.success().getMessageEnum(), result.getMessageEnum());
    }

    @Test
    void putNotice() throws Exception {
        // [TDD 의거 임시작성] 향후 개발 완료후 개선 요망.
        PutNoticeRequestDto putNoticeRequestDto = new PutNoticeRequestDto();
        putNoticeRequestDto.setTitle("testTitle");
        putNoticeRequestDto.setContent("testContent");
        putNoticeRequestDto.setPopupYn("N");
        putNoticeRequestDto.setAddFileList(new ArrayList<>());

        ApiResult result = companyNoticeService.putNotice(putNoticeRequestDto);
        assertEquals(ApiResult.success().getMessageEnum(), result.getMessageEnum());
    }

    @Test
    void getNoticePopupList() throws Exception {
        List<GetNoticePopupListResultVo> getNoticePopupListResultVo = new ArrayList<>();
        GetNoticePopupListResultVo getNoticePopupListResultVo1 = new GetNoticePopupListResultVo();
        getNoticePopupListResultVo1.setCsCompanyBbsId("22");
        getNoticePopupListResultVo1.setPopupCoordinateX("22");
        getNoticePopupListResultVo1.setPopupCoordinateY("11");
        getNoticePopupListResultVo.add(getNoticePopupListResultVo1);
        given(mockCompanyNoticeMapper.getNoticeLoginData(any())).willReturn(null);
        given(mockCompanyNoticeMapper.getNoticePopupList(any())).willReturn(getNoticePopupListResultVo);
        ApiResult result = mockCompanyNoticeService.getNoticePopupList();
        assertEquals(ApiResult.success().getMessageEnum(), result.getMessageEnum());
    }

    @Test
    void getNoticePopup() {
        ApiResult result = companyNoticeService.getNoticePopup(54);
        assertEquals(ApiResult.success().getMessageEnum(), result.getMessageEnum());
    }
}