package kr.co.pulmuone.v1.scenario;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.excel.factory.OrderExcelUploadFactory;
import kr.co.pulmuone.v1.comm.mapper.order.claim.ClaimProcessMapper;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDailyCycleDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.PackageGoodsListDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.stock.dto.StockOrderRequestDto;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockOrderBiz;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.service.ClaimCompleteProcessBiz;
import kr.co.pulmuone.v1.order.claim.service.ClaimRequestProcessBiz;
import kr.co.pulmuone.v1.order.create.dto.OrderCreateDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCreateRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderExcelResponseDto;
import kr.co.pulmuone.v1.order.create.dto.OrderExeclDto;
import kr.co.pulmuone.v1.order.create.service.OrderCreateBiz;
import kr.co.pulmuone.v1.order.order.dto.*;
import kr.co.pulmuone.v1.order.order.dto.mall.*;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlDiscountVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlShippingZoneVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingPriceVo;
import kr.co.pulmuone.v1.order.order.service.MallOrderDetailBiz;
import kr.co.pulmuone.v1.order.order.service.OrderDetailBiz;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.order.service.OrderProcessBiz;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindOrderDetlDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindShippingPriceDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderRegistrationResponseDto;
import kr.co.pulmuone.v1.order.registration.service.OrderBindBiz;
import kr.co.pulmuone.v1.order.registration.service.OrderBindCollectionMallCreateBizImpl;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationBiz;
import kr.co.pulmuone.v1.order.regular.dto.*;
import kr.co.pulmuone.v1.order.regular.service.OrderRegularBiz;
import kr.co.pulmuone.v1.order.regular.service.OrderRegularOrderCreateBiz;
import kr.co.pulmuone.v1.order.regular.service.OrderRegularService;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusUpdateRequestDto;
import kr.co.pulmuone.v1.order.status.service.OrderStatusBiz;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.point.service.PromotionPointBiz;
import kr.co.pulmuone.v1.shopping.cart.dto.*;
import kr.co.pulmuone.v1.shopping.cart.service.ShoppingCartBiz;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseModifyDto;
import kr.co.pulmuone.v1.user.warehouse.service.WarehouseBiz;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class OrderTest extends CommonServiceTestBaseForJunit5 {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    private ShoppingCartBiz shoppingCartBiz;

    @Autowired
    private GoodsGoodsBiz goodsGoodsBiz;

    @Autowired
    private UserCertificationBiz userCertificationBiz;

    @Autowired
    private PromotionCouponBiz promotionCouponBiz;

    @Autowired
    private PromotionPointBiz promotionPointBiz;

    @Autowired
    private OrderOrderBiz orderOrderBiz;

    @Autowired
    private OrderProcessBiz orderProcessBiz;

    @Autowired
    private OrderBindBiz<CreateOrderCartDto> orderBindBiz;

    @Autowired
    private OrderBindBiz<RegularResultCreateOrderGoodsListDto> orderBindRegularBiz;

    @Autowired
    private OrderStatusBiz orderStatusBiz;

    @Autowired
    private OrderRegistrationBiz orderRegistrationBiz;

    @Autowired
    private OrderExcelUploadFactory orderExcelUploadFactory;

    @Autowired
    private OrderCreateBiz orderCreateBiz;

    @Autowired
    private OrderDetailBiz orderDetailBiz;

    @Autowired
    private ClaimRequestProcessBiz claimRequestProcessBiz;

    @Autowired
    private ClaimCompleteProcessBiz claimCompleteProcessBiz;

    @Autowired
    private MallOrderDetailBiz mallOrderDetailBiz;

    @Autowired
    private WarehouseBiz warehouseBiz;

    @Autowired
    private OrderBindCollectionMallCreateBizImpl collectionMallOrderBindBiz;

    @Autowired
    private GoodsStockOrderBiz goodsStockOrderBiz;

    @Autowired
    private ClaimProcessMapper claimProcessMapper;

    @Autowired
    private OrderRegularOrderCreateBiz orderRegularOrderCreateBiz;

    @Autowired
    private OrderRegularBiz orderRegularBiz;

    @Autowired
    private OrderRegularService orderRegularService;

    private ApplyPaymentRequestDto gReqDto;
    private static final String PCID_ID = "test";
    private Long gGoodsCouponIssueId1;
    private Long gGoodsCouponIssueId2;
    private Long gShippingCouponIssueId;
    private Long gCartCouponIssueId;
    private List<Long> gGoodsIdList;
    private CartSummaryDto gCartSummaryDto;
    private Long gUrWareHouseId;

    // 엑셀
    private List<OrderExeclDto> gAdminExcelList;
    // 외부몰
    private List<OutMallOrderDto> gOutMallList;

    // 클레임 테스트 케이스용 유형
    @Getter
    @RequiredArgsConstructor
    public enum ClaimTestType implements CodeCommEnum {
        ALL("ALL", "전체 취소"),
        PART("PART", "부분취소"),
        ;

        private final String code;
        private final String codeName;
    }

    @BeforeEach
    void setUp() {
        gReqDto = new ApplyPaymentRequestDto();
        gGoodsIdList = new ArrayList<>();
        gCartSummaryDto = new CartSummaryDto();

        gAdminExcelList = new ArrayList<>();
        gOutMallList = new ArrayList<>();
    }

    private void mallNormalLogin() {
        if ("prod".equals(activeProfile)) {
            // TODO
            BuyerVo buyerVo = new BuyerVo();
            SessionUtil.setUserVO(buyerVo);
        } else {
            BuyerVo buyerVo = new BuyerVo();
            buyerVo.setSnsAuthorizationState("");
            buyerVo.setUrUserId("1647314");
            buyerVo.setUrErpEmployeeCode("");
            buyerVo.setSnsProvider("");
            buyerVo.setSnsSocialId("");
            buyerVo.setPersonalCertificationUserName("테스터");
            buyerVo.setPersonalCertificationMobile("01072721234");
            buyerVo.setPersonalCertificationGender("M");
            buyerVo.setPersonalCertificationCiCd("123qweasd");
            buyerVo.setPersonalCertificationBirthday("19771215");

            buyerVo.setReceiverName("홍길동");
            buyerVo.setReceiverMobile("0111234567");
            buyerVo.setReceiverZipCode("06362");
            buyerVo.setBuildingCode("1168011500107490000001720");
            buyerVo.setReceiverAddress1("서울 강남구 광평로 144");
            buyerVo.setReceiverAddress2("도서지역");

            buyerVo.setUrGroupId(35L);
            buyerVo.setUserName("test");
            buyerVo.setUserMobile("0111234567");
            buyerVo.setUserEmail("test@test.co.kr");
            SessionUtil.setUserVO(buyerVo);
        }
    }

    private void mallEmployeeLogin() {
        if ("prod".equals(activeProfile)) {
            // TODO
            BuyerVo buyerVo = new BuyerVo();
            SessionUtil.setUserVO(buyerVo);
        } else {
            BuyerVo buyerVo = new BuyerVo();
            buyerVo.setSnsAuthorizationState("");
            buyerVo.setUrUserId("1647344");
            buyerVo.setUrErpEmployeeCode("forbiz05");
            buyerVo.setSnsProvider("");
            buyerVo.setSnsSocialId("");
            buyerVo.setPersonalCertificationUserName("테스터");
            buyerVo.setPersonalCertificationMobile("01072721234");
            buyerVo.setPersonalCertificationGender("M");
            buyerVo.setPersonalCertificationCiCd("123qweasd");
            buyerVo.setPersonalCertificationBirthday("19771215");

            buyerVo.setReceiverName("홍길동");
            buyerVo.setReceiverMobile("0111234567");
            buyerVo.setReceiverZipCode("06362");
            buyerVo.setBuildingCode("1168011500107490000001720");
            buyerVo.setReceiverAddress1("서울 강남구 광평로 144");
            buyerVo.setReceiverAddress2("도서지역");

            buyerVo.setUrGroupId(35L);
            buyerVo.setUserName("test");
            buyerVo.setUserMobile("0111234567");
            buyerVo.setUserEmail("test@test.co.kr");
            SessionUtil.setUserVO(buyerVo);
        }
    }

    private void bosLogin() {
        if ("prod".equals(activeProfile)) {
            // TODO
            UserVo userVO = new UserVo();
            SessionUtil.setUserVO(userVO);
        } else {
            UserVo userVO = new UserVo();
            userVO.setUserId("1");
            userVO.setLoginId("forbiz");
            userVO.setLoginName("포비즈");
            userVO.setUserType(null);
            userVO.setStatusType("EMPLOYEE_STATUS.NORMAL");
            userVO.setCompanyName("(주)풀무원");
            userVO.setRoleId(Constants.ADMIN_LEVEL_1_AUTH_ST_ROLE_TP_ID);
            userVO.setPasswordChangeYn("N");
            userVO.setLastLoginElapsedDay(0);
            userVO.setConnectionId(53308);
            userVO.setTemporaryYn("N");
            userVO.setPersonalInformationAccessYn("N");
            userVO.setLangCode("1");
            userVO.setListAuthSupplierId(new ArrayList<>());
            userVO.setListAuthWarehouseId(new ArrayList<>());
            userVO.setListAuthStoreId(new ArrayList<>());
            userVO.setListAuthSellersId(new ArrayList<>());
            SessionUtil.setUserVO(userVO);
        }
    }

    private void given_normal1() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            gGoodsIdList.add(90018104L);    //(일반+추가) 쿠폰불가	일반상품
            gGoodsIdList.add(90018255L);    //(일반)	일반상품
            gGoodsIdList.add(90018237L);    //(개별배송비)	일반상품
            gGoodsIdList.add(90018252L);    //(일반+일반) 	묶음상품

            // 테스트 데이터
            gReqDto.setCartType(ShoppingEnums.CartType.NORMAL.getCode());
            gReqDto.setUsePoint(24363);
            gGoodsCouponIssueId1 = 459382L;   // 상품 정률
            gGoodsCouponIssueId2 = 459452L;   // 상품 정액
            gShippingCouponIssueId = 459662L;  // 배송비쿠폰 정액
            gCartCouponIssueId = 459522L;    // 장바구니 정률

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(33660);
            gCartSummaryDto.setGoodsSalePrice(33660);
            gCartSummaryDto.setGoodsDiscountPrice(9297);
            gCartSummaryDto.setGoodsPaymentPrice(24363);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(0);
            gCartSummaryDto.setTaxFreePaymentPrice(0);
            gCartSummaryDto.setPaymentPrice(0);
            gCartSummaryDto.setUsePoint(gReqDto.getUsePoint());
        }
    }

    private void given_normal2() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            gGoodsIdList.add(90018104L);    //(일반+추가) 쿠폰불가	일반상품
            gGoodsIdList.add(90018255L);    //(일반)	일반상품
            gGoodsIdList.add(90018237L);    //(개별배송비)	일반상품
            gGoodsIdList.add(90018252L);    //(일반+일반) 	묶음상품
            gGoodsIdList.add(90018267L);    //(일반+증정)	묶음상품
            gGoodsIdList.add(90018265L);    //(일반+식품증정)	묶음상품
            gGoodsIdList.add(90018335L);    //(일반+증정+식마)	묶음상품
//            gGoodsIdList.add(90018224L);    //(일반 + 매장) 합배송 일반/매장판매   //품절상태 변경
            gGoodsIdList.add(90018212L);    //(일반 + 매장) 합배송 일반/매장판매

            // 테스트 데이터
            gReqDto.setCartType(ShoppingEnums.CartType.NORMAL.getCode());
            gReqDto.setUsePoint(10000);
            gGoodsCouponIssueId1 = 459382L;   // 상품 정률
            gGoodsCouponIssueId2 = 459452L;   // 상품 정액
            gShippingCouponIssueId = null;  // 배송비쿠폰 정액
            gCartCouponIssueId = 459592L;    // 장바구니 정액

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(196220);
            gCartSummaryDto.setGoodsSalePrice(157320);
            gCartSummaryDto.setGoodsDiscountPrice(48490);
            gCartSummaryDto.setGoodsPaymentPrice(147730);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(123617);
            gCartSummaryDto.setTaxFreePaymentPrice(14113);
            gCartSummaryDto.setPaymentPrice(137730);
            gCartSummaryDto.setUsePoint(gReqDto.getUsePoint());
        }
    }

    private void given_normal3() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            gGoodsIdList.add(90018104L);    //(일반+추가) 쿠폰불가	일반상품
            gGoodsIdList.add(90018255L);    //(일반)	일반상품
//            gGoodsIsList.add(900135L);      //(일반+추가) 예약판매	일반상품
            gGoodsIdList.add(90018237L);    //(개별배송비)	일반상품
            gGoodsIdList.add(90018252L);    //(일반+일반) 	묶음상품
            gGoodsIdList.add(90018267L);    //(일반+증정)	묶음상품
            gGoodsIdList.add(90018265L);    //(일반+식품증정)	묶음상품
            gGoodsIdList.add(90018335L);    //(일반+증정+식마)	묶음상품
//            gGoodsIsList.add(90018277L);    //(36개월 무료배송)	렌탈상품
            gGoodsIdList.add(90018224L);    //(일반 + 매장) 합배송 일반/매장판매
            gGoodsIdList.add(90018212L);    //(일반 + 매장) 합배송 일반/매장판매
//            gGoodsIsList.add(90018168L);    //(일반 + 매장) 개별배송 일반/매장판매
//            gGoodsIsList.add(90018245L);    //(풀무원녹즙) 일일상품
//            gGoodsIsList.add(900095L);      //(잇슬림) 일일상품
//            gGoodsIsList.add(900230L);      //(베이비밀) 일일상품

            // 테스트 데이터
            gReqDto.setCartType(ShoppingEnums.CartType.NORMAL.getCode());
            gReqDto.setUsePoint(62025);
            gGoodsCouponIssueId1 = null;
            gCartCouponIssueId = null;    // 장바구니 정률
            gUrWareHouseId = 85L;

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(103410);
            gCartSummaryDto.setGoodsSalePrice(62025);
            gCartSummaryDto.setGoodsDiscountPrice(41385);
            gCartSummaryDto.setGoodsPaymentPrice(62025);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(0);
            gCartSummaryDto.setTaxFreePaymentPrice(5);
            gCartSummaryDto.setPaymentPrice(0);
            gCartSummaryDto.setUsePoint(gReqDto.getUsePoint());
        }
    }

    private void given_normal4() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            gGoodsIdList.add(90018104L);    //(일반+추가) 쿠폰불가	일반상품
            gGoodsIdList.add(90018255L);    //(일반)	일반상품
//            gGoodsIsList.add(900135L);      //(일반+추가) 예약판매	일반상품
            gGoodsIdList.add(90018237L);    //(개별배송비)	일반상품
            gGoodsIdList.add(90018252L);    //(일반+일반) 	묶음상품
            gGoodsIdList.add(90018267L);    //(일반+증정)	묶음상품
            gGoodsIdList.add(90018265L);    //(일반+식품증정)	묶음상품
            gGoodsIdList.add(90018335L);    //(일반+증정+식마)	묶음상품
//            gGoodsIsList.add(90018277L);    //(36개월 무료배송)	렌탈상품
//            gGoodsIdList.add(90018224L);    //(일반 + 매장) 합배송 일반/매장판매   //품절
//            gGoodsIdList.add(90018212L);    //(일반 + 매장) 합배송 일반/매장판매
//            gGoodsIsList.add(90018168L);    //(일반 + 매장) 개별배송 일반/매장판매
//            gGoodsIsList.add(90018245L);    //(풀무원녹즙) 일일상품
//            gGoodsIsList.add(900095L);      //(잇슬림) 일일상품
//            gGoodsIsList.add(900230L);      //(베이비밀) 일일상품

            // 테스트 데이터
            gReqDto.setCartType(ShoppingEnums.CartType.NORMAL.getCode());
            gReqDto.setUsePoint(10000);
            gGoodsCouponIssueId1 = null;
            gCartCouponIssueId = null;    // 장바구니 정률
            gUrWareHouseId = 85L;

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(180220);
            gCartSummaryDto.setGoodsSalePrice(110220);
            gCartSummaryDto.setGoodsDiscountPrice(70000);
            gCartSummaryDto.setGoodsPaymentPrice(110220);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(100230);
            gCartSummaryDto.setTaxFreePaymentPrice(0);
            gCartSummaryDto.setPaymentPrice(100220);
            gCartSummaryDto.setUsePoint(gReqDto.getUsePoint());
        }
    }

    private void given_normal5() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            gGoodsIdList.add(90018104L);    //(일반+추가) 쿠폰불가	일반상품
            gGoodsIdList.add(90018255L);    //(일반)	일반상품
            gGoodsIdList.add(90018237L);    //(개별배송비)	일반상품
            gGoodsIdList.add(90018252L);    //(일반+일반) 	묶음상품
//            gGoodsIdList.add(90018267L);    //(일반+증정)	묶음상품
//            gGoodsIdList.add(90018265L);    //(일반+식품증정)	묶음상품
//            gGoodsIdList.add(90018335L);    //(일반+증정+식마)	묶음상품
//            gGoodsIdList.add(90018224L);    //(일반 + 매장) 합배송 일반/매장판매
//            gGoodsIdList.add(90018212L);    //(일반 + 매장) 합배송 일반/매장판매

            // 테스트 데이터
            gReqDto.setCartType(ShoppingEnums.CartType.NORMAL.getCode());
            gReqDto.setUsePoint(24363);
            gGoodsCouponIssueId1 = 459382L;   // 상품 정률
            gGoodsCouponIssueId2 = 459452L;   // 상품 정액
            gShippingCouponIssueId = 459662L;  // 배송비쿠폰 정액
            gCartCouponIssueId = 459522L;    // 장바구니 정률
            gUrWareHouseId = 85L;

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(33660);
            gCartSummaryDto.setGoodsSalePrice(33660);
            gCartSummaryDto.setGoodsDiscountPrice(9297);
            gCartSummaryDto.setGoodsPaymentPrice(24363);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(0);
            gCartSummaryDto.setTaxFreePaymentPrice(0);
            gCartSummaryDto.setPaymentPrice(0);
            gCartSummaryDto.setUsePoint(gReqDto.getUsePoint());
        }
    }

    private void given_normal6() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
//            gGoodsIdList.add(90018104L);    //(일반+추가) 쿠폰불가	일반상품    //재고무제한 아님 - 한 Transaction 안에서 재배송 불가
            gGoodsIdList.add(90018255L);    //(일반)	일반상품
            gGoodsIdList.add(90018237L);    //(개별배송비)	일반상품
            gGoodsIdList.add(90018252L);    //(일반+일반) 	묶음상품
//            gGoodsIdList.add(90018267L);    //(일반+증정)	묶음상품
//            gGoodsIdList.add(90018265L);    //(일반+식품증정)	묶음상품
//            gGoodsIdList.add(90018335L);    //(일반+증정+식마)	묶음상품        //재고무제한 아님 - 한 Transaction 안에서 재배송 불가
            gGoodsIdList.add(90018212L);    //(일반 + 매장) 합배송 일반/매장판매

            // 테스트 데이터
            gReqDto.setCartType(ShoppingEnums.CartType.NORMAL.getCode());
            gReqDto.setUsePoint(10000);
            gGoodsCouponIssueId1 = null;   // 상품 정률
            gCartCouponIssueId = null;    // 장바구니 정률
            gUrWareHouseId = 85L;

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(51520);
            gCartSummaryDto.setGoodsSalePrice(30880);
            gCartSummaryDto.setGoodsDiscountPrice(20640);
            gCartSummaryDto.setGoodsPaymentPrice(30880);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(11280);
            gCartSummaryDto.setTaxFreePaymentPrice(9600);
            gCartSummaryDto.setPaymentPrice(20880);
            gCartSummaryDto.setUsePoint(gReqDto.getUsePoint());
        }
    }

    private void given_shop1() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            gGoodsIdList.add(90018224L);    //(일반 + 매장) 합배송 일반/매장판매
            gGoodsIdList.add(90018212L);    //(일반 + 매장) 합배송 일반/매장판매

            // 테스트 데이터
            gReqDto.setCartType(ShoppingEnums.CartType.SHOP.getCode());
            gReqDto.setStoreDeliveryType(GoodsEnums.StoreDeliveryType.PICKUP.getCode());
            gReqDto.setStoreArrivalScheduledDate(LocalDate.now().plusDays(1));
            gReqDto.setUrStoreScheduleId(97L);
            gReqDto.setUsePoint(12500);
            ChangeArrivalScheduledDto changeArrivalScheduledDto = new ChangeArrivalScheduledDto();
            changeArrivalScheduledDto.setArrivalScheduledDate(LocalDate.now());
            changeArrivalScheduledDto.setDawnDeliveryYn("N");
            gReqDto.setArrivalScheduled(Collections.singletonList(changeArrivalScheduledDto));
            gGoodsCouponIssueId1 = null;   // 상품 정률
            gCartCouponIssueId = null;    // 장바구니 정률

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(13300);
            gCartSummaryDto.setGoodsSalePrice(12500);
            gCartSummaryDto.setGoodsDiscountPrice(800);
            gCartSummaryDto.setGoodsPaymentPrice(12500);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(0);
            gCartSummaryDto.setTaxFreePaymentPrice(0);
            gCartSummaryDto.setPaymentPrice(0);
            gCartSummaryDto.setUsePoint(gReqDto.getUsePoint());
        }
    }

    private void given_shop2() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            gGoodsIdList.add(90018224L);    //(일반 + 매장) 합배송 일반/매장판매
            gGoodsIdList.add(90018212L);    //(일반 + 매장) 합배송 일반/매장판매

            // 테스트 데이터
            gReqDto.setCartType(ShoppingEnums.CartType.SHOP.getCode());
            gReqDto.setStoreDeliveryType(GoodsEnums.StoreDeliveryType.DIRECT.getCode());
            gReqDto.setStoreArrivalScheduledDate(LocalDate.now().plusDays(1));
            gReqDto.setUrStoreScheduleId(97L);
            gReqDto.setUsePoint(7980);
            ChangeArrivalScheduledDto changeArrivalScheduledDto = new ChangeArrivalScheduledDto();
            changeArrivalScheduledDto.setArrivalScheduledDate(LocalDate.now());
            changeArrivalScheduledDto.setDawnDeliveryYn("N");
            gReqDto.setArrivalScheduled(Collections.singletonList(changeArrivalScheduledDto));
            gGoodsCouponIssueId1 = null;   // 상품 정률
            gCartCouponIssueId = null;    // 장바구니 정률

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(13300);
            gCartSummaryDto.setGoodsSalePrice(7980);
            gCartSummaryDto.setGoodsDiscountPrice(5320);
            gCartSummaryDto.setGoodsPaymentPrice(7980);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(0);
            gCartSummaryDto.setTaxFreePaymentPrice(0);
            gCartSummaryDto.setPaymentPrice(0);
            gCartSummaryDto.setUsePoint(gReqDto.getUsePoint());
        }
    }

    private void given_rental1() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            gGoodsIdList.add(90018277L);    //(36개월 무료배송)	렌탈상품

            // 테스트 데이터
            gReqDto.setCartType(ShoppingEnums.CartType.RENTAL.getCode());
            gReqDto.setUsePoint(0);
            gGoodsCouponIssueId1 = null;   // 상품 정률
            gCartCouponIssueId = null;    // 장바구니 정률

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(0);
            gCartSummaryDto.setGoodsSalePrice(0);
            gCartSummaryDto.setGoodsDiscountPrice(0);
            gCartSummaryDto.setGoodsPaymentPrice(0);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(0);
            gCartSummaryDto.setTaxFreePaymentPrice(0);
            gCartSummaryDto.setPaymentPrice(0);
            gCartSummaryDto.setUsePoint(gReqDto.getUsePoint());
        }
    }

    private void given_rental2() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            gGoodsIdList.add(90018277L);    //(36개월 무료배송)	렌탈상품

            // 테스트 데이터
            gReqDto.setCartType(ShoppingEnums.CartType.RENTAL.getCode());
            gReqDto.setUsePoint(0);
            gGoodsCouponIssueId1 = null;   // 상품 정률
            gCartCouponIssueId = null;    // 장바구니 정률

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(0);
            gCartSummaryDto.setGoodsSalePrice(0);
            gCartSummaryDto.setGoodsDiscountPrice(0);
            gCartSummaryDto.setGoodsPaymentPrice(0);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(0);
            gCartSummaryDto.setTaxFreePaymentPrice(0);
            gCartSummaryDto.setPaymentPrice(0);
            gCartSummaryDto.setUsePoint(gReqDto.getUsePoint());
        }
    }

    private void given_incorporeity1() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            gGoodsIdList.add(90018320L);    //무형상품

            // 테스트 데이터
            gReqDto.setCartType(ShoppingEnums.CartType.INCORPOREITY.getCode());
            gReqDto.setUsePoint(0);
            gGoodsCouponIssueId1 = null;   // 상품 정률
            gCartCouponIssueId = null;    // 장바구니 정률

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(10000);
            gCartSummaryDto.setGoodsSalePrice(10000);
            gCartSummaryDto.setGoodsDiscountPrice(0);
            gCartSummaryDto.setGoodsPaymentPrice(10000);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(10000);
            gCartSummaryDto.setTaxFreePaymentPrice(0);
            gCartSummaryDto.setPaymentPrice(10000);
            gCartSummaryDto.setUsePoint(gReqDto.getUsePoint());
        }
    }

    private void given_incorporeity2() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            gGoodsIdList.add(90018320L);    //무형상품

            // 테스트 데이터
            gReqDto.setCartType(ShoppingEnums.CartType.INCORPOREITY.getCode());
            gReqDto.setUsePoint(0);
            gGoodsCouponIssueId1 = null;   // 상품 정률
            gCartCouponIssueId = null;    // 장바구니 정률

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(10000);
            gCartSummaryDto.setGoodsSalePrice(6000);
            gCartSummaryDto.setGoodsDiscountPrice(4000);
            gCartSummaryDto.setGoodsPaymentPrice(6000);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(6000);
            gCartSummaryDto.setTaxFreePaymentPrice(0);
            gCartSummaryDto.setPaymentPrice(6000);
            gCartSummaryDto.setUsePoint(gReqDto.getUsePoint());
        }
    }

    private void given_admin1() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            OrderExeclDto excelDto1 = new OrderExeclDto();
            excelDto1.setRecvNm("홍길동");
            excelDto1.setRecvHp("01112345678");
            excelDto1.setRecvZipCd("1827");
            excelDto1.setRecvAddr1("서울시 강남구 양재");
            excelDto1.setRecvAddr2("포비즈코리아 2층");
            excelDto1.setIlGoodsId("90018104");
            excelDto1.setOrderCnt("1");
            excelDto1.setSalePrice("15900");
            gAdminExcelList.add(excelDto1);

            OrderExeclDto excelDto2 = new OrderExeclDto();
            excelDto2.setRecvNm("홍길동");
            excelDto2.setRecvHp("01112345678");
            excelDto2.setRecvZipCd("1827");
            excelDto2.setRecvAddr1("서울시 강남구 양재");
            excelDto2.setRecvAddr2("포비즈코리아 2층");
            excelDto2.setIlGoodsId("90018255");
            excelDto2.setOrderCnt("1");
            excelDto2.setSalePrice("7980");
            gAdminExcelList.add(excelDto2);

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(23880);
            gCartSummaryDto.setGoodsSalePrice(23880);
            gCartSummaryDto.setGoodsDiscountPrice(0);
            gCartSummaryDto.setGoodsPaymentPrice(23880);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(23880);
            gCartSummaryDto.setTaxFreePaymentPrice(0);
            gCartSummaryDto.setPaymentPrice(23880);
        }
    }

    private void given_admin2() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            OrderExeclDto excelDto1 = new OrderExeclDto();
            excelDto1.setRecvNm("홍길동");
            excelDto1.setRecvHp("01112345678");
            excelDto1.setRecvZipCd("1827");
            excelDto1.setRecvAddr1("서울시 강남구 양재");
            excelDto1.setRecvAddr2("포비즈코리아 2층");
            excelDto1.setIlGoodsId("90018104");
            excelDto1.setOrderCnt("1");
            excelDto1.setSalePrice("15900");
            gAdminExcelList.add(excelDto1);

            OrderExeclDto excelDto2 = new OrderExeclDto();
            excelDto2.setRecvNm("홍길동");
            excelDto2.setRecvHp("01112345678");
            excelDto2.setRecvZipCd("1827");
            excelDto2.setRecvAddr1("서울시 강남구 양재");
            excelDto2.setRecvAddr2("포비즈코리아 2층");
            excelDto2.setIlGoodsId("90018255");
            excelDto2.setOrderCnt("1");
            excelDto2.setSalePrice("7980");
            gAdminExcelList.add(excelDto2);

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(23880);
            gCartSummaryDto.setGoodsSalePrice(23880);
            gCartSummaryDto.setGoodsDiscountPrice(0);
            gCartSummaryDto.setGoodsPaymentPrice(23880);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(23880);
            gCartSummaryDto.setTaxFreePaymentPrice(0);
            gCartSummaryDto.setPaymentPrice(23880);
        }
    }

    private void given_outmall() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            gOutMallList.clear();
            OutMallOrderDto dto1 = OutMallOrderDto.builder()
                    .outmallType("S")
                    .ifOutmallExcelInfoId(0)
                    .ifOutmallExcelSuccId("198642")
                    .collectionMallId("2_TEST_S_01")
                    .collectionMallDetailId("2_TEST_S_01")
                    .sellersGroupCd("SELLERS_GROUP.DIRECT_MNG")
                    .omSellersId("1")
                    .ilItemCd("0045296")
                    .ilGoodsId("90018252")
                    .goodsName("사천식마파꾸러기짱")
                    .orderCount("1")
                    .paidPrice("7980")
                    .buyerName("홍길동")
                    .buyerTel("0111234567")
                    .buyerMobile("0111234567")
                    .receiverName("홍길순")
                    .receiverTel("0111234567")
                    .receiverMobile("0111234567")
                    .receiverZipCode("1257")
                    .receiverAddress1("서울시강남구")
                    .receiverAddress2("로즈데일")
                    .shippingPrice("3000")
                    .deliveryMessage("없음")
                    .outMallId("3_TEST_S_01")
                    .success(false)
                    .urSupplierId("1")
                    .urWarehouseId("85")
                    .grpShippingZone("홍길순∀0111234567∀1257∀서울시강남구∀로즈데일∀198642")
                    .grpShippingPrice("SPG_2_TEST_S_01∀85∀Y∀0")
                    .goodsTp("GOODS_TYPE.PACKAGE")
                    .warehouseGrpCd("WAREHOUSE_GROUP.OWN")
                    .storageMethodTp("ERP_STORAGE_TYPE.COOL")
                    .itemBarcode("8801114146750")
                    .taxYn("Y")
                    .ilCtgryStdId("1061834")
                    .ilCtgryDisplayId("4942")
                    .ilCtgryMallId("0")
                    .saleTp("SALE_TYPE.NORMAL")
                    .ilShippingTmplId("589")
                    .standardPrice(2130)
                    .recommendedPrice(5800)
                    .salePrice(5800)
                    .memo("")
                    .build();
            gOutMallList.add(dto1);
            gUrWareHouseId = 85L;

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(5800);
            gCartSummaryDto.setGoodsSalePrice(7980);
            gCartSummaryDto.setGoodsDiscountPrice(0);
            gCartSummaryDto.setGoodsPaymentPrice(7980);
            gCartSummaryDto.setShippingRecommendedPrice(3000);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(3000);
            gCartSummaryDto.setTaxPaymentPrice(10980);
            gCartSummaryDto.setTaxFreePaymentPrice(0);
            gCartSummaryDto.setPaymentPrice(7980);
        }
    }

    private void given_ezAdmin() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            gOutMallList.clear();
            OutMallOrderDto dto1 = OutMallOrderDto.builder()
                    .outmallType("E")
                    .ifOutmallExcelInfoId(0)
                    .ifOutmallExcelSuccId("198641")
                    .collectionMallId("1_TEST_01")
                    .collectionMallDetailId("2_TEST_01")
                    .sellersGroupCd("SELLERS_GROUP.DIRECT_MNG")
                    .omSellersId("1")
                    .ilItemCd("0045296")
                    .ilGoodsId("90018252")
                    .goodsName("사천식마파꾸러기짱")
                    .orderCount("2")
                    .paidPrice("7980")
                    .buyerName("홍길동")
                    .buyerTel("0111234567")
                    .buyerMobile("0111234567")
                    .receiverName("홍길순")
                    .receiverTel("0111234567")
                    .receiverMobile("0111234567")
                    .receiverZipCode("1257")
                    .receiverAddress1("서울시강남구")
                    .receiverAddress2("로즈데일")
                    .shippingPrice("3000")
                    .deliveryMessage("없음")
                    .outMallId("3_TEST_01")
                    .success(false)
                    .urSupplierId("1")
                    .urWarehouseId("85")
                    .grpShippingZone("홍길순∀0111234567∀1257∀서울시강남구∀로즈데일∀198641")
                    .grpShippingPrice("SPG_1_TEST_01∀85∀Y∀0")
                    .goodsTp("GOODS_TYPE.PACKAGE")
                    .warehouseGrpCd("WAREHOUSE_GROUP.OWN")
                    .storageMethodTp("ERP_STORAGE_TYPE.COOL")
                    .itemBarcode("8801114146750")
                    .taxYn("Y")
                    .ilCtgryStdId("1061834")
                    .ilCtgryDisplayId("4942")
                    .ilCtgryMallId("0")
                    .saleTp("SALE_TYPE.NORMAL")
                    .ilShippingTmplId("589")
                    .standardPrice(2130)
                    .recommendedPrice(5800)
                    .salePrice(7980)
                    .memo("")
                    .build();
            gOutMallList.add(dto1);
            gUrWareHouseId = 85L;

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(5800);
            gCartSummaryDto.setGoodsSalePrice(3990);
            gCartSummaryDto.setGoodsDiscountPrice(0);
            gCartSummaryDto.setGoodsPaymentPrice(7980);
            gCartSummaryDto.setShippingRecommendedPrice(3000);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(3000);
            gCartSummaryDto.setTaxPaymentPrice(10980);
            gCartSummaryDto.setTaxFreePaymentPrice(0);
            gCartSummaryDto.setPaymentPrice(7980);
        }
    }

    private void given_regular1() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            gGoodsIdList.add(90018113L);

            // 테스트 데이터
            gReqDto.setCartType(ShoppingEnums.CartType.REGULAR.getCode());
            gReqDto.setUsePoint(0);
            gGoodsCouponIssueId1 = null;   // 상품 정률
            gGoodsCouponIssueId2 = null;   // 상품 정액
            gShippingCouponIssueId = null;  // 배송비쿠폰 정액
            gCartCouponIssueId = null;    // 장바구니 정률

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(2400);
            gCartSummaryDto.setGoodsSalePrice(2400);
            gCartSummaryDto.setGoodsDiscountPrice(120);
            gCartSummaryDto.setGoodsPaymentPrice(2280);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(0);
            gCartSummaryDto.setTaxFreePaymentPrice(2280);
            gCartSummaryDto.setPaymentPrice(2280);
            gCartSummaryDto.setUsePoint(gReqDto.getUsePoint());
        }
    }

    private void given_regular2() {
        // given - 장바구니 등록
        if ("prod".equals(activeProfile)) {
            //TODO
        } else {
            gGoodsIdList.add(90018113L);

            // 테스트 데이터
            gReqDto.setCartType(ShoppingEnums.CartType.REGULAR.getCode());
            gReqDto.setUsePoint(0);
            gGoodsCouponIssueId1 = null;   // 상품 정률
            gGoodsCouponIssueId2 = null;   // 상품 정액
            gShippingCouponIssueId = null;  // 배송비쿠폰 정액
            gCartCouponIssueId = null;    // 장바구니 정률

            // 검증데이터
            gCartSummaryDto.setGoodsRecommendedPrice(2400);
            gCartSummaryDto.setGoodsSalePrice(2400);
            gCartSummaryDto.setGoodsDiscountPrice(120);
            gCartSummaryDto.setGoodsPaymentPrice(2280);
            gCartSummaryDto.setShippingRecommendedPrice(0);
            gCartSummaryDto.setShippingDiscountPrice(0);
            gCartSummaryDto.setShippingPaymentPrice(0);
            gCartSummaryDto.setTaxPaymentPrice(0);
            gCartSummaryDto.setTaxFreePaymentPrice(2280);
            gCartSummaryDto.setPaymentPrice(2280);
            gCartSummaryDto.setUsePoint(gReqDto.getUsePoint());
        }
    }

    @Test
    @DisplayName("장바구니 주문(일반배송-일반회원가-쿠폰(장바구니(정률))-적립금전체사용)후_전체취소")
    void 일반배송1() throws Exception {
        // given - 정보설정
        mallNormalLogin();
        given_normal1();

        // given - 회원정보 조회
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        Long urUserId = Long.parseLong(buyerVo.getUrUserId());

        // given - 장바구니 등록
        List<Long> cartIdList = new ArrayList<>();
        for (Long goodsId : gGoodsIdList) {
            cartIdList.add(this.addCart(urUserId, goodsId, 1));
        }

        // given - 장바구니 데이터 조회
        List<CartDeliveryDto> cartDataDto = this.getCartDataList(cartIdList, buyerVo);
        GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();

        // 매장 정보
        CartStoreDto cartStoreDto = new CartStoreDto();
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            cartStoreDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
            cartStoreDto.setStoreArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
            cartStoreDto.setUrStoreScheduleId(gReqDto.getUrStoreScheduleId());
        }

        // when - 상품 주문가능 상태
        GetCartDataRequestDto cartDataReqDto = new GetCartDataRequestDto();
        cartDataReqDto.setUrUserId(urUserId);
        CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataReqDto, cartDataDto, cartStoreDto);

        // then - 상품 주문가능 상태
        assertNotNull(checkCartResponseDto);
        assertEquals(ShoppingEnums.ApplyPayment.SUCCESS, checkCartResponseDto.getResult());

        // when - 장바구니 집계 조회
        CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, gReqDto.getUsePoint());

        // when - 주문생성
        OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(OrderEnums.PaymentType.FREE.getCode());
        List<OrderBindDto> orderBindList = this.getOrderBindList(buyerVo, paymentType, cartDataDto, sessionShippingDto, cartSummaryDto);

        // then - 가격 검증
        testThenPrice(cartSummaryDto, orderBindList);

        // when - 주문 임시데이터 생성
        OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "N");

        // then - 주문 임시데이터 생성
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

        // given - 주문 승인 - 데이터 보관용
        int availablePointBeforeOrder = promotionPointBiz.getPointUsable(urUserId);
        // cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
        List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);
        // List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
        List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);
        // 상품 쿠폰 리스트 조회
        List<GoodsCouponDto> goodsCouponListBeforeOrder = promotionCouponBiz.getGoodsCouponApplicationListByUser(urUserId, cartGoodsList, GoodsEnums.DeviceType.PC);

        // when - 주문 승인
        String odid = orderRegistrationResponseDto.getOdids().split(",")[0];

        // 주문번호로 주문데이터 조회
        PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

        // 승인처리
        PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setPgAccountType(PgEnums.PgAccountType.INICIS_BASIC);
        paymentData.setTid("123");
        paymentData.setAuthCode("123");
        paymentData.setCardNumber("123");
        paymentData.setCardQuotaInterest("N");
        paymentData.setCardQuota("");
        paymentData.setVirtualAccountNumber("123");
        paymentData.setBankName(PgEnums.InicisBankCode.INICIS_03.getCodeName());
        paymentData.setInfo("test");
        paymentData.setPaidDueDate(LocalDateTime.now());
        paymentData.setPaidHolder("test");
        paymentData.setPartCancelYn("Y");
        paymentData.setEscrowYn("N");
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setCashReceiptYn("N");
        paymentData.setCashReceiptType("");
        paymentData.setCashReceiptNo("");
        paymentData.setCashReceiptAuthNo("");
        paymentData.setResponseData("");
        PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

        // then - 주문 승인
        assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

        // then - 쿠폰 사용 여부
        List<Long> pmCouponIssueIdList = Arrays.asList(gGoodsCouponIssueId1, gCartCouponIssueId);
        for (Long pmCouponIssueId : pmCouponIssueIdList) {
            CouponEnums.UseCouponValidation couponStatus = promotionCouponBiz.checkUseCouponValidation(urUserId, pmCouponIssueId);
            assertEquals(CouponEnums.UseCouponValidation.USE_ISSUE_STATUS, couponStatus);
        }

        // given - 주문 승인 - 주문이후 데이터 추출용
        int availablePointAfterOrder = promotionPointBiz.getPointUsable(urUserId);
        // 상품 쿠폰 리스트 조회
        List<GoodsCouponDto> goodsCouponListAfterOrder = promotionCouponBiz.getGoodsCouponApplicationListByUser(urUserId, cartGoodsList, GoodsEnums.DeviceType.PC);

        // then - 적립금 사용 여부
        assertEquals(gReqDto.getUsePoint(), availablePointBeforeOrder - availablePointAfterOrder);
        assertTrue(availablePointBeforeOrder > availablePointAfterOrder);

        // then - 재고차감 여부
        List<CartDeliveryDto> cartDataDtoAfterOrder = getCartDataList(cartIdList, buyerVo);
        int stockBeforeOrder = cartDataDto.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();

        int stockAfterOrder = cartDataDtoAfterOrder.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();
        assertTrue(stockBeforeOrder > stockAfterOrder);

        // when - 주문정보 조회
        String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];
        ApiResult<?> orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        MallOrderDetailListResponseDto orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        List<MallOrderDetailGoodsDto> orderDetailInfoList = orderInfo.getOrderDetailList().stream()
                .map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList());
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderDetailInfoList.get(0).getOrderStatusCd());

        // then - 주문 결제 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderInfo.getPayInfo().getStatus());

        // when - 주문 전체 취소
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.CANCEL, true);
        mallNormalLogin();

        // then - 주문 전체 취소
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // when - 쿠폰 원복 여부 - 쿠폰은 재발급 수량비교
        // 상품 쿠폰 리스트 조회
        List<GoodsCouponDto> goodsCouponListAfterClaim = promotionCouponBiz.getGoodsCouponApplicationListByUser(urUserId, cartGoodsList, GoodsEnums.DeviceType.PC);

        // then - 쿠폰 원복 여부
        assertTrue(goodsCouponListBeforeOrder.get(0).getCouponList().size() > goodsCouponListAfterOrder.get(0).getCouponList().size());
        assertEquals(goodsCouponListBeforeOrder.get(0).getCouponList().size(), goodsCouponListAfterClaim.get(0).getCouponList().size());

        // then - 적립금 지급금액
        int availablePointAfterClaim = promotionPointBiz.getPointUsable(urUserId);
        assertEquals(availablePointBeforeOrder, availablePointAfterClaim);

        // then - 재고추가 여부
        List<CartDeliveryDto> cartDataDtoAfterClaim = getCartDataList(cartIdList, buyerVo);
        int stockAfterClaim = cartDataDtoAfterClaim.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();
        assertEquals(stockBeforeOrder, stockAfterClaim);
    }

    @Test
    @DisplayName("장바구니 주문(일반배송-일반회원가-쿠폰(장바구니(정액))-적립금부분사용-카드결제)후_부분취소")
    void 일반배송2() throws Exception {
        // given - 정보설정
        mallNormalLogin();
        given_normal2();

        // given - 회원정보 조회
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        Long urUserId = Long.parseLong(buyerVo.getUrUserId());

        // given - 장바구니 등록
        List<Long> cartIdList = new ArrayList<>();
        for (Long goodsId : gGoodsIdList) {
            cartIdList.add(this.addCart(urUserId, goodsId, 2));
        }

        // given - 장바구니 데이터 조회
        List<CartDeliveryDto> cartDataDto = getCartDataList(cartIdList, buyerVo);

        GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();

        // 매장 정보
        CartStoreDto cartStoreDto = new CartStoreDto();
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            cartStoreDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
            cartStoreDto.setStoreArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
            cartStoreDto.setUrStoreScheduleId(gReqDto.getUrStoreScheduleId());
        }

        // when - 상품 주문가능 상태
        GetCartDataRequestDto cartDataReqDto = new GetCartDataRequestDto();
        cartDataReqDto.setUrUserId(urUserId);
        CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataReqDto, cartDataDto, cartStoreDto);

        // then - 상품 주문가능 상태
        assertNotNull(checkCartResponseDto);
        assertEquals(ShoppingEnums.ApplyPayment.SUCCESS, checkCartResponseDto.getResult());

        // when - 장바구니 집계 조회
        CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, gReqDto.getUsePoint());

        // when - 주문생성
        OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(OrderEnums.PaymentType.CARD.getCode());
        List<OrderBindDto> orderBindList = this.getOrderBindList(buyerVo, paymentType, cartDataDto, sessionShippingDto, cartSummaryDto);

        // then - 가격 검증
        this.testThenPrice(cartSummaryDto, orderBindList);

        // when - 주문 임시데이터 생성
        OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "Y");

        // then - 주문 임시데이터 생성
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

        // given - 주문 승인 - 데이터 보관용
        int availablePointBeforeOrder = promotionPointBiz.getPointUsable(urUserId);
        // cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
        List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);
        // List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
        List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);
        // 상품 쿠폰 리스트 조회
        List<GoodsCouponDto> goodsCouponListBeforeOrder = promotionCouponBiz.getGoodsCouponApplicationListByUser(urUserId, cartGoodsList, GoodsEnums.DeviceType.PC);
        // 장바구니 쿠폰 리스트 조회
        List<CouponDto> cartCouponListBeforeOrder = promotionCouponBiz.getCartCouponApplicationListByUser(urUserId, cartGoodsList, GoodsEnums.DeviceType.PC);

        // when - 주문 승인
        String odid = orderRegistrationResponseDto.getOdids().split(",")[0];

        // 주문번호로 주문데이터 조회
        PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

        // 승인처리
        PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setPgAccountType(PgEnums.PgAccountType.INICIS_BASIC);
        paymentData.setTid("123");
        paymentData.setAuthCode("123");
        paymentData.setCardNumber("123");
        paymentData.setCardQuotaInterest("N");
        paymentData.setCardQuota("");
        paymentData.setVirtualAccountNumber("123");
        paymentData.setBankName(PgEnums.InicisBankCode.INICIS_03.getCodeName());
        paymentData.setInfo("test");
        paymentData.setPaidDueDate(LocalDateTime.now());
        paymentData.setPaidHolder("test");
        paymentData.setPartCancelYn("Y");
        paymentData.setEscrowYn("N");
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setCashReceiptYn("N");
        paymentData.setCashReceiptType("");
        paymentData.setCashReceiptNo("");
        paymentData.setCashReceiptAuthNo("");
        paymentData.setResponseData("");
        PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

        // then - 주문 승인
        assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

        // then - 쿠폰 사용 여부
        List<Long> pmCouponIssueIdList = Arrays.asList(gGoodsCouponIssueId1, gCartCouponIssueId);
        for (Long pmCouponIssueId : pmCouponIssueIdList) {
            CouponEnums.UseCouponValidation couponStatus = promotionCouponBiz.checkUseCouponValidation(urUserId, pmCouponIssueId);
            assertEquals(CouponEnums.UseCouponValidation.USE_ISSUE_STATUS, couponStatus);
        }

        // given - 주문 승인 - 주문이후 데이터 추출용
        int availablePointAfterOrder = promotionPointBiz.getPointUsable(urUserId);
        // 상품 쿠폰 리스트 조회
        List<GoodsCouponDto> goodsCouponListAfterOrder = promotionCouponBiz.getGoodsCouponApplicationListByUser(urUserId, cartGoodsList, GoodsEnums.DeviceType.PC);
        // 장바구니 쿠폰 리스트 조회
        List<CouponDto> cartCouponListAfterOrder = promotionCouponBiz.getCartCouponApplicationListByUser(urUserId, cartGoodsList, GoodsEnums.DeviceType.PC);

        // then - 적립금 사용 여부
        assertEquals(gReqDto.getUsePoint(), availablePointBeforeOrder - availablePointAfterOrder);
        assertTrue(availablePointBeforeOrder > availablePointAfterOrder);

        // then - 재고차감 여부
        int stockBeforeOrder = cartDataDto.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();

        int stockAfterOrder = getGoodsStock(gGoodsIdList.get(0));
        assertTrue(stockBeforeOrder > stockAfterOrder);

        // when - 주문정보 조회
        String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];
        ApiResult<?> orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        MallOrderDetailListResponseDto orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        List<MallOrderDetailGoodsDto> orderDetailInfoList = orderInfo.getOrderDetailList().stream()
                .map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList());
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderDetailInfoList.get(0).getOrderStatusCd());

        // then - 주문 결제 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderInfo.getPayInfo().getStatus());

        // when - 부분 취소
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.PART, OrderClaimEnums.ClaimStatusTp.CANCEL, true);
        mallNormalLogin();

        // then - 주문 부분 취소
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // when - 쿠폰 원복 여부 - 쿠폰은 재발급 수량비교
        // 상품 쿠폰 리스트 조회
        List<CouponDto> cartCouponListAfterClaim = promotionCouponBiz.getCartCouponApplicationListByUser(urUserId, cartGoodsList, GoodsEnums.DeviceType.PC);

        // then - 쿠폰 원복 여부
        assertTrue(cartCouponListBeforeOrder.size() > cartCouponListAfterOrder.size());
        assertEquals(cartCouponListBeforeOrder.size(), cartCouponListAfterClaim.size());

        // then - 적립금 지급금액 - 부분취소 적립금취소 없음
        int availablePointAfterClaim = promotionPointBiz.getPointUsable(urUserId);
        assertEquals(availablePointAfterOrder, availablePointAfterClaim);

        // then - PG 결제잔여금액 합
        OrderClaimRegisterRequestDto claimRequestDto = new OrderClaimRegisterRequestDto();
        claimRequestDto.setOdOrderId(Long.parseLong(odOrderId));
        RefundPaymentInfoDto payInfoDto = claimProcessMapper.getRefundPaymentInfo(claimRequestDto);
        assertTrue(payInfoDto.getRemaindPrice() > 0);

        // then - 재고추가 여부
        int stockAfterClaim = getGoodsStock(gGoodsIdList.get(0));
        assertEquals(stockBeforeOrder - 1, stockAfterClaim);

        // when - 주문 전체 취소
        orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();
        ClaimRequestProcessDto claimRequestPart = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.CANCEL, true);
        mallNormalLogin();

        // then - 주문 전체 취소
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestPart.getClaimResult());

        // when - 쿠폰 원복 여부 - 쿠폰은 재발급 수량비교
        // 상품 쿠폰 리스트 조회
        List<GoodsCouponDto> goodsCouponListAfterClaim = promotionCouponBiz.getGoodsCouponApplicationListByUser(urUserId, cartGoodsList, GoodsEnums.DeviceType.PC);

        // then - 쿠폰 원복 여부
        assertTrue(goodsCouponListBeforeOrder.get(0).getCouponList().size() > goodsCouponListAfterOrder.get(0).getCouponList().size());
        assertEquals(goodsCouponListBeforeOrder.get(0).getCouponList().size(), goodsCouponListAfterClaim.get(0).getCouponList().size());

        // then - PG 결제잔여금액 합
        payInfoDto = claimProcessMapper.getRefundPaymentInfo(claimRequestDto);
        assertEquals(0, payInfoDto.getRemaindPrice());

        // then - 재고추가 여부
        stockAfterClaim = getGoodsStock(gGoodsIdList.get(0));
        assertEquals(stockBeforeOrder, stockAfterClaim);
    }

    @Test
    @DisplayName("장바구니 주문(일반배송-임직원가-적립금전체사용)후_배송처리후_전체반품(판매자귀책)")
    void 일반배송3() throws Exception {
        // given - 정보설정
        mallEmployeeLogin();
        given_normal3();

        // given - 회원정보 조회
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        Long urUserId = Long.parseLong(buyerVo.getUrUserId());

        // given - 장바구니 등록
        List<Long> cartIdList = new ArrayList<>();
        for (Long goodsId : gGoodsIdList) {
            cartIdList.add(this.addCart(urUserId, goodsId, 1));
        }

        // given - 장바구니 데이터 조회
        List<CartDeliveryDto> cartDataDto = this.getCartDataList(cartIdList, buyerVo);

        GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();

        // 매장 정보
        CartStoreDto cartStoreDto = new CartStoreDto();
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            cartStoreDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
            cartStoreDto.setStoreArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
            cartStoreDto.setUrStoreScheduleId(gReqDto.getUrStoreScheduleId());
        }

        // when - 상품 주문가능 상태
        GetCartDataRequestDto cartDataReqDto = new GetCartDataRequestDto();
        cartDataReqDto.setUrUserId(urUserId);
        CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataReqDto, cartDataDto, cartStoreDto);

        // then - 상품 주문가능 상태
        assertNotNull(checkCartResponseDto);
        assertEquals(ShoppingEnums.ApplyPayment.SUCCESS, checkCartResponseDto.getResult());

        // when - 장바구니 집계 조회
        CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, gReqDto.getUsePoint());

        // when - 주문생성
        OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(OrderEnums.PaymentType.FREE.getCode());
        List<OrderBindDto> orderBindList = this.getOrderBindList(buyerVo, paymentType, cartDataDto, sessionShippingDto, cartSummaryDto);

        // then - 가격 검증
        testThenPrice(cartSummaryDto, orderBindList);

        // when - 주문 임시데이터 생성
        OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "Y");

        // then - 주문 임시데이터 생성
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

        // given - 주문 승인 - 데이터 보관용
        int availablePointBeforeOrder = promotionPointBiz.getPointUsable(urUserId);
        // cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
        List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);
        // List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
        List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);

        // when - 주문 승인
        String odid = orderRegistrationResponseDto.getOdids().split(",")[0];

        // 주문번호로 주문데이터 조회
        PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

        // 승인처리
        PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setPgAccountType(PgEnums.PgAccountType.INICIS_BASIC);
        paymentData.setTid("123");
        paymentData.setAuthCode("123");
        paymentData.setCardNumber("123");
        paymentData.setCardQuotaInterest("N");
        paymentData.setCardQuota("");
        paymentData.setVirtualAccountNumber("123");
        paymentData.setBankName(PgEnums.InicisBankCode.INICIS_03.getCodeName());
        paymentData.setInfo("test");
        paymentData.setPaidDueDate(LocalDateTime.now());
        paymentData.setPaidHolder("test");
        paymentData.setPartCancelYn("Y");
        paymentData.setEscrowYn("N");
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setCashReceiptYn("N");
        paymentData.setCashReceiptType("");
        paymentData.setCashReceiptNo("");
        paymentData.setCashReceiptAuthNo("");
        paymentData.setResponseData("");
        PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

        // then - 주문 승인
        assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

        // given - 주문 승인 - 주문이후 데이터 추출용
        int availablePointAfterOrder = promotionPointBiz.getPointUsable(urUserId);

        // then - 적립금 사용 여부
        assertEquals(gReqDto.getUsePoint(), availablePointBeforeOrder - availablePointAfterOrder);
        assertTrue(availablePointBeforeOrder > availablePointAfterOrder);

        // then - 재고차감 여부
        List<CartDeliveryDto> cartDataDtoAfterOrder = getCartDataList(cartIdList, buyerVo);
        int stockBeforeOrder = cartDataDto.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();

        int stockAfterOrder = getGoodsStock(gGoodsIdList.get(0));
        assertTrue(stockBeforeOrder > stockAfterOrder);

        // when - 주문정보 조회
        String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];
        ApiResult<?> orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        MallOrderDetailListResponseDto orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        List<MallOrderDetailGoodsDto> orderDetailInfoList = orderInfo.getOrderDetailList().stream()
                .map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList());
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderDetailInfoList.get(0).getOrderStatusCd());

        // then - 주문 결제 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderInfo.getPayInfo().getStatus());

        // when - 배송준비중 변경
        // 출고처 상태 변경
        bosLogin();
        WarehouseModifyDto warehouseModifyDto = new WarehouseModifyDto();
        warehouseModifyDto.setUrWarehouseId(String.valueOf(gUrWareHouseId));
        warehouseModifyDto.setWarehouseGroupCode("WAREHOUSE_GROUP.OWN");
        warehouseModifyDto.setInputWarehouseName("[QA]용인물류");
        warehouseModifyDto.setInputCompanyName("");
        warehouseModifyDto.setStockOrderYn("Y");
        warehouseModifyDto.setStlmnYn("Y");
        warehouseModifyDto.setHour("17");
        warehouseModifyDto.setMinute("15");
        warehouseModifyDto.setDawnDlvryYn("Y");
        warehouseModifyDto.setStoreYn("N");
        warehouseModifyDto.setReceiverZipCode("17098");
        warehouseModifyDto.setReceiverAddress1("경기 용인시 기흥구 청명산로 2");
        warehouseModifyDto.setReceiverAddress2("1234");
        warehouseModifyDto.setUndeliverableAreaTp("UNDELIVERABLE_TP.JEJU");
        warehouseModifyDto.setDawnUndeliverableAreaTp("");
        warehouseModifyDto.setWarehouseMemo("");
        warehouseModifyDto.setOrderChangeType("ORDER_CHANGE_TP.ORDER_CHANGE");
        ApiResult<?> warehouseResult = warehouseBiz.putWarehouse(warehouseModifyDto);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), warehouseResult.getCode());

        UserVo userVo = SessionUtil.getBosUserVO();

        List<Long> detlIdList = new ArrayList<>();
        for (MallOrderDetailGoodsDto orderDetailInfo : orderDetailInfoList) {
            if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderDetailInfo.getGoodsTpCd())) {
                for (MallOrderDetailGoodsDto packageGoodsInfo : orderDetailInfo.getPackageGoodsList()) {
                    detlIdList.add(packageGoodsInfo.getOdOrderDetlId());
                }
            } else {
                detlIdList.add(orderDetailInfo.getOdOrderDetlId());
            }
        }

        OrderStatusUpdateRequestDto orderStatusUpdateRequestDto = OrderStatusUpdateRequestDto.builder()
                .userId(Long.parseLong(userVo.getUserId()))
                .loginName(userVo.getLoginName())
                .detlIdList(detlIdList)
                .shippingCompIdList(null)
                .trackingNoList(null)
                .statusCd(OrderEnums.OrderStatus.DELIVERY_READY.getCode())
                .build();

        ApiResult<?> orderStatusDR = orderStatusBiz.putOrderDetailListStatus(orderStatusUpdateRequestDto);

        // then - 배송준비중 변경
        assertEquals(BaseEnums.Default.SUCCESS.getCode(), orderStatusDR.getCode());

        // when - 배송중 변경
        orderStatusUpdateRequestDto = OrderStatusUpdateRequestDto.builder()
                .userId(Long.parseLong(userVo.getUserId()))
                .loginName(userVo.getLoginName())
                .detlIdList(detlIdList)
                .shippingCompIdList(Collections.singletonList(1L))
                .trackingNoList(Collections.singletonList("1"))
                .statusCd(OrderEnums.OrderStatus.DELIVERY_ING.getCode())
                .build();

        ApiResult<?> orderStatusDI = orderStatusBiz.putOrderDetailListStatus(orderStatusUpdateRequestDto);
        mallEmployeeLogin();

        // then - 배송중 변경
        assertEquals(BaseEnums.Default.SUCCESS.getCode(), orderStatusDI.getCode());


        // when - 주문반품
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.RETURN, true);
        mallEmployeeLogin();
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // then - 적립금 지급금액
        int availablePointAfterClaim = promotionPointBiz.getPointUsable(urUserId);
        assertEquals(availablePointBeforeOrder, availablePointAfterClaim);

        // then - 재고추가 여부 - 반품은 재고원복 없음
        int stockAfterClaim = getGoodsStock(gGoodsIdList.get(0));
        assertEquals(stockAfterOrder, stockAfterClaim);
    }

    @Test
    @DisplayName("장바구니 주문(일반배송-임직원가-적립금부분사용-카드결제)후_배송처리후_부분반품(구매자귀책)")
    void 일반배송4() throws Exception {
        // given - 정보설정
        mallEmployeeLogin();
        given_normal4();

        // given - 회원정보 조회
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        Long urUserId = Long.parseLong(buyerVo.getUrUserId());

        // given - 장바구니 등록
        List<Long> cartIdList = new ArrayList<>();
        for (Long goodsId : gGoodsIdList) {
            cartIdList.add(this.addCart(urUserId, goodsId, 2));
        }

        // given - 장바구니 데이터 조회
        List<CartDeliveryDto> cartDataDto = this.getCartDataList(cartIdList, buyerVo);

        GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();

        // 매장 정보
        CartStoreDto cartStoreDto = new CartStoreDto();
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            cartStoreDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
            cartStoreDto.setStoreArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
            cartStoreDto.setUrStoreScheduleId(gReqDto.getUrStoreScheduleId());
        }

        // when - 상품 주문가능 상태
        GetCartDataRequestDto cartDataReqDto = new GetCartDataRequestDto();
        cartDataReqDto.setUrUserId(urUserId);
        CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataReqDto, cartDataDto, cartStoreDto);

        // then - 상품 주문가능 상태
        assertNotNull(checkCartResponseDto);
        assertEquals(ShoppingEnums.ApplyPayment.SUCCESS, checkCartResponseDto.getResult());

        // when - 장바구니 집계 조회
        CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, gReqDto.getUsePoint());

        // when - 주문생성
        OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(OrderEnums.PaymentType.FREE.getCode());
        List<OrderBindDto> orderBindList = this.getOrderBindList(buyerVo, paymentType, cartDataDto, sessionShippingDto, cartSummaryDto);

        // then - 가격 검증
        testThenPrice(cartSummaryDto, orderBindList);

        // when - 주문 임시데이터 생성
        OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "Y");

        // then - 주문 임시데이터 생성
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

        // given - 주문 승인 - 데이터 보관용
        int availablePointBeforeOrder = promotionPointBiz.getPointUsable(urUserId);
        // cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
        List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);
        // List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
        List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);

        // when - 주문 승인
        String odid = orderRegistrationResponseDto.getOdids().split(",")[0];

        // 주문번호로 주문데이터 조회
        PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

        // 승인처리
        PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setPgAccountType(PgEnums.PgAccountType.INICIS_BASIC);
        paymentData.setTid("123");
        paymentData.setAuthCode("123");
        paymentData.setCardNumber("123");
        paymentData.setCardQuotaInterest("N");
        paymentData.setCardQuota("");
        paymentData.setVirtualAccountNumber("123");
        paymentData.setBankName(PgEnums.InicisBankCode.INICIS_03.getCodeName());
        paymentData.setInfo("test");
        paymentData.setPaidDueDate(LocalDateTime.now());
        paymentData.setPaidHolder("test");
        paymentData.setPartCancelYn("Y");
        paymentData.setEscrowYn("N");
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setCashReceiptYn("N");
        paymentData.setCashReceiptType("");
        paymentData.setCashReceiptNo("");
        paymentData.setCashReceiptAuthNo("");
        paymentData.setResponseData("");
        PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

        // then - 주문 승인
        assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

        // given - 주문 승인 - 주문이후 데이터 추출용
        int availablePointAfterOrder = promotionPointBiz.getPointUsable(urUserId);

        // then - 적립금 사용 여부
        assertEquals(gReqDto.getUsePoint(), availablePointBeforeOrder - availablePointAfterOrder);
        assertTrue(availablePointBeforeOrder > availablePointAfterOrder);

        // then - 재고차감 여부
        int stockBeforeOrder = cartDataDto.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();

        int stockAfterOrder = getGoodsStock(gGoodsIdList.get(0));
        assertTrue(stockBeforeOrder > stockAfterOrder);

        // when - 주문정보 조회
        String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];
        ApiResult<?> orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        MallOrderDetailListResponseDto orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        List<MallOrderDetailGoodsDto> orderDetailInfoList = orderInfo.getOrderDetailList().stream()
                .map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList());
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderDetailInfoList.get(0).getOrderStatusCd());

        // then - 주문 결제 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderInfo.getPayInfo().getStatus());

        // when - 배송준비중 변경
        // 출고처 상태 변경
        bosLogin();
        WarehouseModifyDto warehouseModifyDto = new WarehouseModifyDto();
        warehouseModifyDto.setUrWarehouseId(String.valueOf(gUrWareHouseId));
        warehouseModifyDto.setWarehouseGroupCode("WAREHOUSE_GROUP.OWN");
        warehouseModifyDto.setInputWarehouseName("[QA]용인물류");
        warehouseModifyDto.setInputCompanyName("");
        warehouseModifyDto.setStockOrderYn("Y");
        warehouseModifyDto.setStlmnYn("Y");
        warehouseModifyDto.setHour("17");
        warehouseModifyDto.setMinute("15");
        warehouseModifyDto.setDawnDlvryYn("Y");
        warehouseModifyDto.setStoreYn("N");
        warehouseModifyDto.setReceiverZipCode("17098");
        warehouseModifyDto.setReceiverAddress1("경기 용인시 기흥구 청명산로 2");
        warehouseModifyDto.setReceiverAddress2("1234");
        warehouseModifyDto.setUndeliverableAreaTp("UNDELIVERABLE_TP.JEJU");
        warehouseModifyDto.setDawnUndeliverableAreaTp("");
        warehouseModifyDto.setWarehouseMemo("");
        warehouseModifyDto.setOrderChangeType("ORDER_CHANGE_TP.ORDER_CHANGE");
        ApiResult<?> warehouseResult = warehouseBiz.putWarehouse(warehouseModifyDto);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), warehouseResult.getCode());

        UserVo userVo = SessionUtil.getBosUserVO();

        List<Long> detlIdList = new ArrayList<>();
        for (MallOrderDetailGoodsDto orderDetailInfo : orderDetailInfoList) {
            if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderDetailInfo.getGoodsTpCd())) {
                for (MallOrderDetailGoodsDto packageGoodsInfo : orderDetailInfo.getPackageGoodsList()) {
                    detlIdList.add(packageGoodsInfo.getOdOrderDetlId());
                }
            } else {
                detlIdList.add(orderDetailInfo.getOdOrderDetlId());
            }
        }

        OrderStatusUpdateRequestDto orderStatusUpdateRequestDto = OrderStatusUpdateRequestDto.builder()
                .userId(Long.parseLong(userVo.getUserId()))
                .loginName(userVo.getLoginName())
                .detlIdList(detlIdList)
                .shippingCompIdList(null)
                .trackingNoList(null)
                .statusCd(OrderEnums.OrderStatus.DELIVERY_READY.getCode())
                .build();

        ApiResult<?> orderStatusDR = orderStatusBiz.putOrderDetailListStatus(orderStatusUpdateRequestDto);

        // then - 배송준비중 변경
        assertEquals(BaseEnums.Default.SUCCESS.getCode(), orderStatusDR.getCode());

        // when - 배송중 변경
        orderStatusUpdateRequestDto = OrderStatusUpdateRequestDto.builder()
                .userId(Long.parseLong(userVo.getUserId()))
                .loginName(userVo.getLoginName())
                .detlIdList(detlIdList)
                .shippingCompIdList(Collections.singletonList(1L))
                .trackingNoList(Collections.singletonList("1"))
                .statusCd(OrderEnums.OrderStatus.DELIVERY_ING.getCode())
                .build();

        ApiResult<?> orderStatusDI = orderStatusBiz.putOrderDetailListStatus(orderStatusUpdateRequestDto);
        mallEmployeeLogin();

        // then - 배송중 변경
        assertEquals(BaseEnums.Default.SUCCESS.getCode(), orderStatusDI.getCode());

        // when - 주문반품 - 부분반품
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.PART, OrderClaimEnums.ClaimStatusTp.RETURN, true);
        mallEmployeeLogin();

        // then - 주문반품 확인
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // then - 적립금 지급금액
        int availablePointAfterClaim = promotionPointBiz.getPointUsable(urUserId);
        assertEquals(availablePointAfterOrder, availablePointAfterClaim);

        // then - 재고추가 여부 - 반품시 재고 원복 없음
        int stockAfterClaim = getGoodsStock(gGoodsIdList.get(0));
        assertTrue(stockBeforeOrder > stockAfterClaim);

        // when - 클레임 - 전체취소
        orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();
        claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.CANCEL, true);
        mallEmployeeLogin();

        // then - 주문반품 확인
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // then - 적립금 지급금액
        availablePointAfterClaim = promotionPointBiz.getPointUsable(urUserId);
        assertEquals(availablePointBeforeOrder, availablePointAfterClaim);

        // then - PG 결제잔여금액 합
        OrderClaimRegisterRequestDto claimRequestDto = new OrderClaimRegisterRequestDto();
        claimRequestDto.setOdOrderId(Long.parseLong(odOrderId));
        RefundPaymentInfoDto payInfoDto = claimProcessMapper.getRefundPaymentInfo(claimRequestDto);
        assertEquals(0, payInfoDto.getRemaindPrice());

        // then - 재고추가 여부 - 반품시 재고 원복 없음
        stockAfterClaim = getGoodsStock(gGoodsIdList.get(0));
        assertTrue(stockBeforeOrder > stockAfterClaim);
    }

    @Test
    @DisplayName("장바구니 주문(일반배송-일반회원가-쿠폰(장바구니(정률))-적립금전체사용)후_재배송처리후_전체반품(구매자귀책)")
    void 일반배송5() throws Exception {
        // given - 정보설정
        mallNormalLogin();
        given_normal5();

        // given - 회원정보 조회
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        Long urUserId = Long.parseLong(buyerVo.getUrUserId());

        // given - 장바구니 등록
        List<Long> cartIdList = new ArrayList<>();
        for (Long goodsId : gGoodsIdList) {
            cartIdList.add(this.addCart(urUserId, goodsId, 1));
        }

        // given - 장바구니 데이터 조회
        List<CartDeliveryDto> cartDataDto = this.getCartDataList(cartIdList, buyerVo);

        GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();

        // 매장 정보
        CartStoreDto cartStoreDto = new CartStoreDto();
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            cartStoreDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
            cartStoreDto.setStoreArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
            cartStoreDto.setUrStoreScheduleId(gReqDto.getUrStoreScheduleId());
        }

        // when - 상품 주문가능 상태
        GetCartDataRequestDto cartDataReqDto = new GetCartDataRequestDto();
        cartDataReqDto.setUrUserId(urUserId);
        CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataReqDto, cartDataDto, cartStoreDto);

        // then - 상품 주문가능 상태
        assertNotNull(checkCartResponseDto);
        assertEquals(ShoppingEnums.ApplyPayment.SUCCESS, checkCartResponseDto.getResult());

        // when - 장바구니 집계 조회
        CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, gReqDto.getUsePoint());

        // when - 주문생성
        OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(OrderEnums.PaymentType.FREE.getCode());
        List<OrderBindDto> orderBindList = this.getOrderBindList(buyerVo, paymentType, cartDataDto, sessionShippingDto, cartSummaryDto);

        // then - 가격 검증
        testThenPrice(cartSummaryDto, orderBindList);

        // when - 주문 임시데이터 생성
        OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "Y");

        // then - 주문 임시데이터 생성
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

        // given - 주문 승인 - 데이터 보관용
        int availablePointBeforeOrder = promotionPointBiz.getPointUsable(urUserId);
        // cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
        List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);
        // List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
        List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);
        // 상품 쿠폰 리스트 조회
        List<GoodsCouponDto> goodsCouponListBeforeOrder = promotionCouponBiz.getGoodsCouponApplicationListByUser(urUserId, cartGoodsList, GoodsEnums.DeviceType.PC);
        // 장바구니 쿠폰 리스트 조회
        List<CouponDto> cartCouponListBeforeOrder = promotionCouponBiz.getCartCouponApplicationListByUser(urUserId, cartGoodsList, GoodsEnums.DeviceType.PC);

        // when - 주문 승인
        String odid = orderRegistrationResponseDto.getOdids().split(",")[0];

        // 주문번호로 주문데이터 조회
        PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

        // 승인처리
        PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setPgAccountType(PgEnums.PgAccountType.INICIS_BASIC);
        paymentData.setTid("123");
        paymentData.setAuthCode("123");
        paymentData.setCardNumber("123");
        paymentData.setCardQuotaInterest("N");
        paymentData.setCardQuota("");
        paymentData.setVirtualAccountNumber("123");
        paymentData.setBankName(PgEnums.InicisBankCode.INICIS_03.getCodeName());
        paymentData.setInfo("test");
        paymentData.setPaidDueDate(LocalDateTime.now());
        paymentData.setPaidHolder("test");
        paymentData.setPartCancelYn("Y");
        paymentData.setEscrowYn("N");
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setCashReceiptYn("N");
        paymentData.setCashReceiptType("");
        paymentData.setCashReceiptNo("");
        paymentData.setCashReceiptAuthNo("");
        paymentData.setResponseData("");
        PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

        // then - 주문 승인
        assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

        // then - 쿠폰 사용 여부
        List<Long> pmCouponIssueIdList = Arrays.asList(gGoodsCouponIssueId1, gCartCouponIssueId);
        for (Long pmCouponIssueId : pmCouponIssueIdList) {
            CouponEnums.UseCouponValidation couponStatus = promotionCouponBiz.checkUseCouponValidation(urUserId, pmCouponIssueId);
            assertEquals(CouponEnums.UseCouponValidation.USE_ISSUE_STATUS, couponStatus);
        }

        // given - 주문 승인 - 주문이후 데이터 추출용
        int availablePointAfterOrder = promotionPointBiz.getPointUsable(urUserId);
        // 상품 쿠폰 리스트 조회
        List<GoodsCouponDto> goodsCouponListAfterOrder = promotionCouponBiz.getGoodsCouponApplicationListByUser(urUserId, cartGoodsList, GoodsEnums.DeviceType.PC);
        // 장바구니 쿠폰 리스트 조회
        List<CouponDto> cartCouponListAfterOrder = promotionCouponBiz.getCartCouponApplicationListByUser(urUserId, cartGoodsList, GoodsEnums.DeviceType.PC);

        // then - 적립금 사용 여부
        assertEquals(gReqDto.getUsePoint(), availablePointBeforeOrder - availablePointAfterOrder);
        assertTrue(availablePointBeforeOrder > availablePointAfterOrder);

        // then - 재고차감 여부
        int stockBeforeOrder = cartDataDto.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();

        int stockAfterOrder = getGoodsStock(gGoodsIdList.get(0));
        assertTrue(stockBeforeOrder > stockAfterOrder);

        // when - 주문정보 조회
        String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];
        ApiResult<?> orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        MallOrderDetailListResponseDto orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        List<MallOrderDetailGoodsDto> orderDetailInfoList = orderInfo.getOrderDetailList().stream()
                .map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList());
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderDetailInfoList.get(0).getOrderStatusCd());

        // then - 주문 결제 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderInfo.getPayInfo().getStatus());

        // when - 배송준비중 변경
        // 출고처 상태 변경
        bosLogin();
        WarehouseModifyDto warehouseModifyDto = new WarehouseModifyDto();
        warehouseModifyDto.setUrWarehouseId(String.valueOf(gUrWareHouseId));
        warehouseModifyDto.setWarehouseGroupCode("WAREHOUSE_GROUP.OWN");
        warehouseModifyDto.setInputWarehouseName("[QA]용인물류");
        warehouseModifyDto.setInputCompanyName("");
        warehouseModifyDto.setStockOrderYn("Y");
        warehouseModifyDto.setStlmnYn("Y");
        warehouseModifyDto.setHour("17");
        warehouseModifyDto.setMinute("15");
        warehouseModifyDto.setDawnDlvryYn("Y");
        warehouseModifyDto.setStoreYn("N");
        warehouseModifyDto.setReceiverZipCode("17098");
        warehouseModifyDto.setReceiverAddress1("경기 용인시 기흥구 청명산로 2");
        warehouseModifyDto.setReceiverAddress2("1234");
        warehouseModifyDto.setUndeliverableAreaTp("UNDELIVERABLE_TP.JEJU");
        warehouseModifyDto.setDawnUndeliverableAreaTp("");
        warehouseModifyDto.setWarehouseMemo("");
        warehouseModifyDto.setOrderChangeType("ORDER_CHANGE_TP.ORDER_CHANGE");
        ApiResult<?> warehouseResult = warehouseBiz.putWarehouse(warehouseModifyDto);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), warehouseResult.getCode());

        UserVo userVo = SessionUtil.getBosUserVO();

        List<Long> detlIdList = new ArrayList<>();
        for (MallOrderDetailGoodsDto orderDetailInfo : orderDetailInfoList) {
            if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderDetailInfo.getGoodsTpCd())) {
                for (MallOrderDetailGoodsDto packageGoodsInfo : orderDetailInfo.getPackageGoodsList()) {
                    detlIdList.add(packageGoodsInfo.getOdOrderDetlId());
                }
            } else {
                detlIdList.add(orderDetailInfo.getOdOrderDetlId());
            }
        }

        OrderStatusUpdateRequestDto orderStatusUpdateRequestDto = OrderStatusUpdateRequestDto.builder()
                .userId(Long.parseLong(userVo.getUserId()))
                .loginName(userVo.getLoginName())
                .detlIdList(detlIdList)
                .shippingCompIdList(null)
                .trackingNoList(null)
                .statusCd(OrderEnums.OrderStatus.DELIVERY_READY.getCode())
                .build();

        ApiResult<?> orderStatusDR = orderStatusBiz.putOrderDetailListStatus(orderStatusUpdateRequestDto);

        // then - 배송준비중 변경
        assertEquals(BaseEnums.Default.SUCCESS.getCode(), orderStatusDR.getCode());

        // when - 배송중 변경
        orderStatusUpdateRequestDto = OrderStatusUpdateRequestDto.builder()
                .userId(Long.parseLong(userVo.getUserId()))
                .loginName(userVo.getLoginName())
                .detlIdList(detlIdList)
                .shippingCompIdList(Collections.singletonList(1L))
                .trackingNoList(Collections.singletonList("1"))
                .statusCd(OrderEnums.OrderStatus.DELIVERY_ING.getCode())
                .build();

        ApiResult<?> orderStatusDI = orderStatusBiz.putOrderDetailListStatus(orderStatusUpdateRequestDto);
        mallNormalLogin();

        // then - 배송중 변경
        assertEquals(BaseEnums.Default.SUCCESS.getCode(), orderStatusDI.getCode());

        // when - 주문재배송
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.RETURN_DELIVERY, true);
        mallNormalLogin();

        // then - 주문재배송 확인
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // when - 쿠폰 원복 여부 - 쿠폰은 재발급 수량비교
        // 상품 쿠폰 리스트 조회
        List<CouponDto> cartCouponListAfterClaim = promotionCouponBiz.getCartCouponApplicationListByUser(urUserId, cartGoodsList, GoodsEnums.DeviceType.PC);

        // then - 쿠폰 원복 여부 - 사용상태 유지
        assertTrue(cartCouponListBeforeOrder.size() > cartCouponListAfterOrder.size());
        assertEquals(cartCouponListAfterOrder.size(), cartCouponListAfterClaim.size());

        // then - 적립금 지급금액 - 사용상태 유지
        int availablePointAfterClaim = promotionPointBiz.getPointUsable(urUserId);
        assertEquals(availablePointAfterOrder, availablePointAfterClaim);

        // then - 재고추가 여부 - 사용상태 유지
        int stockAfterClaim = getGoodsStock(gGoodsIdList.get(0));
        assertTrue(stockBeforeOrder > stockAfterClaim);

        // when - 주문반품 - 전체반품처리 로직 확인필요함
        ApiResult<?> orderInfoApiResult2 = orderDetailBiz.getOrderDetailGoodsList(odOrderId);
        OrderDetailGoodsListResponseDto orderDetailInfo = (OrderDetailGoodsListResponseDto) orderInfoApiResult2.getData();

        // when - 주문 전체 반품
        bosLogin();
        ClaimRequestProcessDto claimReturnResponse = this.claimRequestExcel(Long.parseLong(odOrderId), odid, orderDetailInfo, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.RETURN);

        // then - 주문 전체 취소
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimReturnResponse.getClaimResult());

    }

    @Test
    @DisplayName("장바구니 주문(일반배송-임직원가-적립금부분사용-카드결제)후_재배송처리후_부분반품(판매자귀책)")
    void 일반배송6() throws Exception {
        // given - 정보설정
        mallEmployeeLogin();
        given_normal6();

        // given - 회원정보 조회
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        Long urUserId = Long.parseLong(buyerVo.getUrUserId());

        // given - 장바구니 등록
        List<Long> cartIdList = new ArrayList<>();
        for (Long goodsId : gGoodsIdList) {
            cartIdList.add(this.addCart(urUserId, goodsId, 2));
        }

        // given - 장바구니 데이터 조회
        List<CartDeliveryDto> cartDataDto = this.getCartDataList(cartIdList, buyerVo);

        GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();

        // 매장 정보
        CartStoreDto cartStoreDto = new CartStoreDto();
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            cartStoreDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
            cartStoreDto.setStoreArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
            cartStoreDto.setUrStoreScheduleId(gReqDto.getUrStoreScheduleId());
        }

        // when - 상품 주문가능 상태
        GetCartDataRequestDto cartDataReqDto = new GetCartDataRequestDto();
        cartDataReqDto.setUrUserId(urUserId);
        CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataReqDto, cartDataDto, cartStoreDto);

        // then - 상품 주문가능 상태
        assertNotNull(checkCartResponseDto);
        assertEquals(ShoppingEnums.ApplyPayment.SUCCESS, checkCartResponseDto.getResult());

        // when - 장바구니 집계 조회
        CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, gReqDto.getUsePoint());

        // when - 주문생성
        OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(OrderEnums.PaymentType.FREE.getCode());
        List<OrderBindDto> orderBindList = this.getOrderBindList(buyerVo, paymentType, cartDataDto, sessionShippingDto, cartSummaryDto);

        // then - 가격 검증
        testThenPrice(cartSummaryDto, orderBindList);

        // when - 주문 임시데이터 생성
        OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "Y");

        // then - 주문 임시데이터 생성
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

        // given - 주문 승인 - 데이터 보관용
        int availablePointBeforeOrder = promotionPointBiz.getPointUsable(urUserId);
        // cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
        List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);
        // List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
        List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);

        // when - 주문 승인
        String odid = orderRegistrationResponseDto.getOdids().split(",")[0];

        // 주문번호로 주문데이터 조회
        PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

        // 승인처리
        PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setPgAccountType(PgEnums.PgAccountType.INICIS_BASIC);
        paymentData.setTid("123");
        paymentData.setAuthCode("123");
        paymentData.setCardNumber("123");
        paymentData.setCardQuotaInterest("N");
        paymentData.setCardQuota("");
        paymentData.setVirtualAccountNumber("123");
        paymentData.setBankName(PgEnums.InicisBankCode.INICIS_03.getCodeName());
        paymentData.setInfo("test");
        paymentData.setPaidDueDate(LocalDateTime.now());
        paymentData.setPaidHolder("test");
        paymentData.setPartCancelYn("Y");
        paymentData.setEscrowYn("N");
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setCashReceiptYn("N");
        paymentData.setCashReceiptType("");
        paymentData.setCashReceiptNo("");
        paymentData.setCashReceiptAuthNo("");
        paymentData.setResponseData("");
        PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

        // then - 주문 승인
        assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

        // given - 주문 승인 - 주문이후 데이터 추출용
        int availablePointAfterOrder = promotionPointBiz.getPointUsable(urUserId);

        // then - 적립금 사용 여부
        assertEquals(gReqDto.getUsePoint(), availablePointBeforeOrder - availablePointAfterOrder);
        assertTrue(availablePointBeforeOrder > availablePointAfterOrder);

        // then - 재고차감 여부 - 변화없음
        int stockBeforeOrder = cartDataDto.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();
        int stockAfterOrder = getGoodsStock(gGoodsIdList.get(0));
        assertEquals(stockBeforeOrder, stockAfterOrder);

        // when - 주문정보 조회
        String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];
        ApiResult<?> orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        MallOrderDetailListResponseDto orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        List<MallOrderDetailGoodsDto> orderDetailInfoList = orderInfo.getOrderDetailList().stream()
                .map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList());
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderDetailInfoList.get(0).getOrderStatusCd());

        // then - 주문 결제 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderInfo.getPayInfo().getStatus());

        // when - 배송준비중 변경
        // 출고처 상태 변경
        bosLogin();
        WarehouseModifyDto warehouseModifyDto = new WarehouseModifyDto();
        warehouseModifyDto.setUrWarehouseId(String.valueOf(gUrWareHouseId));
        warehouseModifyDto.setWarehouseGroupCode("test");
        warehouseModifyDto.setInputWarehouseName("test");
        warehouseModifyDto.setInputCompanyName("test");
        warehouseModifyDto.setStockOrderYn("N");
        warehouseModifyDto.setStlmnYn("N");
        warehouseModifyDto.setOrderChangeType("test");
        warehouseModifyDto.setOrderStatusAlamYn("N");
        warehouseModifyDto.setHolidayGroupYn("N");
        warehouseModifyDto.setHour("01");
        warehouseModifyDto.setMinute("10");
        warehouseModifyDto.setDawnDlvryYn("N");
        warehouseModifyDto.setStoreYn("N");
        warehouseModifyDto.setReceiverZipCode("test");
        warehouseModifyDto.setReceiverAddress1("test");
        warehouseModifyDto.setReceiverAddress2("test");
        warehouseModifyDto.setUndeliverableAreaTp("test");
        warehouseModifyDto.setDawnUndeliverableAreaTp("test");
        warehouseModifyDto.setWarehouseMemo("test");
        warehouseModifyDto.setOrderChangeType("ORDER_CHANGE_TP.ORDER_CHANGE");
        ApiResult<?> warehouseResult = warehouseBiz.putWarehouse(warehouseModifyDto);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), warehouseResult.getCode());

        UserVo userVo = SessionUtil.getBosUserVO();

        List<Long> detlIdList = new ArrayList<>();
        for (MallOrderDetailGoodsDto orderDetailInfo : orderDetailInfoList) {
            if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderDetailInfo.getGoodsTpCd())) {
                for (MallOrderDetailGoodsDto packageGoodsInfo : orderDetailInfo.getPackageGoodsList()) {
                    detlIdList.add(packageGoodsInfo.getOdOrderDetlId());
                }
            } else {
                detlIdList.add(orderDetailInfo.getOdOrderDetlId());
            }
        }

        OrderStatusUpdateRequestDto orderStatusUpdateRequestDto = OrderStatusUpdateRequestDto.builder()
                .userId(Long.parseLong(userVo.getUserId()))
                .loginName(userVo.getLoginName())
                .detlIdList(detlIdList)
                .shippingCompIdList(null)
                .trackingNoList(null)
                .statusCd(OrderEnums.OrderStatus.DELIVERY_READY.getCode())
                .build();

        ApiResult<?> orderStatusDR = orderStatusBiz.putOrderDetailListStatus(orderStatusUpdateRequestDto);

        // then - 배송준비중 변경
        assertEquals(BaseEnums.Default.SUCCESS.getCode(), orderStatusDR.getCode());

        // when - 배송중 변경
        orderStatusUpdateRequestDto = OrderStatusUpdateRequestDto.builder()
                .userId(Long.parseLong(userVo.getUserId()))
                .loginName(userVo.getLoginName())
                .detlIdList(detlIdList)
                .shippingCompIdList(Collections.singletonList(1L))
                .trackingNoList(Collections.singletonList("1"))
                .statusCd(OrderEnums.OrderStatus.DELIVERY_ING.getCode())
                .build();

        ApiResult<?> orderStatusDI = orderStatusBiz.putOrderDetailListStatus(orderStatusUpdateRequestDto);
        mallEmployeeLogin();


        // then - 배송중 변경
        assertEquals(BaseEnums.Default.SUCCESS.getCode(), orderStatusDI.getCode());

        // when - 클레임상태변경 - 재배송
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.PART, OrderClaimEnums.ClaimStatusTp.RETURN_DELIVERY, true);
        mallEmployeeLogin();

        // then - 클레임상태변경 - 재배송 확인
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // then - 적립금 지급금액 - 사용상태 유지
        int availablePointAfterClaim = promotionPointBiz.getPointUsable(urUserId);
        assertEquals(availablePointAfterOrder, availablePointAfterClaim);

        // then - 재고추가 여부 - 변화없음
        int stockAfterClaim = getGoodsStock(gGoodsIdList.get(0));
        assertEquals(stockBeforeOrder, stockAfterClaim);

        // when - 주문정보 조회
        orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();

        // when - 주문반품 - 부분반품처리
        claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.PART, OrderClaimEnums.ClaimStatusTp.RETURN, true);
        mallEmployeeLogin();

        // then - 주문반품 확인
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // then - 적립금 지급금액
        availablePointAfterClaim = promotionPointBiz.getPointUsable(urUserId);
        assertEquals(availablePointAfterOrder, availablePointAfterClaim);

        // then - PG 결제잔여금액 합
        OrderClaimRegisterRequestDto claimRequestDto = new OrderClaimRegisterRequestDto();
        claimRequestDto.setOdOrderId(Long.parseLong(odOrderId));
        RefundPaymentInfoDto payInfoDto = claimProcessMapper.getRefundPaymentInfo(claimRequestDto);

        assertTrue(payInfoDto.getRemaindPrice() > 0);

        // then - 재고추가 여부 - 변화없음
        stockAfterClaim = getGoodsStock(gGoodsIdList.get(0));
        assertEquals(stockBeforeOrder, stockAfterClaim);
    }

    @Test
    @DisplayName("장바구니 주문(매장배송-매장픽업-일반회원가-적립금전체사용)후_전체취소")
    void 매장배송1() throws Exception {
        // given - 정보설정
        mallNormalLogin();
        given_shop1();

        // given - 회원정보 조회
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        Long urUserId = Long.parseLong(buyerVo.getUrUserId());

        // given - 장바구니 등록
        List<Long> cartIdList = new ArrayList<>();
        for (Long goodsId : gGoodsIdList) {
            cartIdList.add(this.addCart(urUserId, goodsId, 1));
        }

        // given - 장바구니 데이터 조회
        List<CartDeliveryDto> cartDataDto = this.getCartDataList(cartIdList, buyerVo);

        GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();
        // 정기 배송일때 기존 주문건이 있으면 주소지를 기존 주문건 유지해야함
//        if (ShoppingEnums.CartType.REGULAR.getCode().equals(reqDto.getCartType())) {
//            CartRegularShippingDto cartRegularShippingDto = orderRegularBiz.getRegularInfoByCart(longUrUserId);
//            if (cartRegularShippingDto.isAdditionalOrder()) {
//                sessionShippingDto = orderRegularBiz.getRegularShippingZone(cartRegularShippingDto.getOdRegularReqId());
//            }
//        }

        // 매장 정보
        CartStoreDto cartStoreDto = new CartStoreDto();
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            cartStoreDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
            cartStoreDto.setStoreArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
            cartStoreDto.setUrStoreScheduleId(gReqDto.getUrStoreScheduleId());
        }

        // when - 상품 주문가능 상태
        GetCartDataRequestDto cartDataReqDto = new GetCartDataRequestDto();
        cartDataReqDto.setUrUserId(urUserId);
        CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataReqDto, cartDataDto, cartStoreDto);

        // then - 상품 주문가능 상태
        assertNotNull(checkCartResponseDto);
        assertEquals(ShoppingEnums.ApplyPayment.SUCCESS, checkCartResponseDto.getResult());

        // when - 장바구니 집계 조회
        CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, gReqDto.getUsePoint());

        // when - 주문생성
        OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(OrderEnums.PaymentType.FREE.getCode());
        List<OrderBindDto> orderBindList = this.getOrderBindList(buyerVo, paymentType, cartDataDto, sessionShippingDto, cartSummaryDto);

        // then - 가격 검증
        testThenPrice(cartSummaryDto, orderBindList);

        // when - 주문 임시데이터 생성
        OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "Y");

        // then - 주문 임시데이터 생성
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

        // given - 주문 승인 - 데이터 보관용
        int availablePointBeforeOrder = promotionPointBiz.getPointUsable(urUserId);
        // cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
        List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);
        // List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
        List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);

        // when - 주문 승인
        String odid = orderRegistrationResponseDto.getOdids().split(",")[0];

        // 주문번호로 주문데이터 조회
        PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

        // 승인처리
        PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setPgAccountType(PgEnums.PgAccountType.INICIS_BASIC);
        paymentData.setTid("123");
        paymentData.setAuthCode("123");
        paymentData.setCardNumber("123");
        paymentData.setCardQuotaInterest("N");
        paymentData.setCardQuota("");
        paymentData.setVirtualAccountNumber("123");
        paymentData.setBankName(PgEnums.InicisBankCode.INICIS_03.getCodeName());
        paymentData.setInfo("test");
        paymentData.setPaidDueDate(LocalDateTime.now());
        paymentData.setPaidHolder("test");
        paymentData.setPartCancelYn("Y");
        paymentData.setEscrowYn("N");
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setCashReceiptYn("N");
        paymentData.setCashReceiptType("");
        paymentData.setCashReceiptNo("");
        paymentData.setCashReceiptAuthNo("");
        paymentData.setResponseData("");
        PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

        // then - 주문 승인
        assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

        // given - 주문 승인 - 주문이후 데이터 추출용
        int availablePointAfterOrder = promotionPointBiz.getPointUsable(urUserId);

        // then - 적립금 사용 여부
        assertEquals(gReqDto.getUsePoint(), availablePointBeforeOrder - availablePointAfterOrder);
        assertTrue(availablePointBeforeOrder > availablePointAfterOrder);

        // then - 재고차감 여부
        List<CartDeliveryDto> cartDataDtoAfterOrder = getCartDataList(cartIdList, buyerVo);
        int stockBeforeOrder = cartDataDto.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();

        int stockAfterOrder = cartDataDtoAfterOrder.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();
        assertTrue(stockBeforeOrder > stockAfterOrder);

        // when - 주문정보 조회
        String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];
        ApiResult<?> orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        MallOrderDetailListResponseDto orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        List<MallOrderDetailGoodsDto> orderDetailInfoList = orderInfo.getOrderDetailList().stream()
                .map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList());
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderDetailInfoList.get(0).getOrderStatusCd());

        // then - 주문 결제 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderInfo.getPayInfo().getStatus());

        // when - 주문 전체 취소
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.CANCEL, true);
        mallNormalLogin();

        // then - 주문 전체 취소
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // then - 적립금 지급금액
        int availablePointAfterClaim = promotionPointBiz.getPointUsable(urUserId);
        assertEquals(availablePointBeforeOrder, availablePointAfterClaim);

        // then - PG 결제잔여금액 합
        OrderClaimRegisterRequestDto claimRequestDto = new OrderClaimRegisterRequestDto();
        claimRequestDto.setOdOrderId(Long.parseLong(odOrderId));
        RefundPaymentInfoDto payInfoDto = claimProcessMapper.getRefundPaymentInfo(claimRequestDto);

        assertEquals(0, payInfoDto.getRemaindPrice());

        // then - 재고추가 여부
        List<CartDeliveryDto> cartDataDtoAfterClaim = getCartDataList(cartIdList, buyerVo);
        int stockAfterClaim = cartDataDtoAfterClaim.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();
        assertEquals(stockBeforeOrder, stockAfterClaim);

    }

    @Test
    @DisplayName("장바구니 주문(매장배송-매장배송-임직원가-적립금전체사용)후_전체취소")
    void 매장배송2() throws Exception {
        // given - 정보설정
        mallEmployeeLogin();
        given_shop2();

        // given - 회원정보 조회
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        Long urUserId = Long.parseLong(buyerVo.getUrUserId());

        // given - 장바구니 등록
        List<Long> cartIdList = new ArrayList<>();
        for (Long goodsId : gGoodsIdList) {
            cartIdList.add(this.addCart(urUserId, goodsId, 1));
        }

        // given - 장바구니 데이터 조회
        List<CartDeliveryDto> cartDataDto = this.getCartDataList(cartIdList, buyerVo);

        GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();

        // 매장 정보
        CartStoreDto cartStoreDto = new CartStoreDto();
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            cartStoreDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
            cartStoreDto.setStoreArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
            cartStoreDto.setUrStoreScheduleId(gReqDto.getUrStoreScheduleId());
        }

        // when - 상품 주문가능 상태
        GetCartDataRequestDto cartDataReqDto = new GetCartDataRequestDto();
        cartDataReqDto.setUrUserId(urUserId);
        CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataReqDto, cartDataDto, cartStoreDto);

        // then - 상품 주문가능 상태
        assertNotNull(checkCartResponseDto);
        assertEquals(ShoppingEnums.ApplyPayment.SUCCESS, checkCartResponseDto.getResult());

        // when - 장바구니 집계 조회
        CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, gReqDto.getUsePoint());

        // when - 주문생성
        OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(OrderEnums.PaymentType.FREE.getCode());
        List<OrderBindDto> orderBindList = this.getOrderBindList(buyerVo, paymentType, cartDataDto, sessionShippingDto, cartSummaryDto);

        // then - 가격 검증
        testThenPrice(cartSummaryDto, orderBindList);

        // when - 주문 임시데이터 생성
        OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "Y");

        // then - 주문 임시데이터 생성
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

        // given - 주문 승인 - 데이터 보관용
        int availablePointBeforeOrder = promotionPointBiz.getPointUsable(urUserId);
        // cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
        List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);
        // List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
        List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);

        // when - 주문 승인
        String odid = orderRegistrationResponseDto.getOdids().split(",")[0];

        // 주문번호로 주문데이터 조회
        PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

        // 승인처리
        PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setPgAccountType(PgEnums.PgAccountType.INICIS_BASIC);
        paymentData.setTid("123");
        paymentData.setAuthCode("123");
        paymentData.setCardNumber("123");
        paymentData.setCardQuotaInterest("N");
        paymentData.setCardQuota("");
        paymentData.setVirtualAccountNumber("123");
        paymentData.setBankName(PgEnums.InicisBankCode.INICIS_03.getCodeName());
        paymentData.setInfo("test");
        paymentData.setPaidDueDate(LocalDateTime.now());
        paymentData.setPaidHolder("test");
        paymentData.setPartCancelYn("Y");
        paymentData.setEscrowYn("N");
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setCashReceiptYn("N");
        paymentData.setCashReceiptType("");
        paymentData.setCashReceiptNo("");
        paymentData.setCashReceiptAuthNo("");
        paymentData.setResponseData("");
        PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

        // then - 주문 승인
        assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

        // given - 주문 승인 - 주문이후 데이터 추출용
        int availablePointAfterOrder = promotionPointBiz.getPointUsable(urUserId);

        // then - 적립금 사용 여부
        assertEquals(gReqDto.getUsePoint(), availablePointBeforeOrder - availablePointAfterOrder);
        assertTrue(availablePointBeforeOrder > availablePointAfterOrder);

        // then - 재고차감 여부
        List<CartDeliveryDto> cartDataDtoAfterOrder = getCartDataList(cartIdList, buyerVo);
        int stockBeforeOrder = cartDataDto.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();

        int stockAfterOrder = cartDataDtoAfterOrder.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();
        assertTrue(stockBeforeOrder > stockAfterOrder);

        // when - 주문정보 조회
        String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];
        ApiResult<?> orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        MallOrderDetailListResponseDto orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        List<MallOrderDetailGoodsDto> orderDetailInfoList = orderInfo.getOrderDetailList().stream()
                .map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList());
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderDetailInfoList.get(0).getOrderStatusCd());

        // then - 주문 결제 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderInfo.getPayInfo().getStatus());

        // when - 주문 전체 취소
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.CANCEL, true);
        mallEmployeeLogin();

        // then - 주문 전체 취소
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // then - 적립금 지급금액
        int availablePointAfterClaim = promotionPointBiz.getPointUsable(urUserId);
        assertEquals(availablePointBeforeOrder, availablePointAfterClaim);

        // then - PG 결제잔여금액 합
        OrderClaimRegisterRequestDto claimRequestDto = new OrderClaimRegisterRequestDto();
        claimRequestDto.setOdOrderId(Long.parseLong(odOrderId));
        RefundPaymentInfoDto payInfoDto = claimProcessMapper.getRefundPaymentInfo(claimRequestDto);

        assertEquals(0, payInfoDto.getRemaindPrice());

        // then - 재고추가 여부
        List<CartDeliveryDto> cartDataDtoAfterClaim = getCartDataList(cartIdList, buyerVo);
        int stockAfterClaim = cartDataDtoAfterClaim.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();
        assertEquals(stockBeforeOrder, stockAfterClaim);

    }

    @Test
    @DisplayName("장바구니 주문(렌탈-일반회원가)후_취소")
    void 렌탈1() throws Exception {
        // given - 정보설정
        mallNormalLogin();
        given_rental1();

        // given - 회원정보 조회
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        Long urUserId = Long.parseLong(buyerVo.getUrUserId());

        // given - 장바구니 등록
        List<Long> cartIdList = new ArrayList<>();
        for (Long goodsId : gGoodsIdList) {
            cartIdList.add(this.addCart(urUserId, goodsId, 1));
        }

        // given - 장바구니 데이터 조회
        List<CartDeliveryDto> cartDataDto = this.getCartDataList(cartIdList, buyerVo);

        GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();

        // 매장 정보
        CartStoreDto cartStoreDto = new CartStoreDto();
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            cartStoreDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
            cartStoreDto.setStoreArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
            cartStoreDto.setUrStoreScheduleId(gReqDto.getUrStoreScheduleId());
        }

        // when - 상품 주문가능 상태
        GetCartDataRequestDto cartDataReqDto = new GetCartDataRequestDto();
        cartDataReqDto.setUrUserId(urUserId);
        CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataReqDto, cartDataDto, cartStoreDto);

        // then - 상품 주문가능 상태
        assertNotNull(checkCartResponseDto);
        assertEquals(ShoppingEnums.ApplyPayment.SUCCESS, checkCartResponseDto.getResult());

        // when - 장바구니 집계 조회
        CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, gReqDto.getUsePoint());

        // when - 주문생성
        OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(OrderEnums.PaymentType.FREE.getCode());
        List<OrderBindDto> orderBindList = this.getOrderBindList(buyerVo, paymentType, cartDataDto, sessionShippingDto, cartSummaryDto);

        // then - 가격 검증
        testThenPrice(cartSummaryDto, orderBindList);

        // when - 주문 임시데이터 생성
        OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "Y");

        // then - 주문 임시데이터 생성
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

        // when - 주문 승인
        String odid = orderRegistrationResponseDto.getOdids().split(",")[0];

        // 주문번호로 주문데이터 조회
        PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

        // 승인처리
        PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
        paymentData.setApprovalDate(LocalDateTime.now());
        PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

        // then - 주문 승인
        assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

        // when - 주문정보 조회
        String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];
        ApiResult<?> orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        MallOrderDetailListResponseDto orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        List<MallOrderDetailGoodsDto> orderDetailInfoList = orderInfo.getOrderDetailList().stream()
                .map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList());
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderDetailInfoList.get(0).getOrderStatusCd());

        // then - 주문 결제 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderInfo.getPayInfo().getStatus());

        // when - 주문 전체 취소
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.CANCEL, true);
        mallNormalLogin();

        // then - 주문 전체 취소
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());
    }

    @Test
    @DisplayName("장바구니 주문(렌탈-임직원가)후_취소")
    void 렌탈2() throws Exception {
        // given - 정보설정
        mallEmployeeLogin();
        given_rental2();

        // given - 회원정보 조회
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        Long urUserId = Long.parseLong(buyerVo.getUrUserId());

        // given - 장바구니 등록
        List<Long> cartIdList = new ArrayList<>();
        for (Long goodsId : gGoodsIdList) {
            cartIdList.add(this.addCart(urUserId, goodsId, 1));
        }

        // given - 장바구니 데이터 조회
        List<CartDeliveryDto> cartDataDto = this.getCartDataList(cartIdList, buyerVo);

        GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();

        // 매장 정보
        CartStoreDto cartStoreDto = new CartStoreDto();
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            cartStoreDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
            cartStoreDto.setStoreArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
            cartStoreDto.setUrStoreScheduleId(gReqDto.getUrStoreScheduleId());
        }

        // when - 상품 주문가능 상태
        GetCartDataRequestDto cartDataReqDto = new GetCartDataRequestDto();
        cartDataReqDto.setUrUserId(urUserId);
        CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataReqDto, cartDataDto, cartStoreDto);

        // then - 상품 주문가능 상태
        assertNotNull(checkCartResponseDto);
        assertEquals(ShoppingEnums.ApplyPayment.SUCCESS, checkCartResponseDto.getResult());

        // when - 장바구니 집계 조회
        CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, gReqDto.getUsePoint());

        // when - 주문생성
        OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(OrderEnums.PaymentType.FREE.getCode());
        List<OrderBindDto> orderBindList = this.getOrderBindList(buyerVo, paymentType, cartDataDto, sessionShippingDto, cartSummaryDto);

        // then - 가격 검증
        testThenPrice(cartSummaryDto, orderBindList);

        // when - 주문 임시데이터 생성
        OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "Y");

        // then - 주문 임시데이터 생성
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

        // when - 주문 승인
        String odid = orderRegistrationResponseDto.getOdids().split(",")[0];

        // 주문번호로 주문데이터 조회
        PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

        // 승인처리
        PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
        paymentData.setApprovalDate(LocalDateTime.now());
        PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

        // then - 주문 승인
        assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

        // when - 주문정보 조회
        String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];
        ApiResult<?> orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        MallOrderDetailListResponseDto orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        List<MallOrderDetailGoodsDto> orderDetailInfoList = orderInfo.getOrderDetailList().stream()
                .map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList());
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderDetailInfoList.get(0).getOrderStatusCd());

        // then - 주문 결제 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderInfo.getPayInfo().getStatus());

        // when - 주문 전체 취소
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.CANCEL, true);
        mallEmployeeLogin();

        // then - 주문 전체 취소
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());
    }

    @Test
    @DisplayName("장바구니 주문(무형-일반회원가)후_전체취소")
    void 무형1() throws Exception {
        // given - 정보설정
        mallNormalLogin();
        given_incorporeity1();

        // given - 회원정보 조회
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        Long urUserId = Long.parseLong(buyerVo.getUrUserId());

        // given - 장바구니 등록
        List<Long> cartIdList = new ArrayList<>();
        for (Long goodsId : gGoodsIdList) {
            cartIdList.add(this.addCart(urUserId, goodsId, 1));
        }

        // given - 장바구니 데이터 조회
        List<CartDeliveryDto> cartDataDto = this.getCartDataList(cartIdList, buyerVo);

        GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();

        // 매장 정보
        CartStoreDto cartStoreDto = new CartStoreDto();
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            cartStoreDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
            cartStoreDto.setStoreArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
            cartStoreDto.setUrStoreScheduleId(gReqDto.getUrStoreScheduleId());
        }

        // when - 상품 주문가능 상태
        GetCartDataRequestDto cartDataReqDto = new GetCartDataRequestDto();
        cartDataReqDto.setUrUserId(urUserId);
        CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataReqDto, cartDataDto, cartStoreDto);

        // then - 상품 주문가능 상태
        assertNotNull(checkCartResponseDto);
        assertEquals(ShoppingEnums.ApplyPayment.SUCCESS, checkCartResponseDto.getResult());

        // when - 장바구니 집계 조회
        CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, gReqDto.getUsePoint());

        // when - 주문생성
        OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(OrderEnums.PaymentType.FREE.getCode());
        List<OrderBindDto> orderBindList = this.getOrderBindList(buyerVo, paymentType, cartDataDto, sessionShippingDto, cartSummaryDto);

        // then - 가격 검증
        testThenPrice(cartSummaryDto, orderBindList);

        // when - 주문 임시데이터 생성
        OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "Y");

        // then - 주문 임시데이터 생성
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

        // when - 주문 승인
        String odid = orderRegistrationResponseDto.getOdids().split(",")[0];

        // 주문번호로 주문데이터 조회
        PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

        // 승인처리
        PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setPgAccountType(PgEnums.PgAccountType.INICIS_BASIC);
        paymentData.setTid("123");
        paymentData.setAuthCode("123");
        paymentData.setCardNumber("123");
        paymentData.setCardQuotaInterest("N");
        paymentData.setCardQuota("");
        paymentData.setVirtualAccountNumber("123");
        paymentData.setBankName(PgEnums.InicisBankCode.INICIS_03.getCodeName());
        paymentData.setInfo("test");
        paymentData.setPaidDueDate(LocalDateTime.now());
        paymentData.setPaidHolder("test");
        paymentData.setPartCancelYn("Y");
        paymentData.setEscrowYn("N");
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setCashReceiptYn("N");
        paymentData.setCashReceiptType("");
        paymentData.setCashReceiptNo("");
        paymentData.setCashReceiptAuthNo("");
        paymentData.setResponseData("");
        PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

        // then - 주문 승인
        assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

        // then - 재고차감 여부
        int stockBeforeOrder = cartDataDto.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();
        int stockAfterOrder = getGoodsStock(gGoodsIdList.get(0));
        assertEquals(stockBeforeOrder, stockAfterOrder);    // 무형상품 - 재고 무제한 상품으로 설정

        // when - 주문정보 조회
        String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];
        ApiResult<?> orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        MallOrderDetailListResponseDto orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        List<MallOrderDetailGoodsDto> orderDetailInfoList = orderInfo.getOrderDetailList().stream()
                .map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList());
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderDetailInfoList.get(0).getOrderStatusCd());

        // then - 주문 결제 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderInfo.getPayInfo().getStatus());

        // when - 주문 전체 취소
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.CANCEL, true);
        mallEmployeeLogin();

        // then - 주문 전체 취소
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // then - PG 결제잔여금액 합
        OrderClaimRegisterRequestDto claimRequestDto = new OrderClaimRegisterRequestDto();
        claimRequestDto.setOdOrderId(Long.parseLong(odOrderId));
        RefundPaymentInfoDto payInfoDto = claimProcessMapper.getRefundPaymentInfo(claimRequestDto);
        assertEquals(payInfoDto.getRemaindPrice(), 0);

        // then - 재고추가 여부
        int stockAfterClaim = getGoodsStock(gGoodsIdList.get(0));
        assertEquals(stockBeforeOrder, stockAfterClaim);    // 무형상품 - 재고 무제한 상품으로 설정
    }

    @Test
    @DisplayName("장바구니 주문(무형-임직원가)후_전체취소")
    void 무형2() throws Exception {
        // given - 정보설정
        mallEmployeeLogin();
        given_incorporeity2();

        // given - 회원정보 조회
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        Long urUserId = Long.parseLong(buyerVo.getUrUserId());

        // given - 장바구니 등록
        List<Long> cartIdList = new ArrayList<>();
        for (Long goodsId : gGoodsIdList) {
            cartIdList.add(this.addCart(urUserId, goodsId, 1));
        }

        // given - 장바구니 데이터 조회
        List<CartDeliveryDto> cartDataDto = this.getCartDataList(cartIdList, buyerVo);

        GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();

        // 매장 정보
        CartStoreDto cartStoreDto = new CartStoreDto();
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            cartStoreDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
            cartStoreDto.setStoreArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
            cartStoreDto.setUrStoreScheduleId(gReqDto.getUrStoreScheduleId());
        }

        // when - 상품 주문가능 상태
        GetCartDataRequestDto cartDataReqDto = new GetCartDataRequestDto();
        cartDataReqDto.setUrUserId(urUserId);
        CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataReqDto, cartDataDto, cartStoreDto);

        // then - 상품 주문가능 상태
        assertNotNull(checkCartResponseDto);
        assertEquals(ShoppingEnums.ApplyPayment.SUCCESS, checkCartResponseDto.getResult());

        // when - 장바구니 집계 조회
        CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, gReqDto.getUsePoint());

        // when - 주문생성
        OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(OrderEnums.PaymentType.FREE.getCode());
        List<OrderBindDto> orderBindList = this.getOrderBindList(buyerVo, paymentType, cartDataDto, sessionShippingDto, cartSummaryDto);

        // then - 가격 검증
        testThenPrice(cartSummaryDto, orderBindList);

        // when - 주문 임시데이터 생성
        OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "Y");

        // then - 주문 임시데이터 생성
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

        // when - 주문 승인
        String odid = orderRegistrationResponseDto.getOdids().split(",")[0];

        // 주문번호로 주문데이터 조회
        PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

        // 승인처리
        PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setPgAccountType(PgEnums.PgAccountType.INICIS_BASIC);
        paymentData.setTid("123");
        paymentData.setAuthCode("123");
        paymentData.setCardNumber("123");
        paymentData.setCardQuotaInterest("N");
        paymentData.setCardQuota("");
        paymentData.setVirtualAccountNumber("123");
        paymentData.setBankName(PgEnums.InicisBankCode.INICIS_03.getCodeName());
        paymentData.setInfo("test");
        paymentData.setPaidDueDate(LocalDateTime.now());
        paymentData.setPaidHolder("test");
        paymentData.setPartCancelYn("Y");
        paymentData.setEscrowYn("N");
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setCashReceiptYn("N");
        paymentData.setCashReceiptType("");
        paymentData.setCashReceiptNo("");
        paymentData.setCashReceiptAuthNo("");
        paymentData.setResponseData("");
        PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

        // then - 주문 승인
        assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

        // then - 재고차감 여부
        int stockBeforeOrder = cartDataDto.stream()
                .mapToInt(vo -> vo.getShipping().stream()
                        .mapToInt(vo1 -> vo1.getGoods().stream()
                                .filter(vo2 -> Objects.equals(vo2.getIlGoodsId(), gGoodsIdList.get(0)))
                                .mapToInt(CartGoodsDto::getStockQty)
                                .sum()
                        ).sum()
                ).sum();
        int stockAfterOrder = getGoodsStock(gGoodsIdList.get(0));
        assertEquals(stockBeforeOrder, stockAfterOrder);    // 무형상품 - 재고 무제한 상품으로 설정

        // when - 주문정보 조회
        String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];
        ApiResult<?> orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        MallOrderDetailListResponseDto orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        List<MallOrderDetailGoodsDto> orderDetailInfoList = orderInfo.getOrderDetailList().stream()
                .map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList());
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderDetailInfoList.get(0).getOrderStatusCd());

        // then - 주문 결제 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderInfo.getPayInfo().getStatus());

        // when - 주문 전체 취소
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.CANCEL, true);
        mallEmployeeLogin();

        // then - 주문 전체 취소
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // then - PG 결제잔여금액 합
        OrderClaimRegisterRequestDto claimRequestDto = new OrderClaimRegisterRequestDto();
        claimRequestDto.setOdOrderId(Long.parseLong(odOrderId));
        RefundPaymentInfoDto payInfoDto = claimProcessMapper.getRefundPaymentInfo(claimRequestDto);
        assertEquals(payInfoDto.getRemaindPrice(), 0);

        // then - 재고추가 여부
        int stockAfterClaim = getGoodsStock(gGoodsIdList.get(0));
        assertEquals(stockBeforeOrder, stockAfterClaim);    // 무형상품 - 재고 무제한 상품으로 설정
    }

    @Test
    @DisplayName("관리자 엑셀 주문(가상계좌)후_전체취소")
    void 관리자엑셀1() throws Exception {
        // given - 정보설정
        bosLogin();
        given_admin1();

        // 검증용 상품정보 조회
        String excelUploadType = ExcelUploadEnums.ExcelUploadType.BOS_CREATE.getCode();
        List<OrderExcelResponseDto> excelGoodsList = orderExcelUploadFactory.getBoarCreateIlGoodsIdList(excelUploadType, gAdminExcelList);

        // 항목별 검증 진행
        excelGoodsList = (List<OrderExcelResponseDto>) orderExcelUploadFactory.getGoodsRowItemValidator(excelUploadType, excelGoodsList, null, null, null);

        // 업로드 현황 설정
        List<OrderExcelResponseDto> successVoList = new ArrayList<>();    // 성공리스트
        List<OrderExcelResponseDto> failVoList = new ArrayList<>();    // 실패리스트

        for (OrderExcelResponseDto dto : excelGoodsList) {
            if (dto.isSuccess()) {
                successVoList.add(dto);
            } else {
                failVoList.add(dto);
            }
        }

        // then - 상품주문가능상태
        assertEquals(gAdminExcelList.size(), successVoList.size());

        // 주문생성
        OrderCreateRequestDto orderCreateRequestDto = new OrderCreateRequestDto();
        orderCreateRequestDto.setUrUserId(1647314L);
        orderCreateRequestDto.setOrderType("INDIVIDUAL");
        orderCreateRequestDto.setBuyerNm("홍길동");
        orderCreateRequestDto.setBuyerHp("01112345678");
        orderCreateRequestDto.setBuyerMail("test@test.co.kr");
        orderCreateRequestDto.setPsPayCd("PAY_TP.VIRTUAL_BANK");
        orderCreateRequestDto.setFreeShippingPriceYn("N");
        orderCreateRequestDto.setBankCode("BANK_CODE.IBK");
        orderCreateRequestDto.setAccountNumber("000101011");
        orderCreateRequestDto.setHolderName("홍길동");

        List<OrderCreateDto> orderCreateList = new ArrayList<>();
        for (OrderExcelResponseDto responseDto : successVoList) {
            OrderCreateDto requestDto = new OrderCreateDto();
            requestDto.setRecvNm(responseDto.getRecvNm());
            requestDto.setRecvHp(responseDto.getRecvHp());
            requestDto.setRecvZipCd(responseDto.getRecvZipCd());
            requestDto.setRecvAddr1(responseDto.getRecvAddr1());
            requestDto.setRecvAddr2(responseDto.getRecvAddr2());
            requestDto.setItemCode(responseDto.getIlItemCd());
            requestDto.setItemBarcode(responseDto.getItemBarcode());
            requestDto.setGoodsId(responseDto.getGoodsId());
            requestDto.setIlGoodsId(Long.parseLong(responseDto.getIlGoodsId()));
            requestDto.setGoodsName(responseDto.getGoodsName());
            requestDto.setStorageMethodTypeCode(responseDto.getStorageMethodTypeCode());
            requestDto.setStorageMethodTypeName(responseDto.getStorageMethodTypeName());
            requestDto.setOrderCnt(responseDto.getOrderCnt());
            requestDto.setRecommendedPrice(responseDto.getRecommendedPrice());
            requestDto.setSalePrice(String.valueOf(responseDto.getSalePrice()));
            requestDto.setOrgSalePrice(responseDto.getOrgSalePrice());
            requestDto.setOrderAmt(responseDto.getOrderAmt());
            requestDto.setGrpShippingId(responseDto.getGrpShippingId());
            requestDto.setIlShippingTmplId(Long.parseLong(responseDto.getIlShippingTmplId()));
            requestDto.setUrWarehouseId(responseDto.getUrWarehouseId());
            requestDto.setGoodsTp(responseDto.getGoodsTp());
            orderCreateList.add(requestDto);
        }
        orderCreateRequestDto.setOrderCreateList(orderCreateList);

        // when - 주문생성
        int goodsInfoBeforeOrder = getGoodsStock(Long.parseLong(gAdminExcelList.get(0).getIlGoodsId()));
        ApiResult<?> apiResult = orderCreateBiz.addOrderCreate(orderCreateRequestDto);
        assertEquals(BaseEnums.Default.SUCCESS.getCode(), apiResult.getCode());

        BasicDataResponseDto resDto = (BasicDataResponseDto) apiResult.getData();
        String odOrderId = resDto.getOdOrderId();
        String odid = resDto.getOdid();

        // when - 주문 정보 조회
        ApiResult<?> orderInfoApiResult = orderDetailBiz.getOrderDetailGoodsList(odOrderId);
        OrderDetailGoodsListResponseDto orderDetailInfo = (OrderDetailGoodsListResponseDto) orderInfoApiResult.getData();
        OrderDetailShippingZoneListResponseDto orderShippingInfo = (OrderDetailShippingZoneListResponseDto) orderDetailBiz.getOrderDetailShippingZoneList(odOrderId, String.valueOf(orderDetailInfo.getRows().get(0).getOdShippingZoneId())).getData();
        OrderDetailPayListResponseDto orderPayInfo = (OrderDetailPayListResponseDto) orderDetailBiz.getOrderDetailPayList(odOrderId).getData();

        // then - 가격 검증
        testThenAdminPrice(orderDetailInfo.getRows(), orderShippingInfo, orderPayInfo);

        // 주문번호로 주문데이터 조회
        PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

        // 승인처리
        PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setPgAccountType(PgEnums.PgAccountType.INICIS_BASIC);
        paymentData.setTid("123");
        paymentData.setAuthCode("123");
        paymentData.setCardNumber("123");
        paymentData.setCardQuotaInterest("N");
        paymentData.setCardQuota("");
        paymentData.setVirtualAccountNumber("123");
        paymentData.setBankName(PgEnums.InicisBankCode.INICIS_03.getCodeName());
        paymentData.setInfo("test");
        paymentData.setPaidDueDate(LocalDateTime.now());
        paymentData.setPaidHolder("test");
        paymentData.setPartCancelYn("Y");
        paymentData.setEscrowYn("N");
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setCashReceiptYn("N");
        paymentData.setCashReceiptType("");
        paymentData.setCashReceiptNo("");
        paymentData.setCashReceiptAuthNo("");
        paymentData.setResponseData("");
        PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

        // then - 주문 승인
        assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

        // then - 재고차감 여부
        int goodsInfoAfterOrder = getGoodsStock(Long.parseLong(gAdminExcelList.get(0).getIlGoodsId()));
        assertTrue(goodsInfoBeforeOrder > goodsInfoAfterOrder);

        // when - 주문정보 조회
        orderInfoApiResult = orderDetailBiz.getOrderDetailGoodsList(odOrderId);
        orderDetailInfo = (OrderDetailGoodsListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_READY.getCode(), orderDetailInfo.getRows().get(0).getOrderStatusCode());

        // then - 주문 결제 상태 변경 여부
        orderPayInfo = (OrderDetailPayListResponseDto) orderDetailBiz.getOrderDetailPayList(odOrderId).getData();
        assertEquals(OrderEnums.OrderStatus.INCOM_READY.getCodeName(), orderPayInfo.getPayDetailList().get(0).getStatus());

        // when - 주문 전체 취소
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequestExcel(Long.parseLong(odOrderId), odid, orderDetailInfo, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.CANCEL);

        // then - 주문 전체 취소
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // then - PG 결제잔여금액 합
        OrderClaimRegisterRequestDto claimRequestDto = new OrderClaimRegisterRequestDto();
        claimRequestDto.setOdOrderId(Long.parseLong(odOrderId));
        RefundPaymentInfoDto payInfoDto = claimProcessMapper.getRefundPaymentInfo(claimRequestDto);
        assertEquals(0, payInfoDto.getRemaindPrice());

        // then - 재고추가 여부
        int goodsInfoAfterClaim = getGoodsStock(Long.parseLong(gAdminExcelList.get(0).getIlGoodsId()));
        assertEquals(goodsInfoBeforeOrder, goodsInfoAfterClaim);
    }

    @Test
    @DisplayName("관리자 엑셀 주문(비인증결제)후_전체취소")
    void 관리자엑셀2() throws Exception {
        // given - 정보설정
        bosLogin();
        given_admin2();

        // 검증용 상품정보 조회
        String excelUploadType = ExcelUploadEnums.ExcelUploadType.BOS_CREATE.getCode();
        List<OrderExcelResponseDto> excelGoodsList = orderExcelUploadFactory.getBoarCreateIlGoodsIdList(excelUploadType, gAdminExcelList);

        // 항목별 검증 진행
        excelGoodsList = (List<OrderExcelResponseDto>) orderExcelUploadFactory.getGoodsRowItemValidator(excelUploadType, excelGoodsList, null, null, null);

        // 업로드 현황 설정
        List<OrderExcelResponseDto> successVoList = new ArrayList<>();    // 성공리스트
        List<OrderExcelResponseDto> failVoList = new ArrayList<>();    // 실패리스트

        for (OrderExcelResponseDto dto : excelGoodsList) {
            if (dto.isSuccess()) {
                successVoList.add(dto);
            } else {
                failVoList.add(dto);
            }
        }

        // then - 상품주문가능상태
        assertEquals(gAdminExcelList.size(), successVoList.size());

        // 주문생성
        OrderCreateRequestDto orderCreateRequestDto = new OrderCreateRequestDto();
        orderCreateRequestDto.setOrderType("INDIVIDUAL");
        orderCreateRequestDto.setBuyerNm("홍길동");
        orderCreateRequestDto.setBuyerHp("01112345678");
        orderCreateRequestDto.setBuyerMail("test@test.co.kr");
        orderCreateRequestDto.setPsPayCd("PAY_TP.CARD");
        orderCreateRequestDto.setFreeShippingPriceYn("N");
        List<OrderCreateDto> orderCreateList = new ArrayList<>();
        for (OrderExcelResponseDto responseDto : successVoList) {
            OrderCreateDto requestDto = new OrderCreateDto();
            requestDto.setRecvNm(responseDto.getRecvNm());
            requestDto.setRecvHp(responseDto.getRecvHp());
            requestDto.setRecvZipCd(responseDto.getRecvZipCd());
            requestDto.setRecvAddr1(responseDto.getRecvAddr1());
            requestDto.setRecvAddr2(responseDto.getRecvAddr2());
            requestDto.setItemCode(responseDto.getIlItemCd());
            requestDto.setItemBarcode(responseDto.getItemBarcode());
            requestDto.setGoodsId(responseDto.getGoodsId());
            requestDto.setIlGoodsId(Long.parseLong(responseDto.getIlGoodsId()));
            requestDto.setGoodsName(responseDto.getGoodsName());
            requestDto.setStorageMethodTypeCode(responseDto.getStorageMethodTypeCode());
            requestDto.setStorageMethodTypeName(responseDto.getStorageMethodTypeName());
            requestDto.setOrderCnt(responseDto.getOrderCnt());
            requestDto.setRecommendedPrice(responseDto.getRecommendedPrice());
            requestDto.setSalePrice(String.valueOf(responseDto.getSalePrice()));
            requestDto.setOrgSalePrice(responseDto.getOrgSalePrice());
            requestDto.setOrderAmt(responseDto.getOrderAmt());
            requestDto.setGrpShippingId(responseDto.getGrpShippingId());
            requestDto.setIlShippingTmplId(Long.parseLong(responseDto.getIlShippingTmplId()));
            requestDto.setUrWarehouseId(responseDto.getUrWarehouseId());
            requestDto.setGoodsTp(responseDto.getGoodsTp());
            orderCreateList.add(requestDto);
        }
        orderCreateRequestDto.setOrderCreateList(orderCreateList);

        // when - 주문생성
        int goodsInfoBeforeOrder = getGoodsStock(Long.parseLong(gAdminExcelList.get(0).getIlGoodsId()));
        ApiResult<?> apiResult = orderCreateBiz.addOrderCreate(orderCreateRequestDto);
        assertEquals(BaseEnums.Default.SUCCESS.getCode(), apiResult.getCode());

        BasicDataResponseDto resDto = (BasicDataResponseDto) apiResult.getData();
        String odOrderId = resDto.getOdOrderId();
        String odid = resDto.getOdid();

        // when - 주문 정보 조회
        ApiResult<?> orderInfoApiResult = orderDetailBiz.getOrderDetailGoodsList(odOrderId);
        OrderDetailGoodsListResponseDto orderDetailInfo = (OrderDetailGoodsListResponseDto) orderInfoApiResult.getData();
        OrderDetailShippingZoneListResponseDto orderShippingInfo = (OrderDetailShippingZoneListResponseDto) orderDetailBiz.getOrderDetailShippingZoneList(odOrderId, String.valueOf(orderDetailInfo.getRows().get(0).getOdShippingZoneId())).getData();
        OrderDetailPayListResponseDto orderPayInfo = (OrderDetailPayListResponseDto) orderDetailBiz.getOrderDetailPayList(odOrderId).getData();

        // then - 가격 검증
        testThenAdminPrice(orderDetailInfo.getRows(), orderShippingInfo, orderPayInfo);

        // 주문번호로 주문데이터 조회
        PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

        // 승인처리
        PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setPgAccountType(PgEnums.PgAccountType.INICIS_BASIC);
        paymentData.setTid("123");
        paymentData.setAuthCode("123");
        paymentData.setCardNumber("123");
        paymentData.setCardQuotaInterest("N");
        paymentData.setCardQuota("");
        paymentData.setVirtualAccountNumber("123");
        paymentData.setBankName(PgEnums.InicisBankCode.INICIS_03.getCodeName());
        paymentData.setInfo("test");
        paymentData.setPaidDueDate(LocalDateTime.now());
        paymentData.setPaidHolder("test");
        paymentData.setPartCancelYn("Y");
        paymentData.setEscrowYn("N");
        paymentData.setApprovalDate(LocalDateTime.now());
        paymentData.setCashReceiptYn("N");
        paymentData.setCashReceiptType("");
        paymentData.setCashReceiptNo("");
        paymentData.setCashReceiptAuthNo("");
        paymentData.setResponseData("");
        PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

        // then - 주문 승인
        assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

        // then - 재고차감 여부
        int goodsInfoAfterOrder = getGoodsStock(Long.parseLong(gAdminExcelList.get(0).getIlGoodsId()));
        assertTrue(goodsInfoBeforeOrder > goodsInfoAfterOrder);

        // when - 주문정보 조회
        orderInfoApiResult = orderDetailBiz.getOrderDetailGoodsList(odOrderId);
        orderDetailInfo = (OrderDetailGoodsListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderDetailInfo.getRows().get(0).getOrderStatusCode());

        // then - 주문 결제 상태 변경 여부
        orderPayInfo = (OrderDetailPayListResponseDto) orderDetailBiz.getOrderDetailPayList(odOrderId).getData();
        assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCodeName(), orderPayInfo.getPayDetailList().get(0).getStatus());

        // when - 주문 전체 취소
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequestExcel(Long.parseLong(odOrderId), odid, orderDetailInfo, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.CANCEL);

        // then - 주문 전체 취소
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // then - PG 결제잔여금액 합
        OrderClaimRegisterRequestDto claimRequestDto = new OrderClaimRegisterRequestDto();
        claimRequestDto.setOdOrderId(Long.parseLong(odOrderId));
        RefundPaymentInfoDto payInfoDto = claimProcessMapper.getRefundPaymentInfo(claimRequestDto);
        assertEquals(0, payInfoDto.getRemaindPrice());

        // then - 재고추가 여부
        int goodsInfoAfterClaim = getGoodsStock(Long.parseLong(gAdminExcelList.get(0).getIlGoodsId()));
        assertEquals(goodsInfoBeforeOrder, goodsInfoAfterClaim);
    }

    @Test
    @DisplayName("외부몰 엑셀 주문후__배송처리후_전체반품(구매자귀책)")
    void 외부몰엑셀() throws Exception {
        // given - 정보설정
        given_outmall();

        // COLLECTION_MALL_ID별 그룹핑
        Map<String, Map<String, List<OutMallOrderDto>>> shippingZoneMap = gOutMallList.stream()
                .collect(
                        groupingBy(OutMallOrderDto::getCollectionMallId, LinkedHashMap::new,    // 1. 주문번호 COLLECTION_MALL_ID
                                //groupingBy(OutMallOrderDto::getGrpShippingZone, LinkedHashMap::new,     // 2. 배송지별 수취인 정보
                                groupingBy(OutMallOrderDto::getGrpShippingPrice, LinkedHashMap::new,           // 3. 배송정책별 BUNDLE_GRP
                                        toList()
                                )));
        for (String key : shippingZoneMap.keySet()) {
            Map<String, List<OutMallOrderDto>> shippingPriceMap = shippingZoneMap.get(key);

            List<OrderBindDto> orderBindList = collectionMallOrderBindBiz.orderDataBind(shippingPriceMap);

            // when - 주문 임시데이터 생성
            int goodsInfoBeforeOrder = getGoodsStock(Long.parseLong(gOutMallList.get(0).getIlGoodsId()));
            OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "Y");

            // then - 주문 임시데이터 생성
            assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

            String odid = orderRegistrationResponseDto.getOdids().split(",")[0];
            String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];

            // when - 재고반영
            List<StockOrderRequestDto> stockOrderReqDtoList = new ArrayList<>();
            StockOrderRequestDto stockOrderRequestDto = new StockOrderRequestDto();
            List<StockCheckOrderDetailDto> orderGoodsList = orderOrderBiz.getStockCheckOrderDetailList(StringUtil.nvlLong(odOrderId));
            for (StockCheckOrderDetailDto goods : orderGoodsList) {
                StockOrderRequestDto stockOrderReqDto = new StockOrderRequestDto();
                stockOrderReqDto.setIlGoodsId(goods.getIlGoodsId());
                stockOrderReqDto.setOrderQty(goods.getOrderCnt());
                stockOrderReqDto.setScheduleDt(StringUtil.nvl(goods.getShippingDt(), "2000-01-01"));
                stockOrderReqDto.setOrderYn("Y");
                stockOrderReqDto.setStoreYn("N");
                stockOrderReqDto.setMemo(String.valueOf(goods.getOdOrderDetlId()));
                stockOrderReqDtoList.add(stockOrderReqDto);
            }
            stockOrderRequestDto.setOrderList(stockOrderReqDtoList);
            ApiResult<?> stockRes = goodsStockOrderBiz.stockOrderHandle(stockOrderRequestDto);

            // 출고처 일자별 출고수량 업데이트
            orderOrderBiz.putWarehouseDailyShippingCount(orderRegistrationResponseDto.getOdOrderIds());

            // when - 주문 정보 조회
            ApiResult<?> orderInfoApiResult = orderDetailBiz.getOrderDetailGoodsList(odOrderId);
            OrderDetailGoodsListResponseDto orderDetailInfo = (OrderDetailGoodsListResponseDto) orderInfoApiResult.getData();
            OrderDetailShippingZoneListResponseDto orderShippingInfo = (OrderDetailShippingZoneListResponseDto) orderDetailBiz.getOrderDetailShippingZoneList(odOrderId, String.valueOf(orderDetailInfo.getRows().get(0).getOdShippingZoneId())).getData();
            OrderDetailPayListResponseDto orderPayInfo = (OrderDetailPayListResponseDto) orderDetailBiz.getOrderDetailPayList(odOrderId).getData();

            // then - 가격 검증
            testThenAdminPrice(orderDetailInfo.getRows(), orderShippingInfo, orderPayInfo);

            // 주문번호로 주문데이터 조회
            PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

            // 승인처리
            PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
            paymentData.setApprovalDate(LocalDateTime.now());
            paymentData.setPgAccountType(PgEnums.PgAccountType.INICIS_BASIC);
            paymentData.setTid("123");
            paymentData.setAuthCode("123");
            paymentData.setCardNumber("123");
            paymentData.setCardQuotaInterest("N");
            paymentData.setCardQuota("");
            paymentData.setVirtualAccountNumber("123");
            paymentData.setBankName(PgEnums.InicisBankCode.INICIS_03.getCodeName());
            paymentData.setInfo("test");
            paymentData.setPaidDueDate(LocalDateTime.now());
            paymentData.setPaidHolder("test");
            paymentData.setPartCancelYn("Y");
            paymentData.setEscrowYn("N");
            paymentData.setApprovalDate(LocalDateTime.now());
            paymentData.setCashReceiptYn("N");
            paymentData.setCashReceiptType("");
            paymentData.setCashReceiptNo("");
            paymentData.setCashReceiptAuthNo("");
            paymentData.setResponseData("");
            PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

            // then - 주문 승인
            assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

            // then - 재고차감 여부
            int goodsInfoAfterOrder = getGoodsStock(Long.parseLong(gOutMallList.get(0).getIlGoodsId()));
//            assertTrue(goodsInfoBeforeOrder > goodsInfoAfterOrder);

            // when - 주문정보 조회
            orderInfoApiResult = orderDetailBiz.getOrderDetailGoodsList(odOrderId);
            orderDetailInfo = (OrderDetailGoodsListResponseDto) orderInfoApiResult.getData();

            // then - 주문 상품 상태 변경 여부
            assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderDetailInfo.getRows().get(0).getOrderStatusCode());

            // then - 주문 결제 상태 변경 여부
            orderPayInfo = (OrderDetailPayListResponseDto) orderDetailBiz.getOrderDetailPayList(odOrderId).getData();
            assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCodeName(), orderPayInfo.getPayDetailList().get(0).getStatus());

            // when - 배송준비중 변경
            // 출고처 상태 변경
            bosLogin();
            WarehouseModifyDto warehouseModifyDto = new WarehouseModifyDto();
            warehouseModifyDto.setUrWarehouseId(String.valueOf(gUrWareHouseId));
            warehouseModifyDto.setWarehouseGroupCode("test");
            warehouseModifyDto.setInputWarehouseName("test");
            warehouseModifyDto.setInputCompanyName("test");
            warehouseModifyDto.setStockOrderYn("N");
            warehouseModifyDto.setStlmnYn("N");
            warehouseModifyDto.setOrderChangeType("test");
            warehouseModifyDto.setOrderStatusAlamYn("N");
            warehouseModifyDto.setHolidayGroupYn("N");
            warehouseModifyDto.setHour("01");
            warehouseModifyDto.setMinute("10");
            warehouseModifyDto.setDawnDlvryYn("N");
            warehouseModifyDto.setStoreYn("N");
            warehouseModifyDto.setReceiverZipCode("test");
            warehouseModifyDto.setReceiverAddress1("test");
            warehouseModifyDto.setReceiverAddress2("test");
            warehouseModifyDto.setUndeliverableAreaTp("test");
            warehouseModifyDto.setDawnUndeliverableAreaTp("test");
            warehouseModifyDto.setWarehouseMemo("test");
            warehouseModifyDto.setOrderChangeType("ORDER_CHANGE_TP.ORDER_CHANGE");
            ApiResult<?> warehouseResult = warehouseBiz.putWarehouse(warehouseModifyDto);

            assertEquals(BaseEnums.Default.SUCCESS.getCode(), warehouseResult.getCode());

            UserVo userVo = SessionUtil.getBosUserVO();

            List<Long> detlIdList = new ArrayList<>();
            for (OrderDetailGoodsListDto orderDetail : orderDetailInfo.getRows()) {
                if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderDetail.getGoodsTpCd())) {
                    continue;
                }
                detlIdList.add((long) orderDetail.getOdOrderDetlId());
            }

            OrderStatusUpdateRequestDto orderStatusUpdateRequestDto = OrderStatusUpdateRequestDto.builder()
                    .userId(Long.parseLong(userVo.getUserId()))
                    .loginName(userVo.getLoginName())
                    .detlIdList(detlIdList)
                    .shippingCompIdList(null)
                    .trackingNoList(null)
                    .statusCd(OrderEnums.OrderStatus.DELIVERY_READY.getCode())
                    .build();

            ApiResult<?> orderStatusDR = orderStatusBiz.putOrderDetailListStatus(orderStatusUpdateRequestDto);

            // then - 배송준비중 변경
            assertEquals(BaseEnums.Default.SUCCESS.getCode(), orderStatusDR.getCode());

            // when - 배송중 변경
            orderStatusUpdateRequestDto = OrderStatusUpdateRequestDto.builder()
                    .userId(Long.parseLong(userVo.getUserId()))
                    .loginName(userVo.getLoginName())
                    .detlIdList(detlIdList)
                    .shippingCompIdList(Collections.singletonList(1L))
                    .trackingNoList(Collections.singletonList("1"))
                    .statusCd(OrderEnums.OrderStatus.DELIVERY_ING.getCode())
                    .build();

            ApiResult<?> orderStatusDI = orderStatusBiz.putOrderDetailListStatus(orderStatusUpdateRequestDto);

            // then - 배송중 변경
            assertEquals(BaseEnums.Default.SUCCESS.getCode(), orderStatusDI.getCode());

            // when - 주문반품
            ClaimRequestProcessDto claimRequestProcessDto = this.claimRequestExcel(Long.parseLong(odOrderId), odid, orderDetailInfo, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.RETURN);

            // then - 주문반품 확인
            assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

            // then - PG 결제잔여금액 합
            OrderClaimRegisterRequestDto claimRequestDto = new OrderClaimRegisterRequestDto();
            claimRequestDto.setOdOrderId(Long.parseLong(odOrderId));
            RefundPaymentInfoDto payInfoDto = claimProcessMapper.getRefundPaymentInfo(claimRequestDto);
            assertEquals(0, payInfoDto.getRemaindPrice());

            // then - 재고추가 여부
            int goodsInfoAfterClaim = getGoodsStock(Long.parseLong(gOutMallList.get(0).getIlGoodsId()));
//            assertEquals(goodsInfoBeforeOrder, goodsInfoAfterClaim);
        }

    }

    @Test
    @DisplayName("ezAdmin 주문후__배송처리후_부분반품(판매자귀책)")
    void ezAdmin() throws Exception {
        // given - 정보설정
        given_ezAdmin();

        // COLLECTION_MALL_ID별 그룹핑
        Map<String, Map<String, List<OutMallOrderDto>>> shippingZoneMap = gOutMallList.stream()
                .collect(
                        groupingBy(OutMallOrderDto::getCollectionMallId, LinkedHashMap::new,    // 1. 주문번호 COLLECTION_MALL_ID
                                //groupingBy(OutMallOrderDto::getGrpShippingZone, LinkedHashMap::new,     // 2. 배송지별 수취인 정보
                                groupingBy(OutMallOrderDto::getGrpShippingPrice, LinkedHashMap::new,           // 3. 배송정책별 BUNDLE_GRP
                                        toList()
                                )));
        for (String key : shippingZoneMap.keySet()) {
            Map<String, List<OutMallOrderDto>> shippingPriceMap = shippingZoneMap.get(key);

            List<OrderBindDto> orderBindList = collectionMallOrderBindBiz.orderDataBind(shippingPriceMap);

            // when - 주문 임시데이터 생성
            int goodsInfoBeforeOrder = getGoodsStock(Long.parseLong(gOutMallList.get(0).getIlGoodsId()));
            OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "Y");

            // then - 주문 임시데이터 생성
            assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

            String odid = orderRegistrationResponseDto.getOdids().split(",")[0];
            String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];

            // when - 재고반영
            List<StockOrderRequestDto> stockOrderReqDtoList = new ArrayList<>();
            StockOrderRequestDto stockOrderRequestDto = new StockOrderRequestDto();
            List<StockCheckOrderDetailDto> orderGoodsList = orderOrderBiz.getStockCheckOrderDetailList(StringUtil.nvlLong(odOrderId));
            for (StockCheckOrderDetailDto goods : orderGoodsList) {
                StockOrderRequestDto stockOrderReqDto = new StockOrderRequestDto();
                stockOrderReqDto.setIlGoodsId(goods.getIlGoodsId());
                stockOrderReqDto.setOrderQty(goods.getOrderCnt());
                stockOrderReqDto.setScheduleDt(StringUtil.nvl(goods.getShippingDt(), "2000-01-01"));
                stockOrderReqDto.setOrderYn("Y");
                stockOrderReqDto.setStoreYn("N");
                stockOrderReqDto.setMemo(String.valueOf(goods.getOdOrderDetlId()));
                stockOrderReqDtoList.add(stockOrderReqDto);
            }
            stockOrderRequestDto.setOrderList(stockOrderReqDtoList);
            ApiResult<?> stockRes = goodsStockOrderBiz.stockOrderHandle(stockOrderRequestDto);

            // 출고처 일자별 출고수량 업데이트
            orderOrderBiz.putWarehouseDailyShippingCount(orderRegistrationResponseDto.getOdOrderIds());

            // when - 주문 정보 조회
            ApiResult<?> orderInfoApiResult = orderDetailBiz.getOrderDetailGoodsList(odOrderId);
            OrderDetailGoodsListResponseDto orderDetailInfo = (OrderDetailGoodsListResponseDto) orderInfoApiResult.getData();
            OrderDetailShippingZoneListResponseDto orderShippingInfo = (OrderDetailShippingZoneListResponseDto) orderDetailBiz.getOrderDetailShippingZoneList(odOrderId, String.valueOf(orderDetailInfo.getRows().get(0).getOdShippingZoneId())).getData();
            OrderDetailPayListResponseDto orderPayInfo = (OrderDetailPayListResponseDto) orderDetailBiz.getOrderDetailPayList(odOrderId).getData();

            // then - 가격 검증
            testThenAdminPrice(orderDetailInfo.getRows(), orderShippingInfo, orderPayInfo);

            // 주문번호로 주문데이터 조회
            PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

            // 승인처리
            PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
            paymentData.setApprovalDate(LocalDateTime.now());
            paymentData.setPgAccountType(PgEnums.PgAccountType.INICIS_BASIC);
            paymentData.setTid("123");
            paymentData.setAuthCode("123");
            paymentData.setCardNumber("123");
            paymentData.setCardQuotaInterest("N");
            paymentData.setCardQuota("");
            paymentData.setVirtualAccountNumber("123");
            paymentData.setBankName(PgEnums.InicisBankCode.INICIS_03.getCodeName());
            paymentData.setInfo("test");
            paymentData.setPaidDueDate(LocalDateTime.now());
            paymentData.setPaidHolder("test");
            paymentData.setPartCancelYn("Y");
            paymentData.setEscrowYn("N");
            paymentData.setApprovalDate(LocalDateTime.now());
            paymentData.setCashReceiptYn("N");
            paymentData.setCashReceiptType("");
            paymentData.setCashReceiptNo("");
            paymentData.setCashReceiptAuthNo("");
            paymentData.setResponseData("");
            PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

            // then - 주문 승인
            assertEquals(PgEnums.PgErrorType.SUCCESS.getCode(), putResDto.getResult().getCode());

            // then - 재고차감 여부
            int goodsInfoAfterOrder = getGoodsStock(Long.parseLong(gOutMallList.get(0).getIlGoodsId()));
//            assertTrue(goodsInfoBeforeOrder > goodsInfoAfterOrder);

            // when - 주문정보 조회
            orderInfoApiResult = orderDetailBiz.getOrderDetailGoodsList(odOrderId);
            orderDetailInfo = (OrderDetailGoodsListResponseDto) orderInfoApiResult.getData();

            // then - 주문 상품 상태 변경 여부
            assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), orderDetailInfo.getRows().get(0).getOrderStatusCode());

            // then - 주문 결제 상태 변경 여부
            orderPayInfo = (OrderDetailPayListResponseDto) orderDetailBiz.getOrderDetailPayList(odOrderId).getData();
            assertEquals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCodeName(), orderPayInfo.getPayDetailList().get(0).getStatus());

            // when - 배송준비중 변경
            // 출고처 상태 변경
            bosLogin();
            WarehouseModifyDto warehouseModifyDto = new WarehouseModifyDto();
            warehouseModifyDto.setUrWarehouseId(String.valueOf(gUrWareHouseId));
            warehouseModifyDto.setWarehouseGroupCode("test");
            warehouseModifyDto.setInputWarehouseName("test");
            warehouseModifyDto.setInputCompanyName("test");
            warehouseModifyDto.setStockOrderYn("N");
            warehouseModifyDto.setStlmnYn("N");
            warehouseModifyDto.setOrderChangeType("test");
            warehouseModifyDto.setOrderStatusAlamYn("N");
            warehouseModifyDto.setHolidayGroupYn("N");
            warehouseModifyDto.setHour("01");
            warehouseModifyDto.setMinute("10");
            warehouseModifyDto.setDawnDlvryYn("N");
            warehouseModifyDto.setStoreYn("N");
            warehouseModifyDto.setReceiverZipCode("test");
            warehouseModifyDto.setReceiverAddress1("test");
            warehouseModifyDto.setReceiverAddress2("test");
            warehouseModifyDto.setUndeliverableAreaTp("test");
            warehouseModifyDto.setDawnUndeliverableAreaTp("test");
            warehouseModifyDto.setWarehouseMemo("test");
            warehouseModifyDto.setOrderChangeType("ORDER_CHANGE_TP.ORDER_CHANGE");
            ApiResult<?> warehouseResult = warehouseBiz.putWarehouse(warehouseModifyDto);

            assertEquals(BaseEnums.Default.SUCCESS.getCode(), warehouseResult.getCode());

            UserVo userVo = SessionUtil.getBosUserVO();

            List<Long> detlIdList = new ArrayList<>();
            for (OrderDetailGoodsListDto orderDetail : orderDetailInfo.getRows()) {
                if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderDetail.getGoodsTpCd())) {
                    continue;
                }
                detlIdList.add((long) orderDetail.getOdOrderDetlId());
            }

            OrderStatusUpdateRequestDto orderStatusUpdateRequestDto = OrderStatusUpdateRequestDto.builder()
                    .userId(Long.parseLong(userVo.getUserId()))
                    .loginName(userVo.getLoginName())
                    .detlIdList(detlIdList)
                    .shippingCompIdList(null)
                    .trackingNoList(null)
                    .statusCd(OrderEnums.OrderStatus.DELIVERY_READY.getCode())
                    .build();

            ApiResult<?> orderStatusDR = orderStatusBiz.putOrderDetailListStatus(orderStatusUpdateRequestDto);

            // then - 배송준비중 변경
            assertEquals(BaseEnums.Default.SUCCESS.getCode(), orderStatusDR.getCode());

            // when - 배송중 변경
            orderStatusUpdateRequestDto = OrderStatusUpdateRequestDto.builder()
                    .userId(Long.parseLong(userVo.getUserId()))
                    .loginName(userVo.getLoginName())
                    .detlIdList(detlIdList)
                    .shippingCompIdList(Collections.singletonList(1L))
                    .trackingNoList(Collections.singletonList("1"))
                    .statusCd(OrderEnums.OrderStatus.DELIVERY_ING.getCode())
                    .build();

            ApiResult<?> orderStatusDI = orderStatusBiz.putOrderDetailListStatus(orderStatusUpdateRequestDto);

            // then - 배송중 변경
            assertEquals(BaseEnums.Default.SUCCESS.getCode(), orderStatusDI.getCode());

            // when - 주문반품
            ClaimRequestProcessDto claimRequestProcessDto = this.claimRequestExcel(Long.parseLong(odOrderId), odid, orderDetailInfo, ClaimTestType.PART, OrderClaimEnums.ClaimStatusTp.RETURN);

            // then - 주문반품 확인
            assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

            // then - PG 결제잔여금액 합
            OrderClaimRegisterRequestDto claimRequestDto = new OrderClaimRegisterRequestDto();
            claimRequestDto.setOdOrderId(Long.parseLong(odOrderId));
            RefundPaymentInfoDto payInfoDto = claimProcessMapper.getRefundPaymentInfo(claimRequestDto);
            assertTrue(payInfoDto.getRemaindPrice() > 0);

            // then - 재고추가 여부
            int goodsInfoAfterClaim = getGoodsStock(Long.parseLong(gOutMallList.get(0).getIlGoodsId()));
//            assertEquals(goodsInfoBeforeOrder, goodsInfoAfterClaim);
        }
    }

    @Test
    @DisplayName("정기배송 신청 후_1회차 주문생성_및_1회차 주문 전체 취소")
    void 정기배송1() throws Exception {
        // given - 정보설정
        mallNormalLogin();
        given_regular1();

        // given - 회원정보 조회
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        Long urUserId = Long.parseLong(buyerVo.getUrUserId());

        // given - 장바구니 등록
        List<Long> cartIdList = new ArrayList<>();
        for (Long goodsId : gGoodsIdList) {
            cartIdList.add(this.addCart(urUserId, goodsId, 1));
        }

        // given - 장바구니 데이터 조회
        List<CartDeliveryDto> cartDataDto = this.getCartDataList(cartIdList, buyerVo);
        GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();

        // 매장 정보
        CartStoreDto cartStoreDto = new CartStoreDto();
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            cartStoreDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
            cartStoreDto.setStoreArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
            cartStoreDto.setUrStoreScheduleId(gReqDto.getUrStoreScheduleId());
        }

        // when - 상품 주문가능 상태
        GetCartDataRequestDto cartDataReqDto = new GetCartDataRequestDto();
        cartDataReqDto.setUrUserId(urUserId);
        CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataReqDto, cartDataDto, cartStoreDto);

        // then - 상품 주문가능 상태
        assertNotNull(checkCartResponseDto);
        assertEquals(ShoppingEnums.ApplyPayment.SUCCESS, checkCartResponseDto.getResult());

        // when - 장바구니 집계 조회
        CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, gReqDto.getUsePoint());

        // then - 가격 검증
        testThenPrice(cartSummaryDto, null);

        // 임시 주문 생성 등록
        CartBuyerDto cartBuyerDto = new CartBuyerDto();
        cartBuyerDto.setUrUserId(Long.parseLong(buyerVo.getUrUserId()));
        cartBuyerDto.setUrGroupId(buyerVo.getUrGroupId());
        cartBuyerDto.setUrEmployeeCd(buyerVo.getUrErpEmployeeCode());
        cartBuyerDto.setGuestCi("");
        cartBuyerDto.setBuyerName(buyerVo.getUserName());
        cartBuyerDto.setBuyerMobile(buyerVo.getUserMobile());
        cartBuyerDto.setBuyerEmail(buyerVo.getUserEmail());
        cartBuyerDto.setBuyerType(UserEnums.BuyerType.USER);

        cartBuyerDto.setPaymentType(OrderEnums.PaymentType.findByCode(OrderEnums.PaymentType.FREE.getCode()));
        cartBuyerDto.setAgentType(SystemEnums.AgentType.PC);
        cartBuyerDto.setUrPcidCd(PCID_ID);
        //환불 계좌 정보
        cartBuyerDto.setBankCode("1");
        cartBuyerDto.setAccountNumber("test");
        cartBuyerDto.setHolderName("test");

        CartRegularDto cartRegularDto = new CartRegularDto();
        cartRegularDto.setCycleType(OrderEnums.RegularShippingCycle.WEEK1.getCode());
        cartRegularDto.setCycleTermType(OrderEnums.RegularShippingCycleTerm.MONTH1.getCode());
        cartRegularDto.setArrivalScheduledDate(LocalDate.now().plusDays(3));

        CreateOrderCartDto createOrderCartDto = new CreateOrderCartDto();
        createOrderCartDto.setBuyer(cartBuyerDto);
        createOrderCartDto.setShippingZone(sessionShippingDto);
        createOrderCartDto.setCartList(cartDataDto);
        createOrderCartDto.setCartSummary(cartSummaryDto);
        createOrderCartDto.setRegular(cartRegularDto);

        // when - 정기배송 신청
        ApplyRegularResponseDto applyRegularResponseDto = orderRegularBiz.applyRegular(createOrderCartDto);

        // then - 정기배송 신청
        assertTrue(applyRegularResponseDto.isResult());

        // when - 정기배송 정보조회
        List<RegularResultCreateOrderListDto> regularOrderResultCreateGoodsList = orderRegularOrderCreateBiz.getRegularOrderResultCreateGoodsList(0).stream()
                .filter(v -> v.getUrPcidCd().equals(PCID_ID))
                .collect(toList());

        // 정기배송주문결과PK, 출고처 별 grouping
        Map<Long, Map<String, List<RegularResultCreateOrderListDto>>> resultMap = regularOrderResultCreateGoodsList.stream()
                .filter(data -> data.getParentIlGoodsId() == 0)
                .collect(Collectors.groupingBy(RegularResultCreateOrderListDto::getOdRegularResultId, LinkedHashMap::new,
                        Collectors.groupingBy(RegularResultCreateOrderListDto::getGrpWarehouseShippingTmplId, LinkedHashMap::new, Collectors.toList())));

        // 정기배송결과PK 주문상세 forEach
        Long odRegularResultId = 0L;
        Map<String, List<RegularResultCreateOrderListDto>> entryData = null;
        for (Long key : resultMap.keySet()) {
            odRegularResultId = key;
            entryData = resultMap.get(key);
        }

        Long finalOdRegularResultId = odRegularResultId;
        List<RegularResultCreateOrderListDto> regularGoodsList = regularOrderResultCreateGoodsList.stream()
                .filter(data -> data.getOdRegularResultId() == finalOdRegularResultId)
                .collect(Collectors.toList());

        // 정기배송 주문 생성 처리
        int addDiscountStdReqRound = 1;
        Map<Long, List<RegularResultCreateOrderListDto>> childGoodsList = regularGoodsList.stream()
                .filter(data -> data.getOdRegularResultId() == finalOdRegularResultId && data.getParentIlGoodsId() != 0 && OrderEnums.RegularDetlSaleStatusCd.ON_SALE.getCode().equals(data.getSaleStatus()))
                .collect(Collectors.groupingBy(RegularResultCreateOrderListDto::getParentIlGoodsId, LinkedHashMap::new, Collectors.toList()));

        // when - 정기배송 주문 생성
        RegularResultCreateOrderGoodsListDto createOrderGoodsList = new RegularResultCreateOrderGoodsListDto();
        createOrderGoodsList.setOdRegularResultId(odRegularResultId);
        createOrderGoodsList.setAddDiscountStdReqRound(addDiscountStdReqRound);
        createOrderGoodsList.setGoodsList(entryData);
        createOrderGoodsList.setChildGoodsList(childGoodsList);

        // 주문서 생성용 데이터 Bind
        List<OrderBindDto> orderBindList = orderBindRegularBiz.orderDataBind(createOrderGoodsList);

        // when - 주문 임시데이터 생성
        OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "N");

        // then - 주문 임시데이터 생성
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

        // when - 주문정보 조회
        String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];
        String odid = orderRegistrationResponseDto.getOdids().split(",")[0];
        ApiResult<?> orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        MallOrderDetailListResponseDto orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        List<MallOrderDetailGoodsDto> orderDetailInfoList = orderInfo.getOrderDetailList().stream()
                .map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList());
        assertEquals(OrderEnums.OrderStatus.INCOM_READY.getCode(), orderDetailInfoList.get(0).getOrderStatusCd());

        // then - 주문 결제 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_READY.getCode(), orderInfo.getPayInfo().getStatus());

        // when - 주문 전체 취소
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.CANCEL, false);
        mallNormalLogin();

        // then - 주문 전체 취소
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // then - PG 결제잔여금액 합
        OrderClaimRegisterRequestDto claimRequestDto = new OrderClaimRegisterRequestDto();
        claimRequestDto.setOdOrderId(Long.parseLong(odOrderId));
        RefundPaymentInfoDto payInfoDto = claimProcessMapper.getRefundPaymentInfo(claimRequestDto);
        assertTrue(payInfoDto.getRemaindPrice() > 0);
    }

    @Test
    @DisplayName("정기배송 추가신청 후_추가할인 회차 주문생성_및_전체 취소_후_주문 정기배송 신청 전체 취소")
    void 정기배송2() throws Exception {
        // given - 정보설정
        mallNormalLogin();
        given_regular2();

        // given - 회원정보 조회
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        Long urUserId = Long.parseLong(buyerVo.getUrUserId());

        // given - 장바구니 등록
        List<Long> cartIdList = new ArrayList<>();
        for (Long goodsId : gGoodsIdList) {
            cartIdList.add(this.addCart(urUserId, goodsId, 1));
        }

        // given - 장바구니 데이터 조회
        List<CartDeliveryDto> cartDataDto = this.getCartDataList(cartIdList, buyerVo);
        GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();

        // 매장 정보
        CartStoreDto cartStoreDto = new CartStoreDto();
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            cartStoreDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
            cartStoreDto.setStoreArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
            cartStoreDto.setUrStoreScheduleId(gReqDto.getUrStoreScheduleId());
        }

        // when - 상품 주문가능 상태
        GetCartDataRequestDto cartDataReqDto = new GetCartDataRequestDto();
        cartDataReqDto.setUrUserId(urUserId);
        CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataReqDto, cartDataDto, cartStoreDto);

        // then - 상품 주문가능 상태
        assertNotNull(checkCartResponseDto);
        assertEquals(ShoppingEnums.ApplyPayment.SUCCESS, checkCartResponseDto.getResult());

        // when - 장바구니 집계 조회
        CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, gReqDto.getUsePoint());

        // then - 가격 검증
        testThenPrice(cartSummaryDto, null);

        // 임시 주문 생성 등록
        CartBuyerDto cartBuyerDto = new CartBuyerDto();
        cartBuyerDto.setUrUserId(Long.parseLong(buyerVo.getUrUserId()));
        cartBuyerDto.setUrGroupId(buyerVo.getUrGroupId());
        cartBuyerDto.setUrEmployeeCd(buyerVo.getUrErpEmployeeCode());
        cartBuyerDto.setGuestCi("");
        cartBuyerDto.setBuyerName(buyerVo.getUserName());
        cartBuyerDto.setBuyerMobile(buyerVo.getUserMobile());
        cartBuyerDto.setBuyerEmail(buyerVo.getUserEmail());
        cartBuyerDto.setBuyerType(UserEnums.BuyerType.USER);

        cartBuyerDto.setPaymentType(OrderEnums.PaymentType.findByCode(OrderEnums.PaymentType.FREE.getCode()));
        cartBuyerDto.setAgentType(SystemEnums.AgentType.PC);
        cartBuyerDto.setUrPcidCd(PCID_ID);
        //환불 계좌 정보
        cartBuyerDto.setBankCode("1");
        cartBuyerDto.setAccountNumber("test");
        cartBuyerDto.setHolderName("test");

        CartRegularDto cartRegularDto = new CartRegularDto();
        cartRegularDto.setCycleType(OrderEnums.RegularShippingCycle.WEEK1.getCode());
        cartRegularDto.setCycleTermType(OrderEnums.RegularShippingCycleTerm.MONTH1.getCode());
        cartRegularDto.setArrivalScheduledDate(LocalDate.now().plusDays(3));

        CreateOrderCartDto createOrderCartDto = new CreateOrderCartDto();
        createOrderCartDto.setBuyer(cartBuyerDto);
        createOrderCartDto.setShippingZone(sessionShippingDto);
        createOrderCartDto.setCartList(cartDataDto);
        createOrderCartDto.setCartSummary(cartSummaryDto);
        createOrderCartDto.setRegular(cartRegularDto);

        // when - 정기배송 신청
        ApplyRegularResponseDto applyRegularResponseDto = orderRegularBiz.applyRegular(createOrderCartDto);

        // then - 정기배송 신청
        assertTrue(applyRegularResponseDto.isResult());

        // when - 정기배송 추가 신청
        applyRegularResponseDto = orderRegularBiz.applyRegular(createOrderCartDto);

        // then - 정기배송 추가 신청
        assertTrue(applyRegularResponseDto.isResult());

        // when - 정기배송 정보조회
        List<RegularResultCreateOrderListDto> regularOrderResultCreateGoodsList = orderRegularOrderCreateBiz.getRegularOrderResultCreateGoodsList(0).stream()
                .filter(v -> v.getUrPcidCd().equals(PCID_ID))
                .collect(toList());

        // 정기배송주문결과PK, 출고처 별 grouping
        Map<Long, Map<String, List<RegularResultCreateOrderListDto>>> resultMap = regularOrderResultCreateGoodsList.stream()
                .filter(data -> data.getParentIlGoodsId() == 0)
                .collect(Collectors.groupingBy(RegularResultCreateOrderListDto::getOdRegularResultId, LinkedHashMap::new,
                        Collectors.groupingBy(RegularResultCreateOrderListDto::getGrpWarehouseShippingTmplId, LinkedHashMap::new, Collectors.toList())));

        // 정기배송결과PK 주문상세 forEach
        Long odRegularResultId = 0L;
        Map<String, List<RegularResultCreateOrderListDto>> entryData = null;
        for (Long key : resultMap.keySet()) {
            odRegularResultId = key;
            entryData = resultMap.get(key);
        }

        Long finalOdRegularResultId = odRegularResultId;
        List<RegularResultCreateOrderListDto> regularGoodsList = regularOrderResultCreateGoodsList.stream()
                .filter(data -> data.getOdRegularResultId() == finalOdRegularResultId)
                .collect(Collectors.toList());

        // 정기배송 주문 생성 처리
        int addDiscountStdReqRound = 1;
        Map<Long, List<RegularResultCreateOrderListDto>> childGoodsList = regularGoodsList.stream()
                .filter(data -> data.getOdRegularResultId() == finalOdRegularResultId && data.getParentIlGoodsId() != 0 && OrderEnums.RegularDetlSaleStatusCd.ON_SALE.getCode().equals(data.getSaleStatus()))
                .collect(Collectors.groupingBy(RegularResultCreateOrderListDto::getParentIlGoodsId, LinkedHashMap::new, Collectors.toList()));

        // when - 정기배송 주문 생성
        RegularResultCreateOrderGoodsListDto createOrderGoodsList = new RegularResultCreateOrderGoodsListDto();
        createOrderGoodsList.setOdRegularResultId(odRegularResultId);
        createOrderGoodsList.setAddDiscountStdReqRound(addDiscountStdReqRound);
        createOrderGoodsList.setGoodsList(entryData);
        createOrderGoodsList.setChildGoodsList(childGoodsList);

        // 주문서 생성용 데이터 Bind
        List<OrderBindDto> orderBindList = orderBindRegularBiz.orderDataBind(createOrderGoodsList);

        // when - 주문 임시데이터 생성
        OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "N");

        // then - 주문 임시데이터 생성
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, orderRegistrationResponseDto.getOrderRegistrationResult());

        // when - 주문정보 조회
        String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];
        String odid = orderRegistrationResponseDto.getOdids().split(",")[0];
        ApiResult<?> orderInfoApiResult = mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
        MallOrderDetailListResponseDto orderInfo = (MallOrderDetailListResponseDto) orderInfoApiResult.getData();

        // then - 주문 상품 상태 변경 여부
        List<MallOrderDetailGoodsDto> orderDetailInfoList = orderInfo.getOrderDetailList().stream()
                .map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList());
        assertEquals(OrderEnums.OrderStatus.INCOM_READY.getCode(), orderDetailInfoList.get(0).getOrderStatusCd());

        // then - 주문 결제 상태 변경 여부
        assertEquals(OrderEnums.OrderStatus.INCOM_READY.getCode(), orderInfo.getPayInfo().getStatus());

        // when - 주문 전체 취소
        ClaimRequestProcessDto claimRequestProcessDto = this.claimRequest(orderInfo, orderRegistrationResponseDto, odid, ClaimTestType.ALL, OrderClaimEnums.ClaimStatusTp.CANCEL, false);
        mallNormalLogin();

        // then - 주문 전체 취소
        assertEquals(OrderEnums.OrderRegistrationResult.SUCCESS, claimRequestProcessDto.getClaimResult());

        // then - PG 결제잔여금액 합
        OrderClaimRegisterRequestDto claimRequestDto = new OrderClaimRegisterRequestDto();
        claimRequestDto.setOdOrderId(Long.parseLong(odOrderId));
        RefundPaymentInfoDto payInfoDto = claimProcessMapper.getRefundPaymentInfo(claimRequestDto);
        assertTrue(payInfoDto.getRemaindPrice() > 0);

        // when - 정기배송 정보 조회
        RegularReqGoodsListRequestDto regularReqGoodsListRequestDto = new RegularReqGoodsListRequestDto();
        regularReqGoodsListRequestDto.setUrUserId(urUserId);
        MallRegularReqInfoResponseDto regularInfo = orderRegularBiz.getOrderRegularReqInfo(regularReqGoodsListRequestDto);
        List<Long> odRegularResultDetlIdList = regularInfo.getReqRoundList().stream()
                .map(RegularResultReqRoundListDto::getShippingZoneList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(RegularResultShippingZoneListDto::getGoodsList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(RegularResultGoodsListDto::getOdRegularResultDetlId)
                .collect(Collectors.toList());

        // when - 정기배송 상품 취소
//        for (Long odRegularResultDetlId:odRegularResultDetlIdList) {
//            orderRegularService.putOrderRegularGoodsCancel(odRegularResultDetlId, Long.parseLong(buyerVo.getUrUserId()));
//        }
    }

    private int getGoodsStock(Long ilGoodsId) throws Exception {
        // 상품 정보 조회
        GoodsRequestDto goodsRequestDto = new GoodsRequestDto();
        goodsRequestDto.setIlGoodsId(ilGoodsId);
        goodsRequestDto.setDeviceInfo("PC");
        goodsRequestDto.setApp(false);
        goodsRequestDto.setMember(false);
        goodsRequestDto.setEmployee(false);
        goodsRequestDto.setDawnDelivery(false);
//        goodsRequestDto.setArrivalScheduledDate(LocalDate.parse(orderInfo.getRows().get(0).getDeliveryDt()));
        goodsRequestDto.setArrivalScheduledDate(null);
        goodsRequestDto.setGoodsDailyCycleType(null);
        goodsRequestDto.setBuyQty(1);
//        goodsRequestDto.setStoreDelivery(ShoppingEnums.CartType.SHOP.getCode().equals(reqDto.getCartType()));
//        goodsRequestDto.setStoreDeliveryInfo(storeDeliveryInfo);
//        // 매장 상품일때 재고를 실시간으로 조회하여 가지고 오기
//        goodsRequestDto.setRealTimeStoreStock(ShoppingEnums.CartType.SHOP.getCode().equals(reqDto.getCartType()));
//        // 베이비밀 일괄배송이고 일반 배송 (택배배송)일때 true
//        goodsRequestDto.setDailyDelivery("Y".equals(goodsDto.getGoodsDailyBulkYn()) && ShoppingEnums.DeliveryType.NORMAL.getCode().equals(deliveryTypeData.getDeliveryType()));
        // 중복 품목 재고 정보
//        goodsRequestDto.setOverlapBuyItem(overlapBuyItem);
//        goodsRequestDto.setGoodsDailyBulk("Y".equals(goodsDto.getGoodsDailyBulkYn()) && ShoppingEnums.DeliveryType.DAILY.getCode().equals(deliveryTypeData.getDeliveryType()));

        // 상품
//        goodsRequestDto.setBosCreateOrder(true);

        BasicSelectGoodsVo basicSelectGoodsVo = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);
        if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(basicSelectGoodsVo.getGoodsType())) {
            List<PackageGoodsListDto> packageList = goodsGoodsBiz.getPackagGoodsInfoList(ilGoodsId, false, false, false, null, 0);
            return packageList.get(0).getStockQty();
        } else {
            return basicSelectGoodsVo.getStockQty();
        }
    }

    private void testThenPrice(CartSummaryDto cartSummaryDto, List<OrderBindDto> orderBindList) {
        // then - 상품 정가 합
        assertEquals(gCartSummaryDto.getGoodsRecommendedPrice(), cartSummaryDto.getGoodsRecommendedPrice());
        if (orderBindList != null) {
            assertEquals(cartSummaryDto.getGoodsRecommendedPrice(), orderBindList.stream()
                    .mapToInt(vo -> vo.getOrderShippingZoneList().stream()
                            .mapToInt(vo1 -> vo1.getShippingPriceList().stream()
                                    .mapToInt(vo2 -> vo2.getOrderDetlList().stream()
                                            .map(OrderBindOrderDetlDto::getOrderDetlVo)
                                            .filter(vo3 -> !GoodsEnums.GoodsType.RENTAL.getCode().equals(vo3.getGoodsTpCd()))
                                            .mapToInt(vo3 -> vo3.getRecommendedPrice() * vo3.getOrderCnt())
                                            .sum()
                                    ).sum()
                            ).sum()
                    ).sum()
            );
        }

        // then - 상품 판매가 합
        assertEquals(gCartSummaryDto.getGoodsSalePrice(), cartSummaryDto.getGoodsSalePrice());
        if (orderBindList != null) {
            assertEquals(cartSummaryDto.getGoodsSalePrice(), orderBindList.stream()
                    .map(OrderBindDto::getOrderPaymentVo)
                    .mapToInt(OrderPaymentVo::getSalePrice)
                    .sum()
            );
        }

        // then - 상품 할인액 합
        assertEquals(gCartSummaryDto.getGoodsDiscountPrice(), cartSummaryDto.getGoodsDiscountPrice());
        if (orderBindList != null) {
            assertEquals(cartSummaryDto.getGoodsDiscountPrice(), orderBindList.stream()
                    .mapToInt(vo -> vo.getOrderShippingZoneList().stream()
                            .mapToInt(vo1 -> vo1.getShippingPriceList().stream()
                                    .mapToInt(vo2 -> vo2.getOrderDetlList().stream()
                                            .mapToInt(vo3 -> vo3.getOrderDetlDiscountList().stream()
                                                    .mapToInt(OrderDetlDiscountVo::getDiscountPrice)
                                                    .sum()
                                            ).sum()
                                    ).sum()
                            ).sum()
                    ).sum()
            );
        }

        // then - 상품 결제금액 합
        assertEquals(gCartSummaryDto.getGoodsPaymentPrice(), cartSummaryDto.getGoodsPaymentPrice());
        if (orderBindList != null) {
            assertEquals(cartSummaryDto.getGoodsPaymentPrice(), orderBindList.stream()
                    .map(OrderBindDto::getOrderPaymentVo)
                    .mapToInt(OrderPaymentVo::getPaidPrice)
                    .sum()
            );
        }

        // then - 배송비 정가 합
        assertEquals(gCartSummaryDto.getShippingRecommendedPrice(), cartSummaryDto.getShippingRecommendedPrice());
        if (orderBindList != null) {
            assertEquals(cartSummaryDto.getShippingRecommendedPrice(), orderBindList.stream()
                    .mapToInt(vo -> vo.getOrderShippingZoneList().stream()
                            .mapToInt(vo1 -> vo1.getShippingPriceList().stream()
                                    .map(OrderBindShippingPriceDto::getOrderShippingPriceVo)
                                    .mapToInt(OrderShippingPriceVo::getOrgShippingPrice)
                                    .sum())
                            .sum())
                    .sum()
            );
        }

        // then - 배송비 할인 합
        assertEquals(gCartSummaryDto.getShippingDiscountPrice(), cartSummaryDto.getShippingDiscountPrice());
        if (orderBindList != null) {
            assertEquals(cartSummaryDto.getShippingDiscountPrice(), orderBindList.stream()
                    .mapToInt(vo -> vo.getOrderShippingZoneList().stream()
                            .mapToInt(vo1 -> vo1.getShippingPriceList().stream()
                                    .map(OrderBindShippingPriceDto::getOrderShippingPriceVo)
                                    .mapToInt(OrderShippingPriceVo::getShippingDiscountPrice)
                                    .sum())
                            .sum())
                    .sum()
            );
        }

        // then - 배송비 결제금액 합
        assertEquals(gCartSummaryDto.getShippingPaymentPrice(), cartSummaryDto.getShippingPaymentPrice());
        if (orderBindList != null) {
            assertEquals(cartSummaryDto.getShippingPaymentPrice(), orderBindList.stream()
                    .mapToInt(vo -> vo.getOrderShippingZoneList().stream()
                            .mapToInt(vo1 -> vo1.getShippingPriceList().stream()
                                    .map(OrderBindShippingPriceDto::getOrderShippingPriceVo)
                                    .mapToInt(OrderShippingPriceVo::getShippingPrice)
                                    .sum())
                            .sum())
                    .sum()
            );
        }

        // then - PG 결제금액 과세금액 합
        assertEquals(gCartSummaryDto.getTaxPaymentPrice(), cartSummaryDto.getTaxPaymentPrice());
        if (orderBindList != null) {
            assertEquals(cartSummaryDto.getTaxPaymentPrice(), orderBindList.stream()
                    .map(OrderBindDto::getOrderPaymentVo)
                    .mapToInt(OrderPaymentVo::getTaxablePrice)
                    .sum()
            );
        }

        // then - PG 결제금액 비과세금액 합
        assertEquals(gCartSummaryDto.getTaxFreePaymentPrice(), cartSummaryDto.getTaxFreePaymentPrice());
        if (orderBindList != null) {
            assertEquals(cartSummaryDto.getTaxFreePaymentPrice(), orderBindList.stream()
                    .map(OrderBindDto::getOrderPaymentVo)
                    .mapToInt(OrderPaymentVo::getNonTaxablePrice)
                    .sum()
            );
        }

        // then - PG 결제금액 금액 합
        assertEquals(gCartSummaryDto.getPaymentPrice(), cartSummaryDto.getPaymentPrice());
        if (orderBindList != null) {
            assertEquals(cartSummaryDto.getPaymentPrice(), orderBindList.stream()
                    .map(OrderBindDto::getOrderPaymentVo)
                    .mapToInt(OrderPaymentVo::getPaymentPrice)
                    .sum()
            );
        }

        // then - 적립금 사용금액
        assertEquals(gCartSummaryDto.getUsePoint(), cartSummaryDto.getUsePoint());
        if (orderBindList != null) {
            assertEquals(cartSummaryDto.getUsePoint(), orderBindList.stream()
                    .map(OrderBindDto::getOrderPaymentVo)
                    .mapToInt(OrderPaymentVo::getPointPrice)
                    .sum()
            );
        }

    }

    private void testThenAdminPrice(List<OrderDetailGoodsListDto> orderDetailList, OrderDetailShippingZoneListResponseDto orderShippingInfo, OrderDetailPayListResponseDto orderPayInfo) {
        // then - 상품 정가 합
        assertEquals(gCartSummaryDto.getGoodsRecommendedPrice(), orderDetailList.stream()
                .mapToInt(OrderDetailGoodsListDto::getRecommendedPrice)
                .sum()
        );

        // then - 상품 판매가 합
        assertEquals(gCartSummaryDto.getGoodsSalePrice(), orderDetailList.stream()
                .mapToInt(OrderDetailGoodsListDto::getSalePrice)
                .sum()
        );

        // then - 상품 할인액 합
        assertEquals(gCartSummaryDto.getGoodsDiscountPrice(), orderDetailList.stream()
                .mapToInt(OrderDetailGoodsListDto::getDiscountPrice)
                .sum()
        );

        // then - 상품 결제금액 합
        assertEquals(gCartSummaryDto.getGoodsPaymentPrice(), orderDetailList.stream()
                .mapToInt(OrderDetailGoodsListDto::getPaidPrice)
                .sum()
        );

        // then - 배송비 정가 합
        assertEquals(gCartSummaryDto.getShippingRecommendedPrice(), orderDetailList.stream()
                .mapToInt(OrderDetailGoodsListDto::getShippingPrice)
                .distinct()
                .sum()
        );

        // then - 배송비 할인 합
        assertEquals(gCartSummaryDto.getShippingDiscountPrice(), orderShippingInfo.getRows().stream()
                .mapToInt(OrderDetlShippingZoneVo::getShippingDiscountPrice)
                .sum()
        );

        // then - 배송비 결제금액 합
        assertEquals(gCartSummaryDto.getShippingPaymentPrice(), orderShippingInfo.getRows().stream()
                .mapToInt(OrderDetlShippingZoneVo::getShippingPrice)
                .sum()
        );

        // then - PG 결제금액 과세금액 합
        assertEquals(gCartSummaryDto.getTaxPaymentPrice(), orderPayInfo.getPayDetailList().stream()
                .mapToInt(vo -> Integer.parseInt(vo.getTaxablePrice()))
                .sum()
        );

        // then - PG 결제금액 비과세금액 합
        assertEquals(gCartSummaryDto.getTaxFreePaymentPrice(), orderPayInfo.getPayDetailList().stream()
                .mapToInt(vo -> Integer.parseInt(vo.getNonTaxablePrice()))
                .sum()
        );

        // then - PG 결제금액 금액 합
        assertEquals(gCartSummaryDto.getPaymentPrice(), orderPayInfo.getPayDetailList().stream()
                .mapToInt(vo -> Integer.parseInt(vo.getPaidPrice()))
                .sum()
        );

    }

    // 클레임 요청
    private ClaimRequestProcessDto claimRequest(MallOrderDetailListResponseDto orderInfo, OrderRegistrationResponseDto orderRegistrationResponseDto, String odid, ClaimTestType claimTestType, OrderClaimEnums.ClaimStatusTp claimStatusTp, boolean processComplete) throws Exception {
        String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];

        List<MallOrderDetailGoodsDto> orderDetailInfoList = orderInfo.getOrderDetailList().stream()
                .map(MallOrderDetailDeliveryTypeDto::getDeliveryTypeOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailDeliveryDtDto::getDeliveryDtOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList())
                .stream()
                .map(MallOrderDetailtrackingNoDto::getTrackingNoOrderDetailList)
                .flatMap(Collection::stream)
                .collect(toList());

//        if (ClaimTestType.PART.equals(claimTestType)) {
//            orderDetailInfoList = orderDetailInfoList.subList(0, orderDetailInfoList.size() - 2);
//        }

        String claimStatusCd = "";
        if (OrderClaimEnums.ClaimStatusTp.CANCEL.equals(claimStatusTp)) {
            claimStatusCd = OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode();
        } else if (OrderClaimEnums.ClaimStatusTp.RETURN.equals(claimStatusTp)) {
            claimStatusCd = OrderEnums.OrderStatus.RETURN_COMPLETE.getCode();
        } else if (OrderClaimEnums.ClaimStatusTp.RETURN_DELIVERY.equals(claimStatusTp)) {
            claimStatusCd = OrderEnums.OrderStatus.EXCHANGE_COMPLETE.getCode();
        }

        OrderClaimRegisterRequestDto requestDto = new OrderClaimRegisterRequestDto();
        requestDto.setOdOrderId(Long.parseLong(odOrderId));
        requestDto.setOdid(odid);
        requestDto.setStatus("IC");
        requestDto.setClaimStatusTp(claimStatusTp.getCode());
        requestDto.setClaimStatusCd(claimStatusCd);
        requestDto.setFrontTp(1);

        if (gGoodsCouponIssueId1 != null) {
            List<OrderClaimCouponInfoDto> goodsCouponInfoList = new ArrayList<>();
            OrderClaimCouponInfoDto orderClaimCouponInfoDto = new OrderClaimCouponInfoDto();
            orderClaimCouponInfoDto.setPmCouponIssueId(gGoodsCouponIssueId1);
            orderClaimCouponInfoDto.setOdOrderId(Long.parseLong(odOrderId));
            goodsCouponInfoList.add(orderClaimCouponInfoDto);
            if (gGoodsCouponIssueId2 != null) {
                OrderClaimCouponInfoDto orderClaimCouponInfoDto2 = new OrderClaimCouponInfoDto();
                orderClaimCouponInfoDto2.setPmCouponIssueId(gGoodsCouponIssueId2);
                orderClaimCouponInfoDto2.setOdOrderId(Long.parseLong(odOrderId));
                goodsCouponInfoList.add(orderClaimCouponInfoDto2);
            }
            requestDto.setGoodsCouponInfoList(goodsCouponInfoList);
        }
        if (gCartCouponIssueId != null) {
            OrderClaimCouponInfoDto cartCouponInfo = new OrderClaimCouponInfoDto();
            cartCouponInfo.setPmCouponIssueId(gCartCouponIssueId);
            cartCouponInfo.setOdOrderId(Long.parseLong(odOrderId));
            requestDto.setCartCouponInfoList(Collections.singletonList(cartCouponInfo));
        }
        if (gShippingCouponIssueId != null) {
            OrderClaimCouponInfoDto cartCouponInfo = new OrderClaimCouponInfoDto();
            cartCouponInfo.setPmCouponIssueId(gShippingCouponIssueId);
            cartCouponInfo.setOdOrderId(Long.parseLong(odOrderId));
            requestDto.setDeliveryCouponList(Collections.singletonList(cartCouponInfo));
        }

        List<OrderClaimGoodsInfoDto> orderClaimGoodsInfoDtoList = new ArrayList<>();
        for (MallOrderDetailGoodsDto orderDetailInfo : orderDetailInfoList) {
            if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderDetailInfo.getGoodsTpCd())) {
                for (MallOrderDetailGoodsDto packageGoodsInfo : orderDetailInfo.getPackageGoodsList()) {
                    if (OrderEnums.OrderStatus.EXCHANGE_COMPLETE.getCode().equals(packageGoodsInfo.getOrderStatusCd())) {
                        continue;
                    }
                    if (OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(packageGoodsInfo.getOrderStatusCd())) {
                        continue;
                    }
                    if (packageGoodsInfo.getOdClaimId() != 0) continue;

                    OrderClaimGoodsInfoDto orderClaimGoodsInfoDto = new OrderClaimGoodsInfoDto();
                    orderClaimGoodsInfoDto.setOdOrderId(packageGoodsInfo.getOdOrderId());
                    orderClaimGoodsInfoDto.setOdOrderDetlId(packageGoodsInfo.getOdOrderDetlId());
                    orderClaimGoodsInfoDto.setOdOrderDetlStepId(packageGoodsInfo.getOdOrderDetlStepId());
//            orderClaimGoodsInfoDto.setOdClaimDetlId(packageGoodsInfo.getOdClaimDetlId());
                    if (ClaimTestType.PART.equals(claimTestType)) {
                        orderClaimGoodsInfoDto.setClaimCnt(1);
                    } else {
                        orderClaimGoodsInfoDto.setClaimCnt(packageGoodsInfo.getOrderCnt());
                    }
                    orderClaimGoodsInfoDto.setCancelCnt(0);
                    orderClaimGoodsInfoDto.setOrderCnt(packageGoodsInfo.getOrderCnt());
                    orderClaimGoodsInfoDto.setOrderPrice(packageGoodsInfo.getPaidPrice());
                    orderClaimGoodsInfoDto.setPaidPrice(packageGoodsInfo.getPaidPrice());
                    orderClaimGoodsInfoDto.setUrWarehouseId(packageGoodsInfo.getUrWarehouseId());
                    orderClaimGoodsInfoDto.setIlGoodsId(Long.parseLong(orderDetailInfo.getIlGoodsId()));
                    orderClaimGoodsInfoDto.setIlItemCd(orderDetailInfo.getIlItemCd());
                    LocalDate localDate = LocalDate.parse(orderDetailInfo.getDeliveryDt(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    orderClaimGoodsInfoDto.setShippingDt(localDate.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    orderClaimGoodsInfoDto.setGoodsDeliveryType(orderDetailInfo.getGoodsDeliveryType());

                    if (OrderClaimEnums.ClaimStatusTp.RETURN_DELIVERY.equals(claimStatusTp)) {
                        orderClaimGoodsInfoDto.setRedeliveryType(OrderClaimEnums.RedeliveryType.RETURN_DELIVERY.getCode());
                        orderClaimGoodsInfoDto.setOrderIfDt(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        orderClaimGoodsInfoDto.setDeliveryDt(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }

                    orderClaimGoodsInfoDtoList.add(orderClaimGoodsInfoDto);

                    if (OrderClaimEnums.ClaimStatusTp.RETURN_DELIVERY.equals(claimStatusTp)) {
                        OrderClaimGoodsInfoDto orderClaimGoodsInfoDto2 = new OrderClaimGoodsInfoDto();
                        orderClaimGoodsInfoDto2.setOdOrderId(orderClaimGoodsInfoDto.getOdOrderId());
                        orderClaimGoodsInfoDto2.setOdOrderDetlId(0L);
                        orderClaimGoodsInfoDto2.setOdOrderDetlDepthId(3);
                        orderClaimGoodsInfoDto2.setOdOrderDetlStepId(orderClaimGoodsInfoDto.getOdOrderDetlStepId());
                        orderClaimGoodsInfoDto2.setClaimCnt(orderClaimGoodsInfoDto.getClaimCnt());
                        orderClaimGoodsInfoDto2.setCancelCnt(0);
                        orderClaimGoodsInfoDto2.setOrgOrderCnt(orderClaimGoodsInfoDto.getOrderCnt());
                        orderClaimGoodsInfoDto2.setOrderCnt(orderClaimGoodsInfoDto.getOrderCnt());
                        orderClaimGoodsInfoDto2.setOrderPrice(orderClaimGoodsInfoDto.getPaidPrice());
                        orderClaimGoodsInfoDto2.setPaidPrice(orderClaimGoodsInfoDto.getPaidPrice());
                        orderClaimGoodsInfoDto2.setUrWarehouseId(orderClaimGoodsInfoDto.getUrWarehouseId());
                        orderClaimGoodsInfoDto2.setIlGoodsId(Long.parseLong(orderDetailInfo.getIlGoodsId()));
                        orderClaimGoodsInfoDto2.setIlItemCd(orderClaimGoodsInfoDto.getIlItemCd());
                        orderClaimGoodsInfoDto2.setGoodsDeliveryType(orderDetailInfo.getGoodsDeliveryType());
                        orderClaimGoodsInfoDto2.setOdOrderDetlParentId(orderClaimGoodsInfoDto.getOdOrderDetlId());
                        orderClaimGoodsInfoDto2.setOrderIfDt(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        orderClaimGoodsInfoDto2.setShippingDt(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        orderClaimGoodsInfoDto2.setDeliveryDt(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        orderClaimGoodsInfoDtoList.add(orderClaimGoodsInfoDto2);
                    }

                }
            } else {
                if (OrderEnums.OrderStatus.EXCHANGE_COMPLETE.getCode().equals(orderDetailInfo.getOrderStatusCd())) {
                    continue;
                }
                if (OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(orderDetailInfo.getOrderStatusCd())) {
                    continue;
                }
                if (orderDetailInfo.getOdClaimId() != 0) continue;

                OrderClaimGoodsInfoDto orderClaimGoodsInfoDto = new OrderClaimGoodsInfoDto();
                orderClaimGoodsInfoDto.setOdOrderId(orderDetailInfo.getOdOrderId());
                orderClaimGoodsInfoDto.setOdOrderDetlId(orderDetailInfo.getOdOrderDetlId());
                orderClaimGoodsInfoDto.setOdOrderDetlStepId(orderDetailInfo.getOdOrderDetlStepId());
//            orderClaimGoodsInfoDto.setOdClaimDetlId(orderDetailInfo.getOdClaimDetlId());
                if (ClaimTestType.PART.equals(claimTestType)) {
                    orderClaimGoodsInfoDto.setClaimCnt(1);
                } else {
                    orderClaimGoodsInfoDto.setClaimCnt(orderDetailInfo.getOrderCnt());
                }
                orderClaimGoodsInfoDto.setCancelCnt(0);
                orderClaimGoodsInfoDto.setOrderCnt(orderDetailInfo.getOrderCnt());
                orderClaimGoodsInfoDto.setOrderPrice(orderDetailInfo.getPaidPrice());
//            orderClaimGoodsInfoDto.setGoodsCouponPrice(orderDetailInfo.getGoodsCouponPrice());
                orderClaimGoodsInfoDto.setPaidPrice(orderDetailInfo.getPaidPrice());
                orderClaimGoodsInfoDto.setUrWarehouseId(orderDetailInfo.getUrWarehouseId());
//            orderClaimGoodsInfoDto.setClaimGoodsYn(orderDetailInfo.getClaimGoodsYn());
                orderClaimGoodsInfoDto.setIlGoodsId(Long.parseLong(orderDetailInfo.getIlGoodsId()));
                orderClaimGoodsInfoDto.setIlItemCd(orderDetailInfo.getIlItemCd());
                LocalDate localDate = LocalDate.now();
                if (!StringUtil.isEmpty(orderDetailInfo.getDeliveryDt())) {
                    localDate = LocalDate.parse(orderDetailInfo.getDeliveryDt(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
                orderClaimGoodsInfoDto.setShippingDt(localDate.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                orderClaimGoodsInfoDto.setGoodsDeliveryType(orderDetailInfo.getGoodsDeliveryType());

                if (OrderClaimEnums.ClaimStatusTp.RETURN_DELIVERY.equals(claimStatusTp)) {
                    orderClaimGoodsInfoDto.setRedeliveryType(OrderClaimEnums.RedeliveryType.RETURN_DELIVERY.getCode());
                    orderClaimGoodsInfoDto.setOrderIfDt(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    orderClaimGoodsInfoDto.setDeliveryDt(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }

                orderClaimGoodsInfoDtoList.add(orderClaimGoodsInfoDto);

                if (OrderClaimEnums.ClaimStatusTp.RETURN_DELIVERY.equals(claimStatusTp)) {
                    OrderClaimGoodsInfoDto orderClaimGoodsInfoDto2 = new OrderClaimGoodsInfoDto();
                    orderClaimGoodsInfoDto2.setOdOrderId(orderClaimGoodsInfoDto.getOdOrderId());
                    orderClaimGoodsInfoDto2.setOdOrderDetlId(0L);
                    orderClaimGoodsInfoDto2.setOdOrderDetlDepthId(3);
                    orderClaimGoodsInfoDto2.setOdOrderDetlStepId(orderClaimGoodsInfoDto.getOdOrderDetlStepId());
                    orderClaimGoodsInfoDto2.setClaimCnt(orderClaimGoodsInfoDto.getClaimCnt());
                    orderClaimGoodsInfoDto2.setCancelCnt(0);
                    orderClaimGoodsInfoDto2.setOrderCnt(orderClaimGoodsInfoDto.getOrderCnt());
                    orderClaimGoodsInfoDto2.setOrgOrderCnt(orderClaimGoodsInfoDto.getOrderCnt());
                    orderClaimGoodsInfoDto2.setOrderPrice(orderClaimGoodsInfoDto.getPaidPrice());
                    orderClaimGoodsInfoDto2.setPaidPrice(orderClaimGoodsInfoDto.getPaidPrice());
                    orderClaimGoodsInfoDto2.setUrWarehouseId(orderClaimGoodsInfoDto.getUrWarehouseId());
                    orderClaimGoodsInfoDto2.setIlGoodsId(Long.parseLong(orderDetailInfo.getIlGoodsId()));
                    orderClaimGoodsInfoDto2.setIlItemCd(orderClaimGoodsInfoDto.getIlItemCd());
                    orderClaimGoodsInfoDto2.setGoodsDeliveryType(orderDetailInfo.getGoodsDeliveryType());
                    orderClaimGoodsInfoDto2.setOdOrderDetlParentId(orderClaimGoodsInfoDto.getOdOrderDetlId());
                    orderClaimGoodsInfoDto2.setOrderIfDt(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    orderClaimGoodsInfoDto2.setShippingDt(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    orderClaimGoodsInfoDto2.setDeliveryDt(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                    orderClaimGoodsInfoDtoList.add(orderClaimGoodsInfoDto2);
                }
            }
        }
        requestDto.setGoodsInfoList(orderClaimGoodsInfoDtoList);
        boolean isClaimSave = false;
        ClaimRequestProcessDto claimResult = claimRequestProcessBiz.claimRequestProcess(claimStatusTp.getCode(), claimStatusCd, requestDto, isClaimSave);
        if (OrderEnums.OrderRegistrationResult.SUCCESS.equals(claimResult.getClaimResult()) && processComplete) {
            // 클레임 완료 처리
            // BOS 로그인
            bosLogin();
            ClaimCompleteProcessDto claimCompleteProcessDto = claimCompleteProcessBiz.claimCompleteProcess(claimStatusTp.getCode(), claimStatusCd, requestDto, isClaimSave);
        }

        return claimResult;
    }

    // 클레임 요청
    private ClaimRequestProcessDto claimRequestExcel(Long odOrderId, String odid, OrderDetailGoodsListResponseDto orderDetail, ClaimTestType claimTestType, OrderClaimEnums.ClaimStatusTp claimStatusTp) throws Exception {
        List<OrderDetailGoodsListDto> orderDetailInfoList = orderDetail.getRows().stream()
                .filter(x -> x.getOrderCnt() - x.getCancelCnt() > 0)
                .collect(toList());

//        if (ClaimTestType.PART.equals(claimTestType)) {
//            orderDetailInfoList = orderDetailInfoList.subList(0, orderDetailInfoList.size() - 2);
//        }

        String claimStatusCd = "";
        if (OrderClaimEnums.ClaimStatusTp.CANCEL.equals(claimStatusTp)) {
            claimStatusCd = OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode();
        } else if (OrderClaimEnums.ClaimStatusTp.RETURN.equals(claimStatusTp)) {
            claimStatusCd = OrderEnums.OrderStatus.RETURN_COMPLETE.getCode();
        } else if (OrderClaimEnums.ClaimStatusTp.RETURN_DELIVERY.equals(claimStatusTp)) {
            claimStatusCd = OrderEnums.OrderStatus.EXCHANGE_COMPLETE.getCode();
        }

        OrderClaimRegisterRequestDto requestDto = new OrderClaimRegisterRequestDto();
        requestDto.setOdOrderId(odOrderId);
        requestDto.setOdid(odid);
        requestDto.setStatus("IC");
        requestDto.setClaimStatusTp(claimStatusTp.getCode());
        requestDto.setClaimStatusCd(claimStatusCd);
        requestDto.setFrontTp(0);

        if (gGoodsCouponIssueId1 != null) {
            List<OrderClaimCouponInfoDto> goodsCouponInfoList = new ArrayList<>();
            OrderClaimCouponInfoDto orderClaimCouponInfoDto = new OrderClaimCouponInfoDto();
            orderClaimCouponInfoDto.setPmCouponIssueId(gGoodsCouponIssueId1);
            orderClaimCouponInfoDto.setOdOrderId(odOrderId);
            goodsCouponInfoList.add(orderClaimCouponInfoDto);
            if (gGoodsCouponIssueId2 != null) {
                OrderClaimCouponInfoDto orderClaimCouponInfoDto2 = new OrderClaimCouponInfoDto();
                orderClaimCouponInfoDto2.setPmCouponIssueId(gGoodsCouponIssueId2);
                orderClaimCouponInfoDto2.setOdOrderId(odOrderId);
                goodsCouponInfoList.add(orderClaimCouponInfoDto2);
            }
            requestDto.setGoodsCouponInfoList(goodsCouponInfoList);
        }
        if (gCartCouponIssueId != null) {
            OrderClaimCouponInfoDto cartCouponInfo = new OrderClaimCouponInfoDto();
            cartCouponInfo.setPmCouponIssueId(gCartCouponIssueId);
            cartCouponInfo.setOdOrderId(odOrderId);
            requestDto.setCartCouponInfoList(Collections.singletonList(cartCouponInfo));
        }
        if (gShippingCouponIssueId != null) {
            OrderClaimCouponInfoDto cartCouponInfo = new OrderClaimCouponInfoDto();
            cartCouponInfo.setPmCouponIssueId(gShippingCouponIssueId);
            cartCouponInfo.setOdOrderId(odOrderId);
            requestDto.setDeliveryCouponList(Collections.singletonList(cartCouponInfo));
        }

        List<OrderClaimGoodsInfoDto> orderClaimGoodsInfoDtoList = new ArrayList<>();
        for (OrderDetailGoodsListDto orderDetailInfo : orderDetailInfoList) {
            if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderDetailInfo.getGoodsTpCd())) continue;
            if (orderDetailInfo.getOdClaimId() != 0) continue;
            if (OrderEnums.OrderStatus.EXCHANGE_COMPLETE.getCode().equals(orderDetailInfo.getOrderStatusCode())) {
                continue;
            }
            OrderClaimGoodsInfoDto orderClaimGoodsInfoDto = new OrderClaimGoodsInfoDto();
            orderClaimGoodsInfoDto.setOdOrderId(odOrderId);
            orderClaimGoodsInfoDto.setOdOrderDetlId(orderDetailInfo.getOdOrderDetlId());
//            orderClaimGoodsInfoDto.setOdClaimDetlId(orderDetailInfo.getOdClaimDetlId());
            if (ClaimTestType.PART.equals(claimTestType)) {
                orderClaimGoodsInfoDto.setClaimCnt(1);
            } else {
                orderClaimGoodsInfoDto.setClaimCnt(orderDetailInfo.getOrderCnt());
            }
            orderClaimGoodsInfoDto.setCancelCnt(0);
            orderClaimGoodsInfoDto.setOrderCnt(orderDetailInfo.getOrderCnt());
            orderClaimGoodsInfoDto.setOrderPrice(orderDetailInfo.getPaidPrice());
//            orderClaimGoodsInfoDto.setGoodsCouponPrice(orderDetailInfo.getGoodsCouponPrice());
            orderClaimGoodsInfoDto.setPaidPrice(orderDetailInfo.getPaidPrice());
            orderClaimGoodsInfoDto.setUrWarehouseId(orderDetailInfo.getUrWarehouseId());
//            orderClaimGoodsInfoDto.setClaimGoodsYn(orderDetailInfo.getClaimGoodsYn());
            orderClaimGoodsInfoDto.setIlGoodsId(orderDetailInfo.getIlGoodsId());
            LocalDate localDate = LocalDate.parse(orderDetailInfo.getDeliveryDt(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            orderClaimGoodsInfoDto.setShippingDt(localDate.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            orderClaimGoodsInfoDto.setGoodsDeliveryType(orderDetailInfo.getGoodsDeliveryType());

            orderClaimGoodsInfoDtoList.add(orderClaimGoodsInfoDto);
        }
        requestDto.setGoodsInfoList(orderClaimGoodsInfoDtoList);
        requestDto.setTargetTp("S");
        boolean isClaimSave = false;
        ClaimRequestProcessDto claimResult = claimRequestProcessBiz.claimRequestProcess(claimStatusTp.getCode(), claimStatusCd, requestDto, isClaimSave);
        if (OrderEnums.OrderRegistrationResult.SUCCESS.equals(claimResult.getClaimResult())) {
            // 클레임 완료 처리
            // BOS 로그인
            bosLogin();
            ClaimCompleteProcessDto claimCompleteProcessDto = claimCompleteProcessBiz.claimCompleteProcess(claimStatusTp.getCode(), claimStatusCd, requestDto, isClaimSave);
        }

        return claimResult;
    }

    // 장바구니 상품 등록
    private Long addCart(Long urUserId, Long goodsId, int orderCount) throws Exception {
        BasicSelectGoodsVo goods = goodsGoodsBiz.getGoodsBasicInfo(GoodsRequestDto.builder()
                .ilGoodsId(goodsId)
                .build());
        ShoppingEnums.DeliveryType deliveryType = shoppingCartBiz.getDeliveryTypeBySaleType(goods.getSaleType());
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            if (GoodsEnums.StoreDeliveryType.PICKUP.getCode().equals(gReqDto.getStoreDeliveryType())) {
                deliveryType = ShoppingEnums.DeliveryType.SHOP_PICKUP;
            } else {
                deliveryType = ShoppingEnums.DeliveryType.SHOP_DELIVERY;
            }
        } else if (ShoppingEnums.CartType.RENTAL.getCode().equals(gReqDto.getCartType())) {
            deliveryType = ShoppingEnums.DeliveryType.RENTAL;
        } else if (ShoppingEnums.CartType.REGULAR.getCode().equals(gReqDto.getCartType())) {
            deliveryType = ShoppingEnums.DeliveryType.REGULAR;
        } else if (ShoppingEnums.CartType.INCORPOREITY.getCode().equals(gReqDto.getCartType())) {
            deliveryType = ShoppingEnums.DeliveryType.INCORPOREITY;
        }

        GoodsDailyCycleDto goodsDailyCycle = null;

        // 일일상품일 경우
        if (GoodsEnums.SaleType.DAILY.getCode().equals(goods.getSaleType())) {
            // 일일상품 정보 조회
            List<GoodsDailyCycleDto> goodsDailyCycleList = goodsGoodsBiz.getGoodsDailyCycleList(goodsId, goods.getGoodsDailyType(), "06362", "1168011500107490000001720");
            goodsDailyCycle = goodsDailyCycleList.get(0);
        }

        // 장바구니 등록 요청 DTO 세팅
        AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
        addCartInfoRequestDto.setUrPcidCd(PCID_ID);
        addCartInfoRequestDto.setIlGoodsId(goodsId);
        addCartInfoRequestDto.setQty(orderCount);
        addCartInfoRequestDto.setBuyNowYn("N");
        addCartInfoRequestDto.setSaleType(goods.getSaleType());
        addCartInfoRequestDto.setGoodsDailyType(goods.getGoodsDailyType());
        addCartInfoRequestDto.setDeliveryType(deliveryType.getCode());
        addCartInfoRequestDto.setUrUserId(urUserId);
        addCartInfoRequestDto.setGoodsGiftPossible(true);
        // 일일배송 -> 배송패턴 기본값 세팅
        if (ObjectUtils.isNotEmpty(goodsDailyCycle) && GoodsEnums.SaleType.DAILY.getCode().equals(goods.getSaleType())) {
            addCartInfoRequestDto.setGoodsDailyCycleType(goodsDailyCycle.getGoodsDailyCycleType());
            addCartInfoRequestDto.setGoodsDailyCycleTermType(goodsDailyCycle.getTerm().get(0).get("goodsDailyCycleTermType"));

            // 녹즙
            if (GoodsEnums.GoodsDailyType.GREENJUICE.getCode().equals(goods.getGoodsDailyType())) {
                String[] goodsDailyCycleGreenJuiceWeekType = new String[1];
                goodsDailyCycleGreenJuiceWeekType[0] = goodsDailyCycle.getWeek().get(0).get("goodsDailyCycleGreenJuiceWeekType");
                addCartInfoRequestDto.setGoodsDailyCycleGreenJuiceWeekType(goodsDailyCycleGreenJuiceWeekType);
                // 베이비밀
            } else if (GoodsEnums.GoodsDailyType.BABYMEAL.getCode().equals(goods.getGoodsDailyType())) {
                addCartInfoRequestDto.setGoodsDailyAllergyYn("N");
                if ("Y".equals(goods.getGoodsDailyBulkYn())) {
                    addCartInfoRequestDto.setGoodsDailyBulkYn("Y");
                    List<HashMap<String, String>> goodsDailyBulkList = goodsGoodsBiz.getGoodsDailyBulkList(goods.getIlGoodsId());
                    addCartInfoRequestDto.setGoodsBulkType(goodsDailyBulkList.get(0).get("goodsBulkType"));
                } else {
                    addCartInfoRequestDto.setGoodsDailyBulkYn("N");
                }
            }
        }

        return shoppingCartBiz.addCartInfo(addCartInfoRequestDto);
    }

    // 장바구니 데이터 조회
    private List<CartDeliveryDto> getCartDataList(List<Long> cartIdList, BuyerVo buyerVo) throws Exception {
        String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
        long longUrUserId = 0L;
        if (!"".equals(urUserId)) {
            longUrUserId = Long.parseLong(urUserId);
        }
        boolean isMember = StringUtil.isNotEmpty(urUserId);
        boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());

        // 장바구니 데이터 조회
        GetCartDataRequestDto cartDataRequestDto = new GetCartDataRequestDto();
        cartDataRequestDto.setCartType(gReqDto.getCartType());
        cartDataRequestDto.setSpCartId(cartIdList);
        cartDataRequestDto.setEmployeeYn(isEmployee ? "Y" : "N");
        if (gGoodsCouponIssueId1 != null) {
            List<UseGoodsCouponDto> goodsCouponList = new ArrayList<>();
            UseGoodsCouponDto couponDto = new UseGoodsCouponDto();
            couponDto.setSpCartId(cartIdList.get(0));
            couponDto.setPmCouponIssueId(gGoodsCouponIssueId1);
            goodsCouponList.add(couponDto);

            if (gGoodsCouponIssueId2 != null) {
                UseGoodsCouponDto couponDto2 = new UseGoodsCouponDto();
                couponDto2.setSpCartId(cartIdList.get(1));
                couponDto2.setPmCouponIssueId(gGoodsCouponIssueId2);
                goodsCouponList.add(couponDto2);
            }

            cartDataRequestDto.setUseGoodsCoupon(goodsCouponList);
        }

        if (gShippingCouponIssueId != null) {
            UseShippingCouponDto shippingCouponDto = new UseShippingCouponDto();
            shippingCouponDto.setShippingIndex(1);
            shippingCouponDto.setPmCouponIssueId(gShippingCouponIssueId);
            cartDataRequestDto.setUseShippingCoupon(Collections.singletonList(shippingCouponDto));
        }
        cartDataRequestDto.setUrPcidCd(PCID_ID);
        cartDataRequestDto.setUrUserId(longUrUserId);
        cartDataRequestDto.setDeviceInfo(DeviceUtil.getDirInfo());
        cartDataRequestDto.setApp(DeviceUtil.isApp());
        cartDataRequestDto.setMember(isMember);
        cartDataRequestDto.setEmployee(isEmployee);
        cartDataRequestDto.setReceiverZipCode(buyerVo.getReceiverZipCode());
        cartDataRequestDto.setBuildingCode(buyerVo.getBuildingCode());
        cartDataRequestDto.setUrErpEmployeeCode(buyerVo.getUrErpEmployeeCode());
        cartDataRequestDto.setArrivalScheduled(gReqDto.getArrivalScheduled());
//        cartDataRequestDto.setArrivalGoods(reqDto.getArrivalGoods());
//
//        // given - 매장 관련 데이터
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            cartDataRequestDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
            cartDataRequestDto.setNextArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
        }

        if (ShoppingEnums.CartType.REGULAR.getCode().equals(gReqDto.getCartType())) {
            cartDataRequestDto.setBridgeYn("Y");
        }

        List<CartDeliveryDto> cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);

        // 장바구니 할인 정보 반영된 카트정보 get
        if (gCartCouponIssueId != null) {
            // cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
            List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);

            // List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
            List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);

            CouponDto cartCouponDto = promotionCouponBiz.getCartCouponApplicationListByUser(longUrUserId, cartGoodsList,
                    GoodsEnums.DeviceType.PC, gCartCouponIssueId);
            cartDataRequestDto.setUseCartCoupon(cartCouponDto);

            cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);
        }


        return cartDataDto;
    }

    // 주문 데이터 생성
    private List<OrderBindDto> getOrderBindList(BuyerVo buyerVo, OrderEnums.PaymentType paymentType, List<CartDeliveryDto> cartDataDto, GetSessionShippingResponseDto sessionShippingDto, CartSummaryDto cartSummaryDto) throws Exception {
        String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
        long longUrUserId = 0L;
        if (!"".equals(urUserId)) {
            longUrUserId = Long.parseLong(urUserId);
        }
        boolean isMember = StringUtil.isNotEmpty(urUserId);
        boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());

        SystemEnums.AgentType agentType = SystemEnums.AgentType.PC;
        // 임시 주문 생성 등록
        CartBuyerDto cartBuyerDto = new CartBuyerDto();
        if (isMember) {
            cartBuyerDto.setUrUserId(longUrUserId);
            cartBuyerDto.setUrGroupId(buyerVo.getUrGroupId());
            cartBuyerDto.setUrEmployeeCd(buyerVo.getUrErpEmployeeCode());
            cartBuyerDto.setGuestCi("");
            // 회원이 렌탈 신청시에는 세션정보가 아닌 요청정보로 처리
//            if (ShoppingEnums.CartType.RENTAL.getCode().equals(reqDto.getCartType())) {
//                cartBuyerDto.setBuyerName(reqDto.getBuyerName());
//                cartBuyerDto.setBuyerMobile(reqDto.getBuyerMobile());
//                cartBuyerDto.setBuyerEmail(reqDto.getBuyerMail());
//            } else {
            cartBuyerDto.setBuyerName(buyerVo.getUserName());
            cartBuyerDto.setBuyerMobile(buyerVo.getUserMobile());
            cartBuyerDto.setBuyerEmail(buyerVo.getUserEmail());
            cartBuyerDto.setReceiveName(buyerVo.getReceiverName());
            cartBuyerDto.setReceiveMobile(buyerVo.getReceiverMobile());
//            }
            if (isEmployee) {
//                if("Y".equals(reqDto.getEmployeeYn())) {
                cartBuyerDto.setBuyerType(UserEnums.BuyerType.EMPLOYEE);
//                } else {
//                    cartBuyerDto.setBuyerType(UserEnums.BuyerType.EMPLOYEE_BASIC);
//                }
            } else {
                cartBuyerDto.setBuyerType(UserEnums.BuyerType.USER);
            }
//        } else {
//            cartBuyerDto.setUrUserId(0L);
//            cartBuyerDto.setUrGroupId(0L);
//            cartBuyerDto.setUrEmployeeCd("");
//            cartBuyerDto.setGuestCi(nonMemberCertificationDto.getCi());
//            cartBuyerDto.setBuyerName(nonMemberCertificationDto.getUserName());
//            cartBuyerDto.setBuyerMobile(nonMemberCertificationDto.getMobile());
//            cartBuyerDto.setBuyerEmail(reqDto.getBuyerMail());
//            cartBuyerDto.setBuyerType(UserEnums.BuyerType.GUEST);
        }
        cartBuyerDto.setPaymentType(paymentType);
        cartBuyerDto.setAgentType(agentType);
        cartBuyerDto.setUrPcidCd(PCID_ID);
        //환불 계좌 정보
        cartBuyerDto.setBankCode("1");
        cartBuyerDto.setAccountNumber("test");
        cartBuyerDto.setHolderName("test");

        CartRegularDto cartRegularDto = new CartRegularDto();
//        cartRegularDto.setCycleType(reqDto.getRegularShippingCycleType());
//        cartRegularDto.setCycleTermType(reqDto.getRegularShippingCycleTermType());
//        cartRegularDto.setArrivalScheduledDate(reqDto.getRegularShippingArrivalScheduledDate());

        CreateOrderCartDto createOrderCartDto = new CreateOrderCartDto();
        createOrderCartDto.setBuyer(cartBuyerDto);
        createOrderCartDto.setShippingZone(sessionShippingDto);
        createOrderCartDto.setCartList(cartDataDto);
        createOrderCartDto.setCartSummary(cartSummaryDto);
        createOrderCartDto.setRegular(cartRegularDto);
        // 매장 정보
        if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
            CartStoreDto cartStoreDto = new CartStoreDto();
            if (ShoppingEnums.CartType.SHOP.getCode().equals(gReqDto.getCartType())) {
                cartStoreDto.setStoreDeliveryType(gReqDto.getStoreDeliveryType());
                cartStoreDto.setStoreArrivalScheduledDate(gReqDto.getStoreArrivalScheduledDate());
                cartStoreDto.setUrStoreScheduleId(gReqDto.getUrStoreScheduleId());
            }
            createOrderCartDto.setStore(cartStoreDto);
        }
        return orderBindBiz.orderDataBind(createOrderCartDto);
    }

}
