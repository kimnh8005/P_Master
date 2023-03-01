package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.SearchEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.display.SynonymDictionaryMapper;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymAddRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymDictionaryDto;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymSearchRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.SynonymDictionaryVo;
import kr.co.pulmuone.v1.search.indexer.analyzer.WordAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class SynonymDictionaryService {

    @Autowired
    private SynonymDictionaryMapper synonymDictionaryMapper;

    protected List<SynonymDictionaryVo> getSynonymDictionaryList(SynonymSearchRequestDto searchDto) {
        return synonymDictionaryMapper.getSynonymDictionaryList(searchDto);
    }

    protected int getSynonymDictionaryListCount(SynonymSearchRequestDto searchDto) {
        return synonymDictionaryMapper.getSynonymDictionaryListCount(searchDto);
    }

    protected ApiResult<?> addSynonym(List<SynonymDictionaryDto> list) {

        List<String> synonymList = new ArrayList<>();
        for (SynonymDictionaryDto dto : list) {
            synonymList.add(dto.getRepresentSynonym());
            synonymList.addAll(Arrays.asList(dto.getSynonym().split(",")));
        }
        List<String> duplicateList = checkDuplicateSynonym(synonymList);

        if (CollectionUtils.isNotEmpty(duplicateList)) {
            return ApiResult.result(duplicateList, SearchEnums.DictionaryMessage.DUPLICATE_DATA);
        }

        for (SynonymDictionaryDto dto : list) {
            synonymDictionaryMapper.addSynonym(dto);
            String dpSynonymDicId = dto.getDpSynonymDicId();

            List<String> memberList = new ArrayList<>();
            memberList.addAll(Arrays.asList(dto.getSynonym().split(",")));
            memberList.add(dto.getRepresentSynonym());

            for (String synonym : memberList) {
                SynonymAddRequestDto memberSynonymDto = new SynonymAddRequestDto();
                memberSynonymDto.setDpSynonymDicId(dpSynonymDicId);
                if (dto.getRepresentSynonym().equals(synonym) ) {
                    memberSynonymDto.setRepresentYn("Y");
                } else {
                    memberSynonymDto.setRepresentYn("N");
                }
                memberSynonymDto.setSynonym(synonym);
                synonymDictionaryMapper.addSynonymDetail(memberSynonymDto);
            }

        }

        List<String> compoundNounList = WordAnalyzer.reduceCompoundNounList(synonymList);
        if (CollectionUtils.isNotEmpty(compoundNounList)) {
            return ApiResult.result(compoundNounList, SearchEnums.DictionaryMessage.SYNONYM_NEED_USER_DEFINE_WORD);
        }

        return ApiResult.success();
    }

    protected ApiResult updateSynonymDictionary(List<SynonymDictionaryDto> list) throws BaseException {
        synonymDictionaryMapper.deleteSynonymDetail(list);

        List<String> synonymList = new ArrayList<>();
        for (SynonymDictionaryDto dto : list) {
            synonymList.add(dto.getRepresentSynonym());
            synonymList.addAll(Arrays.asList(dto.getSynonym().split(",")));
        }
        List<String> duplicateList = checkDuplicateSynonym(synonymList);

        if (CollectionUtils.isNotEmpty(duplicateList)) {
//            return ApiResult.result(duplicateList, SearchEnums.DictionaryMessage.DUPLICATE_DATA);
            throw new BaseException(SearchEnums.DictionaryMessage.DUPLICATE_DATA);
        }

        for (SynonymDictionaryDto dto : list) {
            synonymDictionaryMapper.updateSynonym(dto);

            String dpSynonymDicId = dto.getDpSynonymDicId();
            List<String> memberList = new ArrayList<>();
            memberList.addAll(Arrays.asList(dto.getSynonym().split(",")));
            memberList.add(dto.getRepresentSynonym());

            for (String synonym : memberList) {
                SynonymAddRequestDto memberSynonymDto = new SynonymAddRequestDto();
                memberSynonymDto.setDpSynonymDicId(dpSynonymDicId);
                if (dto.getRepresentSynonym().equals(synonym) ) {
                    memberSynonymDto.setRepresentYn("Y");
                } else {
                    memberSynonymDto.setRepresentYn("N");
                }
                memberSynonymDto.setSynonym(synonym);
                synonymDictionaryMapper.addSynonymDetail(memberSynonymDto);
            }

        }

        List<String> compoundNounList = WordAnalyzer.reduceCompoundNounList(synonymList);
        if (CollectionUtils.isNotEmpty(compoundNounList)) {
            return ApiResult.result(compoundNounList, SearchEnums.DictionaryMessage.SYNONYM_NEED_USER_DEFINE_WORD);
        }
        return ApiResult.success();

    }

    protected ApiResult deleteSynonymDictionary(List<SynonymDictionaryDto> deleteList) {
        synonymDictionaryMapper.deleteSynonym(deleteList);
        synonymDictionaryMapper.deleteSynonymDetail(deleteList);
        return ApiResult.success();
    }


    private List<String> checkDuplicateSynonym(List<String> synonymList) {
        List<String> duplicateList = new ArrayList<>();
        for (String synonym : synonymList) {
            int count = synonymDictionaryMapper.checkSynonymDuplicate(synonym);
            if (count > 0) duplicateList.add(synonym);
        }
        return duplicateList;
    }

}
