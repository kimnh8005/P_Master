package kr.co.pulmuone.v1.system.log.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.base.dto.ExcelDownLogRequestDto;
import kr.co.pulmuone.v1.base.dto.MenuOperLogRequestDto;
import kr.co.pulmuone.v1.base.dto.PrivacyMenuOperLogRequestDto;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import kr.co.pulmuone.v1.system.log.dto.*;
import kr.co.pulmuone.v1.system.log.dto.vo.DeviceLogVo;
import kr.co.pulmuone.v1.system.log.dto.vo.ExcelDownloadLogResultVo;
import kr.co.pulmuone.v1.system.log.dto.vo.MenuOperLogResultVo;
import kr.co.pulmuone.v1.system.log.dto.vo.PrivacyMenuOperLogResultVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class SystemLogServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    private SystemLogService service;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void 접속_로그_리스트() {
        GetConnectLogListRequestDto connectLogListRequestDto = new GetConnectLogListRequestDto();
        connectLogListRequestDto.setStartCreateDate("2021-02-01");
        connectLogListRequestDto.setEndCreateDate("2021-02-01");
        connectLogListRequestDto.setCondiType("ACCESS_LOG_DIV.1");
        connectLogListRequestDto.setCondiValue("pmo1234");
        GetConnectLogListResponseDto result =  service.getConnectLogList(connectLogListRequestDto);
        boolean total = result.getTotal() > 0;
        assertTrue(total);
    }

    @Test
    void 배치_로그_리스트() {
        GetBatchLogListRequestDto batchLogListRequestDto = new GetBatchLogListRequestDto();
        batchLogListRequestDto.setSearchType("batchName");
        batchLogListRequestDto.setSearchText("SystemBatchStatusFailJob");
        batchLogListRequestDto.setStartCreateDate("2021-08-10");
        batchLogListRequestDto.setEndCreateDate("2021-08-19");
        GetBatchLogListResponseDto result =  service.getBatchLogList(batchLogListRequestDto);
        boolean total = result.getTotal() > 0;
        assertTrue(total);
    }

    @Test
    void 메뉴_이용_로그_리스트() {
        MenuOperLogRequestDto menuOperLogRequestDto = new MenuOperLogRequestDto();
        menuOperLogRequestDto.setSearchValue("출고처");
        menuOperLogRequestDto.setSearchType("menu");
        menuOperLogRequestDto.setLoginName("포비즈");
        menuOperLogRequestDto.setStartCreateDate("2020-11-11");
        menuOperLogRequestDto.setEndCreateDate("2020-11-11");

    	Page<MenuOperLogResultVo> result =  service.getMenuOperLogList(menuOperLogRequestDto);
        boolean total = result.getTotal() > 0;
        assertTrue(total);
    }

    @Test
    void 메뉴사용이력_리스트_조회() {

        PrivacyMenuOperLogRequestDto privacyMenuOperLogRequestDto = new PrivacyMenuOperLogRequestDto();
        privacyMenuOperLogRequestDto.setSearchValue("forbiz");
        privacyMenuOperLogRequestDto.setSearchType("loginId");
        privacyMenuOperLogRequestDto.setStartCreateDate("2020-11-11");
        privacyMenuOperLogRequestDto.setEndCreateDate("2020-11-11");
        privacyMenuOperLogRequestDto.setCrudType("CRUD_TP.R");

    	Page<PrivacyMenuOperLogResultVo> result =  service.getPrivacyMenuOperLogList(privacyMenuOperLogRequestDto);
        boolean total = result.getTotal() > 0;
        assertTrue(total);
    }

    @Test
    void 엑셀다운로드_로그_리스트()  {
        ExcelDownLogRequestDto excelDownLogRequestDto = new ExcelDownLogRequestDto();
        excelDownLogRequestDto.setSearchType("id");
        excelDownLogRequestDto.setSearchValue("forbiz");
        excelDownLogRequestDto.setStartCreateDate("2020-11-04");
        excelDownLogRequestDto.setEndCreateDate("2020-11-04");

    	Page<ExcelDownloadLogResultVo> result =  service.getExcelDownloadLogList(excelDownLogRequestDto);
        boolean total = result.getTotal() > 0;
        assertTrue(total);
    }

    @Test
    void addDeviceLogUserJoin_저장_성공() {
        //given
        String urPcidCd = "test";
        Long urUserId = 1L;

        //when
        Boolean result = service.addDeviceLogUserJoin(urPcidCd, urUserId);

        //then
        assertTrue(result);
    }

    @Test
    void addDeviceLogLoginFail_저장_성공() {
        //given
        String urPcidCd = "test";
        Long urUserId = 1L;

        //when
        Boolean result = service.addDeviceLogLoginFail(urPcidCd, urUserId);

        //then
        assertTrue(result);
    }

    @Test
    void addIllegalLogStolenLostCard_저장_성공() {
        //given
        String urPcidCd = "test";
        Long urUserId = 1L;

        //when
        Boolean result = service.addIllegalLogStolenLostCard(urPcidCd, urUserId);

        //then
        assertTrue(result);
    }

    @Test
    void addIllegalLogTransactionNotCard_저장_성공() {
        //given
        String urPcidCd = "test";
        Long urUserId = 1L;

        //when
        Boolean result = service.addIllegalLogTransactionNotCard(urPcidCd, urUserId);

        //then
        assertTrue(result);
    }

    @Test
    void addIllegalLogTransactionNotCard_저장_성공_비회원() {
        //given
        String urPcidCd = "test";

        //when
        Boolean result = service.addIllegalLogTransactionNotCard(urPcidCd, null);

        //then
        assertTrue(result);
    }

    @Test
    void getDeviceLogDetect_조회_성공() {
        //given
        DetectDeviceLogRequestDto dto = DetectDeviceLogRequestDto.builder()
                .deviceLogType(SystemEnums.DeviceLogType.USER_JOIN)
                .startDateTime("2021-06-01 00:00:00")
                .endDateTime("2021-06-30 23:59:59")
                .detectCount(2)
                .build();

        //when
        List<DeviceLogVo> result = service.getDeviceLogDetect(dto);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void addExcelDownloadAsync() {
        //given
        ExcelDownloadAsyncRequestDto requestDto = new ExcelDownloadAsyncRequestDto();
        requestDto.setUrUserId(0L);
        requestDto.setFileName("test");

        //when, then
        service.addExcelDownloadAsync(requestDto);
    }

    @Test
    void putExcelDownloadAsyncSetUse() {
        //given
        Long stExcelDownloadAsyncId = 894L;

        //when, then
        service.putExcelDownloadAsyncSetUse(stExcelDownloadAsyncId);
    }

    @Test
    void getExcelDownloadAsyncUseYn() {
        //given
        Long stExcelDownloadAsyncId = 894L;

        //when
        String result = service.getExcelDownloadAsyncUseYn(stExcelDownloadAsyncId);

        //then
        assertEquals("N", result);
    }

    @Test
    void putExcelDownloadAsyncSetError() {
        //given
        Long stExcelDownloadAsyncId = 894L;

        //when, then
        service.putExcelDownloadAsyncSetError(stExcelDownloadAsyncId);
    }

}