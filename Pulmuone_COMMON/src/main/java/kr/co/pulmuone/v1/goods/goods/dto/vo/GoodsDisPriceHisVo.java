package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsDisPriceHisVo {

	@ApiModelProperty(value = "공급처 ID")
    private String supplierId;

	@ApiModelProperty(value = "공급처 업체명")
    private String compNm;

	@ApiModelProperty(value = "가격 시작일")
    private String priceStartDt;

	@ApiModelProperty(value = "가격 종료일")
    private String priceEndDt;

	@ApiModelProperty(value = "상품 ID")
    private String goodsId;

	@ApiModelProperty(value = "상품명")
    private String goodsNm;

	@ApiModelProperty(value = "품목코드")
	private String itemCd;

	@ApiModelProperty(value = "과세여부")
    private String taxYn;

	@ApiModelProperty(value = "과세여부명")
    private String taxYnNm;

	@ApiModelProperty(value = "원가")
    private int standardPrice;

	@ApiModelProperty(value = "원가")
    private String standardPriceStr;

	@ApiModelProperty(value = "정상가")
    private int recommendedPrice;

	@ApiModelProperty(value = "정상가")
    private String recommendedPriceStr;

	@ApiModelProperty(value = "정상마진율")
    private int marginRate;

	@ApiModelProperty(value = "정상마진율")
    private String marginRateStr;

	@ApiModelProperty(value = "판매가")
    private int salePrice;

	@ApiModelProperty(value = "판매가")
    private String salePriceStr;

	@ApiModelProperty(value = "판매마진율")
    private int marginRate2;

	@ApiModelProperty(value = "판매마진율")
    private String marginRate2Str;

	@ApiModelProperty(value = "할인구분")
    private String discountTp;

	@ApiModelProperty(value = "할인구분명")
    private String discountTpNm;

	@ApiModelProperty(value = "적용일")
    private String createDt;

	@ApiModelProperty(value = "할인 시작일")
	private String discountStartDt;

	@ApiModelProperty(value = "할인 종료일")
	private String discountEndDt;

	@ApiModelProperty(value = "승인요정")
    private String aproveId;

	@ApiModelProperty(value = "승인완료")
    private String confirmId;

	@ApiModelProperty(value = "승인완료일")
	private String confirmDt;

}
