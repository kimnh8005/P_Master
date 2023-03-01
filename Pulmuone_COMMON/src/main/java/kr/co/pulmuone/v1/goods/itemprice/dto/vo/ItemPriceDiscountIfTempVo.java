package kr.co.pulmuone.v1.goods.itemprice.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ItemPriceDiscountIfTemp API 임시테이블")
public class ItemPriceDiscountIfTempVo {

    @ApiModelProperty(value = "SEQ")
    private String ilItemPriceDiscountIfTempId;

	@ApiModelProperty(value = "중계서버 DB SEQ")
	private Integer ifSeq;

	@ApiModelProperty(value = "품목 PK")
	private String ilItemCd;

	@ApiModelProperty(value = "적용 시작일")
	private String startDt;

	@ApiModelProperty(value = "적용 종료일")
	private String endDt;

	@ApiModelProperty(value = "할인 원가")
	private String standardPrice;

	@ApiModelProperty(value = "할인 판매가")
	private String recommendedPrice;

	@ApiModelProperty(value = "정상 원가")
	private String normalStandardPrice;

	@ApiModelProperty(value = "정상 판매가")
	private String normalRecommendedPrice;

	@ApiModelProperty(value = "행사구분")
	private String saleType;

}
