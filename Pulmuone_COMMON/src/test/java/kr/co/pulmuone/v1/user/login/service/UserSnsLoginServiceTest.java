package kr.co.pulmuone.v1.user.login.service;

import kr.co.pulmuone.v1.api.sns.service.SnsApiBiz;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionUserSnsCertificationResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.UserSocialInformationDto;
import kr.co.pulmuone.v1.user.login.dto.UnlinkAccountRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserSnsLoginServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private UserSnsLoginService userSnsLoginService;

    @Autowired
    private SnsApiBiz snsApiBiz;

    @BeforeEach
    void beforeEach() {
        buyerLogin();
    }

    @Test
    void 세션_유효성_검증을_윈한_난수_생성기_true() throws Exception {

        assertTrue(
            Optional.ofNullable(snsApiBiz.generateRandomString())
                    .map(String::length)
                    .orElse(0) > 0
        );
    }

    @Test
    void SNS회원_정보_세션에_저장_조회_true() throws Exception {

        UserSocialInformationDto dto = new UserSocialInformationDto();
        dto.setProvider("provider");
        dto.setSocialId("socialId");
        dto.setName("name");

        userSnsLoginService.addSessionUserSnsCertification(dto);

        GetSessionUserSnsCertificationResponseDto sessionDto = userSnsLoginService.getSessionUserSnsCertification();

        assertTrue(
            Optional.ofNullable(sessionDto)
                .filter(c -> c.getProvider().equals("provider"))
                .filter(c -> c.getSocialId().equals("socialId"))
                //.filter(c -> c.getSocialName().equals("name"))
                //.filter(c -> c.getSocialMail().equals("mail"))
                //.filter(c -> c.getSocialProfileImage().equals("socialProfileImage"))
                .map(GetSessionUserSnsCertificationResponseDto::getSocialId)
                .map(String::length)
                .orElse(0) > 0
        );
    }

    @Test
    void SNS로그인_계정_연결끊기_true() throws Exception {

        UnlinkAccountRequestDto dto = new UnlinkAccountRequestDto();
        dto.setUser_id("419929");

        ApiResult<?> result = userSnsLoginService.unlinkAccount(dto);
        assertEquals(result.getCode(), ApiResult.success().getCode());
    }
}