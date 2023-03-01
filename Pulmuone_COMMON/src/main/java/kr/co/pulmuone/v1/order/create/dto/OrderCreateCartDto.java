package kr.co.pulmuone.v1.order.create.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.shopping.cart.dto.CartAdditionalGoodsDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문생성 카트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 04. 12.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문생성 카트 Dto")
public class OrderCreateCartDto {

    @ApiModelProperty(value = "배송타입")
    private String deliveryType;

	@ApiModelProperty(value = "배송정책 index")
	private int shippingIndex;

	@ApiModelProperty(value = "장바구니 PK")
	private Long spCartId;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "재고 수량")
    private int stockQty;

    @ApiModelProperty(value = "구매 수량")
    private int qty;

    @ApiModelProperty(value = "결제금액")
	private int paymentPrice;

    @ApiModelProperty(value = "배송비")
	private int shippingRecommendedPrice;

    @ApiModelProperty(value = "도착 예정일")
	private LocalDate deliveryDt;

    @ApiModelProperty(value = "정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "판매가")
    private int salePrice;

    /**
     * 주문일자 (주문 I/F 일자) - 관리자 주문생성 에서 사용
     */
    @ApiModelProperty(value = "주문일자 (주문 I/F 일자)")
    private LocalDate orderIfDt;

    /**
     * 출고 예정 일자 - 관리자 주문생성 에서 사용
     */
    @ApiModelProperty(value = "출고 예정 일자")
    private LocalDate shippingDt;

    /**
     * 상품명 - 관리자 주문생성 에서 사용
     */
    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    /**
     * 출고처 PK
     */
    @ApiModelProperty(value = "출고처 PK")
    private Long urWarehouseId;

    @ApiModelProperty(value = "출고처명")
    private String warehouseNm;

    @ApiModelProperty(value = "도착 예정일 선택일")
	private List<LocalDate> choiceArrivalScheduledDateList;

    /**
     * 상품 타입
     */
    private String goodsType;

    /**
     * 새벽배송YN
     */
    @ApiModelProperty(value = "새벽배송여부")
    private String dawnDeliveryYn;

    /**
     * 재고 관련
     * */
    @ApiModelProperty(value = "품목별 출고처 PK")
    private Long ilItemWarehouseId;

    @ApiModelProperty(value = "재고 무제한")
    private boolean unlimitStockYn;

    @ApiModelProperty(value = "미연동 재고 수량")
    private int notIfStockCnt;
    
    /**
     * 일일배송
     */
    @ApiModelProperty(value = "일일상품 타입")
    private String goodsDailyType;
    
    @ApiModelProperty(value = "일일 배송주기코드")
    private String goodsDailyCycleType;

    @ApiModelProperty(value = "일일 배송주기명")
    private String goodsDailyCycleTypeName;

    @ApiModelProperty(value = "일일 배송기간코드")
    private String goodsDailyCycleTermType;

    @ApiModelProperty(value = "일일 배송기간명")
    private String goodsDailyCycleTermTypeName;

    @ApiModelProperty(value = "일일 배송 녹즙 요일 코드 -- 일일 녹즙일때만")
    private String[] goodsDailyCycleGreenJuiceWeekType;

    @ApiModelProperty(value = "일일 배송 녹즙 요일명 -- 일일 녹즙일때만")
    private String[] goodsDailyCycleGreenJuiceWeekTypeName;

    @ApiModelProperty(value = "일일 배송 요일명")
    private String goodsDailyWeekText;

    @ApiModelProperty(value = "알러지 식단 선택여부 - 일일/베이비판매유형일때만")
    private String goodsDailyAllergyYn;

    @ApiModelProperty(value = "일괄 배송 가능 여부 - 일일/베이비판매유형일때만")
    private String goodsDailyBulkYn;

    @ApiModelProperty(value = "일괄배송 배송 세트 코드")
    private String goodsBulkType;

    @ApiModelProperty(value = "일괄배송 배송 세트 명")
    private String goodsBulkTypeName;

    @ApiModelProperty(value = "배송방식(매일,격일)")
    private String storeDeliveryIntervalType;

    /**
     * 추가 상품
     * */
    @ApiModelProperty(value = "추가구성상품 존재여부")
    private String additionalGoodsExistYn;

    @ApiModelProperty(value = "추가구성상품")
    private List<CartAdditionalGoodsDto> additionalGoods;

}