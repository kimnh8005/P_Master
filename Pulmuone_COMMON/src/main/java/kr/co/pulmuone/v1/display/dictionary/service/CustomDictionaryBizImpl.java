package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.PagingListDataDto;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionaryDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionarySaveRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionarySearchDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.CustomDictionaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomDictionaryBizImpl implements CustomDictionaryBiz {

    @Autowired
    private CustomDictionaryService customDictionaryService;

    @Override
    public List<CustomDictionaryVo> getCustomDictionary(CustomDictionarySearchDto customDictionarySearchDto) {
        return customDictionaryService.getCustomDictionary(customDictionarySearchDto);
    }


    @Override
    public PagingListDataDto<CustomDictionaryVo> customDictionaryPagingList(CustomDictionarySearchDto customDictionarySearchDto) {

        List<CustomDictionaryVo> dictionaryList = customDictionaryService.getCustomDictionary(customDictionarySearchDto);
        int totalCount = customDictionaryService.getCustomDictionaryCount(customDictionarySearchDto);

        return new PagingListDataDto<>(customDictionarySearchDto.getPage()
                , customDictionarySearchDto.getPageSize()
                , totalCount
                , dictionaryList);
    }


    @Override
    @Transactional
    public ApiResult<?> saveCustomDictionary(CustomDictionarySaveRequestDto saveRequestDto) {

        if (saveRequestDto.hasInsertData()) {
            ApiResult insertResult = customDictionaryService.addCustomDictionary(saveRequestDto.getInsertList());
            if (BaseEnums.Default.SUCCESS.getCode() != insertResult.getCode()) return insertResult;
        }

        if (saveRequestDto.hasUpdateData()) {
            ApiResult updateResult = customDictionaryService.updateCustomDictionary(saveRequestDto.getUpdateList());
            if (BaseEnums.Default.SUCCESS.getCode() != updateResult.getCode()) return updateResult;
        }

        if (saveRequestDto.hasDeleteData()) {
            ApiResult deleteResult = customDictionaryService.deleteCustomDictionary(saveRequestDto.getDeleteList());
            if (BaseEnums.Default.SUCCESS.getCode() !=  deleteResult.getCode()) return deleteResult;
        }

        return ApiResult.success();
    }


    @Override
    public ApiResult<?> addCustomDictionary(List<CustomDictionaryDto> list) {
        ApiResult insertResult = customDictionaryService.addCustomDictionary(list);
        if (BaseEnums.Default.SUCCESS.getCode() != insertResult.getCode()) return insertResult;

        return ApiResult.success();
    }


}
