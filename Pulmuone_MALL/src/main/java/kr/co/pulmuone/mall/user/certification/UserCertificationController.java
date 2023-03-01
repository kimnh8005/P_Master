package kr.co.pulmuone.mall.user.certification;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.pulmuone.mall.user.certification.service.UserCertificationMallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SHA256Util;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.comm.util.UidUtil;
import kr.co.pulmuone.v1.user.certification.dto.AddSessionUserCertificationRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.FindLoginIdResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.FindPasswordResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.GetEmployeesCertificationRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetEmployeesLoginRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetFindLoginIdRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetFindMaskingLoginIdRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetFindPasswordRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetIsCheckBuyerMoveInfoDto;
import kr.co.pulmuone.v1.user.certification.dto.GetLoginRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetLoginResponseDataDto;
import kr.co.pulmuone.v1.user.certification.dto.GetNonMemberOrderRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionAsisCustomerDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionUserCertificationResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.KmcCertRequestDataDto;
import kr.co.pulmuone.v1.user.certification.dto.KmcDecodeResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.PutPasswordRequestDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetCertificationResultVo;
import lombok.extern.slf4j.Slf4j;

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
@RestController
public class UserCertificationController {

  @Autowired
  private UserCertificationMallService userCertificationMallService;

  @Autowired
  private UserCertificationBiz userCertificationBiz;

  @Value("${app.domain}")
  private String               domain;

  @Value("${kmc.cpId}")
  private String               cpId;

  @Value("${kmc.urlCode}")
  private String               urlCode;

  @RequestMapping(value = "/user/certification/openPersonalCertification")
  public void openPersonalCertification(HttpServletResponse res) throws Exception {

    KmcCertRequestDataDto reqDto = new KmcCertRequestDataDto();

    reqDto.setCpId(cpId);
    reqDto.setUrlCode(urlCode);
    reqDto.setCertNum(UidUtil.randomUUID().toString());
    TimeZone time = TimeZone.getTimeZone("Asia/Seoul");
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN); // 현재
                                                                                        // 서버
                                                                                        // 시각
                                                                                        // 구하기
    formatter.setTimeZone(time);
    reqDto.setDate(formatter.format(new Date()));

    // 요청정보(암호화)
    String tr_cert = userCertificationBiz.getCert(reqDto);
    // 결과수신URL
    String tr_url = domain + "/user/certification/returnPersonalCertification";
    // IFrame사용여부
    String tr_add = "Y";

    try {
      res.setContentType("text/html;charset=UTF-8");

      PrintWriter writer = res.getWriter();
      writer.println("<form name=\"reqKMCISForm\" method=\"POST\" action=\"https://www.kmcert.com/kmcis/web/kmcisReq.jsp\" id=\"reqKMCISForm\">\r\n");
      writer.println("<input type=\"hidden\" name=\"tr_cert\"     value = \"" + tr_cert + "\">");
      writer.println("<input type=\"hidden\" name=\"tr_url\"     value = \"" + tr_url + "\">");
      writer.println("<input type=\"hidden\" name=\"tr_add\"     value = \"" + tr_add + "\">");
      writer.println("</form>");

      writer.println("<script>");
      writer.println("document.reqKMCISForm.submit();");
      writer.println("</script>");
      writer.flush();
      writer.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @RequestMapping(value = "/user/certification/returnPersonalCertification")
  public void returnPersonalCertification(HttpServletRequest req, HttpServletResponse res) throws Exception {

    KmcDecodeResponseDto responseDto = userCertificationBiz.getDecode(req.getParameter("rec_cert").trim(),
                                                                      req.getParameter("certNum").trim());

    String code = UserEnums.Join.NICE_CHECK_FAIL.getCode();
    String massage = UserEnums.Join.NICE_CHECK_FAIL.getMessage();

    if (StringUtil.isNotEmpty(responseDto.getCI()) && StringUtil.isEquals(responseDto.getResult(), "Y")) {

      AddSessionUserCertificationRequestDto addSessionUserCertificationRequestDto = new AddSessionUserCertificationRequestDto();

      // 14 미만 회원 데이터가 있을경우
      if (StringUtil.isNotEmpty(responseDto.getM_name())) {
        addSessionUserCertificationRequestDto.setUserName(responseDto.getM_name());
        addSessionUserCertificationRequestDto.setGender(responseDto.getM_Gender().equals("0") ? "M" : "F");
        addSessionUserCertificationRequestDto.setBirthday(responseDto.getM_birthDay());
      } else {
        addSessionUserCertificationRequestDto.setUserName(responseDto.getName());
        addSessionUserCertificationRequestDto.setGender(responseDto.getGender().equals("0") ? "M" : "F");
        addSessionUserCertificationRequestDto.setBirthday(responseDto.getBirthDay());
      }
      addSessionUserCertificationRequestDto.setMobile(responseDto.getPhoneNo());
      addSessionUserCertificationRequestDto.setCi(responseDto.getCI());

      String beforeUserDropYn = userCertificationBiz.getBeforeUserDropYn(responseDto.getCI());
      addSessionUserCertificationRequestDto.setBeforeUserDropYn(beforeUserDropYn);

      userCertificationBiz.addSessionUserCertification(addSessionUserCertificationRequestDto);

      code = BaseEnums.Default.SUCCESS.getCode();
      massage = BaseEnums.Default.SUCCESS.getMessage();
    }

    res.setContentType("text/html;charset=UTF-8");

    PrintWriter writer = res.getWriter();
    writer.println("<script>");
    writer.println("var data = {code : '" + code + "', message : '" + massage + "', data : null}; ");
    writer.println("	try { ");
    writer.println("		window.opener.returnUserCertification(data); ");
    writer.println("	} ");
    writer.println("	catch(ex) { ");
    writer.println("	 	alert(ex);");
    writer.println("		window.location.href='/error' ");
    writer.println("	} ");
    writer.println("</script>");
  }

  /**
   * 본인인증 정보 조회
   *
   * @param
   * @return GetCertificationResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "본인인증 정보 조회")
  @PostMapping(value = "/user/certification/getSessionUserCertification")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = GetCertificationResultVo.class),
      @ApiResponse(code = 901, message = "" + "[NOT_ANY_CERTI] 1203 - 본인 인증정보 없음 \n") })
  public ApiResult<?> getSessionUserCertification() throws Exception {
    GetCertificationResultVo getCertificationResultVo = new GetCertificationResultVo();

    GetSessionUserCertificationResponseDto getSessionUserCertificationResponseDto =
                                                                                  userCertificationBiz.getSessionUserCertification();
    if (getSessionUserCertificationResponseDto.getCi() != null) {
      getCertificationResultVo.setUserName(getSessionUserCertificationResponseDto.getUserName());
      getCertificationResultVo.setMobile(getSessionUserCertificationResponseDto.getMobile());
      getCertificationResultVo.setBirth(getSessionUserCertificationResponseDto.getBirthday());
      getCertificationResultVo.setBeforeUserDropYn(getSessionUserCertificationResponseDto.getBeforeUserDropYn());

      return ApiResult.success(getCertificationResultVo);
    } else {
      log.info("====1203 본인인증정보가 없습니다.===");
      return ApiResult.result(UserEnums.Join.NOT_ANY_CERTI);
    }
  }

  /**
   * 로그인
   *
   * @param request
   * @return GetLoginResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "로그인")
  @PostMapping(value = "/user/certification/login")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = GetLoginResponseDataDto.class),
      @ApiResponse(code = 901, message = "" + "[LOGIN_FAIL] 1208 - 로그인 실패 \n" + "[TEMP_PASSWORD] 1212 - 임시비밀번호 \n"
          + "[NEED_INFO_CHG_GUIDE] 1207 - 통합몰 전환 가입 안내 필요 \n" + "[RECAPTCHA_FAIL] 1214 - 캡차 인증 실패 \n"
          + "[OVER5_FAIL_PASSWORD] 1209 - 5회 연속 실패 \n" + "[SLEEP_MEMEBER] 1211 - 휴먼계정 \n"
          + "[CHECK_VERSION_UP_CLAUSE] 1213 - 계정된 약관 동의체크 \n") })
  public ApiResult<?> login(GetLoginRequestDto getLoginRequestDto,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
    return userCertificationBiz.login(getLoginRequestDto, request, response);
  }

  /**
   * 아이디 찾기
   *
   * @param getFindMaskingLoginIdRequestDto
   * @return GetFindMaskingLoginIdResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "아이디 찾기")
  @PostMapping(value = "/user/certification/getFindMaskingLoginId")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = FindLoginIdResponseDto.class),
      @ApiResponse(code = 901, message = "" + "[NO_FIND_USERID] 1221 - 아이디 찾을수 없음 \n") })
  public ApiResult<?> getFindMaskingLoginId(GetFindMaskingLoginIdRequestDto getFindMaskingLoginIdRequestDto) throws Exception {
    log.info("====getFindMaskingLoginIdRequestDto {}", getFindMaskingLoginIdRequestDto);

    return userCertificationBiz.getFindMaskingLoginId(getFindMaskingLoginIdRequestDto);
  }

  /**
   * 전체아이디 찾기 조회
   *
   * @param getFindLoginIdRequestDto
   * @return GetFindLoginIdResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "전체아이디 찾기 조회")
  @PostMapping(value = "/user/certification/getFindLoginId")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = FindLoginIdResponseDto.class),
      @ApiResponse(code = 901, message = "" + "[NOT_ANY_CERTI] 1203 - 본인 인증정보 없음 \n"
          + "[NO_FIND_INFO_USER] 1222 - 사용자 정보 없음 \n") })
  public ApiResult<?> getFindLoginId(GetFindLoginIdRequestDto getFindLoginIdRequestDto) throws Exception {
    // 유저네임,모바일 -파라미터
    // ci는 세션
    return userCertificationBiz.getFindLoginId(getFindLoginIdRequestDto);
  }

  /**
   * 비밀번호 찾기
   *
   * @param getFindPasswordRequestDto
   * @return BaseResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "비밀번호 찾기")
  @PostMapping(value = "/user/certification/getFindPassword")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null"),
      @ApiResponse(code = 901, message = "" + "[NO_FIND_INFO_USER] 1222 - 사용자 정보 없음 \n") })
  public ApiResult<?> getFindPassword(GetFindPasswordRequestDto getFindPasswordRequestDto) throws Exception {
    return userCertificationBiz.getFindPassword(getFindPasswordRequestDto);
  }

  /**
   * 비밀번호 찾기
   *
   * @param getFindPasswordRequestDto
   * @return GetFindPasswordCIResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "비밀번호 찾기(본인인증체크)_세션의 이름,전번,ci")
  @PostMapping(value = "/user/certification/getFindPasswordCI")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = FindPasswordResponseDto.class),
      @ApiResponse(code = 901, message = "" + "[NO_FIND_INFO_USER] 1222 - 사용자 정보 없음 \n") })
  public ApiResult<?> getFindPasswordCI(GetFindPasswordRequestDto getFindPasswordRequestDto) throws Exception {
    // 유저네임,모바일 ,로그인아이디 -파라미터
    // ci는 세션
    return userCertificationBiz.getFindPasswordCI(getFindPasswordRequestDto);
  }

  /**
   * 로그아웃
   *
   * @param request
   * @return BaseResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "로그아웃")
  @PostMapping(value = "/user/certification/logout")
  public ApiResult<?> logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return userCertificationBiz.logout(request, response);
  }

  /**
   * 비밀번호 변경
   *
   * @param putPasswordRequestDto
   * @return BaseResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "비밀번호 변경")
  @PostMapping(value = "/user/certification/putPassword")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null"), @ApiResponse(code = 901, message = ""
      + "[SAME_PASSWORD_NOTI] 1224 - 이전 비밀번호 동일 사용 \n" + "[ID_PW_SAME_NOTI] 1225 - 비밀번호에 아이디는 사용하실 수 없습니다 \n") })
  public ApiResult<?> putPassword(PutPasswordRequestDto putPasswordRequestDto) throws Exception {
    return userCertificationBiz.putPassword(putPasswordRequestDto);
  }

  /**
   * 비밀번호 다음에 변경
   *
   * @param
   * @return BaseResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "비밀번호 다음에 변경")
  @PostMapping(value = "/user/certification/putNextPassword")
  public ApiResult<?> putNextPassword() throws Exception {
    return userCertificationBiz.putNextPassword();
  }

  /**
   * 비회원 로그인
   *
   * @param getNonMemberOrderRequestDto
   * @return BaseResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "비회원 로그인")
  @PostMapping(value = "/user/certification/getNonMemberOrder")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null"),
      @ApiResponse(code = 901, message = "" + "[NON_MEMBER_LOGIN_FAIL] 1216 - 비회원 로그인 실패 \n") })
  public ApiResult<?> getNonMemberOrder(GetNonMemberOrderRequestDto getNonMemberOrderRequestDto) throws Exception {
    return userCertificationBiz.getNonMemberOrder(getNonMemberOrderRequestDto);
  }

  /**
   * 비회원 주문조회 (본인인증 체크)
   *
   * @param getNonMemberOrderRequestDto
   * @return BaseResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "비회원 주문조회 (본인인증 체크)")
  @PostMapping(value = "/user/certification/getNonMemberOrderCI")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null"),
      @ApiResponse(code = 901, message = "" + "[NON_MEMBER_LOGIN_FAIL] 1216 - 비회원 로그인 실패 \n") })
  public ApiResult<?> getNonMemberOrderCI(GetNonMemberOrderRequestDto getNonMemberOrderRequestDto) throws Exception {
    return userCertificationBiz.getNonMemberOrderCI(getNonMemberOrderRequestDto);
  }

  /**
   * 휴면회원 CI 조회
   *
   * @param getIsCheckBuyerMoveInfoDto
   * @return BaseResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "휴면회원 CI 조회")
  @PostMapping(value = "/user/certification/isCheckBuyerMoveInfo")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null"),
      @ApiResponse(code = 901, message = "" + "[NOT_ANY_CERTI] 1203 - 본인 인증정보 없음 \n") })
  public ApiResult<?> isCheckBuyerMoveInfo(GetIsCheckBuyerMoveInfoDto getIsCheckBuyerMoveInfoDto) throws Exception {
    return userCertificationBiz.isCheckBuyerMoveInfo(getIsCheckBuyerMoveInfoDto);
  }

  /**
   * 임직원 로그인
   *
   * @param request
   * @return GetLoginResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "임직원 로그인")
  @PostMapping(value = "/user/certification/employeesLogin")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = GetLoginResponseDataDto.class),
      @ApiResponse(code = 901, message = "" + "[LOGIN_FAIL] 1208 - 로그인 실패 \n" + "[TEMP_PASSWORD] 1212 - 임시비밀번호 \n"
          + "[NEED_INFO_CHG_GUIDE] 1207 - 통합몰 전환 가입 안내 필요 \n" + "[RECAPTCHA_FAIL] 1214 - 캡차 인증 실패 \n"
          + "[OVER5_FAIL_PASSWORD] 1209 - 5회 연속 실패 \n" + "[SLEEP_MEMEBER] 1211 - 휴먼계정 \n"
          + "[CHECK_VERSION_UP_CLAUSE] 1213 - 계정된 약관 동의체크 \n") })
  public ApiResult<?> employeesLogin(GetEmployeesLoginRequestDto getEmployeesLoginRequestDto,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
    return userCertificationBiz.employeesLogin(getEmployeesLoginRequestDto, request, response);
  }

  /**
   * 임직원 인증
   *
   * @param getEmployeesCertificationRequestDto
   * @return BaseResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "임직원 인증")
  @PostMapping(value = "/user/certification/employeesCertification")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = GetLoginResponseDataDto.class),
      @ApiResponse(code = 901, message = "" + "[ALREADY_EMPLOYEE_CERTIFI_DONE] 1401 - 이미 임직원인증을 완료 \n") })
  public ApiResult<?> employeesCertification(GetEmployeesCertificationRequestDto getEmployeesCertificationRequestDto) throws Exception {
    return userCertificationBiz.employeesCertification(getEmployeesCertificationRequestDto);
  }

  /**
   * 임직원 인증 확인
   *
   * @param certificationCode
   * @return BaseResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "임직원 인증확인")
  @PostMapping(value = "/user/certification/employeesCertificationVeriyfy")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null"),
      @ApiResponse(code = 901, message = "" + "[FAIL_EMPLOYEE_CERTIFI_CODE] 1402 - 유효하지 않은 인증코드 \n"
          + "[NEED_LOGIN] 0001 - 로그인필요 \n" + "[OVER5_FAIL_CERTIFI_CODE] 1403 - 5회 연속 실패 \n") })
  @ApiImplicitParams({ @ApiImplicitParam(name = "certificationCode", value = "인증코드", dataType = "String") })
  public ApiResult<?> employeesCertificationVeriyfy(@RequestParam(value = "certificationCode", required = true) String certificationCode) throws Exception {
    return userCertificationBiz.employeesCertificationVeriyfy(certificationCode);
  }

  /**
   * 비밀번호 확인
   *
   * @param password
   * @throws Exception
   */
  @ApiOperation(value = "비밀번호 확인")
  @PostMapping(value = "/user/certification/isPasswordCorrect")
  @ApiImplicitParams({ @ApiImplicitParam(name = "password", value = "비밀번호", required = true, dataType = "String") })
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null"),
      @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] 0001 - 로그인필요 \n" + "[CHECK_PASSWORD] 1801 - 비밀번호 확인을 해주세요 \n") })

  public ApiResult<?> isPasswordCorrect(String password) throws Exception {
    String encryptPassword = SHA256Util.getUserPassword(password);// 2020.09.29일 추가
    return userCertificationBiz.isPasswordCorrect(encryptPassword);

  }

  @ApiOperation(value = "비밀번호변경발급코드 조회")
  @PostMapping(value = "/user/certification/getPasswordChangeCd")
  @ApiResponses(value = {
        @ApiResponse(code = 900, message = "response data : null"),
        @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] 0001 - 로그인필요 \n")
  })
  public ApiResult<?> getPasswordChangeCd() throws Exception {
    return userCertificationMallService.getPasswordChangeCd();
  }

  /**
   * 본인인증 정보 조회(as-is회원 배송지 조회 API추가된 서비스)
   *
   * @return GetCertificationResultVo
   * @throws Exception
   */
  @ApiOperation(value = "본인인증 정보 조회(as-is회원 배송지 조회 API추가된 서비스)")
  @PostMapping(value = "/user/join/joinInputInfo")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = GetCertificationResultVo.class),
      @ApiResponse(code = 901, message = "" + "[NOT_ANY_CERTI] 1203 - 본인 인증정보 없음 \n") })
  public ApiResult<?> joinInputInfo() throws Exception {
    GetCertificationResultVo getCertificationResultVo = new GetCertificationResultVo();

    //본인인증 관련 세선 정보 조회
    GetSessionUserCertificationResponseDto getSessionUserCertificationResponseDto =
                                                                                  userCertificationBiz.getSessionUserCertification();

    //AS-IS회원 기본 배송지 관련 세션 조회
    GetSessionAsisCustomerDto getSessionAsisCustomerDto = userCertificationBiz.getSessionAsisCustomerInfo();

    if (getSessionUserCertificationResponseDto.getCi() != null) {
      getCertificationResultVo.setUserName(getSessionUserCertificationResponseDto.getUserName());
      getCertificationResultVo.setMobile(getSessionUserCertificationResponseDto.getMobile());
      getCertificationResultVo.setBirth(getSessionUserCertificationResponseDto.getBirthday());
      getCertificationResultVo.setBeforeUserDropYn(getSessionUserCertificationResponseDto.getBeforeUserDropYn());
      getCertificationResultVo.setAsisLoginId(getSessionAsisCustomerDto.getAsisLoginId());
      getCertificationResultVo.setAsisReceiverZipCode(getSessionAsisCustomerDto.getAsisReceiverZipCd());
      getCertificationResultVo.setAsisReceiverAddress1(getSessionAsisCustomerDto.getAsisReceiverAddr1());
      getCertificationResultVo.setAsisReceiverAddress2(getSessionAsisCustomerDto.getAsisReceiverAddr2());
      getCertificationResultVo.setAsisBuildingCode(getSessionAsisCustomerDto.getAsisBuildingCode());

      return ApiResult.success(getCertificationResultVo);
    } else {
      log.info("====1203 본인인증정보가 없습니다.===");
      return ApiResult.result(UserEnums.Join.NOT_ANY_CERTI);
    }
  }
}
