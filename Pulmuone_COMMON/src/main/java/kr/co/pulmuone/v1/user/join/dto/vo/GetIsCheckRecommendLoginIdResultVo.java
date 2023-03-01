package kr.co.pulmuone.v1.user.join.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetIsCheckRecommendLoginIdResultVo")
public class GetIsCheckRecommendLoginIdResultVo
{

	@ApiModelProperty(value = "추천인아이디")
	private String recommUserId;

}
