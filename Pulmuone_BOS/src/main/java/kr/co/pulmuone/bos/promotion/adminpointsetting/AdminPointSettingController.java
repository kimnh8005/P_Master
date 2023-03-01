package kr.co.pulmuone.bos.promotion.adminpointsetting;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.AdminPointSettingDetailResponseDto;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.AdminPointSettingRequestDto;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.AdminPointSettingResponseDto;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.RoleGroupListResponseDto;
import kr.co.pulmuone.v1.promotion.adminpointsetting.service.AdminPointSettingBiz;

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
 *  1.0    20201006		       안치열            최초작성
 * =======================================================================
 * </PRE>
 */


@RestController
public class AdminPointSettingController {

	@Autowired
	private AdminPointSettingBiz adminPointSettingBiz;

	@Autowired(required=true)
	private HttpServletRequest request;


	/**
	 * 관리자 적립금 지급 한도 설정 리스트조회
	 */
	@PostMapping(value = "/admin/promotion/adminPointSetting/getAdminPointSetting")
	@ApiOperation(value = "관리자 적립금 사용 설정 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = AdminPointSettingResponseDto.class)
	})
	public ApiResult<?> getAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception {
		return adminPointSettingBiz.getAdminPointSetting((AdminPointSettingRequestDto) BindUtil.convertRequestToObject(request, AdminPointSettingRequestDto.class));
	}


	/**
	 * 역할그룹 조회
	 * @param model
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/promotion/adminPointSetting/getRoleGroupList")
	@ApiOperation(value = "역할그룹 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = RoleGroupListResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> getRoleGroupList(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception {

		return adminPointSettingBiz.getRoleGroupList(adminPointSettingRequestDto);
	}


	/**
	 * 관리자 적립금 지급 한도 설정 이력 리스트조회
	 */
	@PostMapping(value = "/admin/promotion/adminPointSetting/getAdminPointSettingList")
	@ApiOperation(value = "관리자 적립금 사용 설정 이력 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = AdminPointSettingResponseDto.class)
	})
	public ApiResult<?> getAdminPointSettingList(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception {
		return adminPointSettingBiz.getAdminPointSettingList((AdminPointSettingRequestDto) BindUtil.convertRequestToObject(request, AdminPointSettingRequestDto.class));
	}

	/**
	 * 관리자 적립금 지급 한도 설정 상세 조회
	 */
	@PostMapping(value = "/admin/promotion/adminPointSetting/getAdminPointSettingDetail")
	@ApiOperation(value = "관리자 적립금 지급 한도 설정 상세 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = AdminPointSettingDetailResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> getAdminPointSettingDetail(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception {
		return adminPointSettingBiz.getAdminPointSettingDetail(adminPointSettingRequestDto);
	}



	/**
	 * 관리자 적립금 지급 한도 설정 등록
	 * @param AdminPointSettingRequestDto
	 * @return BaseResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/promotion/adminPointSetting/addAdminPointSetting")
	public ApiResult<?> addAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception {
		return adminPointSettingBiz.addAdminPointSetting(adminPointSettingRequestDto);
	}

	/**
	 * 관리자 적립금 지급 한도 설정 수정
	 * @param AdminPointSettingRequestDto
	 * @return BaseResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/promotion/adminPointSetting/putAdminPointSetting")
	public ApiResult<?> putAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception {
		return adminPointSettingBiz.putAdminPointSetting(adminPointSettingRequestDto);
	}


	/**
	 * 관리자 적립금 지급 한도 설정 삭제
	 * @param AdminPointSettingRequestDto
	 * @return BaseResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/promotion/adminPointSetting/removeAdminPointSetting")
	public ApiResult<?> removeAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception {
		return adminPointSettingBiz.removeAdminPointSetting(adminPointSettingRequestDto);
	}
}
