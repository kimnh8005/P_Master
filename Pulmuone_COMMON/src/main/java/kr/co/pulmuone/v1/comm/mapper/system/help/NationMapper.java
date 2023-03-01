package kr.co.pulmuone.v1.comm.mapper.system.help;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.system.help.dto.DictionarySearchRequestDto;
import kr.co.pulmuone.v1.system.help.vo.DictionaryMasterVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NationMapper {

    void saveDictionaryMaster(DictionaryMasterVo vo);

    void saveDictionary(DictionaryMasterVo vo);

    int updateDictionaryMaster(DictionaryMasterVo vo);

    int updateDictionary(DictionaryMasterVo vo);

    DictionaryMasterVo findDictionaryMasterById(Long id);

    Page<DictionaryMasterVo> findDictionaryMasterList(DictionarySearchRequestDto dto);

    int countDictionaryMasterByTypeAndBaseName(DictionaryMasterVo vo);
}
