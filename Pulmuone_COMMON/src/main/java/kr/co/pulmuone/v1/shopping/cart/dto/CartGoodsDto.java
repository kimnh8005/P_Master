package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.BuyPurchaseType;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.CartEmployeeYn;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.CartPromotionType;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.DeliveryType;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 상품 DTO")
public class CartGoodsDto {

	public CartGoodsDto(SpCartVo vo) {
		this.setIngRegularGoodsYn(vo.getIngRegularGoodsYn());
		this.setSpCartId(vo.getSpCartId());
		this.setIlGoodsId(vo.getIlGoodsId());
		this.setIlShippingTmplId(vo.getIlShippingTmplId());
		this.setDeliveryQty(vo.getQty());
		this.setQty(vo.getQty());
		this.setIlGoodsReserveOptionId(vo.getIlGoodsReserveOptionId());
		this.setForwardingScheduledDate(vo.getReleaseDate());
		this.setArrivalScheduledDate(vo.getArriveDate());
		this.setGoodsDailyCycleType(vo.getGoodsDailyCycleType());
		this.setGoodsDailyCycleTypeName(vo.getGoodsDailyCycleTypeName());
		this.setGoodsDailyCycleTermType(vo.getGoodsDailyCycleTermType());
		this.setGoodsDailyCycleTermTypeName(vo.getGoodsDailyCycleTermTypeName());
		if (vo.getGoodsDailyCycleGreenJuiceWeekType() != null) {
			String[] goodsDailyCycleGreenJuiceWeekTypeList = vo.getGoodsDailyCycleGreenJuiceWeekType().split(",");
			String[] goodsDailyCycleGreenJuiceWeekTypeNameList = goodsDailyCycleGreenJuiceWeekTypeList.clone();
			for (int i = 0; i < goodsDailyCycleGreenJuiceWeekTypeNameList.length; i++) {
				goodsDailyCycleGreenJuiceWeekTypeNameList[i] = GoodsEnums.WeekCodeByGreenJuice
						.findByCode(goodsDailyCycleGreenJuiceWeekTypeNameList[i]).getCodeName();
			}
			this.setGoodsDailyCycleGreenJuiceWeekType(goodsDailyCycleGreenJuiceWeekTypeList);
			this.setGoodsDailyCycleGreenJuiceWeekTypeName(goodsDailyCycleGreenJuiceWeekTypeNameList);
		}
		this.setGoodsDailyAllergyYn(vo.getGoodsDailyAllergyYn());
		this.setGoodsDailyBulkYn(vo.getGoodsDailyBulkYn());
		this.setGoodsBulkType(vo.getGoodsBulkType());
		this.setGoodsBulkTypeName(vo.getGoodsBulkTypeName());
		this.setCartPromotionType(vo.getCartPromotionType());
		this.setEvExhibitId(vo.getEvExhibitId());
		this.setPmAdExternalCd(vo.getPmAdExternalCd());
		this.setPmAdInternalPageCd(vo.getPmAdInternalPageCd());
		this.setPmAdInternalContentCd(vo.getPmAdInternalContentCd());
		this.setApplyRestockNotice(vo.isApplyRestockNotice());
	}

	public void setByGoodsResultVo(BasicSelectGoodsVo vo) {
		this.setUrSupplierId(vo.getUrSupplierId());
		this.setIlItemCd(vo.getIlItemCode());
		this.setGoodsType(vo.getGoodsType());
		this.setSystemSaleStatus(vo.getSystemSaleStatus());
		this.setSaleType(vo.getSaleType());
		this.setGoodsName(vo.getGoodsName());
		this.setSaleStatus(vo.getSaleStatus());
		this.setTaxYn(vo.getTaxYn());
		this.setLimitMinCnt(vo.getLimitMinimumCount());
		this.setLimitMaxCntYn(vo.getLimitMaximumCountYn());
		this.setLimitMaxType(vo.getLimitMaximumType());
		this.setLimitMaxCnt(vo.getLimitMaximumCount());
		this.setGoodsDailyType(vo.getGoodsDailyType());
		this.setStockQty(vo.getStockQty());
		this.setArrivalScheduledDateDto(vo.getArrivalScheduledDateDto());
		this.setArrivalScheduledDateDtoList(vo.getArrivalScheduledDateDtoList());
		this.setUrBrandId(vo.getUrBrandId());
		this.setDpBrandId(vo.getDpBrandId());
		this.setDpBrandName(vo.getDpBrandName());
		this.setUrWareHouseId(vo.getUrWareHouseId());
		this.setIlCtgryId(vo.getIlCtgryId());
		this.setSalePrice(vo.getSalePrice());
		this.setRecommendedPrice(vo.getRecommendedPrice());
		this.setPresentYn(vo.getPresentYn());
	}

	public void checkChangeSaleType(String deliveryType) {
		if (!GoodsEnums.SaleStatus.STOP_SALE.getCode().equals(saleStatus) && !GoodsEnums.SaleStatus.STOP_PERMANENT_SALE.getCode().equals(saleStatus)) {
			//일반 배송 : 일반, 일반/정기, 일일상품에 일괄배송시
			if ((ShoppingEnums.DeliveryType.NORMAL.getCode().equals(deliveryType) && !(GoodsEnums.SaleType.NORMAL.getCode().equals(saleType) || GoodsEnums.SaleType.REGULAR.getCode().equals(saleType) || GoodsEnums.SaleType.DAILY.getCode().equals(saleType) || GoodsEnums.SaleType.NOT_FOR_SALE.getCode().equals(saleType)))
					|| (ShoppingEnums.DeliveryType.RESERVATION.getCode().equals(deliveryType) && !GoodsEnums.SaleType.RESERVATION.getCode().equals(saleType))
					|| (ShoppingEnums.DeliveryType.REGULAR.getCode().equals(deliveryType) && !GoodsEnums.SaleType.REGULAR.getCode().equals(saleType))) {
				saleStatus = GoodsEnums.SaleStatus.STOP_SALE.getCode();
			}
		}
	}

	public void setBuyPurchaseType(GetCartDataRequestDto reqDto, BasicSelectGoodsVo goodsResultVo) {
		buyPurchaseType = BuyPurchaseType.BUY_POSSIBLE.getCode();
		if (reqDto.isEmployee()) {
			if (CartEmployeeYn.NO_EMPLOY_DISCOUNT.getCode().equals(reqDto.getEmployeeYn())) {
				// 회원가 주문시
				if (!"Y".equals(goodsResultVo.getPurchaseMemberYn())) {
					buyPurchaseType = BuyPurchaseType.NOT_BUY_BUYER.getCode();
				}
			} else {
				// 임직원가 주문시
				if (!"Y".equals(goodsResultVo.getPurchaseEmployeeYn())) {
					buyPurchaseType = BuyPurchaseType.NOT_BUY_EMPLOYEE.getCode();
				} else if(CartPromotionType.EXHIBIT_SELECT.getCode().equals(cartPromotionType)) {
					// 기획전 골라담기 상품은 임직원 가로 구매 불가
					buyPurchaseType = BuyPurchaseType.NOT_BUY_EMPLOYEE.getCode();
				}
			}
		}
	}

	public void setBuyPurchaseType(CartDeliveryDto deliveryDto, int cartGoodsIndex) {
		// 일일배송 합배송일때 첫번째 상품 제외하고 모두 구매 불가 처리
		if (DeliveryType.DAILY.getCode().equals(deliveryDto.getDeliveryType()) && cartGoodsIndex != 0) {
			buyPurchaseType = BuyPurchaseType.BUY_IMPOSSIBLE.getCode();
		}
	}

	/**
	 * 배송장소코드 - 녹즙일때만 노출
	 */
	private String storeDeliveryTypeCode;

	/**
	 * 배송장소명
	 */
	private String storeDeliveryTypeName;

	/**
	 * 스토어(매장/가맹점)PK (ERP연동)
	 */
	private Long urStoreId;

	/**
	 * 정기배송 기존 상품 여부 (Y: 기존, N: 추가)
	 */
	private String ingRegularGoodsYn;

	/**
	 * 장바구니 PK
	 */
	private Long spCartId;

	/**
	 * 상품 PK
	 */
	private Long ilGoodsId;

	/**
	 * 공급처 PK
	 */
	private Long urSupplierId;

	/**
	 * 품목 PK
	 */
	private String ilItemCd;

	/**
	 * 배송 탬플릿 PK
	 */
	private Long ilShippingTmplId;

	/**
	 * 상품 타입
	 */
	private String goodsType;

	/**
	 * 판매 유형
	 */
	private String saleType;

	/**
	 * 상품명
	 */
	private String goodsName;

	/**
	 * 상품 이미지
	 */
	private String goodsImage;

	/**
	 * 회원 구매 권한 타입
	 */
	private String buyPurchaseType;

	/**
	 * 상품 상태 (DB 판매 상태)
	 */
	private String systemSaleStatus;

	/**
	 * 상품 상태 (프론트 정의 판매 상태)
	 */
	private String saleStatus;

	/**
	 * 구매 수량 (실 배송 수량)
	 */
	private int deliveryQty;

	/**
	 * 구매 수량 (고객이 선택한 수량)
	 */
	private int qty;

	/**
	 * 상품 정가
	 */
	private int recommendedPrice;

	/**
	 * 상품 정가 (*수량)
	 */
	private int recommendedPriceMltplQty;

	/**
	 * 상품 판매가
	 */
	private int salePrice;

	/**
	 * 상품 판매가 (*수량)
	 */
	private int salePriceMltplQty;

	/**
	 * 상품 결제 금액 (모든 할인 포함)
	 */
	private int paymentPrice;

	/**
	 * 과세 여부 (Y:과세)
	 */
	private String taxYn;

	/**
	 * 최소 구매 수량
	 */
	private int limitMinCnt;

	/**
	 * 최대 구매 수량 제한 여부
	 */
	private String limitMaxCntYn;

	/**
	 * 최대 구매수량유형
	 */
	private String limitMaxType;

	/**
	 * 최대 구매 수량
	 */
	private int limitMaxCnt;

	/**
	 * 재고 수량
	 */
	private int stockQty;

	/**
	 * 찜한 상품 PK
	 */
	private Long spFavoritesGoodsId;

	/**
	 * 주소기반으로 배송 가능 여부
	 */
	private boolean isShippingPossibility;

	/**
	 * 배송불가 내용
	 */
	private String shippingImpossibilityMsg;

	/**
	 * 신규회원 특가 여부
	 */
	private boolean isNewBuyerSpecials;

	/**
	 * 신규회원 특가 판매가
	 */
	private int newBuyerSpecialSsalePrice;

	/**
	 * 예약 정보 PK
	 */
	private Long ilGoodsReserveOptionId;

	/**
	 * 예약 회차 정보
	 */
	private int reserveSaleSeq;

	/**
	 * 출고 예정 일자
	 */
	private LocalDate forwardingScheduledDate;

	/**
	 * 예약 정보 도착일자
	 */
	private LocalDate arrivalScheduledDate;

	/**
	 * 일일상품 유형(공통코드)
	 */
	private String goodsDailyType;

	/**
	 * 일일 배송주기코드
	 */
	private String goodsDailyCycleType;

	/**
	 * 일일 배송주기명
	 */
	private String goodsDailyCycleTypeName;

	/**
	 * 일일 배송기간코드
	 */
	private String goodsDailyCycleTermType;

	/**
	 * 일일 배송기간명
	 */
	private String goodsDailyCycleTermTypeName;

	/**
	 * 일일 배송 녹즙 요일 코드 -- 일일 녹즙일때만
	 */
	private String[] goodsDailyCycleGreenJuiceWeekType;

	/**
	 * 일일 배송 녹즙 요일명 -- 일일 녹즙일때만
	 */
	private String[] goodsDailyCycleGreenJuiceWeekTypeName;

	/**
	 * 알러지 식단 선택여부 - 일일/베이비판매유형일때만
	 */
	private String goodsDailyAllergyYn;

	/**
	 * 일괄 배송 가능 여부 - 일일/베이비판매유형일때만
	 */
	private String goodsDailyBulkYn;

	/**
	 * 일괄배송 배송 세트 코드
	 */
	private String goodsBulkType;

	/**
	 * 일괄배송 배송 세트 명
	 */
	private String goodsBulkTypeName;

	/**
	 * 할인 정보
	 */
	private List<CartGoodsDiscountDto> discount;

	/**
	 * 추가 구성 상품
	 */
	private List<CartAdditionalGoodsDto> additionalGoods;

	/**
	 * 패키지 상품 상품
	 */
	private List<CartGoodsPackageDto> goodsPackage;

	/**
	 * 상품 도착 예정일 Dto
	 */
	private ArrivalScheduledDateDto arrivalScheduledDateDto;

	/**
	 * 상품 도착 예정일 Dto 리스트
	 */
	private List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList;

	/**
	 * 새벽배송 도착 예정일 - 브릿지 페이지에서 새벽배송 가능할때만
	 */
	private ArrivalScheduledDateDto dawnArrivalScheduledDateDto;

	/**
	 * 새벽배송 선택 도착 예정일 리스트 - 브릿지 페이지에서 새벽배송 가능할때만
	 */
	private List<ArrivalScheduledDateDto> dawnArrivalScheduledDateDtoList;

	/**
	 * 표준브랜드 PK
	 */
	private Long urBrandId;

	/**
	 * 전시브랜드 PK
	 */
	private Long dpBrandId;

	/**
	 * 전시브랜드명
	 */
	private String dpBrandName;

	/**
	 * 출고처 PK
	 */
	private Long urWareHouseId;

	/**
	 * 출고처 명
	 */
	private String warehouseNm;

	/**
	 * 카테고리 PK
	 */
	private Long ilCtgryId;

	/**
	 * 기획전 유형 골라담기 타입
	 */
	private String cartPromotionType;

	/**
	 * 골라담기 기획전 PK
	 */
	private Long evExhibitId;

	/**
	 * 패키지 상품 상품
	 */
	private List<CartPickGoodsDto> pickGoods;


	/**
	 * 주문시 정렬 순서
	 */

	private int orderSort;

	/**
	 * 외부광고코드PK
	 */
	private String pmAdExternalCd;

	/**
	 * 내부광고코드-페이지코드
	 */
	private String pmAdInternalPageCd;

	/**
	 * 내부광고코드-영역코드
	 */
	private String pmAdInternalContentCd;

	/**
	 * 재입고 신청여부
	 */
	private boolean isApplyRestockNotice;

	/**
	 * 선물하기 가능 여부
	 */
	private String presentYn;
}
