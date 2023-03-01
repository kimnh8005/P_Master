package kr.co.pulmuone.v1.user.login.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.user.login.dto.LoginDto;
import kr.co.pulmuone.v1.user.login.dto.LoginRequestDto;
import kr.co.pulmuone.v1.user.login.dto.LoginResponseDto;
import kr.co.pulmuone.v1.user.login.dto.vo.LoginResultVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserLoginBosServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private UserLoginBosService userLoginBosService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void 로그인_체크_true() throws Exception {

        int n = userLoginBosService.chkLogin(LoginDto.builder().LOGIN_ID("forbiz").build());
        assertTrue(n > 0);
    }

    @Test
    void 로그인아이디로_사용자아이디_조회_true() throws Exception {

        String str = userLoginBosService.urUserId(LoginDto.builder().LOGIN_ID("forbiz").build());
        assertEquals("1", str);
    }

    @Test
    void 관리자_로그인_true() throws Exception {

        LoginRequestDto dto = new LoginRequestDto();
        dto.setLoginId("forbiz_cli");
        dto.setPassword("kTz4ka1mjgzK17vcE0k+PRhsFg78D5DPK0piCBUlTwg=");
        dto.setAddressIp("127.0.0.1");

        ApiResult<?> result = userLoginBosService.hasLoginData(dto, null);

        assertTrue(
            Optional.ofNullable(result)
//                    .map(m -> (LoginResponseDto)m.getRows())
                    .map(m -> (LoginResponseDto)m.getData())
                    .map(LoginResponseDto::getUserVo)
                    .map(UserVo::getUserId)
                    .map(String::length)
                    .orElse(0) > 0
        );
    }

    @Test
    void 로그인_실패_처리_false() throws Exception {

        LoginRequestDto dto = new LoginRequestDto();
        dto.setLoginId("forbiz");
        dto.setPassword("xxxxxx");
        dto.setAddressIp("127.0.0.1");
        dto.setUrUserId("1");

        int cnt = 0;
        for(int n=0; n<3; n++) {
            cnt = userLoginBosService.putUserStatus(dto);
        }
        assertTrue(cnt > 0);
    }

    @Test
    void 로그인_성공_처리_true() throws Exception {

        UserVo userVo = new UserVo();
        userVo.setUserId("1");

        LoginRequestDto dto = new LoginRequestDto();
        dto.setAddressIp("127.0.0.1");

        assertTrue(userLoginBosService.putUserLoginData(userVo, dto) > 0);
    }

    @Test
    void 관리자_비밀번호_찾기_true() throws Exception {

        LoginRequestDto dto = new LoginRequestDto();
        dto.setLoginId("test123");
        dto.setUserName("test");
        dto.setEmail("test123@test.co.kr");
        dto.setPassword("test123");
        dto.setAddressIp("127.0.0.1");
        dto.setUrUserId("1");

        LoginResultVo result = userLoginBosService.getPassWordByData(dto);

        assertTrue(result != null);
    }



    @Test
    void 관리자_비밀번호_찾기_실패() throws Exception {

        LoginRequestDto dto = new LoginRequestDto();
        dto.setLoginId("forbiz11");
        dto.setUserName("포비즈");
        dto.setEmail("greenjuice8@forbiz.co.kr");
        dto.setPassword("xxxxxx");
        dto.setAddressIp("127.0.0.1");
        dto.setUrUserId("1");

        LoginResultVo result = userLoginBosService.getPassWordByData(dto);

        System.out.println("result:"+ result);

        assertFalse(result != null);
    }



    @Test
    void 비밀번호_초기화_true() throws Exception {

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setNewPassword("123123123");
        loginRequestDto.setUrUserId("1");
        loginRequestDto.setTemporaryYn("Y");

        LoginResultVo loginResultVo = new LoginResultVo();
        loginResultVo.setUrUserId("1");
        loginResultVo.setLoginId("test123");
        loginResultVo.setPassword("62080f96a2bfc48794326c5b9750942d15886e6a9746fc215cd0d04127196db2");
        loginResultVo.setPasswordChangeDate("2020-09-17 00:00:00");


        assertTrue(userLoginBosService.putPasswordChange(loginRequestDto) > 0);
    }


    @Test
    void 비밀번호_초기화_실패() throws Exception {

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setNewPassword("123123123");
        loginRequestDto.setUrUserId("9999999");
        loginRequestDto.setTemporaryYn("Y");

        LoginResultVo loginResultVo = new LoginResultVo();
        loginResultVo.setUrUserId("1");
        loginResultVo.setLoginId("test123");
        loginResultVo.setPassword("62080f96a2bfc48794326c5b9750942d15886e6a9746fc215cd0d04127196db2");
        loginResultVo.setPasswordChangeDate("2020-09-17 00:00:00");


        assertTrue(userLoginBosService.putPasswordChange(loginRequestDto) == 0);
    }



    @Test
    void 관리자_비밀번호_재설정_true() throws Exception {

        LoginRequestDto dto = new LoginRequestDto();
        dto.setLoginId("forbiz_cli");
        dto.setUrUserId("12");
        dto.setPassword("62080f96a2bfc48794326c5b9750942d15886e6a9746fc215cd0d04127196db2");
        dto.setNewPassword("MoBwnx+H4XG6OMoIWiw+R052yfAsHIHG75RD4T8+7Fg=");
        dto.setAddressIp("127.0.0.1");


        ApiResult<?> result = null;

        // 비밀번호 틀린경우
        result = userLoginBosService.putPassWordByLogin(dto);
        assertEquals(UserEnums.Login.LOGIN_PASSWORD_NO_DATA.getCode(), result.getCode());

        dto.setPassword("kTz4ka1mjgzK17vcE0k+PRhsFg78D5DPK0piCBUlTwg=");

        // 기존비번호와 동일한경우
        result = userLoginBosService.putPassWordByLogin(dto);
        assertEquals(UserEnums.Login.LOGIN_PASSWORD_CHANGE_FAIL.getCode(), result.getCode());

        dto.setNewPassword("62080f96a2bfc48794326c5b9750942d15886e6a9746fc215cd0d04127196db1");

        result = userLoginBosService.putPassWordByLogin(dto);

        assertEquals(ApiResult.success().getCode(), result.getCode());
    }

    @Test
    void 비밀번호_변경이력_등록_성공() throws Exception {

    	LoginResultVo dto = new LoginResultVo();
        dto.setUrUserId("1");
        dto.setPassword("xxxxxx");
        dto.setPasswordChangeDate("20201231");

        int cnt = userLoginBosService.addUrPwdChgHist(dto);

        assertTrue(cnt > 0);
    }

    @Test
    void 비밀번호_변경이력_등록_실패() throws Exception {

    	LoginResultVo dto = new LoginResultVo();
        dto.setUrUserId("xxxxxx");
        dto.setPassword("xxxxxx");
        dto.setPasswordChangeDate("20201231");

        // when, then
        assertThrows(Exception.class, () -> {
        	userLoginBosService.addUrPwdChgHist(dto);
        });

    }


}