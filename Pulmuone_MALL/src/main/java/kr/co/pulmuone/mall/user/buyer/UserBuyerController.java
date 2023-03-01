package kr.co.pulmuone.mall.user.buyer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackByUserRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackByUserResponseDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.*;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaInfoByUserVo;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeePastInfoByUserResponseDto;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeByUserVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponListByUserRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponListByUserResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponCountByUserVo;
import kr.co.pulmuone.v1.promotion.event.dto.EventListFromMyPageRequestDto;
import kr.co.pulmuone.v1.promotion.event.dto.EventListFromMyPageResponseDto;
import kr.co.pulmuone.v1.promotion.point.dto.CommonGetPointListByUserRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.CommonGetPointListByUserResponseDto;
import kr.co.pulmuone.v1.promotion.point.dto.GetPointInfoResponseDto;
import kr.co.pulmuone.v1.promotion.point.dto.vo.CommonGetPointExpectExpireListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.vo.BuyerFromMypageResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetRefundBankResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetShippingAddressListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetShippingAddressListResultVo;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.user.certification.dto.UserSocialInformationDto;
import kr.co.pulmuone.v1.user.group.dto.GroupInfoByUserResponseDto;
import kr.co.pulmuone.v1.user.login.dto.LoginResponseDto;
import kr.co.pulmuone.mall.user.buyer.service.UserBuyerMallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
/*
 * SpringBootBosApplication.java에 선언하지 말고 Controller에서 필요한 Domain의 Package를
 * Scan해서 사용해야 함 kr.co.pulmuone.mall는 명시해도 되고 안해도 됨
 */

/**
 * <PRE>
 * Forbiz Korea
 * Class 의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200617   	 	김경민            최초작성
 * 1.1    20200826   	 	이원호            마이페이지 회원정보, 환불계좌 관리, 적립금, 쿠폰관리 API 추가
 * =======================================================================
 * </PRE>
 */
@Slf4j
@ComponentScan(basePackages = {"kr.co.pulmuone.common"})
@RestController
@Api("구매자정보")
public class UserBuyerController {
    @Autowired
    private UserBuyerMallService userBuyerMallService;

    @Autowired
    private UserBuyerBiz userBuyerBiz;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 개정 약관 동의 처리
     *
     * @param request,HttpServletRequest,HttpServletResponse
     * @return AddChangeClauseAgreeResponseDto
     * @throws Exception
     */
    @ApiOperation(value = "개정 약관 동의 처리")
    @PostMapping(value = "/user/buyer/addChangeClauseAgree")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data", response = LoginResponseDto.class),
            @ApiResponse(code = 901, message = "" + "(LOGIN_FAIL) 1208 - 로그인 실패 \n" + "(NO_SNS_SYNC) 1701 - SNS 계정연동되어 있는 계정 없음 \n"
                    + "(NO_SNS_SESSION) 1702 - SNS 계정 정보 없음 \n")})
    public ApiResult<?> addChangeClauseAgree(AddChangeClauseAgreeRequestDto addChangeClauseAgreeRequestDto,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws Exception {
        return userBuyerBiz.addChangeClauseAgree(addChangeClauseAgreeRequestDto, request, response);
    }

    /**
     * 장바구니 배송지 목록 조회
     *
     * @param
     * @return GetShippingAddressListResponseDto
     * @throws Exception
     */
    @ApiOperation(value = "장바구니 배송지 목록 조회")
    @PostMapping(value = "/user/buyer/getCartShippingAddressList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response List<data>", response = GetShippingAddressListResultVo.class),
            @ApiResponse(code = 901, message = "" + "(LOGIN_FAIL) 1208 - 로그인 실패 \n")})
    public ApiResult<?> getCartShippingAddressList() throws Exception {
        return userBuyerBiz.getCartShippingAddressList();
    }

    /**
     * 배송지 목록 조회
     *
     * @param
     * @return GetShippingAddressListResponseDto
     * @throws Exception
     */

    @ApiOperation(value = "배송지 목록 조회")
    @PostMapping(value = "/user/buyer/getShippingAddressList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response List<data>", response = GetShippingAddressListResultVo.class),
            @ApiResponse(code = 901, message = "" + "(LOGIN_FAIL) 1208 - 로그인 실패 \n")})
    public ApiResult<?> getShippingAddressList() throws Exception {
        return userBuyerBiz.getShippingAddressList();
    }

    /**
     * 최근 검색어 삭제
     *
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/user/searchWord/delUserSearchWordLog")
    public ApiResult<?> delUserSearchWordLog(@RequestParam(value = "urUserSearchWordLogId") List<Integer> urUserSearchWordLogId) throws Exception {

        return userBuyerBiz.delUserSearchWordLog(urUserSearchWordLogId);
    }

    @GetMapping(value = "/user/buyer/getBuyerFromMypage")
    @ApiOperation(value = "마이페이지 회원 정보 조회", httpMethod = "GET", notes = "마이페이지 회원 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = BuyerFromMypageResultVo.class),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")
    })
    public ApiResult<?> getBuyerFromMypage() throws Exception {
        return userBuyerMallService.getBuyerFromMypage();
    }

    @PostMapping(value = "/user/buyer/putBuyerFromMypage")
    @ApiOperation(value = "마이페이지 회원 정보 수정", httpMethod = "POST", notes = "마이페이지 회원 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")
    })
    public ApiResult<?> putBuyerFromMypage(CommonPutBuyerFromMypageRequestDto dto) throws Exception {
        return userBuyerMallService.putBuyerFromMypage(dto);
    }

    /**
     * 환불계좌 정보 조회
     *
     * @param dto CommonGetRefundBankRequestDto
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @GetMapping(value = "/user/buyer/getRefundBank")
    @ApiOperation(value = "환불계좌 정보 조회", httpMethod = "GET", notes = "환불계좌 정보 조회")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data", response = CommonGetRefundBankResultVo.class),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")})
    public ApiResult<?> getRefundBank(CommonGetRefundBankRequestDto dto) throws Exception {
        return userBuyerMallService.getRefundBank(dto);
    }

    /**
     * 환불계좌 정보 등록
     *
     * @param dto CommonSaveRefundBankRequestDto
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/user/buyer/addRefundBank")
    @ApiOperation(value = "환불계좌 정보 등록", httpMethod = "POST", notes = "환불계좌 정보 등록")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")})
    public ApiResult<?> addRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception {
        return userBuyerMallService.addRefundBank(dto);
    }

    /**
     * 환불계좌 정보 수정
     *
     * @param dto CommonSaveRefundBankRequestDto
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/user/buyer/putRefundBank")
    @ApiOperation(value = "환불계좌 정보 수정", httpMethod = "POST", notes = "환불계좌 정보 수정")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")})
    public ApiResult<?> putRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception {
        return userBuyerMallService.putRefundBank(dto);
    }

    /**
     * 환불계좌 정보 삭제
     *
     * @param urRefundBankId Long
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/user/buyer/delRefundBank")
    @ApiOperation(value = "환불계좌 정보 삭제", httpMethod = "POST", notes = "환불계좌 정보 삭제")
    @ApiImplicitParams({@ApiImplicitParam(name = "urRefundBankId", value = "환불계좌 ID", required = true, dataType = "Long")})
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data : null"),})
    public ApiResult<?> delRefundBank(Long urRefundBankId) throws Exception {
        return userBuyerMallService.delRefundBank(urRefundBankId);
    }

    /**
     * 유효계좌 인증
     *
     * @param bankCode      String
     * @param accountNumber String
     * @param holderName    String
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/user/buyer/isValidationBankAccountNumber")
    @ApiOperation(value = "유효계좌 인증", httpMethod = "POST", notes = "유효계좌 인증")
    @ApiImplicitParams({@ApiImplicitParam(name = "bankCode", value = "은행코드", required = true, dataType = "String"),
            @ApiImplicitParam(name = "accountNumber", value = "계좌번호", required = true, dataType = "String"),
            @ApiImplicitParam(name = "holderName", value = "계좌주", required = true, dataType = "String")})
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data", response = boolean.class)})
    public ApiResult<?> isValidationBankAccountNumber(String bankCode, String accountNumber, String holderName) throws Exception {
        return userBuyerMallService.isValidationBankAccountNumber(bankCode, accountNumber, holderName);
    }

    /**
     * 적립금 페이지 정보 조회
     *
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @GetMapping(value = "/user/buyer/getPointInfo")
    @ApiOperation(value = "적립금 페이지 정보 조회", httpMethod = "GET", notes = "적립금 페이지 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetPointInfoResponseDto.class),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")
    })
    public ApiResult<?> getPointInfo() throws Exception {
        return userBuyerMallService.getPointInfo();
    }

    /**
     * 적립금 정보 목록 조회
     *
     * @param dto CommonGetPointListByUserRequestDto
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/user/buyer/getPointListByUser")
    @ApiOperation(value = "적립금 정보 목록 조회", httpMethod = "POST", notes = "적립금 정보 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = CommonGetPointListByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")})
    public ApiResult<?> getPointListByUser(CommonGetPointListByUserRequestDto dto) throws Exception {
        return userBuyerMallService.getPointListByUser(dto);
    }

    /**
     * 적립금 소멸예정 목록 조회
     *
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/user/buyer/getPointExpectExpireList")
    @ApiOperation(value = "적립금 소멸예정 목록 조회", httpMethod = "POST", notes = "적립금 소멸예정 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = CommonGetPointExpectExpireListResultVo.class),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")})
    public ApiResult<?> getPointExpectExpireList() throws Exception {
        return userBuyerMallService.getPointExpectExpireList();
    }

    /**
     * 쿠폰 정보 목록 조회
     *
     * @param dto CommonGetCouponListByUserRequestDto
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/user/buyer/getCouponListByUser")
    @ApiOperation(value = "쿠폰 정보 목록 조회", httpMethod = "POST", notes = "쿠폰 정보 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = CouponListByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")})
    public ApiResult<?> getCouponListByUser(CouponListByUserRequestDto dto) throws Exception {
        return userBuyerMallService.getCouponListByUser(dto);
    }

    /**
     * 쿠폰 적용대상 조회
     *
     * @param pmCouponId Long
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/user/buyer/getCouponCoverage")
    @ApiOperation(value = "쿠폰 적용대상 조회", httpMethod = "POST", notes = "쿠폰 적용대상 조회")
    @ApiImplicitParams({@ApiImplicitParam(name = "pmCouponId", value = "쿠폰 ID", required = true, dataType = "Long")})
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = CouponListByUserResponseDto.class),})
    public ApiResult<?> getCouponCoverage(Long pmCouponId) throws Exception {
        return userBuyerMallService.getCouponCoverage(pmCouponId);
    }

    /**
     * 이용권 등록 페이지 정보 조회
     *
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/user/buyer/getAddPromotionPageInfo")
    @ApiOperation(value = "이용권 등록 페이지 정보 조회", httpMethod = "POST", notes = "이용권 등록 페이지 정보 조회")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인필요")})
    public ApiResult<?> getAddPromotionPageInfo() throws Exception {
        return userBuyerMallService.getAddPromotionPageInfo();
    }

    /**
     * 이용권 등록
     *
     * @param serialNumber String
     * @param captcha      String
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/user/buyer/addPromotionByUser")
    @ApiOperation(value = "이용권 등록", httpMethod = "POST", notes = "이용권 등록")
    @ApiImplicitParams({@ApiImplicitParam(name = "serialNumber", value = "이용권 코드", required = true, dataType = "String"),
            @ApiImplicitParam(name = "captcha", value = "캡챠", dataType = "String")})
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인필요 \n"
                    + "[RECAPTCHA_FAIL] RECAPTCHA_FAIL - 캡차 인증 실패 \n" + "[SUCCESS_ADD_COUPON] SUCCESS_ADD_COUPON - 이용권이 정상적으로 등록되었습니다. \n"
                    + "[SUCCESS_ADD_POINT] SUCCESS_ADD_POINT - 이용권이 정상적으로 등록되었습니다. \n"
                    + "[NOT_FIND_SERIAL_NUMBER] NOT_FIND_SERIAL_NUMBER - 인증번호를 잘못 입력하셨습니다. 다시 입력해 주세요. \n"
                    + "[OVER_ISSUE_DATE] OVER_ISSUE_DATE - 이용권의 사용기간이 만료되었습니다. \n"
                    + "[USE_SERIAL_NUMBER] USE_SERIAL_NUMBER - 이미 사용된 이용권입니다.")})
    public ApiResult<?> addPromotionByUser(String serialNumber, String captcha) throws Exception {
        return userBuyerMallService.addPromotionByUser(serialNumber, captcha);
    }

    @ApiOperation(value = "배송지관리 목록 조회")
    @PostMapping(value = "/user/buyer/getShippingAddressListFromMyPage")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ShippingAddressListFromMyPageResponseDto.class),
            @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] 0001 - 로그인필요 \n")
    })
    public ApiResult<?> getShippingAddressListFromMyPage(ShippingAddressListFromMyPageRequestDto dto) throws Exception {
        return userBuyerMallService.getShippingAddressListFromMyPage(dto);
    }

    /**
     * 배송지 관련정보 조회
     *
     * @param
     * @throws Exception
     */
    @ApiOperation(value = "배송지 관련정보 조회(공통코드조회)")
    @PostMapping(value = "/user/buyer/getShippingAddressInfo")
    public ApiResult<?> getShippingAddressInfo() throws Exception {
        return userBuyerMallService.getShippingAddressInfo();
    }

    /**
     * 배송지 추가
     *
     * @param dto CommonSaveShippingAddressRequestDto
     * @throws Exception
     */
    @ApiOperation(value = "배송지 추가")
    @PostMapping(value = "/user/buyer/addShippingAddress")
    public ApiResult<?> addShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception {
        return userBuyerMallService.addShippingAddress(dto);
    }

    /**
     * 배송지 수정
     *
     * @param dto CommonSaveShippingAddressRequestDto
     * @throws Exception
     */
    @ApiOperation(value = "배송지 수정")
    @PostMapping(value = "/user/buyer/putShippingAddress")
    public ApiResult<?> putShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception {
        return userBuyerMallService.putShippingAddress(dto);
    }

    @ApiOperation(value = "배송지 삭제")
    @PostMapping(value = "/user/buyer/delShippingAddress")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "urShippingAddrId", value = "urShippingAddrId", required = true, dataType = "Long")})
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] 0001 - 로그인필요 \n")})
    public ApiResult<?> delShippingAddress(Long urShippingAddrId) throws Exception {
        return userBuyerMallService.delShippingAddress(urShippingAddrId);
    }

    @ApiOperation(value = "기본배송지 설정")
    @PostMapping(value = "/user/buyer/putShippingAddressSetDefault")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "urShippingAddrId", value = "urShippingAddrId", required = true, dataType = "Long")})
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] 0001 - 로그인필요 \n")})
    public ApiResult<?> putShippingAddressSetDefault(Long urShippingAddrId) throws Exception {
        return userBuyerMallService.putShippingAddressSetDefault(urShippingAddrId);
    }

    /**
     * 배송지 주소 배송 정보 조회 kkm
     *
     * @param zipCode,buildingCode
     * @throws Exception
     */
    @ApiOperation(value = "배송지 주소 배송 정보 조회")
    @PostMapping(value = "/user/buyer/getShippingAddressPossibleDeliveryInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "zipCode", value = "우편 번호", required = true, dataType = "String"),
            @ApiImplicitParam(name = "buildingCode", value = "빌딩 번호", required = true, dataType = "String")
    })
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data : null")})

    public ApiResult<?> getShippingAddressPossibleDeliveryInfo(String zipCode, String buildingCode) throws Exception {
        return userBuyerMallService.getShippingAddressPossibleDeliveryInfo(zipCode, buildingCode);
    }

    /**
     * 은행목록
     *
     * @param
     * @throws Exception
     */
    @ApiOperation(value = "은행목록")
    @PostMapping(value = "/user/buyer/getRefundBankInfo")
    public ApiResult<?> getRefundBankInfo() throws Exception {
        return userBuyerMallService.getRefundBankInfo();
    }

    /**
     * 쿠폰 적용대상 조회
     *
     * @param status String
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/user/buyer/getCouponCountByUser")
    @ApiOperation(value = "쿠폰 수량 조회", httpMethod = "POST", notes = "쿠폰 수량 조회")
    @ApiImplicitParams({@ApiImplicitParam(name = "status", value = "쿠폰 발급 상태", dataType = "String")})
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = CouponCountByUserVo.class),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")})
    public ApiResult<?> getCouponCountByUser(String status) throws Exception {
        return userBuyerMallService.getCouponCountByUser(status);
    }

    /**
     * SNS 로그인 (네이버 네아로 인증 URL 생성)
     *
     * @return ApiResult<?>
     * @throws Exception
     */
    @GetMapping(value = "/user/buyer/getUrlNaver")
    @ApiOperation(value = "SNS 로그인 (네이버)", httpMethod = "GET", notes = "SNS 로그인 (네이버)")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = CallbackSocialLoginResponseDataDto.class)})
    public ApiResult<?> getUrlNaver() throws Exception {
        return userBuyerMallService.getUrlNaver();
    }

    /**
     * SNS 로그인 응답 (네이버)
     *
     * @return ApiResult<?>
     * @throws Exception
     */
    @ApiOperation(value = "SNS 로그인 응답 (네이버)")
    @GetMapping(value = "/user/buyer/callbackNaver")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = CallbackSocialLoginResponseDataDto.class),
            @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] 로그인필요 \n" + "[-1] API 통신 실패 \n")})
    public void callbackNaver() throws Exception {
        try {

            ApiResult<?> snsResult = userBuyerMallService.callbackNaver(request);

            ApiResult<?> result = ApiResult.result((UserSocialInformationDto) snsResult.getData(), snsResult.getMessageEnum());

            response.setContentType("text/html;charset=UTF-8");

            String jsonData = mapper.writeValueAsString(result);

            PrintWriter writer = response.getWriter();
            writer.print("<script>" + "var data = " + jsonData + "; " + "       \r\n" + "window.opener.returnSnsLogin(data);"
                    + "       \r\n" + "</script>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * SNS 로그인 응답 (카카오)
     *
     * @param userSocialInformationDto
     * @throws Exception
     */
    @ApiOperation(value = "SNS 로그인 응답 (카카오)")
    @PostMapping(value = "/user/buyer/callbackKakao")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = UserSocialInformationDto.class),
            @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] 로그인필요 \n" + "[-1] API 통신 실패 \n")})
    public ApiResult<?> callbackKakao(UserSocialInformationDto userSocialInformationDto) throws Exception {
        return userBuyerMallService.callbackKakao(userSocialInformationDto);
    }

    /**
     * SNS 로그인 응답 (구글)
     *
     * @param userSocialInformationDto
     * @throws Exception
     */
    @ApiOperation(value = "SNS 로그인 응답 (구글)")
    @PostMapping(value = "/user/buyer/callbackGoogle")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = UserSocialInformationDto.class),
            @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] 로그인필요 \n" + "[-1] API 통신 실패 \n")})
    public ApiResult<?> callbackGoogle(UserSocialInformationDto userSocialInformationDto) throws Exception {
        return userBuyerMallService.callbackGoogle(userSocialInformationDto);
    }

    /**
     * SNS 로그인 응답 (페이스북)
     *
     * @param userSocialInformationDto
     * @throws Exception
     */
    @ApiOperation(value = "SNS 로그인 응답 (페이스북)")
    @PostMapping(value = "/user/buyer/callbackFacebook")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = UserSocialInformationDto.class),
            @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] 로그인필요 \n" + "[-1] API 통신 실패 \n")})
    public ApiResult<?> callbackFacebook(UserSocialInformationDto userSocialInformationDto) throws Exception {
        return userBuyerMallService.callbackFacebook(userSocialInformationDto);
    }

    /**
     * SNS 로그인 응답 (애플)
     *
     * @param userSocialInformationDto
     * @throws Exception
     */
    @ApiOperation(value = "SNS 로그인 응답 (애플)")
    @PostMapping(value = "/user/buyer/callbackApple")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = UserSocialInformationDto.class),
            @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] 로그인필요 \n" + "[-1] API 통신 실패 \n")})
    public ApiResult<?> callbackApple(UserSocialInformationDto userSocialInformationDto) throws Exception {
        return userBuyerMallService.callbackApple(userSocialInformationDto);
    }

    /**
     * SNS 연결해지
     *
     * @param urSocialId
     * @param provider
     * @throws Exception
     */
    @ApiOperation(value = "SNS 연결해지")
    @PostMapping(value = "/user/buyer/delSyncAccount")
    public ApiResult<?> delSyncAccount(String urSocialId, String provider) throws Exception {
        return userBuyerMallService.delSyncAccount(urSocialId, provider);
    }

    @GetMapping(value = "/user/buyer/getGroupInfoByUser")
    @ApiOperation(value = "등급혜택 메인 조회", httpMethod = "GET", notes = "등급혜택 메인 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GroupInfoByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")
    })
    public ApiResult<?> getGroupInfoByUser() throws Exception {
        return userBuyerMallService.getGroupInfoByUser();
    }

    @ApiOperation(value = "[마이페이지] 상품구매후기 페이지 정보 조회")
    @PostMapping(value = "/user/buyer/getFeedbackInfoByUser")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인필요 \n")
    })
    public ApiResult<?> getFeedbackInfoByUser() throws Exception {
        return userBuyerMallService.getFeedbackInfoByUser();
    }

    @ApiOperation(value = "[마이페이지] 상품구매후기 대상 목록조회")
    @PostMapping(value = "/user/buyer/getFeedbackTargetListByUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "page", required = true, dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "limit", required = true, dataType = "int")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] 0001 - 로그인필요 \n")
    })

    public ApiResult<?> getFeedbackTargetListByUser(int page, int limit) throws Exception {
        return userBuyerMallService.getFeedbackTargetListByUser(page, limit);
    }

    @ApiOperation(value = "[마이페이지] 상품구매후기  목록조회")
    @PostMapping(value = "/user/buyer/getFeedbackByUser")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = FeedbackByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] 0001 - 로그인필요 \n")
    })
    public ApiResult<?> getFeedbackByUser(FeedbackByUserRequestDto dto) throws Exception {
        return userBuyerMallService.getFeedbackByUser(dto);
    }

    /**
     * 상품구매후기 등록
     *
     * @param dto AddFeedbackRequestDto
     * @throws Exception Exception
     */
    @ApiOperation(value = "[마이페이지]  상품구매후기 등록")
    @PostMapping(value = "/user/buyer/addFeedback")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")})
    public ApiResult<?> addFeedback(FeedbackRequestDto dto) throws Exception {
        return userBuyerMallService.addFeedback(dto);
    }


    /**
     * 1:1 문의 현황 조회
     *
     * @param dto QnaInfoByUserRequestDto
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/user/buyer/getQnaInfoByUser")
    @ApiOperation(value = "1:1 문의 현황 조회", httpMethod = "POST", notes = "1:1 문의 현황 조회")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data", response = QnaInfoByUserVo.class),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")})
    public ApiResult<?> getQnaInfoByUser(QnaInfoByUserRequestDto dto) throws Exception {
        return userBuyerMallService.getQnaInfoByUser(dto);
    }

    /**
     * 1:1 문의 목록 조회
     *
     * @param dto QnaListByUserRequestDto
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/user/buyer/getQnaListByUser")
    @ApiOperation(value = "1:1 문의 목록 조회", httpMethod = "POST", notes = "1:1 문의 목록 조회")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data", response = QnaListByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")})
    public ApiResult<?> getQnaListByUser(QnaListByUserRequestDto dto) throws Exception {
        return userBuyerMallService.getQnaListByUser(dto);
    }

    /**
     * 상품 문의 목록 조회
     *
     * @param dto ProductQnaListByUserRequestDto
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/user/buyer/getProductQnaListByUser")
    @ApiOperation(value = "상품 문의 목록 조회", httpMethod = "POST", notes = "상품 문의 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ProductQnaListByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")})
    public ApiResult<?> getProductQnaListByUser(ProductQnaListByUserRequestDto dto) throws Exception {
        return userBuyerMallService.getProductQnaListByUser(dto);
    }

    /**
     * 문의 비공개 처리 - 고객
     *
     * @param csQnaId Long
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/user/buyer/putProductQnaSetSecretByUser")
    @ApiOperation(value = "문의 비공개 처리 - 고객", httpMethod = "POST", notes = "문의 비공개 처리 - 고객")
    @ApiImplicitParams({@ApiImplicitParam(name = "csQnaId", value = "문의 PK", required = true, dataType = "Long")})
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인필요 \n" +
                    "[NOT_QNA] NOT_QNA - 해당하는 문의가 없음 \n" +
                    "[USER_CECHK_FAIL] USER_CECHK_FAIL - 입력한 사용자가 아님"
            )
    })
    public ApiResult<?> putProductQnaSetSecretByUser(Long csQnaId) throws Exception {
        return userBuyerMallService.putProductQnaSetSecretByUser(csQnaId);
    }

    @PostMapping(value = "/user/buyer/putQnaAnswerUserCheck")
    @ApiOperation(value = "답변 확인 처리 - 고객", httpMethod = "POST", notes = "답변 확인 처리 - 고객")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "csQnaId", value = "문의 PK", required = true, dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" )
    })
    public ApiResult<?> putQnaAnswerUserCheckYn(Long csQnaId) throws Exception {
        return userBuyerMallService.putQnaAnswerUserCheckYn(csQnaId);
    }

    @PostMapping(value = "/user/buyer/getEventListFromMyPage")
    @ApiOperation(value = "이벤트 참여현황 목록 조회", httpMethod = "POST", notes = "이벤트 참여현황 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = EventListFromMyPageResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인필요 "
            )
    })
    public ApiResult<?> getEventListFromMyPage(EventListFromMyPageRequestDto dto) throws Exception {
        return userBuyerMallService.getEventListFromMyPage(dto);
    }

    @GetMapping(value = "/user/buyer/getEmployeeDiscount")
    @ApiOperation(value = "임직원 할인정보 조회", httpMethod = "GET", notes = "임직원 할인정보 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = PolicyBenefitEmployeeByUserVo.class),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인필요 "
            )
    })
    public ApiResult<?> getEmployeeDiscount() throws Exception {
        return userBuyerMallService.getEmployeeDiscount();
    }

    @PostMapping(value = "/user/buyer/getEmployeeDiscountPastInfo")
    @ApiOperation(value = "임직원 할인정보 과거내역 조회", httpMethod = "POST", notes = "임직원 할인정보 과거내역 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchDate", value = "조회 년월", required = true, dataType = "String")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = PolicyBenefitEmployeePastInfoByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인필요 "
            )
    })
    public ApiResult<?> getEmployeeDiscountPastInfo(String searchDate) throws Exception {
        return userBuyerMallService.getEmployeeDiscountPastInfo(searchDate);
    }

    @GetMapping(value = "/user/buyer/getDropPageInfo")
    @ApiOperation(value = "회원탈퇴 화면 정보 조회", httpMethod = "GET", notes = "회원탈퇴 화면 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = DropPageResponseDto.class),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")
    })
    public ApiResult<?> getDropPageInfo() throws Exception {
        return userBuyerMallService.getDropPageInfo();
    }

    @PostMapping(value = "/user/buyer/progressUserDrop")
    @ApiOperation(value = "회원탈퇴 진행", httpMethod = "POST", notes = "회원탈퇴 진행")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")
    })
    public ApiResult<?> progressUserDrop(UserDropRequestDto dto) throws Exception {
        return userBuyerMallService.progressUserDrop(dto);
    }

    @GetMapping(value = "/user/buyer/getMypageMainInfo")
    @ApiOperation(value = "마이페이지 메인페이지 정보 조회", httpMethod = "GET", notes = "마이페이지 메인페이지 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")
    })
    public ApiResult<?> getMypageMainInfo() throws Exception {
        return userBuyerMallService.getMypageMainInfo();
    }

    @GetMapping(value = "/user/buyer/getQnaNewAnswer")
    @ApiOperation(value = "사용자 확인해야할 답변 건수 ", httpMethod = "GET", notes = "사용자 확인해야할 답변 건수")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> getQnaNewAnswer() throws Exception {
        return userBuyerMallService.getQnaNewAnswer();
    }

    /**
     * 마이페이지 배송지 목록 조회
     *
     * @param  ilGoodsId
     * @param odOrderDetlId
     * @return List<CommonGetShippingAddressListResultVo>
     * @throws Exception
     */
    @ApiOperation(value = "마이페이지 배송지 목록 조회")
    @GetMapping(value = "/user/buyer/getMypageShippingAddressList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response List<data>", response = CommonGetShippingAddressListResultVo.class),
            @ApiResponse(code = 901, message = "" + "(LOGIN_FAIL) 1208 - 로그인 실패 \n")})
    public ApiResult<?> getMypageShippingAddressList(
            @RequestParam(value = "ilGoodsId", required = false, defaultValue = "0") long ilGoodsId
            ,@RequestParam(value = "odOrderDetlId", required = false, defaultValue = "0") long odOrderDetlId
    ) throws Exception {
        return userBuyerMallService.getMypageShippingAddressList(ilGoodsId,odOrderDetlId);
    }

    @ApiOperation(value = "기존 회원 적립금 정보 조회")
    @PostMapping(value = "/user/buyer/getAsisUserPoint")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = AsisUserPointResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인필요 \n" +
                    "[LOGIN_FAIL] LOGIN_FAIL - 로그인실패"
            )
    })
    public ApiResult<?> getAsisUserPoint(AsisUserPointRequestDto dto) throws Exception {
        return userBuyerMallService.getAsisUserPoint(dto);
    }

    @ApiOperation(value = "기존 회원 적립금 적립 처리")
    @PostMapping(value = "/user/buyer/depositPointByAsisPoint")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = AsisUserPointResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인필요 \n" +
                    "[LOGIN_FAIL] LOGIN_FAIL - 로그인실패 \n" +
                    "[ASIS_OVER_POINT] ASIS_OVER_POINT - 기존 적립금 보다 초과한 적립금 \n" +
                    "[ASIS_API_ERROR] ASIS_API_ERROR - 기존 적립금 보다 초과한 적립금 \n" +
                    "[PARTIAL_DEPOSIT_OVER_LIMIT] PARTIAL_DEPOSIT_OVER_LIMIT - 보유 한도초과로 부분적립 되었습니다 \n" +
                    "[DEPOST_POINT_ERROR] DEPOST_POINT_ERROR - 적립금 적립 오류"
            )
    })
    public ApiResult<?> depositPointByAsisPoint(AsisUserPointRequestDto dto) throws Exception {
        return userBuyerMallService.depositPointByAsisPoint(dto);
    }

    @ApiOperation(value = "마이페이지 > 배송지 변경 가능 여부 체크")
    @PostMapping(value = "/user/buyer/isPossibleChangeDeliveryAddress")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data")
    })
    public ApiResult<?> isPossibleChangeDeliveryAddress(IsPossibleChangeDeliveryAddressRequestDto reqDto) throws Exception {
        return userBuyerMallService.isPossibleChangeDeliveryAddress(reqDto);
    }

}
