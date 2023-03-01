package kr.co.pulmuone.bos.display.search.controller;


import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.bos.display.search.service.SearchDictionaryService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.PagingListDataDto;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionarySaveRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionarySearchDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.CustomDictionaryVo;
import kr.co.pulmuone.v1.display.dictionary.service.CustomDictionaryBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class CustomDictionaryController {

    @Autowired
    CustomDictionaryBiz customDictionaryBiz;

    @Autowired
    SearchDictionaryService searchDictionaryService;

    /**
     * 전시관리 > 사용자 정의 사전 목록 조회
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/dp/customizeDictionary/getCustomizeDictionaryList")
    public ApiResult<PagingListDataDto<CustomDictionaryVo>> getCustomizeDictionaryList(HttpServletRequest request) throws Exception {
        CustomDictionarySearchDto searchDto = (CustomDictionarySearchDto) BindUtil.convertRequestToObject(request, CustomDictionarySearchDto.class);
        return ApiResult.success(customDictionaryBiz.customDictionaryPagingList(searchDto));
    }


    /**
     * 전시관리 > 사용자 정의 사전 편집
     * @param saveRequestDto
     * @return
     * @throws Exception
     */
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" +
                    "[DUPLICATE_DATA] DUPLICATE_DATA - 중복 데이터가 있습니다."
            )
    })
    @PostMapping(value = "/admin/dp/customizeDictionary/saveCustomizeDictionary")
    public ApiResult<?> saveCustomDictionary(CustomDictionarySaveRequestDto saveRequestDto) throws Exception {
        saveRequestDto.convertDataList();
        return customDictionaryBiz.saveCustomDictionary(saveRequestDto);
    }


    /**
     * 전시관리 > 사용자 정의 사전 업로드
     * @return
     */
    @PostMapping(value = "/admin/dp/customizeDictionary/createReflectionCustomizeDictionaryFile")
    public ApiResult<?> uploadCustomDictionary() {
        return searchDictionaryService.uploadCustomDictionary();
    }


}
