package kr.co.pulmuone.v1.system.emailtmplt.service;

import org.springframework.web.servlet.ModelAndView;

import java.util.List;



public interface EmailTmpltBiz {

	ModelAndView getFindPassWordEmailTmpltMV(String userId, String userPassword) throws Exception;

	String getFindBosPassWordEmailTmplt(String urUserId, String userPassword) throws Exception;

	String getFindBuyerPassWordEmailTmplt(String urUserId, String userPassword) throws Exception;

	String getSignUpCompletedEmailTmplt(String urUserId) throws Exception;

	String getBosSignUpCompletedEmailTmplt(String employeeNumber) throws Exception;

	String getUserDropInfoEmailTmplt(Long urUserDropId) throws Exception;

	String getBuyerStopInfoEmailTmplt(String urUserId) throws Exception;

	String getBuyerNormalInfoEmailTmplt(String urUserId) throws Exception;

	String getEmployeeCertificationEmailTmplt(String tempCertiNo) throws Exception;

	String getOnetooneQnaAddEmailTmplt(String urUserId) throws Exception;

	String getOnetooneQnaAnswerEmailTmplt(String csQnaId) throws Exception;

	String getProductQnaAnswerEmailTmplt(String csQnaId) throws Exception;

	String getUserDormantCompletedEmailTmplt(Long urUserId) throws Exception;

	String getUserDormantExpected(Long urUserId) throws Exception;

	String getMarketingInfo(Long urUserId) throws Exception;

	String getPointExpectExpired(Long urUserId) throws Exception;

	String getDirectShippingUnregisteredInvoice() throws Exception;

	String orderReceivedComplete(Long odOrderId) throws Exception;

	String orderPaymentComplete(Long odOrderId) throws Exception;

	String orderDepositComplete(Long odOrderId) throws Exception;

	String orderRegularPaymentComplete(Long odRegularResultId) throws Exception;

	String orderGoodsDelivery(List<Long> orderDetailGoodsList) throws Exception;

	String orderCancelComplete(Long odClaimId, List<Long> odOrderDetlIdList) throws Exception;

	String orderReturnComplete(Long odClaimId, List<Long> odOrderDetlIdList) throws Exception;

	String orderRegularApplyCompleted(Long odRegularReqId, String firstOrderYn) throws Exception;

	String orderRegualrPaymentFailSecond(Long odRegularResultId) throws Exception;

	String orderRegularCancelCompleted(Long odRegularResultDetlId) throws Exception;

	String orderRegularGoodsSkipCompleted(Long odRegularResultDetlId) throws Exception;

	String orderRegularReqRoundSkipCompleted(Long odRegularResultId) throws Exception;

	String orderRegularGoodsPriceChange(Long odRegularReqId, Long ilGoodsId) throws Exception;

	String goodsStockDisposalTmpl() throws Exception;

	String bosOrderStatusNotification(Long urClientId) throws Exception;

	String bosCollectionMallInterfaceFailNotification() throws Exception;

	String bosOrgaCautionOrderNotification(Long odOrderId) throws Exception;

	String orderShopPickupGoodsDelivery(List<Long> orderDetailGoodsList) throws Exception;

	String getBosTwoFactorAuthentificationEmailTmplt(String authCertiNo) throws Exception;

	String getRewardApplyCompensationEmailTmplt(String csRewardApplyId) throws Exception;

	String getRewardApplyCompleteEmailTmplt(String csRewardApplyId) throws Exception;

	String getRewardApplyDeniedCompleteEmailTmplt(String csRewardApplyId) throws Exception;

}


