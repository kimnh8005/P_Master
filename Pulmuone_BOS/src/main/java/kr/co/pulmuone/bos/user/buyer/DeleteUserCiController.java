package kr.co.pulmuone.bos.user.buyer;

import javax.servlet.http.HttpServletRequest;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.buyer.service.DeleteUserCIBiz;
import kr.co.pulmuone.v1.user.login.dto.LoginRequestDto;
import kr.co.pulmuone.v1.user.login.dto.LoginResponseDto;
import kr.co.pulmuone.v1.user.login.service.UserLoginBosBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


/**
 *
 * <PRE>
 * <B>History:</B>
 *
 *      임시로 만든 페이지..  향후 삭제 예정..  최용호.
 *
 * =======================================================================
 * </PRE>
 **/
@RestController
public class DeleteUserCiController {

    @Autowired
    private DeleteUserCIBiz deleteUserCIBiz;

    @Value("${spring.profiles}")
    private String springProfiles;

    /**
     * 사용자 CI 삭제
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/login/hasLoginData_DeleteUserCi")
    public String deleteUserCiData(HttpServletRequest req) throws Exception {

        String userid = req.getParameter("urUserId");

        if(userid == null || userid.length() == 0) {
            return "Fail!!";
        }

        if("prod".equals(springProfiles)) {
            return "Fail!!";
        }

        int cnt = deleteUserCIBiz.resetUserCi(userid);

        if(cnt > 0) {
           return "Success!!";
        } else {
            return "Fail!!";
        }
    }
}
