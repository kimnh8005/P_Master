package kr.co.pulmuone.v1.comm.mapper.display;

import kr.co.pulmuone.v1.display.dictionary.dto.SynonymAddRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymDictionaryDto;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymSearchRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.SynonymDictionaryVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SynonymDictionaryMapper {
    
    List<SynonymDictionaryVo> getSynonymDictionaryList(SynonymSearchRequestDto searchDto);

    int getSynonymDictionaryListCount(SynonymSearchRequestDto searchDto);

    int deleteSynonym(List<SynonymDictionaryDto> deleteList);

    int deleteSynonymDetail(List<SynonymDictionaryDto> deleteList);

    int addSynonym(SynonymDictionaryDto insertRequestDto);

    int checkSynonymDuplicate(String synonym);

    int addSynonymDetail(SynonymAddRequestDto detailSynonym);

    int updateSynonym(SynonymDictionaryDto dto);
}
