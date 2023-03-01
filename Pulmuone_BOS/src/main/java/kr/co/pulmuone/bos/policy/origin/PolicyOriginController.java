package kr.co.pulmuone.bos.policy.origin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.origin.dto.basic.AddPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.DelPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginListRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginTypeListRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.PutPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.service.PolicyOriginBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * <PRE>
 * Forbiz Korea
 * 원산지 목록
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200630         박영후            최초작성
 *  1.0    20201023         최윤지            NEW 변경
 * =======================================================================
 * </PRE>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PolicyOriginController {

    private final PolicyOriginBiz policyOriginBiz;

    @Autowired
	private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    @ApiOperation(value = "원산지 목록 조회")
    @PostMapping(value = "/admin/policy/origin/getOriginList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetPolicyOriginListRequestDto.class)
        })
    public ApiResult<?> getOriginList(HttpServletRequest request, GetPolicyOriginListRequestDto getPolicyOriginListRequestDto) throws Exception{

        return policyOriginBiz.getOriginList(BindUtil.bindDto(request, GetPolicyOriginListRequestDto.class));

    }

    @ApiOperation(value = "원산지 구분 목록 조회")
    @GetMapping(value = "/admin/policy/origin/getOriginTypeList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetPolicyOriginTypeListRequestDto.class)
        })
    public ApiResult<?> getOriginTypeList(GetPolicyOriginTypeListRequestDto getPolicyOriginTypeListRequestDto){

        return policyOriginBiz.getOriginTypeList(getPolicyOriginTypeListRequestDto);
    }

    @ApiOperation(value = "원산지 등록")
    @PostMapping(value = "/admin/policy/origin/addOrigin")
    @ApiResponses(value = {
           @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
     })
    public ApiResult<?> addOrigin(AddPolicyOriginRequestDto addPolicyOriginRequestDto) throws Exception{

        return policyOriginBiz.addOrigin(addPolicyOriginRequestDto);

    }

    @ApiOperation(value = "원산지 수정")
    @PostMapping(value = "/admin/policy/origin/putOrigin")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = PutPolicyOriginRequestDto.class)
     })
    public ApiResult<?> putOrigin(PutPolicyOriginRequestDto putPolicyOriginRequestDto){

        return policyOriginBiz.putOrigin(putPolicyOriginRequestDto);

    }

    @ApiOperation(value = "원산지 삭제")
    @PostMapping(value = "/admin/policy/origin/delOrigin")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = DelPolicyOriginRequestDto.class)
     })
    public ApiResult<?> delOrigin(DelPolicyOriginRequestDto delPolicyOriginRequestDto){

        return policyOriginBiz.delOrigin(delPolicyOriginRequestDto);
    }

    @ApiOperation(value = "원산지 상세 조회")
    @PostMapping(value = "/admin/policy/origin/getOrigin")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetPolicyOriginRequestDto.class)
        })
    public ApiResult<?> getOrigin(GetPolicyOriginRequestDto getPolicyOriginRequestDto){
        return policyOriginBiz.getOrigin(getPolicyOriginRequestDto);
    }

	@PostMapping(value = "/admin/policy/origin/getOriginListExportExcel")
	@ApiOperation(value = "원산지 목록 조회 엑셀다운로드", httpMethod = "POST", notes = "원산지 목록 조회 엑셀다운로드")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetPolicyOriginListRequestDto.class),
	})
	public ModelAndView getOriginListExportExcel(@RequestBody GetPolicyOriginListRequestDto dto) {
		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, policyOriginBiz.getOriginListExportExcel(dto));
		return modelAndView;
	}

}