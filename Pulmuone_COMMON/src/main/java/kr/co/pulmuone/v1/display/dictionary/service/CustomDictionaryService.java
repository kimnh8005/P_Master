package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.SearchEnums;
import kr.co.pulmuone.v1.comm.mapper.display.CustomDictionaryMapper;
import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionaryDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionarySearchDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.CustomDictionaryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomDictionaryService {

    @Autowired
    private CustomDictionaryMapper customDictionaryMapper;


    /**
     * 사용자 정의 사전 목록 조회
     * @param customDictionarySearchDto
     * @return
     */
    protected List<CustomDictionaryVo> getCustomDictionary(CustomDictionarySearchDto customDictionarySearchDto) {
        return customDictionaryMapper.getCustomDictionary(customDictionarySearchDto);
    }

    /**
     * 사용자 정의 사전 total count
     * @param customDictionarySearchDto
     * @return
     */
    protected int getCustomDictionaryCount(CustomDictionarySearchDto customDictionarySearchDto) {
        return customDictionaryMapper.getCustomDictionaryCount(customDictionarySearchDto);
    }


    /**
     * 사용자정의사전 데이터 삭제
     * @param list
     * @return
     */
    protected ApiResult<?> deleteCustomDictionary(List<CustomDictionaryDto> list) {
        customDictionaryMapper.deleteCustomDictionary(list);
        return ApiResult.success();
    }


    /**
     * 사용자정의사전 데이터 수정
     * @param list
     * @return
     */
    protected ApiResult<?> updateCustomDictionary(List<CustomDictionaryDto> list) {

        if (isDuplicateCustomWordByUpdate(list)) {
            return ApiResult.result(SearchEnums.DictionaryMessage.DUPLICATE_DATA);
        }
        customDictionaryMapper.updateCustomDictionary(list);

        return ApiResult.success();

    }

    /**
     * 사용자정의사전 데이터 추가
     * @param list
     * @return
     */
    protected ApiResult<?> addCustomDictionary(List<CustomDictionaryDto> list) {

        if (isDuplicateCustomWord(list)) {
            return ApiResult.result(SearchEnums.DictionaryMessage.DUPLICATE_DATA);
        }
        customDictionaryMapper.addCustomDictionary(list);

        return ApiResult.success();

    }


    /**
     * 사용자 정의 사전 편집 대상 중복 여부
     * @param requestList
     * @return
     */
    private boolean isDuplicateCustomWord(List<CustomDictionaryDto> requestList) {
        List<String> reducedList = requestList.stream()
                .map(CustomDictionaryDto::getCustomizeWord)
                .distinct()
                .collect(Collectors.toList());

        if (requestList.size() > reducedList.size()) return true;

        for (String customizeWord : reducedList) {
            int count = customDictionaryMapper.checkCustomizeDictionaryDuplicate(customizeWord);
            if (count > 0) return true;
        }

        return false;
    }

    /**
     * 사용자 정의 사전 편집 대상 중복 여부 (업데이트용)
     *
     * @param requestList
     * @return
     */
	private boolean isDuplicateCustomWordByUpdate(List<CustomDictionaryDto> requestList) {
		int count = customDictionaryMapper.checkCustomizeDictionaryDuplicateByUpdate(requestList);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}
}
