package kr.co.pulmuone.v1.shopping.cart.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.ShippingTemplateGroupByVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 배송 정책 DTO")
public class CartShippingDto {

	public CartShippingDto(int shippingTemplateDataIndex, ShippingTemplateGroupByVo vo, boolean isDawnDeliveryArea) {
		this.setShippingIndex(shippingTemplateDataIndex);
		this.setUrWarehouseId(vo.getUrWarehouseId());
		this.setWarehouseNm(vo.getWarehouseNm());
		this.setBundleYn(vo.getBundleYn());
		this.setIlShippingTmplId(vo.getIlShippingTmplId());
		this.setDawnDeliveryYn(isDawnDeliveryArea && "Y".equals(vo.getDawnDeliveryYn()) ? "Y" : "N");
		this.setGoodsType(vo.getGoodsType());
	}

	/**
	 * 출고처 index key
	 */
	@ApiModelProperty(value = "출고처 index key")
	private int shippingIndex;

	/**
	 * 출고처 PK
	 */
	@ApiModelProperty(value = "출고처 PK")
	private Long urWarehouseId;

	@ApiModelProperty(value = "출고처명")
	private String warehouseNm;

	/**
	 * 장바구니 배송비 탬플릿 PK
	 */
	@ApiModelProperty(value = "장바구니 배송비 탬플릿 PK")
	private Long ilShippingTmplId;

	/**
	 * 합배송 여부
	 */
	@ApiModelProperty(value = "합배송 여부")
	private String bundleYn;

	/**
	 * 상품 정가
	 */
	@ApiModelProperty(value = "")
	private int goodsRecommendedPrice;

	/**
	 * 상품 판매가
	 */
	@ApiModelProperty(value = "상품 판매가")
	private int goodsSalePrice;

	/**
	 * 상품 할인 금액
	 */
	@ApiModelProperty(value = "상품 할인 금액")
	private int goodsDiscountPrice;

	/**
	 * 상품 결제 금액
	 */
	@ApiModelProperty(value = "상품 결제 금액")
	private int goodsPaymentPrice;

	/**
	 * 상품 결제 과세 금액
	 */
	@ApiModelProperty(value = "상품 결제 과세 금액")
	private int goodsTaxPaymentPrice;

	/**
	 * 상품 결제 비과세 금액
	 */
	@ApiModelProperty(value = "상품 결제 비과세 금액")
	private int goodsTaxFreePaymentPrice;

	/**
	 * 기본 배송비
	 */
	@ApiModelProperty(value = "기본 배송비")
	private int shippingBaiscPrice;

	/**
	 * 지역별 배송비
	 */
	@ApiModelProperty(value = "지역별 배송비")
	private int shippingRegionPrice;

	/**
	 * 배송비 (기본 배송비 + 지역별 배송비 )
	 */
	@ApiModelProperty(value = "배송비 (기본 배송비 + 지역별 배송비 )")
	private int shippingRecommendedPrice;

	/**
	 * 총 배송비 할인금액
	 */
	@ApiModelProperty(value = "총 배송비 할인금액")
	private int shippingDiscountPrice;

	/**
	 * 총 배송비 할인금액
	 */
	@ApiModelProperty(value = "총 배송비 할인금액")
	private int shippingPaymentPrice;

	/**
	 * 정산 배송비금액
	 */
	@ApiModelProperty(value = "정산 배송비금액")
	private int originShippingPrice;

	/**
	 * 주문금액 (결제 금액)
	 */
	@ApiModelProperty(value = "주문금액 (결제 금액)")
	private int paymentPrice;

	/**
	 * 쿠폰 발급 PK
	 */
	@ApiModelProperty(value = "쿠폰 발급 PK")
	private Long pmCouponIssueId;

	/**
	 * 배송비 무료배송 조건을 위한 추가 결제금액
	 */
	@ApiModelProperty(value = "배송비 무료배송 조건을 위한 추가 결제금액")
	private int freeShippingForNeedGoodsPrice;

	/**
	 * 도착 예정일
	 */
	@ApiModelProperty(value = "도착 예정일")
	private LocalDate arrivalScheduledDate;

	/**
	 * 주문일자 (주문 I/F 일자) - 관리자 주문생성 에서 사용
	 */
	@ApiModelProperty(value = "주문일자 (주문 I/F 일자)")
	private LocalDate orderDate;

	/**
	 * 출고 예정 일자 - 관리자 주문생성 에서 사용
	 */
	@ApiModelProperty(value = "출고 예정 일자")
	private LocalDate forwardingScheduledDate;

	/**
	 * 새벽배송YN
	 */
	@ApiModelProperty(value = "새벽배송YN")
	private String dawnDeliveryYn;

	/**
	 * 선택 도착 예정일 리스트
	 */
	@ApiModelProperty(value = "선택 도착 예정일 리스트")
	private List<LocalDate> choiceArrivalScheduledDateList;

	/**
	 * 상품
	 */
	@ApiModelProperty(value = "상품")
	private List<CartGoodsDto> goods;

	/**
	 * 새벽배송 도착 예정일 - 브릿지 페이지에서 새벽배송 가능할때만
	 */
	@ApiModelProperty(value = "새벽배송 도착 예정일 - 브릿지 페이지에서 새벽배송 가능할때만")
	private LocalDate dawnArrivalScheduledDate;

	/**
	 * 새벽배송 선택 도착 예정일 리스트 - 브릿지 페이지에서 새벽배송 가능할때만
	 */
	@ApiModelProperty(value = "새벽배송 선택 도착 예정일 리스트 - 브릿지 페이지에서 새벽배송 가능할때만")
	private List<LocalDate> dawnChoiceArrivalScheduledDateList;

	/**
	 * 상품 타입
	 */
	private String goodsType;
}
