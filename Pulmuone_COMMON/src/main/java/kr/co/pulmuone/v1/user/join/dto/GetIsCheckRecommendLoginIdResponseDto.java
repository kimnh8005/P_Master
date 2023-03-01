package kr.co.pulmuone.v1.user.join.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetIsCheckRecommendLoginIdResponseDto")
public class GetIsCheckRecommendLoginIdResponseDto
{

	@ApiModelProperty(value = "추천인유저아이디")
	private String recommUserId;

}
