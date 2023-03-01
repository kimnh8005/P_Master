package kr.co.pulmuone.v1.statics.sale.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.statics.sale.dto.vo.SaleStaticsVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "SaleStaticsResponseDto")
public class SaleStaticsResponseDto {

  @ApiModelProperty(value = "결과코드")
  private String resultCode;

  @ApiModelProperty(value = "결과메시지")
  private String resultMessage;

  @ApiModelProperty(value = "리스트전체건수")
  private long total;

  @ApiModelProperty(value = "리스트")
  private List<?> rows;

}
