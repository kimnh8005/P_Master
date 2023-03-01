package kr.co.pulmuone.mall.display.layout.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.pulmuone.v1.comm.enums.CodeCommEnum;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums.AutoLoginKeyActionCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.mall.display.layout.dto.CommonDataDto;
import kr.co.pulmuone.mall.display.layout.dto.GetLayoutInfoResultDto;
import kr.co.pulmuone.mall.display.layout.dto.LogoDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.constants.DisplayConstants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.DisplayEnums;
import kr.co.pulmuone.v1.comm.util.CookieUtil;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.SHA256Util;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.brand.service.GoodsBrandBiz;
import kr.co.pulmuone.v1.goods.category.dto.GetMallDataResponseDto;
import kr.co.pulmuone.v1.goods.category.dto.vo.GetCategoryListResultVo;
import kr.co.pulmuone.v1.goods.category.service.GoodsCategoryBiz;
import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import kr.co.pulmuone.v1.promotion.event.service.PromotionEventBiz;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartDataRequestDto;
import kr.co.pulmuone.v1.shopping.cart.service.ShoppingCartBiz;
import kr.co.pulmuone.v1.user.buyer.dto.DoLoginRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.DoLoginResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetSearchWordResultVo;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.user.certification.dto.GetBuyerCertificationByAutoLoginKeyResponseDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.company.service.UserCompanyBiz;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataAutoLoginDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LayoutService {

    @Autowired
    private GoodsCategoryBiz goodsCategoryBiz;

    @Autowired
    private GoodsBrandBiz goodsBrandBiz;

    @Autowired
    private UserBuyerBiz userBuyerBiz;

    @Autowired
    private UserCompanyBiz userCompanyBiz;

    @Autowired
    private UserCertificationBiz userCertificationBiz;

	@Autowired
	public ShoppingCartBiz shoppingCartBiz;

	@Autowired
    private PromotionEventBiz promotionEventBiz;

	@Autowired
	PolicyConfigBiz policyConfigBiz;

    public ApiResult<?> getCommonData() throws Exception {

        CommonDataDto dataDto = new CommonDataDto();
        dataDto.setBrand(goodsBrandBiz.getUrBrandList());
        dataDto.setDpBrand(goodsBrandBiz.getDpBrandList());
        dataDto.setCompany(userCompanyBiz.getHeadquartersCompany());
		LogoDto logoDto = new LogoDto();
		logoDto.setShopLogoImage(getLogImgPath(DisplayConstants.SHOP_LOGO_IMAGE_FILE_PATH_KEY, DisplayConstants.SHOP_LOGO_IMAGE_FILE_NAME_KEY));
		logoDto.setShopLogoDetailImage(getLogImgPath(DisplayConstants.SHOP_LOGO_DETAIL_IMAGE_FILE_PATH_KEY, DisplayConstants.SHOP_LOGO_DETAIL_IMAGE_FILE_NAME_KEY));
		dataDto.setLogo(logoDto);
        return ApiResult.success(dataDto);
    }

	private String getLogImgPath(String pathKey, String nameKey) throws Exception {
		String path = policyConfigBiz.getConfigValue(pathKey);
		String name = policyConfigBiz.getConfigValue(nameKey);
		return path + name;
	}

    public ApiResult<?> getMallData(String mallDiv) throws Exception {

    	GetMallDataResponseDto result = new GetMallDataResponseDto();

    	//카테고리 정보
    	List<GetCategoryListResultVo> categoryList = goodsCategoryBiz.getCategoryList(mallDiv);
        if (categoryList.isEmpty()) {
            return ApiResult.result(DisplayEnums.LayoutMessageEnums.MALL_CATEGORY_DATA_EMPTY);
        }
        result.setCategory(categoryList);

        //브랜드 정보 추가 예정


        return ApiResult.success(result);
    }


    public ApiResult<?> getLayoutInfoPc(HttpServletRequest request, HttpServletResponse response) throws Exception {

        GetLayoutInfoResultDto layoutInfoPcResultDto = new GetLayoutInfoResultDto();
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		boolean isNonMember = StringUtil.isNotEmpty(buyerVo.getNonMemberCiCd());

        HashMap<String, Object> user = new HashMap<>();
        if (isMember) {
            // 로그인 여부,회원명
            user.put("isLogin", true);
            user.put("isEmployee", StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode()));
            user.put("isNonMember", false);
            user.put("userName", buyerVo.getUserName());
            user.put("userMobile", buyerVo.getUserMobile());
            user.put("userEmail", buyerVo.getUserEmail());
            // 최근 검색어 ID,최근 검색어
            List<GetSearchWordResultVo> getSearchWordResultVoList = userBuyerBiz.getUserSearchWordLogList(urUserId);
            layoutInfoPcResultDto.setSearchWord(getSearchWordResultVoList);
		} else if (isNonMember) {
			// 비회원 로그인
			user.put("isLogin", false);
            user.put("isEmployee", false);
            user.put("isNonMember", true);
            user.put("userName", buyerVo.getNonMemberUserName());
            user.put("userMobile", buyerVo.getNonMemberMobile());
            user.put("userEmail", "");
        } else {
            // 로그인 여부
            user.put("isLogin", false);
            user.put("isNonMember", false);
        }
        layoutInfoPcResultDto.setUser(user);

        // 장바구니 정보 요청 파라미터 세팅
        GetCartDataRequestDto getCartDataRequestDto = new GetCartDataRequestDto();
		getCartDataRequestDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
		if (isMember) {
			getCartDataRequestDto.setUrUserId(new Long(urUserId));
		}
		getCartDataRequestDto.setDeviceInfo(DeviceUtil.getDirInfo());
		getCartDataRequestDto.setApp(DeviceUtil.isApp());
		getCartDataRequestDto.setMember(isMember);
		getCartDataRequestDto.setEmployee(isEmployee);

		// 장바구니 담은 품목 수
        HashMap<String, Integer> cart = new HashMap<>();
        cart.put("cartCount", shoppingCartBiz.getCartCount(getCartDataRequestDto));
        layoutInfoPcResultDto.setCart(cart);

        // 미션 스탬프 이벤트 - 정보 조회
        long urUserIdL = 0L;
        long userGroup = 0L;

        if(isMember) {
            urUserIdL = Long.parseLong(urUserId);
            userGroup = buyerVo.getUrGroupId();
        }

        CodeCommEnum userStatus = UserEnums.UserStatusType.NONMEMBER;
        if (isMember) userStatus = UserEnums.UserStatusType.MEMBER;
        if (isEmployee) userStatus = UserEnums.UserStatusType.EMPLOYEE;

        layoutInfoPcResultDto.setStamp(promotionEventBiz.getMissionStampByUser(urUserIdL, DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode(), userStatus, userGroup));

        return ApiResult.success(layoutInfoPcResultDto);
    }

    public ApiResult<?> getLayoutInfoMobie(HttpServletRequest request, HttpServletResponse response, String autoLoginKey) throws Exception {
        GetLayoutInfoResultDto layoutInfoMobileResultDto = new GetLayoutInfoResultDto();
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		boolean isNonMember = StringUtil.isNotEmpty(buyerVo.getNonMemberCiCd());

        if (StringUtil.isEmpty(buyerVo.getUrUserId()))
        {
            DoLoginResponseDto loginResponseDto = autoLogin(request, response, autoLoginKey);

            if (loginResponseDto != null) {
            	// 로그인후 세션성보 다시 가지고와야함
                buyerVo = SessionUtil.getBuyerUserVO();
                urUserId = StringUtil.nvl(buyerVo.getUrUserId());
        		isMember = StringUtil.isNotEmpty(urUserId);
        		isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());

                if (loginResponseDto.getCertification() != null)
                {
                    layoutInfoMobileResultDto.setCertification(loginResponseDto.getCertification());
                }
                if (loginResponseDto.getClause() != null)
                {
                    layoutInfoMobileResultDto.setClause(loginResponseDto.getClause());
                }
                if (loginResponseDto.getMaketting() != null)
                {
                    layoutInfoMobileResultDto.setMaketting(loginResponseDto.getMaketting());
                }
                if (loginResponseDto.getNoti() != null)
                {
                    layoutInfoMobileResultDto.setNoti(loginResponseDto.getNoti());
                }
                if (loginResponseDto.getStop() != null)
                {
                    layoutInfoMobileResultDto.setStop(loginResponseDto.getStop());
                }
                if (loginResponseDto.getAutoLogin() != null)
                {
                    layoutInfoMobileResultDto.setAutoLogin(loginResponseDto.getAutoLogin());
                }
            }
        }

        HashMap<String, Object> user = new HashMap<>();
        if (isMember) {
            // 로그인 여부, 회원명
        	user.put("isLogin", true);
            user.put("isEmployee", StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode()));
            user.put("isNonMember", false);
            user.put("userName", buyerVo.getUserName());
            user.put("userMobile", buyerVo.getUserMobile());
            user.put("userEmail", buyerVo.getUserEmail());

            // 최근 검색어 ID,최근 검색어
            List<GetSearchWordResultVo> getSearchWordResultVoList = userBuyerBiz.getUserSearchWordLogList(urUserId);
            layoutInfoMobileResultDto.setSearchWord(getSearchWordResultVoList);
        } else if (isNonMember) {
			// 비회원 로그인
			user.put("isLogin", false);
            user.put("isEmployee", false);
            user.put("isNonMember", true);
            user.put("userName", buyerVo.getNonMemberUserName());
            user.put("userMobile", buyerVo.getNonMemberMobile());
            user.put("userEmail", "");
        } else {
            // 로그인 여부
            user.put("isLogin", false);
            user.put("isNonMember", false);
        }
        layoutInfoMobileResultDto.setUser(user);

        // 장바구니 정보 요청 파라미터 세팅
        GetCartDataRequestDto getCartDataRequestDto = new GetCartDataRequestDto();
		getCartDataRequestDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
		if (isMember) {
			getCartDataRequestDto.setUrUserId(new Long(urUserId));
		}
		getCartDataRequestDto.setDeviceInfo(DeviceUtil.getDirInfo());
		getCartDataRequestDto.setApp(DeviceUtil.isApp());
		getCartDataRequestDto.setMember(isMember);
		getCartDataRequestDto.setEmployee(isEmployee);

        // 장바구니 담은 품목 수
        HashMap<String, Integer> cart = new HashMap<>();
        cart.put("cartCount", shoppingCartBiz.getCartCount(getCartDataRequestDto));
        layoutInfoMobileResultDto.setCart(cart);

        // 미션 스탬프 이벤트 - 정보 조회
        long urUserIdL = 0L;
        long userGroup = 0L;
        if(isMember) {
            urUserIdL = Long.parseLong(urUserId);
            userGroup = buyerVo.getUrGroupId();
        }

        CodeCommEnum userStatus = UserEnums.UserStatusType.NONMEMBER;
        if (isMember) userStatus = UserEnums.UserStatusType.MEMBER;
        if (isEmployee) userStatus = UserEnums.UserStatusType.EMPLOYEE;
        layoutInfoMobileResultDto.setStamp(promotionEventBiz.getMissionStampByUser(urUserIdL, DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode(), userStatus, userGroup));

        return ApiResult.success(layoutInfoMobileResultDto);
    }


    private DoLoginResponseDto autoLogin(HttpServletRequest request, HttpServletResponse response, String autoLoginValue) throws Exception {

        DoLoginResponseDto loginResponseDto = new DoLoginResponseDto();

        if (autoLoginValue != null && !autoLoginValue.equals("")) {
            String[] tmp = autoLoginValue.split("-");
            String urUserId = tmp[0];
            String autoLoginKey = tmp[1];
            for (int i = 2; i < tmp.length; i++) {
                autoLoginKey += "-" + tmp[i];
            }

            GetBuyerCertificationByAutoLoginKeyResponseDto getBuyerCertificationByAutoLoginKeyResponseDto = userCertificationBiz.getBuyerCertificationByAutoLoginKey(urUserId, autoLoginKey);

            if (getBuyerCertificationByAutoLoginKeyResponseDto != null) {
                DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
                doLoginRequestDto.setLoginId(getBuyerCertificationByAutoLoginKeyResponseDto.getLoginId());
                doLoginRequestDto.setEncryptPassword(getBuyerCertificationByAutoLoginKeyResponseDto.getEncryptPassword());
                doLoginRequestDto.setAutoLogin(true);
                doLoginRequestDto.setSaveLoginId(true);

                ApiResult<?> result = userCertificationBiz.doLogin(doLoginRequestDto, false, request, response);

                loginResponseDto = (DoLoginResponseDto) result.getData();

                // 로그인 실패시 프론트 자동 로그인 키 제거
                if (!result.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
                	DoLoginResponseDataAutoLoginDto autoLoginDto = new DoLoginResponseDataAutoLoginDto();
                	autoLoginDto.setAutoLoginKeyActionCode(AutoLoginKeyActionCode.DELETE.getCode());
                	loginResponseDto.setAutoLogin(autoLoginDto);
                }
            } else {
            	// 로그인 실패시 프론트 자동 로그인 키 제거
            	DoLoginResponseDataAutoLoginDto autoLoginDto = new DoLoginResponseDataAutoLoginDto();
            	autoLoginDto.setAutoLoginKeyActionCode(AutoLoginKeyActionCode.DELETE.getCode());
            	loginResponseDto.setAutoLogin(autoLoginDto);
            }
        }

        return loginResponseDto;
    }

}
