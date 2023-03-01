package kr.co.pulmuone.v1.display.manage.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.display.manage.dto.vo.PageVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "DisplayPageResponseDto")
public class DisplayPageResponseDto {// extends BaseResponseDto {

  @ApiModelProperty(value = "결과코드")
  private String resultCode;

  @ApiModelProperty(value = "결과메시지")
  private String resultMessage;

  @ApiModelProperty(value = "리스트전체건수")
  private int total;

  @ApiModelProperty(value = "리스트")
  private List<PageVo> rows = new ArrayList<PageVo>();

  @ApiModelProperty(value = "상세")
  private PageVo detail = new PageVo();

}
