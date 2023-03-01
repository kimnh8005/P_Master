package kr.co.pulmuone.bos.display.search.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.bos.display.search.service.SearchDictionaryService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.PagingListDataDto;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymDictionarySaveRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymDictionarySaveResultDto;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymSearchRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.SynonymDictionaryVo;
import kr.co.pulmuone.v1.display.dictionary.service.SynonymDictionaryBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class SynonymDictionaryController {

    @Autowired
    SynonymDictionaryBiz synonymDictionaryBiz;

    @Autowired
    SearchDictionaryService searchDictionaryService;


    /**
     * 전시관리 > 동의어 사전관리 데이터를 조회
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/dp/synonymDictionary/getSynonymList")
    public ApiResult<PagingListDataDto<SynonymDictionaryVo>> getSynonymList(HttpServletRequest request) throws Exception {
        SynonymSearchRequestDto searchDto = (SynonymSearchRequestDto) BindUtil.convertRequestToObject(request, SynonymSearchRequestDto.class);
        return ApiResult.success(synonymDictionaryBiz.getSynonymDictionaryPagingList(searchDto));
    }

    /**
     * 전시관리 > 동의어 사전 업로드
     * @return
     */
    @PostMapping(value = "/admin/dp/synonymDictionary/createReflectionSynonymFile")
    public ApiResult<?> uploadCustomDictionary() {
        return searchDictionaryService.uploadSynonymDictionary();
    }


    /**
     * 전시관리 > 동의어 사전 편집
     *
     */
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = SynonymDictionarySaveResultDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[DUPLICATE_DATA] DUPLICATE_DATA - 중복 데이터가 있습니다. \n" +
                    "[SYNONYM_NEED_USER_DEFINE_WORD] SYNONYM_NEED_USER_DEFINE_WORD - 사용자 정의 사전에 미등록 되어 있는 단어가 발견되어, 자동으로 등록 처리 되었습니다."
            )
    })
    @RequestMapping(value = "/admin/dp/synonymDictionary/saveSynonym")
    public ApiResult<?> saveSynonym(SynonymDictionarySaveRequestDto dto) throws Exception {
        dto.convertDataList();
        return synonymDictionaryBiz.saveSynonymDictionary(dto);
    }


}
