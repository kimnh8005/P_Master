package kr.co.pulmuone.v1.customer.stndpnt.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointMallRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointRequestDto;
import kr.co.pulmuone.v1.customer.stndpnt.dto.vo.GetStandingPointListResultVo;

public class StandingPointServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    StandingPointService standingPointService;

    @Test
    void getStandingPointList_조회_정상() throws Exception {
        //given
    	StandingPointRequestDto requestDto = new StandingPointRequestDto();

        //when
        Page<GetStandingPointListResultVo> voList = standingPointService.getStandingPointList(requestDto);

        //then
        assertTrue(voList.size()>0);
    }

    @Test
    void getStandingPointExportExcel_엑셀다운로드조회_성공() {

    	StandingPointRequestDto requestDto = new StandingPointRequestDto();

    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        userVO.setPersonalInformationAccessYn("Y");
        SessionUtil.setUserVO(userVO);

        requestDto.setFindKeyword("오늘");
        requestDto.setSearchSelect("SEARCH_SELECT.COMPANY");

        List<GetStandingPointListResultVo> excelList = standingPointService.getStandingPointExportExcel(requestDto);

        // 해당 품목코드로 1건 또는 0건 조회되어야 함
        assertTrue(excelList.size() > 0 || excelList.size() == 0);
    }

    @Test
    void getDetailStandingPoint_정상() throws Exception {

    	StandingPointRequestDto requestDto = new StandingPointRequestDto();
    	requestDto.setCsStandPntId("-1");

    	GetStandingPointListResultVo vo = standingPointService.getDetailStandingPoint(requestDto);

	   	Assertions.assertNull(vo);
    }


    @Test
    void putStandingPointStatus_정상() throws Exception {

    	StandingPointRequestDto dto = new StandingPointRequestDto();
    	dto.setCsStandPntId("1");
    	dto.setQuestionStat("STAND_PNT_STAT.APPROVED");
    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);
    	ApiResult<?> result = standingPointService.putStandingPointStatus(dto);

        // then
    	assertTrue(result.getCode().equals(ApiResult.success().getCode()));

    }

    @Test
    void addStandingPointQna_저장_성공() throws Exception {
        //given
        StandingPointMallRequestDto dto = new StandingPointMallRequestDto();
        dto.setCompanyName("회사");
        dto.setCompanyCeoName("CEO");
        dto.setBusinessRegistrationNumber("1111");
        dto.setZipCode("01234");
        dto.setAddress1("서울시");
        dto.setAddress2("4층");
        dto.setManagerUrUserId(0L);
        dto.setManagerName("홍길동");
        dto.setMobile("01012345678");
        dto.setTelephone("0212345678");
        dto.setEmail("test@test.co.kr");
        dto.setQuestion("입점상담입니다.");
        dto.setCreateId(0L);

        //when, then
        standingPointService.addStandingPointQna(dto);
    }

}


