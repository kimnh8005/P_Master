package kr.co.pulmuone.v1.goods.price.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsPriceVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GoodsPriceResponseDto")
public class GoodsPriceResponseDto {// extends BaseResponseDto {

  @ApiModelProperty(value = "결과코드")
  private String resultCode;

  @ApiModelProperty(value = "결과메시지")
  private String resultMessage;

  @ApiModelProperty(value = "처리오류개수")
  private int procErrorCount = 0;

  @ApiModelProperty(value = "처리오류리스트")
  private List<GoodsVo> procErrorList = new ArrayList<GoodsVo>();

  @ApiModelProperty(value = "리스트전체건수")
  private int total;

  @ApiModelProperty(value = "상품가격리스트")
  private List<GoodsPriceVo> rows = new ArrayList<GoodsPriceVo>();

  @ApiModelProperty(value = "상품가격리스트그룹")
  private List<List<GoodsPriceVo>> groupRows = new ArrayList<List<GoodsPriceVo>>();

  @ApiModelProperty(value = "상품가격상세")
  private GoodsPriceVo detail = new GoodsPriceVo();

}
