package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "적립금 설정 목록 조회 RequestDto")
public class PointSettingListRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "적립금명")
	private String searchPointName;

	@ApiModelProperty(value = "설정구분")
	private String searchPointType;

	@ApiModelProperty(value = "설정상태")
	private String searchPointStatus;

	@ApiModelProperty(value = "등록일자 조회 시작 일자")
	private String startCreateDate;

	@ApiModelProperty(value = "등록일자 조회 종료 일자")
	private String endCreateDate;

	@ApiModelProperty(value = "이용권 수금 상태")
	private String searchIssueTicketType;

	@ApiModelProperty(value = "적립금 구분")
	private String searchPointDivision;
}
