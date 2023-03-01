package kr.co.pulmuone.bos.system.log;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.base.dto.ExcelDownLogRequestDto;
import kr.co.pulmuone.v1.base.dto.MenuOperLogRequestDto;
import kr.co.pulmuone.v1.base.dto.PrivacyMenuOperLogRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.system.log.dto.ExcelDownloadLogResponseDto;
import kr.co.pulmuone.v1.system.log.dto.GetBatchLogListRequestDto;
import kr.co.pulmuone.v1.system.log.dto.GetBatchLogListResponseDto;
import kr.co.pulmuone.v1.system.log.dto.GetConnectLogListRequestDto;
import kr.co.pulmuone.v1.system.log.dto.GetConnectLogListResponseDto;
import kr.co.pulmuone.v1.system.log.dto.MenuOperLogResponseDto;
import kr.co.pulmuone.v1.system.log.dto.PrivacyMenuOperLogResponseDto;
import kr.co.pulmuone.v1.system.log.service.SystemLogBiz;

@RequiredArgsConstructor
@Controller
public class SystemLogController {

	@Autowired(required=true)
	private HttpServletRequest request;

	private final SystemLogBiz systemLogBiz;


	/**
	 * 접속로그 리스트 조회
	 */
	@PostMapping(value = "/admin/st/log/getConnectLogList")
	@ResponseBody
	@ApiOperation(value = "접속로그 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetConnectLogListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getConnectLogList(GetConnectLogListRequestDto dto) throws Exception {
		return systemLogBiz.getConnectLogList((GetConnectLogListRequestDto) BindUtil.convertRequestToObject(request, GetConnectLogListRequestDto.class));
	}

	/**
	 * 배치로그 리스트 조회
	 */
	@PostMapping(value = "/admin/st/log/getBatchLogList")
	@ResponseBody
	@ApiOperation(value = "배치로그 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetBatchLogListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getBatchLogList(GetBatchLogListRequestDto dto)throws Exception{
		return systemLogBiz.getBatchLogList((GetBatchLogListRequestDto) BindUtil.convertRequestToObject(request, GetBatchLogListRequestDto.class));
	}


	@PostMapping(value = "/admin/st/log/getExcelDownloadLogList")
	@ResponseBody
	@ApiOperation(value = "엑셀다운로드 로그 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ExcelDownloadLogResponseDto.class)
	})
	public ApiResult<?> getExcelDownloadLogList(ExcelDownLogRequestDto excelDownLogRequestDto) throws Exception {
		return systemLogBiz.getExcelDownloadLogList((ExcelDownLogRequestDto) BindUtil.convertRequestToObject(request, ExcelDownLogRequestDto.class));
	}


	/**
	 * 메뉴사용이력 리스트 조회
	 */
	@PostMapping(value = "/admin/st/log/getMenuOperLogList")
	@ResponseBody
	@ApiOperation(value = "메뉴사용이력 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = MenuOperLogResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getMenuOperLogList(MenuOperLogRequestDto dto) throws Exception {
		return systemLogBiz.getMenuOperLogList((MenuOperLogRequestDto) BindUtil.convertRequestToObject(request, MenuOperLogRequestDto.class));
	}


	/**
	 * 개인정보 처리 이력 리스트 조회
	 */
	@PostMapping(value = "/admin/st/log/getPrivacyMenuOperLogList")
	@ResponseBody
	@ApiOperation(value = "개인정보 처리 이력 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PrivacyMenuOperLogResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getPrivacyMenuOperLogList(PrivacyMenuOperLogRequestDto dto) throws Exception {
		return systemLogBiz.getPrivacyMenuOperLogList((PrivacyMenuOperLogRequestDto) BindUtil.convertRequestToObject(request, PrivacyMenuOperLogRequestDto.class));
	}

}

