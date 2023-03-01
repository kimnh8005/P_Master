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
@ApiModel(description = "ItemPriceOrigVo 품목가격원본 VO")
public class ItemPriceOrigVo {

    @ApiModelProperty(value = "품목가격원본 SEQ")
    private long ilItemPriceOriginalId;

	@ApiModelProperty(value = "품목 PK")
	private String ilItemCode;

	@ApiModelProperty(value = "적용 시작일")
	private String startDate;

	@ApiModelProperty(value = "원가")
	private String standardPrice;

	@ApiModelProperty(value = "정상가")
	private String recommendedPrice;

	@ApiModelProperty(value = "등록자")
	private long createId;

	@ApiModelProperty(value = "등록일")
	private String createDt;

	@ApiModelProperty(value = "수정자")
	private long modifyId;

	@ApiModelProperty(value = "수정일")
	private String modifyDt;

	@ApiModelProperty(value = "관리자에 의한 가격 관리 유형(A: 원가/정상가, R: 정상가)")
	private String priceManageTp;

	@ApiModelProperty(value = "시스템에 의한 업데이트 유무(Y: 시스템)")
	private String systemUpdateYn;

	@ApiModelProperty(value = "관리자에 의한 업데이트 유무(Y: 관리자)")
	private String managerUpdateYn;

}


