package kr.co.pulmuone.v1.user.join.service;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;

import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.asis.dto.UserInfoCheckResponseDto;
import kr.co.pulmuone.v1.comm.util.google.Recaptcha;
import kr.co.pulmuone.v1.send.template.dto.AddEmailIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.user.buyer.dto.AddBuyerRequestDto;
import kr.co.pulmuone.v1.user.join.dto.GetIsCheckLoginIdRequestDto;
import kr.co.pulmuone.v1.user.join.dto.GetIsCheckMailRequestDto;
import kr.co.pulmuone.v1.user.join.dto.GetIsCheckRecommendLoginIdRequestDto;
import kr.co.pulmuone.v1.user.join.dto.SaveBuyerRequestDto;
import kr.co.pulmuone.v1.user.join.dto.vo.AccountVo;
import kr.co.pulmuone.v1.user.join.dto.vo.JoinResultVo;
import kr.co.pulmuone.v1.user.join.dto.vo.UserVo;

import java.util.HashMap;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200617   	 김경민            최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class UserJoinBizImpl implements UserJoinBiz {

    @Autowired
    private UserJoinService userJoinService;

    @Autowired
	private ComnBizImpl comnBizImpl;

    @Autowired
	private SendTemplateBiz sendTemplateBiz;

    @Autowired
    private PromotionCouponBiz promotionCouponBiz;


    @Autowired
    Recaptcha googleRecaptcha;

    /**
     * 아이디 중복 확인
     *
     * @param GetIsCheckLoginIdRequestDto
     * @throws Exception
     */
    @Override
    public ApiResult<?> getIsCheckLoginId(GetIsCheckLoginIdRequestDto getIsCheckLoginIdRequestDto) throws Exception {

    	//as-is회원일 경우 로그인아이디 전달
    	BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

    	if(buyerVo.getAsisLoginId() != null) {
    		getIsCheckLoginIdRequestDto.setAsisLoginId(buyerVo.getAsisLoginId());
    	}

    	if(userJoinService.isNotAvailableLoginId(getIsCheckLoginIdRequestDto.getLoginId())) {
    		return ApiResult.result(UserEnums.Join.ID_NOT_AVAILABLE);
    	} else {
            if(userJoinService.getIsCheckLoginId(getIsCheckLoginIdRequestDto)) {
                return ApiResult.result(UserEnums.Join.ID_DUPLICATE);
            } else {
                return ApiResult.success();
            }
    	}
    }

    @Override
    public ApiResult<?> getIsCheckMail(GetIsCheckMailRequestDto getIsCheckMailResponseDto) throws Exception {

        boolean result = userJoinService.getIsCheckMail(getIsCheckMailResponseDto);

        if(result) {
            return ApiResult.result(UserEnums.Join.EMAIL_DUPLICATE);
        } else {
            return ApiResult.success();
        }
    }

    @Override
    public ApiResult<?> getIsCheckRecommendLoginId(GetIsCheckRecommendLoginIdRequestDto getIsCheckRecommendLoginIdRequestDto) throws Exception {

        return userJoinService.getIsCheckRecommendLoginId(getIsCheckRecommendLoginIdRequestDto);
    }

    @Transactional
    @Override
    public ApiResult<?> addBuyer(AddBuyerRequestDto addBuyerRequestDto) throws Exception {

    	ApiResult<?> result =  userJoinService.addBuyer(addBuyerRequestDto);

		if (BaseEnums.Default.SUCCESS.getCode().equals(result.getCode())) {
        	// 회원가입완료 정보조회 - urUserId 이용
            JoinResultVo infoJoinResultVo = userJoinService.getJoinCompletedInfo(addBuyerRequestDto.getUrUserId());
            getSignUpCompleted(infoJoinResultVo);
    	}

    	return result;
    }

	/**
	 * @Desc 회원가입 완료 시 자동메일 발송
	 * @param infoJoinResultVo
	 * @return void
	 */
    @Override
	public void getSignUpCompleted(JoinResultVo infoJoinResultVo) {

		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.SIGN_UP_COMPLETED.getCode());
    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

    	//이메일 발송
    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/getSignUpCompletedEmailTmplt?urUserId="+infoJoinResultVo.getUrUserId();
        	String title = getEmailSendResultVo.getMailTitle();
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            String senderName = sendTemplateBiz.getPsValue("SEND_EMAIL_SENDER");
            String senderMail = sendTemplateBiz.getPsValue("SEND_EMAIL_ADDRESS");

            AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
                    .senderName(senderName) // SEND_EMAIL_SENDER
                    .senderMail(senderMail) // SEND_EMAIL_ADDRESS
            		.reserveYn(reserveYn)
                    .content(content)
                    .title(title)
                    .urUserId(infoJoinResultVo.getUrUserId())
                    .mail(infoJoinResultVo.getMail())
                    .build();

            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
    	}

    	//SMS 발송
		if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
			String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, infoJoinResultVo);
			String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
			String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
			AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
	                .content(content)
	                .urUserId(infoJoinResultVo.getUrUserId())
	                .mobile(infoJoinResultVo.getMobile()) //infoJoinResultVo.getMobile()
	                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
	                .reserveYn(reserveYn)
	                .build();

			sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

		}

	}

    @Override
    public boolean isCheckUnderAge14(String birthday) throws Exception {

        return userJoinService.isCheckUnderAge14(birthday);
    }

    @Override
    public int addUrClauseAgreeLog(SaveBuyerRequestDto saveBuyerRequestDto) throws Exception {

        return userJoinService.addUrClauseAgreeLog(saveBuyerRequestDto);
    }

    @Override
    public int putUrAccount(String urUserId) throws Exception {

        return userJoinService.putUrAccount(urUserId);
    }

    /**
     * @Desc 회원기본정보 조회
     * @param loginId
     * @return UserVo
     */
    @Override
    public UserVo getUserInfo(String loginId) {
        return userJoinService.getUserInfo(loginId);
    }

    /**
     * @Desc 회원기본정보 등록
     * @param userVo
     * @return int
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int addUser(UserVo userVo) throws Exception{
        return userJoinService.addUser(userVo);
    }

    /**
     * @Desc 회원정보활동정보 등록
     * @param accountVo
     * @return int
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int addAccount(AccountVo accountVo) throws Exception{
        return userJoinService.addAccount(accountVo);


    }

    /**
     * @Desc 회원기본정보 수정
     * @param userVo
     * @return int
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int putUser(UserVo userVo) throws Exception{
        return userJoinService.putUser(userVo);
    }

    /**
     * 기존 회원 여부 체크
     *
     * @param loginId
     * @param password
     * @param captcha
     * @throws Exception
     */
    @Override
    public ApiResult<?> asisUserCheck(String loginId, String password, String captcha, String siteno) throws Exception {
    	BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

    	//캡차 정보 체크 - API 통신 처리
    	if(StringUtil.isNotEmpty(captcha) && !googleRecaptcha.siteVerify(captcha)) {
            // 1214 캡차 인증 실패 리턴
    		return ApiResult.result(UserEnums.Join.RECAPTCHA_FAIL);
    	}

    	//as-is회원 체크
    	UserInfoCheckResponseDto asisUserCheck = userJoinService.asisUserCheck(loginId, password);

    	if(!asisUserCheck.getResultCode().equals(UserEnums.AsisUserInfoCheckResult.SUCCESS.getCode())) {

    		int sessionTempFailCnt = 0;
    		if(buyerVo.getAsisUserFailCnt() != 0) {
    			sessionTempFailCnt = buyerVo.getAsisUserFailCnt();
    		}
    		int intFailCnt = sessionTempFailCnt;
    		intFailCnt++;

    		// 1403 5회 연속 실패
    		if(intFailCnt >= 5) {
    			return ApiResult.result(UserEnums.Join.OVER5_FAIL_CERTIFI_CODE);
    		}

    		buyerVo.setAsisUserFailCnt(intFailCnt);
    		SessionUtil.setUserVO(buyerVo);

    		// 1208 로그인 실패
    		return ApiResult.result(UserEnums.Join.LOGIN_FAIL);
    	}

    	//로그인 실패 횟수 초기화 & 회원 loginId와 기존회원고객번호 세션 저장
    	String customerNumber = asisUserCheck.getCustomerNumber();
    	buyerVo.setAsisCustomerNumber(customerNumber);
    	buyerVo.setAsisLoginId(loginId);
    	buyerVo.setAsisUserFailCnt(0);
    	SessionUtil.setUserVO(buyerVo);

    	// 유입경로가 있을경우 회원인증이벤트로 본다. (최용호 2021-04-01)
        if(StringUtil.isNotEmpty(siteno)) {
            HashMap<String, String> map = new HashMap<>();
            // 이미 지급된 계정인지 확인
            boolean isDup = promotionCouponBiz.isPmCouponJoinDup(customerNumber);
            if(isDup) {
                map.put("result_siteno", "");
                map.put("result_code", "92");
                map.put("result_msg", "이미 쿠폰이 지급된 계정입니다.");
            } else {
                // 풀샵 또는 올가에서 넝어왔는지 체크
                if (Constants.SITE_NO_PULMUONE.equals(siteno) || Constants.SITE_NO_ORGA.equals(siteno)) {
                    String resultSiteNo = siteno;
                    // 통합몰계정에 유입사이트가 있는지 체크
                    if (asisUserCheck.getResultMessage().indexOf(siteno) < 0) {
                        // 유입된 사이트와 인증계정의 사이트가 맞지 않을경우..
                        if (asisUserCheck.getResultMessage().indexOf(Constants.SITE_NO_PULMUONE) >= 0) {
                            // 풀샵이 있으면 풀삽으로..
                            map.put("result_siteno", Constants.SITE_NO_PULMUONE);
                            map.put("result_code", "0");
                            map.put("result_msg", "정상");
                        } else if (asisUserCheck.getResultMessage().indexOf(Constants.SITE_NO_ORGA) >= 0) {
                            // 올가가 있으면 올가로..
                            map.put("result_siteno", Constants.SITE_NO_ORGA);
                            map.put("result_code", "0");
                            map.put("result_msg", "정상");
                        } else {
                            // 없으면 오류..
                            map.put("result_siteno", "");
                            map.put("result_code", "91");
                            map.put("result_msg", "쿠폰지급대상이 아닙니다.");
                        }
                    } else {
                        map.put("result_siteno", resultSiteNo);
                        map.put("result_code", "0");
                        map.put("result_msg", "정상");
                    }
                } else {
                    map.put("result_siteno", "");
                    map.put("result_code", "99");
                    map.put("result_msg", "올바르지 않은 접근입니다.");
                }
            }
            return ApiResult.success(map);
        }

        return ApiResult.success();
    }

	@Override
	public JoinResultVo getJoinCompletedInfo(String urUserId) {
		//회원가입완료시 정보조회
		return userJoinService.getJoinCompletedInfo(urUserId);
	}
}
