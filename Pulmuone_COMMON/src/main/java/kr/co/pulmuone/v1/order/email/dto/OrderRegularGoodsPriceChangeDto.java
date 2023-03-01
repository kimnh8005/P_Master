package kr.co.pulmuone.v1.order.email.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 결과 결제 대상 목록 조회 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 03. 11.	천혜현		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "자동메일 정기배송 상품 가격 변동 정보 Dto")
public class OrderRegularGoodsPriceChangeDto {

	@ApiModelProperty(value = "정기배송주문신청PK")
	private Long odRegularReqId;

    @ApiModelProperty(value = "상품유형 IL_GOODS.GOODS_TP 공통코드(GOODS_TYPE)")
    private String goodsTp;

    @ApiModelProperty(value = "상품 PK")
    private String ilGoodsId;

    @ApiModelProperty(value = "상품명 : IL_GOODS.GOODS_NM")
    private String goodsNm;

    @ApiModelProperty(value = "주문수량")
    private int orderCnt;

    @ApiModelProperty(value = "대표 상품 이미지")
    private String goodsImgNm;

	@ApiModelProperty(value = "변경 전 결제금액")
	private int beforeSalePrice;

	@ApiModelProperty(value = "변경 후 결제금액")
	private int afterSalePrice;

	@ApiModelProperty(value = "상품 정상가")
	private int recommendedPrice;

}
