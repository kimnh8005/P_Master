package kr.co.pulmuone.bos.policy.shiparea;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.dailygoods.dto.PolicyDailyGoodsPickDto;
import kr.co.pulmuone.v1.policy.shiparea.dto.GetBackCountryResponseDto;
import kr.co.pulmuone.v1.policy.shiparea.dto.PolicyShipareaDto;
import kr.co.pulmuone.v1.policy.shiparea.service.PolicyShipareaBiz;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * 도서산관 권역설정
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200702		   	박영후            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
@RequiredArgsConstructor
public class PolicyShipAreaController {

	@Autowired
	private PolicyShipareaBiz policyShipareaBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
	private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	/**
	 * 도서산관 목록 조회
	 * @param	PoTypeListRequestDto
	 * @return	GetBackCountryListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/shipArea/getBackCountryList")
	@ApiOperation(value = "도서산관 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = GetBackCountryResponseDto.class)
	})
	public ApiResult<?> getBackCountryList(PolicyShipareaDto dto) throws Exception{
		return policyShipareaBiz.getBackCountryList((PolicyShipareaDto) BindUtil.convertRequestToObject(request, PolicyShipareaDto.class));
	}

	/**
	 * 도서산관 추가
	 * @param	AddBackCountryRequestDto
	 * @return	AddBackCountryResponseDto
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/shipArea/addBackCountry")
	@ApiOperation(value = "도서산관 추가", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> addBackCountry(PolicyShipareaDto dto) {
		return policyShipareaBiz.addBackCountry(dto);
	}

	/**
	 * 도서산관 수정
	 * @param	PoTypeRequestDto
	 * @return	PutBackCountryResponseDto
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/shipArea/putBackCountry")
	@ApiOperation(value = "도서산관 수정", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putBackCountry(PolicyShipareaDto dto) {
		return policyShipareaBiz.putBackCountry(dto);
	}

	/**
	 * 도서산관 삭제
	 * @param	DelBackCountryRequestDto
	 * @return	DelBackCountryResponseDto
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/shipArea/delBackCountry")
	@ApiOperation(value = "도서산관 수정", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> delBackCountry(PolicyShipareaDto dto) {
		return policyShipareaBiz.delBackCountry(dto);
	}

	/**
	 * 도서산관 상세 조회
	 * @param	GetBackCountryRequestDto
	 * @return	GetBackCountryResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/shipArea/getBackCountry")
	@ApiOperation(value = "도서산관 상세 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = GetBackCountryResponseDto.class)
	})
	public ApiResult<?> getBackCountry(PolicyShipareaDto dto) throws Exception {
		return policyShipareaBiz.getBackCountry(dto);
	}

	/**
	 * 엑셀 선택 다운로드
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/admin/policy/shipArea/exportExcel")
	@ApiOperation(value = "엑셀 선택 다운로드", httpMethod = "POST", notes = "엑셀 선택 다운로드")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyDailyGoodsPickDto.class),
	})
    public ModelAndView getBackCountryExcelDownload(@RequestParam(value = "zipCodeCsv", required = true) String zipCodes[], Model model) throws Exception {
		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, policyShipareaBiz.getBackCountryExcelList(zipCodes));
		return modelAndView;
    }
}
