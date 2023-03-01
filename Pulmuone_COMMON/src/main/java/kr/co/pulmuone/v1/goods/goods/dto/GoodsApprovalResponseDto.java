package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsApprovalResultVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "GoodsApprovalResponseDto")
@NoArgsConstructor
@AllArgsConstructor
public class GoodsApprovalResponseDto  extends BaseResponseDto {

	@ApiModelProperty(value = "상품 승인 목록 조회 리스트")
	private	List<GoodsApprovalResultVo> rows;

	@ApiModelProperty(value = "상품 승인 목록 조회 총 Count")
	private long total;

}
