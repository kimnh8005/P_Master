package kr.co.pulmuone.v1.goods.itemprice.dto.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ItemPriceIfTemp API 임시테이블")
public class ItemPriceIfTempVo {

    @ApiModelProperty(value = "SEQ")
    private String ilItemPriceIfTempId;

	@ApiModelProperty(value = "중계서버 DB SEQ")
	private Integer ifSeq;

	@ApiModelProperty(value = "품목 PK")
	private String ilItemCd;

	@ApiModelProperty(value = "적용 시작일")
	private String startDt;

	@ApiModelProperty(value = "원가")
	private String standardPrice;

	@ApiModelProperty(value = "정상가")
	private String recommendedPrice;

	@ApiModelProperty(value = "행사구분")
	private String saleType;

	@ApiModelProperty(value = "가격 관리 유형")
	private String priceManageTp;

}


