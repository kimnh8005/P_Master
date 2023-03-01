package kr.co.pulmuone.v1.goods.price.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GoodsPriceReqeustDto")
public class GoodsPriceRequestDto extends BaseRequestPageDto {

  @ApiModelProperty(value = "상품PK", required = false)
  private String ilGoodsId          ;

  @ApiModelProperty(value = "품목PK", required = false)
  private String ilItemCd           ;

  @ApiModelProperty(value = "기준일자", required = false)
  private String baseDe             ;

  @ApiModelProperty(value = "할인유형", required = false)
  private String discountTp         ;

  @ApiModelProperty(value = "판매 가격정보 or 임직원 할인가격정보 구분", required = false)
  private String priceKind;

  @ApiModelProperty(value = "임직원 개별할인 변경이력 or 임직원 할인 가격정보 변경이력 구분", required = false)
  private String historyKind;
}
