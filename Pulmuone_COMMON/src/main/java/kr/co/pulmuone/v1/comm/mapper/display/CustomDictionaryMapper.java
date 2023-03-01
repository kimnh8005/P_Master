package kr.co.pulmuone.v1.comm.mapper.display;

import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionarySearchDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionaryDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.CustomDictionaryVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomDictionaryMapper {

    List<CustomDictionaryVo> getCustomDictionary(CustomDictionarySearchDto customDictionarySearchDto);

    Integer getCustomDictionaryCount(CustomDictionarySearchDto customDictionarySearchDto);

    int checkCustomizeDictionaryDuplicate(String customizeWord);

    int checkCustomizeDictionaryDuplicateByUpdate(List<CustomDictionaryDto> customDictionaryList);

    int addCustomDictionary(List<CustomDictionaryDto> insertList);

    int updateCustomDictionary(List<CustomDictionaryDto> updateList);

    int deleteCustomDictionary(List<CustomDictionaryDto> deleteList);
}
