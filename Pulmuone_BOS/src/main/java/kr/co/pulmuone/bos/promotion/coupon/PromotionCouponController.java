
package kr.co.pulmuone.bos.promotion.coupon;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentCardBenefitDto;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentPromotionDto;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentPromotionRequestDto;
import kr.co.pulmuone.v1.policy.payment.service.PolicyPaymentBiz;
import kr.co.pulmuone.v1.promotion.coupon.dto.BuyerInfoRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.IssueUserRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.OrganizationPopupListRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

@RestController
public class PromotionCouponController {

	@Autowired
	private PromotionCouponBiz promotionCouponBiz;

	@Autowired
	private PolicyPaymentBiz policyPaymentbiz;

	@Autowired(required = true)
	private HttpServletRequest request;

	@Autowired
	private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	/**
	 * 쿠폰목록 조회
	 *
	 * @param CouponRequestDto
	 * @return CouponResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/pm/cpnMgm/getCpnMgm")
	@ApiResponse(code = 900, message = "response data", response = CouponRequestDto.class)
	public ApiResult<?> getCpnMgm(CouponRequestDto couponRequestDto) throws Exception {
		return promotionCouponBiz
				.getCpnMgm((CouponRequestDto) BindUtil.convertRequestToObject(request, CouponRequestDto.class));
	}

	/**
	 * 쿠폰 상세조회
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/pm/cpnMgm/getCoupon")
	@ApiResponse(code = 900, message = "response data", response = CouponRequestDto.class)
	@ResponseBody
	public ApiResult<?> getCoupon(CouponRequestDto couponRequestDto) throws Exception {

		return promotionCouponBiz.getCoupon(couponRequestDto);
	}

	/**
	 * 조직 조회
	 *
	 * @param GetDictionaryListMasterRequestDto
	 * @return GetDictionaryListMasterResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/pm/cpnMgm/getOrganizationPopupList")
	@ApiResponse(code = 900, message = "response data", response = CouponRequestDto.class)
	public ApiResult<?> getOrganizationPopupList(OrganizationPopupListRequestDto organizationPopupListRequestDto)
			throws Exception {

		return promotionCouponBiz.getOrganizationPopupList((OrganizationPopupListRequestDto) BindUtil
				.convertRequestToObject(request, OrganizationPopupListRequestDto.class));
	}

	/**
	 * 쿠폰 정보 등록
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "회원정보 등록")
	@PostMapping(value = "/admin/pm/cpnMgm/addCoupon")
	public ApiResult<?> addCoupon(CouponRequestDto couponRequestDto) throws Exception {
		return promotionCouponBiz.addCoupon(couponRequestDto);
	}

	/**
	 * 쿠폰 정보 수정
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "회원정보 수정")
	@PostMapping(value = "/admin/pm/cpnMgm/putCoupon")
	public ApiResult<?> putCoupon(CouponRequestDto couponRequestDto) throws Exception {
		return promotionCouponBiz.putCoupon(couponRequestDto);
	}

	/**
	 * 쿠폰 삭제
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/pm/cpnMgm/removeCoupon")
	public ApiResult<?> removeCoupon(CouponRequestDto couponRequestDto) throws Exception {

		return promotionCouponBiz.removeCoupon(couponRequestDto);
	}

	/**
	 * 쿠폰 명 수정
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/pm/cpnMgm/updateCouponName")
	public ApiResult<?> updateCouponName(CouponRequestDto couponRequestDto) throws Exception {

		return promotionCouponBiz.updateCouponName(couponRequestDto);
	}

	/**
	 * 쿠폰 복사
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/pm/cpnMgm/getCopyCoupon")
	public ApiResult<?> getCopyCoupon(CouponRequestDto couponRequestDto) throws Exception {

		return promotionCouponBiz.getCopyCoupon(couponRequestDto);
	}

	/**
	 * 쿠폰지급 내역 조회
	 *
	 * @param GetDictionaryListMasterRequestDto
	 * @return GetDictionaryListMasterResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/pm/cpnMgm/getCpnMgmList")
	@ApiResponse(code = 900, message = "response data", response = CouponRequestDto.class)
	public ApiResult<?> getCpnMgmList(CouponRequestDto couponRequestDto) throws Exception {

		return promotionCouponBiz
				.getCpnMgmList((CouponRequestDto) BindUtil.convertRequestToObject(request, CouponRequestDto.class));
	}

	/**
	 * 쿠폰지급 (선택회원)
	 *
	 * @param GetDictionaryListMasterRequestDto
	 * @return GetDictionaryListMasterResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/pm/cpnMgm/putCouponIssueList")
	public ApiResult<?> putCouponIssueList(CouponRequestDto couponRequestDto) throws Exception {

		return promotionCouponBiz.putCouponIssueList(couponRequestDto);
	}

	/**
	 * 쿠폰지정 조회
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/pm/cpnMgm/getCpnMgmIssueList")
	@ApiResponse(code = 900, message = "response data", response = CouponRequestDto.class)
	public ApiResult<?> getCpnMgmIssueList(BuyerInfoRequestDto buyerInfoRequestDto) throws Exception {
		return promotionCouponBiz.getCpnMgmIssueList(
				(BuyerInfoRequestDto) BindUtil.convertRequestToObject(request, BuyerInfoRequestDto.class));
	}

	/**
	 * 쿠폰지급 (검색회원)
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/pm/cpnMgm/putSearchCouponIssueList")
	public ApiResult<?> putSearchCouponIssueList(IssueUserRequestDto issueUserRequestDto) throws Exception {
//		return promotionCouponBiz.putSearchCouponIssueList((IssueUserRequestDto) BindUtil.convertRequestToObject(request, IssueUserRequestDto.class));
		return promotionCouponBiz.putSearchCouponIssueList(issueUserRequestDto);
	}

	/**
	 * 쿠폰 선택회수
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/pm/cpnMgm/putCancelDepositList")
	public ApiResult<?> putCancelDepositList(CouponRequestDto couponRequestDto) throws Exception {
		return promotionCouponBiz.putCancelDepositList(couponRequestDto);
	}

	/**
	 * 쿠폰 승인요청
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/pm/cpnMgm/putCouponStatus")
	public ApiResult<?> putCouponStatus(CouponRequestDto couponRequestDto) throws Exception {
		return promotionCouponBiz.putCouponStatus(couponRequestDto);
	}

	/**
	 * 쿠폰 지급내역 엑셀 선택 다운로드
	 *
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/admin/pm/cpnMgm/issueListExportExcel")
	public ModelAndView getCouponIssueListExcelDownload(@RequestBody CouponRequestDto couponRequestDto)
			throws Exception {

		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel,
				promotionCouponBiz.getCouponIssueListExcelDownload(couponRequestDto));

		return modelAndView;
	}

	@GetMapping(value = "/admin/pm/cpnMgm/getPaymentList")
	@ApiOperation(value = "제휴구분 PG 조회 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyPaymentPromotionDto.class) })
	public ApiResult<?> getPaymentList(PolicyPaymentCardBenefitDto policyPaymentCardBenefitDto) throws Exception {
		return policyPaymentbiz.getPaymentList(policyPaymentCardBenefitDto);
	}

	@GetMapping(value = "/admin/pm/cpnMgm/getPaymentUseList")
	@ApiOperation(value = "제휴구분 결제수단 조회 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyPaymentPromotionDto.class) })
	public ApiResult<?> getPaymentUseList(PolicyPaymentCardBenefitDto policyPaymentCardBenefitDto) throws Exception {
		return policyPaymentbiz.getPaymentUseList(policyPaymentCardBenefitDto);
	}

	@GetMapping(value = "/admin/pm/cpnMgm/getPayCardList")
	@ApiOperation(value = "제휴구분 결제수단 상세 조회 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyPaymentPromotionDto.class) })
	public ApiResult<?> getPayCardList(PolicyPaymentPromotionRequestDto policyPaymentPromotionRequestDto)
			throws Exception {
		return policyPaymentbiz.getPayCardList(policyPaymentPromotionRequestDto);
	}

	@PostMapping(value = "/admin/pm/cpnMgm/addUserExcelUpload")
	@ApiOperation(value = "계정 엑셀업로드", httpMethod = "POST", notes = "계정 엑셀업로드")
	@ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null") })
	public ApiResult<?> addUserExcelUpload(MultipartHttpServletRequest request) throws Exception {
		MultipartFile file = null;
		Iterator<String> iterator = request.getFileNames();
		if (iterator.hasNext()) {
			file = request.getFile(iterator.next());
		}
		return promotionCouponBiz.addUserExcelUpload(file);
	}

	@PostMapping(value = "/admin/pm/cpnMgm/addUserExcelUploadByAdm")
	@ApiOperation(value = "계정 엑셀업로드(관리자 지급/차감)", httpMethod = "POST", notes = "계정 엑셀업로드(관리자 지급/차감)")
	@ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null") })
	public ApiResult<?> addUserExcelUploadByAdm(MultipartHttpServletRequest request) throws Exception {
		MultipartFile file = null;
		Iterator<String> iterator = request.getFileNames();
		if (iterator.hasNext()) {
			file = request.getFile(iterator.next());
		}
		return promotionCouponBiz.addUserExcelUploadByAdm(file);
	}

	@PostMapping(value = "/admin/pm/cpnMgm/addTicketExcelUpload")
	@ApiOperation(value = "이용권 엑셀업로드", httpMethod = "POST", notes = "이용권 엑셀업로드")
	@ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null") })
	public ApiResult<?> addTicketExcelUpload(MultipartHttpServletRequest request) throws Exception {
		MultipartFile file = null;
		Iterator<String> iterator = request.getFileNames();
		if (iterator.hasNext()) {
			file = request.getFile(iterator.next());
		}
		return promotionCouponBiz.addTicketExcelUpload(file);
	}

	@GetMapping(value = "/admin/comn/getEventCallCouponInfo")
	@ResponseBody
	@ApiOperation(value = "쿠폰명 검색")
	@ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = CouponRequestDto.class) })
	public ApiResult<?> getEventCallCouponInfo(CouponRequestDto dto) {
		return promotionCouponBiz.getEventCallCouponInfo(dto);
	}

	@GetMapping(value = "/admin/comn/getCouponSearchStatus")
	@ResponseBody
	@ApiOperation(value = "쿠폰설정상태 검색")
	@ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = CouponRequestDto.class) })
	public ApiResult<?> getCouponSearchStatus(CouponRequestDto dto) {
		return promotionCouponBiz.getCouponSearchStatus(dto);
	}

	@PostMapping(value = "/admin/pm/cpnMgm/putTicketCollectStatus")
	@ApiOperation(value = "이용권 수금 완료 처리")
	@ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = CouponRequestDto.class) })
	public ApiResult<?> putTicketCollectStatus(CouponRequestDto dto) throws Exception {
		return promotionCouponBiz.putTicketCollectStatus(dto);
	}

	@PostMapping(value = "/admiun/pm/cpnMgm/getOrgInfo")
	@ApiOperation(value = "분담조직 정보 조회")
	@ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = CouponRequestDto.class) })
	public ApiResult<?> getOrgInfo(CouponRequestDto dto) {
		return promotionCouponBiz.getOrgInfo(dto);
	}
}
