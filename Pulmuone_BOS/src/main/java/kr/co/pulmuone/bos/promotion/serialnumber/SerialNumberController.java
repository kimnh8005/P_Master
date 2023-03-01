package kr.co.pulmuone.bos.promotion.serialnumber;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import kr.co.pulmuone.v1.promotion.serialnumber.dto.GetSerialNumberListRequestDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.GetSerialNumberListResponseDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.PutSerialNumberCancelRequestDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.PutSerialNumberCancelRequestSaveDto;
import kr.co.pulmuone.v1.promotion.serialnumber.service.SerialNumberBiz;
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
 *  1.0    20200828		        	이한미르          최초작성
 * =======================================================================
 * </PRE>
 */

@RestController
@RequiredArgsConstructor
public class SerialNumberController {

	@Autowired
	private SerialNumberBiz serialNumberBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
	private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰


	/**
	 * 이용권 내역 리스트조회
	 */
	@PostMapping(value = "/admin/promotion/serialNumber/getSerialNumberList")
	@ApiOperation(value = "이용권 내역 리스트조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetSerialNumberListResponseDto.class)
	})
	public ApiResult<?> getSerialNumberList(GetSerialNumberListRequestDto dto) throws Exception {
		return serialNumberBiz.getSerialNumberList((GetSerialNumberListRequestDto) BindUtil.convertRequestToObject(request, GetSerialNumberListRequestDto.class));
	}

	/**
	 * 이용권 내역 사용중지
	 */
	@PostMapping(value = "/admin/promotion/serialNumber/putSerialNumberCancel")
	@ApiOperation(value = "이용권 내역 사용중지")
	public ApiResult<?> putSerialNumberCancel(PutSerialNumberCancelRequestDto dto) throws Exception {
		dto.setUpdateRequestDtoList(BindUtil.convertJsonArrayToDtoList(dto.getUpdateData(), PutSerialNumberCancelRequestSaveDto.class));

		return serialNumberBiz.putSerialNumberCancel(dto);
	}

	/**
	 * 이용권 내역 엑셀 선택 다운로드
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/admin/promotion/serialNumber/serialNumberListExportExcel")
	public ModelAndView serialNumberListExportExcel(@RequestBody GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception {

		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, serialNumberBiz.serialNumberListExportExcel(getSerialNumberListRequestDto));

		return modelAndView;
	}

	/**
	 * 이용권 번호 엑셀 다운로드
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/admin/promotion/serialNumber/ticketNumberExportExcel")
	public ModelAndView ticketNumberExportExcel(@RequestBody GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception {

		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, serialNumberBiz.ticketNumberExportExcel(getSerialNumberListRequestDto));

		return modelAndView;
	}


}
