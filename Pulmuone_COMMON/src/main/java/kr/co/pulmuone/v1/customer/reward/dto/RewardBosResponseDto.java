package kr.co.pulmuone.v1.customer.reward.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardBosDetailVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardBosListVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardTargetGoodsBosVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitVo;
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
public class RewardBosResponseDto extends BaseResponseDto {

	 @ApiModelProperty(value = "목록 total")
	 private long total;

	 @ApiModelProperty(value = "고객보상제 리스트")
	 private List<RewardBosListVo> rows;

	 //등록 후 조회 (수정모드)
	 @ApiModelProperty(value = "고객보상제 관리 PK")
     private String csRewardId;

	 @ApiModelProperty(value = "고객보상제 상세 Vo (기본정보, 지급)")
     private RewardBosDetailVo rewardDetlInfo;

	 @ApiModelProperty(value = "고객보상제 상세 Vo (적용대상 상품)")
	 private List<RewardTargetGoodsBosVo> rewardTargetGoodsInfo;

}

