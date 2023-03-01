package kr.co.pulmuone.bos.system.emailtmplt;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.system.emailtmplt.service.EmailTmpltBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 이메일 템플릿 구현
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20201015   				ykk     최초작성
 *  1.1	   20210104				  최윤지	추가작성
 * =======================================================================
 * </PRE>
 */

@RestController
@RequiredArgsConstructor
public class EmailTmpltController {

    private final EmailTmpltBiz emailTmpltBiz;

    @Autowired
    private HttpServletResponse httpServletResponse;


	/**
	 * @Desc euc-kr 응답
	 * @param contents
	 * @return void
	 */
	private void responseCharacterSetEuckr(String contents) throws Exception {
		httpServletResponse.setContentType("text/html;charset=EUC-KR");
		PrintWriter writer = httpServletResponse.getWriter();

		writer.println(contents);
		writer.flush();
		writer.close();
	}

	/*
	 * 이메일 발송 위한 호출용
	 * 이메일 템플릿 html return 필요
	 */
	@ApiOperation(value = "비밀번호 찾기 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/getPassWordEmailTmpltMV")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "아이디", required = true, dataType = "string")
						,@ApiImplicitParam(name = "userPassword", value = "임시비밀번호", required = true, dataType = "string")
	})
	public ModelAndView getPassWordEmailTmpltMV(@RequestParam(value = "userId", required = true) String userId
            , @RequestParam(value = "userPassword", required = true) String userPassword) throws Exception{
		return emailTmpltBiz.getFindPassWordEmailTmpltMV(userId, userPassword);

	}

	/*
	 * BOS계정 비밀번호 재설정 시 이메일 발송 위한 호출용
	 * 이메일 템플릿 html return 필요
	 */
	@ApiOperation(value = "BOS 계정 비밀번호 재설정 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/getBosPassWordEmailTmplt")
	public void getBosPassWordEmailTmplt(@RequestParam(value = "urUserId", required = true) String urUserId
            , @RequestParam(value = "userPassword", required = true) String userPassword, HttpServletRequest request) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.getFindBosPassWordEmailTmplt(urUserId, userPassword));
	}

	/*
	 * 구매자 비밀번호 재설정 시 이메일 발송 위한 호출용
	 * 이메일 템플릿 html return 필요
	 */
	@ApiOperation(value = "구매자 비밀번호 재설정 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/getBuyerPassWordEmailTmplt")
	public void getBuyerPassWordEmailTmplt(@RequestParam(value = "urUserId", required = true) String urUserId
            , @RequestParam(value = "userPassword", required = true) String userPassword, HttpServletRequest request) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.getFindBuyerPassWordEmailTmplt(urUserId, userPassword));
	}

	/**
	 * 회원가입 시 이메일 발송 위한 호출용
	 * 이메일 템플릿 html return 필요
	 */
	@ApiOperation(value = "회원가입 완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/getSignUpCompletedEmailTmplt")
	public void getSignUpCompletedEmailTmplt(@RequestParam(value = "urUserId", required = true) String urUserId, HttpServletRequest request) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.getSignUpCompletedEmailTmplt(urUserId));
	}

	/**
	 * 관리자 회원가입 시 이메일 발송 위한 호출용
	 * 이메일 템플릿 html return 필요
	 */
	@ApiOperation(value = "관리자 가입 완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/getBosSignUpCompletedEmailTmplt")
	public void getBosSignUpCompletedEmailTmplt(@RequestParam(value = "employeeNumber", required = true) String employeeNumber, HttpServletRequest request) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.getBosSignUpCompletedEmailTmplt(employeeNumber));
	}


	/**
	 * @Desc 회원탈퇴 시 이메일 발송 위한 호출용
	 * @param urUserDropId
	 * @param request
	 * @return String
	 */
	@ApiOperation(value = "회원탈퇴 완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/getUserDropInfoEmailTmplt")
	public void getUserDropInfoEmailTmplt(@RequestParam(value = "urUserDropId", required = true) Long urUserDropId,  HttpServletRequest request) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.getUserDropInfoEmailTmplt(urUserDropId));
	}

	/**
	 * @Desc 정지회원 전환 시 이메일 발송 위한 호출용
	 * @param urUserId
	 * @return String
	 */
	@ApiOperation(value = "정지회원 전환 이메일 ( 일반 -> 정지)")
	@GetMapping(value = "/admin/system/emailtmplt/getBuyerStopInfoEmailTmplt")
	public void getBuyerStopInfoEmailTmplt(@RequestParam(value = "urUserId", required = true) String urUserId) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.getBuyerStopInfoEmailTmplt(urUserId));
	}

	/**
	 * @Desc 정상회원 전환 시 이메일 발송 위한 호출용
	 * @param urUserId
	 * @return String
	 */
	@ApiOperation(value = "정상회원 전환 이메일 ( 정지 -> 회원)")
	@GetMapping(value = "/admin/system/emailtmplt/getBuyerNormalInfoEmailTmplt")
	public void getBuyerNormalInfoEmailTmplt(@RequestParam(value = "urUserId", required = true) String urUserId) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.getBuyerNormalInfoEmailTmplt(urUserId));
	}

	/**
	 * @Desc 임직원회원 인증 시 이메일 발송 위한 호출용
	 * @param tempCertiNo
	 * @return String
	 */
	@ApiOperation(value = "임직원회원 인증 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/getEmployeeCertificationEmailTmplt")
	public void getEmployeeCertificationEmailTmplt(@RequestParam(value = "tempCertiNo", required = true) String tempCertiNo) throws Exception {
		responseCharacterSetEuckr(emailTmpltBiz.getEmployeeCertificationEmailTmplt(tempCertiNo));
	}

	/**
	 * @Desc 1:1문의 접수완료 이메일 발송 위한 호출용
	 * @param urUserId
	 * @return String
	 */
	@ApiOperation(value = "1:1문의 접수완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/getOnetooneQnaAddEmailTmplt")
	public void getOnetooneQnaAddEmailTmplt(@RequestParam(value = "urUserId", required = true) String urUserId) throws Exception {
		responseCharacterSetEuckr(emailTmpltBiz.getOnetooneQnaAddEmailTmplt(urUserId));
	}

	/**
	 * @Desc 1:1문의 답변완료 이메일 발송 위한 호출용
	 * @param csQnaId
	 * @return String
	 */
	@ApiOperation(value = "1:1문의 답변완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/getOnetooneQnaAnswerEmailTmplt")
	public void getOnetooneQnaAnswerEmailTmplt(@RequestParam(value = "csQnaId", required = true) String csQnaId) throws Exception {
		responseCharacterSetEuckr(emailTmpltBiz.getOnetooneQnaAnswerEmailTmplt(csQnaId));
	}

	/**
	 * @Desc 1:1문의 답변완료 이메일 발송 위한 호출용
	 * @param csQnaId
	 * @return String
	 */
	@ApiOperation(value ="상품문의 답변완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/getProductQnaAnswerEmailTmplt")
	public void getProductQnaAnswerEmailTmplt(@RequestParam(value = "csQnaId", required = true) String csQnaId) throws Exception {
		responseCharacterSetEuckr(emailTmpltBiz.getProductQnaAnswerEmailTmplt(csQnaId));
	}

	/**
	 * @Desc 휴면계정 전환 완료시 이메일 발송 위한 호출용
	 * @param urUserId
	 * @return String
	 */
	@ApiOperation(value = "휴면계정 전환 완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/getUserDormantCompletedEmailTmplt")
	public void getUserDormantCompletedEmailTmplt(@RequestParam(value = "urUserId", required = true) Long urUserId) throws Exception {
		responseCharacterSetEuckr(emailTmpltBiz.getUserDormantCompletedEmailTmplt(urUserId));
	}

	/**
	 * @Desc 휴면계정 예정 안내 이메일 발송 위한 호출용
	 * @param urUserId
	 * @return String
	 */
	@ApiOperation(value = "휴면계정 예정 안내 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/getUserDormantExpected")
	public void getUserDormantExpected(@RequestParam(value = "urUserId", required = true) Long urUserId) throws Exception {
		responseCharacterSetEuckr(emailTmpltBiz.getUserDormantExpected(urUserId));
	}

	/**
	 * @Desc 마케팅 정보 수신동의 안내 이메일 발송 위한 호출용
	 * @param urUserId
	 * @return String
	 */
	@ApiOperation(value = "마케팅 정보 수신동의 안내 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/getMarketingInfo")
	public void getMarketingInfo(@RequestParam(value = "urUserId", required = true) Long urUserId) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.getMarketingInfo(urUserId));
	}

	/**
	 * @Desc 적립금 소멸 예정 안내 이메일 발송 위한 호출용
	 * @param urUserId
	 * @return String
	 */
	@ApiOperation(value = "적립금 소멸 예정 안내 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/getPointExpectExpired")
	public void getPointExpectExpired(@RequestParam(value = "urUserId", required = true) Long urUserId) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.getPointExpectExpired(urUserId));
	}

	/**
	 * 직접배송 미등록 송장 알림
	 * @throws Exception
	 */
	@ApiOperation(value = "직접배송 미등록 송장 알림")
	@GetMapping(value = "/admin/system/emailtmplt/getDirectShippingUnregisteredInvoice")
	public void getPointExpectExpired() throws Exception {
		responseCharacterSetEuckr(emailTmpltBiz.getDirectShippingUnregisteredInvoice());
	}

	/**
	 * @Desc 주문 접수 완료 이메일 발송 위한 호출용
	 * @param odOrderId
	 * @return String
	 */
	@ApiOperation(value = "주문 접수 완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/orderReceivedComplete")
	public void orderReceivedComplete(@RequestParam(value = "odOrderId", required = true) Long odOrderId) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.orderReceivedComplete(odOrderId));
	}

	/**
	 * @Desc 주문 결제 완료 이메일 발송 위한 호출용
	 * @param odOrderId
	 * @return String
	 */
	@ApiOperation(value = "주문 결제 완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/orderPaymentComplete")
	public void orderPaymentComplete(@RequestParam(value = "odOrderId", required = true) Long odOrderId) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.orderPaymentComplete(odOrderId));
	}

	/**
	 * @Desc 주문 입금 완료 이메일 발송 위한 호출용
	 * @param odOrderId
	 * @return String
	 */
	@ApiOperation(value = "주문 입금 완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/orderDepositComplete")
	public void orderDepositComplete(@RequestParam(value = "odOrderId", required = true) Long odOrderId) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.orderDepositComplete(odOrderId));
	}

	/**
	 * @Desc 정기 주문 결제 완료 이메일 발송 위한 호출용
	 * @param odRegularResultId
	 * @return String
	 */
	@ApiOperation(value = "정기 주문 결제 완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/orderRegularPaymentComplete")
	public void orderRegularPaymentComplete(@RequestParam(value = "odRegularResultId", required = true) Long odRegularResultId) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.orderRegularPaymentComplete(odRegularResultId));
	}

	/**
	 * @Desc 상품발송 이메일 발송 위한 호출용
	 * @param orderDetailGoodsList
	 * @return String
	 */
	@ApiOperation(value = "상품발송 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/orderGoodsDelivery")
	public void orderGoodsDelivery(@RequestParam(value = "orderDetailGoodsList", required = true) List<Long> orderDetailGoodsList) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.orderGoodsDelivery(orderDetailGoodsList));
	}

	/**
	 * @Desc 주문취소완료 이메일 발송 위한 호출용
	 * @param odClaimId
	 * @return String
	 */
	@ApiOperation(value = "주문취소완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/orderCancelComplete")
	public void orderCancelComplete(@RequestParam(value = "odClaimId", required = true) Long odClaimId,
									@RequestParam(value = "odOrderDetlIdList", required = true) List<Long> odOrderDetlIdList) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.orderCancelComplete(odClaimId,odOrderDetlIdList));
	}

	/**
	 * @Desc 주문반품완료 이메일 발송 위한 호출용
	 * @param odClaimId
	 * @return String
	 */
	@ApiOperation(value = "주문반품완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/orderReturnComplete")
	public void orderReturnComplete(@RequestParam(value = "odClaimId", required = true) Long odClaimId,
									@RequestParam(value = "odOrderDetlIdList", required = true) List<Long> odOrderDetlIdList) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.orderReturnComplete(odClaimId, odOrderDetlIdList));
	}

	/**
	 * @Desc 정기배송 신청완료 이메일 발송 위한 호출용
	 * @param odRegularReqId,firstOrderYn
	 * @return String
	 */
	@ApiOperation(value = "정기배송 신청완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/orderRegularApplyCompleted")
	public void orderRegularApplyCompleted(@RequestParam(value = "odRegularReqId", required = true) Long odRegularReqId
											,@RequestParam(value = "firstOrderYn", required = true) String firstOrderYn) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.orderRegularApplyCompleted(odRegularReqId, firstOrderYn));
	}

	/**
	 * @Desc 정기배송 결제 실패(2차) 이메일 발송 위한 호출용
	 * @param odRegularResultId
	 * @return String
	 */
	@ApiOperation(value = "정기배송 결제 실패(2차) 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/orderRegualrPaymentFailSecond")
	public void orderRegualrPaymentFailSecond(@RequestParam(value = "odRegularResultId", required = true) Long odRegularResultId) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.orderRegualrPaymentFailSecond(odRegularResultId));
	}

	/**
	 * @Desc 정기배송 취소 완료 이메일 발송 위한 호출용
	 * @param odRegularResultDetlId
	 * @return String
	 */
	@ApiOperation(value = "정기배송 취소 완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/orderRegularCancelCompleted")
	public void orderRegularCancelCompleted(@RequestParam(value = "odRegularResultDetlId", required = true) Long odRegularResultDetlId) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.orderRegularCancelCompleted(odRegularResultDetlId));
	}

	/**
	 * @Desc 정기배송 상품 건너뛰기 완료 이메일 발송 위한 호출용
	 * @param odRegularResultDetlId
	 * @return String
	 */
	@ApiOperation(value = "정기배송 상품 건너뛰기 완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/orderRegularGoodsSkipCompleted")
	public void orderRegularGoodsSkipCompleted(@RequestParam(value = "odRegularResultDetlId", required = true) Long odRegularResultDetlId) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.orderRegularGoodsSkipCompleted(odRegularResultDetlId));
	}

	/**
	 * @Desc 정기배송 회차 건너뛰기 완료 이메일 발송 위한 호출용
	 * @param odRegularResultId
	 * @return String
	 */
	@ApiOperation(value = "정기배송 회차 건너뛰기 완료 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/orderRegularReqRoundSkipCompleted")
	public void orderRegularReqRoundSkipCompleted(@RequestParam(value = "odRegularResultId", required = true) Long odRegularResultId) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.orderRegularReqRoundSkipCompleted(odRegularResultId));
	}

	/**
	 * @Desc 정기배송 상품금액 변동 안내 이메일 발송 위한 호출용
	 * @param odRegularReqId
	 * @param ilGoodsId
	 * @return String
	 */
	@ApiOperation(value = "정기배송 상품금액 변동 안내 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/orderRegularGoodsPriceChange")
	public void orderRegularGoodsPriceChange(@RequestParam(value = "odRegularReqId", required = true) Long odRegularReqId
											,@RequestParam(value = "ilGoodsId", required = true) Long ilGoodsId) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.orderRegularGoodsPriceChange(odRegularReqId, ilGoodsId));
	}

	/**
	 * @Desc 임박/폐기 품목안내 템플릿
	 * @return String
	 */
	@ApiOperation(value = "정기배송 상품금액 변동 안내 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/goodsStockDisposalAlarm")
	public void goodsStockDisposalAlarm() throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.goodsStockDisposalTmpl());
	}

	/**
	 * @Desc BOS 주문 상태 알림
	 * @param urClientId
	 * @return String
	 */
	@ApiOperation(value = "BOS 주문 상태 알림 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/bosOrderStatusNotification")
	public void bosOrderStatusNotification(@RequestParam(value = "urClientId", required = true) Long urClientId) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.bosOrderStatusNotification(urClientId));
	}

	/**
	 * @Desc BOS 수집몰 연동 실패 알림
	 * @return String
	 */
	@ApiOperation(value = "BOS 수집몰 연동 실패 알림")
	@GetMapping(value = "/admin/system/emailtmplt/bosCollectionMallInterfaceFailNotification")
	public void bosCollectionMallInterfaceFailNotification() throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.bosCollectionMallInterfaceFailNotification());
	}

	/**
	 * @Desc BOS 올가 식품안전팀 주의주문 발생 알림
	 * @param odOrderId
	 * @return String
	 */
	@ApiOperation(value = "BOS 올가 식품안전팀 주의주문 발생 알림")
	@GetMapping(value = "/admin/system/emailtmplt/bosOrgaCautionOrderNotification")
	public void bosOrgaCautionOrderNotification(@RequestParam(value = "odOrderId", required = true) Long odOrderId) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.bosOrgaCautionOrderNotification(odOrderId));
	}

	/**
	 * @Desc 매장픽업 상품 준비 이메일 발송 위한 호출용
	 * @param orderDetailGoodsList
	 * @return String
	 */
	@ApiOperation(value = "상품발송 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/orderShopPickupGoodsDelivery")
	public void orderShopPickupGoodsDelivery(@RequestParam(value = "orderDetailGoodsList", required = true) List<Long> orderDetailGoodsList) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.orderShopPickupGoodsDelivery(orderDetailGoodsList));
	}

	/**
	 * @Desc BOS 2차인증 코드 발송 이메일
	 * @param authCertiNo
	 * @return String
	 */
	@ApiOperation(value = "BOS 2차인증 코드 발송 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/bosTwoFactorAuthentification")
	public void orderShopPickupGoodsDelivery(@RequestParam(value = "authCertiNo", required = true) String authCertiNo) throws Exception{
		responseCharacterSetEuckr(emailTmpltBiz.getBosTwoFactorAuthentificationEmailTmplt(authCertiNo));
	}

	/**
	 * @Desc 고객보상제 신청 답변 확인완료 이메일
	 * @param csRewardApplyId
	 * @return String
	 */
	@ApiOperation(value = "고객보상제 신청 답변 확인완료 이메일 ")
	@GetMapping(value = "/admin/system/emailtmplt/getRewardApplyCompensation")
	public void getRewardApplyCompensation(@RequestParam(value = "csRewardApplyId", required = true) String csRewardApplyId) throws Exception {
		responseCharacterSetEuckr(emailTmpltBiz.getRewardApplyCompensationEmailTmplt(csRewardApplyId));
	}

	/**
	 * @Desc 고객보상제 신청 답변(보상완료) 이메일
	 * @param csRewardApplyId
	 * @return String
	 */
	@ApiOperation(value = "고객보상제 신청 답변(보상완료) 이메일")
	@GetMapping(value = "/admin/system/emailtmplt/getRewardApplyComplete")
	public void getRewardApplyComplete(@RequestParam(value = "csRewardApplyId", required = true) String csRewardApplyId) throws Exception {
		responseCharacterSetEuckr(emailTmpltBiz.getRewardApplyCompleteEmailTmplt(csRewardApplyId));
	}

	/**
	 * @Desc 고객보상제 신청 답변 확인완료 이메일
	 * @param csRewardApplyId
	 * @return String
	 */
	@ApiOperation(value = "고객보상제 신청 답변 확인완료 이메일 ")
	@GetMapping(value = "/admin/system/emailtmplt/getRewardApplyDeniedComplete")
	public void getRewardApplyDeniedComplete(@RequestParam(value = "csRewardApplyId", required = true) String csRewardApplyId) throws Exception {
		responseCharacterSetEuckr(emailTmpltBiz.getRewardApplyDeniedCompleteEmailTmplt(csRewardApplyId));
	}
}

