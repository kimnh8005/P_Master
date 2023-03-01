package kr.co.pulmuone.bos.base.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.base.dto.ExcelDownLogRequestDto;
import kr.co.pulmuone.v1.base.dto.GetClientPopupRequestDto;
import kr.co.pulmuone.v1.base.dto.GetGrantAuthEmployeePopupRequestDto;
import kr.co.pulmuone.v1.base.dto.GoodsSearchRequestDto;
import kr.co.pulmuone.v1.base.dto.GoodsSearchResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetClientPopupResultVo;
import kr.co.pulmuone.v1.base.dto.vo.GetGrantAuthEmployeePopupResultVo;
import kr.co.pulmuone.v1.base.service.PopupCommonBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponRequestDto;
import kr.co.pulmuone.v1.system.log.dto.ExcelDownloadLogResponseDto;

/**
* <PRE>
* Forbiz Korea
* 공통팝업
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 7. 31.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@RestController
public class PopupCommonController {

	@Autowired
	private PopupCommonBiz popupCommonBiz;

    @Autowired(required=true)
    private HttpServletRequest request;

	@PostMapping(value = "/admin/comn/popup/getClientList")
	@ApiOperation(value = "거래처 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetClientPopupResultVo.class)
	})
	public ApiResult<?> getClientList(GetClientPopupRequestDto getClientPopupRequestDto) {
		return popupCommonBiz.getClientList(getClientPopupRequestDto);
	}

	@PostMapping(value = "/admin/comn/popup/getGrantAuthEmployeeList")
	@ApiOperation(value = "담당자 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetGrantAuthEmployeePopupResultVo.class)
	})
	public ApiResult<?> getGrantAuthEmployeeList(GetGrantAuthEmployeePopupRequestDto getGrantAuthEmployeePopupRequestDto) {
	    return popupCommonBiz.getGrantAuthEmployeeList(getGrantAuthEmployeePopupRequestDto);
	}


    @PostMapping(value = "/admin/comn/popup/getGoodsList")
	@ApiOperation(value = "상품 검색")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GoodsSearchResponseDto.class)
	})
    public ApiResult<?> getGoodsList(GoodsSearchRequestDto goodsSearchRequestDto) throws Exception{
        return popupCommonBiz.getGoodsList((GoodsSearchRequestDto) BindUtil.convertRequestToObject(request, GoodsSearchRequestDto.class));
    }

	@PostMapping(value = "/admin/comn/popup/addExcelDownReason")
	@ResponseBody
	@ApiOperation(value = "엑셀 다운로드 사유 등록")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ExcelDownloadLogResponseDto.class)
	})
	public ApiResult<?> addExcelDownReason(ExcelDownLogRequestDto excelDownLogRequestDto, HttpServletRequest req) throws Exception{
		return popupCommonBiz.addExcelDownReason(excelDownLogRequestDto, req);
	}


	/**
	 * 쿠폰목록  조회 팝업
	 * @param CouponRequestDto
	 * @return CouponResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/comn/popup/getCouponList")
	@ApiOperation(value = "쿠폰목록  조회 팝업")
	@ApiResponse(code = 900, message = "response data", response = CouponRequestDto.class)
	public ApiResult<?> getCouponList(CouponRequestDto couponRequestDto) throws Exception {
		return popupCommonBiz.getCouponList((CouponRequestDto) BindUtil.convertRequestToObject(request, CouponRequestDto.class));
	}

}
