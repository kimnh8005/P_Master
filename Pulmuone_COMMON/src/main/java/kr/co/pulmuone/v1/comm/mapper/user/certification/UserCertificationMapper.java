package kr.co.pulmuone.v1.comm.mapper.user.certification;

import kr.co.pulmuone.v1.user.buyer.dto.DoLoginRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.*;
import kr.co.pulmuone.v1.user.certification.dto.vo.*;
import kr.co.pulmuone.v1.user.login.dto.UnlinkAccountRequestDto;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;

@Mapper
public interface UserCertificationMapper {

  GetcheckDuplicateCIResultVo checkDuplicateCI(GetCheckDuplicateCIRequestDto getcheckDuplicateCIRequestDto) throws Exception;

  GetFindPasswordResultVo getFindPassword(GetFindPasswordRequestDto getFindPasswordRequestDto) throws Exception;

  GetFindPasswordCIResultVo getFindPasswordCI(GetCertificationRequestDto getCertificationRequestDto) throws Exception;

  GetFindMaskingLoginIdResultVo getFindMaskingLoginId(GetFindMaskingLoginIdRequestDto getFindMaskingLoginIdRequestDto) throws Exception;

  GetFindLoginIdResultVo getFindLoginId(GetCertificationRequestDto getCertificationRequestDto) throws Exception;

  String getBeforeUserDropYn(@Param("ciCd") String ciCd) throws Exception;

  void putPasswordChangeCd(PutPasswordChangeCdRequestDto putPasswordChangeCdRequestDto) throws Exception;

  GetLoginDataResultVo getLoginData(DoLoginRequestDto doLoginRequestDto) throws Exception;

  int getisCheckLogin(DoLoginRequestDto doLoginRequestDto) throws Exception;

  int putLoginFailCountClear(String urUserId) throws Exception;

  GetStopReasonResultVo getStopReason(String urUserId) throws Exception;

  int putLoginFailCountIncrease(String urUserId) throws Exception;

  GetBuyerSessionDataResultVo getBuyerSessionData(@Param("urUserId") String urUserId,
                                                  @Param("databaseEncryptionKey") String databaseEncryptionKey) throws Exception;

  int addConnectionLog(ConnectionLogVo connectionLogVo) throws Exception;

  int putAutoLoginKey(@Param("urUserId") String urUserId, @Param("autoLoginKey") String autoLoginKey) throws Exception;

  GetSocialLoginDataResultVo getSocialLoginData(GetSocialLoginDataRequestDto getSocialLoginDataRequestDto) throws Exception;

  void addUserSocial(UserSocialInformationDto userSocialInformationDto) throws Exception;

  void putConnectionLogout(HashMap<String, String> connectionLogoutMap) throws Exception;

  int getCheckSamePasswordCnt(@Param("password") String password, @Param("passwordChangeCd") String passwordChangeCd) throws Exception;

  int putPassword(@Param("password") String password, @Param("passwordChangeCd") String passwordChangeCd) throws Exception;

  int putNextPassword(String urUserId) throws Exception;

  int getNonMemberOrder(@Param("buyerName") String buyerName,
                        @Param("buyerMobile") String buyerMobile,
                        @Param("databaseEncryptionKey") String databaseEncryptionKey) throws Exception;

  void putNonMemberOrderCI(@Param("buyerName") String buyerName,
          @Param("buyerMobile") String buyerMobile,
          @Param("ciCd") String ciCd) throws Exception;

  int getNonMemberOrderCI(@Param("buyerName") String buyerName,
                             @Param("buyerMobile") String buyerMobile,
                             @Param("ciCd") String ciCd,
                             @Param("databaseEncryptionKey") String databaseEncryptionKey) throws Exception;

  int isCheckBuyerMoveInfo(@Param("loginId") String loginId,
                           @Param("encryptPassword") String encryptPassword,
                           @Param("ciCd") String ciCd) throws Exception;

  GetBuyerCertificationByAutoLoginKeyResponseVo getBuyerCertificationByAutoLoginKey(@Param("urUserId") String urUserId,
                                                                                    @Param("autoLoginKey") String autoLoginKey) throws Exception;

  int getEmployeesCertification(@Param("email") String email,
                                @Param("urErpEmployeeCode") String urErpEmployeeCode,
                                @Param("databaseEncryptionKey") String databaseEncryptionKey) throws Exception;

  EmployeeCertificationResultVo getEmployeeCertificationInfo(String urErpEmployeeCd);

  int getIsUrBuyerEmployeesExistCnt(@Param("urUserId") String urUserId,
                                    @Param("urErpEmployeeCode") String urErpEmployeeCode,
                                    @Param("databaseEncryptionKey") String databaseEncryptionKey) throws Exception;

  int putUrBuyerForEmployees(@Param("urUserId") String urUserId,
                             @Param("urErpEmployeeCode") String urErpEmployeeCode) throws Exception;

  int isPasswordCorrect(@Param("urUserId") String urUserId, @Param("encryptPassword") String encryptPassword) throws Exception;

  String getPasswordChangeCd(@Param("urUserId") String urUserId) throws Exception;

  void unlinkAccount(UnlinkAccountRequestDto unlinkAccountRequestDto) throws Exception;

  void unlinkAllAccount(Long urUserId) throws Exception;

  /**
   * @Desc 로그인 인증정보 등록
   * @param certificationVo
   * @throws Exception
   * @return int
   */
  int addCertification(CertificationVo certificationVo) throws Exception;



}
