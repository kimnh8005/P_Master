package kr.co.pulmuone.bos.base.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.base.dto.vo.CategoryVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.pulmuone.v1.base.dto.CategoryRequestDto;
import kr.co.pulmuone.v1.base.service.IlCommonBiz;

/**
* <PRE>
* Forbiz Korea
* IL 공통 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 8. 19.                손진구         최초작성
* =======================================================================
* </PRE>
*/
@RestController
public class IlCommonController {

    @Autowired
	private IlCommonBiz ilCommonBiz;

	@GetMapping(value = "/admin/comn/getDropDownCategoryList")
	@ApiOperation(value = "전시 카테고리 조회 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = CategoryVo.class)
	})
	public ApiResult<?> getDropDownCategoryList(CategoryRequestDto categoryRequestDto) {
		return ilCommonBiz.getDropDownCategoryList(categoryRequestDto);
	}

	@GetMapping(value = "/admin/comn/getDropDownCategoryStandardList")
	@ApiOperation(value = "표준 카테고리 조회 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = CategoryVo.class)
	})
	public ApiResult<?> getDropDownCategoryStandardList(CategoryRequestDto categoryRequestDto) {
	    return ilCommonBiz.getDropDownCategoryStandardList(categoryRequestDto);
	}
}
