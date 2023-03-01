package kr.co.pulmuone.v1.goods.price.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.price.dto.vo.ItemPriceVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.ItemVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ItemPriceResponseDto")
public class ItemPriceResponseDto {// extends BaseResponseDto {

  @ApiModelProperty(value = "결과코드")
  private String resultCode;

  @ApiModelProperty(value = "결과메시지")
  private String resultMessage;

  @ApiModelProperty(value = "리스트전체건수")
  private int total;

  @ApiModelProperty(value = "품목가격리스트")
  private List<ItemPriceVo> rows = new ArrayList<ItemPriceVo>();

  @ApiModelProperty(value = "품목가격상세")
  private ItemPriceVo detail = new ItemPriceVo();

}
