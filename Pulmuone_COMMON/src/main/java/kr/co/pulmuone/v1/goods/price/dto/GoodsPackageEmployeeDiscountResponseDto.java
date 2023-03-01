package kr.co.pulmuone.v1.goods.price.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPriceInfoResultVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GoodsDiscountResponseDto")
public class GoodsPackageEmployeeDiscountResponseDto {// extends BaseResponseDto {

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

  @ApiModelProperty(value = "임직원 개별할인 정보 리스트")
  private List<GoodsPriceInfoResultVo> rows = new ArrayList<GoodsPriceInfoResultVo>();
}
