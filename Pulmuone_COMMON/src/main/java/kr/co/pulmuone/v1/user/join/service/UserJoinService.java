package kr.co.pulmuone.v1.user.join.service;

import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.CouponEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.user.join.UserJoinMapper;
import kr.co.pulmuone.v1.comm.util.SHA256Util;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.comm.util.asis.AsisUserApiUtil;
import kr.co.pulmuone.v1.comm.util.asis.dto.SearchCustomerDeliveryDto;
import kr.co.pulmuone.v1.comm.util.asis.dto.SearchCustomerDeliveryListResponseDto;
import kr.co.pulmuone.v1.comm.util.asis.dto.UserInfoCheckResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponJoinCertEventRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponInfoByUserJoinVo;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.system.log.service.SystemLogBiz;
import kr.co.pulmuone.v1.user.buyer.dto.AddBuyerRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.CommonDuplicateMailRequestDto;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.user.certification.dto.GetClauseArrayRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionAsisCustomerDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionUserCertificationResponseDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.group.service.UserGroupBiz;
import kr.co.pulmuone.v1.user.join.dto.*;
import kr.co.pulmuone.v1.user.join.dto.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200617   	 김경민            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class UserJoinService {

    private final UserJoinMapper userJoinMapper;

    private final UserCertificationBiz userCertificationBiz;

    private final UserBuyerBiz userBuyerBiz;

    private final PromotionCouponBiz promotionCouponBiz;

    @Autowired
    private PointBiz pointBiz;

    @Autowired
    AsisUserApiUtil	asisUserApiUtil;

    @Value("${resource.server.url.bos}")
	private String serverUrlBos;

    @Autowired
	private SendTemplateBiz sendTemplateBiz;

	@Autowired
	private ComnBizImpl comnBizImpl;

	@Autowired
    private UserGroupBiz userGroupBiz;

	@Autowired
    private SystemLogBiz systemLogBiz;

    /**
     * 14세미만 체크
     *
     * @param personalNo
     * @return boolean
     * @throws Exception
     */
    protected boolean isCheckUnderAge14(String personalNo) throws Exception {

        log.info("personalNo: " + personalNo);
        if (personalNo == null || personalNo.equals("") || personalNo.length() != 8) {
            log.info("생년월일 오류입니다. " + personalNo);
            return false;
        }

        String curDate = null; // 현재 날짜

        int interAge = 0; // 만 나이

        // 현재 날짜 구하기

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Calendar cal = Calendar.getInstance();

        curDate = sdf.format(cal.getTime());

        // 비교를 위한 형 변환

        // 현재월일, 생월일 형변환

        int nCurDay = Integer.parseInt(curDate.substring(4, 8));

        int nBirthDay = Integer.parseInt(personalNo.substring(4, 8));

        // 현재년도, 생년 형변환

        int nCurYear = Integer.parseInt(curDate.substring(0, 4));

        int nBirthYear = Integer.parseInt(personalNo.substring(0, 4));

        // 만 나이 계산

        // 현재월일이 유저의 월일보다 크거나 같으면 현재년도-생년년도로 만나이를 구하고 아니면 그 값에 -1을 한다

        interAge = (nCurDay >= nBirthDay) ? nCurYear - nBirthYear : nCurYear - nBirthYear - 1;

        log.info("현재날짜: " + curDate);

        log.info("만나이: " + interAge);

        if (interAge >= 14) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 아이디 중복 확인
     *
     * @param getIsCheckLoginIdRequestDto
     * @return GetIsCheckLoginIdResponseDto
     * @throws Exception
     */
    protected boolean getIsCheckLoginId(GetIsCheckLoginIdRequestDto getIsCheckLoginIdRequestDto) throws Exception {
    	int cnt = userJoinMapper.getIsCheckLoginId(getIsCheckLoginIdRequestDto);
    	if(cnt > 0) {
    		return true;
    	}

        // 풀무원샵,올가 아이디 체크 API 호출 케이스
    	// to-be회원
    	// as-is회원의 기존아이디와 회원가입시 입력한 아이디가 다르면 풀무원샵,올가 아이디 체크 API 호출
        if(getIsCheckLoginIdRequestDto.getAsisLoginId() == null ||
        		(getIsCheckLoginIdRequestDto.getAsisLoginId() != null && !getIsCheckLoginIdRequestDto.getAsisLoginId().equals(getIsCheckLoginIdRequestDto.getLoginId()))) {

        	//아이디 존재여부 확인 API 호출
        	boolean asisUserSearchIdApiReuslt = asisUserApiUtil.userSerachId(getIsCheckLoginIdRequestDto.getLoginId());
        	if(asisUserSearchIdApiReuslt) {
        		return true;
        	}
        }

        return false;
    }

    /**
     * 이메일 중복 확인
     *
     * @param getIsCheckMailRequestDto
     * @return GetIsCheckMailResponseDto
     * @throws Exception
     */
    protected boolean getIsCheckMail(GetIsCheckMailRequestDto getIsCheckMailRequestDto) throws Exception {
        CommonDuplicateMailRequestDto commonDuplicateMailRequestDto = new CommonDuplicateMailRequestDto();
        commonDuplicateMailRequestDto.setMail(getIsCheckMailRequestDto.getMail());
        commonDuplicateMailRequestDto.setUrUserId("0");

        int emailDupliCnt = userBuyerBiz.checkDuplicateMail(commonDuplicateMailRequestDto);
        return emailDupliCnt > 0;
    }

    /**
     * 추천인 ID 체크
     *
     * @param getIsCheckRecommendLoginIdRequestDto
     * @return GetIsCheckRecommendLoginIdResponseDto
     * @throws Exception
     */
    protected ApiResult<?> getIsCheckRecommendLoginId(GetIsCheckRecommendLoginIdRequestDto getIsCheckRecommendLoginIdRequestDto) throws Exception {
        GetIsCheckRecommendLoginIdResponseDto result = new GetIsCheckRecommendLoginIdResponseDto();

        GetIsCheckRecommendLoginIdResultVo getIsCheckRecommendLoginIdResultVo = userJoinMapper.getIsCheckRecommendLoginId(getIsCheckRecommendLoginIdRequestDto);
        log.info("====getIsCheckRecommendLoginIdResultVo ===" + getIsCheckRecommendLoginIdResultVo);

        if (getIsCheckRecommendLoginIdResultVo != null && !getIsCheckRecommendLoginIdResultVo.getRecommUserId().equals("")) {
            result.setRecommUserId(getIsCheckRecommendLoginIdResultVo.getRecommUserId());
        } else {
            return ApiResult.result(UserEnums.Join.NO_FIND_RECOMENDID);
        }

        return ApiResult.success(result);
    }

    /**
     * 회원가입
     *
     * @param addBuyerRequestDto
     * @return AddBuyerResponseDto
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    protected ApiResult<?> addBuyer(AddBuyerRequestDto addBuyerRequestDto) throws Exception {
        SaveBuyerRequestDto saveBuyerRequestDto = new SaveBuyerRequestDto();

        // ---------본인인증 정보 체크
        GetSessionUserCertificationResponseDto getSessionUserCertificationResponseDto = userCertificationBiz.getSessionUserCertification();
        // ---------AS-IS회원 기본 배송지 관련 세션 조회
        GetSessionAsisCustomerDto getSessionAsisCustomerDto = userCertificationBiz.getSessionAsisCustomerInfo();
        if (getSessionUserCertificationResponseDto == null || getSessionUserCertificationResponseDto.getCi() == null) {
            return ApiResult.result(UserEnums.Join.NO_FIND_INFO_USER);
        }

        log.info("==" + getSessionUserCertificationResponseDto);

        String userName = getSessionUserCertificationResponseDto.getUserName();
        String birthday = getSessionUserCertificationResponseDto.getBirthday();
        String mobile = getSessionUserCertificationResponseDto.getMobile();
        String gender = getSessionUserCertificationResponseDto.getGender();
        String ci = getSessionUserCertificationResponseDto.getCi();
        String asisCustomerNumber = getSessionAsisCustomerDto.getAsisCustomerNumber(); //기존회원 고객번호

        // ---------회원 Ci 중복 체크
        ApiResult<?> result = userCertificationBiz.checkDuplicateJoinUser();
        log.info("===" + result.getMessage());
        if (!result.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
            return ApiResult.result(UserEnums.Join.NO_FIND_INFO_USER);
        }

        // ---------아이디 이용불가 체크
		if (isNotAvailableLoginId(addBuyerRequestDto.getLoginId())) {
			return ApiResult.result(UserEnums.Join.ID_NOT_AVAILABLE);
		}

        // ---------아이디 중복체크

        GetIsCheckLoginIdRequestDto getIsCheckLoginIdRequestDto = new GetIsCheckLoginIdRequestDto();
        getIsCheckLoginIdRequestDto.setLoginId(addBuyerRequestDto.getLoginId());

        int cnt = userJoinMapper.getIsCheckLoginId(getIsCheckLoginIdRequestDto);
        if (cnt > 0) {
            log.info("====1301 아이디 중복 ===");
            return ApiResult.result(UserEnums.Join.ID_DUPLICATE);
        }

        // 기존회원 풀무원샵,올가 아이디 체크 API 호출
        if(getSessionAsisCustomerDto.getAsisLoginId() == null ||
        		(getSessionAsisCustomerDto.getAsisLoginId() != null && !getSessionAsisCustomerDto.getAsisLoginId().equals(addBuyerRequestDto.getLoginId()))) {

        	//아이디 존재여부 확인 API 호출
        	boolean asisUserSearchIdApiReuslt = asisUserApiUtil.userSerachId(addBuyerRequestDto.getLoginId());
        	if(asisUserSearchIdApiReuslt) {
        		 log.info("====1301 아이디 중복 ===");
                 return ApiResult.result(UserEnums.Join.ID_DUPLICATE);
        	}
        }

        // ---------이메일 중복체크

        CommonDuplicateMailRequestDto commonDuplicateMailRequestDto = new CommonDuplicateMailRequestDto();
        commonDuplicateMailRequestDto.setMail(addBuyerRequestDto.getMail());
        commonDuplicateMailRequestDto.setUrUserId("0");

        int emailDupliCnt = userBuyerBiz.checkDuplicateMail(commonDuplicateMailRequestDto);

        log.info("====emailDupliCnt===" + emailDupliCnt);

        if (emailDupliCnt > 0) {
            log.info("====1302 이메일중복===");
            return ApiResult.result(UserEnums.Join.EMAIL_DUPLICATE);
        }

        // ---------추천 아이디 체크
        if (addBuyerRequestDto.getRecommendLoginId() != null) {
            GetIsCheckRecommendLoginIdRequestDto getIsCheckRecommendLoginIdRequestDto = new GetIsCheckRecommendLoginIdRequestDto();
            getIsCheckRecommendLoginIdRequestDto.setRecommendLoginId(addBuyerRequestDto.getRecommendLoginId());

            result = getIsCheckRecommendLoginId(getIsCheckRecommendLoginIdRequestDto);

            if (!result.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
                return ApiResult.result(UserEnums.Join.NO_FIND_RECOMENDID);
            } else {
                saveBuyerRequestDto.setRecommUserId(((GetIsCheckRecommendLoginIdResponseDto) result.getData()).getRecommUserId());
            }
        }

        String password = SHA256Util.getUserPassword(addBuyerRequestDto.getPassword());
        log.info("password {}", password);
        saveBuyerRequestDto.setAddress1(addBuyerRequestDto.getAddress1());
        saveBuyerRequestDto.setAddress2(addBuyerRequestDto.getAddress2());
        saveBuyerRequestDto.setBuildingCode(addBuyerRequestDto.getBuildingCode());// 건물 관리 번호
        saveBuyerRequestDto.setLoginId(addBuyerRequestDto.getLoginId());
        saveBuyerRequestDto.setMail(addBuyerRequestDto.getMail());
        saveBuyerRequestDto.setMailYn(addBuyerRequestDto.getMailYn());
        saveBuyerRequestDto.setBirthday(birthday);
        saveBuyerRequestDto.setPassword(password);
        saveBuyerRequestDto.setMarketingYn(addBuyerRequestDto.getMarketingYn());
        saveBuyerRequestDto.setSmsYn(addBuyerRequestDto.getSmsYn());
        saveBuyerRequestDto.setZipCode(addBuyerRequestDto.getZipCode());
        saveBuyerRequestDto.setMobile(mobile.replaceAll("-", ""));
        saveBuyerRequestDto.setUserName(userName);
        saveBuyerRequestDto.setGender(gender);
        saveBuyerRequestDto.setCiCd(ci);
        saveBuyerRequestDto.setUserType(UserEnums.UserType.BUYER.getCode());
        saveBuyerRequestDto.setUserStatus(UserEnums.UserStatus.ACTIVITY_POSSIBLE.getCode());
        saveBuyerRequestDto.setUrGroupId(userGroupBiz.getDefaultGroup());
        saveBuyerRequestDto.setForeignerYn("N");
        saveBuyerRequestDto.setStatus(UserEnums.BuyerStatus.NORMAL.getCode());
        saveBuyerRequestDto.setPwdChangeCd("");
        saveBuyerRequestDto.setAutoLoginKey("");
        saveBuyerRequestDto.setFailCnt("0");

        if(userJoinMapper.addUrUser(saveBuyerRequestDto) == 0
            || userJoinMapper.addUrBuyer(saveBuyerRequestDto) == 0
            || userJoinMapper.addUrCertification(saveBuyerRequestDto) == 0) {
            throw new BaseException(BaseEnums.Default.FAIL);
        }

        GetClauseArrayRequestDto[] arrayExecuteDateArr = addBuyerRequestDto.getClause();

        int resCnt = 0;
        if (arrayExecuteDateArr != null) {
            for (int i = 0; i < arrayExecuteDateArr.length; i++) {
                saveBuyerRequestDto.setPsClauseGrpCd(arrayExecuteDateArr[i].getPsClauseGrpCd());
                saveBuyerRequestDto.setExecuteDate(arrayExecuteDateArr[i].getExecuteDate());
                resCnt = resCnt + userJoinMapper.addUrClauseAgreeLog(saveBuyerRequestDto);
            }
        }
        if(resCnt == 0) {
            throw new BaseException(BaseEnums.Default.FAIL);
        }
        AddUrShippingAddrRequestDto addUrShippingAddrRequestDto = new AddUrShippingAddrRequestDto();

        addUrShippingAddrRequestDto.setUrUserId(saveBuyerRequestDto.getUrUserId());

        // 주소 등록 공통으로 commonBuyerService.addShippingAddress();
        log.info("====saveBuyerRequestDto ===" + saveBuyerRequestDto);
        addUrShippingAddrRequestDto.setDefaultYn("Y"); // 기본주소여부
        addUrShippingAddrRequestDto.setShippingName("기본주소"); // 배송명칭
        addUrShippingAddrRequestDto.setReceiverName(saveBuyerRequestDto.getUserName());// 수신자명암호
        addUrShippingAddrRequestDto.setReceiverMobile(saveBuyerRequestDto.getMobile()); // 핸드폰번호암호
        addUrShippingAddrRequestDto.setReceiverZipCd(saveBuyerRequestDto.getZipCode());// 우편번호암호
        addUrShippingAddrRequestDto.setReceiverAddr1(saveBuyerRequestDto.getAddress1());// 주소암호
        addUrShippingAddrRequestDto.setReceiverAddr2(saveBuyerRequestDto.getAddress2());// 상세주소암호
        addUrShippingAddrRequestDto.setBuildingCode(saveBuyerRequestDto.getBuildingCode());// 건물관리번호
        addUrShippingAddrRequestDto.setShippingComment("");// 최종배송요청사항
        log.info("====addUrShippingAddrRequestDto ===" + addUrShippingAddrRequestDto);
        if(userJoinMapper.addUrShippingAddr(addUrShippingAddrRequestDto) == 0) {
            throw new BaseException(BaseEnums.Default.FAIL);
        }

        //as-is회원일 경우
        //1. 배송지 조회 API호출 후 기본배송지 제외한 배송지리스트 저장
        //2. 풀무원 적립금 조회 API호출 후 as-is포인트 적립&사용 처리
        if(StringUtil.isNotEmpty(asisCustomerNumber)) {

        	//배송지 조회 API 호출
        	SearchCustomerDeliveryListResponseDto searchCustomerDeliveryListResponseDto = asisUserApiUtil.searchCustomerDeliveryList(asisCustomerNumber);

        	//배송지 리스트 중에서 기본배송지 N인 배송지만 저장
        	if(searchCustomerDeliveryListResponseDto.getData() != null) {
        		List<SearchCustomerDeliveryDto> searchCustomerDeliveryList = searchCustomerDeliveryListResponseDto.getData();
        		for(SearchCustomerDeliveryDto dto : searchCustomerDeliveryList) {
        			if("N".equals(dto.getBasicYn())) {
        				AddUrShippingAddrRequestDto asisUserShippingAddrRequestDto = new AddUrShippingAddrRequestDto();
        				asisUserShippingAddrRequestDto.setUrUserId(saveBuyerRequestDto.getUrUserId());
        				asisUserShippingAddrRequestDto.setDefaultYn("N");
        				asisUserShippingAddrRequestDto.setShippingName(dto.getShippingNm());
        				asisUserShippingAddrRequestDto.setReceiverName(dto.getReceiverNm());
        				asisUserShippingAddrRequestDto.setReceiverMobile(dto.getReceiverMo());
        				asisUserShippingAddrRequestDto.setReceiverTel(dto.getReceiverTel());
        				asisUserShippingAddrRequestDto.setReceiverZipCd(dto.getReceiverZipCd());
        				asisUserShippingAddrRequestDto.setReceiverAddr1(dto.getReceiverAddr1());
        				asisUserShippingAddrRequestDto.setReceiverAddr2(dto.getReceiverAddr2());
        				asisUserShippingAddrRequestDto.setBuildingCode(dto.getBuildingCd());
        				asisUserShippingAddrRequestDto.setShippingComment("");
        				userJoinMapper.addUrShippingAddr(asisUserShippingAddrRequestDto);
        			}
        		}
        	}

            if(Constants.SITE_NO_PULMUONE.equals(addBuyerRequestDto.getSiteNo()) || Constants.SITE_NO_ORGA.equals(addBuyerRequestDto.getSiteNo())) {
                // PF-3858 회원인증이벤트시 유입경로가 있을경우 적립금이관대신 양쪽 사이트에 쿠폰을 지급해준다.
                // AS-IS 사이트에 쿠폰지급(테이블에만 저장후 AS-IS 배치가 가져간다.)
                CouponJoinCertEventRequestDto couponJoinCertEventRequestDto = new CouponJoinCertEventRequestDto();
                couponJoinCertEventRequestDto.setSiteNo(addBuyerRequestDto.getSiteNo());
                couponJoinCertEventRequestDto.setUrUserId(saveBuyerRequestDto.getUrUserId());
                couponJoinCertEventRequestDto.setCustomerNumber(asisCustomerNumber);
                promotionCouponBiz.addPmCouponJoinEventListByJoinUrUserId(couponJoinCertEventRequestDto);
            } else {
                // HGRM-8017 적립금 자동이관 제외 처리 (최용호)
                //as-is풀무원 적립금 조회 API 호출
//                SearchCustomerRsrvTotalResponseDto searchCustomerRsrvTotalResponseDto = asisUserApiUtil.searchCustomerRsrvTotal(asisCustomerNumber, "N", "");
//                int totalRemainPrice = searchCustomerRsrvTotalResponseDto.getPulmuoneShopPoint();
//
//                //to-be풀무원 적립금 적립
//                Long urUserId = Long.valueOf(saveBuyerRequestDto.getUrUserId());
//                Long amount = Long.valueOf(totalRemainPrice);
//                ApiResult<?> pointResult = pointBiz.depositPreviousPulmuoneMemberPoints(urUserId, amount, asisCustomerNumber);
//                if(BaseEnums.Default.SUCCESS.equals(pointResult.getMessageEnum()) || PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT.equals(pointResult.getMessageEnum())){
//                    //as-is풀무원 적립금 소멸 API 호출
//                    asisUserApiUtil.minusCustomerRsrv("N", asisCustomerNumber, totalRemainPrice);
//                }
            }
        }

        GetJoinResultVo getJoinResultVo = new GetJoinResultVo();

        getJoinResultVo.setUserName(saveBuyerRequestDto.getUserName());

        Long recommUserId = null;
        if(StringUtil.isNotEmpty(saveBuyerRequestDto.getRecommUserId())) {
            recommUserId = Long.valueOf(saveBuyerRequestDto.getRecommUserId());
        }

        // 쿠폰 자동지급 - 회원가입
        getJoinResultVo.setCoupon(addBuyerAddPromotion(
                Long.valueOf(saveBuyerRequestDto.getUrUserId()),recommUserId)
        );

        userJoinMapper.addUrAccount(saveBuyerRequestDto.getUrUserId());

        // 회원가입 디바이스 로그 저장
        systemLogBiz.addDeviceLogUserJoin(addBuyerRequestDto.getUrPcidCd(), Long.valueOf(saveBuyerRequestDto.getUrUserId()));

        addBuyerRequestDto.setUrUserId(saveBuyerRequestDto.getUrUserId());
        return ApiResult.success(getJoinResultVo);
    }

    protected List<CouponInfoByUserJoinVo> addBuyerAddPromotion(Long urUserId, Long recommUserId) throws Exception {
        List<CouponInfoByUserJoinVo> result = new ArrayList<>();
        // 탈퇴 CI 체크 하여 기존 고객 재가입 프로세스면 발급 제외
        GetSessionUserCertificationResponseDto getSessionUserCertificationResponseDto = userCertificationBiz.getSessionUserCertification();
        if (getSessionUserCertificationResponseDto == null || getSessionUserCertificationResponseDto.getCi() == null)
            return result;
        if (getSessionUserCertificationResponseDto.getBeforeUserDropYn() == null || getSessionUserCertificationResponseDto.getBeforeUserDropYn().equals("Y"))
            return result;

        // 쿠폰 발급 Process
        List<CouponInfoByUserJoinVo> targetCouponList = promotionCouponBiz.getCouponInfoByUserJoin();
        for (CouponInfoByUserJoinVo vo : targetCouponList) {
            //회원 가입 쿠폰
            if (vo.getIssueDetailType().equals(CouponEnums.IssueDetailType.USER_JOIN.getCode()) ||
                    //추천인 쿠폰 - 추천인을 등록한 계정
                    (recommUserId != null && vo.getIssueDetailType().equals(CouponEnums.IssueDetailType.RECOMMENDER.getCode()))) {
                CouponEnums.AddCouponValidation couponResult = promotionCouponBiz.addCouponByOne(vo.getPmCouponId(), urUserId);
                if (CouponEnums.AddCouponValidation.PASS_VALIDATION.equals(couponResult)) {
                    result.add(vo);
                }
            }
        }

        return result;
    }

    protected int addUrClauseAgreeLog(SaveBuyerRequestDto saveBuyerRequestDto) throws Exception {

        return userJoinMapper.addUrClauseAgreeLog(saveBuyerRequestDto);
    }

    protected int putUrAccount(String urUserId) throws Exception {

        return userJoinMapper.putUrAccount(urUserId);
    }

    /**
     * @param loginId
     * @return UserVo
     * @Desc 회원기본정보 조회
     */
    protected UserVo getUserInfo(String loginId) {
        return userJoinMapper.getUserInfo(loginId);
    }

    /**
     * @param userVo
     * @return int
     * @throws Exception
     * @Desc 회원기본정보 등록
     */
    protected int addUser(UserVo userVo) throws Exception {
        return userJoinMapper.addUser(userVo);
    }

    /**
     * @param accountVo
     * @return int
     * @throws Exception
     * @Desc 회원정보활동정보 등록
     */
    protected int addAccount(AccountVo accountVo) throws Exception {
        return userJoinMapper.addAccount(accountVo);
    }

    /**
     * @param userVo
     * @return int
     * @throws Exception
     * @Desc 회원기본정보 수정
     */
    protected int putUser(UserVo userVo) throws Exception {
        return userJoinMapper.putUser(userVo);
    }


    /**
     * 기존 회원 여부 체크 API호출
     *
     * @param loginId
     * @param password
     * @param captcha
     * @return UserInfoCheckResponseDto
     * @throws Exception
     */
    protected UserInfoCheckResponseDto asisUserCheck(String loginId, String password) throws Exception {
        return asisUserApiUtil.userInfoCheck(loginId, password);
    }

    /**
	 * @Desc  회원가입 완료 시 전송할 정보
	 * @param saveBuyerRequestDto
	 * @return JoinResultVo
	 */
	public JoinResultVo getJoinCompletedInfo(String urUserId) {
		return userJoinMapper.getJoinCompletedInfo(urUserId);
	}

	/**
	 * 이용 불가능 아이디 체크
	 *
	 * @param loginId
	 * @return
	 */
	protected boolean isNotAvailableLoginId(String loginId) throws Exception {
		for (String notId : Constants.NOT_AVAILABLE_LOGIN_ID) {
			if (loginId.indexOf(notId) >= 0) {
				return true;
			}
		}
		return false;
	}
}
