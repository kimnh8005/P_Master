package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문리스트 관련 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 03. 15.    천혜현        최초작성
 *
 *
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "미출 주문 상세리스트 > 일괄 취소완료 팝업 저장 Dto")
public class OrderChangeClaimStatusCCDto{

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문상세 PK")
	private Long odOrderDetlId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "클레임 수량")
	private String claimCnt;

	@ApiModelProperty(value = "판매가")
	private int salePrice;

	@ApiModelProperty(value = "배송비")
	private int shippingPrice;

	/* 일괄 취소 팝업*/
	@ApiModelProperty(value = "클레임사유(대)")
	private long searchLClaimCtgryId;

	@ApiModelProperty(value = "클레임사유(중)")
	private long searchMClaimCtgryId;

	@ApiModelProperty(value = "귀책처")
	private long searchSClaimCtgryId;

	/* 일괄 재배송 팝업*/
	@ApiModelProperty(value = "재배송할 상품 IL_GOODS_ID")
	private long changeIlGoodsId;

	@ApiModelProperty(value = "미출정보 PK")
	private long ifUnreleasedInfoId;
}

