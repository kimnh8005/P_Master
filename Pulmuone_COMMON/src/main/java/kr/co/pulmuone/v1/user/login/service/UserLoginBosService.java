package kr.co.pulmuone.v1.user.login.service;

import javax.servlet.http.HttpServletRequest;

import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.send.template.dto.AddEmailIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;
import kr.co.pulmuone.v1.system.basic.service.SystemBasicEnvironmentBiz;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.login.dto.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.mapper.user.login.UserLoginBosMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.user.login.dto.vo.LoginResultVo;
import kr.co.pulmuone.v1.user.login.dto.vo.RecentlyLoginResultVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserLoginBosService {

    @Autowired
    private UserLoginBosMapper loginMapper;

    @Autowired
    private SystemBasicEnvironmentBiz systemBasicEnvironmentBiz;

    @Autowired
    private SendTemplateBiz sendTemplateBiz;

    @Autowired
    private ComnBizImpl comnBizImpl;

    @Autowired
    private UserCertificationBiz userCertificationBiz;

    //login 실패 count
    private static final int LOGIN_FAIL_CNT = 4;

    protected int chkLogin(LoginDto dto) throws Exception{

        log.info("-----------------ServiceImpl까지 안착!!!----------------");

        return loginMapper.loginInfo(dto);
    }

    protected String urUserId(LoginDto dto) throws Exception{

        log.info("-----------------Servcie : Service Test----------------");

        return loginMapper.loginUrUserId(dto);
    }

    /**
     * 관리자 로그인
     * @param loginRequestDto
     * @return
     * @throws Exception
     */
    protected ApiResult<?> hasLoginData(LoginRequestDto loginRequestDto, HttpServletRequest req) {
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setTwoFactorAuthYn("N");

        //관리자 로그인 정보
        UserVo userVO = loginMapper.hasBosLoginData(loginRequestDto);

        //사용 가능 계정 없음
        if(userVO == null) {
            putUserStatus(loginRequestDto);
            return ApiResult.result(UserEnums.Login.LOGIN_NO_DATA_FAIL);
        }

        String statusType = userVO.getStatusType();

        //회원 상태 휴직/일시정지 and 임시비밀번호가 아닌경우 || 임시비밀번호 유효기간 초과)
        if(statusType.equals(UserEnums.StatusCode.TEMPORARY_STOP.getCode()) &&
        		("N".equals(userVO.getTemporaryYn()) || userVO.getTemporaryExpiration() != 0)) {
            return ApiResult.result(UserEnums.Login.LOGIN_STATUS_TEMPORARY_STOP);
        }
        //회원 상태 정지
        if(statusType.equals(UserEnums.StatusCode.ADMINISTRATIVE_LEAVE.getCode()) || statusType.equals(UserEnums.StatusCode.STOP.getCode())) {
            return ApiResult.result(UserEnums.Login.LOGIN_STATUS_ADMINISTRATIVE_LEAVE);
        }
        //마지막 비밀번호 변경 경과 체크
        if("Y".equals(userVO.getPasswordChangeYn())) {
            return ApiResult.result(UserEnums.Login.LOGIN_PASSWORD_CHANGE);
        }
        //임시비밀번호 여부 확인
        if("Y".equals(userVO.getTemporaryYn())) {
            return ApiResult.result(UserEnums.Login.LOGIN_TEMPORARY_PASSWORD);
        }

        // 이차인증 로직 추가
        try {
            GetEnvironmentListRequestDto getEnvironmentListRequestDto = new GetEnvironmentListRequestDto();
            getEnvironmentListRequestDto.setSearchEnvironmentKey("TWO_FACTOR_AUTH_YN");
            GetEnvironmentListResultVo getEnvironmentListResultVo = systemBasicEnvironmentBiz.getEnvironment(getEnvironmentListRequestDto);

            if("Y".equals(getEnvironmentListResultVo.getEnvironmentValue())) {
                loginResponseDto.setTwoFactorAuthYn(getEnvironmentListResultVo.getEnvironmentValue());
                // SMS 발송 대상자 연락처 정보 조회
                EmployeeResponseDto employeeResponseDto = loginMapper.getEmployeeContactInfo(userVO.getUserId());

                // 연락처 정보가 둘다 없는 경우 실패 리턴
                if(StringUtils.isEmpty(employeeResponseDto.getMobile()) && StringUtils.isEmpty(employeeResponseDto.getEmail())) {
                    return ApiResult.result(UserEnums.Join.BOS_TWO_FACTOR_AUTH_NO_DATA);
                }

                String randomCode = userCertificationBiz.getRandom6();
                employeeResponseDto.setAuthCertiNo(randomCode);
                userVO.setAuthCertiNo(randomCode);
                userVO.setAuthCertiFailCount(0);

                ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BOS_TWO_FACTOR_AUTHENTIFICATION.getCode());
                GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto) result.getData()).getRows();

                String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)

                // SMS 발송
                if(StringUtils.isNotEmpty(employeeResponseDto.getMobile())) {
                    String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, employeeResponseDto);

                    String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
                    AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
                            .content(content)
                            .urUserId(employeeResponseDto.getUrUserId())
                            .mobile(employeeResponseDto.getMobile())
                            .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
                            .reserveYn(reserveYn)
                            .build();

                    sendTemplateBiz.sendSmsApi(addSmsIssueSelectRequestDto);
                } else {
                    // 이메일 발송 - SMS 정보가 없는 경우에만 발송
                    String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/bosTwoFactorAuthentification?authCertiNo=" + randomCode;
                    String title = getEmailSendResultVo.getMailTitle();
                    String senderName = sendTemplateBiz.getPsValue("SEND_EMAIL_SENDER");
                    String senderMail = sendTemplateBiz.getPsValue("SEND_EMAIL_ADDRESS");

                    AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
                            .senderName(senderName) // SEND_EMAIL_SENDER
                            .senderMail(senderMail) // SEND_EMAIL_ADDRESS
                            .reserveYn(reserveYn)
                            .content(content)
                            .title(title)
                            .urUserId("0")
                            .mail(employeeResponseDto.getEmail())
                            .build();

                    sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
                }
            }

        } catch (Exception e) {
            return ApiResult.result(UserEnums.Login.LOGIN_BOS_TWO_FACTOR_AUTH_FAIL);
        }


        //로그인 정보 update
        putUserLoginData(userVO, loginRequestDto);
        loginResponseDto.setUserVo(userVO);
        return ApiResult.success(loginResponseDto);

    }


    /**
     * 로그인 실패 처리 로직
     * @param loginRequestDto
     * @return
     * @throws Exception
     */
    protected int putUserStatus(LoginRequestDto loginRequestDto) {
        //계정 존재 확인
        LoginResultVo failLoginResultVo = loginMapper.getLoginInfoById(loginRequestDto);
        int failCount = 0;
        //비밀번호 틀린 경우
        if(failLoginResultVo != null) {
            loginRequestDto.setUrUserId(failLoginResultVo.getUrUserId());
            loginRequestDto.setService("SERVICE_TYPE.BOS");
            loginRequestDto.setSuccessYn("N");
            loginMapper.putFailCountIncr(loginRequestDto);	//실패카운트 +1처리
            loginMapper.addConnectionLog(loginRequestDto);	//실패로그

            failCount = failLoginResultVo.getFailCount();

            //계정 일시정지 변환
            if(failCount >= LOGIN_FAIL_CNT) {
                loginRequestDto.setStatusType(UserEnums.StatusCode.TEMPORARY_STOP.getCode());

                loginMapper.putUserStatusTp(loginRequestDto);
            }
        }

        return failCount;
    }


    /**
     * 로그인 성공 처리 로직
     * @param loginRequestDto
     * @return
     * @throws Exception
     */
    protected int putUserLoginData(UserVo userVO, LoginRequestDto loginRequestDto) {

        loginRequestDto.setService("SERVICE_TYPE.BOS");
        loginRequestDto.setUrUserId(userVO.getUserId());
        loginRequestDto.setSuccessYn("Y");

        //마지막로그인날짜, 실패카운트, 접속로그 처리
        int rtn = loginMapper.putUrAccountLastLogin(loginRequestDto);
        rtn = loginMapper.putFailCountReset(loginRequestDto);
        rtn = loginMapper.addConnectionLog(loginRequestDto);

        // Add After Key
        userVO.setConnectionId(loginRequestDto.getUrConnectLogId());

        //redis logic
        SessionUtil.setUserVO(userVO);

        return rtn;
    }


    /**
     * 관리자 비밀번호 재설정
     * @param loginRequestDto
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    protected ApiResult<?> putPassWordByLogin(LoginRequestDto loginRequestDto) throws Exception {
        //비밀번호 비교
        LoginResultVo infoLoginResultVo = loginMapper.getPasswordByPassword(loginRequestDto);

        //현재 비밀번호 틀린 경우
        if(infoLoginResultVo == null) {
            return ApiResult.result(UserEnums.Login.LOGIN_PASSWORD_NO_DATA);
        } else {
            loginRequestDto.setUrUserId(infoLoginResultVo.getUrUserId());
            loginRequestDto.setModifyId(infoLoginResultVo.getUrUserId());
            loginRequestDto.setTemporaryYn("N");
        }

        //비밀번호 변경가능 체크
        //신규비밀번호가 현재 비밀번호와 일치 할 경우
        int count = loginMapper.getUrPwdChgHistByNewPwd(loginRequestDto);
        if(count > 0) {
            return ApiResult.result(UserEnums.Login.LOGIN_PASSWORD_CHANGE_FAIL);
        }

        //비밀번호 변경
        loginMapper.putPasswordChange(loginRequestDto);

        //회원 상태 변경
        loginRequestDto.setStatusType(UserEnums.StatusCode.NORMAL.getCode());
        loginMapper.putUserStatusTp(loginRequestDto);

        //비밀번호 변경이력
        loginMapper.addUrPwdChgHist(infoLoginResultVo);

        return ApiResult.success();
    }


    /**
     * 관리자 로그아웃
     * @param loginRequestDto
     * @return
     * @throws Exception
     */
    protected ApiResult<?> delLoginData(LoginRequestDto loginRequestDto, HttpServletRequest req) throws Exception {
        UserVo userVo    = SessionUtil.getBosUserVO();
        //세션 정보 존재 시 로그아웃 정보를 update 한다
        if( userVo != null ){
            loginRequestDto.setUrConnectLogId(userVo.getConnectionId());
            loginMapper.putConnectionLogBylogOut(loginRequestDto);
        }

        //세선 제거
        req.getSession().invalidate();

        return ApiResult.success();
    }


    /**
     * 아이디 비밀번호 데이터 받기
     * @param loginRequestDto
     * @return
     * @throws Exception
     */
    protected LoginResultVo getPasswordByPassword(LoginRequestDto loginRequestDto)  {
        return loginMapper.getPasswordByPassword(loginRequestDto);
    }

    /**
     * 최근 접속 로그인 정보
     * @return
     * @throws Exception
     */
    protected ApiResult<?> getRecentlyLoginData() throws Exception{

        RecentlyLoginResponseDto recentlyLoginResponseDto = new RecentlyLoginResponseDto();

        //세션에서 UserVo 받기
        UserVo userVo = SessionUtil.getBosUserVO();
        String userId = userVo.getUserId();

        //최근 접속 로그인 정보
        RecentlyLoginResultVo recentlyLoginResultVo = loginMapper.getRecentlyLoginData(userId);

        recentlyLoginResponseDto.setRecentlyLoginResultVo(recentlyLoginResultVo);

        // 회원 데이터 여부 확인
        if(recentlyLoginResultVo == null) {
            return ApiResult.result(UserEnums.Login.LOGIN_NO_DATA);
        }

        return ApiResult.success(recentlyLoginResponseDto);
    }


    /**
     *
     * @Desc  관리자 비밀번호 정보 받기 (By 개인정보)
     * @param loginRequestDto
     * @return
     * @throws Exception
     * @return LoginResultVo
     */
    protected LoginResultVo getPassWordByData(LoginRequestDto loginRequestDto) throws Exception {
        return loginMapper.getPasswordByData(loginRequestDto);
    }


    /**
     * @Desc 비밀번호 변경
     * @param loginRequestDto
     * @return
     * @return int
     */
    protected int putPasswordChange(LoginRequestDto loginRequestDto) {
        return loginMapper.putPasswordChange(loginRequestDto);

    }


    /**
     * @Desc 비밀번호 변경이력
     * @param loginResultVo
     * @return
     * @return int
     */
    protected int addUrPwdChgHist(LoginResultVo loginResultVo) {
        return loginMapper.addUrPwdChgHist(loginResultVo);

    }

    protected ApiResult<?> bosTwofactorAuthentification(String authCertiNo) throws Exception {
        UserVo userVo = SessionUtil.getBosUserVO();

        // 인증코드가 없을때 예외처리
        if (StringUtil.isEmpty(userVo.getUserId())) {
            log.info("====0001 로그인필요===");
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        if(userVo.getAuthCertiFailCount() > 4) {
            userVo.setAuthCertiNo("");
            userVo.setAuthCertiFailCount(0);
            SessionUtil.setUserVO(null);
            return ApiResult.result(UserEnums.Join.BOS_TWO_FACTOR_AUTH_FIVE_FAIL);
        }

        if(!authCertiNo.equals(userVo.getAuthCertiNo())) {
            int failCount = userVo.getAuthCertiFailCount() + 1;
            userVo.setAuthCertiFailCount(failCount);
            SessionUtil.setUserVO(userVo);

            return ApiResult.result(UserEnums.Join.BOS_TWO_FACTOR_AUTH_FAIL);
        }

        userVo.setAuthCertiNo("");
        userVo.setAuthCertiFailCount(0);
        SessionUtil.setUserVO(userVo);
        return ApiResult.result(UserEnums.Join.BOS_TWO_FACTOR_AUTH_SUCCESS);
    }
}
