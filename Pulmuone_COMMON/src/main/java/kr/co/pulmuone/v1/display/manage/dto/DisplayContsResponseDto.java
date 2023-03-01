package kr.co.pulmuone.v1.display.manage.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.display.manage.dto.vo.ContsVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "DisplayContsResponseDto")
public class DisplayContsResponseDto {// extends BaseResponseDto {

  @ApiModelProperty(value = "결과코드")
  private String resultCode;

  @ApiModelProperty(value = "결과메시지")
  private String resultMessage;

  @ApiModelProperty(value = "리스트전체건수")
  private int total;

  @ApiModelProperty(value = "컨텐츠리스트")
  private List<ContsVo> rows = new ArrayList<ContsVo>();

  @ApiModelProperty(value = "컨텐츠상세")
  private ContsVo detail = new ContsVo();

  @ApiModelProperty(value = "중복건수")
  private int dupTotal;

  @ApiModelProperty(value = "중복리스트")
  private List<ContsVo> dupRows = new ArrayList<ContsVo>();

  @ApiModelProperty(value = "실패건수")
  private int failTotal;

  @ApiModelProperty(value = "실패리스트")
  private List<ContsVo> failRows = new ArrayList<ContsVo>();


}
