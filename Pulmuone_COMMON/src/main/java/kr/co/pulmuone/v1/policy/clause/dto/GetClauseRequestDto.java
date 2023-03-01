package kr.co.pulmuone.v1.policy.clause.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetClauseRequestDto")
public class GetClauseRequestDto {

	@ApiModelProperty(value = "약관코드")
	String psClauseGrpCd;

	@ApiModelProperty(value = "약관 적용 시작일 ex)YYYY-MM-DD")
	String executeDate;

	@ApiModelProperty(value = "약관코드 리스트")
	List<String> psClauseGrpCdList;
}
