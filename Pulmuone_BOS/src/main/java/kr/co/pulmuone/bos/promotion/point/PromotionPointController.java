package kr.co.pulmuone.bos.promotion.point;

import javax.servlet.http.HttpServletRequest;

import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.promotion.point.dto.PointRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointResponseDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointSettingListRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointSettingListResponseDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointSettingMgmRequestDto;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import kr.co.pulmuone.v1.promotion.point.service.PromotionPointBiz;
import org.springframework.web.servlet.ModelAndView;

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
 *  1.0    20200917		       안치열            최초작성
 * =======================================================================
 * </PRE>
 */


@RestController
public class PromotionPointController {

	@Autowired
	private PromotionPointBiz promotionPointBiz;

	@Autowired
	private PointBiz pointBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
	private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰


	/**
	 * 적립금 설정 리스트조회
	 */
	@PostMapping(value = "/admin/promotion/point/getPointSettingList")
	@ApiOperation(value = "적립금 설정 리스트조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PointSettingListResponseDto.class)
	})
	public ApiResult<?> getPointSettingList(PointSettingListRequestDto pointSettingListRequestDto) throws Exception {
		return promotionPointBiz.getPointSettingList((PointSettingListRequestDto) BindUtil.convertRequestToObject(request, PointSettingListRequestDto.class));
	}


	/**
	 * 적립금 설정 상세 조회
	 */
	@PostMapping(value = "/admin/promotion/point/getPointDetail")
	@ApiOperation(value = "적립금 설정 상세조회")
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PointResponseDto.class)
	})
	public ApiResult<?> getPointDetail(PointRequestDto pointRequestDto) throws Exception {
		return promotionPointBiz.getPointDetail(pointRequestDto);
	}



	/**
	 * 적립금 설정 등록
	 * @param PointSettingMgmRequestDto
	 * @return BaseResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/promotion/point/addPointSetting")
	@ApiOperation(value = "적립금 설정 등록", httpMethod = "POST")
	public ApiResult<?> addPointSetting(@RequestBody PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception {
		return promotionPointBiz.addPointSetting(pointSettingMgmRequestDto);
	}


	/**
	 * 적립금 설정 수정
	 * @param PointSettingMgmRequestDto
	 * @return BaseResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/promotion/point/putPointSetting")
	@ApiOperation(value = "적립금 설정 수정", httpMethod = "POST")
	public ApiResult<?> putPointSetting(@RequestBody PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception {
		return promotionPointBiz.putPointSetting(pointSettingMgmRequestDto);
	}


	/**
	 * 적립금 승인상태 변경
	 * @param PointSettingMgmRequestDto
	 * @return BaseResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/promotion/point/putPointStatus")
	@ApiOperation(value = "적립금 승인상태 변경")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PointResponseDto.class),
			@ApiResponse(code = 901, message = "" +
		                "USER_POINT_LACK - 차감할 적립금이 부족합니다. \n" +
		                "GROUP_POINT_LACK - 명 차감할 적립금이 부족합니다. \n" +
		                "APPROVAL_POINT_LACK - 역할그룹 적립금이 부족하여 승인을 진행할 수 없습니다."
		        )
	})
	public ApiResult<?> putPointStatus(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception {
		return promotionPointBiz.putPointStatus(pointSettingMgmRequestDto);
	}


	/**
	 * 적립금 명 수정
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/pm/point/updatePointName")
	public ApiResult<?> updatePointName(@RequestBody PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception {

		return promotionPointBiz.updatePointName(pointSettingMgmRequestDto);
	}

	/**
	 * 적립금 관리자지급/차감 발급사유 수정
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/pm/point/updatePointIssueReason")
	public ApiResult<?> updatePointIssueReason(@RequestBody PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception {

		return promotionPointBiz.updatePointIssueReason(pointSettingMgmRequestDto);
	}

	/**
	 * 적립금 설정 삭제
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/promotion/point/removePoint")
	@ApiOperation(value = "적립금 설정 삭제", httpMethod = "POST")
	public ApiResult<?> removePoint(PointRequestDto pointRequestDto) throws Exception {

		return promotionPointBiz.removePoint(pointRequestDto);
	}

	/**
	 * -----------------------------------------------------------------------------------------------
	 * start 적립금 적립/소진/환불/소멸 테스트 용 (임시)

	@PostMapping(value = "/test/point/depositOrgaTransferPoints")
	@ApiOperation(value = "이전 올가 회원 적립금 적립", httpMethod = "POST")
	public ApiResult<?> depositOrgaTransferPoints(Long urUserId, Long amount, String previusOrgaUserId) throws Exception {
		return pointBiz.depositOrgaTransferPoints(urUserId, amount, previusOrgaUserId);
	}

	@PostMapping(value = "/test/point/depositPreviousPulmuoneMemberPoints")
	@ApiOperation(value = "이전 풀무원 회원 적립금 적립", httpMethod = "POST")
	public ApiResult<?> depositPreviousPulmuoneMemberPoints(Long urUserId, Long amount, String previusPmoUserId) throws Exception {
		return pointBiz.depositPreviousPulmuoneMemberPoints(urUserId, amount, previusPmoUserId);
	}

	@PostMapping(value = "/test/point/goodsFeedbackPointReward")
	@ApiOperation(value = "상품 후기 적립금 적립", httpMethod = "POST")
	public ApiResult<?> goodsFeedbackPointReward(Long urUserId, Long urGroupId, FeedbackEnums.FeedbackType feedbackType) throws Exception {
		return pointBiz.goodsFeedbackPointReward(urUserId, urGroupId, feedbackType);
	}

	@PostMapping(value = "/test/point/depositReservationGoodsFeedbackPoint")
	@ApiOperation(value = "적립 기준일로 예약된 후기 적립금 적립", httpMethod = "POST")
	public ApiResult<Boolean> goodsFeedbackPointReward(String depositDate) throws Exception {
		return pointBiz.depositReservationGoodsFeedbackPoint(depositDate);
	}

	@PostMapping(value = "/test/point/depositEventPoint")
	@ApiOperation(value = "이벤트 적립금 적립", httpMethod = "POST")
	public ApiResult<?> depositEventPoint(Long urUserId, Long pmPointId, Long evEventId, String refNo2) throws Exception {
		return pointBiz.depositEventPoint(urUserId, pmPointId, evEventId, refNo2);
	}

	@PostMapping(value = "/test/point/depositRecommendationPoint")
	@ApiOperation(value = "추천인 적립금 적립", httpMethod = "POST")
	public ApiResult<?> depositRecommendationPoint(Long urUserId, Long pmPointId, Long recommenderId, String refNo2) throws Exception {
		return pointBiz.depositRecommendationPoint(urUserId, pmPointId, recommenderId, refNo2);
	}

	@PostMapping(value = "/test/point/redeem")
	@ApiOperation(value = "적립금 소진", httpMethod = "POST")
	@ApiResponse(code = 901, message = "" +
			"[INVALID_POINT_AMOUNT] INVALID_POINT_AMOUNT - 유효하지 않은 금액이 입력되었습니다. \n" +
			"[USER_POINT_LACK] USER_POINT_LACK - 적립금 잔액이 부족합니다."
	)
	public ApiResult<Boolean> redeemPoint(DepositPointDto depositPointDto) throws Exception {
		return pointBiz.redeemPoint(depositPointDto);
	}

	@PostMapping(value = "/test/point/refund")
	@ApiOperation(value = "적립금 환불", httpMethod = "POST")
	@ApiResponse(code = 901, message = "" +
			"[EXCEEDS_REFUNDABLE_POINT] EXCEEDS_REFUNDABLE_POINT - 환불 가능한 적립금 금액을 초과하였습니다. \n" +
			"[INVALID_POINT_AMOUNT] INVALID_POINT_AMOUNT - 유효하지 않은 금액이 입력되었습니다. \n" +
			"[INVALID_REFUND_REQUEST_ORDER_NO] INVALID_REFUND_REQUEST_ORDER_NO - 환불 가능한 주문 정보가 아닙니다. \n" +
			"[INVALID_REFUND_REQUEST_PARAM] INVALID_REFUND_REQUEST_PARAM - 유효하지 않은 적립금 환불 요청 파라미터 입니다. \n"
	)
	public ApiResult<?> refundPoint(PointRefundRequestDto pointRefundRequestDto) throws Exception {
		return pointBiz.refundPoint(pointRefundRequestDto);
	}

	@PostMapping(value = "/test/point/collect")
	@ApiOperation(value = "적립금 적립", httpMethod = "POST")
	public ApiResult<?> collectPoint(DepositPointDto depositPointDto) throws Exception {
		return pointBiz.depositPoints(depositPointDto);
	}

	@PostMapping(value = "/test/point/expire")
	@ApiOperation(value = "적립금 소멸", httpMethod = "POST")
	@ApiResponse(code = 901, message = "" +
			"[INVALID_POINT_AMOUNT] INVALID_POINT_AMOUNT - 유효하지 않은 금액이 입력되었습니다. \n" +
			"[USER_POINT_LACK] USER_POINT_LACK - 적립금 잔액이 부족합니다."
	)
	public ApiResult<Boolean> expirePoint(String expiredDate) throws Exception {
		return pointBiz.expirePoint(expiredDate);
	}

	@PostMapping(value = "/test/point/deposit/serialnumber")
	@ApiOperation(value = "시리얼코드로 적립금 적립", httpMethod = "POST")
	@ApiResponse(code = 901, message = "" +
			"[INVALID_POINT_SERIAL_NUMBER] INVALID_POINT_SERIAL_NUMBER - 유효하지 않은 시리얼 번호 입니다.  \n" +
			"[MAXIMUM_DEPOSIT_POINT_EXCEEDED] MAXIMUM_DEPOSIT_POINT_EXCEEDED - 적립 가능 적립금 초과. \n" +
			"[ERROR_REDEEM_SERIAL_NUMBER] ERROR_REDEEM_SERIAL_NUMBER - 시리얼 번호 사용처리 중 에러가 발생했습니다."
	)
	public ApiResult<?> depositPointsBySerialNumber(@ApiParam("회원번호") @RequestParam Long urUserId, @ApiParam("시리얼번호") @RequestParam String pointSerialNumber) throws Exception {
		return pointBiz.depositPointsBySerialNumber(urUserId, pointSerialNumber);
	}

	@PostMapping(value = "/test/point/expireImmediateRefundPoint")
	@ApiOperation(value = "환불 적립금 만료로 즉시 소멸", httpMethod = "POST")
	@ApiResponse(code = 901, message = "" +
			"[NEED_LOGIN] NEED_LOGIN - 로그인이 필요합니다.  \n" +
			"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요.  \n" +
			"[INVALID_EXPIRE_IMMEDIATE_REFUND_REASON] INVALID_EXPIRE_IMMEDIATE_REFUND_REASON - 유효하지 않은 즉시 소멸 가능한 환불 적립금 귀책사유 파라미터 입니다.  \n" +
			"[INVALID_EXPIRE_IMMEDIATE_REFUND_REQUEST_ORDER] INVALID_EXPIRE_IMMEDIATE_REFUND_REQUEST_ORDER - 유효하지 않은 즉시 소멸 가능한 환불 적립금에 주문 정보입니다."
	)
	public ApiResult<?> expireImmediateRefundPoint(PointRefundRequestDto pointRefundRequestDto) {
		return pointBiz.expireImmediateRefundPoint(pointRefundRequestDto);
	}

	@PostMapping(value = "/test/point/depositCsRefundOrderPoint")
	@ApiOperation(value = "CS 환불 적립금 적립", httpMethod = "POST")
	@ApiResponse(code = 901, message = "" +
			"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요."
	)
	public ApiResult<?> depositCsRefundOrderPoint(Long urUserId, String orderNo, Long amount, Boolean isCsRoleManager, String finOrganizationCode, Integer csPointValidityDay) throws Exception {
		return pointBiz.depositCsRefundOrderPoint(urUserId, orderNo, amount, isCsRoleManager, finOrganizationCode, csPointValidityDay);
	}


	 * End 적립금 적립/소진/환불/소멸 테스트 용 (임시)
	 * -----------------------------------------------------------------------------------------------
	 */


	@GetMapping(value = "/admin/comn/getEventCallPointInfo")
	@ResponseBody
	@ApiOperation(value = "적립금 정보 검색")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PointSettingListResponseDto.class)
	})
	public ApiResult<?> getEventCallPointInfo(PointRequestDto pointRequestDto) {
		return promotionPointBiz.getEventCallPointInfo(pointRequestDto);
	}


	@GetMapping(value = "/admin/comn/getPointSearchStatus")
	@ResponseBody
	@ApiOperation(value = "적립금 정보 검색")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PointSettingListResponseDto.class)
	})
	public ApiResult<?> getPointSearchStatus(PointRequestDto pointRequestDto) {
		return promotionPointBiz.getPointSearchStatus(pointRequestDto);
	}


	@PostMapping(value = "/admin/pm/point/putTicketCollectStatus")
	@ApiOperation(value = "이용권 수금 완료 처리")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PointRequestDto.class)
	})
	public ApiResult<?> putTicketCollectStatus(PointRequestDto dto) throws Exception {
		return promotionPointBiz.putTicketCollectStatus(dto);
	}


	/**
	 * 관리자 지급 (지급한도 정보)
	 */
	@PostMapping(value = "/admin/promotion/point/getAdminAmountCheck")
	@ApiOperation(value = "관리자 지급 (지급한도 정보)")
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PointResponseDto.class)
	})
	public ApiResult<?> getAdminAmountCheck(PointRequestDto pointRequestDto) throws Exception {
		return promotionPointBiz.getAdminAmountCheck(pointRequestDto);
	}

	/**
	 * 적립금 지급 목록 엑셀 다운로드 ( 승인요청 상태에서만 가능 )
	 */
	@PostMapping(value = "/admin/promotion/point/getPointPayListExportExcel")
	@ApiOperation(value = "엑셀 대량 지급 목록 엑셀 다운로드 ( 승인요청 상태만 )")
	public ModelAndView getPointPayListExportExcel(@RequestBody PointRequestDto pointRequestDto) throws Exception {

		ExcelDownloadDto excelDownloadDto = promotionPointBiz.getPointPayListExportExcel(pointRequestDto);

		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

		return modelAndView;
	}

}


