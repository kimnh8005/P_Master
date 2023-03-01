package kr.co.pulmuone.v1.customer.reward.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardBosDetailVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardBosListVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardTargetGoodsBosVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 고객 보상제 리스트 ResponseDto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "고객 보상제 리스트 ResponseDto")
public class RewardTargetGoodsResponseDto extends BaseResponseDto {

	 @ApiModelProperty(value = "목록 total")
	 private long total;

	 @ApiModelProperty(value = "고객보상제 상세 Vo (적용대상 상품)")
	 private List<RewardTargetGoodsBosVo> rows;

}

