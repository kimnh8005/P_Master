package kr.co.pulmuone.mall.comm.interceptor;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.pulmuone.v1.comm.constants.GoodsConstants;
import kr.co.pulmuone.v1.comm.constants.SystemConstants;
import kr.co.pulmuone.v1.promotion.event.dto.vo.EventInfoFromMetaVo;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.ExhibitInfoFromMetaVo;
import kr.co.pulmuone.v1.send.slack.service.SendSlackBiz;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.user.group.service.UserGroupBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;

import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.constants.DisplayConstants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.PolicyEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.*;
import kr.co.pulmuone.v1.customer.inspect.service.InspectNoticeBiz;
import kr.co.pulmuone.v1.display.contents.dto.vo.ContentsDetailVo;
import kr.co.pulmuone.v1.display.contents.service.DisplayContentsBiz;
import kr.co.pulmuone.v1.goods.brand.dto.vo.DpBrandListResultVo;
import kr.co.pulmuone.v1.goods.brand.service.GoodsBrandBiz;
import kr.co.pulmuone.v1.goods.category.dto.GetCategoryResultDto;
import kr.co.pulmuone.v1.goods.category.dto.vo.GetCategoryListResultVo;
import kr.co.pulmuone.v1.goods.category.dto.vo.GetSubCategoryListResultVo;
import kr.co.pulmuone.v1.goods.category.service.GoodsCategoryBiz;
import kr.co.pulmuone.v1.goods.goods.dto.vo.DetailSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.policy.config.dto.vo.MetaConfigVo;
import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import kr.co.pulmuone.v1.promotion.event.dto.EventListByUserRequestDto;
import kr.co.pulmuone.v1.promotion.event.dto.EventListByUserResponseDto;
import kr.co.pulmuone.v1.promotion.event.dto.vo.EventListByUserVo;
import kr.co.pulmuone.v1.promotion.event.service.PromotionEventBiz;
import kr.co.pulmuone.v1.promotion.exhibit.dto.ExhibitListByUserRequestDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.ExhibitListByUserResponseDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.SelectExhibitResponseDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.SelectListByUserResponseDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.ExhibitListByUserVo;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.SelectListByUserVo;
import kr.co.pulmuone.v1.promotion.exhibit.service.PromotionExhibitBiz;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;
import kr.co.pulmuone.v1.system.basic.service.SystemBasicEnvironmentBiz;
import kr.co.pulmuone.v1.user.environment.service.UserEnvironmentBiz;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserBuyerBiz userBuyerBiz;

    @Autowired
    private UserEnvironmentBiz userEnvironmentBiz;

    @Autowired
    private PolicyConfigBiz policyConfigBiz;

    @Autowired
    private GoodsCategoryBiz goodsCategoryBiz;

    @Autowired
    private GoodsGoodsBiz goodsGoodsBiz;

    @Autowired
    private PromotionExhibitBiz promotionExhibitBiz;

    @Autowired
    private PromotionEventBiz promotionEventBiz;

    @Autowired
    private DisplayContentsBiz displayContentsBiz;

    @Autowired
    private GoodsBrandBiz goodsBrandBiz;

    @Autowired
    private InspectNoticeBiz inspectNoticeBiz;

    @Autowired
    private UserGroupBiz userGroupBiz;

    @Autowired
    private SystemBasicEnvironmentBiz systemBasicEnvironmentBiz;

    @Autowired
    private SendSlackBiz sendSlackBiz;

    @Autowired
    private SendTemplateBiz sendTemplateBiz;

    @Value("${spring.profiles}")
    private String profiles;

    @Value("${app.storage.public.public-storage-url}")
    private String publicStorageUrl; // public 저장소 접근 url

    @Value("${app.storage.public.public-url-path}")
    private String publicUrlPath; // public 저장소를 경유하지 않고 API 서버로 저장소 접근시 url 경로 ( CORS 회피용 )

    @Value("${app.domain}")
    private String domain; // 도메인

    @Value("${app.storage.public.public-cdn-url}")
    private String publicCDNUrl; // public CDN url

    /**
     * Controller 실행 직전에 동작
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isSuccess = true;

        log.info("===================================preHandle===================================");

        //request Uri
        String requestURI = request.getRequestURI();

        // CSRF 처리 S
        GetEnvironmentListRequestDto getEnvironmentListRequestDto = new GetEnvironmentListRequestDto();
        getEnvironmentListRequestDto.setSearchEnvironmentKey("CSRF_MALL_CHECK_YN"); // BOS CSRF 체크 설정 확인
        GetEnvironmentListResultVo getEnvironmentListResultVo = systemBasicEnvironmentBiz.getEnvironment(getEnvironmentListRequestDto);

        if ("Y".equals(getEnvironmentListResultVo.getEnvironmentValue())) {
            String requestMethod = request.getMethod();
            if (!"GET".equals(requestMethod)) { // 우선 GET 메소드는 제외
                if (
                        requestURI.indexOf("/pg/inicis") > -1 // 이니시스 결제 callback url 제외
                                || requestURI.indexOf("/pg/kcp") > -1 // KCP 결제 callback url 제외
                ) {
                    // 예외 처리
                    log.info("## CSRF 예외 URL ## : " + requestURI);
                } else {
                    String origin = request.getHeader(HttpHeaders.ORIGIN);

// DEV1, DEV2 서버에 대한 CSRF 임시 처리 S
                    // 현재 yml에서 관리하는 domain은 dev0로 설정되어 있기 때문에 dev1, dev2에서는 항상 throw exception을 던진다.
                    // 때문에 dev1, dev2에 대해 임시처리 진행
                    if ("https://dev1shop.pulmuone.online".equals(origin) || "https://dev2shop.pulmuone.online".equals(origin)) {
                    	domain = origin;
                    }
// DEV1, DEV2 서버에 대한 CSRF 임시 처리 E

                    // ORIGIN referer 체크
                    if (!domain.equals(origin)) {
                        String errorMessage = "## MALL CSRF ERROR - ORIGIN domain failed ## : " + requestURI;
                        log.error(errorMessage);
                        sendSlackBiz.notify(errorMessage);
                        if (!SystemConstants.AUTH_CSRF_SMS_TARGET_MOBILE.equals("0")) {
                            sendTemplateBiz.sendSmsApi(AddSmsIssueSelectRequestDto.builder()
                                    .content(errorMessage)
                                    .urUserId("0")
                                    .mobile(SystemConstants.AUTH_CSRF_SMS_TARGET_MOBILE)
                                    .reserveYn("N")
                                    .build());
                        }
                        throw new BaseException(UserEnums.Join.NEED_LOGIN);
                    }
                }
            }
        }
        // CSRF 처리 E

        //새션 처리
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null) {
            SessionUtil.setUserVO(new BuyerVo());
        }

        //PCID 추가 및 쿠키생성
        if (CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY) == "") {
            String urPcidCd = UidUtil.randomUUID().toString();
            String agent = request.getHeader("User-Agent");
            if (StringUtil.isNotEmpty(agent)) {
                userEnvironmentBiz.addPCID(urPcidCd, agent);

                CookieUtil.setCookie(response, BuyerConstants.COOKIE_PCID_CODE_KEY, urPcidCd, 365 * 24 * 60 * 60);
            }
        }

        return isSuccess;
    }

    /**
     * Controller 진입 후 view 랜더링 전에 동작
     **/
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("===================================postHandle===================================");
        if (request.getRequestURI().indexOf("/pg/") == 0 || request.getRequestURI().indexOf("/ad/gateway") == 0) return;
        if (modelAndView == null) return;
        List<MetaConfigVo> metaConfigVos = policyConfigBiz.getMetaConfig();
        String metaTitle = "";
        String metaKeyword = "";
        String metaDescription = "";
        String configFaviconPath = "";
        String configFaviconName = "";

        for (MetaConfigVo vo : metaConfigVos) {
            if (vo.getPolicyKey().equals(PolicyEnums.PolicyKey.ST_F_TITLE.getCode())) {
                metaTitle = vo.getPolicyValue();
            }
            if (vo.getPolicyKey().equals(PolicyEnums.PolicyKey.ST_SCH_EG_KEYWORD.getCode())) {
                metaKeyword = vo.getPolicyValue();
            }
            if (vo.getPolicyKey().equals(PolicyEnums.PolicyKey.ST_DESCRIPTION.getCode())) {
                metaDescription = vo.getPolicyValue();
            }
            if (vo.getPolicyKey().equals(PolicyEnums.PolicyKey.FAVICON_IMAGE_FILE_PATH.getCode())) {
                configFaviconPath = vo.getPolicyValue();
            }
            if (vo.getPolicyKey().equals(PolicyEnums.PolicyKey.FAVICON_IMAGE_FILE_NAME.getCode())) {
                configFaviconName = vo.getPolicyValue();
            }
        }
        setMetaAttr(modelAndView, metaTitle, metaKeyword, metaDescription);
        modelAndView.addObject("metaFavicon", this.publicStorageUrl + configFaviconPath + configFaviconName);
        modelAndView.addObject("imageDomain", this.publicCDNUrl); // MALL Vue JS 에서만 사용되는 이미지 URL 을 CDN URL 으로 변경
        modelAndView.addObject("metaGrade", userGroupBiz.getGroupNameByMeta());
        modelAndView.addObject("metaUserStatus", userBuyerBiz.getUserStatusByMeta());

        //정기점검 화면 redirect 설정 및 임직원 화면
        String uri = request.getRequestURI();
        String remoteAddr = request.getRemoteAddr();
//        String welcomeToken = CookieUtil.getCookie(request, "urWcidCd");
        boolean isInspectAllow = inspectNoticeBiz.checkInspectAllowed(remoteAddr);

        //정기점검 화면 redirect 설정
        if (isInspectAllow == false && "/inspectNotice".equals(uri) == false) {
            response.sendRedirect("/inspectNotice ");
        } else if (isInspectAllow == true && "/inspectNotice".equals(uri) == true) {
            response.sendRedirect("");
        }

//        if(!BaseEnums.ServerType.LOCAL.getCode().equals(profiles)) {
//            //임직원 Open 용 Intercept 추가
//            if ("/welcomeLogin".equals(uri) == false
//                    && "/welcomeLogin/employeeAuth".equals(uri) == false
//                    && "/user/welcomeLogin/employeesCertification".equals(uri) == false
//                    && "/user/welcomeLogin/employeesCertificationVeriyfy".equals(uri) == false
//                    && "/user/preJoin".equals(uri) == false
//                    && "/SHOPCBT".equals(uri) == false
//                    && "/shopcbt".equals(uri) == false
//                    && uri.indexOf("/user/login/") < 0
//            ) {
//                //url 비교 및 쿠키 사이즈 비교
//                if (welcomeToken.getBytes(StandardCharsets.UTF_8).length != 36) {
//                    response.sendRedirect("/welcomeLogin");
//                }
//            }
//        }

        try {
            setModelAndView(request, response, modelAndView);
        } catch (Exception e) {
            response.sendError(500);
        }
    }

    /**
     * Controller 진입 후 view 랜더링 후에 동작
     **/
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exp) throws Exception {

        log.info("===================================afterCompletion===================================");

    }

    /**
     * 비동기 요청 시 PostHandle와 afterCompletion메서드를 수행하지 않고 이 메서드를 수행
     **/
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) {

        log.info("===================================afterConcurrentHandlingStarted===================================");

    }

    private void setModelAndView(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
        String requestUri = request.getRequestURI();
        String deviceType = DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode();

        switch (requestUri) {
            //1.메인
            case "/":
                setMetaAttr(modelAndView, UserEnums.MetaType.MAIN.getTitle(), UserEnums.MetaType.MAIN.getKeyword(), UserEnums.MetaType.MAIN.getDescription());
                break;
            case "/orga":
                setMetaAttr(modelAndView, UserEnums.MetaType.ORGA.getTitle(), UserEnums.MetaType.ORGA.getKeyword(), UserEnums.MetaType.ORGA.getDescription());
                break;
            case "/eatslim":
                setMetaAttr(modelAndView, UserEnums.MetaType.EATSLIM.getTitle(), UserEnums.MetaType.EATSLIM.getKeyword(), UserEnums.MetaType.EATSLIM.getDescription());
                break;
            case "/babymeal":
                setMetaAttr(modelAndView, UserEnums.MetaType.BABYMEAL.getTitle(), UserEnums.MetaType.BABYMEAL.getKeyword(), UserEnums.MetaType.BABYMEAL.getDescription());
                break;
            //2.공통
            case "/shop/goodsList":
                String categoryParam = request.getParameter("itemId");
                if (StringUtil.isEmpty(categoryParam)) return;
                GetCategoryResultDto categoryResultDto = goodsCategoryBiz.getCategory(Long.valueOf(categoryParam));
                if (categoryResultDto == null) return;
                setMetaAttr(modelAndView,
                        UserEnums.MetaType.SHOP_GOODSLIST.getTitle().replace("{text}", categoryResultDto.getCategoryName()),
                        categoryResultDto.getCategoryName(),
                        UserEnums.MetaType.SHOP_GOODSLIST.getDescription().replace("{text}", categoryResultDto.getCategoryName()));
                break;
            case "/shop/goodsView": //상품상세
                String goodsParam = request.getParameter("goods");
                if (StringUtil.isEmpty(goodsParam)) return;
                DetailSelectGoodsVo goodsVo = goodsGoodsBiz.getDetailSelectGoods(Long.valueOf(goodsParam));
                if (goodsVo == null) return;
                setMetaAttr(modelAndView,
                        UserEnums.MetaType.SHOP_GOODSVIEW.getTitle(),
                        goodsVo.getGoodsName() + " " + goodsVo.getGoodsSearchKeyword() + " " + goodsVo.getGoodsDescription(),
                        UserEnums.MetaType.SHOP_GOODSVIEW.getDescription().replace("{text}", goodsVo.getGoodsName()),
                        goodsVo.getGoodsImage());
                break;
            // AS-IS 에서 품목코드/바코드로 넘어오는경우 리다이렉션 처리
            case "/shop/itemView":
                String itemParam = request.getParameter("item");
                String barCodeParam = request.getParameter("barCode");
                if (StringUtil.isEmpty(itemParam) && StringUtil.isEmpty(barCodeParam)) return;

                DetailSelectGoodsVo itemGoodsVo = null;
                if (!StringUtil.isEmpty(itemParam)) {
                    // 올가가 아닌 품목코드가 넘어온경우
                    itemGoodsVo = goodsGoodsBiz.getDetailSelectGoodsForItem(itemParam);
                } else if (!StringUtil.isEmpty(barCodeParam)) {
                    // 올가인경우 바코드로 넘어온경우
                    itemGoodsVo = goodsGoodsBiz.getDetailSelectGoodsForBarCode(barCodeParam);
                } else {
                    return;
                }
                if (itemGoodsVo == null) return;
                response.sendRedirect("/shop/goodsView?goods=" + itemGoodsVo.getIlGoodsId());
                break;
            case "/events":
                String eventTypeParam = request.getParameter("type");
                if (StringUtil.isEmpty(eventTypeParam)) return;
                if (eventTypeParam.equals("promotions")) {
                    ExhibitListByUserRequestDto exhibitRequestDto = new ExhibitListByUserRequestDto();
                    exhibitRequestDto.setPage(1);
                    exhibitRequestDto.setLimit(10);
                    ExhibitListByUserResponseDto exhibitResponseDto = promotionExhibitBiz.getExhibitListByUser(exhibitRequestDto);
                    if (exhibitResponseDto == null) return;
                    String exhibitName = exhibitResponseDto.getExhibit().stream().map(ExhibitListByUserVo::getTitle).collect(Collectors.joining(", "));
                    setMetaAttr(modelAndView,
                            UserEnums.MetaType.EVENTS.getTitle(),
                            UserEnums.MetaType.EVENTS.getKeyword().replace("{text}", exhibitName),
                            UserEnums.MetaType.EVENTS.getDescription());
                } else if (eventTypeParam.equals("events")) {
                    EventListByUserRequestDto eventRequestDto = new EventListByUserRequestDto();
                    eventRequestDto.setPage(1);
                    eventRequestDto.setLimit(10);
                    EventListByUserResponseDto eventResponseDto = promotionEventBiz.getEventListByUser(eventRequestDto);
                    if (eventResponseDto == null) return;
                    String eventName = eventResponseDto.getEvent().stream().map(EventListByUserVo::getTitle).collect(Collectors.joining(", "));
                    setMetaAttr(modelAndView,
                            UserEnums.MetaType.EVENTS.getTitle(),
                            UserEnums.MetaType.EVENTS.getKeyword().replace("{text}", eventName),
                            UserEnums.MetaType.EVENTS.getDescription());
                }
                break;
            case "/events/promotion":   //기획전 상세
                String exhibitParam = request.getParameter("event");
                if (StringUtil.isEmpty(exhibitParam)) return;
                ExhibitInfoFromMetaVo exhibitVo = promotionExhibitBiz.getExhibitFromMeta(Long.valueOf(exhibitParam));
                if (exhibitVo == null) return;
                setMetaAttr(modelAndView,
                        UserEnums.MetaType.EVENTS_EXHIBIT.getTitle().replace("{text}", exhibitVo.getTitle()),
                        exhibitVo.getTitle(),
                        UserEnums.MetaType.EVENTS_EXHIBIT.getDescription().replace("{text}", exhibitVo.getDateInfo()),
                        exhibitVo.getBannerImagePath()
                );
                break;
            case "/events/reply":
            case "/events/roulette":
            case "/events/stamp":
            case "/events/survey":
            case "/events/experience":  //이벤트 상세
                String eventParam = request.getParameter("event");
                if (StringUtil.isEmpty(eventParam)) return;
                EventInfoFromMetaVo eventInfo = promotionEventBiz.getEventInfoFromMeta(Long.valueOf(eventParam), deviceType);
                if (eventInfo == null) return;
                setMetaAttr(modelAndView,
                        UserEnums.MetaType.EVENTS_EVENT.getTitle().replace("{text}", eventInfo.getTitle()),
                        eventInfo.getTitle(),
                        UserEnums.MetaType.EVENTS_EVENT.getDescription().replace("{text}", eventInfo.getDateInfo()),
                        eventInfo.getBannerImagePath()
                );
                break;
            case "/user/joinInput":
                setMetaAttr(modelAndView, UserEnums.MetaType.USER_JOIN.getTitle(), UserEnums.MetaType.USER_JOIN.getKeyword(), UserEnums.MetaType.USER_JOIN.getDescription());
                break;
            //3.통합몰 코너
            case "/lohas":
                List<GetCategoryListResultVo> categoryList = goodsCategoryBiz.getCategoryList(GoodsEnums.MallDiv.PULMUONE.getCode());
                if (categoryList == null || categoryList.size() == 0) return;
                String lohasCategory = categoryList.stream()
                        .filter(vo -> vo.getIlCategoryId() == GoodsConstants.LOHAS_IL_CTGRY_ID) //로하스 대카 선택
                        .map(GetCategoryListResultVo::getSubCategoryList)   // 로하스 중카 추출
                        .map(vo -> vo.stream().map(GetSubCategoryListResultVo::getCategoryName).collect(Collectors.joining(", ")))  // 로하스 중카 이름 추출
                        .collect(Collectors.joining());
                setMetaAttr(modelAndView,
                        UserEnums.MetaType.LOHAS.getTitle(),
                        UserEnums.MetaType.LOHAS.getKeyword().replace("{text}", lohasCategory),
                        UserEnums.MetaType.LOHAS.getDescription().replace("{text}", lohasCategory));
                break;
            case "/bestItem":
                List<GetCategoryListResultVo> pulmuoneCategoryList = goodsCategoryBiz.getCategoryList(GoodsEnums.MallDiv.PULMUONE.getCode());
                if (pulmuoneCategoryList == null || pulmuoneCategoryList.size() == 0) return;
                String largeCategoryName_1 = pulmuoneCategoryList.stream()
                        .map(GetCategoryListResultVo::getCategoryName)   // 대카 이름 추출
                        .collect(Collectors.joining(" BEST, "));
                String largeCategoryName_2 = pulmuoneCategoryList.stream()
                        .map(GetCategoryListResultVo::getCategoryName)   // 대카 이름 추출
                        .collect(Collectors.joining(" 인기상품, "));
                setMetaAttr(modelAndView, UserEnums.MetaType.BEST.getTitle(),
                        UserEnums.MetaType.BEST.getKeyword().replace("{text}", largeCategoryName_1 + largeCategoryName_2.substring(0, largeCategoryName_2.lastIndexOf(","))),
                        UserEnums.MetaType.BEST.getDescription());
                break;
            case "/keywordItem":
                List<ContentsDetailVo> keywordContents = displayContentsBiz.getContentsLevel1ByInventoryCd(DisplayConstants.KEYWORDS_GOODS, deviceType);
                if (keywordContents == null || keywordContents.size() == 0) return;
                String keywordTitle_1 = keywordContents.stream()
                        .map(ContentsDetailVo::getTitleName)    //title 추출
                        .collect(Collectors.joining(" BEST, "));
                String keywordTitle_2 = keywordContents.stream()
                        .map(ContentsDetailVo::getTitleName)    //title 추출
                        .collect(Collectors.joining(", "));
                setMetaAttr(modelAndView,
                        UserEnums.MetaType.KEYWORD.getTitle(),
                        UserEnums.MetaType.KEYWORD.getKeyword().replace("{text}", keywordTitle_1.substring(0, keywordTitle_1.lastIndexOf(","))),
                        UserEnums.MetaType.KEYWORD.getDescription().replace("{text}", keywordTitle_2));
                break;
            case "/newItem":
                setMetaAttr(modelAndView, UserEnums.MetaType.NEW.getTitle(), UserEnums.MetaType.NEW.getKeyword(), UserEnums.MetaType.NEW.getDescription());
                break;
            case "/nowSale":
                setMetaAttr(modelAndView, UserEnums.MetaType.SALE.getTitle(), UserEnums.MetaType.SALE.getKeyword(), UserEnums.MetaType.SALE.getDescription());
                break;
            case "/pickingOut":
                ExhibitListByUserRequestDto exhibitListByUserRequestDto = new ExhibitListByUserRequestDto();
                exhibitListByUserRequestDto.setPage(1);
                exhibitListByUserRequestDto.setLimit(20);
                exhibitListByUserRequestDto.setMallDiv(GoodsEnums.MallDiv.PULMUONE.getCode());
                SelectListByUserResponseDto selectListByUserResponseDto = promotionExhibitBiz.getSelectListByUser(exhibitListByUserRequestDto);
                if (selectListByUserResponseDto == null) return;
                String selectAllTitle = selectListByUserResponseDto.getSelect().stream().map(SelectListByUserVo::getTitle)
                        .collect(Collectors.joining(", "));
                setMetaAttr(modelAndView,
                        UserEnums.MetaType.PICKING.getTitle(),
                        UserEnums.MetaType.PICKING.getKeyword().replace("{text}", selectAllTitle),
                        UserEnums.MetaType.PICKING.getDescription());
                break;
            case "/pickingOutView":
                String selectParam = request.getParameter("select");
                if (StringUtil.isEmpty(selectParam)) return;
                SelectExhibitResponseDto selectExhibit = promotionExhibitBiz.getSelectExhibit(Long.valueOf(selectParam));
                if (selectExhibit == null) return;
                String selectTitle = selectExhibit.getTitle();
                setMetaAttr(modelAndView,
                        UserEnums.MetaType.PICKING_VIEW.getTitle().replace("{text}", selectTitle),
                        UserEnums.MetaType.PICKING_VIEW.getKeyword().replace("{text}", selectTitle),
                        UserEnums.MetaType.PICKING_VIEW.getDescription());
                break;
            case "/freeOrder":
                setMetaAttr(modelAndView, UserEnums.MetaType.FREE_ORDER.getTitle(), UserEnums.MetaType.FREE_ORDER.getKeyword(), UserEnums.MetaType.FREE_ORDER.getDescription());
                break;
            case "/dailyShipping":
                setMetaAttr(modelAndView, UserEnums.MetaType.DAILY_SHIPPING.getTitle(), UserEnums.MetaType.DAILY_SHIPPING.getKeyword(), UserEnums.MetaType.DAILY_SHIPPING.getDescription());
                break;
            case "/shippingGuide":
                setMetaAttr(modelAndView, UserEnums.MetaType.SHIPPING_GUIDE.getTitle(), UserEnums.MetaType.SHIPPING_GUIDE.getKeyword(), UserEnums.MetaType.SHIPPING_GUIDE.getDescription());
                break;
            case "/brand":
                String brandId = request.getParameter("brandId");
                String dpBrandTitle;
                if (StringUtil.isEmpty(brandId)) {
                    dpBrandTitle = goodsBrandBiz.getDpBrandList().stream()
                            .map(DpBrandListResultVo::getBrandName)
                            .collect(Collectors.joining(","));
                } else {
                    dpBrandTitle = goodsBrandBiz.getDpBrandTitleById(Long.valueOf(brandId));
                    if (dpBrandTitle == null) return;
                }
                setMetaAttr(modelAndView,
                        UserEnums.MetaType.BRAND.getTitle(),
                        UserEnums.MetaType.BRAND.getKeyword().replace("{text}", dpBrandTitle),
                        UserEnums.MetaType.BRAND.getDescription().replace("{text}", dpBrandTitle));
                break;
            case "/newMemberBenifit":
                setMetaAttr(modelAndView, UserEnums.MetaType.NEW_MEMBER_BENEFIT.getTitle(), UserEnums.MetaType.NEW_MEMBER_BENEFIT.getKeyword(), UserEnums.MetaType.NEW_MEMBER_BENEFIT.getDescription());
                break;
            //4.올가 몰인몰 코너
            case "/orga/bestItem":
                List<GetCategoryListResultVo> orgaCategoryList = goodsCategoryBiz.getCategoryList(GoodsEnums.MallDiv.ORGA.getCode());
                if (orgaCategoryList == null || orgaCategoryList.size() == 0) return;
                String orgaLargeCategoryName_1 = orgaCategoryList.stream()
                        .map(GetCategoryListResultVo::getCategoryName)   // 올가 대카 이름 추출
                        .collect(Collectors.joining(" BEST, "));
                String orgaLargeCategoryName_2 = orgaCategoryList.stream()
                        .map(GetCategoryListResultVo::getCategoryName)   // 올가 대카 이름 추출
                        .collect(Collectors.joining(" 인기상품, "));
                setMetaAttr(modelAndView, UserEnums.MetaType.HOT_ITEM.getTitle(),
                        UserEnums.MetaType.HOT_ITEM.getKeyword().replace("{text}", orgaLargeCategoryName_1 + orgaLargeCategoryName_2.substring(0, orgaLargeCategoryName_2.lastIndexOf(","))),
                        UserEnums.MetaType.HOT_ITEM.getDescription());
                break;
            case "/orga/flyerItem":
                setMetaAttr(modelAndView, UserEnums.MetaType.FLYER_ITEM.getTitle(), UserEnums.MetaType.FLYER_ITEM.getKeyword(), UserEnums.MetaType.FLYER_ITEM.getDescription());
                break;
            case "/orga/pbItem":
                setMetaAttr(modelAndView, UserEnums.MetaType.ORGA_PB.getTitle(), UserEnums.MetaType.ORGA_PB.getKeyword(), UserEnums.MetaType.ORGA_PB.getDescription());
                break;
            case "/orga/localItem":
                setMetaAttr(modelAndView, UserEnums.MetaType.LOCAL_ITEM.getTitle(), UserEnums.MetaType.LOCAL_ITEM.getKeyword(), UserEnums.MetaType.LOCAL_ITEM.getDescription());
                break;
            case "/orga/shopDelivery":
                setMetaAttr(modelAndView, UserEnums.MetaType.ORGA_SHOP.getTitle(), UserEnums.MetaType.ORGA_SHOP.getKeyword(), UserEnums.MetaType.ORGA_SHOP.getDescription());
                break;
            case "/orga/newItem":
                setMetaAttr(modelAndView, UserEnums.MetaType.ORGA_NEW.getTitle(), UserEnums.MetaType.ORGA_NEW.getKeyword(), UserEnums.MetaType.ORGA_NEW.getDescription());
                break;
        }
    }

    private void setMetaAttr(ModelAndView modelAndView, String title, String keyword, String description) {
        modelAndView.addObject("metaTitle", title);
        modelAndView.addObject("metaKeyword", keyword);
        modelAndView.addObject("metaDescription", description);
    }

    private void setMetaAttr(ModelAndView modelAndView, String title, String keyword, String description, String imagePath) {
        modelAndView.addObject("metaTitle", title);
        modelAndView.addObject("metaKeyword", keyword);
        modelAndView.addObject("metaDescription", description);
        modelAndView.addObject("metaImage", this.publicStorageUrl + imagePath);
    }

}
