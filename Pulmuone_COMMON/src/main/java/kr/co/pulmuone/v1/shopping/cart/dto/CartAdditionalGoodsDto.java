package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartAddGoodsVo;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "장바구니 추가 구성상품 DTO")
public class CartAdditionalGoodsDto {
	public CartAdditionalGoodsDto(SpCartAddGoodsVo vo) {
		this.setSpCartAddGoodsId(vo.getSpCartAddGoodsId());
		this.setIlGoodsId(vo.getIlGoodsId());
		this.setQty(vo.getQty());
		this.setSalePrice(vo.getSalePrice());
		setCalculationPaymentPrice();
	}

	public void setGoodsResultVo(BasicSelectGoodsVo vo) {
		this.setGoodsName(vo.getGoodsName());
		this.setUrBrandId(vo.getUrBrandId());
		this.setTaxYn(vo.getTaxYn());
		this.setRecommendedPrice(vo.getRecommendedPrice());
		// 판매가가 정가보다 클경우 정가로 처리
		if (this.getRecommendedPrice() < this.getSalePrice()) {
			this.setSalePrice(this.getRecommendedPrice());
			setCalculationPaymentPrice();
		}
		this.setSaleStatus(vo.getSaleStatus());
		this.setStockQty(vo.getStockQty());
		this.setArrivalScheduledDateDto(vo.getArrivalScheduledDateDto());
		this.setArrivalScheduledDateDtoList(vo.getArrivalScheduledDateDtoList());
	}

	public void setCalculationPaymentPrice() {
		this.setPaymentPrice(this.getSalePrice() * this.getQty());
	}

	/**
	 * 장바구니 추가 구성 상품 PK
	 */
	private Long spCartAddGoodsId;

	/**
	 * 상품 PK
	 */
	private Long ilGoodsId;

	/**
	 * 표준브랜드 PK
	 */
	private Long urBrandId;

	/**
	 * 상품명
	 */
	private String goodsName;

	/**
	 * 구매 수량
	 */
	private int qty;

	/**
	 * 과세여부
	 */
	private String taxYn;

	/**
	 * 상품 정가
	 */
	private int recommendedPrice;

	/**
	 * 상품 판매가
	 */
	private int salePrice;

	/**
	 * 묶음 상품별 할인 정보
	 */
	List<CartGoodsDiscountDto> discount;

	/**
	 * 결제금액
	 */
	private int paymentPrice;

	/**
	 * 상품 상태
	 */
	private String saleStatus;

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
	 * 새벽배송 도착 예정일 - 브릿지 페이지에서 새벽배송 가능할때만
	 */
	private ArrivalScheduledDateDto dawnArrivalScheduledDateDto;

	/**
	 * 새벽배송 선택 도착 예정일 리스트 - 브릿지 페이지에서 새벽배송 가능할때만
	 */
	private List<ArrivalScheduledDateDto> dawnArrivalScheduledDateDtoList;
	
	/**
	 * 재고관련 - BOS 주문생성시 사용
	 */
	@ApiModelProperty(value = "품목별 출고처 PK")
	private Long ilItemWarehouseId;

	@ApiModelProperty(value = "재고 무제한")
	private boolean unlimitStockYn;

	@ApiModelProperty(value = "미연동 재고 수량")
	private int notIfStockCnt;
}
