package kr.co.pulmuone.mall.user.buyer.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackByUserRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackTargetListByUserRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackTargetListByUserResponseDto;
import kr.co.pulmuone.v1.customer.feedback.service.CustomerFeedbackBiz;
import kr.co.pulmuone.v1.customer.qna.dto.*;
import kr.co.pulmuone.v1.customer.qna.service.CustomerQnaBiz;
import kr.co.pulmuone.v1.customer.reward.dto.RewardApplyRequestDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardApplyResponseDto;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyListVo;
import kr.co.pulmuone.v1.customer.reward.service.CustomerRewardMallBiz;
import kr.co.pulmuone.v1.order.front.dto.vo.OrderCountFromMyPageVo;
import kr.co.pulmuone.v1.order.front.service.OrderFrontBiz;
import kr.co.pulmuone.v1.policy.benefit.service.PolicyBenefitEmployeeBiz;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponListByUserRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponCountByUserVo;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.event.dto.EventListFromMyPageRequestDto;
import kr.co.pulmuone.v1.promotion.event.service.PromotionEventBiz;
import kr.co.pulmuone.v1.promotion.point.dto.CommonGetPointListByUserRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.GetPointInfoResponseDto;
import kr.co.pulmuone.v1.promotion.point.service.PromotionPointBiz;
import kr.co.pulmuone.v1.promotion.serialnumber.service.SerialNumberBiz;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetRefundBankResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetShippingAddressResultVo;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerDropBiz;
import kr.co.pulmuone.v1.user.certification.dto.AddSessionShippingRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.UserSocialInformationDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.group.dto.vo.GroupInfoByUserResultVo;
import kr.co.pulmuone.v1.user.group.service.UserGroupBiz;
import kr.co.pulmuone.v1.user.movereason.service.UserMoveReasonBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * <PRE>
 * Forbiz Korea
 * Class 의 기능과 역할을 상세히 기술한다.
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

@Slf4j
@Service("userBuyerMallService")
public class UserBuyerMallServiceImpl implements UserBuyerMallService {
    @Autowired
    private UserBuyerBiz userBuyerBiz;

    @Autowired
    private PromotionPointBiz promotionPointBiz;

    @Autowired
    private PromotionCouponBiz promotionCouponBiz;

    @Autowired
    private SerialNumberBiz serialNumberBiz;

    @Autowired
    private CustomerFeedbackBiz customerFeedbackBiz;

    @Autowired
    private CustomerQnaBiz customerQnaBiz;

    @Autowired
    private PromotionEventBiz promotionEventBiz;

    @Autowired
    private PolicyBenefitEmployeeBiz policyBenefitEmployeeBiz;

    @Autowired
    private UserMoveReasonBiz userMoveReasonBiz;

    @Autowired
    private UserBuyerDropBiz userBuyerDropBiz;

    @Autowired
    private UserGroupBiz userGroupBiz;

    @Autowired
    private OrderFrontBiz orderFrontBiz;

    @Autowired
    private StoreDeliveryBiz storeDeliveryBiz;

    @Autowired
    private UserCertificationBiz userCertificationBiz;

    @Autowired
    private CustomerRewardMallBiz customerRewardMallBiz;

    @Override
    public ApiResult<?> getBuyerFromMypage() throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        return ApiResult.success(userBuyerBiz.getBuyerFromMypage(Long.valueOf(buyerVo.getUrUserId())));
    }

    @Override
    public ApiResult<?> putBuyerFromMypage(CommonPutBuyerFromMypageRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        userBuyerBiz.putBuyerFromMypage(dto);

        // 세션정보 반영
        if(!dto.getUserName().equals(buyerVo.getUserName())){
            buyerVo.setUserName(dto.getUserName());
        }
        if(!dto.getMobile().equals(buyerVo.getUserMobile())){
            buyerVo.setUserMobile(dto.getMobile());
        }
        if(!dto.getMail().equals(buyerVo.getUserEmail())){
            buyerVo.setUserEmail(dto.getMail());
        }

        SessionUtil.setUserVO(buyerVo);
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> getRefundBank(CommonGetRefundBankRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        dto.setUrUserId(buyerVo.getUrUserId());
        return ApiResult.success(userBuyerBiz.getRefundBank(dto));
    }

    @Override
    public ApiResult<?> putRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        dto.setUrUserId(buyerVo.getUrUserId());
        userBuyerBiz.putRefundBank(dto);
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> addRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        dto.setUrUserId(buyerVo.getUrUserId());
        CommonGetRefundBankRequestDto getRefundBankDto = new CommonGetRefundBankRequestDto();
		getRefundBankDto.setUrUserId(buyerVo.getUrUserId());
		CommonGetRefundBankResultVo refundBankVo = userBuyerBiz.getRefundBank(getRefundBankDto);
		if (refundBankVo != null) {
			dto.setUrRefundBankId(refundBankVo.getUrRefundBankId());
			userBuyerBiz.putRefundBank(dto);
		} else {
			userBuyerBiz.addRefundBank(dto);
		}
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> delRefundBank(Long urRefundBankId) throws Exception {
        userBuyerBiz.delRefundBank(urRefundBankId);
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> isValidationBankAccountNumber(String bankCode, String accountNumber, String holderName) throws Exception {
        if (!userBuyerBiz.isValidationBankAccountNumber(bankCode, accountNumber, holderName)) {
            return ApiResult.result(UserEnums.ValidationBankAccountReturnType.NO_VALIDATION_BANK_ACCOUNT);
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> getPointInfo() throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }

        return ApiResult.success(promotionPointBiz.getPointInfo(Long.valueOf(buyerVo.getUrUserId())));
    }

    @Override
    public ApiResult<?> getPointListByUser(CommonGetPointListByUserRequestDto dto) throws Exception {
        return promotionPointBiz.getPointListByUser(dto);
    }

    @Override
    public ApiResult<?> getPointExpectExpireList() throws Exception {
        return promotionPointBiz.getPointExpectExpireList();
    }

    @Override
    public ApiResult<?> getCouponListByUser(CouponListByUserRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        dto.setUrUserId(Long.parseLong(buyerVo.getUrUserId()));
        return ApiResult.success(promotionCouponBiz.getCouponListByUser(dto));
    }

    @Override
    public ApiResult<?> getCouponCoverage(Long pmCouponId) throws Exception {
        return ApiResult.success(promotionCouponBiz.getCouponCoverage(pmCouponId));
    }

    @Override
    public ApiResult<?> getAddPromotionPageInfo() throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(SerialEnums.AddPromotion.NEED_LOGIN);
        }
        buyerVo.setPromotionRecaptchaFailCount(0);
        SessionUtil.setUserVO(buyerVo);
        return ApiResult.success(0);
    }

    @Override
    public ApiResult<?> addPromotionByUser(String serialNumber, String captcha) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(SerialEnums.AddPromotion.NEED_LOGIN);
        }
        int failCount = userBuyerBiz.getPromotionRecaptchaFailCount();

        if (failCount >= 5) {
            if (!serialNumberBiz.checkCaptcha(captcha)) {
                return ApiResult.result(failCount, SerialEnums.AddPromotion.RECAPTCHA_FAIL);
            }
        }

        return serialNumberBiz.addPromotionByUser(serialNumber, Long.valueOf(buyerVo.getUrUserId()));
    }

    /**
     * 배송지 추가
     *
     * @param dto CommonSaveShippingAddressRequestDto
     * @throws Exception
     */
    @Override
    public ApiResult<?> addShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(SerialEnums.AddPromotion.NEED_LOGIN);
        }
        dto.setUrUserId(Long.parseLong(buyerVo.getUrUserId()));

        if (dto.getDefaultYn().equals("Y")) {
            if(userCertificationBiz.getSessionShipping().getSelectBasicYn().equals("Y")){
                AddSessionShippingRequestDto sessionDto = new AddSessionShippingRequestDto();
                sessionDto.setReceiverName(dto.getReceiverName());
                sessionDto.setReceiverZipCode(dto.getReceiverZipCode());
                sessionDto.setReceiverAddress1(dto.getReceiverAddress1());
                sessionDto.setReceiverAddress2(dto.getReceiverAddress2());
                sessionDto.setBuildingCode(dto.getBuildingCode());
                sessionDto.setReceiverMobile(dto.getReceiverMobile());
                sessionDto.setAccessInformationType(dto.getAccessInformationType());
                sessionDto.setAccessInformationPassword(dto.getAccessInformationPassword());
                sessionDto.setShippingComment(dto.getShippingComment());
                sessionDto.setSelectBasicYn(dto.getDefaultYn());
                sessionDto.setShippingAddressId(dto.getUrShippingAddrId());
                userCertificationBiz.addSessionShipping(sessionDto);
            }
        }

        return ApiResult.success(userBuyerBiz.addShippingAddress(dto));
    }

    /**
     * 배송지 수정
     *
     * @param dto CommonSaveShippingAddressRequestDto
     * @throws Exception
     */
    @Override
    public ApiResult<?> putShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }
        dto.setUrUserId(Long.parseLong(buyerVo.getUrUserId()));

        if(userCertificationBiz.getSessionShipping().getSelectBasicYn().equals("Y")){
            AddSessionShippingRequestDto sessionDto = new AddSessionShippingRequestDto();
            sessionDto.setReceiverName(dto.getReceiverName());
            sessionDto.setReceiverZipCode(dto.getReceiverZipCode());
            sessionDto.setReceiverAddress1(dto.getReceiverAddress1());
            sessionDto.setReceiverAddress2(dto.getReceiverAddress2());
            sessionDto.setBuildingCode(dto.getBuildingCode());
            sessionDto.setReceiverMobile(dto.getReceiverMobile());
            sessionDto.setAccessInformationType(dto.getAccessInformationType());
            sessionDto.setAccessInformationPassword(dto.getAccessInformationPassword());
            sessionDto.setShippingComment(dto.getShippingComment());
            sessionDto.setSelectBasicYn(dto.getDefaultYn());
            sessionDto.setShippingAddressId(dto.getUrShippingAddrId());
            userCertificationBiz.addSessionShipping(sessionDto);
        }

        return ApiResult.success(userBuyerBiz.putShippingAddress(dto));
    }

    /**
     * 배송지 삭제
     *
     * @param urShippingAddrId
     * @throws Exception
     */
    @Override
    public ApiResult<?> delShippingAddress(Long urShippingAddrId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }
        userBuyerBiz.delShippingAddress(urShippingAddrId);
        return ApiResult.success();
    }

    /**
     * 기본배송지 설정
     *
     * @param urShippingAddrId
     * @throws Exception
     */
    @Override
    public ApiResult<?> putShippingAddressSetDefault(Long urShippingAddrId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        userBuyerBiz.putShippingAddressSetDefault(Long.valueOf(buyerVo.getUrUserId()), urShippingAddrId);

        CommonGetShippingAddressRequestDto requestDto = new CommonGetShippingAddressRequestDto();
        requestDto.setUrShippingAddrId(String.valueOf(urShippingAddrId));
        CommonGetShippingAddressResultVo dto = userBuyerBiz.getShippingAddress(requestDto);
        if(userCertificationBiz.getSessionShipping().getSelectBasicYn().equals("Y")){
            AddSessionShippingRequestDto sessionDto = new AddSessionShippingRequestDto();
            sessionDto.setReceiverName(dto.getReceiverName());
            sessionDto.setReceiverZipCode(dto.getReceiverZipCode());
            sessionDto.setReceiverAddress1(dto.getReceiverAddress1());
            sessionDto.setReceiverAddress2(dto.getReceiverAddress2());
            sessionDto.setBuildingCode(dto.getBuildingCode());
            sessionDto.setReceiverMobile(dto.getReceiverMobile());
            sessionDto.setAccessInformationType(dto.getAccessInformationType());
            sessionDto.setAccessInformationPassword(dto.getAccessInformationPassword());
            sessionDto.setShippingComment(dto.getShippingComment());
            sessionDto.setSelectBasicYn("Y");
            sessionDto.setShippingAddressId(urShippingAddrId);
            userCertificationBiz.addSessionShipping(sessionDto);
        }

        return ApiResult.success();
    }

    /**
     * 배송지 주소 배송 정보 조회
     *
     * @param zipCode,buildingCode
     * @throws Exception
     */
    @Override
    public ApiResult<?> getShippingAddressPossibleDeliveryInfo(String zipCode, String buildingCode) throws Exception {
        return ApiResult.success(storeDeliveryBiz.getShippingAddressPossibleDeliveryInfo(zipCode, buildingCode));
    }

    @Override
    public ApiResult<?> getShippingAddressListFromMyPage(ShippingAddressListFromMyPageRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        return ApiResult.success(userBuyerBiz.getShippingAddressListFromMyPage(dto));
    }

    /**
     * 배송지 관련정보 조회
     *
     * @param
     * @throws Exception
     */
    @Override
    public ApiResult<?> getShippingAddressInfo() throws Exception {
        return userBuyerBiz.getShippingAddressInfo();
    }

    /**
     * 은행목록
     *
     * @param
     * @throws Exception
     */
    @Override
    public ApiResult<?> getRefundBankInfo() throws Exception {
        return userBuyerBiz.getRefundBankInfo();
    }

    /**
     * SNS 로그인 (네이버)
     *
     * @throws Exception
     */
    @Override
    public ApiResult<?> getUrlNaver() throws Exception {
        return userBuyerBiz.getUrlNaver();
    }

    /**
     * SNS 로그인 응답 (네이버)
     */
    public ApiResult<?> callbackNaver(HttpServletRequest request) throws Exception {
        return userBuyerBiz.callbackNaver(request);
    }

    /**
     * SNS 로그인 응답 (카카오)
     */
    public ApiResult<?> callbackKakao(UserSocialInformationDto userSocialInformationDto) throws Exception {
        return userBuyerBiz.callbackKakao(userSocialInformationDto);
    }

    /**
     * SNS 로그인 응답 (구글)
     */
    public ApiResult<?> callbackGoogle(UserSocialInformationDto userSocialInformationDto) throws Exception {
        return userBuyerBiz.callbackGoogle(userSocialInformationDto);
    }

    /**
     * SNS 로그인 응답 (페이스북)
     */
    public ApiResult<?> callbackFacebook(UserSocialInformationDto userSocialInformationDto) throws Exception {
        return userBuyerBiz.callbackFacebook(userSocialInformationDto);
    }

    /**
     * SNS 로그인 응답 (애플)
     */
    public ApiResult<?> callbackApple(UserSocialInformationDto userSocialInformationDto) throws Exception {
        return userBuyerBiz.callbackApple(userSocialInformationDto);
    }

    /**
     * SNS 로그인 계정 연결끊기
     */
    public ApiResult<?> delSyncAccount(String urSocialId, String provider) throws Exception {

        return userBuyerBiz.delSyncAccount(urSocialId, provider);
    }

    /**
     * 쿠폰 수량 조회
     *
     * @param status String
     * @return CommonGetCouponCountByUserVo
     * @throws Exception Exception
     */
    @Override
    public ApiResult<?> getCouponCountByUser(String status) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        return ApiResult.success(promotionCouponBiz.getCouponCountByUser(Long.valueOf(buyerVo.getUrUserId()), status));
    }

    @Override
    public ApiResult<?> getGroupInfoByUser() throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        return ApiResult.success(userGroupBiz.getGroupInfoByUser(Long.valueOf(buyerVo.getUrUserId())));
    }

    @Override
    public ApiResult<?> getFeedbackInfoByUser() throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }

        return ApiResult.success(customerFeedbackBiz.getFeedbackInfoByUser(Long.valueOf(buyerVo.getUrUserId()), buyerVo.getUrGroupId()));
    }

    /**
     * 상품구매후기 대상 목록조회
     *
     * @param page,limit
     * @throws Exception
     */
    @Override
    public ApiResult<?> getFeedbackTargetListByUser(int page, int limit) throws Exception {

        FeedbackTargetListByUserRequestDto paramDto = new FeedbackTargetListByUserRequestDto();
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        paramDto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        paramDto.setPage(page);
        paramDto.setLimit(limit);
        paramDto.setUrGroupId(buyerVo.getUrGroupId());

        return ApiResult.success(customerFeedbackBiz.getFeedbackTargetListByUser(paramDto));
    }

    /**
     * 나의상품구매후기 목록조회 2020-09-24
     *
     * @param dto FeedbackByUserRequestDto
     * @return ApiResult<?>
     * @throws Exception Exception
     */
    @Override
    public ApiResult<?> getFeedbackByUser(FeedbackByUserRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        return ApiResult.success(customerFeedbackBiz.getFeedbackByUser(dto));
    }

    /**
     * 상품구매후기 등록
     *
     * @param dto AddFeedbackRequestDto
     * @throws Exception Exception
     */
    @Override
    public ApiResult<?> addFeedback(FeedbackRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        dto.setUrGroupId(buyerVo.getUrGroupId());
        return customerFeedbackBiz.addFeedback(dto);
    }

    /**
     * 1:1문의 현항 조회
     *
     * @param dto QnaInfoByUserRequestDto
     * @return QnaInfoByUserVo
     * @throws Exception Exception
     */
    @Override
    public ApiResult<?> getQnaInfoByUser(QnaInfoByUserRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }
        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));

        return ApiResult.success(customerQnaBiz.getQnaInfoByUser(dto));
    }

    /**
     * 1:1문의 목록 조회
     *
     * @param dto QnaListByUserRequestDto
     * @return QnaListByUserVo
     * @throws Exception Exception
     */
    @Override
    public ApiResult<?> getQnaListByUser(QnaListByUserRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }
        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));

        return ApiResult.success(customerQnaBiz.getQnaListByUser(dto));
    }

    /**
     * 상품 문의 목록 조회
     *
     * @param dto ProductQnaListByUserRequestDto
     * @return ProductQnaListByUserResponseDto
     * @throws Exception Exception
     */
    @Override
    public ApiResult<?> getProductQnaListByUser(ProductQnaListByUserRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }
        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));

        return ApiResult.success(customerQnaBiz.getProductQnaListByUser(dto));
    }

    /**
     * 문의 비공개 처리 - 고객
     *
     * @param csQnaId Long
     * @return ApiResult<?>
     * @throws Exception Exception
     */
    @Override
    public ApiResult<?> putProductQnaSetSecretByUser(Long csQnaId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        return customerQnaBiz.putProductQnaSetSecretByUser(csQnaId, Long.valueOf(buyerVo.getUrUserId()));
    }

    @Override
    public ApiResult<?> putQnaAnswerUserCheckYn(Long csQnaId) throws Exception {
        customerQnaBiz.putQnaAnswerUserCheckYn(csQnaId);
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> getEventListFromMyPage(EventListFromMyPageRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        dto.setDeviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode());
        return ApiResult.success(promotionEventBiz.getEventListFromMyPage(dto));
    }

    @Override
    public ApiResult<?> getEmployeeDiscount() throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        return ApiResult.success(policyBenefitEmployeeBiz.getEmployeeDiscountBrandByUser(buyerVo.getUrErpEmployeeCode()));
    }

    @Override
    public ApiResult<?> getEmployeeDiscountPastInfo(String searchDate) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        LocalDate localDate = LocalDate.parse(searchDate + "-01");
        return ApiResult.success(policyBenefitEmployeeBiz.getEmployeeDiscountPastByUser(buyerVo.getUrErpEmployeeCode(), searchDate + "-01", searchDate + "-" + localDate.lengthOfMonth()));
    }

    @Override
    public CodeCommEnum getUserStatus(BuyerVo buyerVo) {
        String urUserId = (buyerVo != null) ? StringUtil.nvl(buyerVo.getUrUserId()) : "";
        boolean isMember = StringUtil.isNotEmpty(urUserId);
        boolean isEmployee = (buyerVo != null) && StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());

        CodeCommEnum userStatus = UserEnums.UserStatusType.NONMEMBER;
        if (isMember) userStatus = UserEnums.UserStatusType.MEMBER;
        if (isEmployee) userStatus = UserEnums.UserStatusType.EMPLOYEE;

        return userStatus;
    }

    @Override
    public ApiResult<?> getDropPageInfo() throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        DropPageResponseDto result = new DropPageResponseDto();
        result.setReason(userMoveReasonBiz.getMoveReasonCode());

        CouponCountByUserVo couponCountByUserVo = promotionCouponBiz.getCouponCountByUser(Long.valueOf(buyerVo.getUrUserId()), CouponEnums.CouponStatus.NOTUSE.getCode());
        result.setCouponCount(couponCountByUserVo.getGoodsCount() + couponCountByUserVo.getCartCount() + couponCountByUserVo.getShippingPriceCount());

        GetPointInfoResponseDto getPointInfoResponseDto = promotionPointBiz.getPointInfo(Long.valueOf(buyerVo.getUrUserId()));
        result.setPointUsable(getPointInfoResponseDto.getPointUsable());

        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> progressUserDrop(UserDropRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        dto.setCreateId(Long.valueOf(buyerVo.getUrUserId()));


        ApiResult<?> res = userBuyerDropBiz.progressUserDrop(dto);

		if (res.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
			//세션 초기화
			SessionUtil.setUserVO(new BuyerVo());
		}
        return res;
    }

    @Override
    public ApiResult<?> getMypageMainInfo() throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        MypageMainInfoResponseDto result = new MypageMainInfoResponseDto();

        //회원 등급 정보
        GroupInfoByUserResultVo group = userGroupBiz.getGroupByUser(Long.valueOf(buyerVo.getUrUserId()));
        result.setGroupName(group.getGroupName());
        result.setTopImagePath(group.getTopImagePath());

        //적립금 정보
        GetPointInfoResponseDto getPointInfoResponseDto = promotionPointBiz.getPointInfo(Long.valueOf(buyerVo.getUrUserId()));
        result.setPointUsable(getPointInfoResponseDto.getPointUsable());

        //쿠폰 정보
        CouponCountByUserVo couponCountByUserVo = promotionCouponBiz.getCouponCountByUser(Long.valueOf(buyerVo.getUrUserId()), CouponEnums.CouponStatus.NOTUSE.getCode());
        result.setCouponCount(couponCountByUserVo.getCouponTotalCount());

        //주문 정보
        String startDate = LocalDate.now().minusMonths(BuyerConstants.MYPAGE_MAIN_MONTH).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00";
        String endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59";
        OrderCountFromMyPageVo orderVo = orderFrontBiz.getOrderCountFromMyPage(Long.valueOf(buyerVo.getUrUserId()), startDate, endDate);
        result.setDepositReadyCount(orderVo.getDepositReadyCount());
        result.setDepositCompleteCount(orderVo.getDepositCompleteCount());
        result.setDeliveryReadyCount(orderVo.getDeliveryReadyCount());
        result.setDeliveryDoingCount(orderVo.getDeliveryDoingCount());
        result.setDeliveryCompleteCount(orderVo.getDeliveryCompleteCount());
        result.setOrderCancelCount(orderVo.getOrderCancelCount());
        result.setOrderReturnRefundCount(orderVo.getOrderReturnRefundCount());

        //배송 정보
        result.setNormalDeliveryCount(orderVo.getNormalCount());
        result.setDailyDeliveryCount(orderVo.getDailyCount());
        result.setRegularDeliveryCount(orderVo.getRegularCount());
        result.setStoreDeliveryCount(orderVo.getShopCount());

        //후기 정보
        FeedbackTargetListByUserRequestDto feedbackDto = new FeedbackTargetListByUserRequestDto();
        feedbackDto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        feedbackDto.setPage(1);
        feedbackDto.setLimit(3);
        FeedbackTargetListByUserResponseDto feedbackResponseDto = customerFeedbackBiz.getFeedbackTargetListByUser(feedbackDto);
        result.setFeedbackTargetCount(feedbackResponseDto.getTotal());
        result.setFeedback(feedbackResponseDto.getList());

        //1:1문의
        QnaListByUserRequestDto qnaRequestDto = new QnaListByUserRequestDto();
        qnaRequestDto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        qnaRequestDto.setStartDate(startDate);
        qnaRequestDto.setEndDate(endDate);
        qnaRequestDto.setPage(1);
        qnaRequestDto.setLimit(3);
        QnaListByUserResponseDto qnaResponseDto = customerQnaBiz.getQnaListByUser(qnaRequestDto);
        result.setOnetoone(qnaResponseDto.getQnaList());
        if(qnaResponseDto.getQnaList().size() > 0){
            qnaResponseDto.getQnaList().forEach(vo -> {
                if(vo.getUserCheckYn() != null && vo.getUserCheckYn().equals("N")){
                    result.setOnetooneNewAnswerYn("Y");
                }
            });
        }

        //상품문의
        ProductQnaListByUserRequestDto productRequestDto = new ProductQnaListByUserRequestDto();
        productRequestDto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        productRequestDto.setStartDate(startDate);
        productRequestDto.setEndDate(endDate);
        productRequestDto.setPage(1);
        productRequestDto.setLimit(3);
        ProductQnaListByUserResponseDto productResponseDto = customerQnaBiz.getProductQnaListByUser(productRequestDto);
        result.setProduct(productResponseDto.getList());
        productResponseDto.getList().forEach(vo -> {
            if(vo.getUserCheckYn() != null && vo.getUserCheckYn().equals("N")){
                result.setProductNewAnswerYn("Y");
            }
        });

        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> getQnaNewAnswer() throws Exception {
        QnaNewAnswerResponseDto result = new QnaNewAnswerResponseDto();

        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.success(result);
        }

        //1:1문의
        String startDate = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00";
        String endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59";
        QnaListByUserRequestDto qnaRequestDto = new QnaListByUserRequestDto();
        qnaRequestDto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        qnaRequestDto.setStartDate(startDate);
        qnaRequestDto.setEndDate(endDate);
        qnaRequestDto.setPage(1);
        qnaRequestDto.setLimit(3);

        QnaListByUserResponseDto qnaResponseDto = customerQnaBiz.getQnaListByUser(qnaRequestDto);
        if(qnaResponseDto.getQnaList().size() > 0){
            qnaResponseDto.getQnaList().forEach(vo -> {
                if(vo.getUserCheckYn() != null && vo.getUserCheckYn().equals("N")){
                    result.setOnetooneNewAnswerYn("Y");
                }
            });
        }

        //상품문의
        ProductQnaListByUserRequestDto productRequestDto = new ProductQnaListByUserRequestDto();
        productRequestDto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        productRequestDto.setStartDate(startDate);
        productRequestDto.setEndDate(endDate);
        productRequestDto.setPage(1);
        productRequestDto.setLimit(3);

        ProductQnaListByUserResponseDto productResponseDto = customerQnaBiz.getProductQnaListByUser(productRequestDto);
        productResponseDto.getList().forEach(vo -> {
            if (vo.getUserCheckYn() != null && "N".equals(vo.getUserCheckYn())) {
                result.setProductNewAnswerYn("Y");
            }
        });

        //고객보상제
        RewardApplyRequestDto rewardRequestDto = new RewardApplyRequestDto();
        rewardRequestDto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        rewardRequestDto.setStartDate(startDate);
        rewardRequestDto.setEndDate(endDate);
        rewardRequestDto.setPage(1);
        rewardRequestDto.setLimit(999);
        RewardApplyResponseDto rewardResponseDto = customerRewardMallBiz.getRewardApplyList(rewardRequestDto);
        for (RewardApplyListVo vo : rewardResponseDto.getReward()) {
            if (CustomerEnums.RewardApplyStatus.COMPLETE.getCode().equals(vo.getRewardApplyStatus()) ||
                    CustomerEnums.RewardApplyStatus.IMPOSSIBLE.getCode().equals(vo.getRewardApplyStatus())) {
                if ("N".equals(vo.getUserCheckYn())) {
                    result.setRewardNewAnswerYn("Y");
                    break;
                }
            }
        }

        return ApiResult.success(result);
    }

    /**
     * 마이페이지 배송지 목록 조회
     *
     * @param  ilGoodsId
     * @param  odOrderDetlId
     * @return List<CommonGetShippingAddressListResultVo>
     * @throws Exception
     */
    public ApiResult<?> getMypageShippingAddressList(long ilGoodsId, long odOrderDetlId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            log.info("====0001 로그인필요===");
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        return ApiResult.success(userBuyerBiz.getMypageShippingAddressList(Long.parseLong(buyerVo.getUrUserId()),ilGoodsId,  odOrderDetlId));
    }

    public ApiResult<?> getAsisUserPoint(AsisUserPointRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        return userBuyerBiz.getAsisUserPoint(dto);
    }

    @Override
    public ApiResult<?> depositPointByAsisPoint(AsisUserPointRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }
        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));

        return userBuyerBiz.depositPointByAsisPoint(dto);
    }

    @Override
    public ApiResult<?> isPossibleChangeDeliveryAddress(IsPossibleChangeDeliveryAddressRequestDto reqDto) throws Exception{
    	return userBuyerBiz.isPossibleChangeDeliveryAddress(reqDto);
    }

}