package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 기획정 증점품 상품 정보 DTO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 10. 25.            김명진         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 기획정 증점품 상품 정보 DTO")
public class OrderClaimGiftGoodsInfoDto {

	@ApiModelProperty(value = "기획전PK")
	private long evExhibitId;

	@ApiModelProperty(value = "기획전 상품PK")
	private long evExhibitGiftId;

	@ApiModelProperty(value = "증정조건유형 - 상품별(GIFT_TP.GOODS) 장바구니별(GIFT_TP.CART)")
	private String giftTp;

	@ApiModelProperty(value = "장바구니별제한금액")
	private int overPrice;

	@ApiModelProperty(value = "증정범위 유형 - 포함(GIFT_RANGE_TP.INCLUDE) 동일(GIFT_RANGE_TP.EQUAL)")
	private String giftRangeTp;

	@ApiModelProperty(value = "적용대상 유형 - 상품(GIFT_TARGET_TP.GOODS) 브랜드(GIFT_TARGET_TP.BRAND)")
	private String giftTargetTp;

	@ApiModelProperty(value = "적용대상상품PK")
	private long targetIlGoodsId;

	@ApiModelProperty(value = "적용대상브랜드PK")
	private long targetBrandId;
}
