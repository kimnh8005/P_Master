package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemApprovalResultVo;
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
@ApiModel(description = "ItemApprovalResponseDto")
@NoArgsConstructor
@AllArgsConstructor
public class ItemApprovalResponseDto  extends BaseResponseDto {

	@ApiModelProperty(value = "품목 승인 목록 조회 리스트")
	private	List<ItemApprovalResultVo> rows;

	@ApiModelProperty(value = "품목 승인 목록 조회 총 Count")
	private long total;

}
