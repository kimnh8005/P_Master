package kr.co.pulmuone.v1.customer.stndpnt.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StandingPointRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "조건 분류")
    private String searchSelect;

	@ApiModelProperty(value = "조건검색 키워드")
	private String findKeyword;

	@ApiModelProperty(value = "등록일 시작")
	private String createDateStart;

	@ApiModelProperty(value = "등록일 종료")
	private String createDateEnd;

	@ApiModelProperty(value = "상품입점문의 ID")
	private String csStandPntId;

	@ApiModelProperty(value = "문의승인 상태")
	private String questionStat;

	@ApiModelProperty(value = "승인상태")
	private String apprStat;

	@ApiModelProperty(value = "조회 문의승인 상태")
	private String apprSearchStat;

	@ApiModelProperty(value = "문의승인 상태 리스트")
    private List<String> apprSearchStatList;

}
