package kr.co.pulmuone.bos.display.qrcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import kr.co.pulmuone.bos.display.qrcode.service.QrcodeBosService;
import kr.co.pulmuone.v1.comm.base.ApiResult;

import kr.co.pulmuone.v1.display.qrcode.dto.AddQrcodeRequestDto;
import kr.co.pulmuone.v1.display.qrcode.dto.AddQrcodeResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 전시관리>QR코드 관리 BOS Controller
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020.11.18     최윤지          최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@RestController
@RequiredArgsConstructor
public class QrcodeController {

	@Autowired
	private QrcodeBosService qrcodeBosService;

	@ApiOperation(value = "QR코드 생성")
	@PostMapping(value = "/admin/display/qrcode/addQrcode")
	@ApiResponses(value = {
	        @ApiResponse(code = 900, message = "response data", response = AddQrcodeResponseDto.class)
	    })
	public ApiResult<?> addQrcode(AddQrcodeRequestDto addQrcodeRequestDto) throws Exception {

		return ApiResult.success(qrcodeBosService.addQrcode(addQrcodeRequestDto));
	}




}