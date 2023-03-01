package kr.co.pulmuone.v1.promotion.exhibit.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.GoodsConstants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.promotion.exhibit.dto.*;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.*;
import kr.co.pulmuone.v1.shopping.cart.dto.SpCartPickGoodsRequestDto;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

class PromotionExhibitServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    PromotionExhibitService promotionExhibitService;

    @InjectMocks
    private PromotionExhibitService mockPromotionExhibitService;

    @Mock
    GoodsGoodsBiz mockGoodsGoodsBiz;

    @Mock
    StoreDeliveryBiz mockStoreDeliveryBiz;

    @Mock
    UserCertificationBiz mockUserCertificationBiz;

    @BeforeEach
    void setUp() {
        mockPromotionExhibitService = new PromotionExhibitService(mockGoodsGoodsBiz, mockStoreDeliveryBiz, mockUserCertificationBiz);
    }

    @Test
    void getExhibitListByUser_조회_정상() throws Exception {
        //given
        ExhibitListByUserRequestDto dto = new ExhibitListByUserRequestDto();
        dto.setPage(1);
        dto.setLimit(20);

        //when
        ExhibitListByUserResponseDto result = promotionExhibitService.getExhibitListByUser(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void getNormalByUser_조회_정상() throws Exception {
        //given
        Long evExhibitId = 13L;
        String deviceType = "PC";

        //when
        NormalByUserVo result = promotionExhibitService.getNormalByUser(evExhibitId, deviceType);

        //then
        assertTrue(result.getTitle().length() > 0);
    }

    @Test
    void getSelectListByUser_조회_정상() throws Exception {
        //given
        ExhibitListByUserRequestDto dto = new ExhibitListByUserRequestDto();
        dto.setPage(1);
        dto.setLimit(20);
        buyerLogin();

        //when
        SelectListByUserResponseDto result = promotionExhibitService.getSelectListByUser(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void getSelectByUser_조회_정상() throws Exception {
        //given
        Long evExhibitId = 21L;
        String deviceType = "PC";

        //when
        SelectByUserVo result = promotionExhibitService.getSelectByUser(evExhibitId, deviceType);

        //then
        assertTrue(result.getTitle().length() > 0);
    }

    @Test
    void getGroupByUser_정상() throws Exception {
        //given
        Long evExhibitId = 3L;

        //when
        List<ExhibitGroupByUserVo> result = promotionExhibitService.getGroupByUser(evExhibitId);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getGroupDetailByUser_정상() throws Exception {
        //given
        Long evExhibitGroupId = 1L;

        //when
        List<Long> result = promotionExhibitService.getGroupDetailByUser(evExhibitGroupId);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getSelectGoodsByUser_정상() throws Exception {
        //given
        Long evExhibitId = 5L;

        //when
        List<Long> result = promotionExhibitService.getSelectGoodsByUser(evExhibitId);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getExhibitValidation_정상() {
        //given
        ExhibitValidationRequestDto dto = new ExhibitValidationRequestDto();
        dto.setStartDate("2020-01-01");
        dto.setEndDate("2199-01-01");
        dto.setDisplayWebPcYn("Y");
        dto.setDisplayWebMobileYn("Y");
        dto.setDisplayAppYn("Y");
        dto.setDisplayNonmemberYn("Y");
        dto.setAlwaysYn("N");
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.NO_LIMIT.getCode());
        dto.setUserGroupList(null);

        //when
        MessageCommEnum result = mockPromotionExhibitService.getExhibitValidation(dto);

        //then
        assertEquals(ExhibitEnums.GetValidation.PASS_VALIDATION, result);
    }

    @Test
    void getExhibitValidation_오류_이벤트없음() {
        //given, when
        MessageCommEnum result = mockPromotionExhibitService.getExhibitValidation(null);

        //then
        assertEquals(ExhibitEnums.GetValidation.NO_EXHIBIT, result);
    }

    @Test
    void getExhibitValidation_오류_시작일이전() {
        //given
        ExhibitValidationRequestDto dto = new ExhibitValidationRequestDto();
        dto.setStartDate("2100-01-01");
        dto.setEndDate("2100-01-01");
        dto.setDisplayWebPcYn("Y");
        dto.setDisplayWebMobileYn("Y");
        dto.setDisplayAppYn("Y");
        dto.setDisplayNonmemberYn("Y");
        dto.setAlwaysYn("N");
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.NO_LIMIT.getCode());
        dto.setUserGroupList(null);

        //when
        MessageCommEnum result = mockPromotionExhibitService.getExhibitValidation(dto);

        //then
        assertEquals(ExhibitEnums.GetValidation.NOT_DATE_BEFORE, result);
    }

    @Test
    void getExhibitValidation_오류_임직원전용() {
        //given
        ExhibitValidationRequestDto dto = new ExhibitValidationRequestDto();
        dto.setStartDate("2020-01-01");
        dto.setEndDate("2199-01-01");
        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());
        dto.setDisplayWebPcYn("Y");
        dto.setDisplayWebMobileYn("Y");
        dto.setDisplayAppYn("Y");
        dto.setDisplayNonmemberYn("Y");
        dto.setAlwaysYn("N");
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.EMPLOYEE_ONLY.getCode());
        dto.setUserGroupList(null);

        //when
        MessageCommEnum result = mockPromotionExhibitService.getExhibitValidation(dto);

        //then
        assertEquals(ExhibitEnums.GetValidation.ONLY_EMPLOYEE, result);
    }

    @Test
    void getExhibitValidation_오류_디바이스_PC() {
        //given
        ExhibitValidationRequestDto dto = new ExhibitValidationRequestDto();
        dto.setStartDate("2020-01-01");
        dto.setEndDate("2199-01-01");
        dto.setDisplayWebPcYn("N");
        dto.setDeviceType(GoodsEnums.DeviceType.PC.getCode());
        dto.setDisplayWebMobileYn("Y");
        dto.setDisplayAppYn("Y");
        dto.setDisplayNonmemberYn("Y");
        dto.setAlwaysYn("N");
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.NO_LIMIT.getCode());
        dto.setUserGroupList(null);

        //when
        MessageCommEnum result = mockPromotionExhibitService.getExhibitValidation(dto);

        //then
        assertEquals(ExhibitEnums.GetValidation.NOT_DEVICE, result);
    }

    @Test
    void getExhibitValidation_오류_디바이스_Mobile() {
        //given
        ExhibitValidationRequestDto dto = new ExhibitValidationRequestDto();
        dto.setStartDate("2020-01-01");
        dto.setEndDate("2199-01-01");
        dto.setDisplayWebPcYn("Y");
        dto.setDisplayWebMobileYn("N");
        dto.setDeviceType(GoodsEnums.DeviceType.MOBILE.getCode());
        dto.setDisplayAppYn("Y");
        dto.setDisplayNonmemberYn("Y");
        dto.setAlwaysYn("N");
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.NO_LIMIT.getCode());
        dto.setUserGroupList(null);

        //when
        MessageCommEnum result = mockPromotionExhibitService.getExhibitValidation(dto);

        //then
        assertEquals(ExhibitEnums.GetValidation.NOT_DEVICE, result);
    }

    @Test
    void getExhibitValidation_오류_디바이스_APP() {
        //given
        ExhibitValidationRequestDto dto = new ExhibitValidationRequestDto();
        dto.setStartDate("2020-01-01");
        dto.setEndDate("2199-01-01");
        dto.setDisplayWebPcYn("Y");
        dto.setDisplayWebMobileYn("Y");
        dto.setDisplayAppYn("N");
        dto.setDeviceType(GoodsEnums.DeviceType.APP.getCode());
        dto.setDisplayNonmemberYn("Y");
        dto.setAlwaysYn("N");
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.NO_LIMIT.getCode());
        dto.setUserGroupList(null);

        //when
        MessageCommEnum result = mockPromotionExhibitService.getExhibitValidation(dto);

        //then
        assertEquals(ExhibitEnums.GetValidation.NOT_DEVICE, result);
    }

    @Test
    void getExhibitValidation_오류_비회원노출여부() {
        //given
        ExhibitValidationRequestDto dto = new ExhibitValidationRequestDto();
        dto.setStartDate("2020-01-01");
        dto.setEndDate("2199-01-01");
        dto.setDisplayWebPcYn("Y");
        dto.setDisplayWebMobileYn("Y");
        dto.setDisplayAppYn("Y");
        dto.setDisplayNonmemberYn("N");
        dto.setUrUserId(0L);
        dto.setAlwaysYn("N");
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.NO_LIMIT.getCode());
        dto.setUserGroupList(null);

        //when
        MessageCommEnum result = mockPromotionExhibitService.getExhibitValidation(dto);

        //then
        assertEquals(ExhibitEnums.GetValidation.NOT_GROUP_NONE, result);
    }

    @Test
    void getExhibitValidation_오류_등급() {
        //given
        ExhibitValidationRequestDto dto = new ExhibitValidationRequestDto();
        dto.setStartDate("2020-01-01");
        dto.setEndDate("2199-01-01");
        dto.setDisplayWebPcYn("Y");
        dto.setDisplayWebMobileYn("Y");
        dto.setDisplayAppYn("Y");
        dto.setDisplayNonmemberYn("Y");
        dto.setAlwaysYn("N");
        dto.setUrGroupId(8L);
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.NO_LIMIT.getCode());
        ExhibitUserGroupByUserVo vo1 = new ExhibitUserGroupByUserVo();
        vo1.setUrGroupId(1L);
        ExhibitUserGroupByUserVo vo2 = new ExhibitUserGroupByUserVo();
        vo2.setUrGroupId(2L);
        dto.setUserGroupList(Arrays.asList(vo1, vo2));

        //when
        MessageCommEnum result = mockPromotionExhibitService.getExhibitValidation(dto);

        //then
        assertEquals(ExhibitEnums.GetValidation.NOT_GROUP, result);
    }

    @Test
    void addSelectExhibitValidation_정상() throws Exception {
        //given
        SelectOrderRequestDto dto = new SelectOrderRequestDto();
        dto.setBuyNowYn("N");
        List<SpCartPickGoodsRequestDto> pickGoodsList = new ArrayList<>();
        SpCartPickGoodsRequestDto goods = new SpCartPickGoodsRequestDto();
        goods.setIlGoodsId(123L);
        goods.setQty(1);
        pickGoodsList.add(goods);
        dto.setPickGoodsList(pickGoodsList);
        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());

        SelectByUserVo exhibitInfo = new SelectByUserVo();
        exhibitInfo.setIlGoodsId(123L);
        List<Long> goodsList = new ArrayList<>();
        goodsList.add(175L);
        goodsList.add(124L);
        exhibitInfo.setGoodsList(goodsList);
        exhibitInfo.setDefaultBuyCount(1);
        exhibitInfo.setEndYn("N");

        BasicSelectGoodsVo returnVo = new BasicSelectGoodsVo();
        returnVo.setSaleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode());
        returnVo.setUrWareHouseId(1L);
        returnVo.setIlGoodsId(1L);
        returnVo.setGoodsName("test");
        returnVo.setStockQty(100);
        ArrivalScheduledDateDto arrivalScheduledDateDto = new ArrivalScheduledDateDto();
        arrivalScheduledDateDto.setStock(100);
        returnVo.setArrivalScheduledDateDto(arrivalScheduledDateDto);
        given(mockGoodsGoodsBiz.getGoodsBasicInfo(any())).willReturn(returnVo);

        //when
        ApiResult<?> result = mockPromotionExhibitService.addSelectExhibitValidation(dto, exhibitInfo);

        //then
        assertEquals(ExhibitEnums.GetValidation.PASS_VALIDATION, result.getMessageEnum());
    }

    @Test
    void addSelectExhibitValidation_오류_정보없음() throws Exception {
        //given, when
        ApiResult<?> result = mockPromotionExhibitService.addSelectExhibitValidation(null, null);

        //then
        assertEquals(ExhibitEnums.GetValidation.NO_EXHIBIT, result.getMessageEnum());
    }

    @Test
    void addSelectExhibitValidation_오류_구매하기비회원() throws Exception {
        //given
        SelectOrderRequestDto dto = new SelectOrderRequestDto();
        dto.setBuyNowYn("Y");
        dto.setUrUserId(0L);
        SelectByUserVo exhibitInfo = new SelectByUserVo();
        exhibitInfo.setGoodsList(Collections.singletonList(123L));
        exhibitInfo.setDefaultBuyCount(1);

        //when
        ApiResult<?> result = mockPromotionExhibitService.addSelectExhibitValidation(dto, exhibitInfo);

        //then
        assertEquals(ExhibitEnums.GetValidation.NOT_GROUP_NONE, result.getMessageEnum());
    }

    @Test
    void addSelectExhibitValidation_오류_상품정보틀림() throws Exception {
        //given
        SelectOrderRequestDto dto = new SelectOrderRequestDto();
        dto.setBuyNowYn("N");
        List<SpCartPickGoodsRequestDto> pickGoodsList = new ArrayList<>();
        SpCartPickGoodsRequestDto goods = new SpCartPickGoodsRequestDto();
        goods.setIlGoodsId(12L);
        goods.setQty(1);
        pickGoodsList.add(goods);
        dto.setPickGoodsList(pickGoodsList);
        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());

        SelectByUserVo exhibitInfo = new SelectByUserVo();
        List<Long> goodsList = new ArrayList<>();
        goodsList.add(175L);
        goodsList.add(124L);
        exhibitInfo.setGoodsList(goodsList);
        exhibitInfo.setDefaultBuyCount(1);
        exhibitInfo.setIlGoodsId(123L);
        exhibitInfo.setEndYn("N");

        BasicSelectGoodsVo returnVo = new BasicSelectGoodsVo();
        returnVo.setSaleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode());
        returnVo.setUrWareHouseId(1L);
        returnVo.setIlGoodsId(1L);
        given(mockGoodsGoodsBiz.getGoodsBasicInfo(any())).willReturn(returnVo);

        ArrivalScheduledDateDto arrivalScheduledDateDto = new ArrivalScheduledDateDto();
        arrivalScheduledDateDto.setStock(100);
        given(mockGoodsGoodsBiz.getLatestArrivalScheduledDateDto(any(), any(), anyBoolean(), any())).willReturn(arrivalScheduledDateDto);

        //when
        ApiResult<?> result = mockPromotionExhibitService.addSelectExhibitValidation(dto, exhibitInfo);

        //then
        assertEquals(ExhibitEnums.GetValidation.NOT_GOODS, result.getMessageEnum());
    }

    @Test
    void addSelectExhibitValidation_오류_상품별제한수량() throws Exception {
        //given
        SelectOrderRequestDto dto = new SelectOrderRequestDto();
        dto.setBuyNowYn("N");
        List<SpCartPickGoodsRequestDto> pickGoodsList = new ArrayList<>();
        SpCartPickGoodsRequestDto goods = new SpCartPickGoodsRequestDto();
        goods.setIlGoodsId(123L);
        goods.setQty(5);
        pickGoodsList.add(goods);
        dto.setPickGoodsList(pickGoodsList);
        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());

        SelectByUserVo exhibitInfo = new SelectByUserVo();
        List<Long> goodsList = new ArrayList<>();
        goodsList.add(175L);
        goodsList.add(124L);
        exhibitInfo.setGoodsList(goodsList);
        exhibitInfo.setGoodsBuyLimitCount(1);
        exhibitInfo.setDefaultBuyCount(5);
        exhibitInfo.setIlGoodsId(123L);
        exhibitInfo.setEndYn("N");

        BasicSelectGoodsVo returnVo = new BasicSelectGoodsVo();
        returnVo.setSaleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode());
        returnVo.setUrWareHouseId(1L);
        returnVo.setIlGoodsId(1L);
        given(mockGoodsGoodsBiz.getGoodsBasicInfo(any())).willReturn(returnVo);

        ArrivalScheduledDateDto arrivalScheduledDateDto = new ArrivalScheduledDateDto();
        arrivalScheduledDateDto.setStock(100);
        given(mockGoodsGoodsBiz.getLatestArrivalScheduledDateDto(any(), any(), anyBoolean(), any())).willReturn(arrivalScheduledDateDto);

        //when
        ApiResult<?> result = mockPromotionExhibitService.addSelectExhibitValidation(dto, exhibitInfo);

        //then
        assertEquals(ExhibitEnums.GetValidation.NOT_QTY, result.getMessageEnum());
    }

    @Test
    void addSelectExhibitValidation_오류_수량다름() throws Exception {
        //given
        SelectOrderRequestDto dto = new SelectOrderRequestDto();
        dto.setBuyNowYn("N");
        List<SpCartPickGoodsRequestDto> pickGoodsList = new ArrayList<>();
        SpCartPickGoodsRequestDto goods = new SpCartPickGoodsRequestDto();
        goods.setIlGoodsId(123L);
        goods.setQty(5);
        pickGoodsList.add(goods);
        dto.setPickGoodsList(pickGoodsList);
        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());

        SelectByUserVo exhibitInfo = new SelectByUserVo();
        List<Long> goodsList = new ArrayList<>();
        goodsList.add(175L);
        goodsList.add(124L);
        exhibitInfo.setGoodsList(goodsList);
        exhibitInfo.setDefaultBuyCount(1);
        exhibitInfo.setIlGoodsId(123L);
        exhibitInfo.setEndYn("N");

        BasicSelectGoodsVo returnVo = new BasicSelectGoodsVo();
        returnVo.setSaleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode());
        returnVo.setUrWareHouseId(1L);
        returnVo.setIlGoodsId(1L);
        given(mockGoodsGoodsBiz.getGoodsBasicInfo(any())).willReturn(returnVo);

        ArrivalScheduledDateDto arrivalScheduledDateDto = new ArrivalScheduledDateDto();
        arrivalScheduledDateDto.setStock(100);
        given(mockGoodsGoodsBiz.getLatestArrivalScheduledDateDto(any(), any(), anyBoolean(), any())).willReturn(arrivalScheduledDateDto);

        //when
        ApiResult<?> result = mockPromotionExhibitService.addSelectExhibitValidation(dto, exhibitInfo);

        //then
        assertEquals(ExhibitEnums.GetValidation.NOT_QTY, result.getMessageEnum());
    }

    @Test
    void addSelectExhibitValidation_오류_재고없음() throws Exception {
        //given
        SelectOrderRequestDto dto = new SelectOrderRequestDto();
        dto.setBuyNowYn("Y");
        dto.setUrUserId(100L);
        List<SpCartPickGoodsRequestDto> pickGoodsList = new ArrayList<>();
        SpCartPickGoodsRequestDto goods = new SpCartPickGoodsRequestDto();
        goods.setIlGoodsId(123L);
        goods.setQty(1);
        pickGoodsList.add(goods);
        dto.setPickGoodsList(pickGoodsList);
        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());

        SelectByUserVo exhibitInfo = new SelectByUserVo();
        List<Long> goodsList = new ArrayList<>();
        goodsList.add(175L);
        goodsList.add(124L);
        exhibitInfo.setGoodsList(goodsList);
        exhibitInfo.setDefaultBuyCount(1);
        exhibitInfo.setIlGoodsId(123L);
        exhibitInfo.setEndYn("N");

        BasicSelectGoodsVo returnVo = new BasicSelectGoodsVo();
        returnVo.setSaleStatus(GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_MANAGER.getCode());
        returnVo.setUrWareHouseId(1L);
        returnVo.setIlGoodsId(1L);
        given(mockGoodsGoodsBiz.getGoodsBasicInfo(any())).willReturn(returnVo);

        ArrivalScheduledDateDto arrivalScheduledDateDto = new ArrivalScheduledDateDto();
        arrivalScheduledDateDto.setStock(100);
        given(mockGoodsGoodsBiz.getLatestArrivalScheduledDateDto(any(), any(), anyBoolean(), any())).willReturn(arrivalScheduledDateDto);

        //when
        ApiResult<?> result = mockPromotionExhibitService.addSelectExhibitValidation(dto, exhibitInfo);

        //then
        assertEquals(ExhibitEnums.GetValidation.NO_SALE, result.getMessageEnum());
    }

    @Test
    void addGreenJuiceExhibitValidation_오류_데이터없음() throws Exception {
        //given, when
        ExhibitEnums.GetValidation result = mockPromotionExhibitService.addGreenJuiceExhibitValidation(null);

        //then
        assertEquals(ExhibitEnums.GetValidation.NO_EXHIBIT, result);
    }

    @Test
    void addGreenJuiceExhibitValidation_오류_비회원() throws Exception {
        //given
        GreenJuiceOrderRequestDto dto = new GreenJuiceOrderRequestDto();
        dto.setUrUserId(0L);

        //when
        ExhibitEnums.GetValidation result = mockPromotionExhibitService.addGreenJuiceExhibitValidation(dto);

        //then
        assertEquals(ExhibitEnums.GetValidation.NOT_GROUP_NONE, result);
    }

    @Test
    void addGreenJuiceExhibitValidation_오류_배송권역() throws Exception {
        //given -- 해당기능 SPEC OUT
//        GreenJuiceOrderRequestDto dto = new GreenJuiceOrderRequestDto();
//        dto.setUrUserId(100L);
//        List<SpCartPickGoodsRequestDto> pickGoodsList = new ArrayList<>();
//        SpCartPickGoodsRequestDto goods = new SpCartPickGoodsRequestDto();
//        goods.setIlGoodsId(123L);
//        goods.setQty(1);
//        pickGoodsList.add(goods);
//        dto.setPickGoodsList(pickGoodsList);
//        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());
//
//        GetSessionShippingResponseDto shippingAddress = new GetSessionShippingResponseDto();
//        shippingAddress.setReceiverZipCode("test");
//        shippingAddress.setBuildingCode("test");
//        given(mockUserCertificationBiz.getSessionShipping()).willReturn(shippingAddress);
//
//        BasicSelectGoodsVo goodsResultVo = new BasicSelectGoodsVo();
//        goodsResultVo.setUrSupplierId(1L);
//        goodsResultVo.setIlItemCode("test");
//        given(mockGoodsGoodsBiz.getGoodsBasicInfo(any())).willReturn(goodsResultVo);
//
//        List<String> storeDeliveralbeItemTypeBySupplierIdList = new ArrayList<>();
//        storeDeliveralbeItemTypeBySupplierIdList.add("test");
//        given(mockGoodsGoodsBiz.getStoreDeliverableItemTypeBySupplierId(any())).willReturn(storeDeliveralbeItemTypeBySupplierIdList);
//
//        given(mockStoreDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(any(), anyList(), any(), any())).willReturn(null);
//
//        //when
//        ExhibitEnums.GetValidation result = mockPromotionExhibitService.addGreenJuiceExhibitValidation(dto);
//
//        //then
//        assertEquals(ExhibitEnums.GetValidation.NO_SHIPPING_ADDRESS, result);
    }

    @Test
    void addGreenJuiceExhibitValidation_정상() throws Exception {
        //given
        GreenJuiceOrderRequestDto dto = new GreenJuiceOrderRequestDto();
        dto.setUrUserId(100L);
        List<SpCartPickGoodsRequestDto> pickGoodsList = new ArrayList<>();
        SpCartPickGoodsRequestDto goods = new SpCartPickGoodsRequestDto();
        goods.setIlGoodsId(123L);
        goods.setQty(1);
        pickGoodsList.add(goods);
        dto.setPickGoodsList(pickGoodsList);
        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());

        GetSessionShippingResponseDto shippingAddress = new GetSessionShippingResponseDto();
        shippingAddress.setReceiverZipCode("test");
        shippingAddress.setBuildingCode("test");
        given(mockUserCertificationBiz.getSessionShipping()).willReturn(shippingAddress);

        BasicSelectGoodsVo goodsResultVo = new BasicSelectGoodsVo();
        goodsResultVo.setUrSupplierId(1L);
        goodsResultVo.setIlItemCode("test");
        given(mockGoodsGoodsBiz.getGoodsBasicInfo(any())).willReturn(goodsResultVo);

        List<String> storeDeliveralbeItemTypeBySupplierIdList = new ArrayList<>();
        storeDeliveralbeItemTypeBySupplierIdList.add("test");
        given(mockGoodsGoodsBiz.getStoreDeliverableItemTypeBySupplierId(any())).willReturn(storeDeliveralbeItemTypeBySupplierIdList);

        ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = new ShippingPossibilityStoreDeliveryAreaDto();
        shippingPossibilityStoreDeliveryAreaInfo.setStoreDeliveryIntervalType("test");
        given(mockStoreDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(any(), anyList(), any(), any())).willReturn(shippingPossibilityStoreDeliveryAreaInfo);

        //when
        ExhibitEnums.GetValidation result = mockPromotionExhibitService.addGreenJuiceExhibitValidation(dto);

        //then
        assertEquals(ExhibitEnums.GetValidation.PASS_VALIDATION, result);
    }

    @Test
    void getGiftFromGoods() throws Exception {
        //given
        GiftListRequestDto dto = GiftListRequestDto.builder()
                .ilGoodsId(15380L)
                .deviceType(GoodsEnums.DeviceType.PC.getCode())
                .urGroupId(8L)
                .dpBrandId(12L)
                .userStatus(UserEnums.UserStatusType.MEMBER.getCode())
                .build();

        //when
        List<GiftListVo> result = promotionExhibitService.getGiftList(dto);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getSelectExhibit_조회_정상() throws Exception {
        //given
        Long evExhibitId = 5L;

        //when
        SelectExhibitVo result = promotionExhibitService.getSelectExhibit(evExhibitId);

        //then
        assertEquals(evExhibitId, result.getEvExhibitId());
    }

    @Test
    void getSelectExhibitValidation_오류_시작일이전() {
        //given
        SelectExhibitVo dto = new SelectExhibitVo();
        dto.setAlwaysYn("N");
        dto.setStartDate("2999-01-01");
        dto.setEndDate("2999-01-02");

        //when
        MessageCommEnum result = promotionExhibitService.getSelectExhibitValidation(dto);

        //then
        assertEquals(ExhibitEnums.GetValidation.NO_EXHIBIT, result);
    }

    @Test
    void getSelectExhibitValidation_오류_시작일이후() {
        //given
        SelectExhibitVo dto = new SelectExhibitVo();
        dto.setAlwaysYn("N");
        dto.setStartDate("1999-01-01");
        dto.setEndDate("1999-01-02");

        //when
        MessageCommEnum result = promotionExhibitService.getSelectExhibitValidation(dto);

        //then
        assertEquals(ExhibitEnums.GetValidation.NO_EXHIBIT, result);
    }

    @Test
    void getSelectExhibitValidation_오류_삭제여부() {
        //given
        SelectExhibitVo dto = new SelectExhibitVo();
        dto.setAlwaysYn("Y");
        dto.setStartDate("1999-01-01");
        dto.setEndDate("1999-01-02");
        dto.setDelYn("Y");

        //when
        MessageCommEnum result = promotionExhibitService.getSelectExhibitValidation(dto);

        //then
        assertEquals(ExhibitEnums.GetValidation.NO_EXHIBIT, result);
    }

    @Test
    void getSelectExhibitValidation_오류_사용여부() {
        //given
        SelectExhibitVo dto = new SelectExhibitVo();
        dto.setAlwaysYn("Y");
        dto.setStartDate("1999-01-01");
        dto.setEndDate("1999-01-02");
        dto.setDelYn("N");
        dto.setUseYn("N");

        //when
        MessageCommEnum result = promotionExhibitService.getSelectExhibitValidation(dto);

        //then
        assertEquals(ExhibitEnums.GetValidation.NO_EXHIBIT, result);
    }

    @Test
    void getSelectExhibitValidation_정상() {
        //given
        SelectExhibitVo dto = new SelectExhibitVo();
        dto.setAlwaysYn("Y");
        dto.setStartDate("1999-01-01");
        dto.setEndDate("1999-01-02");
        dto.setDelYn("N");
        dto.setUseYn("Y");

        //when
        MessageCommEnum result = promotionExhibitService.getSelectExhibitValidation(dto);

        //then
        assertEquals(BaseEnums.Default.SUCCESS, result);
    }

    @Test
    void getGreenJuiceGoods_조회_정상() throws Exception {
        //given
        Long urSuppliedId = GoodsConstants.GREEN_JUICE_UR_SUPPLIER_ID;

        //when
        List<Long> result = promotionExhibitService.getGreenJuiceGoods(urSuppliedId);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getGiftByUser_조회_정상() throws Exception {
        //given
        Long evExhibitId = 36L;
        String deviceType = "PC";

        //when
        GiftByUserVo result = promotionExhibitService.getGiftByUser(evExhibitId, deviceType);

        //then
        assertTrue(result.getTitle().length() > 0);
    }

    @Test
    void getExhibitTitle_조회_정상() throws Exception {
        //given
        Long evExhibitId = 1L;

        //when
        ExhibitInfoFromMetaVo result = promotionExhibitService.getExhibitInfoFromMeta(evExhibitId);

        //then
        assertNotNull(result);
    }

}