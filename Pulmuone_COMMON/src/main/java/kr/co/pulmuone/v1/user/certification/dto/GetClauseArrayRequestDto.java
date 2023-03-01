package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = " GetClauseArrayRequestDto")
public class GetClauseArrayRequestDto {

	@ApiModelProperty(value = "약관코드", required = true)
	private String psClauseGrpCd;

	@ApiModelProperty(value = "약관 적용 시작일", required = true)
	private String executeDate;

	@Builder
	GetClauseArrayRequestDto(String psClauseGrpCd, String executeDate) {
		this.psClauseGrpCd = psClauseGrpCd;
		this.executeDate = executeDate;
	}

}
