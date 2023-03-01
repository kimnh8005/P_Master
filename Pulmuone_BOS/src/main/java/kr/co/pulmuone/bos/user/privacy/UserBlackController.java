package kr.co.pulmuone.bos.user.privacy;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.bos.user.privacy.service.UserBlackService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBlackListBiz;
import kr.co.pulmuone.v1.user.dormancy.dto.AddUserBlackRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserBlackHistoryListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserBlackListRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;


/**
 * <PRE>
 * Forbiz Korea
 * 회원관리 - 블랙리스트 회원 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200625		   	박영후            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class UserBlackController {

	@Autowired
	private UserBuyerBlackListBiz userBuyerBlackListBiz;

	@Autowired
	private UserBlackService userBlackService;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
	private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	/**
	 * 회원 블랙리스트 리스트조회
	 * @param getUserBlackListRequestDto
	 * @return GetUserDormantListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/userBlack/getBlackListUserList")
	@ApiOperation(value = "휴면회원 리스트조회", httpMethod = "POST", notes = "휴면회원 리스트조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApiResult.class),
			@ApiResponse(code = 901, message = "" +
					"0000 - 성공 \n" +
					"9999 - 관리자에게 문의하세요."
			)
	})
	public ApiResult<?> getUserDormantList(GetUserBlackListRequestDto getUserBlackListRequestDto) throws Exception {

		return ApiResult.success(userBlackService.getBlackListUserList((GetUserBlackListRequestDto) BindUtil.convertRequestToObject(request, GetUserBlackListRequestDto.class)));
	}


	/**
	 * 회원 블랙리스트 히스토리 조회
	 * @param getUserBlackHistoryListRequestDto
	 * @return GetUserBlackHistoryListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/userBlack/getUserBlackHistoryList")
	@ApiOperation(value = "휴면회원 리스트조회", httpMethod = "POST", notes = "휴면회원 리스트조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApiResult.class),
			@ApiResponse(code = 901, message = "" +
					"0000 - 성공 \n" +
					"9999 - 관리자에게 문의하세요."
			)
	})
	public ApiResult<?> getUserBlackHistoryList(GetUserBlackHistoryListRequestDto getUserBlackHistoryListRequestDto) throws Exception {

		return ApiResult.success(userBlackService.getUserBlackHistoryList((GetUserBlackHistoryListRequestDto) BindUtil.convertRequestToObject(request, GetUserBlackHistoryListRequestDto.class)));
	}


	/**
	 * 회원 블랙 리스트 등록
	 * @param addUserBlackRequestDto
	 * @return	AddUserBlackResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/userBlack/addUserBlack")
	@ApiOperation(value = "휴면회원 리스트조회", httpMethod = "POST", notes = "휴면회원 리스트조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApiResult.class),
			@ApiResponse(code = 901, message = "" +
					"0000 - 성공 \n" +
					"9999 - 관리자에게 문의하세요."
			)
	})
	public ApiResult<?> addUserBlack(AddUserBlackRequestDto addUserBlackRequestDto) throws Exception {

		return userBuyerBlackListBiz.addUserBlack(addUserBlackRequestDto);
	}

	/**
	 * 회원 블랙리스트 리스트조회 엑셀 다운로드
	 * @param getUserBlackListRequestDto
	 * @return
	 */
	@PostMapping(value = "/admin/ur/userBlack/getBlackListUserListExportExcel")
	@ApiOperation(value = "회원 블랙리스트 리스트조회 엑셀 다운로드")
	public ModelAndView getBlackListUserListExportExcel(@RequestBody GetUserBlackListRequestDto getUserBlackListRequestDto) throws Exception {

		ExcelDownloadDto excelDownloadDto = userBuyerBlackListBiz.getBlackListUserListExportExcel(getUserBlackListRequestDto);

		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

		return modelAndView;

	}

	/**
	 * 회원 블랙리스트 엑셀업로드
	 * @param fileRequest
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/userBlack/addBlackListUserExcelUpload")
	@ApiOperation(value = "클레임 엑셀업로드", httpMethod = "POST", notes = "회원 블랙리스트 엑셀업로드")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : null")
	})
	public ApiResult<?> addBlackListUserExcelUpload(MultipartHttpServletRequest fileRequest) throws Exception {
		MultipartFile file = null;
		Iterator<String> iterator = fileRequest.getFileNames();
		if (iterator.hasNext()) {
			file = fileRequest.getFile(iterator.next());
		}
		return userBuyerBlackListBiz.addBlackListUserExcelUpload(file);
	}
}


