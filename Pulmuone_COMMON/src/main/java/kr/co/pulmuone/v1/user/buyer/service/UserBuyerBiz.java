package kr.co.pulmuone.v1.user.buyer.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.vo.*;
import kr.co.pulmuone.v1.user.certification.dto.UserSocialInformationDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

public interface UserBuyerBiz {

  /**
   * 이메일 중복체크
   */
  int checkDuplicateMail(CommonDuplicateMailRequestDto dto) throws Exception;

  /**
   * 환불계좌 정보 조회
   */
  CommonGetRefundBankResultVo getRefundBank(CommonGetRefundBankRequestDto dto) throws Exception;

  /**
   * 환불계좌 수정
   */
  int putRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception;

  /**
   * 환불계좌 추가
   */
  int addRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception;

  /**
   * 환불계좌 삭제
   */
  void delRefundBank(Long urRefundBankId) throws Exception;

  /**
   * 유효계좌 인증
   */
  boolean isValidationBankAccountNumber(String bankCode, String accountNumber, String holderName) throws Exception;

  /**
   * 배송지 리스트 조회
   */
  CommonGetShippingAddressListResponseDto getShippingAddressList(CommonGetShippingAddressListRequestDto dto) throws Exception;

  /**
   * 배송지 단건 조회
   */
  CommonGetShippingAddressResultVo getShippingAddress(CommonGetShippingAddressRequestDto dto) throws Exception;

  /**
   * 배송지 추가
   */
  int addShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception;

  /**
   * 배송지 수정
   */
  int putShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception;

  /**
   * 마이페이지 회원정보 조회
   */
  BuyerFromMypageResultVo getBuyerFromMypage(Long urUserId) throws Exception;

  /**
   * 마이페이지 회원정보 수정
   */
  void putBuyerFromMypage(CommonPutBuyerFromMypageRequestDto dto) throws Exception;

  int putMaketingReceiptAgreementSms(String urUserId) throws Exception;

  int putMaketingReceiptAgreementMail(String urUserId) throws Exception;

  ApiResult<?> addChangeClauseAgree(AddChangeClauseAgreeRequestDto addChangeClauseAgreeRequestDto,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception;

  GetShippingAddressListResultVo getBasicShippingAddress(String databaseEncryptionKey, String urUserId) throws Exception;

  ApiResult<?> getShippingAddressList(GetShippingAddressListRequestDto getShippingAddressListRequestDto) throws Exception;

  // 장바구니 배송목록
  ApiResult<?> getCartShippingAddressList() throws Exception;

  // 배송지 리스트 조회
  ApiResult<?> getShippingAddressList() throws Exception;

  ApiResult<?> delUserSearchWordLog(List<Integer> urUserSearchWordLogId) throws Exception;

  List<GetSearchWordResultVo> getUserSearchWordLogList(String urUserId) throws Exception;

  void addUserSearchWordLog(String keyword, String urUserId) throws Exception;

  List<GetChangeClauseNoAgreeListResultVo> getChangeClauseNoAgreeList(String urUserId) throws Exception;

  /**
   * 신규회원특가 쿠폰 조회
   */
  int getNewBuyerSpecialsCouponByUser(Long ilGoodsId, String urUserId, String deviceInfo, boolean isApp) throws Exception;

  /**
   * 배송지 삭제
   */
  void delShippingAddress(Long urShippingAddrId) throws Exception;

  /**
   * 기본배송지 설정
   */
  void putShippingAddressSetDefault(Long urUserId, Long urShippingAddrId) throws Exception;

  /**
   * 배송지관리 목록 조회
   */
  ShippingAddressListFromMyPageResponseDto getShippingAddressListFromMyPage(ShippingAddressListFromMyPageRequestDto dto) throws Exception;

  /**
   * 배송지 관련정보 조회
   */
  ApiResult<?> getShippingAddressInfo() throws Exception;

  /**
   * 은행목록
   */
  ApiResult<?> getRefundBankInfo() throws Exception;

  int getPromotionRecaptchaFailCount() throws Exception;

  void putPromotionRecaptchaFailCount(int count) throws Exception;

  /**
   * SNS 로그인 (네이버)
   */
  ApiResult<?> getUrlNaver() throws Exception;

  /**
   * SNS 로그인 응답 (네이버)
   */
  ApiResult<?> callbackNaver(HttpServletRequest request) throws Exception;

  /**
   * SNS 로그인 응답 (카카오)
   */
  ApiResult<?> callbackKakao(UserSocialInformationDto userSocialInformationDto) throws Exception;

  /**
    * SNS 로그인 응답 (구글)
    */
  ApiResult<?> callbackGoogle(UserSocialInformationDto userSocialInformationDto) throws Exception;

  /**
   * SNS 로그인 응답 (페이스북)
   */
  ApiResult<?> callbackFacebook(UserSocialInformationDto userSocialInformationDto) throws Exception;

  /**
   * SNS 로그인 응답 (애플)
   */
  ApiResult<?> callbackApple(UserSocialInformationDto userSocialInformationDto) throws Exception;

  /**
   * SNS 로그인 계정 연결끊기
   */
  ApiResult<?> delSyncAccount(String urSocialId,String provider ) throws Exception;

  /**
   * 공통코드 조회
   */
  List<CodeInfoVo> getCommonCode(String masterCode) throws Exception;

  /**
   * 회원 사용 카드 정보 조회
   */
  HashMap<String,String> getUserPaymentInfo(String urUserId) throws Exception;

	/**
	 * 회원 사용 결제정보 저장
	 *
	 * @param urUserId
	 * @param psPayCd
	 * @param CardCode
	 * @throws Exception
	 */
	void putUserPaymentInfo(Long urUserId, String psPayCd, String cardCode) throws Exception;

	/**
	 * @Desc 배송지 목록에서 주문배송지로 설정
	 * @param shippingAddressId
	 * @param urUserId
	 * @return int
	 */
	ApiResult<?> putShippingAddressIntoOrderShippingZone(CommonSaveShippingAddressRequestDto dto) throws Exception;


    /**
     * 마이페이지 배송지 목록 조회
     * @param urUserId
     * @param ilGoodsId
     * @param odOrderDetlId
     * @return List<CommonGetShippingAddressListResultVo>
     * @throws Exception
     */
	List<CommonGetShippingAddressListResultVo> getMypageShippingAddressList(long urUserId, long ilGoodsId, long odOrderDetlId) throws Exception;

	/**
	 * @Desc 최근배송지 저장
	 * @param urUserId
	 * @param odOrderid
	 * @return int
	 */
	int saveLatelyShippingAddress(long urUserId, long odOrderId) throws Exception;

    ApiResult<?> getAsisUserPoint(AsisUserPointRequestDto dto) throws Exception;

    ApiResult<?> depositPointByAsisPoint(AsisUserPointRequestDto dto) throws Exception;

    ApiResult<?> isPossibleChangeDeliveryAddress(IsPossibleChangeDeliveryAddressRequestDto reqDto) throws Exception;

    String getUserStatusByMeta();

}
