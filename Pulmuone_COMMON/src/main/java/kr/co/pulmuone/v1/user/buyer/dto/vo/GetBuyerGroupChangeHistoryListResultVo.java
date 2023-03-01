package kr.co.pulmuone.v1.user.buyer.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBuyerGroupChangeHistoryListResultVo")
public class GetBuyerGroupChangeHistoryListResultVo {

	@ApiModelProperty(value = "")
	private String groupName;

	@ApiModelProperty(value = "")
	private String createDate;



}
