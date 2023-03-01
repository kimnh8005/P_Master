package kr.co.pulmuone.v1.user.join.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetIsCheckRecommendLoginIdRequestDto")
public class GetIsCheckRecommendLoginIdRequestDto
{

	@ApiModelProperty(value = "추천인아이디", required = true)
	private String recommendLoginId;

}
