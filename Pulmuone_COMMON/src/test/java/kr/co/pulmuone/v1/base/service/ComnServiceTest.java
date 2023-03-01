package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.base.dto.MenuOperLogRequestDto;
import kr.co.pulmuone.v1.base.dto.PrivacyMenuOperLogRequestDto;
import kr.co.pulmuone.v1.base.dto.vo.MenuUrlResultVo;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.mapper.base.ComnMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class ComnServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    ComnService comnService;

    @InjectMocks
    private ComnService mockComnService;

    @Mock
    ComnMapper mockComnMapper;

    @BeforeEach
    void setUp() {
        preLogin();
    }

    @Test
    void 기존_로그인_세션_조회() throws Exception {
        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();

        ApiResult<?> apiResult = comnService.hasSessionInfoByLoginId(request);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void 기존_로그인_세션_삭제() throws Exception {
        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        UserVo myUserVo = SessionUtil.getBosUserVO();

        assertTrue(comnService.delPreviousLoginSession(request, myUserVo));
    }

    @Test
    void getMenuUrlDataByUrl() throws Exception {
        String systemMenuId = "1";
        String url = "/admin/comn/getPageInfo";
        MenuUrlResultVo menuUrlResultVo = comnService.getMenuUrlDataByUrl(systemMenuId, url);
        assertTrue(menuUrlResultVo != null);
    }

    @Test
    void addMenuOperLog() throws Exception{
        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = servletContainer.getRequest();

        MenuOperLogRequestDto menuOperLogRequestDto = new MenuOperLogRequestDto();

        given(mockComnMapper.addMenuOperLog(any())).willReturn(1);

        //when
        int n = mockComnService.addMenuOperLog(menuOperLogRequestDto, req);
        assertTrue(n > 0);
    }

    @Test
    void addPrivacyMenuOperLog() throws Exception {
        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = servletContainer.getRequest();

        PrivacyMenuOperLogRequestDto privacyMenuOperLogRequestDto = new PrivacyMenuOperLogRequestDto();

        given(mockComnMapper.addPrivacyMenuOperLog(any())).willReturn(1);

        //when
        int n = mockComnService.addPrivacyMenuOperLog(privacyMenuOperLogRequestDto, req);
        assertTrue(n > 0);

    }
}