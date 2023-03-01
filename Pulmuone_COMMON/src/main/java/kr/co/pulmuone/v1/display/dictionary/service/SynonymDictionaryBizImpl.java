package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.PagingListDataDto;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.SearchEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionaryDto;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymDictionarySaveRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymDictionarySaveResultDto;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymSearchRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.SynonymDictionaryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SynonymDictionaryBizImpl implements SynonymDictionaryBiz {

    @Autowired
    SynonymDictionaryService synonymDictionaryService;

    @Autowired
    CustomDictionaryBiz customDictionaryBiz;


    @Override
    public List<SynonymDictionaryVo> getSynonymDictionary(SynonymSearchRequestDto searchDto) {
        return synonymDictionaryService.getSynonymDictionaryList(searchDto);
    }

    @Override
    public PagingListDataDto<SynonymDictionaryVo> getSynonymDictionaryPagingList(SynonymSearchRequestDto searchDto) {
        List<SynonymDictionaryVo> list = synonymDictionaryService.getSynonymDictionaryList(searchDto);
        int totalCount = synonymDictionaryService.getSynonymDictionaryListCount(searchDto);

        return new PagingListDataDto<>(searchDto.getPage()
                , searchDto.getPageSize()
                , totalCount
                , list);
    }

    @Override
    @Transactional
    public ApiResult<?> saveSynonymDictionary(SynonymDictionarySaveRequestDto dto) throws BaseException {

        SynonymDictionarySaveResultDto result = new SynonymDictionarySaveResultDto();

        if (dto.hasInsertData()) {
            ApiResult insertResult = synonymDictionaryService.addSynonym(dto.getInsertList());
            if (SearchEnums.DictionaryMessage.DUPLICATE_DATA.getCode() == insertResult.getCode()) {
                result.setDuplicateWordList((List<String>)insertResult.getData());
            }
            if (SearchEnums.DictionaryMessage.SYNONYM_NEED_USER_DEFINE_WORD.getCode() == insertResult.getCode()) {
                result = addCustomDictionary(insertResult);
            }
        }

        if (dto.hasUpdateData()) {
            ApiResult updateResult = synonymDictionaryService.updateSynonymDictionary(dto.getUpdateList());
            if (SearchEnums.DictionaryMessage.DUPLICATE_DATA.getCode() == updateResult.getCode()) {
                result.setDuplicateWordList((List<String>)updateResult.getData());
            }
            if (SearchEnums.DictionaryMessage.SYNONYM_NEED_USER_DEFINE_WORD.getCode() == updateResult.getCode()) {
                result = addCustomDictionary(updateResult);
            }
        }

        if (dto.hasDeleteData()) {
            ApiResult deleteResult = synonymDictionaryService.deleteSynonymDictionary(dto.getDeleteList());
            if (BaseEnums.Default.SUCCESS.getCode() !=  deleteResult.getCode()) return deleteResult;
        }

        return ApiResult.success(result);
    }


    private SynonymDictionarySaveResultDto addCustomDictionary(ApiResult apiResult) {

        List<CustomDictionaryDto> customWordDtoList = new ArrayList<>();
        List<String> customWordList = (List<String>) apiResult.getData();
        customWordList.stream().forEach(
                w -> {
                    log.info(w);
                    CustomDictionaryDto customWordDto = new CustomDictionaryDto();
                    customWordDto.setCustomizeWord(w);
                    customWordDto.setUseYn("Y");
                    customWordDtoList.add(customWordDto);
                }
        );
        customDictionaryBiz.addCustomDictionary(customWordDtoList);

        SynonymDictionarySaveResultDto result = new SynonymDictionarySaveResultDto();
        result.setCustomWordList(customWordList);

        return result;
    }
}
