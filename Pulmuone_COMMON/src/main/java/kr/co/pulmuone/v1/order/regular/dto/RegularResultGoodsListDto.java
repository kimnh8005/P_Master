package kr.co.pulmuone.v1.order.regular.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 상품 리스트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 09.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 상품 리스트 Dto")
public class RegularResultGoodsListDto {

	@ApiModelProperty(value = "정기배송주문결과상세PK")
	private long odRegularResultDetlId;

	@ApiModelProperty(value = "정기배송주문결과PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "상품품목PK")
	private String ilItemCd;

	@ApiModelProperty(value = "상품PK")
	private long ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "상품판매상태")
	private String saleStatus;

	@ApiModelProperty(value = "상품판매상태명")
	private String saleStatusNm;

	@ApiModelProperty(value = "정기배송상품판매상태")
	private String regularSaleStatus;

	@ApiModelProperty(value = "정기배송상품판매상태명")
	private String regularSaleStatusNm;

	@ApiModelProperty(value = "상품이미지")
	private String thumbnailPath;

	@ApiModelProperty(value = "정기배송상품 기본 할인율")
	private int goodsDefaultRate;

	@ApiModelProperty(value = "상품결제금액")
	private int paidPrice;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "건너뛰기여부")
	private String regularSkipYn;

	@ApiModelProperty(value = "건너뛰기가능여부")
	private String regularSkipPsbYn;

	@ApiModelProperty(value = "신청상세상태공통코드")
	private String reqDetailStatusCd;

	@ApiModelProperty(value = "신청상세상태공통코드명")
	private String reqDetailStatusCdNm;

	@ApiModelProperty(value = "정기배송 취소 가능 여부")
	private String regularCancelPsbYn;

	@ApiModelProperty(value = "추가상품목록")
	private List<RegularResultGoodsListDto> addGoodsList;
}
