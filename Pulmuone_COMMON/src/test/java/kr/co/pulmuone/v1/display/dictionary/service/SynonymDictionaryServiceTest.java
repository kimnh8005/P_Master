package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.SearchEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymDictionaryDto;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymSearchRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.SynonymDictionaryVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class SynonymDictionaryServiceTest  extends CommonServiceTestBaseForJunit5 {

    @Autowired
    SynonymDictionaryService synonymDictionaryService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    public void test_동의어사전_조회() {
        SynonymSearchRequestDto dto = new SynonymSearchRequestDto();
        dto.setUseYn("Y");
        dto.setRepresentSynonym("사과");
        List<SynonymDictionaryVo> list = synonymDictionaryService.getSynonymDictionaryList(dto);
        list.stream().forEach(l->log.info(l.toString()));

        assertTrue(list.size() >= 0);
    }

    @Test
    public void test_동의어사전_카운트_조회() {
        SynonymSearchRequestDto dto = new SynonymSearchRequestDto();
        dto.setUseYn("Y");
        dto.setRepresentSynonym("사과");
        int count = synonymDictionaryService.getSynonymDictionaryListCount(dto);
        assertTrue(count >= 0);
    }

    @Test
    public void test_동의어_삭제() {
        List<SynonymDictionaryDto> list = new ArrayList<>();
        SynonymDictionaryDto dto = new SynonymDictionaryDto();
        dto.setDpSynonymDicId("1");
        list.add(dto);
        ApiResult result = synonymDictionaryService.deleteSynonymDictionary(list);
        assertTrue(BaseEnums.Default.SUCCESS.getCode() ==  result.getCode());
    }

    @Test
    public void test_동의어_추가() {
        List<SynonymDictionaryDto> list = new ArrayList<>();
        SynonymDictionaryDto dto = new SynonymDictionaryDto();
        dto.setRepresentSynonym("풀무원");
        dto.setSynonym("pulmuone,풀무");
        dto.setUseYn("Y");
        list.add(dto);
        ApiResult result = synonymDictionaryService.addSynonym(list);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() ==  result.getCode());

    }

    @Test
    public void test_동의어_추가_복합명사포함() {
        List<SynonymDictionaryDto> list = new ArrayList<>();
        SynonymDictionaryDto dto = new SynonymDictionaryDto();
        dto.setRepresentSynonym("풀무원");
        dto.setSynonym("pulmuone,풀무,건강식품");
        dto.setUseYn("Y");
        list.add(dto);
        ApiResult result = synonymDictionaryService.addSynonym(list);

        List<String> compoundNounList = (List<String>) result.getData();
        compoundNounList.stream().forEach(
                w -> log.info(w)
        );
        assertTrue(SearchEnums.DictionaryMessage.SYNONYM_NEED_USER_DEFINE_WORD.getCode() ==  result.getCode());
    }

    @Test
    public void test_동의어_추가_중복() {
        List<SynonymDictionaryDto> list = new ArrayList<>();
        SynonymDictionaryDto dto = new SynonymDictionaryDto();
        dto.setRepresentSynonym("사과");
        dto.setSynonym("애플");
        dto.setUseYn("Y");
        list.add(dto);
        ApiResult result = synonymDictionaryService.addSynonym(list);

        List<String> duplicateWordList = (List<String>) result.getData();
        duplicateWordList.stream().forEach(
                w -> log.info(w)
        );
        assertTrue(SearchEnums.DictionaryMessage.DUPLICATE_DATA.getCode() ==  result.getCode());
    }

    @Test
    public void test_동의어_수정() throws BaseException {
        List<SynonymDictionaryDto> list = new ArrayList<>();
        SynonymDictionaryDto dto = new SynonymDictionaryDto();
        dto.setDpSynonymDicId("1");
        dto.setRepresentSynonym("사과");
        dto.setSynonym("애플,apple,능금");
        dto.setUseYn("Y");
        list.add(dto);
        ApiResult result = synonymDictionaryService.updateSynonymDictionary(list);

        SynonymSearchRequestDto searchRequestDto = new SynonymSearchRequestDto();
        searchRequestDto.setUseYn("Y");
        searchRequestDto.setRepresentSynonym("사과");
        List<SynonymDictionaryVo> list2 = synonymDictionaryService.getSynonymDictionaryList(searchRequestDto);
        list2.stream().forEach(l->log.info(l.getSynonym()));

        assertTrue(BaseEnums.Default.SUCCESS.getCode() ==  result.getCode());
    }

}