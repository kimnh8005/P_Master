package kr.co.pulmuone.v1.user.welcomeLogin;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.send.template.dto.AddEmailIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.user.certification.dto.vo.EmployeeCertificationResultVo;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.welcomeLogin.dto.GetEmployeeWelcomeLoginDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WelcomeCertificationBizImpl implements WelcomeCertificationBiz {

    @Autowired
    WelcomeCertificationService welcomeCertificationService;

    @Autowired
    private SendTemplateBiz sendTemplateBiz;

    @Autowired
    private ComnBizImpl comnBizImpl;

    @Autowired
    private UserCertificationBiz userCertificationBiz;

    @Override
    public ApiResult<?> employeesCertification(GetEmployeeWelcomeLoginDto getEmployeeWelcomeLoginDto) throws Exception {
    	//임직원 여부 확인
		if (userCertificationBiz.getEmployeesCertification(getEmployeeWelcomeLoginDto.getEmail(), getEmployeeWelcomeLoginDto.getUrErpEmployeeCode()) == 0) {
			return ApiResult.result(UserEnums.Join.NO_FIND_EMPLOYEE);
		}

        ApiResult<?> result = welcomeCertificationService.employeesCertification(getEmployeeWelcomeLoginDto);
        if (result.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
            // 임직원 회원 인증 직후 자동메일/SMS 전송
            EmployeeCertificationResultVo employeeCertificationResultVo = welcomeCertificationService.getEmployeeCertificationInfo(getEmployeeWelcomeLoginDto.getUrErpEmployeeCode());
            // 이메일, 휴대폰 번호 모두 없는경우 실패 처리
 			if (StringUtil.isEmpty(employeeCertificationResultVo.getMobile()) && StringUtil.isEmpty(employeeCertificationResultVo.getMail())) {
             	return ApiResult.result(UserEnums.Join.EMPTY_EMPLOYEE_MESSAGE_SEND_INFO);
            }
            String certificationCode = getEmployeeWelcomeLoginDto.getCertificationCode();
            getEmployeeCertificationCompleted(employeeCertificationResultVo, certificationCode);
        }
        return result;
    }

    @Override
    public ApiResult<?> employeesCertificationVeriyfy(HttpServletResponse response, String certificationCode) throws Exception {
        return welcomeCertificationService.employeesCertificationVeriyfy(response, certificationCode);
    }

    /**
     * @param employeeCertificationResultVo
     * @return void
     * @Desc 임직원 회원 인증 시 자동메일/sms 발송
     */
    private void getEmployeeCertificationCompleted(EmployeeCertificationResultVo employeeCertificationResultVo, String tempCertiNo) throws Exception {
        ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.EMPLOYEE_CERTIFICATION_INFO.getCode());
        GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto) result.getData()).getRows();
        employeeCertificationResultVo.setTempCertiNo(tempCertiNo);

        //이메일 발송
        if ("Y".equals(getEmailSendResultVo.getMailSendYn())) {
            //serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
            String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/getEmployeeCertificationEmailTmplt?tempCertiNo=" + tempCertiNo;
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
                    .urUserId("0")
                    .mail(employeeCertificationResultVo.getMail())
                    .build();

            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
        }

        //SMS 발송
        if ("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
            String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, employeeCertificationResultVo);
            String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
                    .content(content)
                    .urUserId("0")
                    .mobile(employeeCertificationResultVo.getMobile())
                    .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
                    .reserveYn(reserveYn)
                    .build();

            sendTemplateBiz.sendSmsApi(addSmsIssueSelectRequestDto);
        }

    }

    public ApiResult<?> publishWelcomeToeken(HttpServletResponse response, String employeeNumber) throws Exception {
        return welcomeCertificationService.publishWelcomeToeken(response, employeeNumber);
    }
}