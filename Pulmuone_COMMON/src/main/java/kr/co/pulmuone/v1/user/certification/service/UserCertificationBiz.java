package kr.co.pulmuone.v1.user.certification.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.buyer.dto.DoLoginRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.AddSessionEmployeeCertificationRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.AddSessionShippingRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.AddSessionUserCertificationRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetBuyerCertificationByAutoLoginKeyResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.GetEmployeesCertificationRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetEmployeesLoginRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetFindLoginIdRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetFindMaskingLoginIdRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetFindPasswordRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetIsCheckBuyerMoveInfoDto;
import kr.co.pulmuone.v1.user.certification.dto.GetLoginRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetNonMemberOrderRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionAsisCustomerDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionUserCertificationResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSocialLoginDataRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.KmcCertRequestDataDto;
import kr.co.pulmuone.v1.user.certification.dto.KmcDecodeResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.PutPasswordChangeCdRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.PutPasswordRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.UserSocialInformationDto;
import kr.co.pulmuone.v1.user.certification.dto.vo.CertificationVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.EmployeeCertificationResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetLoginDataResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetSocialLoginDataResultVo;
import kr.co.pulmuone.v1.user.login.dto.UnlinkAccountRequestDto;

public interface UserCertificationBiz {

    void addSessionShipping(AddSessionShippingRequestDto addSessionShippingRequestDto) throws Exception;

    GetSessionShippingResponseDto getSessionShipping() throws Exception;

    GetSessionUserCertificationResponseDto getSessionUserCertification() throws Exception;

    GetSessionAsisCustomerDto getSessionAsisCustomerInfo() throws Exception;

    void addSessionUserCertification(AddSessionUserCertificationRequestDto addSessionUserCertificationRequestDto) throws Exception;

    // 가입가능 체크
    ApiResult<?> checkDuplicateJoinUser() throws Exception;

    ApiResult<?> getFindMaskingLoginId(GetFindMaskingLoginIdRequestDto getFindMaskingLoginIdRequestDto) throws Exception;

    ApiResult<?> getFindLoginId(GetFindLoginIdRequestDto getFindLoginIdRequestDto) throws Exception;

    ApiResult<?> getFindPassword(GetFindPasswordRequestDto getFindPasswordRequestDto) throws Exception;

    ApiResult<?> getFindPasswordCI(GetFindPasswordRequestDto getFindPasswordRequestDto) throws Exception;

    //기존 탈퇴 회원 여부(Y)
    String getBeforeUserDropYn(String ci) throws Exception;

    String getRandom6() throws Exception;

    void putPasswordChangeCd(PutPasswordChangeCdRequestDto putPasswordChangeCdRequestDto) throws Exception;

    ApiResult<?> login(GetLoginRequestDto getLoginRequestDto, HttpServletRequest request, HttpServletResponse response) throws Exception;

    ApiResult<?> doLogin(DoLoginRequestDto doLoginRequestDto, boolean useCaptcha, HttpServletRequest request, HttpServletResponse response) throws Exception;

    String getAutoLoginKey() throws Exception;

    GetSocialLoginDataResultVo getSocialLoginData(GetSocialLoginDataRequestDto getSocialLoginDataRequestDto) throws Exception;

    void addUserSocial(UserSocialInformationDto userSocialInformationDto) throws Exception;

    ApiResult<?> logout(HttpServletRequest request, HttpServletResponse respons) throws Exception;

    ApiResult<?> putPassword(PutPasswordRequestDto putPasswordRequestDto) throws Exception;

    ApiResult<?> putNextPassword() throws Exception;

    ApiResult<?> getNonMemberOrder(GetNonMemberOrderRequestDto getNonMemberOrderRequestDto) throws Exception;

    ApiResult<?> getNonMemberOrderCI(GetNonMemberOrderRequestDto getNonMemberOrderRequestDto) throws Exception;

    ApiResult<?> isCheckBuyerMoveInfo(GetIsCheckBuyerMoveInfoDto getIsCheckBuyerMoveInfoDto) throws Exception;

    GetBuyerCertificationByAutoLoginKeyResponseDto getBuyerCertificationByAutoLoginKey(String urUserId, String autoLoginKey) throws Exception;

    void addSessionEmployeeCertification(AddSessionEmployeeCertificationRequestDto addSessionEmployeeCertificationRequestDto) throws Exception;

    int putAutoLoginKey(String urUserId, String autoLoginKey) throws Exception;

    ApiResult<?> employeesLogin(GetEmployeesLoginRequestDto getEmployeesLoginRequestDto, HttpServletRequest request, HttpServletResponse response) throws Exception;

    ApiResult<?> employeesCertification(GetEmployeesCertificationRequestDto getEmployeesCertificationRequestDto) throws Exception;

    int getEmployeesCertification(String email, String urErpEmployeeCode) throws Exception;

    EmployeeCertificationResultVo getEmployeeCertificationInfo(String urUserId);

    ApiResult<?> employeesCertificationVeriyfy(String certificationCode) throws Exception;

    String getCert(KmcCertRequestDataDto reqDto) throws Exception;

    KmcDecodeResponseDto getDecode(String rec_cert, String k_certNum) throws Exception;

    int getisCheckLogin(DoLoginRequestDto doLoginRequestDto) throws Exception;

    GetLoginDataResultVo getLoginData(DoLoginRequestDto doLoginRequestDto) throws Exception;

    ApiResult<?> isPasswordCorrect( String encryptPassword) throws Exception;

    String getPasswordChangeCd(String urUserId) throws Exception;

    void unlinkAccount(UnlinkAccountRequestDto unlinkAccountRequestDto) throws Exception;

    void unlinkAllAccount(Long urUserId) throws Exception;

    int addCertification(CertificationVo certificationVo) throws Exception;

    boolean isUrBuyerEmployeesExist(String employeeCode) throws Exception;
}
