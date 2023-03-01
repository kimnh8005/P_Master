package kr.co.pulmuone.mall.user.buyer.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.CodeCommEnum;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackByUserRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaListByUserRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaInfoByUserRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaListByUserRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponListByUserRequestDto;
import kr.co.pulmuone.v1.promotion.event.dto.EventListFromMyPageRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.CommonGetPointListByUserRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.certification.dto.UserSocialInformationDto;

import javax.servlet.http.HttpServletRequest;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200826   	 	이원호            최초작성
 * =======================================================================
 * </PRE>
 */

public interface UserBuyerMallService {
    ApiResult<?> getBuyerFromMypage() throws Exception;

    ApiResult<?> putBuyerFromMypage(CommonPutBuyerFromMypageRequestDto dto) throws Exception;

    ApiResult<?> getRefundBank(CommonGetRefundBankRequestDto dto) throws Exception;

    ApiResult<?> putRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception;

    ApiResult<?> addRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception;

    ApiResult<?> delRefundBank(Long urRefundBankId) throws Exception;

    ApiResult<?> isValidationBankAccountNumber(String bankCode, String accountNumber, String holderName) throws Exception;

    ApiResult<?> getPointInfo() throws Exception;

    ApiResult<?> getPointListByUser(CommonGetPointListByUserRequestDto dto) throws Exception;

    ApiResult<?> getPointExpectExpireList() throws Exception;

    ApiResult<?> getCouponListByUser(CouponListByUserRequestDto dto) throws Exception;

    ApiResult<?> getCouponCoverage(Long pmCouponId) throws Exception;

    ApiResult<?> getAddPromotionPageInfo() throws Exception;

    ApiResult<?> addPromotionByUser(String serialNumber, String captcha) throws Exception;

    ApiResult<?> addShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception;

    ApiResult<?> putShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception;

    ApiResult<?> getShippingAddressListFromMyPage(ShippingAddressListFromMyPageRequestDto dto) throws Exception;

    ApiResult<?> getShippingAddressInfo() throws Exception;

    ApiResult<?> delShippingAddress(Long urShippingAddrId) throws Exception;

    ApiResult<?> putShippingAddressSetDefault(Long urShippingAddrId) throws Exception;

    ApiResult<?> getShippingAddressPossibleDeliveryInfo(String zipCode, String buildingCode) throws Exception;

    ApiResult<?> getRefundBankInfo() throws Exception;

    ApiResult<?> getUrlNaver() throws Exception;

    ApiResult<?> callbackNaver(HttpServletRequest request) throws Exception;

    ApiResult<?> callbackKakao(UserSocialInformationDto userSocialInformationDto) throws Exception;

    ApiResult<?> callbackGoogle(UserSocialInformationDto userSocialInformationDto) throws Exception;

    ApiResult<?> callbackFacebook(UserSocialInformationDto userSocialInformationDto) throws Exception;

    ApiResult<?> callbackApple(UserSocialInformationDto userSocialInformationDto) throws Exception;

    ApiResult<?> delSyncAccount(String urSocialId, String provider) throws Exception;

    ApiResult<?> getCouponCountByUser(String status) throws Exception;

    ApiResult<?> getGroupInfoByUser() throws Exception;

    ApiResult<?> getFeedbackInfoByUser() throws Exception;

    ApiResult<?> getFeedbackByUser(FeedbackByUserRequestDto dto) throws Exception;

    ApiResult<?> addFeedback(FeedbackRequestDto dto) throws Exception;

    ApiResult<?> getFeedbackTargetListByUser(int page, int limit) throws Exception;

    ApiResult<?> getQnaInfoByUser(QnaInfoByUserRequestDto dto) throws Exception;

    ApiResult<?> getQnaListByUser(QnaListByUserRequestDto dto) throws Exception;

    ApiResult<?> getProductQnaListByUser(ProductQnaListByUserRequestDto dto) throws Exception;

    ApiResult<?> putProductQnaSetSecretByUser(Long csQnaId) throws Exception;

    ApiResult<?> putQnaAnswerUserCheckYn(Long csQnaId) throws Exception;

    ApiResult<?> getEventListFromMyPage(EventListFromMyPageRequestDto dto) throws Exception;

    ApiResult<?> getEmployeeDiscount() throws Exception;

    ApiResult<?> getEmployeeDiscountPastInfo(String searchDate) throws Exception;

    CodeCommEnum getUserStatus(BuyerVo buyerVo);

    ApiResult<?> getDropPageInfo() throws Exception;

    ApiResult<?> progressUserDrop(UserDropRequestDto dto) throws Exception;

    ApiResult<?> getMypageMainInfo() throws Exception;

    ApiResult<?> getQnaNewAnswer() throws Exception;

    ApiResult<?> getMypageShippingAddressList(long ilGoodsId, long odOrderDetlId) throws Exception;

    ApiResult<?> getAsisUserPoint(AsisUserPointRequestDto dto) throws Exception;

    ApiResult<?> depositPointByAsisPoint(AsisUserPointRequestDto dto) throws Exception;

    ApiResult<?> isPossibleChangeDeliveryAddress(IsPossibleChangeDeliveryAddressRequestDto reqDto) throws Exception;

}
