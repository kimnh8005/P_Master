package kr.co.pulmuone.v1.order.regular.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 신청 결과 상세 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 07.	김명진 		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 신청 결과 상세 VO")
public class OrderRegularResultDetlVo {

	@ApiModelProperty(value = "정기배송주문신청결과 상세PK")
	private long odRegularResultDetlId;

	@ApiModelProperty(value = "정기배송주문결과 PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "품목PK")
	private String ilItemCd;

	@ApiModelProperty(value = "상품PK")
	private long ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "신청상세상태 공통코드: REQ_DETAIL_STATUS_CD")
	private String reqDetailStatusCd;

	@ApiModelProperty(value = "정기배송 판매상태 공통코드: REGULAR_GOODS_SALE_STATUS")
	private String saleStatus;
}
