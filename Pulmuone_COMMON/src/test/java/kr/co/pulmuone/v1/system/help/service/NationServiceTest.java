package kr.co.pulmuone.v1.system.help.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.mapper.system.help.NationMapper;
import kr.co.pulmuone.v1.system.help.dto.DictionarySearchRequestDto;
import kr.co.pulmuone.v1.system.help.vo.DictionaryMasterVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import static kr.co.pulmuone.v1.system.help.vo.DictionaryTypes.WORD;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
class NationServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private NationService nationService;

    @InjectMocks
    private NationService mockNationService;

    @Mock
    NationMapper mockNationMapper;

    @BeforeEach
    void beforeEach() {
        preLogin();
        mockNationService = new NationService(mockNationMapper);
    }

    @Test
    void 표준용어마스터_등록_중복ID() {
        // given
        DictionaryMasterVo requestVo = DictionaryMasterVo.builder()
            .id(1L)
            .baseName("test")
            .dictionaryType(WORD)
            .build();
        log.info("requestVo: {}", requestVo);

        // when, then
        assertThrows(DuplicateKeyException.class, () -> {
            nationService.saveDictionaryMaster(requestVo);
        });
    }

    @Test
    void 표준용어_등록_정상() {
        // given
        DictionaryMasterVo requestVo = DictionaryMasterVo.builder()
            .id(Long.MAX_VALUE)
            .baseName("test")
            .dictionaryType(WORD)
            .build();
        log.info("requestVo: {}", requestVo);

        // when, then
        nationService.saveDictionaryMaster(requestVo);
     }

    @Test
    void 표준용어_수정_정상() {
        // given
        DictionaryMasterVo requestVo = DictionaryMasterVo.builder()
            .id(Long.MAX_VALUE)
            .baseName("test")
            .dictionaryType(WORD)
            .build();
        nationService.saveDictionaryMaster(requestVo);
        log.info("requestVo: {}", requestVo);

        // when
        String changedBaseName = "pmo";
        requestVo.modifyBaseName(changedBaseName);
        int updatedCount = nationService.updateDictionaryMaster(requestVo);

        // then
        assertEquals(1, updatedCount);
        DictionaryMasterVo savedVo = nationService.findDictionaryMasterById(requestVo.getId());
        assertEquals(changedBaseName, savedVo.getBaseName());
    }

    @Test
    void 표준용어_단어검색시_다른_구분타입_검색되면_안됨() {
        // given
        DictionarySearchRequestDto dto = new DictionarySearchRequestDto("", WORD.getCode());

        // when
        Page<DictionaryMasterVo> result = nationService.findDictionaryMasterList(dto);
        boolean matched = result.stream()
            .anyMatch(vo -> vo.getDictionaryType() != WORD);

        // then
        assertFalse(matched);
    }

    @Test
    void 표준용어구분과_표중용어가_같은_데이터가_존재할경우_true() {
        // given
        DictionaryMasterVo requestVo = DictionaryMasterVo.builder()
            .id(Long.MAX_VALUE)
            .baseName("test")
            .dictionaryType(WORD)
            .build();
        nationService.saveDictionaryMaster(requestVo);

        // when
        boolean exists = nationService.existsDictionaryMasterEqualsTypeAndBaseName(requestVo);

        // then
        assertTrue(exists);
    }

    @Test
    void saveDictionary() {
        mockNationService.saveDictionary(null);
    }

    @Test
    void updateDictionary() {
        given(mockNationMapper.updateDictionary(any())).willReturn(1);
        int n = mockNationService.updateDictionary(null);
        assertTrue(n > 0);
    }
}