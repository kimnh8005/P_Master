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
@ApiModel(description = "ItemPriceRequestDto")
public class ItemPriceRequestDto extends BaseRequestPageDto {

  @ApiModelProperty(value = "품목PK", required = false)
  private String ilItemCd           ;
}
