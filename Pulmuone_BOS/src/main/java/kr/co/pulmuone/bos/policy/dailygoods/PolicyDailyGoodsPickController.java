package kr.co.pulmuone.bos.policy.dailygoods;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.dailygoods.dto.PolicyDailyGoodsPickDto;
import kr.co.pulmuone.v1.policy.dailygoods.service.PolicyDailyGoodsPickBiz;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0		20201014		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
public class PolicyDailyGoodsPickController {


	@Autowired
	private PolicyDailyGoodsPickBiz policyDailyGoodsPickBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
	private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	/**
	 * 일일상품 골라담기 허용여부 목록 조회
	 * @param PolicyDailyGoodsPickDto
	 * @return ApiResult
	 */
	@PostMapping(value = "/admin/policy/dailygoods/getPolicyDailyGoodsPickList")
	@ApiOperation(value = "일일상품 골라담기 허용여부 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = PolicyDailyGoodsPickDto.class)
	})
	public ApiResult<?> getPolicyDailyGoodsPickList(PolicyDailyGoodsPickDto dto) {
		try {
			return policyDailyGoodsPickBiz.getPolicyDailyGoodsPickList((PolicyDailyGoodsPickDto)BindUtil.convertRequestToObject(request, PolicyDailyGoodsPickDto.class));
		} catch (Exception e) {
			return ApiResult.fail();
		}
	}

	/**
	 * 일일상품 골라담기 허용여부 수정
	 * @param PolicyDailyGoodsPickDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/policy/dailygoods/putPolicyDailyGoodsPick")
	@ApiOperation(value = "일일상품 골라담기 허용여부 수정", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putPolicyDailyGoodsPick(@RequestBody PolicyDailyGoodsPickDto dto) {
		return policyDailyGoodsPickBiz.putPolicyDailyGoodsPick(dto);
	}

	/**
	 * 일일상품 골라담기 허용여부 목록 조회 엑셀다운로드
	 *
	 * @param PolicyDailyGoodsPickDto
	 * @return PolicyDailyGoodsPickDto
	 */
	@PostMapping(value = "/admin/policy/dailygoods/getPolicyDailyGoodsPickListExportExcel")
	@ApiOperation(value = "일일상품 골라담기 허용여부 목록 조회 엑셀다운로드", httpMethod = "POST", notes = "일일상품 골라담기 허용여부 목록 조회 엑셀다운로드")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyDailyGoodsPickDto.class),
	})
	public ModelAndView getPolicyDailyGoodsPickListExportExcel(@RequestBody PolicyDailyGoodsPickDto dto) {
		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, policyDailyGoodsPickBiz.getPolicyDailyGoodsPickListExportExcel(dto));
		return modelAndView;
	}

}

