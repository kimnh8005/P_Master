package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문 리스트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 12.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 주문 리스트 Dto")
public class MallOrderListDto {

    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문 클레임 PK")
    private String odClaimId;

	@ApiModelProperty(value = "주문일자(등록일시)/주문 클레임(등록일시), YYYY-MM-DD")
	private String createDt;

	@ApiModelProperty(value = "상품명(상품명 외 몇건)")
    private String repGoodsNm;

    @ApiModelProperty(value = "대표 상품 이미지")
    private String repGoodsImg;

    @ApiModelProperty(value = "대표 상품의 주문상태")
    private String repGoodsOrderStatusNm;

    @ApiModelProperty(value = "대표 상품의 주문상태 코드명")
    private String repGoodsOrderStatusCd;

    @ApiModelProperty(value = "총 결제 금액(주문건내 실결제금액)/환불금액 (주문상품금액 - 상품쿠폰 - 장바구니 쿠폰 +- 배송비)")
    private int paidPrice;

    @ApiModelProperty(value = "추가결제여부")
    private String addPaymentYn;

    @ApiModelProperty(value = "골라담기 구분 C:균일가 골라담기 G:녹즙골라담기 이외 일반")
    private String packageGbn;

    @ApiModelProperty(value = "주문 총 상품개수 -1")
    private int orderGoodsCount;

    @ApiModelProperty(value = "클레임 총 상품개수 -1")
    private int claimGoodsCount;

	@ApiModelProperty(value = "대표 상품명")
    private String goodsNm;

    @ApiModelProperty(value = "상담원 결제 여부")
    private String directPaymentYn;

    @ApiModelProperty(value = "대표 상품 PK")
    private String ilGoodsId;

    @ApiModelProperty(value = "주문상태 배송유형")
    private String orderStatusDeliTp;

    @ApiModelProperty(value = "일반주문: N, 선물하기주문: Y")
    private String presentYn;

	@ApiModelProperty(value = "선물 받기상태")
	private String presentOrderStatus;

    @ApiModelProperty(value = "정기배송 주문 여부")
    private String regularYn;

    @ApiModelProperty(value = "정기배송결제실패건수")
    private int regularPaymentFailCnt;
}