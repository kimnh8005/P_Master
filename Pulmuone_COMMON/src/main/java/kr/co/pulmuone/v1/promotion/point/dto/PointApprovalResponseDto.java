package kr.co.pulmuone.v1.promotion.point.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.point.dto.vo.PointApprovalResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PointApprovalResponseDto")
public class PointApprovalResponseDto  extends BaseResponseDto {

	@ApiModelProperty(value = "포인트승인 목록 조회 리스트")
	private	List<PointApprovalResultVo> rows;

	@ApiModelProperty(value = "포인트승인 목록 조회 총 Count")
	private long total;

}
