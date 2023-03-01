package kr.co.pulmuone.v1.user.welcomeLogin;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.mapper.user.certification.UserCertificationMapper;
import kr.co.pulmuone.v1.comm.mapper.user.employee.UserEmployeeMapper;
import kr.co.pulmuone.v1.comm.mapper.user.login.EmployeeWelcomeLoginMapper;
import kr.co.pulmuone.v1.comm.util.*;
import kr.co.pulmuone.v1.comm.util.asis.AsisUserApiUtil;
import kr.co.pulmuone.v1.user.certification.dto.AddSessionEmployeeCertificationRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.vo.EmployeeCertificationResultVo;
import kr.co.pulmuone.v1.user.employee.dto.EmployeeResponseDto;
import kr.co.pulmuone.v1.user.employee.service.UserEmployeeBiz;
import kr.co.pulmuone.v1.user.welcomeLogin.dto.GetEmployeeWelcomeLoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@Slf4j
@Service
public class WelcomeCertificationService {

    @Autowired
    UserEmployeeBiz userEmployeeBiz;

    @Autowired
    AsisUserApiUtil asisUserApiUtil;

    @Autowired
    UserEmployeeMapper userEmployeeMapper;

    @Autowired
    EmployeeWelcomeLoginMapper employeeWelcomeLoginMapper;

    @Autowired
    private UserCertificationMapper certificationMapper;

    private final String WELCOME_TOCKEN_NAME = "urWcidCd";

    protected ApiResult<?> employeesCertification(GetEmployeeWelcomeLoginDto getEmployeeWelcomeLoginDto) throws Exception {
        String email = getEmployeeWelcomeLoginDto.getEmail();
        String inputUrErpEmployeeCode = getEmployeeWelcomeLoginDto.getUrErpEmployeeCode();

        // 입력한 이메일과 사번이 있다면 6자리 난수코드를 생성
        String tempCertiNo = getRandom6();
        log.info("tempCertiNo {}", tempCertiNo);
        /* 세션에 임시번호를 저장한다. */
        AddSessionEmployeeCertificationRequestDto addSessionEmployeeCertificationRequestDto =
                new AddSessionEmployeeCertificationRequestDto();
        addSessionEmployeeCertificationRequestDto.setTempCertiNo(tempCertiNo);
        addSessionEmployeeCertificationRequestDto.setTempUrErpEmployeeCode(inputUrErpEmployeeCode);
        addSessionEmployeeCertification(addSessionEmployeeCertificationRequestDto);// 세션저장

        getEmployeeWelcomeLoginDto.setEmail(email);
        getEmployeeWelcomeLoginDto.setUrErpEmployeeCode(inputUrErpEmployeeCode);
        getEmployeeWelcomeLoginDto.setCertificationCode(tempCertiNo);

        return ApiResult.success();
    }

    /**
     * 임직원 인증 확인
     *
     * @param certificationCode
     * @return BaseResponseDto
     * @throws Exception
     */

    protected ApiResult<?> employeesCertificationVeriyfy(HttpServletResponse response, String certificationCode) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        String tempUrErpEmployeeCode = buyerVo.getTempUrErpEmployeeCode();
        String getTempCertiNo = buyerVo.getTempCertiNo();

        log.info("저장된 ===>getTempCertiNo {}", getTempCertiNo);

        AddSessionEmployeeCertificationRequestDto addSessionEmployeeCertificationRequestDto =
                new AddSessionEmployeeCertificationRequestDto();

        if (certificationCode.equals(getTempCertiNo))// 입력값과 인증코드값이 일치하다면
        {
            addSessionEmployeeCertificationRequestDto.setFailCnt("0");
            addSessionEmployeeCertificationRequestDto.setUrErpEmployeeCode("");
            addSessionEmployeeCertificationRequestDto.setTempCertiNo("");
            addSessionEmployeeCertificationRequestDto.setTempUrErpEmployeeCode("");
            addSessionEmployeeCertification(addSessionEmployeeCertificationRequestDto);// 세션저장

            //TOKEN 발행
            return this.publishWelcomeToeken(response, tempUrErpEmployeeCode);

        } else {
            String sessionTempFailCnt = "0";
            if (StringUtil.isNotEmpty(buyerVo.getFailCnt())) {
                sessionTempFailCnt = buyerVo.getFailCnt();
            }
            log.info("sessionTempFailCnt {}", sessionTempFailCnt);
            int intFailCnt = Integer.parseInt(sessionTempFailCnt);

            intFailCnt++;
            log.info("intFailCnt {}", intFailCnt);
            MessageCommEnum messageCommEnum = null;
            if (intFailCnt >= 5) {
                // 인증코드,사번 카운트 초기화 시킨다
                addSessionEmployeeCertificationRequestDto.setFailCnt("0");
                addSessionEmployeeCertificationRequestDto.setTempUrErpEmployeeCode("");
                addSessionEmployeeCertificationRequestDto.setTempCertiNo("");
                addSessionEmployeeCertificationRequestDto.setUrUserId("");
                messageCommEnum = UserEnums.Join.OVER5_FAIL_CERTIFI_CODE;
            } else {
                String strFailCnt = String.valueOf(intFailCnt);
                addSessionEmployeeCertificationRequestDto.setFailCnt(strFailCnt);
                addSessionEmployeeCertificationRequestDto.setTempUrErpEmployeeCode(tempUrErpEmployeeCode);
                addSessionEmployeeCertificationRequestDto.setTempCertiNo(getTempCertiNo);
                messageCommEnum = UserEnums.Join.FAIL_EMPLOYEE_CERTIFI_CODE;
            }
            addSessionEmployeeCertification(addSessionEmployeeCertificationRequestDto);// 세션저장
            return ApiResult.result(messageCommEnum);
        }

    }

    protected ApiResult<?> publishWelcomeToeken(HttpServletResponse response, String employeeNumber) throws Exception {
        if (StringUtils.isEmpty(employeeNumber)) {
            return ApiResult.fail();
        }

        String urWcidCd = UidUtil.randomUUID().toString();
        CookieUtil.setCookie(response, WELCOME_TOCKEN_NAME, urWcidCd, 90 * 24 * 60 * 60);
        return ApiResult.success();
    }

    protected Boolean chkValidEmployee(String employeeNumber) {
        EmployeeResponseDto employeeResponseDto = (EmployeeResponseDto) userEmployeeBiz.getEmployeeInfo(employeeNumber).getData();

        if (ObjectUtils.isEmpty(employeeResponseDto.getEmployeeInfo())) {
            return false;
        }

        return ApprovalUtil.isAbleEmployeeStatus(employeeResponseDto.getEmployeeInfo().getUserStatus());
    }

    protected void addSessionEmployeeCertification(AddSessionEmployeeCertificationRequestDto addSessionEmployeeCertificationRequestDto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        buyerVo.setUrErpEmployeeCode(addSessionEmployeeCertificationRequestDto.getUrErpEmployeeCode());
        buyerVo.setTempUrErpEmployeeCode(addSessionEmployeeCertificationRequestDto.getTempUrErpEmployeeCode());
        buyerVo.setTempCertiNo(addSessionEmployeeCertificationRequestDto.getTempCertiNo());
        buyerVo.setFailCnt(addSessionEmployeeCertificationRequestDto.getFailCnt());
        buyerVo.setUrUserId(addSessionEmployeeCertificationRequestDto.getUrUserId());

        SessionUtil.setUserVO(buyerVo);
    }

    /**
     * @param urErpEmployeeCd
     * @return EmployeeCertificationResultVo
     * @Desc 임직원 회원 인증 후 정보 조회
     */
    protected EmployeeCertificationResultVo getEmployeeCertificationInfo(String urErpEmployeeCd) {
        return certificationMapper.getEmployeeCertificationInfo(urErpEmployeeCd);
    }

    protected String getRandom6() throws Exception {

        Random rand = new Random();
        String numStr = ""; // 난수가 저장될 변수

        for (int i = 0; i < 6; i++) {

            // 0~9 까지 난수 생성
            String ran = Integer.toString(rand.nextInt(10));

            // 중복을 허용하지 않을시 중복된 값이 있는지 검사한다
            if (!numStr.contains(ran)) {
                // 중복된 값이 없으면 numStr에 append
                numStr += ran;
            } else {
                // 생성된 난수가 중복되면 루틴을 다시 실행한다
                i -= 1;
            }

        }
        return numStr;
    }

}
