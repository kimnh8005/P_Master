package kr.co.pulmuone.v1.system.emailtmplt.service;

import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SHA256Util;
import kr.co.pulmuone.v1.comm.util.SystemUtil;
import kr.co.pulmuone.v1.customer.qna.dto.vo.OnetooneQnaResultVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosDetailVo;
import kr.co.pulmuone.v1.customer.qna.service.CustomerQnaBiz;
import kr.co.pulmuone.v1.customer.reward.dto.RewardApplyRequestBosDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardApplyResponseBosDto;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyVo;
import kr.co.pulmuone.v1.customer.reward.service.RewardBiz;
import kr.co.pulmuone.v1.order.email.dto.DirectShippingUnregistetedInvoiceDto;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.dto.UnregistetedInvoiceDto;
import kr.co.pulmuone.v1.order.email.dto.vo.BosOrderInfoForEmailVo;
import kr.co.pulmuone.v1.order.email.dto.vo.GoodsStockDisposalForEmailVo;
import kr.co.pulmuone.v1.order.email.dto.vo.OrderInfoForEmailVo;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.promotion.point.dto.vo.PointExpiredForEmailVo;
import kr.co.pulmuone.v1.promotion.point.dto.vo.PointExpiredListForEmailVo;
import kr.co.pulmuone.v1.promotion.point.service.PromotionPointBiz;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.user.buyer.dto.MarketingInfoDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.BuyerStatusResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.UserDropResultVo;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerDropBiz;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerStatusBiz;
import kr.co.pulmuone.v1.user.certification.dto.vo.EmployeeCertificationResultVo;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.UserDormancyResultVo;
import kr.co.pulmuone.v1.user.dormancy.service.UserDormancyBiz;
import kr.co.pulmuone.v1.user.employee.dto.EmployeeResponseDto;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeVo;
import kr.co.pulmuone.v1.user.employee.service.UserEmployeeBiz;
import kr.co.pulmuone.v1.user.join.dto.SaveBuyerRequestDto;
import kr.co.pulmuone.v1.user.join.dto.vo.JoinResultVo;
import kr.co.pulmuone.v1.user.join.service.UserJoinBiz;
import kr.co.pulmuone.v1.user.login.dto.LoginRequestDto;
import kr.co.pulmuone.v1.user.login.dto.vo.LoginResultVo;
import kr.co.pulmuone.v1.user.login.service.UserLoginBosBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmailTmpltBizImpl implements EmailTmpltBiz {

	@Autowired
	private UserLoginBosBiz userLoginBosBiz;

	@Autowired
	private UserJoinBiz userJoinBiz;

	@Autowired
	private UserBuyerDropBiz userBuyerDropBiz;

	@Autowired
	private UserBuyerStatusBiz userBuyerStatusBiz;

	@Autowired
	private CustomerQnaBiz customerQnaBiz;

	@Autowired
	private ComnBizImpl comnBizImpl;

    @Autowired
	private SendTemplateBiz sendTemplateBiz;

    @Autowired
    private UserDormancyBiz userDormancyBiz;

    @Autowired
    private PromotionPointBiz promotionPointBiz;

    @Autowired
    private OrderEmailBiz orderEmailBiz;

    @Autowired
    private UserEmployeeBiz userEmployeeBiz;

    @Autowired
	private RewardBiz rewardBiz;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	/**
	 *
	 * @Desc 비밀번호 찾기
	 * @param userId
	 * @param userPassword
	 * @return
	 * @throws Exception
	 */
	@Override
	public ModelAndView getFindPassWordEmailTmpltMV(String userId, String userPassword) throws Exception {

		LoginRequestDto loginRequestDto = new LoginRequestDto();
		loginRequestDto.setLoginId(userId);
		loginRequestDto.setPassword(SHA256Util.getUserPassword(userPassword));

		LoginResultVo loginResultVo = userLoginBosBiz.getPasswordByPassword(loginRequestDto);

		ModelAndView mv = new ModelAndView();
		mv.addObject("findUserId", userId);
		mv.addObject("findUserPasswordId", userPassword);
		mv.addObject("findUserName", loginResultVo.getLoginName());
		mv.setViewName("/email/emailFindPwd");
		return mv;
	}

	/**
	 * @Desc BOS 계정 비밀번호 재설정 이메일 템플릿
	 * @param urUserId
	 * @param userPassword
	 * @return
	 */
	@Override
	public String getFindBosPassWordEmailTmplt(String urUserId, String userPassword) throws Exception{

		LoginRequestDto loginRequestDto = new LoginRequestDto();
		loginRequestDto.setUrUserId(urUserId);
		loginRequestDto.setPassword(SHA256Util.getUserPassword(userPassword));
		LoginResultVo loginResultVo = userLoginBosBiz.getPasswordByPassword(loginRequestDto);

		if(loginResultVo == null) {
			return null;
		}
		loginResultVo.setPassword(userPassword);

		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BOS_FIND_PASSWORD.getCode());
    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, loginResultVo);
	}

	/**
	 * @Desc 구매자 비밀번호 재설정 이메일 템플릿
	 * @param urUserId
	 * @param userPassword
	 * @return
	 */
	@Override
	public String getFindBuyerPassWordEmailTmplt(String urUserId, String userPassword) throws Exception{

		LoginRequestDto loginRequestDto = new LoginRequestDto();
		loginRequestDto.setUrUserId(urUserId);
		loginRequestDto.setPassword(SHA256Util.getUserPassword(userPassword));
		LoginResultVo loginResultVo = userLoginBosBiz.getPasswordByPassword(loginRequestDto);

		if(loginResultVo == null) {
			return null;
		}
		loginResultVo.setPassword(userPassword);

		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BUYER_FIND_PASSWORD.getCode());
    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, loginResultVo);
	}

	/**
	 * @Desc 회원가입 완료 이메일 템플릿
	 * @param urUserId
	 * @return
	 */
	@Override
	public String getSignUpCompletedEmailTmplt(String urUserId) throws Exception{

		SaveBuyerRequestDto saveBuyerRequestDto = new SaveBuyerRequestDto();
		saveBuyerRequestDto.setUrUserId(urUserId);
		JoinResultVo joinResultVo = userJoinBiz.getJoinCompletedInfo(saveBuyerRequestDto.getUrUserId());// 회원가입완료시 정보받기

		if(joinResultVo == null) {
			return null;
		}

		//Email,SMS 템플릿 코드로 상세조회 'SIGN_UP_COMPLETED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.SIGN_UP_COMPLETED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, joinResultVo);

	}

	/**
	 * @Desc 관리자 가입 완료 이메일 템플릿
	 * @param employeeNumber
	 */
	@Override
	public String getBosSignUpCompletedEmailTmplt(String employeeNumber) throws Exception{

		EmployeeResponseDto result = (EmployeeResponseDto)userEmployeeBiz.getEmployeeInfo(employeeNumber).getData();
		EmployeeVo employeeInfo = result.getEmployeeInfo();

		if(employeeInfo == null) {
			return null;
		}

		//Email,SMS 템플릿 코드로 상세조회 'BOS_SIGN_UP_COMPLETED'
		ApiResult<?> apiResult = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BOS_SIGN_UP_COMPLETED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)apiResult.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, employeeInfo);

	}

	/**
	 * @Desc 회원탈퇴 완료 이메일 템플릿
	 * @param urUserDropId
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getUserDropInfoEmailTmplt(Long urUserDropId) throws Exception {
		UserDropResultVo userDropResultVo = userBuyerDropBiz.getUserDropInfo(urUserDropId);

		if(userDropResultVo == null) {
			return null;
		}

		//Email,SMS 템플릿 코드로 상세조회 'USER_DROP_INFO'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.USER_DROP_INFO.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, userDropResultVo);
	}

	/**
	 * @Desc 정지회원 전환 완료 이메일 템플릿
	 * @param urUserId
	 */
	@Override
	public String getBuyerStopInfoEmailTmplt(String urUserId) throws Exception{
		BuyerStatusResultVo buyerStatusResultVo = userBuyerStatusBiz.getBuyerStatusConvertInfo(urUserId);

		buyerStatusResultVo.setServiceMail(sendTemplateBiz.getPsValue("SEND_EMAIL_ADDRESS")); // 고객센터이메일 (임시 : shop@pulmuone.com)

		//Email,SMS 템플릿 코드로 상세조회 'BUYER_STOP_CONVERT_INFO'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BUYER_STOP_CONVERT_INFO.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, buyerStatusResultVo);
	}

	/**
	 * @Desc 정상회원 전환 완료 이메일 템플릿
	 * @param urUserId
	 */
	@Override
	public String getBuyerNormalInfoEmailTmplt(String urUserId) throws Exception{
		BuyerStatusResultVo buyerStatusResultVo = userBuyerStatusBiz.getBuyerStatusConvertInfo(urUserId);

		if(buyerStatusResultVo == null) {
			return null;
		}

		//Email,SMS 템플릿 코드로 상세조회 'BUYER_NORMAL_CONVERT_INFO'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BUYER_NORMAL_CONVERT_INFO.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, buyerStatusResultVo);
	}

	/**
	 * @Desc 임직원 회원 인증코드 이메일 템플릿
	 * @param tempCertiNo
	 */
	@Override
	public String getEmployeeCertificationEmailTmplt(String tempCertiNo) throws Exception{
		EmployeeCertificationResultVo employeeCertificationResultVo = new EmployeeCertificationResultVo();

        String[] arrayTempCertiNo;
        arrayTempCertiNo = tempCertiNo.split("");

        employeeCertificationResultVo.setTempCertiNoFirst(arrayTempCertiNo[0]);
        employeeCertificationResultVo.setTempCertiNoSecond(arrayTempCertiNo[1]);
        employeeCertificationResultVo.setTempCertiNoThird(arrayTempCertiNo[2]);
        employeeCertificationResultVo.setTempCertiNoFourth(arrayTempCertiNo[3]);
        employeeCertificationResultVo.setTempCertiNoFifth(arrayTempCertiNo[4]);
        employeeCertificationResultVo.setTempCertiNoSixth(arrayTempCertiNo[5]);

		//Email,SMS 템플릿 코드로 상세조회 'EMPLOYEE_CERTIFICATION_INFO'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.EMPLOYEE_CERTIFICATION_INFO.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, employeeCertificationResultVo);
	}

	/**
	 * @Desc 1:1문의 접수완료 이메일 템플릿
	 * @param urUserId
	 */
	@Override
	public String getOnetooneQnaAddEmailTmplt(String urUserId) throws Exception{
		OnetooneQnaResultVo onetooneQnaResultVo = customerQnaBiz.getOnetooneQnaAddInfo(urUserId);

		if(onetooneQnaResultVo == null) {
			return null;
		}

		//Email,SMS 템플릿 코드로 상세조회 'ONETOONE_QNA_ADD_COMPLETED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ONETOONE_QNA_ADD_COMPLETED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, onetooneQnaResultVo);
	}

	/**
	 * @Desc 1:1문의 답변완료 이메일 템플릿
	 * @param csQnaId
	 */
	@Override
	public String getOnetooneQnaAnswerEmailTmplt(String csQnaId) throws Exception{
		QnaBosDetailVo qnaBosDetailResultVo = customerQnaBiz.getQnaAnswerInfo(csQnaId);

		if(qnaBosDetailResultVo == null) {
			return null;
		}

		//Email,SMS 템플릿 코드로 상세조회 'ONETOONE_QNA_ANSWER_COMPLETED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ONETOONE_QNA_ANSWER_COMPLETED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, qnaBosDetailResultVo);
	}

	/**
	 * @Desc 상품문의 답변완료 이메일 템플릿
	 * @param csQnaId
	 */
	@Override
	public String getProductQnaAnswerEmailTmplt(String csQnaId) throws Exception{
		QnaBosDetailVo qnaBosDetailResultVo = customerQnaBiz.getQnaAnswerInfo(csQnaId);

		if(qnaBosDetailResultVo == null) {
			return null;
		}

		//Email,SMS 템플릿 코드로 상세조회 'PRODUCT_QNA_ANSWER_COMPLETED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.PRODUCT_QNA_ANSWER_COMPLETED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, qnaBosDetailResultVo);
	}

	/*
	 * @Desc 휴면계정 전환 완료 이메일 템플릿
	 * @param urUserId
	 */
	@Override
	public String getUserDormantCompletedEmailTmplt(Long urUserId) throws Exception{
		UserDormancyResultVo userDormancyResultVo = userDormancyBiz.getUserDormancyInfo(urUserId);

		//Email,SMS 템플릿 코드로 상세조회 'USER_DORMANT_COMPLETED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.USER_DORMANT_COMPLETED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, userDormancyResultVo);
	}

	/**
	 * @Desc 휴면계정 전환 예정 안내 이메일 템플릿
	 * @param urUserId
	 */
	@Override
	public String getUserDormantExpected(Long urUserId) throws Exception{
		UserDormancyResultVo userDormancyResultVo = userDormancyBiz.getUserDormancyExpected(urUserId);

		//Email,SMS 템플릿 코드로 상세조회 'USER_DORMANT_EXPECTED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.USER_DORMANT_EXPECTED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, userDormancyResultVo);
	}

	/**
	 * @Desc 마케팅 정보 수신동의 안내 이메일 템플릿
	 * @param urUserId
	 */
	@Override
	public String getMarketingInfo(Long urUserId) throws Exception{
		MarketingInfoDto marketingInfoDto = userBuyerStatusBiz.getMarketingInfo(urUserId);

		//Email,SMS 템플릿 코드로 상세조회 'MARKETING_INFO'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.MARKETING_INFO.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, marketingInfoDto);
	}

	/**
	 * @Desc 적립금 소멸 예정 안내 이메일 템플릿
	 * @param urUserId
	 */
	@Override
	public String getPointExpectExpired(Long urUserId) throws Exception{
		PointExpiredForEmailVo pointExpiredForEmailVo = promotionPointBiz.getPointExpectExpiredForEmail(urUserId);
		List<PointExpiredListForEmailVo> pointExpiredlistForEmail = promotionPointBiz.getPointExpectExpireListForEmail(urUserId);
		pointExpiredForEmailVo.setList(pointExpiredlistForEmail);

		//Email,SMS 템플릿 코드로 상세조회 'POINT_EXPECT_EXPIRED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.POINT_EXPECT_EXPIRED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, pointExpiredForEmailVo);
	}

	/**
	 * 직접배송 미등록 송장 알림
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getDirectShippingUnregisteredInvoice() throws Exception {
		DirectShippingUnregistetedInvoiceDto directShippingUnregistetedInvoiceDto = new DirectShippingUnregistetedInvoiceDto();
		List<UnregistetedInvoiceDto> unregistetedInvoiceList = orderEmailBiz.getUnregistetedInvoiceList();
		directShippingUnregistetedInvoiceDto.setList(unregistetedInvoiceList);
		directShippingUnregistetedInvoiceDto.setMailDate(DateUtil.getDate("yyyy/MM/dd"));
		directShippingUnregistetedInvoiceDto.setUnregisteredCount(unregistetedInvoiceList == null ? 0 : unregistetedInvoiceList.size());

		//Email,SMS 템플릿 코드로 상세조회 'DIRECT_SHIPPING_UNREGISTERED_INVOICE_NOTIFICATION'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.DIRECT_SHIPPING_UNREGISTERED_INVOICE_NOTIFICATION.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, directShippingUnregistetedInvoiceDto);
	}

	/**
	 * @Desc 주문 접수 완료 이메일 템플릿
	 * @param odOrderId
	 */
	@Override
	public String orderReceivedComplete(Long odOrderId) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailVo = orderEmailBiz.getOrderInfoForEmail(odOrderId);

		//Email,SMS 템플릿 코드로 상세조회 'ORDER_RECEIVED_COMPLETED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_RECEIVED_COMPLETED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoForEmailVo);
	}

	/**
	 * @Desc 주문 결제 완료 이메일 템플릿
	 * @param odOrderId
	 */
	@Override
	public String orderPaymentComplete(Long odOrderId) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailVo = orderEmailBiz.getOrderInfoForEmail(odOrderId);

		//Email,SMS 템플릿 코드로 상세조회 'ORDER_PAYMENT_COMPLETED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_PAYMENT_COMPLETED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoForEmailVo);
	}

	/**
	 * @Desc 주문 입금 완료 이메일 템플릿
	 * @param odOrderId
	 */
	@Override
	public String orderDepositComplete(Long odOrderId) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailVo = orderEmailBiz.getOrderInfoForEmail(odOrderId);

		//Email,SMS 템플릿 코드로 상세조회 'ORDER_DEPOSIT_COMPLETED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_DEPOSIT_COMPLETED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoForEmailVo);
	}

	/**
	 * @Desc 정기 주문 결제 완료 이메일 템플릿
	 * @param odRegularResultId
	 */
	@Override
	public String orderRegularPaymentComplete(Long odRegularResultId) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailVo = orderEmailBiz.getOrderRegularInfoForEmail(odRegularResultId);

		//Email,SMS 템플릿 코드로 상세조회 'ORDER_REGULAR_PAYMENT_COMPLETE'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_PAYMENT_COMPLETE.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoForEmailVo);
	}


	/**
	 * @Desc 상품 발송 이메일 템플릿
	 * @param orderDetailGoodsList
	 */
	@Override
	public String orderGoodsDelivery(List<Long> orderDetailGoodsList) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailVo = orderEmailBiz.getOrderGoodsDeliveryInfoForEmail(orderDetailGoodsList);

		//Email,SMS 템플릿 코드로 상세조회 'ORDER_GOODS_DELIVERY'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_GOODS_DELIVERY.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoForEmailVo);
	}


	/**
	 * @Desc 주문 취소 완료 이메일 템플릿
	 * @param odClaimId
	 * @param odOrderDetlIdList
	 */
	@Override
	public String orderCancelComplete(Long odClaimId, List<Long> odOrderDetlIdList) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailVo = orderEmailBiz.getOrderClaimCompleteInfoForEmail(odClaimId, odOrderDetlIdList);

		//Email,SMS 템플릿 코드로 상세조회 'ORDER_CANCEL_COMPLETED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_CANCEL_COMPLETED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoForEmailVo);
	}

	/**
	 * @Desc 주문 반품 완료 이메일 템플릿
	 * @param odClaimId
	 * @param odOrderDetlIdList
	 */
	@Override
	public String orderReturnComplete(Long odClaimId, List<Long> odOrderDetlIdList) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailVo = orderEmailBiz.getOrderClaimCompleteInfoForEmail(odClaimId, odOrderDetlIdList);

		//Email,SMS 템플릿 코드로 상세조회 'ORDER_RETURN_COMPLETED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_RETURN_COMPLETED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoForEmailVo);
	}

	/**
	 * @Desc 정기배송 신청 완료 이메일 템플릿
	 * @param odRegularReqId
	 */
	@Override
	public String orderRegularApplyCompleted(Long odRegularReqId, String firstOrderYn) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailVo = orderEmailBiz.getOrderRegularApplyCompletedInfoForEmail(odRegularReqId, firstOrderYn);

		//Email,SMS 템플릿 코드로 상세조회 'ORDER_REGULAR_APPLY_COMPLETED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_APPLY_COMPLETED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoForEmailVo);
	}


	/**
	 * @Desc 정기배송 결제 실패(2차) 이메일 템플릿
	 * @param odRegularResultId
	 */
	@Override
	public String orderRegualrPaymentFailSecond(Long odRegularResultId) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailVo = orderEmailBiz.getOrderRegularInfoForEmail(odRegularResultId);

		//Email,SMS 템플릿 코드로 상세조회 'ORDER_REGULAR_PAYMENT_FAIL_SECOND'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_PAYMENT_FAIL_SECOND.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoForEmailVo);
	}

	/**
	 * @Desc 정기배송 취소 완료 이메일 템플릿
	 * @param odRegularResultDetlId
	 */
	@Override
	public String orderRegularCancelCompleted(Long odRegularResultDetlId) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailVo = orderEmailBiz.getOrderRegularResultDetlInfoForEmail(odRegularResultDetlId);

		//Email,SMS 템플릿 코드로 상세조회 'ORDER_REGULAR_CANCEL_COMPLETED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_CANCEL_COMPLETED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoForEmailVo);
	}

	/**
	 * @Desc 정기배송 상품 건너뛰기 완료 이메일 템플릿
	 * @param odRegularResultDetlId
	 */
	@Override
	public String orderRegularGoodsSkipCompleted(Long odRegularResultDetlId) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailVo = orderEmailBiz.getOrderRegularResultDetlInfoForEmail(odRegularResultDetlId);

		//Email,SMS 템플릿 코드로 상세조회 'ORDER_REGULAR_GOODS_SKIP_COMPLETED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_GOODS_SKIP_COMPLETED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoForEmailVo);
	}


	/**
	 * @Desc 정기배송 회차 건너뛰기 완료 이메일 템플릿
	 * @param odRegularResultId
	 */
	@Override
	public String orderRegularReqRoundSkipCompleted(Long odRegularResultId) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailVo = orderEmailBiz.getOrderRegularInfoForEmail(odRegularResultId);

		//Email,SMS 템플릿 코드로 상세조회 'ORDER_REGULAR_REQ_ROUND_SKIP_COMPLETED'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_REQ_ROUND_SKIP_COMPLETED.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoForEmailVo);
	}


	/**
	 * @Desc 정기배송 상품금액 변동 안내  이메일 템플릿
	 * @param odRegularReqId
	 * @param ilGoodsId
	 */
	@Override
	public String orderRegularGoodsPriceChange(Long odRegularReqId, Long ilGoodsId) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailVo
							= orderEmailBiz.getOrderRegularGoodsPriceChangeInfoForEmail(odRegularReqId, ilGoodsId);

		//Email,SMS 템플릿 코드로 상세조회 'ORDER_REGULAR_GOODS_PRICE_CHANGE'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_GOODS_PRICE_CHANGE.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoForEmailVo);
	}


	/**
	 * @Desc 임박/폐기 품목안내 템플릿
	 */
	@Override
	public String goodsStockDisposalTmpl() throws Exception {
		GoodsStockDisposalForEmailVo goodsStockDisposalForEmailVo
													= GoodsStockDisposalForEmailVo.builder()
																				.baseYear(DateUtil.getCurrentDate("yyyy"))
																				.baseMonth(DateUtil.getCurrentDate("MM"))
																				.baseDay(DateUtil.getCurrentDate("dd"))
																				.build();

		//TODO : 공통 확인 필요
		if (SystemUtil.DEFAULT_PROFILE.equals(activeProfile)) {
			goodsStockDisposalForEmailVo.setProfileUrl("http://localhost:8280/layout.html#/goodsStockDisposal");
		} else if (SystemUtil.DEV_PROFILE.equals(activeProfile)) {
			goodsStockDisposalForEmailVo.setProfileUrl("https://dev0shopbos.pulmuone.online/layout.html#/goodsStockDisposal");
		} else if (SystemUtil.QA_PROFILE.equals(activeProfile)) {
			goodsStockDisposalForEmailVo.setProfileUrl("https://qashopbos.pulmuone.online/layout.html#/goodsStockDisposal");
		} else if (SystemUtil.VER_PROFILE.equals(activeProfile)) {
			goodsStockDisposalForEmailVo.setProfileUrl("https://shopbos.pulmuone.co.kr/layout.html#/goodsStockDisposal");
		} else if (SystemUtil.PROD_PROFILE.equals(activeProfile)) {
			goodsStockDisposalForEmailVo.setProfileUrl("");
		}

		//Email,SMS 템플릿 코드로 상세조회 'ORDER_REGULAR_GOODS_PRICE_CHANGE'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.GOODS_STOCK_DISPOSAL.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, goodsStockDisposalForEmailVo);
	}

	/**
	 * @Desc BOS 주문 상태 알림
	 * @param urClientId
	 */
	@Override
	public String bosOrderStatusNotification(Long urClientId) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailVo = orderEmailBiz.getBosOrderStatusNotificationForEmail(urClientId);

		//Email,SMS 템플릿 코드로 상세조회 'BOS_ORDER_STATUS_NOTIFICATION'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BOS_ORDER_STATUS_NOTIFICATION.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoForEmailVo);
	}

	/**
	 * @Desc BOS 수집몰 연동 실패 알림
	 */
	@Override
	public String bosCollectionMallInterfaceFailNotification() throws Exception{
		BosOrderInfoForEmailVo bosOrderInfoForEmailVo = new BosOrderInfoForEmailVo();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date time = new Date();
		String sendDate = format.format(time);
		bosOrderInfoForEmailVo.setSendDate(sendDate);

		//Email,SMS 템플릿 코드로 상세조회 'BOS_COLLECTIONMALL_INTERFACE_FAIL_NOTIFICATION'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BOS_COLLECTIONMALL_INTERFACE_FAIL_NOTIFICATION.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, bosOrderInfoForEmailVo);
	}

	/**
	 * @Desc BOS 올가 식품안전팀 주의주문 발생 알림
	 * @param odOrderId
	 */
	@Override
	public String bosOrgaCautionOrderNotification(Long odOrderId) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailResultDto = orderEmailBiz.getOrderInfoForEmail(odOrderId);
		OrderInfoForEmailVo orderInfoVo = orderInfoForEmailResultDto.getOrderInfoVo();

		//Email,SMS 템플릿 코드로 상세조회 'BOS_ORGA_CAUTION_ORDER_NOTIFICATION'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BOS_ORGA_CAUTION_ORDER_NOTIFICATION.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoVo);
	}

	/**
	 * @Desc 매장픽업 상품 준비 이메일 템플릿
	 * @param orderDetailGoodsList
	 */
	@Override
	public String orderShopPickupGoodsDelivery(List<Long> orderDetailGoodsList) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailVo = orderEmailBiz.getOrderShopPickupGoodsDeliveryInfoForEmail(orderDetailGoodsList);

		//Email,SMS 템플릿 코드로 상세조회 'ORDER_SHOP_PICKUP_GOODS_DELIVERY'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_SHOP_PICKUP_GOODS_DELIVERY.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, orderInfoForEmailVo);
	}

	/**
	 * @Desc 임직원 회원 인증코드 이메일 템플릿
	 * @param authCertiNo
	 */
	@Override
	public String getBosTwoFactorAuthentificationEmailTmplt(String authCertiNo) throws Exception{
		EmployeeCertificationResultVo employeeCertificationResultVo = new EmployeeCertificationResultVo();

		String[] arrayTempCertiNo;
		arrayTempCertiNo = authCertiNo.split("");

		employeeCertificationResultVo.setTempCertiNoFirst(arrayTempCertiNo[0]);
		employeeCertificationResultVo.setTempCertiNoSecond(arrayTempCertiNo[1]);
		employeeCertificationResultVo.setTempCertiNoThird(arrayTempCertiNo[2]);
		employeeCertificationResultVo.setTempCertiNoFourth(arrayTempCertiNo[3]);
		employeeCertificationResultVo.setTempCertiNoFifth(arrayTempCertiNo[4]);
		employeeCertificationResultVo.setTempCertiNoSixth(arrayTempCertiNo[5]);

		//Email,SMS 템플릿 코드로 상세조회 'BOS_TWO_FACTOR_AUTHENTIFICATION'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BOS_TWO_FACTOR_AUTHENTIFICATION.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, employeeCertificationResultVo);
	}


	/**
	 * @Desc
	 * @param csRewardApplyId
	 */
	@Override
	public String getRewardApplyCompensationEmailTmplt(String csRewardApplyId) throws Exception{
		RewardApplyRequestBosDto dto = new RewardApplyRequestBosDto();
		dto.setCsRewardApplyId(csRewardApplyId);
		RewardApplyVo vo = new RewardApplyVo();
		ApiResult<?> rewardResult = rewardBiz.getRewardApplyDetail(dto);
		vo = ((RewardApplyResponseBosDto)rewardResult.getData()).getRow();
		if(vo.getNoMaskUserName() != null){
			vo.setUserName(vo.getNoMaskUserName());
		}
		//Email,SMS 템플릿 코드로 상세조회 'BOS_ORGA_CAUTION_ORDER_NOTIFICATION'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.REWARD_APPLY_COMPENSATION.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, vo);
	}

	/**
	 * @Desc
	 * @param csRewardApplyId
	 */
	@Override
	public String getRewardApplyCompleteEmailTmplt(String csRewardApplyId) throws Exception{
		RewardApplyRequestBosDto dto = new RewardApplyRequestBosDto();
		dto.setCsRewardApplyId(csRewardApplyId);
		RewardApplyVo vo = new RewardApplyVo();
		ApiResult<?> rewardResult = rewardBiz.getRewardApplyDetail(dto);
		vo = ((RewardApplyResponseBosDto)rewardResult.getData()).getRow();
		if(vo.getNoMaskUserName() != null){
			vo.setUserName(vo.getNoMaskUserName());
		}
		//Email,SMS 템플릿 코드로 상세조회 'BOS_ORGA_CAUTION_ORDER_NOTIFICATION'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.REWARD_APPLY_COMPLETE.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, vo);
	}

	/**
	 * @Desc
	 * @param csRewardApplyId
	 */
	@Override
	public String getRewardApplyDeniedCompleteEmailTmplt(String csRewardApplyId) throws Exception{
		RewardApplyRequestBosDto dto = new RewardApplyRequestBosDto();
		dto.setCsRewardApplyId(csRewardApplyId);
		RewardApplyVo vo = new RewardApplyVo();
		ApiResult<?> rewardResult = rewardBiz.getRewardApplyDetail(dto);
		vo = ((RewardApplyResponseBosDto)rewardResult.getData()).getRow();
		if(vo.getNoMaskUserName() != null){
			vo.setUserName(vo.getNoMaskUserName());
		}
		//Email,SMS 템플릿 코드로 상세조회 'BOS_ORGA_CAUTION_ORDER_NOTIFICATION'
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.REWARD_APPLY_DENIED_COMPLETE.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 내용 받기
		return comnBizImpl.getEmailTmplt(getEmailSendResultVo, vo);
	}

}
