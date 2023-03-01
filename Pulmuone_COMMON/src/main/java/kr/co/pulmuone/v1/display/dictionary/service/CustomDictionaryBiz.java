package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.PagingListDataDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionaryDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionarySaveRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionarySearchDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.CustomDictionaryVo;

import java.util.List;

public interface CustomDictionaryBiz {

    /**
     * 사용자 정의 사전 목록 조회
     * @param customDictionarySearchDto
     * @return
     */
    List<CustomDictionaryVo> getCustomDictionary(CustomDictionarySearchDto customDictionarySearchDto);

    /**
     * 사용자 정의 사전 페이징 리스트
     * @param customDictionarySearchDto
     * @return
     */
     PagingListDataDto<CustomDictionaryVo> customDictionaryPagingList(CustomDictionarySearchDto customDictionarySearchDto);

    /**
     * 사용자 정의 사전 목록 편집
     * @param saveRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> saveCustomDictionary(CustomDictionarySaveRequestDto saveRequestDto) throws Exception;


    /**
     * 사용자 정의 사전 추가
     * @param list
     * @return
     */
    ApiResult<?> addCustomDictionary(List<CustomDictionaryDto> list);

}
