package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 회차별 상품정보 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 25.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 회차별 상품정보 Dto")
public class RegularReqRoundGoodsListResultDto {

	@ApiModelProperty(value = "정기배송신청PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "정기배송결과PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "정기배송결과상세PK")
	private long odRegularResultDetlId;

	@ApiModelProperty(value = "회차")
	private int reqRound;

	@ApiModelProperty(value = "주문생성여부")
	private String orderCreateYn;

	@ApiModelProperty(value = "도착예정일")
	private String arriveDt;

	@ApiModelProperty(value = "요일명")
	private String weekCdNm;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "품목PK")
	private String ilItemCd;

	@ApiModelProperty(value = "품목바코드")
	private String itemBarcode;

	@ApiModelProperty(value = "상품PK")
	private long ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "정기배송결과상세상태코드")
	private String reqDetailStatusCd;

	@ApiModelProperty(value = "정기배송결과상세상태코드명")
	private String reqDetailStatusCdNm;

	@ApiModelProperty(value = "주문상태코드")
	private String orderStatusCd;

	@ApiModelProperty(value = "주문상태코드명")
	private String statusNm;

	@ApiModelProperty(value = "상태변경가능여부")
	private String statusChgPsbYn;

	@ApiModelProperty(value = "다음 회차 여부")
	private boolean isNextRound;
}
