package kr.co.pulmuone.v1.display.manage.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.display.manage.dto.vo.InventoryVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "DisplayInventoryResponseDto")
public class DisplayInventoryResponseDto {// extends BaseResponseDto {

  @ApiModelProperty(value = "결과코드")
  private String resultCode;

  @ApiModelProperty(value = "결과메시지")
  private String resultMessage;

  @ApiModelProperty(value = "리스트전체건수")
  private int total;

  @ApiModelProperty(value = "인벤토리리스트")
  private List<InventoryVo> rows = new ArrayList<InventoryVo>();

  @ApiModelProperty(value = "인벤토리상세")
  private InventoryVo detail = new InventoryVo();

  @ApiModelProperty(value = "인벤토리코드배열String")
  private String inventoryCdsString;

}
