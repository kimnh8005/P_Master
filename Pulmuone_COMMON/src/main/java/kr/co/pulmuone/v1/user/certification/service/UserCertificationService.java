package kr.co.pulmuone.v1.user.certification.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.pulmuone.v1.comm.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.icert.comm.secu.IcertSecuManager;

import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums.AutoLoginKeyActionCode;
import kr.co.pulmuone.v1.comm.mapper.user.certification.UserCertificationMapper;
import kr.co.pulmuone.v1.comm.util.asis.AsisUserApiUtil;
import kr.co.pulmuone.v1.comm.util.asis.dto.SearchCustomerDeliveryDto;
import kr.co.pulmuone.v1.comm.util.asis.dto.SearchCustomerDeliveryListResponseDto;
import kr.co.pulmuone.v1.comm.util.asis.dto.SearchCustomerRsrvTotalResponseDto;
import kr.co.pulmuone.v1.comm.util.asis.dto.UserInfoCheckResponseDto;
import kr.co.pulmuone.v1.comm.util.google.Recaptcha;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.shopping.cart.service.ShoppingCartBiz;
import kr.co.pulmuone.v1.shopping.recently.service.ShoppingRecentlyBiz;
import kr.co.pulmuone.v1.user.buyer.dto.CommonSaveShippingAddressRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.DoLoginRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.DoLoginResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetChangeClauseNoAgreeListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetShippingAddressListResultVo;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.user.certification.dto.AddSessionEmployeeCertificationRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.AddSessionShippingRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.AddSessionUserCertificationRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.FindLoginIdResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.FindPasswordResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.GetBuyerCertificationByAutoLoginKeyResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.GetCertificationRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetCheckDuplicateCIRequestDto;
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
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionUserCertificationResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSocialLoginDataRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.KmcCertRequestDataDto;
import kr.co.pulmuone.v1.user.certification.dto.KmcDecodeResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.PutPasswordChangeCdRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.PutPasswordRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.UserSocialInformationDto;
import kr.co.pulmuone.v1.user.certification.dto.vo.CertificationVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.ConnectionLogVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.EmployeeCertificationResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetBuyerCertificationByAutoLoginKeyResponseVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetBuyerSessionDataResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetFindLoginIdResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetFindMaskingLoginIdResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetFindPasswordCIResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetFindPasswordResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetLoginDataResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetSocialLoginDataResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetStopReasonResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetcheckDuplicateCIResultVo;
import kr.co.pulmuone.v1.user.device.service.UserDeviceBiz;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetIsCheckUserMoveResultVo;
import kr.co.pulmuone.v1.user.dormancy.service.UserDormancyBiz;
import kr.co.pulmuone.v1.user.join.service.UserJoinBiz;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataAutoLoginDto;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataCertificationDto;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataMakettingDto;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataNotiDto;
import kr.co.pulmuone.v1.user.login.dto.UnlinkAccountRequestDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserCertificationService {

  @Autowired
  private UserCertificationMapper certificationMapper;

  @Autowired
  private UserDormancyBiz         userDormancyBiz;

  @Autowired
  private UserDeviceBiz           userDeviceBiz;

  @Autowired
  private UserBuyerBiz            userBuyerBiz;

  @Autowired
  private UserJoinBiz             userJoinBiz;

  @Autowired
  AsisUserApiUtil                 asisUserApiUtil;

  @Autowired
  Recaptcha                       googleRecaptcha;

  @Value("${database.encryption.key}")
  private String                  databaseEncryptionKey;

  @Value("${resource.server.url.bos}")
  private String serverUrlBos;

  @Autowired
  private SendTemplateBiz sendTemplateBiz;

  @Autowired
  private ComnBizImpl comnBizImpl;

  @Autowired
  private ShoppingCartBiz shoppingCartBiz;

  @Autowired
  private PointBiz pointBiz;

  @Autowired
  private ShoppingRecentlyBiz shoppingRecentlyBiz;

  protected void addSessionShipping(AddSessionShippingRequestDto addSessionShippingRequestDto) {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

    buyerVo.setReceiverName(addSessionShippingRequestDto.getReceiverName());
    buyerVo.setReceiverZipCode(addSessionShippingRequestDto.getReceiverZipCode());
    buyerVo.setReceiverAddress1(addSessionShippingRequestDto.getReceiverAddress1());
    buyerVo.setReceiverAddress2(addSessionShippingRequestDto.getReceiverAddress2());
    buyerVo.setBuildingCode(addSessionShippingRequestDto.getBuildingCode());// 건물관리번호
    buyerVo.setReceiverMobile(addSessionShippingRequestDto.getReceiverMobile());
    buyerVo.setAccessInformationType(addSessionShippingRequestDto.getAccessInformationType());
    buyerVo.setAccessInformationPassword(addSessionShippingRequestDto.getAccessInformationPassword());
    buyerVo.setShippingComment(addSessionShippingRequestDto.getShippingComment());
    buyerVo.setSelectBasicYn(addSessionShippingRequestDto.getSelectBasicYn());
    buyerVo.setShippingAddressId(addSessionShippingRequestDto.getShippingAddressId());

    SessionUtil.setUserVO(buyerVo);
  }

  protected GetSessionShippingResponseDto getSessionShipping() throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (StringUtil.isEmpty(buyerVo.getReceiverName())) {
      log.info("===세션에 정보가 없습니다===");
    }
    GetSessionShippingResponseDto getSessionShippingResponseDto = new GetSessionShippingResponseDto();

    getSessionShippingResponseDto.setReceiverName(buyerVo.getReceiverName());
    getSessionShippingResponseDto.setReceiverZipCode(buyerVo.getReceiverZipCode());
    getSessionShippingResponseDto.setReceiverAddress1(buyerVo.getReceiverAddress1());
    getSessionShippingResponseDto.setReceiverAddress2(buyerVo.getReceiverAddress2());
    getSessionShippingResponseDto.setBuildingCode(buyerVo.getBuildingCode());
    getSessionShippingResponseDto.setReceiverMobile(buyerVo.getReceiverMobile());
    getSessionShippingResponseDto.setAccessInformationType(buyerVo.getAccessInformationType());
    getSessionShippingResponseDto.setAccessInformationPassword(buyerVo.getAccessInformationPassword());
    getSessionShippingResponseDto.setShippingComment(buyerVo.getShippingComment());
    getSessionShippingResponseDto.setSelectBasicYn(buyerVo.getSelectBasicYn());
    getSessionShippingResponseDto.setShippingAddressId(buyerVo.getShippingAddressId());

    return getSessionShippingResponseDto;
  }

  protected void addSessionUserCertification(AddSessionUserCertificationRequestDto addSessionUserCertificationRequestDto) throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

    buyerVo.setPersonalCertificationUserName(addSessionUserCertificationRequestDto.getUserName());
    buyerVo.setPersonalCertificationMobile(addSessionUserCertificationRequestDto.getMobile());
    buyerVo.setPersonalCertificationGender(addSessionUserCertificationRequestDto.getGender());
    buyerVo.setPersonalCertificationCiCd(addSessionUserCertificationRequestDto.getCi());
    buyerVo.setPersonalCertificationBirthday(addSessionUserCertificationRequestDto.getBirthday());
    buyerVo.setPersonalCertificationBeforeUserDropYn(addSessionUserCertificationRequestDto.getBeforeUserDropYn());
    SessionUtil.setUserVO(buyerVo);
  }

  protected GetSessionUserCertificationResponseDto getSessionUserCertification() throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

    if (StringUtil.isEmpty(buyerVo.getPersonalCertificationUserName())) {
      log.info("===세션에 정보가 없습니다===");
    }
    GetSessionUserCertificationResponseDto getSessionUserCertificationResponseDto = new GetSessionUserCertificationResponseDto();

    getSessionUserCertificationResponseDto.setUserName(buyerVo.getPersonalCertificationUserName());
    getSessionUserCertificationResponseDto.setMobile(buyerVo.getPersonalCertificationMobile() == null ? null : buyerVo.getPersonalCertificationMobile().replaceAll("-", ""));
    getSessionUserCertificationResponseDto.setGender(buyerVo.getPersonalCertificationGender());
    getSessionUserCertificationResponseDto.setCi(buyerVo.getPersonalCertificationCiCd());
    getSessionUserCertificationResponseDto.setBirthday(buyerVo.getPersonalCertificationBirthday());
    getSessionUserCertificationResponseDto.setBeforeUserDropYn(buyerVo.getPersonalCertificationBeforeUserDropYn());

    return getSessionUserCertificationResponseDto;
  }


  /**
   * AS-IS 회원일때, 배송지 조회 API 호출 후 세션에 저장할 정보 세팅
   *
   * @param customerNumber
   * @return GetSessionAsisCustomerDto
   * @throws Exception
   */
  protected GetSessionAsisCustomerDto getSessionAsisCustomerInfo() throws Exception{
	  GetSessionAsisCustomerDto asisCustomerDto = new GetSessionAsisCustomerDto();
	  BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

	  if (StringUtil.isEmpty(buyerVo.getAsisCustomerNumber())) {
		  log.info("===세션에 as-is회원 고객번호가 없습니다===");
		  return asisCustomerDto;
	  }else {
		  asisCustomerDto.setAsisLoginId(buyerVo.getAsisLoginId());
		  asisCustomerDto.setAsisCustomerNumber(buyerVo.getAsisCustomerNumber());
	  }

	  // AS-IS 회원 기본 배송지 정보 조회 API 호출
	  SearchCustomerDeliveryDto searchCustomerDeliveryDto = asisUserApiUtil.searchCustomerBasicDelivery(buyerVo.getAsisCustomerNumber());

	  if(searchCustomerDeliveryDto != null && searchCustomerDeliveryDto.getBasicYn() != null) {
		  asisCustomerDto.setAsisReceiverZipCd(searchCustomerDeliveryDto.getReceiverZipCd());
		  asisCustomerDto.setAsisReceiverAddr1(searchCustomerDeliveryDto.getReceiverAddr1());
		  asisCustomerDto.setAsisReceiverAddr2(searchCustomerDeliveryDto.getReceiverAddr2());
		  asisCustomerDto.setAsisBuildingCode(searchCustomerDeliveryDto.getBuildingCd());
	  }

	  return asisCustomerDto;
  }


  /**
   * 본인인증 정보 조회 - 가입가능 체크 ci파라미터
   *
   * @param
   * @return BaseResponseDto
   * @throws Exception
   */
  protected ApiResult<?> checkDuplicateJoinUser() throws Exception {
    String ci = getSessionUserCertification().getCi();
    String birthday = getSessionUserCertification().getBirthday();

    log.info("====ci===" + ci);
    GetCheckDuplicateCIRequestDto getcheckDuplicateCIRequestDto = new GetCheckDuplicateCIRequestDto();

    getcheckDuplicateCIRequestDto.setCiCd(ci);

    BaseResponseDto result = new BaseResponseDto();

    GetcheckDuplicateCIResultVo data = certificationMapper.checkDuplicateCI(getcheckDuplicateCIRequestDto);

    if (data == null) {
      return ApiResult.fail();
    }

    log.info("====data ===" + data);
    log.info("====data.getCheckCode() ===" + data.getCheckCode());
    log.info("====UserDivEnum.BASIC.getCode()) ===" + UserEnums.BuyerStatusCode.BASIC.getCode());
    if (userJoinBiz.isCheckUnderAge14(birthday)) {
      log.info("====14세 이상  ===");
      if (data.getCheckCode().equals(UserEnums.BuyerStatusCode.BASIC.getCode())) {
        log.info("====1204  정상회원 CI중복  ===");
        return ApiResult.result(UserEnums.Join.NORMAL_MEMBER_CI_DUPLICATE);
      } else if (data.getCheckCode().equals(UserEnums.BuyerStatusCode.STOP.getCode())) {
        log.info("====1205  정지 회원 CI 중복  ===");
        return ApiResult.result(UserEnums.Join.STOP_MEMBER_CI_DUPLICATE);
//      } else if (data.getCheckCode().equals(UserEnums.BuyerStatusCode.DROP.getCode())) {
//        log.info("====1206  탈퇴 회원 CI 중복  ===");
//        return ApiResult.result(UserEnums.Join.WITHDRAWAL_MEMBER_CI_DUPLICATE);
      } else {
        return ApiResult.success();
      }
    } else if (!userJoinBiz.isCheckUnderAge14(birthday)) {
      log.info("===1202 14세 미만은 회원가입불가  ===");
      return ApiResult.result(UserEnums.Join.UNDER_14_AGE_NOT_ALLOW);
    }

    return ApiResult.success();
  }

  /**
   * 아이디 찾기
   *
   * @param getFindMaskingLoginIdRequestDto
   * @return GetFindMaskingLoginIdResponseDto
   * @throws Exception
   */
  protected ApiResult<?> getFindMaskingLoginId(GetFindMaskingLoginIdRequestDto getFindMaskingLoginIdRequestDto) throws Exception {
    getFindMaskingLoginIdRequestDto.setMobile(getFindMaskingLoginIdRequestDto.getMobile().replaceAll("-", ""));

    log.info("====getFindMaskingLoginIdRequestDto ===" + getFindMaskingLoginIdRequestDto);

    GetFindMaskingLoginIdResultVo data = certificationMapper.getFindMaskingLoginId(getFindMaskingLoginIdRequestDto);
    log.info("====data ===" + data);

    if (data != null) {
      log.info("====data.getMaskingLoginId() ===" + data.getMaskingLoginId());
      FindLoginIdResponseDto findLoginIdDto = new FindLoginIdResponseDto();
      findLoginIdDto.setLoginId(data.getMaskingLoginId());
      return ApiResult.success(findLoginIdDto);
    } else {
      log.info("====1221  아이디 찾을수 없음  ===");
      return ApiResult.result(UserEnums.Join.NO_FIND_USERID);
    }
  }

  /**
   * 전체 아이디 찾기 조회
   *
   * @param getFindLoginIdRequestDto
   * @return GetFindLoginIdResponseDto
   * @throws Exception
   */
  protected ApiResult<?> getFindLoginId(GetFindLoginIdRequestDto getFindLoginIdRequestDto) throws Exception {
    GetSessionUserCertificationResponseDto getSessionUserCertificationResponseDto = getSessionUserCertification();

    if (getSessionUserCertificationResponseDto.getCi() == null) {
      return ApiResult.result(UserEnums.Join.NOT_ANY_CERTI);
    } else {
      String ciCd = getSessionUserCertificationResponseDto.getCi();

      log.info("====getFindLoginId==파리미터 =userName==mobile===");
      GetCertificationRequestDto getCertificationRequestDto = new GetCertificationRequestDto();

      getCertificationRequestDto.setUserName(getFindLoginIdRequestDto.getUserName());
      getCertificationRequestDto.setMobile(getFindLoginIdRequestDto.getMobile().replaceAll("-", ""));
      getCertificationRequestDto.setCiCd(ciCd);

      GetFindLoginIdResultVo data = certificationMapper.getFindLoginId(getCertificationRequestDto);

      if (data != null) {
        FindLoginIdResponseDto findLoginIdDto = new FindLoginIdResponseDto();
        findLoginIdDto.setLoginId(data.getLoginId());
        return ApiResult.success(findLoginIdDto);
      } else {
        log.info("====1221  아이디 찾을수 없음   ===");
        return ApiResult.result(UserEnums.Join.NO_FIND_USERID);
      }
    }
  }

  /**
   * 비밀번호 찾기
   *
   * @param getFindPasswordRequestDto
   * @return ResponseDto<GetLatestClauseResponseDto>
   * @throws Exception
   */
  protected ApiResult<?> getFindPassword(GetFindPasswordRequestDto getFindPasswordRequestDto) throws Exception {
    GetFindPasswordResultVo data = certificationMapper.getFindPassword(getFindPasswordRequestDto);

    log.info("====data ===" + data);
    if (data != null) {
      return ApiResult.success();
    } else {
      log.info("====1222 사용자정보없음  ===");
      return ApiResult.result(UserEnums.Join.NO_FIND_INFO_USER);
    }
  }

  /**
   * 비밀번호 찾기 (본인인증 체크)
   *
   * @param getFindPasswordRequestDto
   * @return GetFindPasswordCIResponseDto
   * @throws Exception
   */
  protected ApiResult<?> getFindPasswordCI(GetFindPasswordRequestDto getFindPasswordRequestDto) throws Exception {
    String ciCd = getSessionUserCertification().getCi();

    // 비밀번호
    GetCertificationRequestDto getCertificationRequestDto = new GetCertificationRequestDto();

    getCertificationRequestDto.setUserName(getFindPasswordRequestDto.getUserName());
    getCertificationRequestDto.setMobile(getFindPasswordRequestDto.getMobile().replaceAll("-", ""));
    getCertificationRequestDto.setLoginId(getFindPasswordRequestDto.getLoginId());
    getCertificationRequestDto.setCiCd(ciCd);

    GetFindPasswordCIResultVo data = certificationMapper.getFindPasswordCI(getCertificationRequestDto);

    log.info("====data ===" + data);
    if (data != null) {
      String passwordChangeCd = makePasswordChangeCd();
      PutPasswordChangeCdRequestDto putPasswordChangeCdRequestDto = new PutPasswordChangeCdRequestDto();
      putPasswordChangeCdRequestDto.setUrUserId(data.getUrUserId());
      putPasswordChangeCdRequestDto.setPasswordChangeCd(passwordChangeCd);

      putPasswordChangeCd(putPasswordChangeCdRequestDto);

      FindPasswordResponseDto findPasswordResponseDto = new FindPasswordResponseDto();
      findPasswordResponseDto.setPasswordChangeCd(passwordChangeCd);
      return ApiResult.success(findPasswordResponseDto);
    } else {
      log.info("====1222 사용자정보없음  ===");
      return ApiResult.result(UserEnums.Join.NO_FIND_INFO_USER);
    }
  }

  protected String makePasswordChangeCd() throws Exception {
    return UidUtil.randomUUID().toString();
  }

  protected String getBeforeUserDropYn(String ciCd) throws Exception {
    return certificationMapper.getBeforeUserDropYn(ciCd);
  }

  protected void putPasswordChangeCd(PutPasswordChangeCdRequestDto putPasswordChangeCdRequestDto) throws Exception {
    certificationMapper.putPasswordChangeCd(putPasswordChangeCdRequestDto);
  }

  /**
   * 로그인
   *
   * @param request,HttpServletRequest,HttpServletResponse
   * @return GetLoginResponseDto
   * @throws Exception
   */
  protected ApiResult<?> login(GetLoginRequestDto getLoginRequestDto,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    GetLoginResponseDataDto getLoginResponseDataDto = new GetLoginResponseDataDto();
    boolean useCaptcha = false;

    DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
    doLoginRequestDto.setLoginId(getLoginRequestDto.getLoginId());
    doLoginRequestDto.setPassword(getLoginRequestDto.getPassword());
    doLoginRequestDto.setEncryptPassword(SHA256Util.getUserPassword(getLoginRequestDto.getPassword()));
    if (StringUtil.isNotEmpty(getLoginRequestDto.getAutoLogin())) {
      doLoginRequestDto.setAutoLogin(getLoginRequestDto.getAutoLogin().equals("Y"));
    }
    if (StringUtil.isNotEmpty(getLoginRequestDto.getSaveLoginId())) {
      doLoginRequestDto.setSaveLoginId(getLoginRequestDto.getSaveLoginId().equals("Y"));
    }
    if (StringUtil.isNotEmpty(getLoginRequestDto.getCaptcha())) {
      useCaptcha = true;
      doLoginRequestDto.setCaptcha(getLoginRequestDto.getCaptcha());
    }
    ApiResult<?> result = doLogin(doLoginRequestDto, useCaptcha, request, response);
    DoLoginResponseDto doLoginResponseDto = (DoLoginResponseDto) result.getData();

    // 로그인 처리
    if (doLoginResponseDto != null) {
      getLoginResponseDataDto.setUrUserId(doLoginResponseDto.getUrUserId());
      if (doLoginResponseDto.getCertification() != null) {
        getLoginResponseDataDto.setCertification(doLoginResponseDto.getCertification());
      }
      if (doLoginResponseDto.getClause() != null) {
        getLoginResponseDataDto.setClause(doLoginResponseDto.getClause());
      }
      if (doLoginResponseDto.getMaketting() != null) {
        getLoginResponseDataDto.setMaketting(doLoginResponseDto.getMaketting());
      }
      if (doLoginResponseDto.getNoti() != null) {
        getLoginResponseDataDto.setNoti(doLoginResponseDto.getNoti());
      }
      if (doLoginResponseDto.getStop() != null) {
        getLoginResponseDataDto.setStop(doLoginResponseDto.getStop());
      }
      if (doLoginResponseDto.getAutoLogin() != null) {
        getLoginResponseDataDto.setAutoLogin(doLoginResponseDto.getAutoLogin());
      }
    }else {

    	GetLoginDataResultVo getLoginDataResultVo = certificationMapper.getLoginData(doLoginRequestDto);

    	//로그인 실패시 회원접속 실패로그 추가(회원정보 있는 회원만)
    	if(getLoginDataResultVo != null) {
    	String urPcidCd = CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY);
/*
    	String ip = request.getHeader("X-Forwarded-For");
    	if (ip == null)
    		ip = request.getRemoteAddr();
*/
	    String ip = SystemUtil.getIpAddress(request);

    	ConnectionLogVo connectionLogVo = new ConnectionLogVo();
    	connectionLogVo.setUrPcidCd(urPcidCd);
    	connectionLogVo.setIp(ip);
    	connectionLogVo.setSuccessYn(UserEnums.LoginSuccessStatus.FAIL.getCode());
    	connectionLogVo.setUrUserId(getLoginDataResultVo.getUrUserId());
    	certificationMapper.addConnectionLog(connectionLogVo);
    	}
    }

    return ApiResult.result(getLoginResponseDataDto, result.getMessageEnum());
  }

  protected ApiResult<?> doLogin(DoLoginRequestDto doLoginRequestDto,
                                 boolean useCaptcha,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
    DoLoginResponseDto result = new DoLoginResponseDto();

    // 세션 생성
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

    // loginId 아이디로만 조회
    GetLoginDataResultVo getLoginDataResultVo = certificationMapper.getLoginData(doLoginRequestDto);

    // 로그인 실패시
    int total = certificationMapper.getisCheckLogin(doLoginRequestDto);
    if (total == 0) { // 실패
      if (getLoginDataResultVo == null) {
        // as-is회원 API
    	UserInfoCheckResponseDto userInfoCheckDto = asisUserApiUtil.userInfoCheck(doLoginRequestDto.getLoginId(), doLoginRequestDto.getPassword());

    	if(userInfoCheckDto.getResultCode() != null && userInfoCheckDto.getResultCode().equals(UserEnums.AsisUserInfoCheckResult.SUCCESS.getCode())) {
    		//as-is회원일 때 회원 아이디&고객번호 세션저장
            buyerVo.setAsisLoginId(doLoginRequestDto.getLoginId());
            buyerVo.setAsisCustomerNumber(userInfoCheckDto.getCustomerNumber());
            SessionUtil.setUserVO(buyerVo);

            // 1207 통합몰 전환 가입 안내 필요
            return ApiResult.result(UserEnums.Join.NEED_INFO_CHG_GUIDE);
    	}else {
    		// 1208 로그인 실패
            return ApiResult.result(UserEnums.Join.LOGIN_FAIL);
    	}
      } else {
        // 회원 - 비밀번호 실패 제한 수
        int urLoginFailCount = getLoginDataResultVo.getUrLoginFailCount();
        // 실패 횟수 증가
        certificationMapper.putLoginFailCountIncrease(getLoginDataResultVo.getUrUserId());
        // 실제 실패 횟수
        int failCnt = getLoginDataResultVo.getFailCnt() + 1;
        // 캡차 정보 체크
        // log.info("doLoginRequestDto==>" + doLoginRequestDto);
        // log.info("useCaptcha==>" + useCaptcha);
        // log.info("529라인 getFailCnt==>" + failCnt);
        if (useCaptcha) {
          // log.info("doLoginRequestDto..getCaptcha()" +
          // doLoginRequestDto.getCaptcha());
          if (doLoginRequestDto.getCaptcha() != null && !doLoginRequestDto.getCaptcha().equals("")) {
            // API 통신 처리
            if (!googleRecaptcha.siteVerify(doLoginRequestDto.getCaptcha())) {
              // 1214 캡차 인증 실패 리턴
              return ApiResult.result(UserEnums.Join.RECAPTCHA_FAIL);
            }
          }
        }
        if (failCnt >= urLoginFailCount) {
          // 1209 5회 연속 실패
          return ApiResult.result(UserEnums.Join.OVER5_FAIL_PASSWORD);
        } else {
          // 1208 로그인 실패
          return ApiResult.result(UserEnums.Join.LOGIN_FAIL);
        }
      }
    } else// 로그인성공
    {
      if (useCaptcha) {
        if (doLoginRequestDto.getCaptcha() != null && !doLoginRequestDto.getCaptcha().equals("")) {
          // API 통신 처리
          if (!googleRecaptcha.siteVerify(doLoginRequestDto.getCaptcha())) {
            // 1214 캡차 인증 실패 리턴
            return ApiResult.result(UserEnums.Join.RECAPTCHA_FAIL);
          }
        }
      }
    }

    String urUserId = getLoginDataResultVo.getUrUserId();

    // 휴면 회원 체크가 먼저되어야 한다. 정지회원을 먼저 체크하면 UR_BUYER테이블에 휴먼정보가 없기때문에 에러발생함
    GetIsCheckUserMoveResultVo getIsCheckUserMoveResultVo = userDormancyBiz.isCheckUserMove(urUserId);

    if (getIsCheckUserMoveResultVo != null) {
      // 1211 휴면계정
      String passwordChangeCd = makePasswordChangeCd();
      PutPasswordChangeCdRequestDto putPasswordChangeCdRequestDto = new PutPasswordChangeCdRequestDto();
      putPasswordChangeCdRequestDto.setPasswordChangeCd(passwordChangeCd);
      putPasswordChangeCdRequestDto.setUrUserId(urUserId);
      this.putPasswordChangeCd(putPasswordChangeCdRequestDto);

      return ApiResult.result(UserEnums.Join.SLEEP_MEMEBER);
    }

    // 정지 상태 체크
    if (StringUtil.isNotEmpty(getLoginDataResultVo.getStatus())) {
      if (getLoginDataResultVo.getStatus().equals("BUYER_STATUS.STOP")) { // 활동정지
        GetStopReasonResultVo getStopReasonResultVo = certificationMapper.getStopReason(urUserId);

        // 1210 정지계정
        result.setStop(getStopReasonResultVo);
        return ApiResult.result(result, UserEnums.Join.STOP_MEMEBER);
      }
    }

    // 임시 비밀번호 체크
    if (getLoginDataResultVo.getTmprrYn().equals("Y")) {

      //임시 비밀번호 유효시간이 지났을 경우
      if(getLoginDataResultVo.getTmprrExpirationYn().equals("Y")) {
    	  return ApiResult.result(UserEnums.Join.TEMP_PASSWORD_DATE_EXPIRATION);
      }

      String passwordChangeCd = makePasswordChangeCd();
      // 1212 임시비밀번호
      DoLoginResponseDataCertificationDto doLoginResponseDataCertificationDto = new DoLoginResponseDataCertificationDto();
      doLoginResponseDataCertificationDto.setLoginId(doLoginRequestDto.getLoginId());
      doLoginResponseDataCertificationDto.setDefaultPwdChangePeriod(getLoginDataResultVo.getDefaultPwdChangePeriod());
      doLoginResponseDataCertificationDto.setDefaultPwdFailLimitCount(getLoginDataResultVo.getDefaultPwdFailLimitCount());

      PutPasswordChangeCdRequestDto putPasswordChangeCdRequestDto = new PutPasswordChangeCdRequestDto();
      putPasswordChangeCdRequestDto.setPasswordChangeCd(passwordChangeCd);
      putPasswordChangeCdRequestDto.setUrUserId(urUserId);
      this.putPasswordChangeCd(putPasswordChangeCdRequestDto);

      // 임시 비밀번호 리턴
      doLoginResponseDataCertificationDto.setPasswordChangeCd(passwordChangeCd);
      result.setCertification(doLoginResponseDataCertificationDto);
      return ApiResult.result(result, UserEnums.Join.TEMP_PASSWORD);
    }

    List<GetChangeClauseNoAgreeListResultVo> getChangeClauseNoAgreeListResultVo =
                                                                                userBuyerBiz.getChangeClauseNoAgreeList(urUserId);

    log.info("getChangeClauseNoAgreeListResultVo==>" + getChangeClauseNoAgreeListResultVo);
    // 개정 약관 필수 체크
    if (getChangeClauseNoAgreeListResultVo != null && getChangeClauseNoAgreeListResultVo.size() > 0) {
      // 1213 개정된 약간 동의체크
      result.setClause(getChangeClauseNoAgreeListResultVo);

      return ApiResult.result(result, UserEnums.Join.CHECK_VERSION_UP_CLAUSE);
    }

    // 로그인 실패 횟수 초기화
    certificationMapper.putLoginFailCountClear(urUserId);

    // 새션정보 생성할 회원정보 조회
    GetBuyerSessionDataResultVo getBuyerSessionDataResultVo = certificationMapper.getBuyerSessionData(urUserId,
                                                                                                      databaseEncryptionKey);
    if (getBuyerSessionDataResultVo == null)
    {
    	return ApiResult.result(UserEnums.Join.NO_FIND_USERID);
    }

    //회원접속 성공로그 추가
    String urPcidCd = CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY);
/*
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null)
      ip = request.getRemoteAddr();
*/
    String ip = SystemUtil.getIpAddress(request);

    ConnectionLogVo connectionLogVo = new ConnectionLogVo();
	connectionLogVo.setUrPcidCd(urPcidCd);
	connectionLogVo.setIp(ip);
	connectionLogVo.setSuccessYn(UserEnums.LoginSuccessStatus.SUCCESS.getCode());
	connectionLogVo.setUrUserId(getLoginDataResultVo.getUrUserId());
    certificationMapper.addConnectionLog(connectionLogVo);

    String urConnectLogId = connectionLogVo.getUrConnectLogId();

    // 세션 생성
    SessionUtil.createNewSessionAfterLogin(); // 로그인 성공시 세션 변경

    buyerVo.setUrUserId(getBuyerSessionDataResultVo.getUrUserId());
    buyerVo.setUserName(getBuyerSessionDataResultVo.getUserName());
    buyerVo.setLoginId(getBuyerSessionDataResultVo.getLoginId());
    buyerVo.setUserMobile(getBuyerSessionDataResultVo.getUserMobile());
    buyerVo.setUserEmail(getBuyerSessionDataResultVo.getUserEmail());
    buyerVo.setUrConnectLogId(urConnectLogId);
    buyerVo.setUrErpEmployeeCode(StringUtil.nvl(getBuyerSessionDataResultVo.getUrErpEmployeeCode()));
    buyerVo.setUrGroupId(getBuyerSessionDataResultVo.getUrGroupId());

    SessionUtil.setUserVO(buyerVo);

    // 아이디 저장
    if (doLoginRequestDto.isSaveLoginId()) {
      CookieUtil.setCookie(response, "saveLoginId", getBuyerSessionDataResultVo.getLoginId(), (60 * 60 * 24 * 365));
    }
    // 자동 로그인 저장
    if (doLoginRequestDto.isAutoLogin()) {
      String autoLoginKey = getAutoLoginKey();
      putAutoLoginKey(urUserId, autoLoginKey);
      DoLoginResponseDataAutoLoginDto autoLoginDto = new DoLoginResponseDataAutoLoginDto();
      autoLoginDto.setAutoLoginKeyActionCode(AutoLoginKeyActionCode.INSERT.getCode());
      autoLoginDto.setAutoLoginKey(urUserId + "-" + autoLoginKey);
      result.setAutoLogin(autoLoginDto);
    }

    // 장바구니 비회원 업데이트
    // 쿠키 urPcidCd 가지고 와서 처리
    shoppingCartBiz.loginCartMerge(urPcidCd, Long.valueOf(urUserId));

    // 최근 본상품 비회원 업데이트
    shoppingRecentlyBiz.mapRecentlyViewUserId(urPcidCd, Long.valueOf(urUserId));

    // 회원 디바이스 정보 업데이트
    // APP 개발시 대응 예정
    String deviceId = "";
    userDeviceBiz.putMemberMapping(deviceId, urUserId);

    // 마지막 비밀번호 변경 체크
    DoLoginResponseDataNotiDto doLoginResponseDataNotiDto = new DoLoginResponseDataNotiDto();
    DoLoginResponseDataMakettingDto doLoginResponseDataMakettingDto = new DoLoginResponseDataMakettingDto();

    log.info("비밀번호 변경일자 pwdChangeDate==>" + getLoginDataResultVo.getPwdChangeDate());
    log.info("비밀번호 바꾼지 3개월 초과여부 getPasswordChangeThreeMonOverYn ==>" + getLoginDataResultVo.getPasswordChangeThreeMonOverYn());

    if (getLoginDataResultVo.getPasswordChangeThreeMonOverYn().equals("Y")) {

      // 3개월 지났을 경우 비빌번호 변경할수 있도록 로직 추가 START
      String passwordChangeCd = makePasswordChangeCd();
      DoLoginResponseDataCertificationDto doLoginResponseDataCertificationDto = new DoLoginResponseDataCertificationDto();
      doLoginResponseDataCertificationDto.setLoginId(doLoginRequestDto.getLoginId());
      doLoginResponseDataCertificationDto.setDefaultPwdChangePeriod(getLoginDataResultVo.getDefaultPwdChangePeriod());
      doLoginResponseDataCertificationDto.setDefaultPwdFailLimitCount(getLoginDataResultVo.getDefaultPwdFailLimitCount());
      PutPasswordChangeCdRequestDto putPasswordChangeCdRequestDto = new PutPasswordChangeCdRequestDto();
      putPasswordChangeCdRequestDto.setPasswordChangeCd(passwordChangeCd);
      putPasswordChangeCdRequestDto.setUrUserId(urUserId);
      this.putPasswordChangeCd(putPasswordChangeCdRequestDto);
      // 임시 비밀번호 리턴
      doLoginResponseDataCertificationDto.setPasswordChangeCd(passwordChangeCd);
      result.setCertification(doLoginResponseDataCertificationDto);
      // 3개월 지났을 경우 비빌번호 변경할수 있도록 로직 추가 END

      log.info("비밀번호 변경일자를 초과했습니다.");
      doLoginResponseDataNotiDto.setChangePassword(true);
    }

    log.info("비밀번호 변경일자를 초과안했다면");
    // 광고성 수신 동의 1년 체크
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();
    String today = null;
    today = formatter.format(cal.getTime());

    doLoginResponseDataMakettingDto.setSmsYn(getLoginDataResultVo.getSmsYn());
    doLoginResponseDataMakettingDto.setMailYn(getLoginDataResultVo.getMailYn());

    doLoginResponseDataMakettingDto.setSmsYnDate(getLoginDataResultVo.getSmsYnDate());
    doLoginResponseDataMakettingDto.setMailYnDate(getLoginDataResultVo.getMailYnDate());

    if (getLoginDataResultVo.getSmsYn().equals("Y") && getLoginDataResultVo.getSmsYnDateOneYearOverYn().equals("Y")) {
      log.info("SMS동의 Y ,1년지나면 Y");
      doLoginResponseDataNotiDto.setMaketing(true);
      doLoginResponseDataMakettingDto.setSmsYnDate(today);
      userBuyerBiz.putMaketingReceiptAgreementSms(urUserId);
    }

    if (getLoginDataResultVo.getMailYn().equals("Y") && getLoginDataResultVo.getMailYnDateOneYearOverYn().equals("Y")) {
      log.info("이메일 동의 Y ,1년지나면 Y");
      doLoginResponseDataNotiDto.setMaketing(true);
      doLoginResponseDataMakettingDto.setMailYnDate(today);
      userBuyerBiz.putMaketingReceiptAgreementMail(urUserId);
    }

    if (doLoginResponseDataNotiDto.isMaketing()) {
      result.setMaketting(doLoginResponseDataMakettingDto);
    }

    int u = userJoinBiz.putUrAccount(urUserId); // 로그인시에 시간업데이트
    if (u > 0) {
      log.info("UR_ACCOUNT에 로그인시간이 업데이트 되었습니다.");
    }
    // 기본배송지가지고 와서 세션에 넣어준다. START
    GetShippingAddressListResultVo getShippingAddressListResultVo = userBuyerBiz.getBasicShippingAddress(databaseEncryptionKey,
                                                                                                         urUserId);
    if (getShippingAddressListResultVo != null) {
      AddSessionShippingRequestDto addSessionShippingRequestDto = new AddSessionShippingRequestDto();
      addSessionShippingRequestDto.setReceiverName(getShippingAddressListResultVo.getReceiverName());
      addSessionShippingRequestDto.setReceiverZipCode(getShippingAddressListResultVo.getReceiverZipCode());
      addSessionShippingRequestDto.setReceiverAddress1(getShippingAddressListResultVo.getReceiverAddress1());
      addSessionShippingRequestDto.setReceiverAddress2(getShippingAddressListResultVo.getReceiverAddress2());
      addSessionShippingRequestDto.setBuildingCode(getShippingAddressListResultVo.getBuildingCode());
      addSessionShippingRequestDto.setReceiverMobile(getShippingAddressListResultVo.getReceiverMobile());
      addSessionShippingRequestDto.setAccessInformationType(getShippingAddressListResultVo.getAccessInformationType());
      addSessionShippingRequestDto.setAccessInformationPassword(getShippingAddressListResultVo.getAccessInformationPassword());
      addSessionShippingRequestDto.setShippingComment(getShippingAddressListResultVo.getShippingComment());
      addSessionShippingRequestDto.setSelectBasicYn("Y");
      addSessionShippingRequestDto.setShippingAddressId(getShippingAddressListResultVo.getShippingAddressId());

      addSessionShipping(addSessionShippingRequestDto);
    }
    // 기본배송지가지고 와서 세션에 넣어준다. END

    result.setNoti(doLoginResponseDataNotiDto);
    result.setUrUserId(urUserId);

    return ApiResult.success(result);
  }

  protected String getAutoLoginKey() throws Exception {
    return UidUtil.randomUUID().toString();
  }

  /**
   * SNS 로그인 아이디 조회
   *
   * @param getSocialLoginDataRequestDto
   * @return GetSocialLoginDataResultVo
   * @throws Exception
   */

  protected GetSocialLoginDataResultVo getSocialLoginData(GetSocialLoginDataRequestDto getSocialLoginDataRequestDto) throws Exception {
    return certificationMapper.getSocialLoginData(getSocialLoginDataRequestDto);
  }

  /**
   * SNS 사용자 정보 추가
   *
   * @param userSocialInformationDto
   * @return
   * @throws Exception
   */

  protected void addUserSocial(UserSocialInformationDto userSocialInformationDto) throws Exception {
    certificationMapper.addUserSocial(userSocialInformationDto);
  }

  /**
   * 로그아웃
   *
   * @param request
   * @return BaseResponseDto
   * @throws Exception
   */

  protected ApiResult<?> logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

    // 회원접속로그 로그아웃 시간 업데이트
    HashMap<String, String> connectionLogoutMap = new HashMap<>();
    connectionLogoutMap.put("urConnectLogId", buyerVo.getUrConnectLogId());
    certificationMapper.putConnectionLogout(connectionLogoutMap);

    // APP 일경우만 자동 로그인 해지
	if (DeviceUtil.isApp()) {
    	putAutoLoginKey(buyerVo.getUrUserId(), "");
    }

    request.getSession().invalidate();

    return ApiResult.success();
  }

  /**
   * 비밀번호 변경
   *
   * @param putPasswordRequestDto
   * @return BaseResponseDto
   * @throws Exception
   */

  protected ApiResult<?> putPassword(PutPasswordRequestDto putPasswordRequestDto) throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (StringUtil.isNotEmpty(buyerVo.getLoginId())) {
      /* 아이디와 비밀번호를 똑같이 입력했을때 오류처리 1225 */
      if (buyerVo.getLoginId().equals(putPasswordRequestDto.getPassword())) {
        return ApiResult.result(UserEnums.Join.ID_PW_SAME_NOTI);
      }
    }

    String password = SHA256Util.getUserPassword(putPasswordRequestDto.getPassword());
    String passwordChangeCd = putPasswordRequestDto.getPasswordChangeCd();
    int samePasswordCount = certificationMapper.getCheckSamePasswordCnt(password, passwordChangeCd);
    log.info("samePasswordCount ==={}", samePasswordCount);
    if (samePasswordCount > 0) {
      return ApiResult.result(UserEnums.Join.SAME_PASSWORD_NOTI);
    }

    int i = certificationMapper.putPassword(password, passwordChangeCd);
    if (i == 0) {
      return ApiResult.fail();
    }
    return ApiResult.success();
  }

  /**
   * 비밀번호 다음에 변경
   *
   * @param
   * @return
   * @throws Exception
   */

  protected ApiResult<?> putNextPassword() throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (StringUtil.isNotEmpty(buyerVo.getUrUserId())) {
      certificationMapper.putNextPassword(buyerVo.getUrUserId());
      return ApiResult.success();
    } else {
      return ApiResult.fail();
    }
  }

  /**
   * 비회원 로그인
   *
   * @param getNonMemberOrderRequestDto
   * @return BaseResponseDto
   * @throws Exception
   */

  protected ApiResult<?> getNonMemberOrder(GetNonMemberOrderRequestDto getNonMemberOrderRequestDto) throws Exception {
    String buyerName = getNonMemberOrderRequestDto.getBuyerName();
    String buyerMobile = getNonMemberOrderRequestDto.getBuyerMobile();

    int i = certificationMapper.getNonMemberOrder(buyerName, buyerMobile, databaseEncryptionKey);
    if (i == 0) {
      return ApiResult.result(UserEnums.Join.NON_MEMBER_LOGIN_FAIL);
    }

    return ApiResult.success();
  }

  /**
   * 비회원 주문조회 (본인인증 체크)
   *
   * @param getNonMemberOrderRequestDto
   * @return BaseResponseDto
   * @throws Exception
   */

  protected ApiResult<?> getNonMemberOrderCI(GetNonMemberOrderRequestDto getNonMemberOrderRequestDto) throws Exception {

    String buyerName = getNonMemberOrderRequestDto.getBuyerName();
    String buyerMobile = getNonMemberOrderRequestDto.getBuyerMobile();
    GetSessionUserCertificationResponseDto getSessionUserCertificationResponseDto = getSessionUserCertification();
    String ciCd = getSessionUserCertificationResponseDto.getCi();
    log.info("getNonMemberOrderRequestDto.getDatabaseEncryptionKey()===>" + databaseEncryptionKey);

    //관리자 비회원 주문 생성시 CI 업기때문에 이름, 핸드폰 번호로 update 추가
    certificationMapper.putNonMemberOrderCI(buyerName, buyerMobile, ciCd);

    int count = certificationMapper.getNonMemberOrderCI(buyerName, buyerMobile, ciCd, databaseEncryptionKey);

    if (count == 0) {
      return ApiResult.result(UserEnums.Join.NON_MEMBER_LOGIN_FAIL);
    }

    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    buyerVo.setNonMemberCiCd(ciCd);
    buyerVo.setNonMemberUserName(buyerName);
    buyerVo.setNonMemberMobile(buyerMobile);
    SessionUtil.setUserVO(buyerVo);

    return ApiResult.success();
  }

  /**
   * 휴면회원 CI 조회
   *
   * @param getIsCheckBuyerMoveInfoDto
   * @return BaseResponseDto
   * @throws Exception
   */

  protected ApiResult<?> isCheckBuyerMoveInfo(GetIsCheckBuyerMoveInfoDto getIsCheckBuyerMoveInfoDto) throws Exception {
    String loginId = getIsCheckBuyerMoveInfoDto.getLoginId();
    String encryptPassword = SHA256Util.getUserPassword(getIsCheckBuyerMoveInfoDto.getPassword());
    String ciCd = getSessionUserCertification().getCi();

    if (StringUtils.isEmpty(ciCd)) {
      log.info("count 0 이면");
      return ApiResult.result(UserEnums.Join.NOT_ANY_CERTI);
    }
    int count = certificationMapper.isCheckBuyerMoveInfo(loginId, encryptPassword, ciCd);

    log.info("count {}", count);

    if (count == 0) {
      log.info("count 0 이면");
      return ApiResult.result(UserEnums.Join.NOT_ANY_CERTI);
    }

    return ApiResult.success();
  }

  protected GetBuyerCertificationByAutoLoginKeyResponseDto getBuyerCertificationByAutoLoginKey(String urUserId,
                                                                                               String autoLoginKey) throws Exception {
    GetBuyerCertificationByAutoLoginKeyResponseVo getBuyerCertificationByAutoLoginKeyResponseVo =
                                                                                                certificationMapper.getBuyerCertificationByAutoLoginKey(urUserId,
                                                                                                                                                        autoLoginKey);

    if (getBuyerCertificationByAutoLoginKeyResponseVo != null) {
      GetBuyerCertificationByAutoLoginKeyResponseDto getBuyerCertificationByAutoLoginKeyResponseDto =
                                                                                                    new GetBuyerCertificationByAutoLoginKeyResponseDto();
      getBuyerCertificationByAutoLoginKeyResponseDto.setEncryptPassword(getBuyerCertificationByAutoLoginKeyResponseVo.getEncryptPassword());
      getBuyerCertificationByAutoLoginKeyResponseDto.setLoginId(getBuyerCertificationByAutoLoginKeyResponseVo.getLoginId());

      return getBuyerCertificationByAutoLoginKeyResponseDto;
    } else {
      return null;
    }
  }

  protected int putAutoLoginKey(String urUserId, String autoLoginKey) throws Exception {
    return certificationMapper.putAutoLoginKey(urUserId, autoLoginKey);
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
   * 임직원 로그인
   *
   * @param request,HttpServletRequest,HttpServletResponse
   * @return GetLoginResponseDto
   * @throws Exception
   */

  protected ApiResult<?> employeesLogin(GetEmployeesLoginRequestDto getEmployeesLoginRequestDto,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {

    GetLoginResponseDataDto getLoginResponseDataDto = new GetLoginResponseDataDto();

    DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
    doLoginRequestDto.setLoginId(getEmployeesLoginRequestDto.getLoginId());
    doLoginRequestDto.setPassword(getEmployeesLoginRequestDto.getPassword());
    doLoginRequestDto.setEncryptPassword(SHA256Util.getUserPassword(getEmployeesLoginRequestDto.getPassword()));
    // doLoginRequestDto.setAutoLogin(getLoginRequestDto.getAutoLogin().equals("Y"));
    // doLoginRequestDto.setSaveLoginId(getLoginRequestDto.getSaveLoginId().equals("Y"));
    doLoginRequestDto.setCaptcha(getEmployeesLoginRequestDto.getCaptcha());

    // 로그인 처리
    ApiResult<?> result = doLogin(doLoginRequestDto, true, request, response);
    DoLoginResponseDto doLoginResponseDto = (DoLoginResponseDto) result.getData();
    if (doLoginResponseDto != null) {
      if (doLoginResponseDto.getCertification() != null) {
        getLoginResponseDataDto.setCertification(doLoginResponseDto.getCertification());
      }
      if (doLoginResponseDto.getClause() != null) {
        getLoginResponseDataDto.setClause(doLoginResponseDto.getClause());
      }
      if (doLoginResponseDto.getMaketting() != null) {
        getLoginResponseDataDto.setMaketting(doLoginResponseDto.getMaketting());
      }
      if (doLoginResponseDto.getNoti() != null) {
        getLoginResponseDataDto.setNoti(doLoginResponseDto.getNoti());
      }
      if (doLoginResponseDto.getStop() != null) {
        getLoginResponseDataDto.setStop(doLoginResponseDto.getStop());
      }
    }
    return ApiResult.result(getLoginResponseDataDto, result.getMessageEnum());
  }

  /**
   * 임직원 인증
   *
   * @param getEmployeesCertificationRequestDto
   * @return BaseResponseDto
   * @throws Exception
   */

  protected ApiResult<?> employeesCertification(GetEmployeesCertificationRequestDto getEmployeesCertificationRequestDto) throws Exception {
    BaseResponseDto result = new BaseResponseDto();
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    String urUserId = buyerVo.getUrUserId();
    String email = getEmployeesCertificationRequestDto.getEmail();
    String inputUrErpEmployeeCode = getEmployeesCertificationRequestDto.getUrErpEmployeeCode();

    // 이미 임직원 인증을 완료한 사람은 이미 임직원 인증을 완료한 계정입니다
    int existCnt = certificationMapper.getIsUrBuyerEmployeesExistCnt(urUserId, inputUrErpEmployeeCode, databaseEncryptionKey);
    if (existCnt > 0) {
      // 이미 임직원등록을 마침
      return ApiResult.result(UserEnums.Join.ALREADY_EMPLOYEE_CERTIFI_DONE);
    }

    // 입력한 이메일과 사번이 있다면 6자리 난수코드를 생성
    int cnt = getEmployeesCertification(email, inputUrErpEmployeeCode);
    if (cnt > 0) {
      String tempCertiNo = getRandom6();
      log.info("tempCertiNo {}", tempCertiNo);
      /* 세션에 임시번호를 저장한다. */
      AddSessionEmployeeCertificationRequestDto addSessionEmployeeCertificationRequestDto =
                                                                                          new AddSessionEmployeeCertificationRequestDto();
      addSessionEmployeeCertificationRequestDto.setTempCertiNo(tempCertiNo);
      addSessionEmployeeCertificationRequestDto.setTempUrErpEmployeeCode(inputUrErpEmployeeCode);
      addSessionEmployeeCertificationRequestDto.setUrUserId(urUserId);

      addSessionEmployeeCertification(addSessionEmployeeCertificationRequestDto);// 세션저장

      getEmployeesCertificationRequestDto.setUrUserId(urUserId);
      getEmployeesCertificationRequestDto.setTempCertiNo(tempCertiNo);

      return ApiResult.success(tempCertiNo);
    } else {
      return ApiResult.result(UserEnums.Join.NO_FIND_EMPLOYEE);
    }
  }

	protected int getEmployeesCertification(String email, String urErpEmployeeCode) throws Exception {
		return certificationMapper.getEmployeesCertification(email, urErpEmployeeCode, databaseEncryptionKey);
	}

	/**
	 * @Desc 임직원 회원 인증 후 정보 조회
	 * @param urErpEmployeeCd
	 * @return EmployeeCertificationResultVo
	 */
	protected EmployeeCertificationResultVo getEmployeeCertificationInfo(String urErpEmployeeCd) {
		return certificationMapper.getEmployeeCertificationInfo(urErpEmployeeCd);
	}


/**
   * 임직원 인증 확인
   *
   * @param certificationCode
   * @return BaseResponseDto
   * @throws Exception
   */

  protected ApiResult<?> employeesCertificationVeriyfy(String certificationCode) throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    String urUserId = "";
    String urErpEmployeeCode = "";
    // 사번이 없을때 예외처리
    if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
      log.info("====0001 로그인필요===");
      return ApiResult.result(UserEnums.Join.NEED_LOGIN);
    } else {
      urUserId = buyerVo.getUrUserId();
      log.info("urUserId {}", urUserId);
      urErpEmployeeCode = buyerVo.getTempUrErpEmployeeCode();
      log.info("urErpEmployeeCode {}", urErpEmployeeCode);
    }

    String getTempCertiNo = buyerVo.getTempCertiNo();
    log.info("저장된 ===>getTempCertiNo {}", getTempCertiNo);
    AddSessionEmployeeCertificationRequestDto addSessionEmployeeCertificationRequestDto =
                                                                                        new AddSessionEmployeeCertificationRequestDto();

    if (certificationCode.equals(getTempCertiNo))// 입력값과 인증코드값이 일치하다면
    {
      addSessionEmployeeCertificationRequestDto.setFailCnt("0");
      addSessionEmployeeCertificationRequestDto.setUrUserId(urUserId);
      addSessionEmployeeCertificationRequestDto.setUrErpEmployeeCode(urErpEmployeeCode);
      addSessionEmployeeCertificationRequestDto.setTempCertiNo("");
      addSessionEmployeeCertificationRequestDto.setTempUrErpEmployeeCode("");
      addSessionEmployeeCertification(addSessionEmployeeCertificationRequestDto);// 세션저장

      // 업데이트 로직 추가
      int u = certificationMapper.putUrBuyerForEmployees(urUserId, urErpEmployeeCode);

      	Long longUrUserId = Long.valueOf(urUserId);

	    //배송지 조회 API 호출
	  	SearchCustomerDeliveryListResponseDto searchCustomerDeliveryListResponseDto = asisUserApiUtil.searchCustomerDeliveryList(urErpEmployeeCode);

	  	//배송지 리스트 중에서 기본배송지 N인 배송지만 저장
	  	if(searchCustomerDeliveryListResponseDto.getData() != null) {
	  		List<SearchCustomerDeliveryDto> searchCustomerDeliveryList = searchCustomerDeliveryListResponseDto.getData();
	  		for(SearchCustomerDeliveryDto dto : searchCustomerDeliveryList) {
	  			CommonSaveShippingAddressRequestDto asisUserShippingAddrRequestDto = new CommonSaveShippingAddressRequestDto();
  				asisUserShippingAddrRequestDto.setUrUserId(longUrUserId);
  				asisUserShippingAddrRequestDto.setDefaultYn("N");
  				asisUserShippingAddrRequestDto.setShippingName(dto.getShippingNm());
  				asisUserShippingAddrRequestDto.setReceiverName(dto.getReceiverNm());
  				asisUserShippingAddrRequestDto.setReceiverMobile(dto.getReceiverMo());
  				asisUserShippingAddrRequestDto.setReceiverTelephone(dto.getReceiverTel());
  				asisUserShippingAddrRequestDto.setReceiverZipCode(dto.getReceiverZipCd());
  				asisUserShippingAddrRequestDto.setReceiverAddress1(dto.getReceiverAddr1());
  				asisUserShippingAddrRequestDto.setReceiverAddress2(dto.getReceiverAddr2());
  				asisUserShippingAddrRequestDto.setBuildingCode(dto.getBuildingCd());
  				asisUserShippingAddrRequestDto.setShippingComment("");
  				userBuyerBiz.addShippingAddress(asisUserShippingAddrRequestDto);
	  		}
	  	}

      // HGRM-8017 적립금 자동이관 제외 처리 (최용호)
	  	//as-is풀무원 적립금 조회 API 호출
//	  	SearchCustomerRsrvTotalResponseDto searchCustomerRsrvTotalResponseDto = asisUserApiUtil.searchCustomerRsrvTotal(urErpEmployeeCode, "N", "");
//	  	int totalRemainPrice = searchCustomerRsrvTotalResponseDto.getPulmuoneShopPoint();
//
//	  	//to-be풀무원 적립금 적립
//	  	Long amount = Long.valueOf(totalRemainPrice);
//	  	ApiResult<?> pointResult = pointBiz.depositPreviousPulmuoneMemberPoints(longUrUserId, amount, urErpEmployeeCode);
//        if(BaseEnums.Default.SUCCESS.equals(pointResult.getMessageEnum()) || PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT.equals(pointResult.getMessageEnum())){
//	  		//as-is풀무원 적립금 소멸 API 호출
//	  		asisUserApiUtil.minusCustomerRsrv("N", urErpEmployeeCode, totalRemainPrice);
//	  	}

      log.info(" 업데이트 건수 u {}", u);
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
			addSessionEmployeeCertificationRequestDto.setTempUrErpEmployeeCode(urErpEmployeeCode);
			addSessionEmployeeCertificationRequestDto.setTempCertiNo(getTempCertiNo);
			addSessionEmployeeCertificationRequestDto.setUrUserId(urUserId);
			messageCommEnum = UserEnums.Join.FAIL_EMPLOYEE_CERTIFI_CODE;
      }
      addSessionEmployeeCertification(addSessionEmployeeCertificationRequestDto);// 세션저장
      return ApiResult.result(messageCommEnum);
    }
    return ApiResult.success();
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

  protected String getCert(KmcCertRequestDataDto reqDto) throws Exception {

    // 01. 한국모바일인증(주) 암호화 모듈 선언
    IcertSecuManager seed = new IcertSecuManager();

    // 02. 1차 암호화 (tr_cert 데이터변수 조합 후 암호화)
    String tr_cert = reqDto.getCpId() + "/" + reqDto.getUrlCode() + "/" + reqDto.getCertNum() + "/" + reqDto.getDate() + "/"
        + reqDto.getCertMet() + "/" + reqDto.getBirthDay() + "/" + reqDto.getGender() + "/" + reqDto.getName() + "/"
        + reqDto.getPhoneNo() + "/" + reqDto.getPhoneCorp() + "/" + reqDto.getNation() + "/" + reqDto.getPlusInfo() + "/"
        + reqDto.getExtedVar();
    String enc_tr_cert = seed.getEnc(tr_cert, "");

    // 03. 1차 암호화 데이터에 대한 위변조 검증값 생성 (HMAC)
    String hmacMsg = seed.getMsg(enc_tr_cert);

    // 04. 2차 암호화 (1차 암호화 데이터, HMAC 데이터, extendVar 조합 후 암호화)
    tr_cert = seed.getEnc(enc_tr_cert + "/" + hmacMsg + "/" + reqDto.getExtedVar(), "");

    return tr_cert;
  }

  protected KmcDecodeResponseDto getDecode(String rec_cert, String k_certNum) throws Exception {

    KmcDecodeResponseDto responseDto = new KmcDecodeResponseDto();
    // 변수선언
    // --------------------------------------------------------------------------------------------------------
    String encPara = "";
    String encMsg1 = "";
    String encMsg2 = "";
    String msgChk = "";
    // -----------------------------------------------------------------------------------------------------------------

    // 01. 암호화 모듈 (jar) Loading
    IcertSecuManager seed = new IcertSecuManager();

    // 02. 1차 복호화
    // 수신된 certNum를 이용하여 복호화
    rec_cert = seed.getDec(rec_cert, k_certNum);

    // 03. 1차 파싱
    int inf1 = rec_cert.indexOf("/", 0);
    int inf2 = rec_cert.indexOf("/", inf1 + 1);

    encPara = rec_cert.substring(0, inf1); // 암호화된 통합 파라미터
    encMsg1 = rec_cert.substring(inf1 + 1, inf2); // 암호화된 통합 파라미터의 Hash값

    // 04. 위변조 검증
    encMsg2 = seed.getMsg(encPara);

    if (encMsg2.equals(encMsg1)) {
      msgChk = "Y";
    }

    if (msgChk.equals("N")) {
      responseDto.setResult("X");
      responseDto.setResultMessage("비정상적인 접근입니다.");
      return responseDto;
    }
    // 05. 2차 복호화
    rec_cert = seed.getDec(encPara, k_certNum);

    // 06. 2차 파싱
    int info1 = rec_cert.indexOf("/", 0);
    int info2 = rec_cert.indexOf("/", info1 + 1);
    int info3 = rec_cert.indexOf("/", info2 + 1);
    int info4 = rec_cert.indexOf("/", info3 + 1);
    int info5 = rec_cert.indexOf("/", info4 + 1);
    int info6 = rec_cert.indexOf("/", info5 + 1);
    int info7 = rec_cert.indexOf("/", info6 + 1);
    int info8 = rec_cert.indexOf("/", info7 + 1);
    int info9 = rec_cert.indexOf("/", info8 + 1);
    int info10 = rec_cert.indexOf("/", info9 + 1);
    int info11 = rec_cert.indexOf("/", info10 + 1);
    int info12 = rec_cert.indexOf("/", info11 + 1);
    int info13 = rec_cert.indexOf("/", info12 + 1);
    int info14 = rec_cert.indexOf("/", info13 + 1);
    int info15 = rec_cert.indexOf("/", info14 + 1);
    int info16 = rec_cert.indexOf("/", info15 + 1);
    int info17 = rec_cert.indexOf("/", info16 + 1);
    int info18 = rec_cert.indexOf("/", info17 + 1);

    String certNum = rec_cert.substring(0, info1);
    String date = rec_cert.substring(info1 + 1, info2);
    String CI = rec_cert.substring(info2 + 1, info3);
    String phoneNo = rec_cert.substring(info3 + 1, info4);
    String phoneCorp = rec_cert.substring(info4 + 1, info5);
    String birthDay = rec_cert.substring(info5 + 1, info6);
    String gender = rec_cert.substring(info6 + 1, info7);
    String nation = rec_cert.substring(info7 + 1, info8);
    String name = rec_cert.substring(info8 + 1, info9);
    String result = rec_cert.substring(info9 + 1, info10);
    String certMet = rec_cert.substring(info10 + 1, info11);
    String ip = rec_cert.substring(info11 + 1, info12);
    String M_name = rec_cert.substring(info12 + 1, info13);
    String M_birthDay = rec_cert.substring(info13 + 1, info14);
    String M_Gender = rec_cert.substring(info14 + 1, info15);
    String M_nation = rec_cert.substring(info15 + 1, info16);
    String plusInfo = rec_cert.substring(info16 + 1, info17);
    String DI = rec_cert.substring(info17 + 1, info18);

    // 07. CI, DI 복호화
    CI = seed.getDec(CI, k_certNum);
    DI = seed.getDec(DI, k_certNum);

    // 파라미터 유효성 검증 --------------------------------------------
    boolean b = true;
    String regex = "";

    if (certNum.length() == 0 || certNum.length() > 40) {
      responseDto.setResult("X");
      responseDto.setResultMessage("요청번호 비정상");
      return responseDto;
    }

    regex = "[0-9]*";
    if (date.length() != 14 || !paramChk(regex, date)) {
      responseDto.setResult("X");
      responseDto.setResultMessage("요청일시 비정상");
      return responseDto;
    }

    regex = "[A-Z]*";
    if (certMet.length() != 1 || !paramChk(regex, certMet)) {
      responseDto.setResult("X");
      responseDto.setResultMessage("본인인증방법 비정상");
      return responseDto;
    }

    regex = "[0-9]*";
    if ((phoneNo.length() != 10 && phoneNo.length() != 11) || !paramChk(regex, phoneNo)) {
      responseDto.setResult("X");
      responseDto.setResultMessage("휴대폰번호 비정상");
      return responseDto;
    }

    regex = "[A-Z]*";
    if (phoneCorp.length() != 3 || !paramChk(regex, phoneCorp)) {
      responseDto.setResult("X");
      responseDto.setResultMessage("이동통신사 비정상");
      return responseDto;
    }

    regex = "[0-9]*";
    if (birthDay.length() != 8 || !paramChk(regex, birthDay)) {
      responseDto.setResult("X");
      responseDto.setResultMessage("생년월일 비정상");
      return responseDto;
    }

    regex = "[0-9]*";
    if (gender.length() != 1 || !paramChk(regex, gender)) {
      responseDto.setResult("X");
      responseDto.setResultMessage("성별 비정상");
      return responseDto;
    }

    regex = "[0-9]*";
    if (nation.length() != 1 || !paramChk(regex, nation)) {
      responseDto.setResult("X");
      responseDto.setResultMessage("내/외국인 비정상");
      return responseDto;
    }

    regex = "[\\sA-Za-z가-�R.,-]*";
    if (name.length() > 60 || !paramChk(regex, name)) {
      responseDto.setResult("X");
      responseDto.setResultMessage("성명 비정상");
      return responseDto;
    }

    regex = "[A-Z]*";
    if (result.length() != 1 || !paramChk(regex, result)) {
      responseDto.setResult("X");
      responseDto.setResultMessage("결과값 비정상");
      return responseDto;
    }

    regex = "[\\sA-Za-z가-?.,-]*";
    if (M_name.length() != 0) {
      if (M_name.length() > 60 || !paramChk(regex, M_name)) {
        responseDto.setResult("X");
        responseDto.setResultMessage("미성년자 성명 비정상");
        return responseDto;
      }
    }

    regex = "[0-9]*";
    if (M_birthDay.length() != 0) {
      if (M_birthDay.length() != 8 || !paramChk(regex, M_birthDay)) {
        responseDto.setResult("X");
        responseDto.setResultMessage("미성년자 생년월일 비정상");
        return responseDto;
      }
    }

    regex = "[0-9]*";
    if (M_Gender.length() != 0) {
      if (M_Gender.length() != 1 || !paramChk(regex, M_Gender)) {
        responseDto.setResult("X");
        responseDto.setResultMessage("미성년자 성별 비정상");
        return responseDto;
      }
    }

    regex = "[0-9]*";
    if (M_nation.length() != 0) {
      if (M_nation.length() != 1 || !paramChk(regex, M_nation)) {
        responseDto.setResult("X");
        responseDto.setResultMessage("미성년자 내/외국인 비정상");
        return responseDto;
      }
    }
    // End 파라미터 유효성 검증 --------------------------------------------

    // Start - 수신내역 유효성 검증(사설망의 사설 IP로 인해 미사용, 공용망의 경우 확인 후 사용)
    // *********************/
    // 1. date 값 검증
    TimeZone time = TimeZone.getTimeZone("Asia/Seoul");
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN); // 현재
    // 서버
    // 시각
    // 구하기
    formatter.setTimeZone(time);
    String strCurrentTime = formatter.format(new Date());

    Date toDate = formatter.parse(strCurrentTime);
    Date fromDate = formatter.parse(date);
    long timediff = toDate.getTime() - fromDate.getTime();

    if (timediff < -30 * 60 * 1000 || 30 * 60 * 100 < timediff) {
      responseDto.setResult("X");
      responseDto.setResultMessage("비정상적인 접근입니다. (요청시간경과)");
      return responseDto;
    }

    // 2. ip 값 검증
    // String client_ip = request.getHeader("HTTP_X_FORWARDED_FOR"); // 사용자IP
    // 구하기
    // if (client_ip != null) {
    // if (client_ip.indexOf(",") != -1)
    // client_ip = client_ip.substring(0, client_ip.indexOf(","));
    // }
    // if (client_ip == null || client_ip.length() == 0) {
    // client_ip = request.getRemoteAddr();
    // }
    //
    // if (!client_ip.equals(ip)) {
    // responseDto.setResult("X");
    // responseDto.setResultMessage("비정상적인 접근입니다. (IP불일치)");
    // return responseDto;
    // }

    responseDto.setCertNum(certNum);
    responseDto.setDate(date);
    responseDto.setCI(CI);
    responseDto.setPhoneNo(phoneNo);
    responseDto.setPhoneCorp(phoneCorp);
    responseDto.setBirthDay(birthDay);
    responseDto.setGender(gender);
    responseDto.setNation(nation);
    responseDto.setName(name);
    responseDto.setResult(result);
    responseDto.setCertMet(certMet);
    responseDto.setM_name(M_name);
    responseDto.setM_birthDay(M_birthDay);
    responseDto.setM_Gender(M_Gender);
    responseDto.setM_nation(M_nation);
    responseDto.setPlusInfo(plusInfo);
    responseDto.setDI(DI);

    return responseDto;
  }

  protected Boolean paramChk(String patn, String param) {

    return Pattern.matches(patn, param);
  }

  protected int getisCheckLogin(DoLoginRequestDto doLoginRequestDto) throws Exception {

    return certificationMapper.getisCheckLogin(doLoginRequestDto);
  }

  protected GetLoginDataResultVo getLoginData(DoLoginRequestDto doLoginRequestDto) throws Exception {

    return certificationMapper.getLoginData(doLoginRequestDto);
  }

  /**
   * 비밀번호 확인
   *
   * @param
   * @return ResponseDto<GetLatestClauseResponseDto>
   * @throws Exception
   */
  protected ApiResult<?> isPasswordCorrect(String encryptPassword) throws Exception {

    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

    // 사번이 없을때 예외처리
    if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
      log.info("====0001 로그인필요===");
      return ApiResult.result(UserEnums.Join.NEED_LOGIN);
    }

    String urUserId = buyerVo.getUrUserId();
    int i = certificationMapper.isPasswordCorrect(urUserId, encryptPassword);

    // 비밀번호가 맞을때만 0 없거나 틀리면 1
    if (i == 0) {
      return ApiResult.success();
    } else {
      log.info("===CHECK_PASSWORD 비밀번호 확인을 해주세요 ===");
      return ApiResult.result(UserEnums.Join.CHECK_PASSWORD);
    }

  }

  /**
   * SNS 로그인 계정 연결끊기
   *
   * @param
   * @return
   * @throws Exception
   */
  protected void unlinkAccount(UnlinkAccountRequestDto unlinkAccountRequestDto) throws Exception {

    certificationMapper.unlinkAccount(unlinkAccountRequestDto);

  }

  /**
   * 회퉌 SNS 로그인 계쩡 모두 끊기
   *
   * @param urUserId
   */
  protected void unlinkAllAccount(Long urUserId) throws Exception {
	  certificationMapper.unlinkAllAccount(urUserId);
  }

  /**
   * @Desc 로그인 인증정보 등록
   * @param certificationVo
   * @throws Exception
   * @return int
   */
  protected int addCertification(CertificationVo certificationVo) throws Exception {
    return certificationMapper.addCertification(certificationVo);
  }

  /**
   * 임직원 회원 가입여부 체크
   */
  protected boolean isUrBuyerEmployeesExist(String employeeCode) throws Exception {
    int existCnt = certificationMapper.getIsUrBuyerEmployeesExistCnt("", employeeCode, "");
    return (existCnt > 0);
  }

}
