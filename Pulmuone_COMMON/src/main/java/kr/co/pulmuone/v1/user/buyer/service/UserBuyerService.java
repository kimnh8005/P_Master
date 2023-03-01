package kr.co.pulmuone.v1.user.buyer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.user.buyer.UserBuyerShippingAddrMapper;
import kr.co.pulmuone.v1.comm.util.asis.AsisUserApiUtil;
import kr.co.pulmuone.v1.order.delivery.service.ShippingZoneBiz;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.certification.dto.AddSessionShippingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.base.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.mapper.order.registration.OrderRegistrationMapper;
import kr.co.pulmuone.v1.comm.mapper.user.buyer.UserBuyerMapper;
import kr.co.pulmuone.v1.comm.util.SHA256Util;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.comm.util.inicis.AccountValidation;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneHistVo;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.shipping.service.OrderShippingBiz;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import kr.co.pulmuone.v1.user.buyer.dto.vo.BuyerFromMypageResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetRefundBankResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetShippingAddressListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetShippingAddressResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetChangeClauseNoAgreeListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetSearchWordResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetShippingAddressListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.ShippingAddressListFromMyPageResultVo;
import kr.co.pulmuone.v1.user.certification.dto.GetClauseArrayRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionUserSnsCertificationResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSocialLoginDataRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetLoginDataResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetSocialLoginDataResultVo;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.join.dto.SaveBuyerRequestDto;
import kr.co.pulmuone.v1.user.join.service.UserJoinBiz;
import kr.co.pulmuone.v1.user.login.service.UserSnsLoginBiz;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200625    	  천혜현           최초작성
 * 1.1    20200826    	  이원호           환불계좌 삭제, 마이페이지 회원정보 관리 API 추가
 * =======================================================================
 * </PRE>
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserBuyerService {

    @Autowired
    private UserBuyerMapper      userBuyerMapper;

    @Autowired
    private UserBuyerShippingAddrMapper userBuyerShippingAddrMapper;


    @Autowired
    private OrderRegistrationMapper orderRegistrationMapper;

    @Autowired
    private UserSnsLoginBiz      userSnsLoginBiz;

    @Autowired
    private UserJoinBiz          userJoinBiz;

    @Autowired
    private OrderShippingBiz     orderShippingBiz;

    @Autowired
    private UserCertificationBiz userCertificationBiz;

    @Autowired
    private AccountValidation    accountValidation;

    @Value("${database.encryption.key}")
    private String               databaseEncryptionKey;

    @Autowired
    private OrderOrderBiz        orderOrderBiz;

    @Autowired
    public StoreDeliveryBiz 	   storeDeliveryBiz;

    private final AsisUserApiUtil asisUserApiUtil;
    private final PointBiz pointBiz;

    @Autowired
    private ShippingZoneBiz shippingZoneBiz;

    /**
     * 이메일 중복체크
     *
     * @param dto
     * @return CommonDuplicateMailResponseDto
     * @throws Exception
     */
    protected int checkDuplicateMail(CommonDuplicateMailRequestDto dto) throws Exception {
        return userBuyerMapper.checkDuplicateMail(dto);
    }

    /**
     * 환불계좌 조회
     *
     * @param dto
     * @return CommonGetRefundBankResponseDto
     * @throws Exception
     */
    protected CommonGetRefundBankResultVo getRefundBank(CommonGetRefundBankRequestDto dto) throws Exception {

        return userBuyerMapper.getRefundBank(dto);
    }

    /**
     * 환불계좌 수정
     *
     * @param CommonSaveRefundBankRequestDto
     * @throws Exception
     */
    protected int putRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception {
    	return userBuyerMapper.putRefundBank(dto);
    }

    /**
     * 환불계좌 추가
     *
     * @param CommonSaveRefundBankRequestDto
     * @throws Exception
     */
    protected int addRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception {
        return userBuyerMapper.addRefundBank(dto);
    }

    /**
     * 환불계좌 삭제
     *
     * @param urRefundBankId Long
     * @throws Exception exception
     */
    protected void delRefundBank(Long urRefundBankId) throws Exception {
        userBuyerMapper.delRefundBank(urRefundBankId);
    }

    /**
     * 계좌 유효 인증 체크
     *
     * @param bankCode String
     * @param accountNumber String
     * @param holderName String
     * @return boolean
     * @throws Exception Exception
     */
    protected boolean isValidationBankAccountNumber(String bankCode, String accountNumber, String holderName) throws Exception {
        return accountValidation.accountNameValidation(bankCode, accountNumber, holderName);
    }

    /**
     * 배송지 리스트조회
     *
     * @param urUserId
     * @return CommonGetShippingAddressListResponseDto
     * @throws Exception
     */
    protected CommonGetShippingAddressListResponseDto getShippingAddressList(String urUserId) throws Exception {
        CommonGetShippingAddressListResponseDto result = new CommonGetShippingAddressListResponseDto();

        int total = userBuyerMapper.getShippingAddressListCount(urUserId); // total
        List<CommonGetShippingAddressListResultVo> rows = userBuyerMapper.getShippingAddressList(urUserId); // rows

        result.setTotal(total);
        result.setRows(rows);

        return result;
    }

    /**
     * 배송지 단일조회
     *
     * @param dto
     * @return CommonGetShippingAddressResponseDto
     * @throws Exception
     */
    protected CommonGetShippingAddressResultVo getShippingAddress(CommonGetShippingAddressRequestDto dto) throws Exception {

        return userBuyerMapper.getShippingAddress(dto);
    }

    /**
     * 배송지 등록
     *
     * @param dto
     * @return CommonSaveShippingAddressResponseDto
     * @throws Exception
     */
    protected int addShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception {
        // 등록하는 배송지가 기본배송지일때, 회원의 나머지 배송지는 기본아닌 배송지로 업데이트
        if (dto.getDefaultYn().equals("Y")) {
            userBuyerMapper.putShippingAddressDefault(dto);
        }

        return userBuyerMapper.addShippingAddress(dto);
    }

    /**
     * 배송지 수정
     *
     * @param dto
     * @throws Exception
     */
    protected int putShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception {

        // 등록하는 배송지가 기본배송지일때, 회원의 나머지 배송지는 기본아닌 배송지로 업데이트
        if ("Y".equals(dto.getDefaultYn()) && dto.getSelectBasicYn() == null) {
            userBuyerMapper.putShippingAddressDefault(dto);
        }

        return userBuyerMapper.putShippingAddress(dto);
    }

    /**
     * 광고성 수신 SMS 업데이트
     *
     * @param urUserId
     * @return int
     * @throws Exception
     */
    protected int putMaketingReceiptAgreementSms(String urUserId) throws Exception {

        int i = userBuyerMapper.putMaketingReceiptAgreementSms(urUserId);

        return i;
    }

    /**
     * 광고성 수신 EMAIL 업데이트
     *
     * @param urUserId
     * @return int
     * @throws Exception
     */
    protected int putMaketingReceiptAgreementMail(String urUserId) throws Exception {
        int i = userBuyerMapper.putMaketingReceiptAgreementMail(urUserId);
        return i;
    }

    /**
     * 약관동의저장
     *
     * @param request,HttpServletRequest,HttpServletResponse
     * @return AddChangeClauseAgreeResponseDto
     * @throws Exception
     */
    protected ApiResult<?> addChangeClauseAgree(AddChangeClauseAgreeRequestDto addChangeClauseAgreeRequestDto,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        AddChangeClauseAgreeResponseDataDto addChangeClauseAgreeResponseDataDto = new AddChangeClauseAgreeResponseDataDto();

        DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();

        // SNS 로그인시 처리 수정 처리 필요
        if (StringUtil.isEmpty(addChangeClauseAgreeRequestDto.getLoginId())
                && StringUtil.isEmpty(addChangeClauseAgreeRequestDto.getPassword())) {

            GetSessionUserSnsCertificationResponseDto getSessionUserSnsCertificationResponseDto =
                    userSnsLoginBiz.getSessionUserSnsCertification();
            if (getSessionUserSnsCertificationResponseDto == null
                    || StringUtil.isEmpty(getSessionUserSnsCertificationResponseDto.getSocialId())) {
                // SNS 새션 없음
                return ApiResult.result(UserEnums.Join.NO_SNS_SESSION);
            } else {
                GetSocialLoginDataRequestDto getSocialLoginDataRequestDto = new GetSocialLoginDataRequestDto();
                getSocialLoginDataRequestDto.setProvider(getSessionUserSnsCertificationResponseDto.getProvider());
                getSocialLoginDataRequestDto.setSocialId(getSessionUserSnsCertificationResponseDto.getSocialId());

                GetSocialLoginDataResultVo getSocialLoginDataResultVo =
                        userCertificationBiz.getSocialLoginData(getSocialLoginDataRequestDto);

                // 기존 SNS회원일때 로그인처리
                if (getSocialLoginDataResultVo != null) {
                    doLoginRequestDto.setLoginId(getSocialLoginDataResultVo.getLoginId());
                    doLoginRequestDto.setEncryptPassword(getSocialLoginDataResultVo.getEncryptPassword());
                } else {
                    return ApiResult.result(UserEnums.Join.NO_SNS_SYNC);
                }
            }
        } else {
            doLoginRequestDto.setLoginId(addChangeClauseAgreeRequestDto.getLoginId());
            doLoginRequestDto.setPassword(addChangeClauseAgreeRequestDto.getPassword());
            doLoginRequestDto.setEncryptPassword(SHA256Util.getUserPassword(addChangeClauseAgreeRequestDto.getPassword()));
        }

        if (StringUtil.isNotEmpty(addChangeClauseAgreeRequestDto.getAutoLogin())) {
            doLoginRequestDto.setAutoLogin(addChangeClauseAgreeRequestDto.getAutoLogin().equals("Y"));
        }
        if (StringUtil.isNotEmpty(addChangeClauseAgreeRequestDto.getSaveLoginId())) {
            doLoginRequestDto.setSaveLoginId(addChangeClauseAgreeRequestDto.getSaveLoginId().equals("Y"));
        }

        int total = userCertificationBiz.getisCheckLogin(doLoginRequestDto);

        if (total == 0) {
            // 1208 로그인 실패
            return ApiResult.result(UserEnums.Join.LOGIN_FAIL);
        } else {
            GetLoginDataResultVo getLoginDataResultVo = userCertificationBiz.getLoginData(doLoginRequestDto);

            SaveBuyerRequestDto saveBuyerRequestDto = new SaveBuyerRequestDto();
            GetClauseArrayRequestDto[] arrayExecuteDateArr = addChangeClauseAgreeRequestDto.getClause();

            if (arrayExecuteDateArr != null) {
                saveBuyerRequestDto.setUrUserId(getLoginDataResultVo.getUrUserId());
                for (int i = 0; i < arrayExecuteDateArr.length; i++) {
                    saveBuyerRequestDto.setPsClauseGrpCd(arrayExecuteDateArr[i].getPsClauseGrpCd());
                    saveBuyerRequestDto.setExecuteDate(arrayExecuteDateArr[i].getExecuteDate());
                    userJoinBiz.addUrClauseAgreeLog(saveBuyerRequestDto);
                }
            }

            // 로그인 처리
            ApiResult<?> result = userCertificationBiz.doLogin(doLoginRequestDto, false, request, response);
            DoLoginResponseDto doLoginResponseDto = (DoLoginResponseDto) result.getData();
            addChangeClauseAgreeResponseDataDto.setUrUserId(doLoginResponseDto.getUrUserId());
            if (doLoginResponseDto.getMaketting() != null) {
                addChangeClauseAgreeResponseDataDto.setMaketting(doLoginResponseDto.getMaketting());
            }
            if (doLoginResponseDto.getNoti() != null) {
                addChangeClauseAgreeResponseDataDto.setNoti(doLoginResponseDto.getNoti());
            }
            if (doLoginResponseDto.getAutoLogin() != null) {
            	addChangeClauseAgreeResponseDataDto.setAutoLogin(doLoginResponseDto.getAutoLogin());
            }
            return ApiResult.success(addChangeClauseAgreeResponseDataDto);
        }
    }

    /**
     * 기본배송지 목록
     *
     * @param databaseEncryptionKey,urUserId
     * @return GetShippingAddressListResultVo
     * @throws Exception
     */
    protected GetShippingAddressListResultVo getBasicShippingAddress(String databaseEncryptionKey,
                                                                     String urUserId) throws Exception {
        return userBuyerMapper.getBasicShippingAddress(databaseEncryptionKey, urUserId);
    }

    /**
     * 배송지 목록 조회
     *
     * @param getShippingAddressListRequestDto
     * @return GetShippingAddressListResponseDto
     * @throws Exception
     */
    protected ApiResult<?> getShippingAddressList(GetShippingAddressListRequestDto getShippingAddressListRequestDto) throws Exception {
        List<GetShippingAddressListResultVo> getCartShippingAddressListResultVo =
                userBuyerMapper.getShippingAddressLimitList(getShippingAddressListRequestDto);

        if (getCartShippingAddressListResultVo != null) {
            return ApiResult.success(getCartShippingAddressListResultVo);
        } else {
            return ApiResult.fail();
        }
    }

    /**
     * 장바구니 배송지 목록 조회
     *
     * @param
     * @return GetShippingAddressListResponseDto
     * @throws Exception
     */
    protected ApiResult<?> getCartShippingAddressList() throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        List<GetShippingAddressListResultVo> recentAddressResponse = new ArrayList<>();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            log.info("====0001 로그인필요===");
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        } else {
            // 기본배송지
            String urUserId = buyerVo.getUrUserId();
            GetShippingAddressListResultVo basicAddressResult = getBasicShippingAddress(databaseEncryptionKey, urUserId);
            basicAddressResult.setDelivery(storeDeliveryBiz.getShippingAddressPossibleDeliveryInfo(basicAddressResult.getReceiverZipCode(), basicAddressResult.getBuildingCode()));
            basicAddressResult.setShippingAddressType(DeliveryEnums.ShippingType.BASIC_SHIPPING_TYPE.getCode());
            basicAddressResult.setShippingAddressTypeName(DeliveryEnums.ShippingType.BASIC_SHIPPING_TYPE.getCodeName());
            recentAddressResponse.add(basicAddressResult);

            // 최근배송지
            GetRecentlyShippingAddressListRequestDto getRecentlyShippingAddressListRequestDto = new GetRecentlyShippingAddressListRequestDto();
            getRecentlyShippingAddressListRequestDto.setUrUserId(urUserId);
            getRecentlyShippingAddressListRequestDto.setDatabaseEncryptionKey(databaseEncryptionKey);
            getRecentlyShippingAddressListRequestDto.setLimitCount(5);
            List<GetShippingAddressListResultVo> recentAddressResult = orderShippingBiz.getRecentlyShippingAddressList(getRecentlyShippingAddressListRequestDto);
            for (GetShippingAddressListResultVo vo2 : recentAddressResult) {
                vo2.setShippingAddressType(DeliveryEnums.ShippingType.RECENT_SHIPPING_TYPE.getCode());
                vo2.setShippingAddressTypeName(DeliveryEnums.ShippingType.RECENT_SHIPPING_TYPE.getCodeName());
                vo2.setDelivery(storeDeliveryBiz.getShippingAddressPossibleDeliveryInfo(vo2.getReceiverZipCode(), vo2.getBuildingCode()));
                recentAddressResponse.add(vo2);
            }

            return ApiResult.success(recentAddressResponse);
        }

    }

    /**
     * 배송지 목록 조회
     *
     * @param
     * @return GetShippingAddressListResponseDto
     * @throws Exception
     */
    protected ApiResult<?> getShippingAddressList() throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            log.info("====0001 로그인필요===");
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        } else {
            String urUserId = buyerVo.getUrUserId();
            GetShippingAddressListRequestDto getShippingAddressListRequestDto = new GetShippingAddressListRequestDto();
            getShippingAddressListRequestDto.setUrUserId(urUserId);
            getShippingAddressListRequestDto.setDatabaseEncryptionKey(databaseEncryptionKey);
            getShippingAddressListRequestDto.setLimitCount(5);
            return getShippingAddressList(getShippingAddressListRequestDto);
        }

    }

    /**
     * 최근 검색어 삭제
     *
     * @param urUserSearchWordLogId<Integer>
     * @return DelUserSearchWordLogResponseDto
     * @throws Exception
     */
    protected ApiResult<?> delUserSearchWordLog(List<Integer> urUserSearchWordLogId) throws Exception {
        userBuyerMapper.delUserSearchWordLog(urUserSearchWordLogId);

        return ApiResult.success();
    }

    /**
     * 최근검색어 관련 레이아웃 관련 정보 (PC,MO)
     *
     * @param urUserId
     * @return List<GetSearchWordResultVo>
     * @throws Exception
     */
    protected List<GetSearchWordResultVo> getUserSearchWordLogList(String urUserId) throws Exception {

        return userBuyerMapper.getUserSearchWordLogList(urUserId);
    }

    /**
     * 공통코드 조회 kkm 20200915
     *
     * @param
     * @return ResponseDto<GetLatestClauseResponseDto>
     * @throws Exception
     */
    protected CommonGetCodeListResponseDto getCommonCode(GetCodeListRequestDto getCodeListRequestDto) throws Exception {
        CommonGetCodeListResponseDto result = new CommonGetCodeListResponseDto();

        List<CodeInfoVo> rows = userBuyerMapper.getCommonCode(getCodeListRequestDto);

        result.setRows(rows);
        return result;
    }

    /**
     * 회원 최근검색어 추가
     *
     * @param keyword
     * @return
     * @throws Exception
     */
    protected void addUserSearchWordLog(String keyword, String urUserId) throws Exception {
        HashMap<String, String> addUserSearchMap = new HashMap<>();
        addUserSearchMap.put("keyword", keyword);
        addUserSearchMap.put("urUserId", urUserId);

        userBuyerMapper.addUserSearchWordLog(addUserSearchMap);
    }

    protected List<GetChangeClauseNoAgreeListResultVo> getChangeClauseNoAgreeList(String urUserId) throws Exception {

        return userBuyerMapper.getChangeClauseNoAgreeList(urUserId);

    }

    /**
     * 신규회원특가 쿠폰 조회
     *
     * @param ilGoodsId, urUserId, deviceInfo, isApp
     * @return HashMap
     * @throws Exception
     */
    protected int getNewBuyerSpecialsCouponByUser(Long ilGoodsId, String urUserId, String deviceInfo, boolean isApp) throws Exception {
        Integer result = userBuyerMapper.getNewBuyerSpecialsCouponByUser(ilGoodsId, urUserId, deviceInfo, isApp);
        if(result == null){
            return 0;
        }
        return result;
    }

    /**
     * 마이페이지 회원정보 조회
     *
     * @param urUserId Long
     * @return GetBuyerFromMypageResultVo
     * @throws Exception exception
     */
    protected BuyerFromMypageResultVo getBuyerFromMypage(Long urUserId) throws Exception {
        return userBuyerMapper.getBuyerFromMypage(urUserId);
    }

    /**
     * 마이페이지 회원정보 수정
     *
     * @param dto PutBuyerFromMypageRequestDto
     * @throws Exception exception
     */
    protected void putBuyerFromMypage(CommonPutBuyerFromMypageRequestDto dto) throws Exception {
        userBuyerMapper.putUserFromMypage(dto);
        userBuyerMapper.putBuyerFromMypage(dto);
    }

    /**
     * 배송지 삭제
     *
     * @param urShippingAddrId Long
     * @throws Exception Exception
     */
    protected void delShippingAddress(Long urShippingAddrId) throws Exception {
        userBuyerMapper.delShippingAddress(urShippingAddrId);
    }

    /**
     * 기본배송지 설정
     *
     * @param urShippingAddrId Long
     * @return CommonSaveShippingAddressResponseDto
     * @throws Exception Exception
     */
    protected void putShippingAddressSetDefault(Long urUserId, Long urShippingAddrId) throws Exception {
        userBuyerMapper.putShippingAddressSetInit(urUserId);
        userBuyerMapper.putShippingAddressSetDefault(urShippingAddrId);

    }

    /**
     * 배송지관리 목록 조회
     *
     * @param dto ShippingAddressListFromMyPageRequestDto
     * @return ShippingAddressListFromMyPageResponseDto
     * @throws Exception Exception
     */
    protected ShippingAddressListFromMyPageResponseDto getShippingAddressListFromMyPage(ShippingAddressListFromMyPageRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<ShippingAddressListFromMyPageResultVo> result = userBuyerMapper.getShippingAddressListFromMyPage(dto);
        return ShippingAddressListFromMyPageResponseDto.builder()
                .total((int)result.getTotal())
                .rows(result.getResult())
                .build();
    }

    /**
     * 프로모션 recaptcha 실패 횟수
     *
     * @return int
     * @throws Exception Exception
     */
    protected int getPromotionRecaptchaFailCount() throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (!Objects.isNull(buyerVo.getPromotionRecaptchaFailCount())) {
            return buyerVo.getPromotionRecaptchaFailCount();
        }
        return 0;
    }

    /**
     * 프로모션 recaptcha 실패 횟수 저장
     *
     * @param count int
     * @throws Exception Exception
     */
    protected void putPromotionRecaptchaFailCount(int count) throws Exception {
        if (count > 5)
            count = 5;
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        buyerVo.setPromotionRecaptchaFailCount(count);
        SessionUtil.setUserVO(buyerVo);
    }

    /**
     * 회원 사용 카드 정보 조회
     *
     * @param	urUserId
     * @return HashMap
     * @throws Exception
     */
    protected HashMap<String,String> getUserPaymentInfo(String urUserId) throws Exception {
        return userBuyerMapper.getUserPaymentInfo(urUserId);
    }

	/**
	 * 회원 사용 결제정보 저장
	 *
	 * @param urUserId
	 * @param psPayCd
	 * @param cardCode
	 * @throws Exception
	 */
	protected void putUserPaymentInfo(Long urUserId, String psPayCd, String cardCode) throws Exception {
		userBuyerMapper.putUserPaymentInfo(urUserId, psPayCd, cardCode);
	}

	/**
	 * @Desc 배송지 목록에서 주문배송지로 설정
	 * @param shippingAddressId
	 * @param urUserId
	 * @throws Exception
	 * @return int
	 */
	protected int putShippingAddressIntoOrderShippingZone(CommonSaveShippingAddressRequestDto dto) throws Exception{
		return userBuyerMapper.putShippingAddressIntoOrderShippingZone(dto);
	}

	/**
	 * @Desc 배송지 목록에서 기본배송지 변경 시 변경이력 저장
	 * @param orderShippingZoneHistVo
	 * @return void
	 */
	protected int addShippingZoneHist(OrderShippingZoneHistVo orderShippingZoneHistVo) {
		return orderRegistrationMapper.addShippingZoneHist(orderShippingZoneHistVo);
	}

    /**
     * 마이페이지 배송지 리스트조회
     *
     * @param urUserId
     * @return CommonGetShippingAddressListResponseDto
     * @throws Exception
     */
    protected List<CommonGetShippingAddressListResultVo> getMypageShippingAddressList(long urUserId) throws Exception {
        return userBuyerShippingAddrMapper.getMypageShippingAddressList(urUserId); // rows
    }

    /**
     * (구)풀무원샵 & (구)올가 포인트 적립 validation
     *
     * @param dto AsisUserPointRequestDto
     * @return ApiResult<?>
     * @throws Exception Exception
     */
    protected ApiResult<?> validationDepositPointByAsIsPoint(AsisUserPointRequestDto dto){
        // 기존 포인트 없음 확인
        if((dto.getPulmuonePoint() > 0 && dto.getAsIsPulmuonePoint() == 0) ||
                dto.getOrgaPoint() > 0 && dto.getAsIsOrgaPoint() == 0){
            return ApiResult.result(UserEnums.AsisPoint.ASIS_POINT_ZERO);
        }

        // 기존 포인트보다 적립하고자 하는 포인트가 높은 경우
        if(dto.getPulmuonePoint() > dto.getAsIsPulmuonePoint() || dto.getOrgaPoint() > dto.getAsIsOrgaPoint()){
            return ApiResult.result(UserEnums.AsisPoint.ASIS_OVER_POINT);
        }

        // 풀무원샵 제한금액 체크 -- SPEC OUT
//        ApiResult<?> userPointResult = pointBiz.getUserAvailablePoints(dto.getUrUserId());
//        int userPoint = 0;
//        Object object = userPointResult.getData();
//        if(object.getClass().isAssignableFrom(Integer.class)){
//            userPoint = (Integer)userPointResult.getData();
//        }else if (object.getClass().isAssignableFrom(Long.class)){
//            Long temp = (Long) userPointResult.getData();
//            userPoint = Math.toIntExact(temp);
//        }
//
//        int expectDepositPoint = dto.getPulmuonePoint() + dto.getOrgaPoint() + userPoint;
//        if(Constants.DEPOSIT_MAXIMUM_POSSIBLE_POINT <= expectDepositPoint){
//            AsisUserPointResponseDto responseDto = new AsisUserPointResponseDto();
//            responseDto.setPulmuonePoint(dto.getPulmuonePoint());
//            responseDto.setOrgaPoint(dto.getOrgaPoint());
//            responseDto.setPointPartialDeposit(expectDepositPoint - Constants.DEPOSIT_MAXIMUM_POSSIBLE_POINT);
//            return ApiResult.result(responseDto, UserEnums.AsisPoint.DEPOSIT_POINT_EXCEEDED);
//        }

        return ApiResult.success();
    }

    /**
     * (구)풀무원샵 & (구)올가 포인트 적립 진행
     *
     * @param dto AsisUserPointRequestDto
     * @param mallDiv  GoodsEnums.MallDiv
     * @return ApiResult<?>
     * @throws Exception Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class})
    public ApiResult<?> depositPointByAsIsPointProcess(AsisUserPointRequestDto dto, GoodsEnums.MallDiv mallDiv) throws Exception {
        //풀무원 적립금
        if (mallDiv.equals(GoodsEnums.MallDiv.PULMUONE) && dto.getPulmuonePoint() > 0) {
            //(신) 풀무원 적립 처리
            ApiResult<?> pulmuonePointResponse = pointBiz.depositPreviousPulmuoneMemberPoints(dto.getUrUserId(), (long) dto.getPulmuonePoint(), dto.getCustomerNumber());

            if (BaseEnums.Default.SUCCESS.equals(pulmuonePointResponse.getMessageEnum())) {
                //기존 적립금 차감 처리
                boolean pulmuoneApiReturn = asisUserApiUtil.minusCustomerRsrv("N", dto.getCustomerNumber(), dto.getPulmuonePoint());
                if(!pulmuoneApiReturn){
                    throw new BaseException(UserEnums.AsisPoint.ASIS_API_ERROR);
                }
            } else if (PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT.equals(pulmuonePointResponse.getMessageEnum())) {
                // 적립금액 최대치 도달 - 부분 적립 케이스
                int partialDepositPoint = 0;
                Object object = pulmuonePointResponse.getData();
                if(object.getClass().isAssignableFrom(Integer.class)){
                    partialDepositPoint = (Integer)pulmuonePointResponse.getData();
                }else if (object.getClass().isAssignableFrom(Long.class)){
                    Long temp = (Long) pulmuonePointResponse.getData();
                    partialDepositPoint = Math.toIntExact(temp);
                }

                //기존 적립금 차감 처리
                boolean pulmuoneApiReturn = asisUserApiUtil.minusCustomerRsrv("N", dto.getCustomerNumber(), partialDepositPoint);
                if(!pulmuoneApiReturn){
                    throw new BaseException(UserEnums.AsisPoint.ASIS_API_ERROR);
                }

                // 리턴값 설정
                AsisUserPointResponseDto asisUserPointResponseDto = new AsisUserPointResponseDto();
                asisUserPointResponseDto.setPulmuonePoint(dto.getAsIsPulmuonePoint() - partialDepositPoint);
                asisUserPointResponseDto.setOrgaPoint(dto.getAsIsOrgaPoint());
                asisUserPointResponseDto.setPointPartialDeposit(partialDepositPoint);
                return ApiResult.result(asisUserPointResponseDto, UserEnums.AsisPoint.PARTIAL_DEPOSIT_OVER_LIMIT);
            } else if (PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED.equals(pulmuonePointResponse.getMessageEnum())){
                // 적립금액 최대치 도달 케이스
                ApiResult<?> userPointResult = pointBiz.getUserAvailablePoints(dto.getUrUserId());
                int userPoint = 0;
                Object object = userPointResult.getData();
                if(object.getClass().isAssignableFrom(Integer.class)){
                    userPoint = (Integer)userPointResult.getData();
                }else if (object.getClass().isAssignableFrom(Long.class)){
                    Long temp = (Long) userPointResult.getData();
                    userPoint = Math.toIntExact(temp);
                }

                int expectDepositPoint = dto.getPulmuonePoint() + dto.getOrgaPoint() + userPoint;
                AsisUserPointResponseDto responseDto = new AsisUserPointResponseDto();
                responseDto.setPulmuonePoint(dto.getPulmuonePoint());
                responseDto.setOrgaPoint(dto.getOrgaPoint());
                responseDto.setPointPartialDeposit(expectDepositPoint - Constants.DEPOSIT_MAXIMUM_POSSIBLE_POINT);
                return ApiResult.result(responseDto, UserEnums.AsisPoint.DEPOSIT_POINT_EXCEEDED);
            } else {
                //그외 적립 오류
                throw new BaseException(UserEnums.AsisPoint.DEPOSIT_POINT_ERROR);
            }
        }

        //올가 적립금
        if(mallDiv.equals(GoodsEnums.MallDiv.ORGA) && dto.getOrgaPoint() > 0){
            //(신) 풀무원 적립 처리
            ApiResult<?> orgaPointResponse = pointBiz.depositOrgaTransferPoints(dto.getUrUserId(), (long) dto.getOrgaPoint(), dto.getCustomerNumber());
            if (BaseEnums.Default.SUCCESS.equals(orgaPointResponse.getMessageEnum())) {
                //기존 적립금 차감 처리
                boolean orgaApiReturn = asisUserApiUtil.minusCustomerRsrv("Y", dto.getCustomerNumber(), dto.getOrgaPoint());
                if(!orgaApiReturn){
                    throw new BaseException(UserEnums.AsisPoint.ASIS_API_ERROR);
                }
            } else if (PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT.equals(orgaPointResponse.getMessageEnum())) {
                // 적립금액 최대치 도달 - 부분 적립 케이스
                int partialDepositPoint = 0;
                Object object = orgaPointResponse.getData();
                if(object.getClass().isAssignableFrom(Integer.class)){
                    partialDepositPoint = (Integer)orgaPointResponse.getData();
                }else if (object.getClass().isAssignableFrom(Long.class)){
                    Long temp = (Long) orgaPointResponse.getData();
                    partialDepositPoint = Math.toIntExact(temp);
                }

                //기존 적립금 차감 처리
                boolean pulmuoneApiReturn = asisUserApiUtil.minusCustomerRsrv("Y", dto.getCustomerNumber(), partialDepositPoint);
                if(!pulmuoneApiReturn){
                    throw new BaseException(UserEnums.AsisPoint.ASIS_API_ERROR);
                }

                // 리턴값 설정
                AsisUserPointResponseDto asisUserPointResponseDto = new AsisUserPointResponseDto();
                asisUserPointResponseDto.setPulmuonePoint(dto.getAsIsPulmuonePoint() - dto.getPulmuonePoint());
                asisUserPointResponseDto.setOrgaPoint(dto.getAsIsOrgaPoint() - partialDepositPoint);
                asisUserPointResponseDto.setPointPartialDeposit(partialDepositPoint);
                return ApiResult.result(asisUserPointResponseDto, UserEnums.AsisPoint.PARTIAL_DEPOSIT_OVER_LIMIT);
            } else if (PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED.equals(orgaPointResponse.getMessageEnum())){
                // 적립금액 최대치 도달 케이스
                ApiResult<?> userPointResult = pointBiz.getUserAvailablePoints(dto.getUrUserId());
                int userPoint = 0;
                Object object = userPointResult.getData();
                if(object.getClass().isAssignableFrom(Integer.class)){
                    userPoint = (Integer)userPointResult.getData();
                }else if (object.getClass().isAssignableFrom(Long.class)){
                    Long temp = (Long) userPointResult.getData();
                    userPoint = Math.toIntExact(temp);
                }

                int expectDepositPoint = dto.getOrgaPoint() + userPoint;
                AsisUserPointResponseDto responseDto = new AsisUserPointResponseDto();
                responseDto.setPulmuonePoint(0);
                responseDto.setOrgaPoint(dto.getOrgaPoint());
                responseDto.setPointPartialDeposit(expectDepositPoint - Constants.DEPOSIT_MAXIMUM_POSSIBLE_POINT);
                return ApiResult.result(responseDto, UserEnums.AsisPoint.DEPOSIT_POINT_EXCEEDED);
            } else {
                //그외 적립 오류
                throw new BaseException(UserEnums.AsisPoint.DEPOSIT_POINT_ERROR);
            }
        }

        return ApiResult.success();
    }

    protected OrderShippingZoneHistVo setAddShippingZoneHistRequestParam(Long odShippingZoneId, CommonSaveShippingAddressRequestDto dto) throws Exception{
        // 주문 배송 정보 조회
        OrderShippingZoneVo orderShipingZoneVo = shippingZoneBiz.getOrderShippingZone(odShippingZoneId);

        // 주문배송지 이력 등록
        OrderShippingZoneHistVo orderShippingZoneHistVo = OrderShippingZoneHistVo.builder()
                .odShippingZoneId(odShippingZoneId)
                .odOrderId(dto.getOdOrderId())
                .deliveryType(orderShipingZoneVo.getDeliveryType())
                .shippingType(orderShipingZoneVo.getShippingType())
                .recvNm(dto.getReceiverName())
                .recvHp(dto.getReceiverMobile())
                .recvTel(dto.getReceiverTelephone())
                .recvMail(dto.getReceiverMail())
                .recvZipCd(dto.getReceiverZipCode())
                .recvAddr1(dto.getReceiverAddress1())
                .recvAddr2(dto.getReceiverAddress2())
                .recvBldNo(dto.getBuildingCode())
                .deliveryMsg(dto.getShippingComment())
                .doorMsgCd(dto.getAccessInformationType())
                .doorMsg(dto.getAccessInformationPassword())
                .build();

        return orderShippingZoneHistVo;
    }

}