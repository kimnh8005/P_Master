package kr.co.pulmuone.v1.policy.clause.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetClauseHistoryResultVo")
public class GetClauseHistoryResultVo {

	@ApiModelProperty(value = "약관 적용 시작일")
	private String executeDate;


}
