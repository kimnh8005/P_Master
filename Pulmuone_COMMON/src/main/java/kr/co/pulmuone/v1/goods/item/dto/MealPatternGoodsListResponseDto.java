package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardTargetGoodsBosVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternGoodsListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "식단 패턴 연결상품 리스트 ResponseDto")
public class MealPatternGoodsListResponseDto extends BaseResponseDto {

	 @ApiModelProperty(value = "목록 total")
	 private long total;

	 @ApiModelProperty(value = "식단 패턴 연결상품 Vo")
	 private List<MealPatternGoodsListVo> rows;

}

