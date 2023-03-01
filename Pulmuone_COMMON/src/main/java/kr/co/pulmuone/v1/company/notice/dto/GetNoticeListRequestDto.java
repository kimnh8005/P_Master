package kr.co.pulmuone.v1.company.notice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "GetNoticeListRequestDto")
public class GetNoticeListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "")
	private String conditionType;

	@ApiModelProperty(value = "")
	private String conditionValue;

	@ApiModelProperty(value = "")
	private String useYn;

	@ApiModelProperty(value = "")
	private String popupYn;

	@ApiModelProperty(value = "")
	private String companyBbsType;

	@ApiModelProperty(value = "")
	private String startCreateDate;

	@ApiModelProperty(value = "")
	private String endCreateDate;

  @ApiModelProperty(value = "대시보드호출여부")
  private boolean isDashboardCallYn;

  @ApiModelProperty(value = "대시보드조회개수")
  private int searchCnt;


}