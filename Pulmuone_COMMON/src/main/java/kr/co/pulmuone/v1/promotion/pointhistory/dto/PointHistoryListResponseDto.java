package kr.co.pulmuone.v1.promotion.pointhistory.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.vo.PointHistoryVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "적립금 내역 리스트 조회 ResponseDto")
public class PointHistoryListResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "적립금 내역 리스트")
	private	List<PointHistoryVo> rows;

	@ApiModelProperty(value = "적립금 내역 리스트 총 Count")
	private long total;

	@ApiModelProperty(value = "지급금액")
	private String totalIssuePoint;

	@ApiModelProperty(value = "사용금액")
	private String totalUsePoint;

	@ApiModelProperty(value = "소멸금액")
	private String totalExpirationPoint;

	@ApiModelProperty(value = "이번달 소멸 예정 금액")
	private String totalMonthExpirationPoint;

}
