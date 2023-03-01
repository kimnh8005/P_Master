package kr.co.pulmuone.v1.user.login.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SHA256Util;
import kr.co.pulmuone.v1.comm.util.SystemUtil;
import kr.co.pulmuone.v1.comm.util.UidUtil;
import kr.co.pulmuone.v1.send.template.dto.AddEmailIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.user.environment.service.UserEnvironmentBiz;
import kr.co.pulmuone.v1.user.login.dto.LoginDto;
import kr.co.pulmuone.v1.user.login.dto.LoginRequestDto;
import kr.co.pulmuone.v1.user.login.dto.vo.LoginResultVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserLoginBosBizImpl implements UserLoginBosBiz {

    @Autowired
    private UserLoginBosService userLoginBosService;

    @Autowired
    private UserEnvironmentBiz userEnvironmentBiz;

    @Autowired
	private SendTemplateBiz sendTemplateBiz;

	@Autowired
	private ComnBizImpl comnBizImpl;

    public int chkLogin(LoginDto dto) throws Exception {

        return userLoginBosService.chkLogin(dto);
    }

    public String urUserId(LoginDto dto) throws Exception {

        return userLoginBosService.urUserId(dto);
    }

    @Override
    public ApiResult<?> hasLoginData(LoginRequestDto loginRequestDto, HttpServletRequest req) {


    	//접속 정보 받기
/*
    	String addressIp = req.getHeader("X-FORWARDED-FOR");
	    if (addressIp == null){
	    	addressIp = req.getRemoteAddr();
	    }
*/
	    String addressIp = SystemUtil.getIpAddress(req);
	    loginRequestDto.setAddressIp(addressIp);

	    String urPcidCd = UidUtil.randomUUID().toString();
	    try {
	    	userEnvironmentBiz.addPCID(urPcidCd, req.getHeader("User-Agent"));
	    } catch (Exception e) {
            //return ApiResult.result(BaseEnums.Default.EXCEPTION_ISSUED);
	    }
	    loginRequestDto.setUserPcidCd(urPcidCd);

        return userLoginBosService.hasLoginData(loginRequestDto, req);
    }

    @Override
    public ApiResult<?> getPassWordByData(LoginRequestDto loginRequestDto) throws Exception {
    	//정보 확인
        LoginResultVo rtnLoginResultVo = userLoginBosService.getPassWordByData(loginRequestDto);

        //일치 계정 미존재시
        if(rtnLoginResultVo == null) {
            return ApiResult.result(UserEnums.Login.LOGIN_PASSWORD_NO_DATA);
        }
        //정지 계정일 경우
        if(UserEnums.StatusCode.STOP.getCode().equals(rtnLoginResultVo.getStatusType())) {
            return ApiResult.result(UserEnums.Login.LOGIN_STATUS_ADMINISTRATIVE_LEAVE);
        }

        putPassWordReset(loginRequestDto, rtnLoginResultVo);

        return ApiResult.success();
    }


    /**
     * 관리자 비밀번호 찾기 > 비밀번호 초기화
     * @param loginRequestDto
     * @return
     * @throws Exception
     */
    protected int putPassWordReset(LoginRequestDto loginRequestDto, LoginResultVo rtnLoginResultVo) throws Exception {

        //임시 비밀번호 생성
        String tempPassWord = RandomStringUtils.randomAlphanumeric(10);
        rtnLoginResultVo.setPassword(tempPassWord);

        //임시 비밀번호 정보 update
        loginRequestDto.setNewPassword(SHA256Util.getUserPassword(tempPassWord));
        loginRequestDto.setUrUserId(rtnLoginResultVo.getUrUserId());
        loginRequestDto.setTemporaryYn("Y");
        loginRequestDto.setStatusType(rtnLoginResultVo.getStatusType());

        int putValue = userLoginBosService.putPasswordChange(loginRequestDto);

        //비밀번호 변경이력
        userLoginBosService.addUrPwdChgHist(rtnLoginResultVo);

        if(putValue == 1) {
        	//메일 sms 발송 확인 체크
        	ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BOS_FIND_PASSWORD.getCode());
        	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();
        	//이메일 발송
        	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
            	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
        		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/getBosPassWordEmailTmplt?urUserId="+rtnLoginResultVo.getUrUserId()+"&userPassword="+tempPassWord;
            	String title = getEmailSendResultVo.getMailTitle();
                String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)

                AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
                        .reserveYn(reserveYn)
                        .content(content)
                        .title(title)
                        .urUserId(rtnLoginResultVo.getUrUserId())
                        .mail(loginRequestDto.getEmail())
                        .build();

                sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
        	}

        	//SMS 발송
    		if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {

    			String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, rtnLoginResultVo);
    			String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
    			String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
    			AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
    	                .content(content)
    	                .urUserId(rtnLoginResultVo.getUrUserId())
    	                .mobile(rtnLoginResultVo.getMobile())
    	                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
    	                .reserveYn(reserveYn)
    	                .build();

    			sendTemplateBiz.sendSmsApi(addSmsIssueSelectRequestDto);

    		}
        }

        return putValue;
    }



    @Override
    public ApiResult<?> putPassWordByLogin(LoginRequestDto loginRequestDto) throws Exception {
        return userLoginBosService.putPassWordByLogin(loginRequestDto);
    }

    @Override
    public ApiResult<?> delLoginData(LoginRequestDto loginRequestDto, HttpServletRequest req) throws Exception {
        return userLoginBosService.delLoginData(loginRequestDto, req);
    }

    @Override
    public LoginResultVo getPasswordByPassword(LoginRequestDto loginRequestDto)  {
        return userLoginBosService.getPasswordByPassword(loginRequestDto);
    }

    @Override
    public ApiResult<?> getRecentlyLoginData() throws Exception{
        return userLoginBosService.getRecentlyLoginData();
    }

    @Override
    public ApiResult<?> bosTwoFactorAuthentificationVeriyfy(String authCertiNo) throws Exception {
        return userLoginBosService.bosTwofactorAuthentification(authCertiNo);
    }
}
