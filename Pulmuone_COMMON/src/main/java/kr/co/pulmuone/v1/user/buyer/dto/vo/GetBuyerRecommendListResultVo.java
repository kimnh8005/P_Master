package kr.co.pulmuone.v1.user.buyer.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBuyerRecommendListResultVo")
public class GetBuyerRecommendListResultVo {

	@ApiModelProperty(value = "")
	@UserMaskingUserName
	private String recommendName;

	@ApiModelProperty(value = "")
	@UserMaskingLoginId
	private String recommendId;

	@ApiModelProperty(value = "")
	private String recommendCreateDate;

	public String getRecommendId() {
		if(!Objects.isNull(recommendName)){
			return recommendName + '(' + recommendId + ')';
		}
		return recommendId;
	}
}
