package kr.co.pulmuone.mall.user.join;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.util.CookieUtil;
import kr.co.pulmuone.v1.user.buyer.dto.AddBuyerRequestDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.join.dto.GetIsCheckLoginIdRequestDto;
import kr.co.pulmuone.v1.user.join.dto.GetIsCheckMailRequestDto;
import kr.co.pulmuone.v1.user.join.dto.GetIsCheckRecommendLoginIdRequestDto;
import kr.co.pulmuone.v1.user.join.dto.vo.GetJoinResultVo;
import kr.co.pulmuone.v1.user.join.service.UserJoinBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
/*
 * SpringBootBosApplication.java에 선언하지 말고 Controller에서 필요한 Domain의 Package를
 * Scan해서 사용해야 함 kr.co.pulmuone.mall는 명시해도 되고 안해도 됨
 */

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200617   	 	김경민            최초작성
 * =======================================================================
 * </PRE>
 */
@ComponentScan(basePackages = { "kr.co.pulmuone.common" })
@RestController
@Api(description = "회원가입")
public class UserJoinController {

    @Autowired
    private UserJoinBiz joinService;

    @Autowired
    private UserCertificationBiz userCertificationBiz;

    /**
     * 가입가능 체크
     *
     * @params
     * @return BaseResponseDto
     * @throws Exception
     */
    @ApiOperation(value = "가입가능 체크")
    @PostMapping(value = "/user/join/checkDuplicateJoinUser")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" +
                    "[NORMAL_MEMBER_CI_DUPLICATE] 1204 - 정상 회원 CI 중복입니다. \n" +
                    "[STOP_MEMBER_CI_DUPLICATE] 1205 - 정지 회원 CI 중복입니다. \n" +
//                    "[WITHDRAWAL_MEMBER_CI_DUPLICATE] 1206 - 탈퇴 회원 CI 중복입니다. \n" +
                    "[UNDER_14_AGE_NOT_ALLOW] 1202 - 14세 미만은 회원가입불가 \n"
            )
    })
    public ApiResult<?> checkDuplicateJoinUser() throws Exception {

        // 14미만은 체크로직 포함
        return userCertificationBiz.checkDuplicateJoinUser();
    }

    /**
     * 아이디 중복 확인
     *
     * @param getIsCheckLoginIdRequestDto
     * @return GetIsCheckLoginIdResponseDto
     * @throws Exception
     */
    @ApiOperation(value = "아이디 중복 확인")
    @PostMapping(value = "/user/join/isCheckLoginId")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" +
                    "[ID_DUPLICATE] 1301 - 아이디 중복입니다. \n"
            )
    })
    public ApiResult<?> isCheckLoginId(GetIsCheckLoginIdRequestDto getIsCheckLoginIdRequestDto) throws Exception {

        return joinService.getIsCheckLoginId(getIsCheckLoginIdRequestDto);
    }

    /**
     * 이메일 중복 확인
     *
     * @param getIsCheckMailRequestDto
     * @return GetIsCheckMailResponseDto
     * @throws Exception
     */
    @ApiOperation(value = "이메일 중복 확인")
    @PostMapping(value = "/user/join/isCheckMail")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" +
                    "[EMAIL_DUPLICATE] 1302 - 이메일 중복입니다 \n"
            )
    })
    public ApiResult<?> isCheckMail(GetIsCheckMailRequestDto getIsCheckMailRequestDto) throws Exception {
        // TODO : 공통으로 변경
        return joinService.getIsCheckMail(getIsCheckMailRequestDto);
    }

    /**
     * 추천인 ID 체크
     *
     * @param getIsCheckRecommendLoginIdRequestDto
     * @return GetIsCheckRecommendLoginIdResponseDto
     * @throws Exception
     */
    @ApiOperation(value = "추천인 ID 체크")
    @PostMapping(value = "/user/join/isCheckRecommendLoginId")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" +
                    "[NO_FIND_RECOMENDID] 1303 - 추천인 아이디가 존재하지 않습니다 \n"
            )
    })
    public ApiResult<?> isCheckRecommendLoginId(GetIsCheckRecommendLoginIdRequestDto getIsCheckRecommendLoginIdRequestDto) throws Exception {
        return joinService.getIsCheckRecommendLoginId(getIsCheckRecommendLoginIdRequestDto);
    }

    /**
     * 회원가입
     *
     * @param geAddBuyerRequestDto
     * @return AddBuyerResponseDto
     * @throws Exception
     */
    @ApiOperation(value = "회원가입")
    @PostMapping(value = "/user/join/addBuyer")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetJoinResultVo.class),
            @ApiResponse(code = 901, message = "" +
                    "[NO_FIND_INFO_USER] 1222 - 사용자 정보 없음 \n" +
                    "[ID_DUPLICATE] 1301 - 아이디 중복입니다. \n" +
                    "[EMAIL_DUPLICATE] 1302 - 이메일 중복입니다 \n" +
                    "[NO_FIND_RECOMENDID] 1303 - 추천인 아이디가 존재하지 않습니다 \n"
            )
    })
    public ApiResult<?> addBuyer(AddBuyerRequestDto geAddBuyerRequestDto, HttpServletRequest request) throws Exception {
        geAddBuyerRequestDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
        return joinService.addBuyer(geAddBuyerRequestDto);
    }


    /**
     * 기존 회원 여부 체크
     *
     * @param loginId
     * @param password
     * @param captcha
     * @throws Exception
     */
    @ApiOperation(value = "기존 회원 여부 체크")
    @PostMapping(value = "/user/join/asisUserCheck")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data : null"),
    	    @ApiResponse(code = 901, message = "" +
    	    		"[LOGIN_FAIL] 1208 - 로그인 실패 \n"
    	    		+ "[OVER5_FAIL_CERTIFI_CODE] 1403 - 5회 연속 실패 \n"
    	    		+ "[RECAPTCHA_FAIL] 1214 - 캡차 인증 실패 \n") })
    public ApiResult<?> asisUserCheck(String loginId, String password, String captcha, String siteno) throws Exception {
        return joinService.asisUserCheck(loginId,password,captcha,siteno);
    }
}