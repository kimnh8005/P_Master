package kr.co.pulmuone.v1.system.help.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.help.dto.DictionarySearchRequestDto;
import kr.co.pulmuone.v1.system.help.vo.DictionaryMasterVo;

public interface NationBiz {

    ApiResult<?> saveDictionaryMaster(DictionaryMasterVo vo) throws Exception;

    ApiResult<?> updateDictionaryMaster(Long id, String baseName) throws Exception;

    DictionaryMasterVo findDictionaryMasterById(Long id);

    Page<DictionaryMasterVo> findDictionaryMasterList(DictionarySearchRequestDto dto);
}
