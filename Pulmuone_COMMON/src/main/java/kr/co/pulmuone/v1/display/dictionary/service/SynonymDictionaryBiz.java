package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.PagingListDataDto;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymDictionarySaveRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymSearchRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.SynonymDictionaryVo;

import java.util.List;

public interface SynonymDictionaryBiz {

    List<SynonymDictionaryVo> getSynonymDictionary(SynonymSearchRequestDto searchDto);

    PagingListDataDto<SynonymDictionaryVo> getSynonymDictionaryPagingList(SynonymSearchRequestDto searchDto);

    ApiResult<?> saveSynonymDictionary(SynonymDictionarySaveRequestDto dto) throws BaseException;
}
