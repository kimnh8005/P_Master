package kr.co.pulmuone.v1.goods.goods.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import kr.co.pulmuone.v1.goods.goods.dto.*;
import kr.co.pulmuone.v1.goods.goods.dto.vo.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.CompanyEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsColumnComment;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDiscountType;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsEtcColumnComment;
import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsRegistCategoryMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.discount.service.GoodsDiscountBiz;
import kr.co.pulmuone.v1.goods.goods.dto.*;
import kr.co.pulmuone.v1.goods.goods.dto.vo.*;
import kr.co.pulmuone.v1.goods.item.dto.ItemReturnPeriodResponseDto;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemRegisterBiz;
import kr.co.pulmuone.v1.goods.price.service.GoodsPriceBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class GoodsRegistServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	GoodsRegistService goodsRegistService;

	@Autowired
	public GoodsDiscountBiz goodsDiscountBiz;

	@Autowired
	GoodsPriceBiz goodsPriceBiz;

	@Autowired
	GoodsRegistBizImpl goodsRegistBizImpl;

	@Autowired
	public GoodsRegistCategoryMapper goodsRegistCategoryMapper;

	@Autowired
	private GoodsItemRegisterBiz goodsItemRegisterBiz;

	int changeTranNum = 0;
	int sameValue = 0;
	GoodsEtcColumnComment goodsEtcColumnComment = null;

	@BeforeEach
	void setUp() {
		preLogin();
	}

	@Test
	void 마스터품목_내역() {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlItemCode("0040102");
		goodsRegistRequestDto.setUrWarehouseId("85");

		GoodsRegistVo itemDetail = goodsRegistService.getItemDetail(goodsRegistRequestDto);

		assertNotNull(itemDetail);
	}

	@Test
	void 상품정보_제공고시_품목내역() {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlItemCode("0915553");

		List<GoodsRegistVo> itemSpecValueList = goodsRegistService.getItemSpecValueList(goodsRegistRequestDto);

		assertTrue(CollectionUtils.isNotEmpty(itemSpecValueList));
	}

	@Test
	void 상품_영양정보_품목내역() {
		String ilItemCode = "0300059";

		List<GoodsRegistVo> iItemNutritionList = goodsRegistService.getItemNutritionList(ilItemCode);

		assertTrue(iItemNutritionList.size() > 0);
	}

	@Test
	void 상품_인증정보_품목내역() {
		String ilItemCode = "0043929";

		List<GoodsRegistVo> itemCertificationList = goodsRegistService.getItemCertificationList(ilItemCode);

		assertTrue(CollectionUtils.isNotEmpty(itemCertificationList));
	}

	@Test
	void 상품_상세_이미지_품목내역() {
		String ilItemCode = "0915553";

		List<GoodsRegistVo> itemImageList = goodsRegistService.getItemImageList(ilItemCode);

		assertTrue(CollectionUtils.isNotEmpty(itemImageList));
	}

	@Test
	void 상품등록_성공() {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlItemCode("0915553");
		goodsRegistRequestDto.setUrWarehouseId("120");
		goodsRegistRequestDto.setSaleType(GoodsEnums.GoodsType.NORMAL.getCode());
		goodsRegistRequestDto.setMdRecommendYn("N");

		//상품명
		goodsRegistRequestDto.setGoodsName("Junit 테스트 상품");
		goodsRegistRequestDto.setPromotionName("Junit 프로모션");
		goodsRegistRequestDto.setPromotionNameStartDate("2020-10-20 00:00");
		goodsRegistRequestDto.setPromotionNameEndDate("2999-10-20 00:00");
		goodsRegistRequestDto.setGoodsDesc("Junit 상품설명");
		goodsRegistRequestDto.setSearchKeyword("Junit키워드1, Junit키워드2");
		goodsRegistRequestDto.setPackageUnitDisplayYn("Y");

		//구매허용범위 체크박스 값 세팅
		goodsRegistRequestDto.setPurchaseMemberYn("N");
		goodsRegistRequestDto.setPurchaseEmployeeYn("N");
		goodsRegistRequestDto.setPurchaseNonmemberYn("N");

		//판매허용범위(PC/Mobile) 체크박스 값 세팅
		goodsRegistRequestDto.setDisplayWebPcYn("N");
		goodsRegistRequestDto.setDisplayWebMobileYn("N");
		goodsRegistRequestDto.setDisplayAppYn("N");

		//판매/전시 > 전시 상태
		goodsRegistRequestDto.setDisplayYn("Y");

		//판매/전시 > 판매 기간
		goodsRegistRequestDto.setSaleStartDate("2020-10-19 00:00");
		goodsRegistRequestDto.setSaleEndDate("2999-12-31 00:00");

		//판매/전시 > 판매 상태
		goodsRegistRequestDto.setSaleStatus(GoodsEnums.SaleStatus.SAVE.getCode());

		//가격정보 > 단위별 용량정보
		goodsRegistRequestDto.setAutoDisplaySizeYn("Y");

		//판매 정보 > 판매 유형
		goodsRegistRequestDto.setGoodsType(GoodsEnums.GoodsType.NORMAL.getCode());

		//판매 정보 > 판매 유형 > 매장 판매 여부
		goodsRegistRequestDto.setSaleShopYn("Y");

		//혜택/구매 정보 > 혜택 설정 > 쿠폰 사용 허용
		goodsRegistRequestDto.setCouponUseYn("Y");

		//혜택/구매 정보 > 구매 제한 설정 > 최소 구매
		goodsRegistRequestDto.setLimitMinimumCnt("1");
		goodsRegistRequestDto.setLimitMaximumType(GoodsEnums.PurchaseLimitMaxType.UNLIMIT.getCode());

		//상세 하단 공지1 첨부 이미지 URL
//		int noticeBelow1ImageCnt = goodsRegistRequestDto.getNoticeBelow1ImageUploadResultList().size();
//
//		if(goodsRegistRequestDto.getNoticeBelow1ImageUploadResultList() != null && noticeBelow1ImageCnt > 0) {
//			for (UploadFileDto uploadFileDto : goodsRegistRequestDto.getNoticeBelow1ImageUploadResultList()) {
//				goodsRegistRequestDto.setNoticeBelow1ImageUrl(uploadFileDto.getServerSubPath() + uploadFileDto.getPhysicalFileName());
//			}
//		}

		//상세 하단 공지2 첨부 이미지 URL
//		int noticeBelow2ImageCnt = goodsRegistRequestDto.getNoticeBelow2ImageUploadResultList().size();
//
//		if(goodsRegistRequestDto.getNoticeBelow2ImageUploadResultList() != null && noticeBelow2ImageCnt > 0) {
//			for (UploadFileDto uploadFileDto : goodsRegistRequestDto.getNoticeBelow2ImageUploadResultList()) {
//				goodsRegistRequestDto.setNoticeBelow2ImageUrl(uploadFileDto.getServerSubPath() + uploadFileDto.getPhysicalFileName());
//			}
//		}

		goodsRegistRequestDto.setCreateId("1");

		//상품 마스터 항목 등록
		int addGoodsInt = 0;
		try {
			addGoodsInt = goodsRegistService.addGoods(goodsRegistRequestDto);
		}
		catch(Exception e) {
			log.info(e.getMessage());
		}

		assertTrue(addGoodsInt > 0);
	}

	@Test
	void 상품등록_실패() {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		//상품 마스터 항목 등록
		int addGoodsInt = 0;
		try {
			addGoodsInt = goodsRegistService.addGoods(goodsRegistRequestDto);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}

		assertFalse(addGoodsInt > 0);
	}

	@Test
	void 카테고리저장_성공() {
		GoodsRegistCategoryRequestDto goodsRegistCategoryRequestDto = new GoodsRegistCategoryRequestDto();

		goodsRegistCategoryRequestDto.setIlGoodsId("10000041");
		goodsRegistCategoryRequestDto.setIlCtgryId("4859");
		goodsRegistCategoryRequestDto.setMallDiv("MALL_DIV.PULMUONE");
		goodsRegistCategoryRequestDto.setBasicYn("Y");
		goodsRegistCategoryRequestDto.setCreateId("1");

		int addCategoryInt = 0;

		try {
			addCategoryInt = goodsRegistService.addGoodsCategory(goodsRegistCategoryRequestDto);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}

		assertTrue(addCategoryInt > 0);
	}

	@Test
	void 카테고리저장_실패() {
		GoodsRegistCategoryRequestDto goodsRegistCategoryRequestDto = new GoodsRegistCategoryRequestDto();

		int addCategoryInt = 0;

		try {
			addCategoryInt = goodsRegistService.addGoodsCategory(goodsRegistCategoryRequestDto);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}

		assertFalse(addCategoryInt > 0);
	}

	@Test
	void 할인설정저장_성공() throws Exception {
		GoodsDiscountRequestDto goodsDiscountRequestDto = new GoodsDiscountRequestDto();

		goodsDiscountRequestDto.setGoodsId("10000041");
		goodsDiscountRequestDto.setDiscountTypeCode(GoodsEnums.GoodsDiscountType.PRIORITY.getCode());
		goodsDiscountRequestDto.setDiscountStartDateTime("2020-10-23 00:00");
		goodsDiscountRequestDto.setDiscountEndDateTime("2020-10-25 23:59");
		goodsDiscountRequestDto.setDiscountMethodTypeCode(GoodsEnums.GoodsDiscountMethodType.FIXED_PRICE.getCode());
		goodsDiscountRequestDto.setDiscountRatio(0);
		goodsDiscountRequestDto.setDiscountSalePrice(14000);
		goodsDiscountRequestDto.setCreateId("1");

		int addDiscountInt = 0;

		try {
			addDiscountInt = goodsRegistService.addGoodsDiscount(goodsDiscountRequestDto);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}

		assertTrue(addDiscountInt > 0);
	}

	@Test
	void 할인설정저장_실패() throws Exception {
		GoodsDiscountRequestDto goodsDiscountRequestDto = new GoodsDiscountRequestDto();

		int addDiscountInt = 0;

		try {
			addDiscountInt = goodsRegistService.addGoodsDiscount(goodsDiscountRequestDto);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}

		assertFalse(addDiscountInt > 0);
	}

	@Test
	void 할인정보연동_성공() throws Exception {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlGoodsId("10000041");
		goodsRegistRequestDto.setIlItemCode("0933246");
		goodsRegistRequestDto.setUrSupplierId("2");
		goodsRegistRequestDto.setErpItemLinkYn("Y");

		int batchInt = 0;

		if(goodsRegistRequestDto.getIlItemCode() != null && "2".equals(goodsRegistRequestDto.getUrSupplierId()) && "Y".equals(goodsRegistRequestDto.getErpItemLinkYn())) {
			String ilItemCode = goodsRegistRequestDto.getIlItemCode();
			String ilGoodsId = goodsRegistRequestDto.getIlGoodsId();
			ApiResult<?> apiResult = goodsDiscountBiz.addGoodsDiscountWithErpIfPriceBatch(ilGoodsId, ilItemCode);	//현재 품목 가격 ERP Interface에서 가져올 정보가 없음(날짜 조건이 맞지 않음)
			batchInt = Integer.parseInt(String.valueOf(apiResult.getData()));
		}

		assertTrue(batchInt > 0);
	}

	@Test
	void 할인정보연동_실패() throws Exception {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		int batchInt = 0;

		if(goodsRegistRequestDto.getIlItemCode() != null && "2".equals(goodsRegistRequestDto.getUrSupplierId()) && "Y".equals(goodsRegistRequestDto.getErpItemLinkYn())) {
			String ilItemCode = goodsRegistRequestDto.getIlItemCode();
			String ilGoodsId = goodsRegistRequestDto.getIlGoodsId();
			ApiResult<?> apiResult = goodsDiscountBiz.addGoodsDiscountWithErpIfPriceBatch(ilGoodsId, ilItemCode);
			batchInt = Integer.parseInt(String.valueOf(apiResult.getData()));
		}

		assertFalse(batchInt > 0);
	}

	@Test
	void 예약판매옵션설정_등록_성공() throws Exception {
		GoodsRegistReservationOptionDto goodsRegistReservationOptionDto = new GoodsRegistReservationOptionDto();

		goodsRegistReservationOptionDto.setIlGoodsId("10000041");
		goodsRegistReservationOptionDto.setSaleSequance("1");
		goodsRegistReservationOptionDto.setReservationStartDate("2020-10-21 00:00");
		goodsRegistReservationOptionDto.setReservationEndDate("2020-10-21 23:59:59");
		goodsRegistReservationOptionDto.setStockQuantity("1");
		//주문수집I/F일 계산 로직 추후 적용
		goodsRegistReservationOptionDto.setOrderIfDate("2020-10-22");
		//출고예정, 도착예정 계산 로직 추후 적용
		goodsRegistReservationOptionDto.setReleaseDate("2020-10-24");
		goodsRegistReservationOptionDto.setArriveDate("2020-10-25");
		goodsRegistReservationOptionDto.setCreateId("1");

		int goodsRegistReservationOptionInt = 0;

		try {
			goodsRegistReservationOptionInt = goodsRegistService.addGoodsReservationOption(goodsRegistReservationOptionDto);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}

		assertTrue(goodsRegistReservationOptionInt > 0);
	}

	@Test
	void 예약판매옵션설정_등록_실패() throws Exception {
		GoodsRegistReservationOptionDto goodsRegistReservationOptionDto = new GoodsRegistReservationOptionDto();

		int goodsRegistReservationOptionInt = 0;

		try {
			goodsRegistReservationOptionInt = goodsRegistService.addGoodsReservationOption(goodsRegistReservationOptionDto);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}

		assertFalse(goodsRegistReservationOptionInt > 0);
	}

	@Test
	void 배송정책_등록_성공() throws Exception {
		GoodsRegistShippingTemplateDto goodsRegistShippingTemplateDto = new GoodsRegistShippingTemplateDto();

		goodsRegistShippingTemplateDto.setIlGoodsId("10000041");
		goodsRegistShippingTemplateDto.setItemWarehouseShippingTemplateList("146");
		goodsRegistShippingTemplateDto.setUrWarehouseId("120");
		goodsRegistShippingTemplateDto.setCreateId("1");

		int addGoodsShippingTemplateInt = 0;

		try {
			addGoodsShippingTemplateInt = goodsRegistService.addGoodsShippingTemplate(goodsRegistShippingTemplateDto);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}

		assertTrue(addGoodsShippingTemplateInt > 0);
	}

	@Test
	void 배송정책_등록_실패() throws Exception {
		GoodsRegistShippingTemplateDto goodsRegistShippingTemplateDto = new GoodsRegistShippingTemplateDto();

		int addGoodsShippingTemplateInt = 0;

		try {
			addGoodsShippingTemplateInt = goodsRegistService.addGoodsShippingTemplate(goodsRegistShippingTemplateDto);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}

		assertFalse(addGoodsShippingTemplateInt > 0);
	}

	@Test
	void 추가상품_등록_성공() throws Exception {
		GoodsRegistAdditionalGoodsDto goodsRegistAdditionalGoodsDto = new GoodsRegistAdditionalGoodsDto();

		goodsRegistAdditionalGoodsDto.setIlGoodsId("10000041");
		goodsRegistAdditionalGoodsDto.setTargetGoodsId("15340");
		goodsRegistAdditionalGoodsDto.setSalePrice("9000");
		//추후 재고, 원가, 정상가 항목 추가 되어야 함
		goodsRegistAdditionalGoodsDto.setCreateId("1");

		int addGoodsAdditionalGoodsMappingInt = 0;

		try {
			addGoodsAdditionalGoodsMappingInt = goodsRegistService.addGoodsAdditionalGoodsMapping(goodsRegistAdditionalGoodsDto);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}

		assertTrue(addGoodsAdditionalGoodsMappingInt > 0);
	}

	@Test
	void 추가상품_등록_실패() throws Exception {
		GoodsRegistAdditionalGoodsDto goodsRegistAdditionalGoodsDto = new GoodsRegistAdditionalGoodsDto();

		int addGoodsAdditionalGoodsMappingInt = 0;

		try {
			addGoodsAdditionalGoodsMappingInt = goodsRegistService.addGoodsAdditionalGoodsMapping(goodsRegistAdditionalGoodsDto);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
		assertFalse(addGoodsAdditionalGoodsMappingInt > 0);
	}

	@Test
	void 품목별_출고처별_배송유형_리스트() {
		GoodsRegistItemWarehouseRequestDto goodsRegistItemWarehouseRequestDto = new GoodsRegistItemWarehouseRequestDto();

		goodsRegistItemWarehouseRequestDto.setIlItemCode("0915553");
		goodsRegistItemWarehouseRequestDto.setUrWarehouseId("120");

		List<GoodsRegistItemWarehouseVo> itemWarehouseList = goodsRegistService.itemWarehouseList(goodsRegistItemWarehouseRequestDto);

		assertTrue(CollectionUtils.isNotEmpty(itemWarehouseList));
	}

	@Test
	void 품목별_출고처별_배송정책_리스트() {
		GoodsRegistItemWarehouseRequestDto goodsRegistItemWarehouseRequestDto = new GoodsRegistItemWarehouseRequestDto();

		goodsRegistItemWarehouseRequestDto.setIlItemCode("0915553");
		goodsRegistItemWarehouseRequestDto.setUrWarehouseId("120");

		List<GoodsRegistItemWarehouseVo> itemWarehouseShippingTemplateList = goodsRegistService.itemWarehouseShippingTemplateList(goodsRegistItemWarehouseRequestDto);

		assertTrue(CollectionUtils.isNotEmpty(itemWarehouseShippingTemplateList));
	}

	@Test
	void 마스터_품목_가격정보() throws Exception {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlItemCode("0915553");

		List<GoodsPriceInfoResultVo> itemPriceList = goodsRegistService.itemPriceList(goodsRegistRequestDto.getIlItemCode());

		assertTrue(CollectionUtils.isNotEmpty(itemPriceList));
	}

	@Test
	void 상품_내역() {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlGoodsId("1");

		GoodsRegistVo ilGoodsDetail = goodsRegistService.goodsDetail(goodsRegistRequestDto);	//상품 내역

		assertNotNull(ilGoodsDetail);
	}

	@Test
	void 카테고리리스트() {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlGoodsId("15347");
		goodsRegistRequestDto.setMallDiv("MALL_DIV.PULMUONE");

		List<GoodsRegistCategoryVo> ilGoodsDisplayCategoryList = goodsRegistCategoryMapper.ilGoodsDisplayCategoryList(goodsRegistRequestDto);

		assertTrue(CollectionUtils.isNotEmpty(ilGoodsDisplayCategoryList));
	}

	@Test
	void 판매_가격정보_내역() throws Exception {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlGoodsId("1");
		goodsRegistRequestDto.setTaxYn("Y");

		List<GoodsPriceInfoResultVo> goodsPrice = goodsRegistService.goodsPrice(goodsRegistRequestDto.getIlGoodsId(), goodsRegistRequestDto.getTaxYn());

		assertTrue(CollectionUtils.isNotEmpty(goodsPrice));
	}

	@Test
	void 임직원_할인_가격정보_내역() throws Exception {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlItemCode("0300557");
		goodsRegistRequestDto.setIlGoodsId("15758");
		goodsRegistRequestDto.setTaxYn("Y");

		List<GoodsPriceInfoResultVo> goodsEmployeePrice = goodsRegistService.goodsEmployeePrice(goodsRegistRequestDto.getIlItemCode(), goodsRegistRequestDto.getIlGoodsId(), goodsRegistRequestDto.getTaxYn());

		assertTrue(CollectionUtils.isNotEmpty(goodsEmployeePrice));
	}

	@Test
	void 행사_할인_내역() throws Exception {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlGoodsId("90018174");

		List<GoodsPriceInfoResultVo> goodsDiscountPriorityList = goodsRegistService.goodsDiscountList(goodsRegistRequestDto.getIlGoodsId(), GoodsDiscountType.PRIORITY.getCode());

		assertTrue(CollectionUtils.isNotEmpty(goodsDiscountPriorityList));
	}

	@Test
	void 예약상품_옵션_설정_리스트() throws Exception {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlGoodsId("15311");

		List<GoodsRegistReserveOptionVo> goodsReservationOptionList = goodsRegistService.goodsReservationOptionList(goodsRegistRequestDto);

		assertTrue(CollectionUtils.isNotEmpty(goodsReservationOptionList));
	}

	@Test
	void 배송정책_설정_값() throws Exception {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlGoodsId("10000040");
		goodsRegistRequestDto.setUrWarehouseId("120");

		List<GoodsRegistShippingTemplateVo> goodsShippingTemplateList = goodsRegistService.goodsShippingTemplateList(goodsRegistRequestDto);
		assertNotNull(goodsShippingTemplateList);
	}

	@Test
	void 추가상품_리스트() {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();
		goodsRegistRequestDto.setIlGoodsId("15584");
		goodsRegistRequestDto.setUrWarehouseId("103");

		List<GoodsRegistAdditionalGoodsVo> goodsAdditionalGoodsMappingList = goodsRegistService.goodsAdditionalGoodsMappingList(goodsRegistRequestDto);

		assertTrue(CollectionUtils.isNotEmpty(goodsAdditionalGoodsMappingList));
	}

	@Test
	void 상품_중복_확인() {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlItemCode("0915553");
		goodsRegistRequestDto.setUrWarehouseId("120");

		GoodsRegistVo duplicateGoods = goodsRegistService.duplicateGoods(goodsRegistRequestDto);	//상품 내역
		assertNotNull(duplicateGoods);
	}

	@Test
	void 카테고리_불러오기() {
		GoodsRegistCategoryRequestDto goodsRegistCategoryRequestDto = new GoodsRegistCategoryRequestDto();

		goodsRegistCategoryRequestDto.setDepth("1");
		goodsRegistCategoryRequestDto.setMallDiv("MALL_DIV.PULMUONE");

		if("1".equals(goodsRegistCategoryRequestDto.getDepth())) {	//대분류 카테고리 일때만 해당하는 카테고리 내역을 불러온다.
			//전시 or 몰인몰 카테고리 최신(USE_YN = 'Y') 대분류 카테고리 불러오기(연도별 관리 대응)
			List<GoodsRegistCategoryVo> recentCategory1DepthIdList = goodsRegistCategoryMapper.recentCategory1DepthIdList(goodsRegistCategoryRequestDto);

			ArrayList<String> arrayList = new ArrayList<>();

			if(!recentCategory1DepthIdList.isEmpty()) {
				for(GoodsRegistCategoryVo getDisplayCategoryResultVo : recentCategory1DepthIdList) {
					arrayList.add(getDisplayCategoryResultVo.getIlCtgryId());
				}
			}
			goodsRegistCategoryRequestDto.setMasterCategoryIdArray(arrayList);
		}

		List<GoodsRegistCategoryVo> displayCategoryList = goodsRegistCategoryMapper.getDisplayCategoryList(goodsRegistCategoryRequestDto);

		assertTrue(CollectionUtils.isNotEmpty(displayCategoryList));
	}

	@Test
	void 묶음상품_ETC조합() {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		String[] ilGoodsIds = {"10000001","10000009","10000010","10000011"};

		goodsRegistRequestDto.setIlGoodsIds(ilGoodsIds);

		List<GoodsRegistVo> getGoodsList = goodsRegistService.getGoodsList(goodsRegistRequestDto);	//상품 내역

		LinkedHashSet<String> undeliverableAreaTypearray = new LinkedHashSet<>();	//배송 불가 지역 중복 제거를 위해서 LinkedHashSet 선언
		LinkedHashSet<Integer> returnPeriodArray = new LinkedHashSet<>();
		int returnPeriod = 0;

		if(!getGoodsList.isEmpty()) {
			for(GoodsRegistVo goodsRegistVo : getGoodsList) {
				undeliverableAreaTypearray.add(goodsRegistVo.getUndeliverableAreaType());	//배송 불가 지역 코드 중복 제거

				ApiResult<?> returnPeriodList = goodsItemRegisterBiz.getReturnPeriod(goodsRegistVo.getIlCategoryStadardId(), goodsRegistVo.getStorageMethodType());	//반품 가능 기간 정보
				ItemReturnPeriodResponseDto itemReturnPeriodResponseDto = (ItemReturnPeriodResponseDto)returnPeriodList.getData();

				if(itemReturnPeriodResponseDto.getItemReturnPeriodVo().getReturnPeriod() == null) {
					returnPeriod = 0;
				}
				else {
					returnPeriod = itemReturnPeriodResponseDto.getItemReturnPeriodVo().getReturnPeriod();
				}

				returnPeriodArray.add(returnPeriod);
			}

			int undeliverableAreaTypeAssembleScore = 0;			//묶음 상품 배송 불가 지역 조합 점수
			String undeliverableAreaTypeAssembleCode = null;		//묶음 상품 배송 불가 지역 조합 코드
			String undeliverableAreaTypeAssembleCodeName = null;	//묶음 상품 배송 불가 지역 조합명

			for(String undeliverableAreaTypeScore : undeliverableAreaTypearray) {
				if(GoodsEnums.UndeliverableAreaType.NONE.getCode().equals(undeliverableAreaTypeScore)) {		//'없음' 일때 0
					undeliverableAreaTypeAssembleScore += 0;
				}
				else if(GoodsEnums.UndeliverableAreaType.A1.getCode().equals(undeliverableAreaTypeScore)) {		//'도서산간(1권역)' 일때 1
					undeliverableAreaTypeAssembleScore += 1;
				}
				else if(GoodsEnums.UndeliverableAreaType.A2.getCode().equals(undeliverableAreaTypeScore)) {		//'제주(2권역)' 일때 2
					undeliverableAreaTypeAssembleScore += 2;
				}
				else if(GoodsEnums.UndeliverableAreaType.A1_A2.getCode().equals(undeliverableAreaTypeScore)) {	//'1권역/2권역' 일때 3
					undeliverableAreaTypeAssembleScore += 3;
				}
			}

			if(undeliverableAreaTypeAssembleScore >= 3) {		//누적 값이 3 이상이면 1권역/2권역
				undeliverableAreaTypeAssembleCode = GoodsEnums.UndeliverableAreaType.A1_A2.getCode();
				undeliverableAreaTypeAssembleCodeName = GoodsEnums.UndeliverableAreaType.A1_A2.getCodeName();
			}
			else if(undeliverableAreaTypeAssembleScore == 2) {	//누적 값이 2면 제주(2권역)
				undeliverableAreaTypeAssembleCode = GoodsEnums.UndeliverableAreaType.A2.getCode();
				undeliverableAreaTypeAssembleCodeName = GoodsEnums.UndeliverableAreaType.A2.getCodeName();
			}
			else if(undeliverableAreaTypeAssembleScore == 1) {	//누적 값이 1면 도서산간(1권역)
				undeliverableAreaTypeAssembleCode = GoodsEnums.UndeliverableAreaType.A1.getCode();
				undeliverableAreaTypeAssembleCodeName = GoodsEnums.UndeliverableAreaType.A1.getCodeName();
			}
			else if(undeliverableAreaTypeAssembleScore == 0) {	//누적 값이 0면 없음
				undeliverableAreaTypeAssembleCode = GoodsEnums.UndeliverableAreaType.NONE.getCode();
				undeliverableAreaTypeAssembleCodeName = GoodsEnums.UndeliverableAreaType.NONE.getCodeName();
			}


			int assembleReturnPerid = Collections.min(returnPeriodArray);		//묶음 상품 중에 가장 작은 반품 가능 기간을 가져온다.(0이면 불가)
			String assembleReturnPeridValue = null;

			if(assembleReturnPerid == 0) {
				assembleReturnPeridValue = "불가";
			}
			else {
				assembleReturnPeridValue = Integer.toString(assembleReturnPerid) + "일";
			}

			System.out.println("undeliverableAreaTypeAssembleCode : " + undeliverableAreaTypeAssembleCode);
			System.out.println("undeliverableAreaTypeAssembleCodeName : " + undeliverableAreaTypeAssembleCodeName);
			System.out.println("assembleReturnPeridValue : " + assembleReturnPeridValue);
		}

		assertNotNull(undeliverableAreaTypearray);
	}

	@Test
	void 임직원기본할인내역() throws Exception {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlItemCode("0926629");

		GoodsPriceInfoResultVo goodsBaseDiscountEmployeeList = goodsRegistService.goodsBaseDiscountEmployeeList(goodsRegistRequestDto.getIlItemCode());

		assertNotNull(goodsBaseDiscountEmployeeList);
	}

	@Test
	void 예약판매옵션설정_주문수집IF일_출고예정일_도착예정일구하기() {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		String urWarehouseId = "87";
		String[] patternStandardDateArray = {"2020-12-10", "2020-12-14", "2020-12-24", "2020-12-30", "2021-01-06", "2021-01-27"};

		if(!ObjectUtils.isEmpty(patternStandardDateArray)) {
			String orderIfDate = null;
			String releaseDate = null;
			String arriveDate = null;
			GoodsRegistReserveOptionVo goodsReservationDateCalc = new GoodsRegistReserveOptionVo();

			for(String patternStandardDate : patternStandardDateArray) {
				goodsRegistRequestDto.setUrWarehouseId(urWarehouseId);
				goodsRegistRequestDto.setPatternStandardDate(patternStandardDate);

				goodsReservationDateCalc = goodsRegistService.goodsReservationDateCalc(goodsRegistRequestDto);	//비교날짜와 같은 요일의 날짜를 구한다.

				if(goodsReservationDateCalc != null) {
					orderIfDate = goodsReservationDateCalc.getOrderIfDate();
					releaseDate = goodsReservationDateCalc.getReleaseDate();
					arriveDate = goodsReservationDateCalc.getArriveDate();
				}

				log.info("orderIfDate : " + orderIfDate);
				log.info("releaseDate : " + releaseDate);
				log.info("arriveDate : " + arriveDate);
			}

			assertTrue(goodsReservationDateCalc != null);
		}
	}

	@Test
	void 예약판매옵션설정_주문수집IF일_배송패턴_요일별_날짜제한() throws Exception {
		String urWarehouseId = "1";

		List<GoodsRegistReserveOptionVo> orderIfDateLimitList = goodsRegistService.orderIfDateLimitList(urWarehouseId);

		String[] ifOrderDatesToDisableArray = {"MO","TU","WE","TH","FR","SA","SU"};

		final List<String> ifOrderDatesToDisableList = new ArrayList<String>();
		Collections.addAll(ifOrderDatesToDisableList, ifOrderDatesToDisableArray);

		if(orderIfDateLimitList != null && !orderIfDateLimitList.isEmpty()) {
			for(GoodsRegistReserveOptionVo orderIfDateLimit : orderIfDateLimitList) {
				ifOrderDatesToDisableList.remove(orderIfDateLimit.getWeekCode());
			}
		}

		log.info("ifOrderDatesToDisableList : " + ifOrderDatesToDisableList);

		assertTrue(ifOrderDatesToDisableList.size() > 0);
	}

	@Test
	void 묶음상품_임직원할인_가격정보_내역() throws Exception {
		String ilGoodsId = "301551";

		List<GoodsPriceInfoResultVo> goodsPackageEmployeePriceList = goodsRegistService.goodsPackageEmployeePriceList(ilGoodsId);

		assertTrue(CollectionUtils.isNotEmpty(goodsPackageEmployeePriceList));
	}

	@Test
	void 묶음상품_임직원개별할인정보_내역() throws Exception {
		String ilGoodsId = "15340";

		List<GoodsPriceInfoResultVo> goodsPackageDiscountEmployeeList = goodsRegistService.goodsPackageDiscountEmployeeList(ilGoodsId);

		assertTrue(CollectionUtils.isNotEmpty(goodsPackageDiscountEmployeeList));
	}

	@Test
	void 증정행사_내역() throws Exception {
		String ilGoodsId = "38";

		List<ExhibitGiftResultVo> exhibitGiftList = goodsRegistService.exhibitGiftList(ilGoodsId);

		assertTrue(CollectionUtils.isNotEmpty(exhibitGiftList));
	}

	@Test
	void 재고운영형태_전일마감재고_정보() throws Exception {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlItemCode("0933787");
		goodsRegistRequestDto.setUrWarehouseId("85");

		GoodsRegistStockInfoVo goodsStockInfo = goodsRegistService.goodsStockInfo(goodsRegistRequestDto);

		assertNotNull(goodsStockInfo);
	}

	@Test
	void 묶음상품_구성정보() throws Exception {
		String ilGoodsId = "15677";

		List<GoodsPackageGoodsMappingVo> goodsPackageGoodsMappingList = goodsRegistService.goodsPackageGoodsMappingList(ilGoodsId, "N");

		assertTrue(CollectionUtils.isNotEmpty(goodsPackageGoodsMappingList));
	}

	@Test
	void 묶음상품_개별상품_대표이미지_정렬순서_Update() {
		String ilGoodsId = "15681";

		List<String> goodsImageOrderLIist = new ArrayList<>();

		goodsImageOrderLIist.add("15631");
		goodsImageOrderLIist.add("15619");

		int updateIntSum = 0;

		if(!goodsImageOrderLIist.isEmpty()) {
			int imageSortSeq = 0;
			for(String targetGoodsId : goodsImageOrderLIist) {
				//묶음상품 > 상품 이미지 > 개별상품 대표이미지 정렬 순서 UPDATE
				int updateInt = goodsRegistService.updateGoodsPackageGoodsMappingImageOrderUpdate(targetGoodsId, imageSortSeq, ilGoodsId);
				updateIntSum = updateIntSum + updateInt;
				imageSortSeq++;
			}
		}

		assertTrue(updateIntSum > 0);
	}

	@Test
	void 품목판매불가_상품단종처리() throws Exception {
		String ilItemCode = "0934400";
		String changeSaleStatus = GoodsEnums.SaleStatus.STOP_SALE.getCode();

		List<GoodsRegistVo> itemExtinctionGoodsStopSaleList = goodsRegistService.itemExtinctionGoodsStopSaleList(ilItemCode);	//품목 판매불가에 따른 상품 판매중지 대상 찾기
		List<String> ilGoodsIds = new ArrayList<String>();

		if(!itemExtinctionGoodsStopSaleList.isEmpty()) {
			for(GoodsRegistVo goodsRegistVo : itemExtinctionGoodsStopSaleList) {

				String saleStatus = goodsRegistVo.getSaleStatus();

				if(saleStatus.equals(GoodsEnums.SaleStatus.WAIT.getCode()) || saleStatus.equals(GoodsEnums.SaleStatus.ON_SALE.getCode())	//판매상태가 판매대기, 판매중, 품절(시스템), 품절(관리자)인 경우에만
						|| saleStatus.equals(GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode()) || saleStatus.equals(GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_MANAGER.getCode())) {
					ilGoodsIds.add(goodsRegistVo.getIlGoodsId());
				}
			}

			if (!ilGoodsIds.isEmpty()) {
				goodsRegistService.updateGoodsStopSale(ilGoodsIds, changeSaleStatus);
			}
		}

		assertTrue(ilGoodsIds.size() > 0);
	}

	@Test
	void 묶음상품_중복체크() throws Exception {

		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		String[] targetGoodsIds = {"15523","14","15546"};
		int[] ilGoodsQuantitys = {1, 4, 1};
		int ilGoodsIdsCount = targetGoodsIds.length;




		//수량 계산을 할 List<Dto> 정보를 만듦
		GoodsRegistPackageGoodsMappingDto goodsMappingDto = new GoodsRegistPackageGoodsMappingDto();
		List<GoodsRegistPackageGoodsMappingDto> selectPackageGoodsList = new ArrayList<GoodsRegistPackageGoodsMappingDto>();
		if(targetGoodsIds != null) {
			int i = 0;
			for(String targetGoodsId : targetGoodsIds) {
				goodsMappingDto.setTargetGoodsId(targetGoodsId);
				goodsMappingDto.setGoodsQuantity(ilGoodsQuantitys[i]);
				selectPackageGoodsList.add(goodsMappingDto);
				i++;
			}
		}
		goodsRegistRequestDto.setSelectPackageGoodsList(selectPackageGoodsList);

		GoodsRegistVo goodsPackageExistChk = new GoodsRegistVo();
		if(goodsRegistRequestDto.getSelectPackageGoodsList() != null && !goodsRegistRequestDto.getSelectPackageGoodsList().isEmpty()) {

			ilGoodsIdsCount = goodsRegistRequestDto.getIlGoodsIds().length;
			Map<String, Object> params = new HashMap<>();
			params.put("list", goodsRegistRequestDto.getSelectPackageGoodsList());
			params.put("ilGoodsIdsCount", ilGoodsIdsCount);
			goodsPackageExistChk = goodsRegistService.goodsPackageExistChk(params);    //ilGoodsIds의 상품으로 구성된 묶음상품이 있는지 체크
		}

		System.out.println("goodsPackageExistChk : " + goodsPackageExistChk);

		/*
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		String[] targetGoodsIds = {"15550","14","15546"};
		int[] ilGoodsQuantitys = {1, 4, 1};
		int ilGoodsIdsCount = targetGoodsIds.length;

		goodsRegistRequestDto.setIlGoodsIds(targetGoodsIds);

		//수량 계산을 할 List<Dto> 정보를 만듦
		List<GoodsRegistPackageGoodsMappingDto> selectPackageGoodsList = new ArrayList<GoodsRegistPackageGoodsMappingDto>();
		GoodsRegistPackageGoodsMappingDto goodsMappingDto = new GoodsRegistPackageGoodsMappingDto();

		if(targetGoodsIds != null) {
			int i = 0;
			for(String targetGoodsId : targetGoodsIds) {
				goodsMappingDto.setTargetGoodsId(targetGoodsId);
				goodsMappingDto.setGoodsQuantity(ilGoodsQuantitys[i]);
				selectPackageGoodsList.add(goodsMappingDto);
				i++;
			}
		}
		goodsRegistRequestDto.setSelectPackageGoodsList(selectPackageGoodsList);

		GoodsRegistVo goodsPackageExistChk = new GoodsRegistVo();
		int goodsPackageQuantityExistChkSum = 0;		//수량 비교 갯수

		if(goodsRegistRequestDto.getSelectPackageGoodsList() != null && !goodsRegistRequestDto.getSelectPackageGoodsList().isEmpty()) {

			ilGoodsIdsCount = goodsRegistRequestDto.getIlGoodsIds().length;

			goodsPackageExistChk = goodsRegistService.goodsPackageExistChk(goodsRegistRequestDto.getIlGoodsIds(), ilGoodsIdsCount);	//ilGoodsIds의 상품으로 구성된 묶음상품이 있는지 체크

			if(goodsPackageExistChk != null) {
				String ilGoodsId = goodsPackageExistChk.getIlGoodsId();												//같은 구성을 가진 묶음상품 ID

				for(GoodsRegistPackageGoodsMappingDto goodsRegistPackageGoodsMappingDto : goodsRegistRequestDto.getSelectPackageGoodsList()) {
					goodsRegistPackageGoodsMappingDto.setIlGoodsId(ilGoodsId);

					//각각의 구성품에 대한 수량 정보를 비교한다.(같은 상품 같은 수량 정보가 있으면 Count = 1을 반환 없으면 0을 반환)
					int goodsPackageQuantityExistChk = goodsRegistService.goodsPackageQuantityExistChk(goodsRegistPackageGoodsMappingDto);

					goodsPackageQuantityExistChkSum = goodsPackageQuantityExistChkSum + goodsPackageQuantityExistChk;
				}
			}
		}

		if(goodsPackageQuantityExistChkSum != ilGoodsIdsCount) {	//구성 상품 갯수와 수량 비교 갯수가 맞지 않으면 묶음상품 생성가능, 같으면 생성 불가
			goodsPackageExistChk = null;
		}
*/
		assertNotNull(goodsPackageExistChk.getIlGoodsId());
	}

	@Test
	void 예약판매옵션설정_저장수정삭제() throws Exception {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		//삭제 관련 Prameter 생성
		List<GoodsRegistReservationOptionDto> deleteGoodsReservationOptionList = Arrays.asList(
				GoodsRegistReservationOptionDto.builder().ilGoodsReserveOptionId("1280").build()
				, GoodsRegistReservationOptionDto.builder().ilGoodsReserveOptionId("1278").build()
		);
		goodsRegistRequestDto.setDeleteGoodsReservationOptionList(deleteGoodsReservationOptionList);

		//Insert, Update 관련 Parameter 생성
		List<GoodsRegistReservationOptionDto> goodsReservationOptionList = Arrays.asList(
				GoodsRegistReservationOptionDto.builder()
					.ilGoodsReserveOptionId("1279")
					.saleSequance("1")
					.reservationStartDate("2021-02-17 00:00")
					.reservationEndDate("2021-02-28 23:59")
					.stockQuantity("4")
					.orderIfDate("2021-03-02")
					.releaseDate("2021-03-02")
					.arriveDate("2021-03-03")
					.build(),
				GoodsRegistReservationOptionDto.builder()
					.ilGoodsReserveOptionId(null)
					.saleSequance("2")
					.reservationStartDate("2021-02-17 00:00")
					.reservationEndDate(" 2021-03-03 23:59")
					.stockQuantity("5")
					.orderIfDate("2021-03-09")
					.releaseDate("2021-03-09")
					.arriveDate("2021-03-10")
					.build(),
				GoodsRegistReservationOptionDto.builder()
					.ilGoodsReserveOptionId(null)
					.saleSequance("3")
					.reservationStartDate(" 2021-03-04 00:00")
					.reservationEndDate(" 2021-04-10 23:59")
					.stockQuantity("6")
					.orderIfDate("2021-04-13")
					.releaseDate("2021-04-13")
					.arriveDate("2021-04-14")
					.build()
		);
		goodsRegistRequestDto.setCreateId("1");
		goodsRegistRequestDto.setIlGoodsId("15758");
		goodsRegistRequestDto.setGoodsReservationOptionList(goodsReservationOptionList);


		//tranjaction 관련 결과 변수 값
		int delete = 0;		// 삭제 tran 횟수
		int insert = 0;		// 삭제 tran 횟수
		int update = 0;		// 삭제 tran 횟수
		int seqUpdate = 0;	// 회차 순서 변경 tran 횟수

		//판매 정보 > 판매 유형(예약판매) > 예약 판매 옵션 설정 Delete and Insert
		// 삭제처리한 항목 먼저 DB에서 삭제 처리
		if(goodsRegistRequestDto.getDeleteGoodsReservationOptionList() != null && !goodsRegistRequestDto.getDeleteGoodsReservationOptionList().isEmpty()) {
			delete = goodsRegistService.deleteGoodsReservationOption(goodsRegistRequestDto.getDeleteGoodsReservationOptionList(), Long.parseLong(goodsRegistRequestDto.getModifyId()));
		}

		// 삭제 처리한 항목 외에 Insert, Update 처리
		if(goodsRegistRequestDto.getGoodsReservationOptionList() != null) {
			for(GoodsRegistReservationOptionDto goodsRegistReservationOptionDto : goodsRegistRequestDto.getGoodsReservationOptionList()) {
				goodsRegistReservationOptionDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
				goodsRegistReservationOptionDto.setCreateId(goodsRegistRequestDto.getCreateId());
				if(goodsRegistReservationOptionDto.getIlGoodsReserveOptionId() == null) {
					insert = goodsRegistService.addGoodsReservationOption(goodsRegistReservationOptionDto);
				}
				else {
					update = goodsRegistService.updateGoodsReservationOption(goodsRegistReservationOptionDto);
				}
			}


			//회차 순서 변경(1순위 : 도착예정일, 2순위 출고예정일, 3순위 : 주문수집I/F일, 4순위 : 예약주문가능기간 종료일)
			seqUpdate = goodsRegistService.updateGoodsReservationOptionSaleSeq(goodsRegistRequestDto.getIlGoodsId());
		}

		assertTrue(delete > 0 && insert > 0 && update > 0 && seqUpdate > 0);
	}

	@Test
	void 상품마스터_변경내역_저장() throws Exception {

		String tranMode = "UPDATE";

		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		//신규 상품 내역
		goodsRegistRequestDto.setIlGoodsId("90018021");
		goodsRegistRequestDto.setGoodsName("ORGA 쇼핑백 수정");
		goodsRegistRequestDto.setPromotionName("프로모션명");
		goodsRegistRequestDto.setPromotionNameStartDate("2021-03-15");
		goodsRegistRequestDto.setPromotionNameEndDate("2021-04-05");

		//기존 상품 내역
		GoodsRegistVo ilGoodsDetail = goodsRegistService.goodsDetail(goodsRegistRequestDto);	//상품 내역

		Map<String, String> beforeGoodsDatas = BeanUtils.describe(goodsRegistRequestDto);
		Map<String, String> afterGoodsDatas = BeanUtils.describe(ilGoodsDetail);

		changeTranNum = 0;

		if(!afterGoodsDatas.isEmpty()) {
			afterGoodsDatas.forEach((afterKey, afterValue)-> {
				GoodsColumnComment goodsColumnComment = GoodsColumnComment.findByComment(afterKey);

				if("INSERT".equals(tranMode) && afterValue != null && goodsColumnComment != null) {
					GoodsChangeLogVo goodsChangeLogVo = GoodsChangeLogVo.builder()
						.ilGoodsId(goodsRegistRequestDto.getIlGoodsId())
						.tableNm("IL_GOODS")
						.tableIdOrig("0")
						.tableIdNew(goodsRegistRequestDto.getIlGoodsId())
						.beforeData("")		//저장시에는 beforeData는 없음
						.afterData(afterValue)
						.columnNm(afterKey)
						.columnLabel(goodsColumnComment.getCodeName())
						.createId("1")
						.build();

					//람다식 사용으로 인해서 Class 단위 전역변수로 선언 해야함
					changeTranNum += goodsRegistService.addGoodsChangeLog(goodsChangeLogVo);
				}
				else if("UPDATE".equals(tranMode) && goodsColumnComment != null) {

					beforeGoodsDatas.forEach((beforeKey, beforeValue)-> {

						if(afterValue != null && afterKey.equals(beforeKey) && !afterValue.equals(beforeValue)) {

							log.info("beforeKey : " + beforeKey + "  beforeValue : " + beforeValue + "// afterKey : " + afterKey + "  afterValue : " + afterValue);

							GoodsChangeLogVo goodsChangeLogVo = GoodsChangeLogVo.builder()
								.ilGoodsId(goodsRegistRequestDto.getIlGoodsId())
								.tableNm("IL_GOODS")
								.tableIdOrig(goodsRegistRequestDto.getIlGoodsId())
								.tableIdNew(goodsRegistRequestDto.getIlGoodsId())
								.beforeData(beforeValue)
								.afterData(afterValue)
								.columnNm(afterKey)
								.columnLabel(goodsColumnComment.getCodeName())
								.createId("1")
								.build();

							//람다식 사용으로 인해서 Class 단위 전역변수로 선언 해야함
							changeTranNum += goodsRegistService.addGoodsChangeLog(goodsChangeLogVo);
						}
					});
				}
			});
		}

		assertTrue(changeTranNum > 0);
	}

	@Test
	void 상품서브_항목_변경내역_저장() throws Exception {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();
		goodsRegistRequestDto.setIlGoodsId("90018021");

		String tranMode = "UPDATE";
		String tableKind = "IL_GOODS_CTGRY";

		//신규 카테고리 내역
		List<GoodsRegistCategoryRequestDto> afterGoodsCategoryList = Arrays.asList(
				GoodsRegistCategoryRequestDto.builder()
					.ilGoodsId(goodsRegistRequestDto.getIlGoodsId())
					.ilCtgryId("4941")
					.categoryFullName("(운영)만두∙핫도그∙떡볶이 > (운영)핫도그 > (운영)핫도그")
					.mallDiv("MALL_DIV.PULMUONE")
					.basicYn("N")
					.createId("1")
					.build(),
				GoodsRegistCategoryRequestDto.builder()
					.ilGoodsId(goodsRegistRequestDto.getIlGoodsId())
					.ilCtgryId("4986")
					.categoryFullName("(운영)정육∙달걀∙수산 > (운영)달걀 > (운영)기타난류")
					.mallDiv("MALL_DIV.PULMUONE")
					.basicYn("Y")
					.createId("1")
					.build(),
				GoodsRegistCategoryRequestDto.builder()
					.ilGoodsId(goodsRegistRequestDto.getIlGoodsId())
					.ilCtgryId("5123")
					.categoryFullName("(운영)쌀∙잡곡 > (운영)현미∙찹쌀∙흑미")
					.mallDiv("MALL_DIV.ORGA")
					.basicYn("Y")
					.createId("1")
					.build()
		);
		goodsRegistRequestDto.setDisplayCategoryList(afterGoodsCategoryList);

		//기존 카테고리 내역
		List<GoodsRegistCategoryVo> ilGoodsDisplayCategoryList = goodsRegistCategoryMapper.ilGoodsDisplayCategoryList(goodsRegistRequestDto);

		List<?> beforeGoodsDatas = ilGoodsDisplayCategoryList;
		List<?> afterGoodsDatas = afterGoodsCategoryList;

		//테이블별 ID , DATA , COLUMN COMMENT 컬럼을 지정
		goodsEtcColumnComment = GoodsEtcColumnComment.findByInfo(tableKind);
		sameValue = 0;

		if("INSERT".equals(tranMode) && afterGoodsDatas != null) {
			for (Object afterGoodsValue : afterGoodsDatas) {
				Map<String, String> afterDataMap = BeanUtils.describe(afterGoodsValue);

				GoodsChangeLogVo goodsChangeLogVo = new GoodsChangeLogVo();
				goodsChangeLogVo.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
				goodsChangeLogVo.setTableNm(tableKind);
				goodsChangeLogVo.setColumnLabel(goodsEtcColumnComment.getComment());
				goodsChangeLogVo.setCreateId("1");

				afterDataMap.forEach((afterKey, afterValue)-> {
					if(goodsEtcColumnComment != null) {
						if(goodsEtcColumnComment.getIdColumn().equals(afterKey)){
							goodsChangeLogVo.setTableIdOrig("0");
							goodsChangeLogVo.setTableIdNew(afterValue);
							goodsChangeLogVo.setColumnNm(afterKey);
						}

						if(goodsEtcColumnComment.getDataColumn().equals(afterKey)){
							goodsChangeLogVo.setBeforeData("");
							goodsChangeLogVo.setAfterData(afterValue);
						}
					}
				});

				changeTranNum += goodsRegistService.addGoodsChangeLog(goodsChangeLogVo);
			}
		}
		else if("UPDATE".equals(tranMode) && beforeGoodsDatas != null && afterGoodsDatas != null) {
			//기존과 동일한 값인지 비교
			for (Object beforeGoodsValue : beforeGoodsDatas) {
				Map<String, String> beforeDataMap = BeanUtils.describe(beforeGoodsValue);

				beforeDataMap.forEach((beforeKey, beforeValue)-> {
					if(goodsEtcColumnComment != null && goodsEtcColumnComment.getIdColumn().equals(beforeKey)) {
						for (Object afterGoodsValue : afterGoodsDatas) {
							try {
								Map<String, String> afterDataMap = BeanUtils.describe(afterGoodsValue);

								afterDataMap.forEach((afterKey, afterValue)-> {
									log.info("beforeKey : " + beforeKey + "  beforeValue : " + beforeValue + "// afterKey : " + afterKey + "  afterValue : " + afterValue);
									if(afterValue != null && beforeKey.equals(afterKey) && beforeValue.equals(afterValue)) {

										sameValue++;
									}
								});
							} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
								log.error("변경이력 sameValue Error Log", e);
							}
						}
					}
				});
			}

			log.info("sameValue : " + sameValue);
			log.info("beforeGoodsDatas.size() : " + beforeGoodsDatas.size());
			log.info("afterGoodsDatas.size() : " + afterGoodsDatas.size());

			if(sameValue != afterGoodsDatas.size() || sameValue != beforeGoodsDatas.size()) {

				for (Object beforeGoodsValue : beforeGoodsDatas) {
					Map<String, String> beforeDataMap = BeanUtils.describe(beforeGoodsValue);

					GoodsChangeLogVo goodsChangeLogVo = new GoodsChangeLogVo();
					goodsChangeLogVo.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
					goodsChangeLogVo.setTableNm(tableKind);
					goodsChangeLogVo.setColumnLabel(goodsEtcColumnComment.getComment());
					goodsChangeLogVo.setCreateId("1");

					beforeDataMap.forEach((beforeKey, beforeValue)-> {
						if(goodsEtcColumnComment != null) {
							if(goodsEtcColumnComment.getIdColumn().equals(beforeKey)){
								goodsChangeLogVo.setTableIdOrig(beforeValue);
								goodsChangeLogVo.setTableIdNew("0");
								goodsChangeLogVo.setColumnNm(beforeKey);
							}

							if(goodsEtcColumnComment.getDataColumn().equals(beforeKey)){
								goodsChangeLogVo.setBeforeData(beforeValue);
								goodsChangeLogVo.setAfterData("");
							}
						}
					});

					changeTranNum += goodsRegistService.addGoodsChangeLog(goodsChangeLogVo);
				}

				for (Object afterGoodsValue : afterGoodsDatas) {
					Map<String, String> afterDataMap = BeanUtils.describe(afterGoodsValue);

					GoodsChangeLogVo goodsChangeLogVo = new GoodsChangeLogVo();
					goodsChangeLogVo.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
					goodsChangeLogVo.setTableNm(tableKind);
					goodsChangeLogVo.setColumnLabel(goodsEtcColumnComment.getComment());
					goodsChangeLogVo.setCreateId("1");

					afterDataMap.forEach((afterKey, afterValue)-> {
						if(goodsEtcColumnComment != null) {
							if(goodsEtcColumnComment.getIdColumn().equals(afterKey)){
								goodsChangeLogVo.setTableIdOrig("0");
								goodsChangeLogVo.setTableIdNew(afterValue);
								goodsChangeLogVo.setColumnNm(afterKey);
							}

							if(goodsEtcColumnComment.getDataColumn().equals(afterKey)){
								goodsChangeLogVo.setBeforeData("");
								goodsChangeLogVo.setAfterData(afterValue);
							}
						}
					});

					changeTranNum += goodsRegistService.addGoodsChangeLog(goodsChangeLogVo);
				}
			}
		}

		assertTrue(changeTranNum > 0);
	}

	@Test
	void 상품_승인완료_반려_요청철회_상품마스터변경() throws Exception {
		MessageCommEnum enums = BaseEnums.Default.FAIL;

		String ilGoodsApprId = "92";
		String apprKindTp = "APPR_KIND_TP.GOODS_CLIENT";

		GoodsRegistApprVo goodsApprInfo = goodsRegistService.goodsApprInfo(null, null, ilGoodsApprId, apprKindTp);
		if(goodsApprInfo != null) {
			GoodsRegistApprVo goodsRegistApprVo = new GoodsRegistApprVo();

			if( apprKindTp.equals(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode())
					&& goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode()) ) {	//관리자 상품 등록 요청 승인이고 승인완료 상태라면
				goodsRegistApprVo.setSaleStatus(GoodsEnums.SaleStatus.WAIT.getCode());
				goodsRegistApprVo.setSavedSaleStatus(null);
				goodsRegistApprVo.setIlGoodsId(goodsApprInfo.getIlGoodsId());

				goodsRegistService.updateGoodsApprovedSaleStatus(goodsRegistApprVo);						//상품의 판매 상태를 '판매대기'으로 UPDATE
				enums = BaseEnums.Default.SUCCESS;
			}
			else if(apprKindTp.equals(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode())
					&& ( goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode())
							|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())
							|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode()) ) ) {	//거래처 상품 변경 요청 승인이고 승인완료or반려or요청철회 상태라면

				if(goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode())) {				//최종 승인일때만 상품 변경내역 저장을 진행
					if(goodsApprInfo.getGoodsName() != null) {
						goodsRegistApprVo.setGoodsName(goodsApprInfo.getGoodsName());
					}

					if(goodsApprInfo.getGoodsDesc() != null) {
						goodsRegistApprVo.setGoodsDesc(goodsApprInfo.getGoodsDesc());
					}

					if(goodsApprInfo.getSearchKeyword() != null) {
						goodsRegistApprVo.setSearchKeyword(goodsApprInfo.getSearchKeyword());
					}

					if(goodsApprInfo.getDisplayYn() != null) {
						goodsRegistApprVo.setDisplayYn(goodsApprInfo.getDisplayYn());
					}

					if(goodsApprInfo.getSaleStartDate() != null) {
						goodsRegistApprVo.setSaleStartDate(goodsApprInfo.getSaleStartDate());
					}

					if(goodsApprInfo.getSaleEndDate() != null) {
						goodsRegistApprVo.setSaleEndDate(goodsApprInfo.getSaleEndDate());
					}

					if(goodsApprInfo.getSaleStatus() != null) {
						goodsRegistApprVo.setSaleStatus(goodsApprInfo.getSaleStatus());
					}

					if(goodsApprInfo.getGoodsMemo() != null) {
						goodsRegistApprVo.setGoodsMemo(goodsApprInfo.getGoodsMemo());
					}

					//상품의 모든 내역을 UPDATE 하기 전에 데이터를 취합한다.(변경 내역 남기기 위한 작업)
					GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();
					goodsRegistRequestDto.setIlGoodsId(goodsApprInfo.getIlGoodsId());

					GoodsRegistResponseDto beforeGoodsDatas = goodsRegistBizImpl.goodsDatas(goodsRegistRequestDto);

					//상품 마스터 변경 내역
					Map<String, String> beforeGoodsData = BeanUtils.describe(beforeGoodsDatas.getIlGoodsDetail());
					Map<String, String> afterGoodsData = BeanUtils.describe(goodsRegistApprVo);

			  		String timestamp = DateUtil.getCurrentDate("yyyyMMddHHmmss");

					goodsRegistBizImpl.addGoodsMasterChangeLog("UPDATE", goodsApprInfo.getIlGoodsId(), goodsApprInfo.getApprReqUserId(), beforeGoodsData, afterGoodsData, timestamp);
				}

				if(goodsApprInfo.getSavedSaleStatus() != null
						&& goodsApprInfo.getSavedSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode())
						&& goodsApprInfo.getSaleStatus() == null) {						//승인 요청 시점의 판매상태가 '판매중'이고 거래처에서 판매상태 변경 요청사항이 없다면
					goodsRegistApprVo.setSaleStatus(goodsApprInfo.getSavedSaleStatus());
					goodsRegistApprVo.setSavedSaleStatus(null);
					goodsRegistApprVo.setIlGoodsId(goodsApprInfo.getIlGoodsId());
					goodsRegistApprVo.setModifyId(goodsApprInfo.getApprReqUserId());

					goodsRegistService.updateGoodsApprovedSaleStatus(goodsRegistApprVo);
				}
				else if(goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode())){	//현재 승인 완료라면

					goodsRegistApprVo.setSavedSaleStatus(null);
					goodsRegistApprVo.setIlGoodsId(goodsApprInfo.getIlGoodsId());
					goodsRegistApprVo.setModifyId(goodsApprInfo.getApprReqUserId());

					goodsRegistService.updateGoodsApprovedSaleStatus(goodsRegistApprVo);
				}
				enums = BaseEnums.Default.SUCCESS;
			}
		}
		else {
			enums = GoodsEnums.GoodsApprProcStatus.NONE_APPR;
		}

		assertTrue(enums.equals(BaseEnums.Default.SUCCESS));
	}

	@Test
	void 상품_승인() throws Exception {
		MessageCommEnum enums = BaseEnums.Default.SUCCESS;

		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

		goodsRegistRequestDto.setIlGoodsId("90018058");
		goodsRegistRequestDto.setSaleStatus("SALE_STATUS.ON_SALE");

		//신규 승인 요청 내역
		List<GoodsRegistApprRequestDto> goodsApprList = Arrays.asList(
				GoodsRegistApprRequestDto.builder()
				.apprKindTp("APPR_KIND_TP.GOODS_CLIENT")
				.apprLoginId("forbiz_a_1")
				.apprManagerTp("APPR_MANAGER_TP.SECOND")
				.apprUserId("4")
				.build(),
				GoodsRegistApprRequestDto.builder()
				.apprKindTp("APPR_KIND_TP.GOODS_CLIENT")
				.apprLoginId("forbiz_a_2")
				.apprManagerTp("APPR_MANAGER_TP.SECOND")
				.apprUserId("5")
				.build()
		);
		goodsRegistRequestDto.setGoodsApprList(goodsApprList);

		String userId = "12";							// USER ID
		String companyType = "COMPANY_TYPE.CLIENT";		// 회사타입
		String clientType = "CLIENT_TYPE.CLIENT";		// 거래처 타입

		if(!goodsRegistRequestDto.getGoodsApprList().isEmpty()) {

			GoodsRegistApprVo goodsRegistApprVo = new GoodsRegistApprVo();

			for(GoodsRegistApprRequestDto goodsRegistApprRequestDto : goodsRegistRequestDto.getGoodsApprList()) {
				if(goodsRegistApprRequestDto.getApprManagerTp().equals(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_1ST.getCode())) {
					goodsRegistApprVo.setApprSubUserId(goodsRegistApprRequestDto.getApprUserId());
				}

				if(goodsRegistApprRequestDto.getApprManagerTp().equals(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_2ND.getCode())) {
					goodsRegistApprVo.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
					goodsRegistApprVo.setApprKindTp(goodsRegistApprRequestDto.getApprKindTp());
					goodsRegistApprVo.setApprStat(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
					goodsRegistApprVo.setApprReqUserId(userId);
					goodsRegistApprVo.setApprUserId(goodsRegistApprRequestDto.getApprUserId());
				}
			}

			if(companyType.equals(CompanyEnums.CompanyType.HEADQUARTERS.getCode())) {	//관리자 권한이라면
				goodsRegistRequestDto.setApprKindTp(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode());
				GoodsRegistApprVo goodsApprInfo = goodsRegistService.goodsApprInfo(userId, goodsRegistRequestDto.getIlGoodsId(), null, goodsRegistRequestDto.getApprKindTp());

				if(goodsApprInfo == null || goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())
						|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode())
						|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DISPOSAL.getCode())) {		//승인 내역이 없거나, 반려/요청철회/폐기 상태일때
					goodsRegistService.addGoodsAppr(goodsRegistApprVo);

					goodsRegistApprVo.setPrevApprStat(ApprovalEnums.ApprovalStatus.NONE.getCode());
					goodsRegistApprVo.setStatusCmnt(null);

					goodsRegistService.addGoodsApprStatusHistory(goodsRegistApprVo);

					if(goodsRegistRequestDto.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode())) {		//승인 요청 시에 상품의 판매 상태가 '판매중'이라면
						goodsRegistApprVo.setSaleStatus(GoodsEnums.SaleStatus.WAIT.getCode());						//판매 상태를 '판매 대기'로 변경
						goodsRegistApprVo.setSavedSaleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode());				//현재 판매 상태를 저장
						goodsRegistService.updateGoodsApprovedSaleStatus(goodsRegistApprVo);
					}
				}
				else {
					enums = GoodsEnums.GoodsApprProcStatus.APPR_DUPLICATE;
				}
			}
			else if(companyType.equals(CompanyEnums.CompanyType.CLIENT.getCode()) && clientType.equals(CompanyEnums.ClientType.CLIENT.getCode())) {	//거래처 권한이라면
				goodsRegistRequestDto.setApprKindTp(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode());
				GoodsRegistApprVo goodsApprInfo = goodsRegistService.goodsApprInfo(userId, goodsRegistRequestDto.getIlGoodsId(), null, goodsRegistRequestDto.getApprKindTp());

				GoodsRegistVo goodsDetail = goodsRegistService.goodsDetail(goodsRegistRequestDto);	//상품 내역
				int differentCount = 0;																//거래처 상품 변경 카운트

				if(goodsDetail != null) {

					int dateCompare = DateUtil.string2Date(goodsDetail.getModifyDate(), "yyyy-MM-dd HH:mm:ss").compareTo(DateUtil.string2Date(goodsRegistRequestDto.getLoadDateTime(), "yyyy-MM-dd HH:mm:ss"));

					if(dateCompare > 0
							&& !goodsDetail.getSaleStatus().equals(goodsRegistRequestDto.getSaleStatus())) {	//DB의 상품 마스터 수정 시간이 거래처에서 페이지 Load한 시간 보다 크고, 거래처에서 입력한 판매 상태 값과 DB의 판매 상태 값이 다르다면
						enums = GoodsEnums.GoodsApprProcStatus.ADMIN_DIFFERENT_GOODS;
						goodsRegistRequestDto.setLoadDateTime(null);
					}
					else {
						if(!goodsDetail.getGoodsName().equals(goodsRegistRequestDto.getGoodsName())) {
							differentCount++;
							goodsRegistApprVo.setGoodsName(goodsRegistRequestDto.getGoodsName());
						}

						if(!GoodsEnums.GoodsType.ADDITIONAL.getCode().equals(goodsRegistRequestDto.getGoodsType())
								&& !GoodsEnums.GoodsType.GIFT.getCode().equals(goodsRegistRequestDto.getGoodsType())
								&& !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(goodsRegistRequestDto.getGoodsType())) {		//추가/증정 상품은 아래 항목들이 존재하지 않으므로 제외
							if(!StringUtil.nvl(goodsDetail.getGoodsDesc()).equals(StringUtil.nvl(goodsRegistRequestDto.getGoodsDesc()))) {
								differentCount++;
								goodsRegistApprVo.setGoodsDesc(goodsRegistRequestDto.getGoodsDesc());
							}

							if(!StringUtil.nvl(goodsDetail.getSearchKeyword()).equals(StringUtil.nvl(goodsRegistRequestDto.getSearchKeyword()))) {
								differentCount++;
								goodsRegistApprVo.setSearchKeyword(goodsRegistRequestDto.getSearchKeyword());
							}

							if(!goodsDetail.getDisplayYn().equals(goodsRegistRequestDto.getDisplayYn())) {
								differentCount++;
								goodsRegistApprVo.setDisplayYn(goodsRegistRequestDto.getDisplayYn());
							}

							if(!goodsDetail.getSaleStartDate().equals(goodsRegistRequestDto.getSaleStartDate()+":00")) {
								differentCount++;
								goodsRegistApprVo.setSaleStartDate(goodsRegistRequestDto.getSaleStartDate());
							}

							if(!goodsDetail.getSaleEndDate().equals(goodsRegistRequestDto.getSaleEndDate()+":59")) {
								differentCount++;
								goodsRegistApprVo.setSaleEndDate(goodsRegistRequestDto.getSaleEndDate());
							}
						}

						if(!goodsDetail.getSaleStatus().equals(goodsRegistRequestDto.getSaleStatus())) {
							differentCount++;
							goodsRegistApprVo.setSaleStatus(goodsRegistRequestDto.getSaleStatus());
						}

						if(!goodsDetail.getGoodsMemo().equals(goodsRegistRequestDto.getGoodsMemo())) {
							differentCount++;
							goodsRegistApprVo.setGoodsMemo(goodsRegistRequestDto.getGoodsMemo());
						}

						if(differentCount == 0) {
							enums = GoodsEnums.GoodsApprProcStatus.NOT_DIFFERENT_GOODS;
						}
						else if(differentCount > 0){
							if(goodsApprInfo == null || goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())
									|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode())
									|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DISPOSAL.getCode())
									|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode())) {	//승인 내역이 없거나, 반려/요청철회/폐기/승인완료 상태일때
								goodsRegistService.addGoodsAppr(goodsRegistApprVo);

								goodsRegistApprVo.setPrevApprStat(ApprovalEnums.ApprovalStatus.NONE.getCode());
								goodsRegistApprVo.setStatusCmnt(null);

								goodsRegistService.addGoodsApprStatusHistory(goodsRegistApprVo);

								if(goodsDetail.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode())) {		//승인 요청 시에 상품의 판매 상태가 '판매중'이라면

									GoodsRegistApprVo goodsApprSaleStatusVo = new GoodsRegistApprVo();

									goodsApprSaleStatusVo.setIlGoodsId(goodsRegistApprVo.getIlGoodsId());
									goodsApprSaleStatusVo.setSaleStatus(GoodsEnums.SaleStatus.WAIT.getCode());						//판매 상태를 '판매 대기'로 변경
									goodsApprSaleStatusVo.setSavedSaleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode());				//현재 판매 상태를 저장
									goodsRegistService.updateGoodsRequestSaleStatus(goodsApprSaleStatusVo);
								}
								enums = GoodsEnums.GoodsApprProcStatus.CLINET_APPR_REQUEST;
							}
							else {
								enums = GoodsEnums.GoodsApprProcStatus.APPR_DUPLICATE;
							}
						}
					}
				}
				else {
					enums = GoodsEnums.GoodsApprProcStatus.NONE_GOODS_ID;
				}
			}
		}
		else {
			if(companyType.equals(CompanyEnums.CompanyType.HEADQUARTERS.getCode())) {	//관리자 권한이라면
				goodsRegistRequestDto.setApprKindTp(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode());
				GoodsRegistApprVo goodsApprInfo = goodsRegistService.goodsApprInfo(userId, goodsRegistRequestDto.getIlGoodsId(), null, goodsRegistRequestDto.getApprKindTp());

				if(goodsApprInfo != null && goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())) {		//거래처 상품 수정 승인 요청 내역이 있다면
					enums = GoodsEnums.GoodsApprProcStatus.CLIENT_APPR_DUPLICATE;
				}
			}
			else if(companyType.equals(CompanyEnums.CompanyType.CLIENT.getCode()) && clientType.equals(CompanyEnums.ClientType.CLIENT.getCode())) {	//거래처 권한이라면
				enums = GoodsEnums.GoodsApprProcStatus.NONE_APPR;
			}
		}

		log.info("enums : " + enums);

		assertTrue(enums.equals(BaseEnums.Default.SUCCESS) || enums.equals(GoodsEnums.GoodsApprProcStatus.CLINET_APPR_REQUEST));
	}

	@Test
	void 품목승인_상품상태값변경() throws Exception {
		String ilItemCode = "0040933";
		String nowSaleStatus = GoodsEnums.SaleStatus.ON_SALE.getCode();
		String chgSaleStatus = GoodsEnums.SaleStatus.WAIT.getCode();

		int goodseSaleStatusChangeNum = goodsRegistService.updateGoodsSaleStatusToBackByItemAppr(ilItemCode);

		int goodsPackageSaleStatusChangeNum = goodsRegistService.updateGoodsSaleStatusToBackByItemAppr(ilItemCode);

		assertTrue(goodseSaleStatusChangeNum > 0 || goodsPackageSaleStatusChangeNum > 0);
	}

	@Test
	void 품목가격변동_상품가격업데이트_프로시저_성공() throws Exception {
		String ilItemCode = "0070201";

		try {
			goodsRegistService.spGoodsPriceUpdateWhenItemPriceChanges(ilItemCode);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
	}

	@Test
	void 품목가격변동_묶음상품가격업데이트_프로시저_성공() throws Exception {
		try {
			goodsRegistService.spPackageGoodsPriceUpdateWhenItemPriceChanges();
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
	}

	@Test
	void 상품할인승인내역저장(String ilGoodsId, String goodsType, List<GoodsDiscountRequestDto> goodsDiscountRequestDtoList
			, List<GoodsRegistApprRequestDto> goodsRegistApprRequestDtoList, List<GoodsRegistPackageGoodsPriceDto> goodsRegistPackageGoodsPriceDto) throws Exception {
		UserVo userVo = SessionUtil.getBosUserVO();
		String userId = userVo.getUserId();					// USER ID
		String today = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");

		if(goodsDiscountRequestDtoList != null && goodsRegistApprRequestDtoList != null
				   && !goodsDiscountRequestDtoList.isEmpty() && !goodsRegistApprRequestDtoList.isEmpty()){
			GoodsDiscountApprVo goodsDiscountApprVo = new GoodsDiscountApprVo();
			int goodsDiscountSeq = 0;

			for(GoodsDiscountRequestDto goodsDiscountRequestDto : goodsDiscountRequestDtoList) {
				if("APPR_STAT.NONE".equals(goodsDiscountRequestDto.getApprovalStatusCode())
						   && goodsDiscountRequestDto.getGoodsDiscountId() == null){		//최초 가격 승인 등록인것만 승인 요청 진행
					goodsDiscountApprVo.setIlGoodsId(ilGoodsId);
					goodsDiscountApprVo.setDiscountTp(goodsDiscountRequestDto.getDiscountTypeCode());
					goodsDiscountApprVo.setApprStat(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
					goodsDiscountApprVo.setDiscountStartDt(goodsDiscountRequestDto.getDiscountStartDateTime());
					goodsDiscountApprVo.setDiscountEndDt(goodsDiscountRequestDto.getDiscountEndDateTime());
					goodsDiscountApprVo.setDiscountMethodTp(goodsDiscountRequestDto.getDiscountMethodTypeCode());

					goodsDiscountApprVo.setStandardPrice(goodsDiscountRequestDto.getItemStandardPrice());
					goodsDiscountApprVo.setRecommendedPrice(goodsDiscountRequestDto.getItemRecommendedPrice());

					if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsType)
							   && GoodsDiscountType.EMPLOYEE.getCode().equals(goodsDiscountRequestDto.getDiscountTypeCode())){
						//묶음상품이고 임직원 개별할인이라면 개별품목별로 할인율을 저장하므로 정률, 고정가에 관계없이 마스터 할인승인 내역에 할인금액, 할인율 다 0으로 입력 처리
						goodsDiscountApprVo.setDiscountSalePrice(0);
						goodsDiscountApprVo.setDiscountRatio(0);
					}
					else {
						if (GoodsEnums.GoodsDiscountMethodType.FIXED_RATE.getCode().equals(goodsDiscountRequestDto.getDiscountMethodTypeCode())) {
							//할인 유형이 '정률할인' 이라면 판매가를 저장하지 않는다.
							goodsDiscountApprVo.setDiscountSalePrice(0);
							goodsDiscountApprVo.setDiscountRatio(goodsDiscountRequestDto.getDiscountRatio());
						} else if (GoodsEnums.GoodsDiscountMethodType.FIXED_PRICE.getCode().equals(goodsDiscountRequestDto.getDiscountMethodTypeCode())) {
							//할인 유형이 '고정가할인' 이라면 할인율을 저장하지 않는다.
							goodsDiscountApprVo.setDiscountSalePrice(goodsDiscountRequestDto.getDiscountSalePrice());
							goodsDiscountApprVo.setDiscountRatio(0);
						}
					}

					for(GoodsRegistApprRequestDto goodsRegistApprRequestDto : goodsRegistApprRequestDtoList) {	//승인 관리자
						if(goodsRegistApprRequestDto.getApprManagerTp().equals(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_1ST.getCode())) {
							goodsDiscountApprVo.setApprSubUserId(goodsRegistApprRequestDto.getApprUserId());
						}

						if(goodsRegistApprRequestDto.getApprManagerTp().equals(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_2ND.getCode())) {
							goodsDiscountApprVo.setApprReqUserId(userId);
							goodsDiscountApprVo.setApprUserId(goodsRegistApprRequestDto.getApprUserId());
						}
					}

					goodsDiscountApprVo.setApprReqDt(today);
					goodsDiscountApprVo.setCreateId(userId);
					goodsDiscountApprVo.setCreateDt(today);

					/*
					묶음상품 임직원 개별할인의 경우 첫번째 행의 할인 시작일/종료일을 가지고 할인승인 마스터 정보를 생성하고, 다른 할인은 첫번째 행만 할인 내역으로 넘어오기 때문에
					goodsDiscountSeq = 0 일때만 할인 승인 마스터, 할인 승인 마스터 히스토리 저장 처리
					*/
					if(goodsDiscountSeq == 0) {
						goodsRegistService.addGoodsDiscountAppr(goodsDiscountApprVo);

						goodsDiscountApprVo.setPrevApprStat(ApprovalEnums.ApprovalStatus.NONE.getCode());
						goodsDiscountApprVo.setStatusCmnt(null);
						goodsRegistService.addGoodsDiscountApprStatusHistory(goodsDiscountApprVo);
					}

					if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsType)
							   && GoodsDiscountType.EMPLOYEE.getCode().equals(goodsDiscountRequestDto.getDiscountTypeCode())){
						//묶음상품이고 임직원 개별할인이라면 개별품목 고정가 할인가격 리스트가 따로 존재하지 않고 할인내역 자체가 리스트 이므로
						if(goodsDiscountSeq != goodsDiscountRequestDto.getRowCount()) {		//합계 행은 계산하지 않는다.
							//개별품목 고정가 할인가격 리스트 승인 저장 처리
							GoodsPackageItemFixedDiscountPriceApprVo goodsPackageItemFixedDiscountPriceApprVo = new GoodsPackageItemFixedDiscountPriceApprVo();

							goodsPackageItemFixedDiscountPriceApprVo.setIlGoodsDiscountApprId(goodsDiscountApprVo.getIlGoodsDiscountApprId());
							goodsPackageItemFixedDiscountPriceApprVo.setIlGoodsPackageGoodsMappingId(goodsDiscountRequestDto.getIlGoodsPackageGoodsMappingId());
							goodsPackageItemFixedDiscountPriceApprVo.setSalePrice(0);
							goodsPackageItemFixedDiscountPriceApprVo.setUnitSalePrice(0);
							goodsPackageItemFixedDiscountPriceApprVo.setDiscountRatio(goodsDiscountRequestDto.getDiscountRatio());

							goodsRegistService.addGoodsPackageItemFixedDiscountPriceAppr(goodsPackageItemFixedDiscountPriceApprVo);
						}
					}
					else if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsType) && goodsRegistPackageGoodsPriceDto != null
									&& !goodsRegistPackageGoodsPriceDto.isEmpty()) {		//묶음상품이고 개별품목 고정가 할인가격 리스트가 있다면
						//개별품목 고정가 할인가격 리스트 승인 저장 처리
						for(GoodsRegistPackageGoodsPriceDto goodsPackageItemFixedDiscountPrice : goodsRegistPackageGoodsPriceDto) {
							GoodsPackageItemFixedDiscountPriceApprVo goodsPackageItemFixedDiscountPriceApprVo = new GoodsPackageItemFixedDiscountPriceApprVo();

							goodsPackageItemFixedDiscountPriceApprVo.setIlGoodsDiscountApprId(goodsDiscountApprVo.getIlGoodsDiscountApprId());
							goodsPackageItemFixedDiscountPriceApprVo.setIlGoodsPackageGoodsMappingId(goodsPackageItemFixedDiscountPrice.getIlGoodsPackageGoodsMappingId());
							if(GoodsEnums.GoodsDiscountMethodType.FIXED_PRICE.getCode().equals(goodsDiscountRequestDto.getDiscountMethodTypeCode())) {
								goodsPackageItemFixedDiscountPriceApprVo.setSalePrice(goodsPackageItemFixedDiscountPrice.getSalePricePerUnit() * goodsPackageItemFixedDiscountPrice.getGoodsQuantity());
								goodsPackageItemFixedDiscountPriceApprVo.setUnitSalePrice(goodsPackageItemFixedDiscountPrice.getSalePricePerUnit());
								goodsPackageItemFixedDiscountPriceApprVo.setDiscountRatio(0);
							}
							else{
								goodsPackageItemFixedDiscountPriceApprVo.setSalePrice(0);
								goodsPackageItemFixedDiscountPriceApprVo.setUnitSalePrice(0);
								goodsPackageItemFixedDiscountPriceApprVo.setDiscountRatio(goodsDiscountRequestDto.getDiscountRatio());
							}

							goodsRegistService.addGoodsPackageItemFixedDiscountPriceAppr(goodsPackageItemFixedDiscountPriceApprVo);
						}
					}
					goodsDiscountSeq++;
				}
			}
		}
	}

	@Test
	void 상품할인승인() throws Exception {
		GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();
		MessageCommEnum enums = BaseEnums.Default.SUCCESS;
		String ilGoodsId = "90018129";
		String goodsType = GoodsEnums.GoodsType.PACKAGE.getCode();

		String companyType = "COMPANY_TYPE.HEADQUARTERS";		// 회사타입

		//신규 즉시할인 내역
		List<GoodsDiscountRequestDto> goodsDiscountImmediateList = Arrays.asList(
				GoodsDiscountRequestDto.builder()
						.approvalStatusCode("APPR_STAT.NONE")
						.approvalStatusCodeName("승인요청")
						.discountStartDateTime("2021-06-10 00:59")
						.discountEndDateTime("2021-06-17 23:59")
						.discountSalePrice(42240)
						.discountRatio(17)
						.discountTypeCode(GoodsDiscountType.IMMEDIATE.getCode())
						.discountMethodTypeCode(GoodsEnums.GoodsDiscountMethodType.FIXED_PRICE.getCode())
						.goodsId(ilGoodsId)
						.itemRecommendedPrice(50940)
						.itemStandardPrice(18005)
						.build()
		);
		goodsRegistRequestDto.setGoodsDiscountImmediateList(goodsDiscountImmediateList);

		//신규 즉시할인 승인 관리자 내역
		List<GoodsRegistApprRequestDto> goodsDiscountImmediateApproList = Arrays.asList(
				GoodsRegistApprRequestDto.builder()
						.apprKindTp("APPR_KIND_TP.GOODS_DISCOUNT")
						.apprManagerTp("APPR_MANAGER_TP.FIRST")
						.apprLoginId("forbiz_a_1")
						.apprUserId("4")
						.build(),
				GoodsRegistApprRequestDto.builder()
						.apprKindTp("APPR_KIND_TP.GOODS_DISCOUNT")
						.apprManagerTp("APPR_MANAGER_TP.SECOND")
						.apprLoginId("forbiz_a_2")
						.apprUserId("5")
						.build()
		);
		goodsRegistRequestDto.setGoodsDiscountImmediateApproList(goodsDiscountImmediateApproList);

		//신규 즉시 할인 개별품목 고정가 할인가격 리스트
		List<GoodsRegistPackageGoodsPriceDto> goodsDiscountImmediateCalcList = Arrays.asList(
				GoodsRegistPackageGoodsPriceDto.builder()
						.discountRatio(0)
						.goodsQuantity(2)
						.ilGoodsId(ilGoodsId)
						.ilGoodsPackageGoodsMappingId("537")
						.recommendedPrice(8480)
						.salePrice(14060)
						.salePricePerUnit(7030)
						.standardPrice(3185)
						.build(),
				GoodsRegistPackageGoodsPriceDto.builder()
						.discountRatio(0)
						.goodsQuantity(1)
						.ilGoodsId(ilGoodsId)
						.ilGoodsPackageGoodsMappingId("538")
						.recommendedPrice(8480)
						.salePrice(7030)
						.salePricePerUnit(7030)
						.standardPrice(3040)
						.build(),
				GoodsRegistPackageGoodsPriceDto.builder()
						.discountRatio(0)
						.goodsQuantity(3)
						.ilGoodsId(ilGoodsId)
						.ilGoodsPackageGoodsMappingId("536")
						.recommendedPrice(8500)
						.salePrice(21150)
						.salePricePerUnit(7050)
						.standardPrice(2865)
						.build()
		);
		goodsRegistRequestDto.setGoodsDiscountImmediateCalcList(goodsDiscountImmediateCalcList);

		if(companyType.equals(CompanyEnums.CompanyType.HEADQUARTERS.getCode())) {	//관리자 권한이라면
			//우선 할인
			if (goodsRegistRequestDto.getGoodsDiscountPriorityList() != null && goodsRegistRequestDto.getGoodsDiscountPriorityApproList() != null) {
				GoodsDiscountApprVo goodsDiscountApprInfo = goodsRegistService.goodsDiscountApprInfo(ilGoodsId, null, GoodsDiscountType.PRIORITY.getCode());

				if(goodsDiscountApprInfo != null &&
						   (goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())
									|| goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode())
						   )){		//현재 상품할인 승인 내역이 승인요청/승인완료(부) 상태라면
					enums = GoodsEnums.GoodsDiscountApprProcStatus.PRIORITY_APPR_DUPLICATE;
				}
				else{
					상품할인승인내역저장(ilGoodsId, goodsType, goodsRegistRequestDto.getGoodsDiscountPriorityList()
							, goodsRegistRequestDto.getGoodsDiscountPriorityApproList(), goodsRegistRequestDto.getGoodsDiscountPriorityCalcList());
				}
			}

			//즉시 할인
			if (goodsRegistRequestDto.getGoodsDiscountImmediateList() != null && goodsRegistRequestDto.getGoodsDiscountImmediateApproList() != null) {
				GoodsDiscountApprVo goodsDiscountApprInfo = goodsRegistService.goodsDiscountApprInfo(ilGoodsId, null, GoodsDiscountType.IMMEDIATE.getCode());

				if(goodsDiscountApprInfo != null &&
						   (goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())
									|| goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode())
						   )){		//현재 상품할인 승인 내역이 승인요청/승인완료(부) 상태라면
					enums = GoodsEnums.GoodsDiscountApprProcStatus.IMMEDIATE_APPR_DUPLICATE;
				}
				else{
					상품할인승인내역저장(ilGoodsId, goodsType, goodsRegistRequestDto.getGoodsDiscountImmediateList()
							, goodsRegistRequestDto.getGoodsDiscountImmediateApproList(), goodsRegistRequestDto.getGoodsDiscountImmediateCalcList());
				}
			}

			if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsType)){		//묶음 상품이라면
				//묶음 상품 기본 판매가
				if (goodsRegistRequestDto.getGoodsPackagePriceList() != null && goodsRegistRequestDto.getGoodsPackagePriceApproList() != null) {
					GoodsDiscountApprVo goodsDiscountApprInfo = goodsRegistService.goodsDiscountApprInfo(ilGoodsId, null, GoodsDiscountType.PACKAGE.getCode());

					if(goodsDiscountApprInfo != null &&
							   (goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())
										|| goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode())
							   )){		//현재 상품할인 승인 내역이 승인요청/승인완료(부) 상태라면
						enums = GoodsEnums.GoodsDiscountApprProcStatus.PACKAGE_APPR_DUPLICATE;
					}
					else{
						상품할인승인내역저장(ilGoodsId, goodsType, goodsRegistRequestDto.getGoodsPackagePriceList()
								, goodsRegistRequestDto.getGoodsPackagePriceApproList(), goodsRegistRequestDto.getGoodsPackageCalcList());
					}
				}

				//임직원 개별 할인
				if (goodsRegistRequestDto.getGoodsPackageDiscountEmployeeList() != null && goodsRegistRequestDto.getGoodsPackageDiscountEmployeeApproList() != null) {
					GoodsDiscountApprVo goodsDiscountApprInfo = goodsRegistService.goodsDiscountApprInfo(ilGoodsId, null, GoodsDiscountType.EMPLOYEE.getCode());

					if (goodsDiscountApprInfo != null &&
								(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())
										 || goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode())
								)) {        //현재 상품할인 승인 내역이 승인요청/승인완료(부) 상태라면
						enums = GoodsEnums.GoodsDiscountApprProcStatus.EMPLOYEE_APPR_DUPLICATE;
					} else {
						상품할인승인내역저장(ilGoodsId, goodsType, goodsRegistRequestDto.getGoodsPackageDiscountEmployeeList()
								, goodsRegistRequestDto.getGoodsPackageDiscountEmployeeApproList(), null);
					}
				}
			}
			else {
				//임직원 개별할인
				if (goodsRegistRequestDto.getGoodsDiscountEmployeeList() != null && goodsRegistRequestDto.getGoodsDiscountEmployeeApproList() != null) {
					GoodsDiscountApprVo goodsDiscountApprInfo = goodsRegistService.goodsDiscountApprInfo(ilGoodsId, null, GoodsDiscountType.EMPLOYEE.getCode());

					if (goodsDiscountApprInfo != null &&
								(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())
										 || goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode())
								)) {        //현재 상품할인 승인 내역이 승인요청/승인완료(부) 상태라면
						enums = GoodsEnums.GoodsDiscountApprProcStatus.EMPLOYEE_APPR_DUPLICATE;
					} else {
						상품할인승인내역저장(ilGoodsId, goodsType, goodsRegistRequestDto.getGoodsDiscountEmployeeList()
								, goodsRegistRequestDto.getGoodsDiscountEmployeeApproList(), null);
					}
				}
			}
		}
		else {
			enums = GoodsEnums.GoodsDiscountApprProcStatus.NOT_HEADQUART;
		}

		log.info("enums : " + enums);

		assertTrue(enums.equals(BaseEnums.Default.SUCCESS));
	}

	@Test
	void 상품할인승인완료_반려_요청철회_이후작업() throws Exception {
		String ilGoodsDiscountApprId = "1527";
		String discountType = GoodsDiscountType.PACKAGE.getCode();

		MessageCommEnum enums = BaseEnums.Default.FAIL;

		GoodsDiscountApprVo goodsDiscountApprInfo = goodsRegistService.goodsDiscountApprInfo(null, ilGoodsDiscountApprId, discountType);

		if(goodsDiscountApprInfo != null){
			if(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode())) {		//현재 상태가 승인완료 상태라면
				//상품 할인 테이블 저장
				GoodsDiscountApprVo goodsDiscountApprVo = new GoodsDiscountApprVo();
				goodsDiscountApprVo.setIlGoodsDiscountApprId(ilGoodsDiscountApprId);

				int discountTranNum = goodsRegistService.addGoodsDiscountByAppr(goodsDiscountApprVo);
				int packageItemDiscountTranNum = goodsRegistService.addGoodsPackageItemFixedDiscountPriceByAppr(goodsDiscountApprVo);

				//상품 할인 승인 테이블에 상품할인ID UPDATE
				goodsRegistService.updateGoodsDiscountAppr(goodsDiscountApprVo);

				//묶음상품 기본 판매가 등록이고 할인 시작일이 존재 할때
				if(GoodsDiscountType.PACKAGE.getCode().equals(discountType) && goodsDiscountApprInfo.getDiscountStartDt() != null){
					//승인완료 처리가 되면 새로 저장된 할인 내역의, 바로 전 할인 내역의 종료일(종료일이 '2999-12-31 23:59:59'인 할인내역)을 새로 저장된 시작일 - 1초 로 변경 한다.
					goodsRegistService.updateGoodsDiscountApprEndTime(goodsDiscountApprInfo.getIlGoodsId(), goodsDiscountApprInfo.getDiscountStartDt());
					goodsRegistService.updateGoodsDiscountEndTime(goodsDiscountApprInfo.getIlGoodsId(), goodsDiscountApprInfo.getDiscountStartDt());
				}

				if(discountTranNum > 0 && packageItemDiscountTranNum > 0) {
					//가격정보 > 판매 가격정보 등록
					GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

					goodsRegistRequestDto.setInDebugFlag(false);
					goodsRegistRequestDto.setIlGoodsId(goodsDiscountApprInfo.getIlGoodsId());
					//프로시져 실행 시 RollBack이 되지 않으므로 주석 처리 함
					//goodsRegistService.spGoodsPriceUpdateWhenGoodsDiscountChanges(goodsRegistRequestDto);

					enums = BaseEnums.Default.SUCCESS;
				}
			}
			else if(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())
							|| goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode())){	//현재 상태가 반려or요청철회 상태라면
				enums = BaseEnums.Default.SUCCESS;
			}
		}

		log.info("enums : " + enums);

		assertTrue(enums.equals(BaseEnums.Default.SUCCESS));
	}

	@Test
	void 풀무원샵_상품코드_조회() throws Exception {
		try {
			String ilGoodsid = "90018139";
			goodsRegistService.getGoodsCodeList(ilGoodsid);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
	}


	@Test
	void 풀무원샵_상품코드_저장() throws Exception {
		try {
			String ilGoodsid = "90018139";
			String createId  = "1";
			List<GoodsCodeVo> list = new ArrayList<>();

			GoodsCodeVo vo1 = new GoodsCodeVo();
			vo1.setGoodsNo("1111");
			list.add(vo1);

			GoodsCodeVo vo2 = new GoodsCodeVo();
			vo2.setGoodsNo("2222");
			list.add(vo2);

			GoodsCodeVo vo3 = new GoodsCodeVo();
			vo3.setGoodsNo("3333");
			list.add(vo3);


			goodsRegistService.setGoodsCodeList(ilGoodsid, list, createId);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
	}

}
