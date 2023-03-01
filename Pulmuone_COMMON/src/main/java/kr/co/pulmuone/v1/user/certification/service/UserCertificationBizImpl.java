package kr.co.pulmuone.v1.user.certification.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.CookieUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.send.template.dto.AddEmailIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
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

@Service
public class UserCertificationBizImpl implements UserCertificationBiz {

    @Autowired
    private UserCertificationService userCertificationService;

    @Autowired
    private SendTemplateBiz sendTemplateBiz;

    @Autowired
    private ComnBizImpl comnBizImpl;

    public void addSessionShipping(AddSessionShippingRequestDto addSessionShippingRequestDto) {
        userCertificationService.addSessionShipping(addSessionShippingRequestDto);
    }

    public GetSessionShippingResponseDto getSessionShipping() throws Exception {
        return userCertificationService.getSessionShipping();
    }

    public GetSessionUserCertificationResponseDto getSessionUserCertification() throws Exception {
        return userCertificationService.getSessionUserCertification();
    }

    public GetSessionAsisCustomerDto getSessionAsisCustomerInfo() throws Exception{
    	return userCertificationService.getSessionAsisCustomerInfo();
    }

    public void addSessionUserCertification(AddSessionUserCertificationRequestDto addSessionUserCertificationRequestDto) throws Exception {
        userCertificationService.addSessionUserCertification(addSessionUserCertificationRequestDto);
    }

    // 가입가능 체크
    public ApiResult<?> checkDuplicateJoinUser() throws Exception {
        return userCertificationService.checkDuplicateJoinUser();
    }

    public ApiResult<?> getFindMaskingLoginId(GetFindMaskingLoginIdRequestDto getFindMaskingLoginIdRequestDto) throws Exception {
        return userCertificationService.getFindMaskingLoginId(getFindMaskingLoginIdRequestDto);
    }

    public ApiResult<?> getFindLoginId(GetFindLoginIdRequestDto getFindLoginIdRequestDto) throws Exception {
        return userCertificationService.getFindLoginId(getFindLoginIdRequestDto);
    }

    public ApiResult<?> getFindPassword(GetFindPasswordRequestDto getFindPasswordRequestDto) throws Exception {
        return userCertificationService.getFindPassword(getFindPasswordRequestDto);
    }

    public ApiResult<?> getFindPasswordCI(GetFindPasswordRequestDto getFindPasswordRequestDto) throws Exception {
        return userCertificationService.getFindPasswordCI(getFindPasswordRequestDto);
    }

    //기존 탈퇴 회원 여부(Y)
    public String getBeforeUserDropYn(String ciCd) throws Exception {
      return userCertificationService.getBeforeUserDropYn(ciCd);
   }

    public String getRandom6() throws Exception {
        return userCertificationService.getRandom6();
    }

    public void putPasswordChangeCd(PutPasswordChangeCdRequestDto putPasswordChangeCdRequestDto) throws Exception {
        userCertificationService.putPasswordChangeCd(putPasswordChangeCdRequestDto);
    }

    public ApiResult<?> login(GetLoginRequestDto getLoginRequestDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return userCertificationService.login(getLoginRequestDto, request, response);
    }

    public ApiResult<?> doLogin(DoLoginRequestDto doLoginRequestDto, boolean useCaptcha, HttpServletRequest request, HttpServletResponse response) throws Exception{
        return userCertificationService.doLogin(doLoginRequestDto, useCaptcha, request, response);
    }

    public String getAutoLoginKey() throws Exception {
        return userCertificationService.getAutoLoginKey();
    }

    public GetSocialLoginDataResultVo getSocialLoginData(GetSocialLoginDataRequestDto getSocialLoginDataRequestDto) throws Exception {
        return userCertificationService.getSocialLoginData(getSocialLoginDataRequestDto);
    }

    public void addUserSocial(UserSocialInformationDto userSocialInformationDto) throws Exception {
        userCertificationService.addUserSocial(userSocialInformationDto);
    }

    public ApiResult<?> logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return userCertificationService.logout(request, response);
    }

    public ApiResult<?> putPassword(PutPasswordRequestDto putPasswordRequestDto) throws Exception {
        return userCertificationService.putPassword(putPasswordRequestDto);
    }

    public ApiResult<?> putNextPassword() throws Exception {
        return userCertificationService.putNextPassword();
    }

    public ApiResult<?> getNonMemberOrder(GetNonMemberOrderRequestDto getNonMemberOrderRequestDto) throws Exception {
        return userCertificationService.getNonMemberOrder(getNonMemberOrderRequestDto);
    }

    public ApiResult<?> getNonMemberOrderCI(GetNonMemberOrderRequestDto getNonMemberOrderRequestDto) throws Exception {
        return userCertificationService.getNonMemberOrderCI(getNonMemberOrderRequestDto);
    }

    public ApiResult<?> isCheckBuyerMoveInfo(GetIsCheckBuyerMoveInfoDto getIsCheckBuyerMoveInfoDto) throws Exception {
        return userCertificationService.isCheckBuyerMoveInfo(getIsCheckBuyerMoveInfoDto);
    }

    public GetBuyerCertificationByAutoLoginKeyResponseDto getBuyerCertificationByAutoLoginKey(String urUserId, String autoLoginKey) throws Exception {
        return userCertificationService.getBuyerCertificationByAutoLoginKey(urUserId, autoLoginKey);
    }

    public void addSessionEmployeeCertification(AddSessionEmployeeCertificationRequestDto addSessionEmployeeCertificationRequestDto) throws Exception {
        userCertificationService.addSessionEmployeeCertification(addSessionEmployeeCertificationRequestDto);
    }

    public int putAutoLoginKey(String urUserId, String autoLoginKey) throws Exception {
        return userCertificationService.putAutoLoginKey(urUserId, autoLoginKey);
    }

    public ApiResult<?> employeesLogin(GetEmployeesLoginRequestDto getEmployeesLoginRequestDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return userCertificationService.employeesLogin(getEmployeesLoginRequestDto, request, response);
    }

    public ApiResult<?> employeesCertification(GetEmployeesCertificationRequestDto getEmployeesCertificationRequestDto) throws Exception {
    	ApiResult<?> result =  userCertificationService.employeesCertification(getEmployeesCertificationRequestDto);
    	if(result.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
            // 임직원 회원 인증 직후 자동메일/SMS 전송
            EmployeeCertificationResultVo employeeCertificationResultVo = userCertificationService.getEmployeeCertificationInfo(getEmployeesCertificationRequestDto.getUrErpEmployeeCode());
            // 이메일, 휴대폰 번호 모두 없는경우 실패 처리
			if (StringUtil.isEmpty(employeeCertificationResultVo.getMobile()) && StringUtil.isEmpty(employeeCertificationResultVo.getMail())) {
            	return ApiResult.result(UserEnums.Join.EMPTY_EMPLOYEE_MESSAGE_SEND_INFO);
            }
            String tempCertiNo = getEmployeesCertificationRequestDto.getTempCertiNo();
            employeeCertificationResultVo.setUrUserId(getEmployeesCertificationRequestDto.getUrUserId());
            getEmployeeCertificationCompleted(employeeCertificationResultVo, tempCertiNo);
    	}
    	return result;
    }

    @Override
    public int getEmployeesCertification(String email, String urErpEmployeeCode) throws Exception {
    	return userCertificationService.getEmployeesCertification(email, urErpEmployeeCode);
    }

	/**
	* @Desc 임직원 회원 인증 시 자동메일/sms 발송
	* @param employeeCertificationResultVo
	* @return void
	*/
    public void getEmployeeCertificationCompleted(EmployeeCertificationResultVo employeeCertificationResultVo, String tempCertiNo) throws Exception {
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.EMPLOYEE_CERTIFICATION_INFO.getCode());
    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();
    	employeeCertificationResultVo.setTempCertiNo(tempCertiNo);

    	//이메일 발송
    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/getEmployeeCertificationEmailTmplt?tempCertiNo="+tempCertiNo;
        	String title = getEmailSendResultVo.getMailTitle();
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            String senderName = sendTemplateBiz.getPsValue("SEND_EMAIL_SENDER");
            String senderMail = sendTemplateBiz.getPsValue("SEND_EMAIL_ADDRESS");

            AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
                    .senderName(senderName) // SEND_EMAIL_SENDER
                    .senderMail(senderMail) // SEND_EMAIL_ADDRESS
            		.reserveYn(reserveYn)
                    .content(content)
                    .title(title)
                    .urUserId(employeeCertificationResultVo.getUrUserId())
                    .mail(employeeCertificationResultVo.getMail())
                    .build();

            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
    	}

    	//SMS 발송
		if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
			String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, employeeCertificationResultVo);
			String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
			String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
			AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
	                .content(content)
	                .urUserId(employeeCertificationResultVo.getUrUserId())
	                .mobile(employeeCertificationResultVo.getMobile())
	                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
	                .reserveYn(reserveYn)
	                .build();

            sendTemplateBiz.sendSmsApi(addSmsIssueSelectRequestDto);
		}

  }
    public EmployeeCertificationResultVo getEmployeeCertificationInfo(String urUserId) {
    	return userCertificationService.getEmployeeCertificationInfo(urUserId);
    }

    public ApiResult<?> employeesCertificationVeriyfy(String certificationCode) throws Exception {
        return userCertificationService.employeesCertificationVeriyfy(certificationCode);
    }

    public String getCert(KmcCertRequestDataDto reqDto) throws Exception {
        return userCertificationService.getCert(reqDto);
    }

    public KmcDecodeResponseDto getDecode(String rec_cert, String k_certNum) throws Exception {
        return userCertificationService.getDecode(rec_cert, k_certNum);
    }

    public int getisCheckLogin(DoLoginRequestDto doLoginRequestDto) throws Exception {
        return userCertificationService.getisCheckLogin(doLoginRequestDto);
    }

    public GetLoginDataResultVo getLoginData(DoLoginRequestDto doLoginRequestDto) throws Exception {
        return userCertificationService.getLoginData(doLoginRequestDto);
    }

    /**
     * 비밀번호 확인
     *
     * @param password
     * @return ApiResult
     * @throws Exception
     */
    @Override
    public ApiResult<?> isPasswordCorrect( String encryptPassword) throws Exception
    {


      return userCertificationService.isPasswordCorrect( encryptPassword);

    }

    @Override
    public String getPasswordChangeCd(String urUserId) throws Exception {
        // 발급
        String passwordChangeCd = userCertificationService.makePasswordChangeCd();

        // 저장
        PutPasswordChangeCdRequestDto requestDto = new PutPasswordChangeCdRequestDto();
        requestDto.setUrUserId(urUserId);
        requestDto.setPasswordChangeCd(passwordChangeCd);
        userCertificationService.putPasswordChangeCd(requestDto);

        return passwordChangeCd;
    }

    public void unlinkAccount(UnlinkAccountRequestDto unlinkAccountRequestDto) throws Exception {
        userCertificationService.unlinkAccount(unlinkAccountRequestDto);
    }

    public void unlinkAllAccount(Long urUserId) throws Exception {
        userCertificationService.unlinkAllAccount(urUserId);
    }

    /**
     * @Desc 로그인 인증정보 등록
     * @param certificationVo
     * @return int
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int addCertification(CertificationVo certificationVo) throws Exception{
        return userCertificationService.addCertification(certificationVo);
    }

    public boolean isUrBuyerEmployeesExist(String employeeCode) throws Exception {
        return userCertificationService.isUrBuyerEmployeesExist(employeeCode);
    }
}
