package kr.co.pulmuone.v1.system.help.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.mapper.system.help.SystemHelpMapper;
import kr.co.pulmuone.v1.system.help.dto.GetHelpListRequestDto;
import kr.co.pulmuone.v1.system.help.dto.GetHelpResponseDto;
import kr.co.pulmuone.v1.system.help.dto.HelpRequestDto;
import kr.co.pulmuone.v1.system.help.dto.vo.GetHelpListResultVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

@Slf4j
class SystemHelpServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private SystemHelpService systemHelpService;

	@InjectMocks
	private SystemHelpService mockSystemHelpService;

	@Mock
	SystemHelpMapper mockSystemHelpMapper;

    @BeforeEach
    void beforeEach() {
        preLogin();
		mockSystemHelpService = new SystemHelpService(mockSystemHelpMapper);
    }

    @Test
    void 도움말_리스트조회_성공() {

    	Page<GetHelpListResultVo> resultVo = systemHelpService.getHelpList(new GetHelpListRequestDto());

    	resultVo.stream().forEach(
                i -> log.info(" resultVo : {}",  i)
        );

        assertTrue(CollectionUtils.isNotEmpty(resultVo));
    }


    @Test
    void 도움말_리스트조회_실패() {

    	Page<GetHelpListResultVo> resultVo = systemHelpService.getHelpList(new GetHelpListRequestDto());

    	resultVo.stream().forEach(
                i -> log.info(" resultVo : {}",  i)
        );

    	assertFalse(CollectionUtils.isEmpty(resultVo));
    }


    @Test
    void 도움말_상세조회_성공() {
    	long id = 147l;
    	GetHelpResponseDto result = systemHelpService.getHelp(id);
    	assertTrue(result.getRows() != null);
    }


    @Test
    void 도움말_상세조회_실패() {
    	long id = 147l;
    	GetHelpResponseDto result = systemHelpService.getHelp(id);
    	assertFalse(result.getRows() == null);
    }


    @Test
    void 도움말_중복체크_성공() {
    	HelpRequestDto dto = new HelpRequestDto();
    	dto.setInputDivisionCode("HELP_DIV.HELP_MENU");
    	dto.setDivisionId("9999");

    	assertTrue(systemHelpService.duplicateHelpCount(dto) == 0);
    }


    @Test
    void 도움말_중복체크_실패() {
    	HelpRequestDto dto = new HelpRequestDto();
    	dto.setInputDivisionCode("HELP_DIV.HELP_ST");
    	dto.setDivisionId("1310203217");

    	assertFalse(systemHelpService.duplicateHelpCount(dto) == 0);
    }



    @Test
    void 도움말_추가_성공() {
    	HelpRequestDto dto = new HelpRequestDto();
    	dto.setInputDivisionCode("HELP_DIV.HELP_MENU");
    	dto.setDivisionId("7224");
    	dto.setTitle("테스트");
    	dto.setContent("TEST JAVA 테스트 내용");
    	dto.setInputUseYn("N");

    	assertTrue(systemHelpService.addHelp(dto) > 0);
    }

    @Test
    void 도움말_추가_실패() {
    	HelpRequestDto dto = new HelpRequestDto();
    	dto.setInputDivisionCode("HELP_DIV.HELP_MENU");
    	dto.setDivisionId("7224");
    	dto.setTitle("테스트");
    	dto.setContent("TEST JAVA 테스트 내용");


        // when, then
        assertThrows(Exception.class, () -> {
        	systemHelpService.addHelp(dto);
        });
    }


    @Test
    void 도움말_수정_성공() {
    	HelpRequestDto dto = new HelpRequestDto();
    	dto.setInputDivisionCode("HELP_DIV.HELP_MENU");
    	dto.setDivisionId("7224");
    	dto.setTitle("테스트");
    	dto.setContent("TEST JAVA 테스트 내용");
    	dto.setInputUseYn("N");
    	dto.setId("147");

    	assertTrue(systemHelpService.putHelp(dto) > 0);
    }

    @Test
    void 도움말_수정_실패() {
    	HelpRequestDto dto = new HelpRequestDto();
    	dto.setInputDivisionCode("HELP_DIV.HELP_MENU");
    	dto.setDivisionId("7224");
    	dto.setTitle("테스트");
    	dto.setContent("TEST JAVA 테스트 내용");
    	dto.setInputUseYn("N");
     	dto.setId("5499999");

     	assertTrue(systemHelpService.putHelp(dto) == 0);
    }

    @Test
    void 도움말_해당도움말_리스트조회_성공() {
    	List<String> systemHelpIdList = new ArrayList<>();
    	systemHelpIdList.add("147");
    	systemHelpIdList.add("148");

    	GetHelpListRequestDto getHelpListRequestDto = new GetHelpListRequestDto();
    	getHelpListRequestDto.setSystemHelpIdList(systemHelpIdList);

    	List<GetHelpListResultVo> resultVo = systemHelpService.getHelpListByArray(getHelpListRequestDto);

    	resultVo.stream().forEach(
                i -> log.info(" resultVo : {}",  i)
        );

        assertTrue(CollectionUtils.isNotEmpty(resultVo));
    }


    @Test
    void 도움말_해당도움말_리스트조회_실패() {

    	List<GetHelpListResultVo> resultVo = systemHelpService.getHelpListByArray(new GetHelpListRequestDto());

    	resultVo.stream().forEach(
                i -> log.info(" resultVo : {}",  i)
        );

    	assertFalse(CollectionUtils.isEmpty(resultVo));
    }

    @Test
    void delHelp() {
		given(mockSystemHelpMapper.delHelp(any())).willReturn(1);
		ApiResult result = mockSystemHelpService.delHelp("1");
		assertEquals(ApiResult.success().getMessageEnum(), result.getMessageEnum());
    }
}