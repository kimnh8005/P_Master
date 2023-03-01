package kr.co.pulmuone.v1.system.help.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import javax.validation.constraints.NotNull;
import kr.co.pulmuone.v1.comm.mapper.system.help.NationMapper;
import kr.co.pulmuone.v1.system.help.dto.DictionarySearchRequestDto;
import kr.co.pulmuone.v1.system.help.vo.DictionaryMasterVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NationService {

    @NotNull
    private final NationMapper nationMapper;


    protected void saveDictionaryMaster(DictionaryMasterVo vo) {
        nationMapper.saveDictionaryMaster(vo);
    }

    protected void saveDictionary(DictionaryMasterVo vo) {
        nationMapper.saveDictionary(vo);
    }

    protected int updateDictionaryMaster(DictionaryMasterVo vo) {
        return nationMapper.updateDictionaryMaster(vo);
    }

    protected int updateDictionary(DictionaryMasterVo vo) {
        return nationMapper.updateDictionary(vo);
    }

    protected DictionaryMasterVo findDictionaryMasterById(Long id) {
        return nationMapper.findDictionaryMasterById(id);
    }

    protected Page<DictionaryMasterVo> findDictionaryMasterList(DictionarySearchRequestDto dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        return nationMapper.findDictionaryMasterList(dto);
    }

    protected boolean existsDictionaryMasterEqualsTypeAndBaseName(DictionaryMasterVo vo) {
        return nationMapper.countDictionaryMasterByTypeAndBaseName(vo) > 0;
    }
}
