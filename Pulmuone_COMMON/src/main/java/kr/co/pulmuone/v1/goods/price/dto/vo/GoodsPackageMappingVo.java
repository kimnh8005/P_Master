package kr.co.pulmuone.v1.goods.price.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModelProperty;
//import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsPackageMappingVo {

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "묶음상품PK")
  private String ilGoodsPackageGoodsMappingId;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "원본상품PK")
	private String ilGoodsId;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "묶음구성상품PK")
  private String targetGoodsId;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상품유형공통코드PK")
  private String goodsTp;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "기준상품여부")
  private String baseGoodsYn;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "구성수량")
  private int goodsQty;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "구성상품판매가(개당)")
  private int salePricePerUnit;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "판매가")
  private int salePrice;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "등록자")
	private String createId;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "등록일")
	private String createDt;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "수정자")
	private String modifyId;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "수정일")
	private String modifyDt;

  // --------------------------------------------------------------------------
  // IL_GOODS
  // --------------------------------------------------------------------------
  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "품목PK")
  private String ilItemCd                     ;

}
