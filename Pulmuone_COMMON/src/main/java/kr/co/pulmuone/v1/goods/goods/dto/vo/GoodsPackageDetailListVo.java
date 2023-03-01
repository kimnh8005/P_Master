package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsPackageDetailListVo {

	@ApiModelProperty(value = "상품 ID")
    private Long goodsId;

	@ApiModelProperty(value = "묶음 상품 ID")
    private Long targetGoodsId;

	@ApiModelProperty(value = "묶음 상품유형 공통코드(GOODS_TYPE)")
	private String goodsPackageTp;

	@ApiModelProperty(value = "묶음 상품유형 공통코드(GOODS_TYPE)")
	private String goodsPackageTpNm;

	@ApiModelProperty(value = "품목 코드")
	private String itemCd;

	@ApiModelProperty(value = "품목 바코드")
	private String itemBarcode;

	@ApiModelProperty(value = "상품유형 타입")
	private String goodsTp;

	@ApiModelProperty(value = "상품유형 명")
	private String goodsTpNm;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "과세여부")
	private String taxYn;

	@ApiModelProperty(value = "구성수량")
	private String goodsQty;


}
