package kr.co.pulmuone.v1.display.popup.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.display.popup.dto.*;
import kr.co.pulmuone.v1.display.popup.dto.vo.GetDisplayPopupListResultVo;
import kr.co.pulmuone.v1.display.popup.dto.vo.GetPopupInfoMallResultVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DisplayPopupServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private DisplayPopupService displayPopupService;

    @Test
    public void getPopupList_성공() {
        //given
        GetDisplayPopupListRequestDto getDisplayPopupListRequestDto = new GetDisplayPopupListRequestDto();
        getDisplayPopupListRequestDto.setUseYn("Y");
        getDisplayPopupListRequestDto.setDisplayTargetType("DP_TARGET_TP.ALL");
        getDisplayPopupListRequestDto.setDisplayTargetTypeList(Collections.singletonList("DP_TARGET_TP.ALL"));
        getDisplayPopupListRequestDto.setExposureState("Y");
        getDisplayPopupListRequestDto.setPopupSubject("");
        getDisplayPopupListRequestDto.setDisplayRangeType("DP_RANGE_TP.PC");
        getDisplayPopupListRequestDto.setDisplayRangeTypeList(Collections.singletonList("DP_RANGE_TP.PC"));

        if (!Objects.equals(getDisplayPopupListRequestDto.getExposureState(), "")) {
            getDisplayPopupListRequestDto
                    .setExposureStateList(Arrays.asList(getDisplayPopupListRequestDto.getExposureState().split("∀")));
        }

        //when
        Page<GetDisplayPopupListResultVo> getDisplayPopupListResultList = displayPopupService.getPopupList(getDisplayPopupListRequestDto);

        //then
        assertTrue(getDisplayPopupListResultList.getTotal() >= 0);
    }

    @Test
    public void addPopup_성공() {
        //given
        AddDisplayPopupRequestDto addDisplayPopupRequestDto = new AddDisplayPopupRequestDto();
        List<FileVo> addFileVoList = new ArrayList<>();
        FileVo fileVo = new FileVo();
        fileVo.setFieldName("");
        fileVo.setOriginalFileName("");
        fileVo.setPhysicalFileName("");

        addDisplayPopupRequestDto.setCreateId("1");
        addDisplayPopupRequestDto.setLinkUrl("1");
        addDisplayPopupRequestDto.setSort(1);
        addDisplayPopupRequestDto.setPopupType("DP_POPUP_TP.IMAGE");
        addDisplayPopupRequestDto.setDisplayRangeType("DP_RANGE_TP.MOBILE");
        addDisplayPopupRequestDto.setDisplayTargetType("DP_TARGET_TP.ALL");
        addDisplayPopupRequestDto.setPopupSubject("테스트1");
        addDisplayPopupRequestDto.setTodayStopYn("Y");
        addDisplayPopupRequestDto.setUseYn("Y");
        addDisplayPopupRequestDto.setAddFile("Filename");
        addDisplayPopupRequestDto.setPopupImagePath("PopupImagePath");
        addDisplayPopupRequestDto.setDisplayStartDate("2020-11-26 11:42:27");
        addDisplayPopupRequestDto.setDisplayEndDate("2020-11-29 11:42:27");
        addDisplayPopupRequestDto.setAddFileList(addFileVoList);

        //when
        int result = displayPopupService.addPopup(addDisplayPopupRequestDto);

        //then
        assertTrue(result > 0);
    }

    @Test
    public void putPopupDetail_성공() {
        //given
        String displayFrontPopupId = "4819";

        //when
        PutDisplayPopupDetailResponseDto putDisplayDetailResponseDto = displayPopupService.putPopupDetail(displayFrontPopupId);

        //then
        assertNotNull(putDisplayDetailResponseDto);
    }

    @Test
    public void putPopup_성공() {
        //given
        PutDisplayPopupRequestDto putDisplayPopupRequestDto = new PutDisplayPopupRequestDto();

        List<FileVo> fileList = new ArrayList<>();
        FileVo vo1 = new FileVo();
        vo1.setFieldName("filePcMain");
        vo1.setOriginalFileName("file1.png");
        vo1.setPhysicalFileName("1D67BCB47DE04C05A183.png");
        vo1.setContentType("image/png");
        vo1.setFileSize(Long.parseLong("9073"));
        vo1.setServerSubPath("BOS/ur/test/2020/10/29/");
        vo1.setSaveResult("SUCCESS");


        putDisplayPopupRequestDto.setDisplayFrontPopupId(4819);
        putDisplayPopupRequestDto.setCreateId("1");
        putDisplayPopupRequestDto.setLinkUrl("1");
        putDisplayPopupRequestDto.setSort(1);
        putDisplayPopupRequestDto.setPopupType("DP_POPUP_TP.IMAGE");
        putDisplayPopupRequestDto.setDisplayRangeType("DP_RANGE_TP.MOBILE");
        putDisplayPopupRequestDto.setDisplayTargetType("DP_TARGET_TP.ALL");
        putDisplayPopupRequestDto.setPopupSubject("테스트1");
        putDisplayPopupRequestDto.setTodayStopYn("Y");
        putDisplayPopupRequestDto.setUseYn("Y");
        putDisplayPopupRequestDto.setDisplayStartDate("2020-11-26 11:42:27");
        putDisplayPopupRequestDto.setDisplayEndDate("2020-11-29 11:42:27");
        putDisplayPopupRequestDto.setAddFileList(fileList);

        //when, then
        assertTrue(displayPopupService.putPopup(putDisplayPopupRequestDto) > 0);
    }

    @Test
    public void delPopup_성공() {
        //given
        String displayFrontPopupId = "4819";

        //when, then
        assertTrue(displayPopupService.delPopup(displayFrontPopupId) > 0);
    }

    @Test
    public void previewPopup_성공() {
        //given
        String displayFrontPopupId = "4819";

        //when
        PreviewPopupResponseDto previewPopupResponseDto = displayPopupService.previewPopup(displayFrontPopupId);

        //then
        assertEquals("Y", previewPopupResponseDto.getTodayStopYn());
    }

    @Test
    void getPopupInfoMobile_성공() {
        //given
        List<String> dpTargetTypeList = new ArrayList<>();
        dpTargetTypeList.add("DP_TARGET_TP.ALL");

        //when
        List<GetPopupInfoMallResultVo> result = displayPopupService.getPopupInfoMobile(dpTargetTypeList, null, null);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getPopupInfoPc_성공() {
        //given
        List<String> dpTargetTypeList = new ArrayList<>();
        dpTargetTypeList.add("DP_TARGET_TP.ALL");

        //when
        List<GetPopupInfoMallResultVo> result = displayPopupService.getPopupInfoPc(dpTargetTypeList, null, null);

        //then
        assertTrue(result.size() > 0);
    }

}