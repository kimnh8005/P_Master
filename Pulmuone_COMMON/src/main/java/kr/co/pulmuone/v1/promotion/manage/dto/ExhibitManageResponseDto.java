package kr.co.pulmuone.v1.promotion.manage.dto;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitGiftGoodsVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitGiftVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitSelectVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ExhibitManageResponseDto")
public class ExhibitManageResponseDto {

  @ApiModelProperty(value = "결과코드")
  private String resultCode;

  @ApiModelProperty(value = "결과메시지")
  private String resultMessage;

  @ApiModelProperty(value = "리스트전체건수")
  private long total;

  @ApiModelProperty(value = "리스트")
  private List<?> rows;

  @ApiModelProperty(value = "상세(기본)")
  private ExhibitVo detail;

  @ApiModelProperty(value = "골라담기-기본정보")
  private ExhibitSelectVo selectDetail;

  @ApiModelProperty(value = "증정행사-기본정보")
  private ExhibitGiftVo giftDetail;

}
