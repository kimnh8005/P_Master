package kr.co.pulmuone.v1.goods.goods.dto.vo;

import kr.co.pulmuone.v1.comm.enums.ErpApiEnums.UrWarehouseGroupId;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class BasicSelectGoodsVo {

	/**
	 * 상품PK
	 */
	private Long ilGoodsId;

	/**
	 * 품목코드
	 */
	private String ilItemCode;

	/**
	 * 상품유형 공통코드
	 */
	private String goodsType;

	/**
	 * 판매유형 공통코드
	 */
	private String saleType;

	/**
	 * 상품 정상가
	 */
	private int recommendedPrice;

	/**
	 * 상품 판매가
	 */
	private int salePrice;

	/**
	 * 상품 적용된 할인유형
	 */
	private String discountType;

	/**
	 * 상품 적용된 할인유형명
	 */
	private String discountTypeName;

	/**
	 * 상품 원가
	 */
	private int standardPrice;

	/**
	 * 상품명
	 */
	private String goodsName;

	/**
	 * 상품 상태 (DB 판매 상태)
	 */
	private String systemSaleStatus;

	/**
	 * 상품 상태 (프론트 정의 판매 상태)
	 */
	private String saleStatus;

	/**
	 * 최소 구매수량
	 */
	private int limitMinimumCount;

	/**
	 * 최대 구매수량
	 */
	private int limitMaximumCount;

	/**
	 * 최대 구매수량유형 공통코드
	 */
	private String limitMaximumType;

	/**
	 * 최대 구매수량 제한 여부
	 */
	private String limitMaximumCountYn;

	/**
	 * 새벽배송가능여부
	 */
	private String dawnDeliveryYn;

	/**
	 * 일일상품 유형
	 */
	private String goodsDailyType;

	/**
	 * 알러지 식단 선택여부
	 */
	private String goodsDailyAllergyYn;

	/**
	 * 일괄 배송 가능 여부
	 */
	private String goodsDailyBulkYn;

	/**
	 * 배송불가지역 유형
	 */
	private String undeliverableAreaType;

	/**
	 * 공급처 PK
	 */
	private Long urSupplierId;

	/**
	 * 새벽배송 가능 여부
	 */
	public boolean canDawnDelivery() {
		boolean canDawnDelivery = false;

		// 판매유형이 일반,정기,예약일 경우 새벽배송 가능
		if (this.dawnDeliveryYn.equals("Y") && (this.saleType.equals(GoodsEnums.SaleType.NORMAL.getCode())
				|| this.saleType.equals(GoodsEnums.SaleType.REGULAR.getCode())
				|| this.saleType.equals(GoodsEnums.SaleType.RESERVATION.getCode()))) {
			canDawnDelivery = true;
		}
		return canDawnDelivery;
	}

	/**
	 * 출고처 PK
	 */
	private Long urWareHouseId;

	/**
	 * 쿠폰 사용 허용 여부
	 */
	private String couponUseYn;

	/**
	 * 과세여부
	 */
	private String taxYn;

	/**
	 * 표준 브랜드PK
	 */
	private Long urBrandId;

	/**
	 * 전시 브랜드PK
	 */
	private Long dpBrandId;

	/**
	 * 전시 브랜드명
	 */
	private String dpBrandName;

	/**
	 * 카테고리 PK
	 */
	private Long ilCtgryId;

	/**
	 * 재고
	 */
	private int stockQty;

	/**
	 * 배송 가능 도착 예정일 DTO 리스트
	 */
	private List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList;

	/**
	 * 상품 배송 도착 예정일 DTO
	 */
	private ArrivalScheduledDateDto arrivalScheduledDateDto;

	/**
	 * 묶음상품 이미지 타입
	 */
	private String goodsPackageImageType;

	/**
	 * 출고처 그룹 코드
	 */
	private String warehouseGroupCode;

	/**
	 * 회원 구매 가능 여부
	 */
	private String purchaseMemberYn;

	/**
	 * 임직원 구매 가능 여부
	 */
	private String purchaseEmployeeYn;

	/**
	 * 비회원 구매 가능 여부
	 */
	private String purchaseNonmemberYn;

	/**
	 * 건강 기능식품 여부
	 */
	private String healthGoodsYn;

	/**
	 * 매장판매 여부
	 */
	private String saleShopYn;

	/**
	 * 선물하기 가능 여부
	 */
	private String presentYn;

	/**
	 * 녹즙 택배배송 여부
	 * @return
	 */
	public boolean isGreenjuiceParcel() {
		return UrWarehouseGroupId.ACCOUNT_FDD.getCode().equals(warehouseGroupCode);
	}

	/**
	 * 상품명(이미지 표시용)
	 */
	private String detailGoodsName;
}
