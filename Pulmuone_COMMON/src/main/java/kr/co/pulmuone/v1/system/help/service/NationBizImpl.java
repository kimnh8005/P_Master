package kr.co.pulmuone.v1.system.help.service;

import static kr.co.pulmuone.v1.system.help.vo.DictionaryTypes.WORD;

import javax.validation.constraints.NotNull;
import org.springframework.validation.BindException;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.system.help.dto.DictionarySearchRequestDto;
import kr.co.pulmuone.v1.system.help.vo.DictionaryMasterVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NationBizImpl implements NationBiz {

    @NotNull
    private final NationService nationService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class, BindException.class})
    public ApiResult<?> saveDictionaryMaster(DictionaryMasterVo vo) throws Exception {
        if (nationService.findDictionaryMasterById(vo.getId()) != null) {
          /* HGRM-2017 : dgyoun : 오류 메시지 변경 */
          //return DUPLICATE_DATA;
          return ApiResult.result(UserEnums.SystemSetting.SYSTEM_SETTING_DUPLICATE_SEQ);
        }
        if (existsDuplicatedWord(vo)) {
          /* HGRM-2017 : dgyoun : 오류 메시지 변경 */
          //return DUPLICATE_DATA;
          return ApiResult.result(UserEnums.SystemSetting.SYSTEM_SETTING_DUPLICATE_WORD);
        }
        nationService.saveDictionaryMaster(vo);
	    nationService.saveDictionary(vo);
	    return ApiResult.success();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class, BindException.class})
    public ApiResult<?> updateDictionaryMaster(Long id, String baseName) throws Exception {
        if (StringUtils.isEmpty(baseName)) {
            return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);
        }
        DictionaryMasterVo vo = nationService.findDictionaryMasterById(id);
        if (vo == null) {
            return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);
        }
        vo.modifyBaseName(baseName);
        if (existsDuplicatedWord(vo)) {
          /* HGRM-2017 : dgyoun : 오류 메시지 변경 */
          //return DUPLICATE_DATA;
          return ApiResult.result(UserEnums.SystemSetting.SYSTEM_SETTING_DUPLICATE_WORD);
        }
        nationService.updateDictionaryMaster(vo);
        nationService.updateDictionary(vo);
        return ApiResult.success();
    }

    /**
     * Seq는 구분 상관없이 유니크한 값이고 표준용어의 경우 단어의 경우는 중복 불가, 도움말은 중복 가능 입니다. (이철호 매니저님)
     * @param vo
     * @return
     */
    private boolean existsDuplicatedWord(DictionaryMasterVo vo) {
        if (vo.getDictionaryType() == WORD) {
            return nationService.existsDictionaryMasterEqualsTypeAndBaseName(vo);
        }
        return false;
    }

    @Override
    public DictionaryMasterVo findDictionaryMasterById(Long id) {
        return nationService.findDictionaryMasterById(id);
    }

    @Override
    public Page<DictionaryMasterVo> findDictionaryMasterList(DictionarySearchRequestDto dto) {
        return nationService.findDictionaryMasterList(dto);
    }
}
