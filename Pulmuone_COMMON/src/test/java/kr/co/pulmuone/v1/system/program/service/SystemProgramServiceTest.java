package kr.co.pulmuone.v1.system.program.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.system.program.dto.*;
import kr.co.pulmuone.v1.system.program.dto.vo.GetProgramListResultVo;
import kr.co.pulmuone.v1.system.program.dto.vo.GetProgramResultVo;
import kr.co.pulmuone.v1.system.program.dto.vo.ProgramAuthVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SystemProgramServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
	private SystemProgramService service;

	@BeforeEach
	void beforeEach() {
		preLogin();
	}

	@Test
	void 프로그램_등록_성공() {
		ProgramRequestDto pgmDto = ProgramRequestDto.builder()
				.programId("99999")
				.url("/test_test")
				.useYn("N")
				.businessType("PP")
				.htmlPath("./")
				.gbDictionaryMasterId(999L)
				.build();

		int flag = service.addProgram(pgmDto);

		assertEquals(1, flag);
	}

	@Test
	void 프로그램_등록_실패() {
		ProgramRequestDto pgmDto = ProgramRequestDto.builder()
				.programId("policyBbsAuthMgm")
				.url("/policyBbsAuthMgm")
				.useYn("Y")
				.businessType("20")
				.htmlPath("admin/ps/bbs/")
				.gbDictionaryMasterId(100002137L)
				.build();

		assertThrows(DuplicateKeyException.class, () -> {
			service.addProgram(pgmDto);
		});
	}


	@Test
	void 프로그램_등록_검색_상세_수정_삭제_성공() {
		ProgramRequestDto pgmDto = ProgramRequestDto.builder()
				.programId("99999")
				.url("/test_test")
				.useYn("N")
				.businessType("PP")
				.htmlPath("./")
				.gbDictionaryMasterId(999L)
				.build();

		if(service.existsProgram(pgmDto) > 0) {
			Throwable exception = assertThrows(DuplicateKeyException.class, () -> {
				throw new DuplicateKeyException("중복 데이터 존재");
			});

			assertEquals(exception.getMessage(), "중복 데이터 존재");
		} else {
			int addFlag = service.addProgram(pgmDto);

			assertEquals(1, addFlag);
		}

		GetProgramListRequestDto pgmListDto = GetProgramListRequestDto.builder()
				.programId("99999")
				.url("/test_test")
				.build();

		GetProgramListResponseDto findAllProgramList = service.findAllProgramList(pgmListDto);
		assertEquals(1, findAllProgramList.getRows().size());

		GetProgramListResultVo pgmResultVo = findAllProgramList.getRows().get(0);

		ProgramRequestDto pgmDtlDto = ProgramRequestDto.builder()
				.stProgramId(pgmResultVo.getStProgramId())
				.build();

		GetProgramResultVo pgmDtlVo = service.findProgram(pgmDtlDto).getRows();
		assertEquals("99999", pgmDtlVo.getProgramId());

		pgmDtlDto = ProgramRequestDto.builder()
				.stProgramId(pgmDtlVo.getStProgramId())
				.programId(pgmDtlVo.getProgramId())
				.gbDictionaryMasterId(pgmDtlVo.getGbDictionaryMasterId())
				.businessType(pgmDtlVo.getBusinessType())
				.htmlPath(pgmDtlVo.getHtmlPath())
				.url(pgmDtlVo.getUrl())
				.useYn(pgmDtlVo.getUseYn())
				.build();


		int updateFlag = service.putProgram(pgmDtlDto);

		assertEquals(1, updateFlag);

		int deleteFlag = service.delProgram(pgmDtlDto.getStProgramId());

		assertEquals(1, deleteFlag);

	}

	@Test
	void 프로그램_이름_검색() {
		GetProgramNameListRequestDto pgmListDto = new GetProgramNameListRequestDto();

		GetProgramNameListResponseDto findAllProgramList = service.findAllProgramNameList(pgmListDto);

		assertTrue(CollectionUtils.isNotEmpty(findAllProgramList.getRows()));
	}


	@Test
	void addProgramAuth_추가() {

		ProgramRequestDto programRequestDto = ProgramRequestDto.builder()
				.stProgramId(999999L)
				.build();

		ProgramAuthVo programAuthVo = new ProgramAuthVo();
		programAuthVo.setProgramAuthCode("TEST");
		programAuthVo.setProgramAuthCodeName("TEST");
		programAuthVo.setProgramAuthCodeNameMemo("TEST");
		programAuthVo.setUseYn("N");

		List<ProgramAuthVo> authInsertVoList = new ArrayList<>();
		authInsertVoList.add(programAuthVo);

		programRequestDto.setAuthInsertVoList(authInsertVoList);

		int count = service.addProgramAuth(programRequestDto);
		Assertions.assertTrue(count > 0);

	}

	@Test
	void putProgramAuth_수정() {
		ProgramRequestDto programRequestDto = ProgramRequestDto.builder()
				.build();

		ProgramAuthVo programAuthVo = new ProgramAuthVo();
		programAuthVo.setProgramAuthCode("DEFAULT");
		programAuthVo.setProgramAuthCodeName("기본");
		programAuthVo.setUseYn("Y");
		programAuthVo.setStProgramAuthId(1L);

		List<ProgramAuthVo> authUpdateVoList = new ArrayList<>();
		authUpdateVoList.add(programAuthVo);
		programRequestDto.setAuthUpdateVoList(authUpdateVoList);

		int count = service.putProgramAuth(programRequestDto);
		Assertions.assertTrue(count > 0);
	}


	@Test
	void deleteProgramAuth_삭제() {
		service.delProgramAuthByStProgramId(999999L);
	}

	@Test
	void getProgramAuthUseList() {
		service.getProgramAuthUseList(999999L);
	}



}