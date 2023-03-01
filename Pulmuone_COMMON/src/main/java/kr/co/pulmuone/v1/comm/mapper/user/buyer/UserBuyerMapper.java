package kr.co.pulmuone.v1.comm.mapper.user.buyer;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.base.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface UserBuyerMapper {
  int checkDuplicateMail(CommonDuplicateMailRequestDto dto);

  CommonGetRefundBankResultVo getRefundBank(CommonGetRefundBankRequestDto dto) throws Exception;

  int putRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception;

  int addRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception;

  void delRefundBank(Long urRefundBankId) throws Exception;

  List<CommonGetShippingAddressListResultVo> getShippingAddressList(String urUserId);

  int getShippingAddressListCount(String urUserId);

  CommonGetShippingAddressResultVo getShippingAddress(CommonGetShippingAddressRequestDto dto) throws Exception;

  int putShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception;

  void putShippingAddressDefault(CommonSaveShippingAddressRequestDto dto) throws Exception;

  int addShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception;

  List<GetChangeClauseNoAgreeListResultVo> getChangeClauseNoAgreeList(String urUserId) throws Exception;

  int putMaketingReceiptAgreementSms(String urUserId) throws Exception;

  int putMaketingReceiptAgreementMail(String urUserId) throws Exception;

  GetShippingAddressListResultVo getBasicShippingAddress(@Param("databaseEncryptionKey") String databaseEncryptionKey,
                                                         @Param("urUserId") String urUserId) throws Exception;

  List<GetShippingAddressListResultVo> getShippingAddressLimitList(GetShippingAddressListRequestDto getShippingAddressListRequestDto) throws Exception;

  void delUserSearchWordLog(List<Integer> urUserSearchWordLogId) throws Exception;

  List<GetSearchWordResultVo> getUserSearchWordLogList(String urUserId) throws Exception;

  void addUserSearchWordLog(HashMap<String, String> addUserSearchMap) throws Exception;

  Integer getNewBuyerSpecialsCouponByUser(@Param("ilGoodsId") Long ilGoodsId,
                                          @Param("urUserId") String urUserId,
                                          @Param("deviceInfo") String deviceInfo,
                                          @Param("isApp") boolean isApp) throws Exception;

  BuyerFromMypageResultVo getBuyerFromMypage(Long urUserId) throws Exception;

  void putUserFromMypage(CommonPutBuyerFromMypageRequestDto commonPutBuyerFromMypageRequestDto) throws Exception;

  void putBuyerFromMypage(CommonPutBuyerFromMypageRequestDto commonPutBuyerFromMypageRequestDto) throws Exception;

  void delShippingAddress(Long urShippingAddrId) throws Exception;

  void putShippingAddressSetDefault(@Param("urShippingAddrId") Long urShippingAddrId) throws Exception;

  void putShippingAddressSetInit(@Param("urUserId") Long urUserId) throws Exception;

  Page<ShippingAddressListFromMyPageResultVo> getShippingAddressListFromMyPage(ShippingAddressListFromMyPageRequestDto dto) throws Exception;

  List<CodeInfoVo> getCommonCode(GetCodeListRequestDto getCodeListRequestDto) throws Exception;

  CodeInfoVo getBankCodeAttr1(String stComnCodeId) throws Exception;

  HashMap<String,String> getUserPaymentInfo(String urUserId) throws Exception;

  void putUserPaymentInfo(@Param("urUserId") Long urUserId, @Param("psPayCd") String psPayCd, @Param("cardCode") String cardCode) throws Exception;

  int putShippingAddressIntoOrderShippingZone(CommonSaveShippingAddressRequestDto dto) throws Exception;

}
