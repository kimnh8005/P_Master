package kr.co.pulmuone.v1.user.buyer.dto.vo;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "AddBuyerChangeHististoryParamVo")
public class AddBuyerChangeHististoryParamVo {

	@ApiModelProperty(value = "")
	private String adminId;

	@ApiModelProperty(value = "")
	private String urUserId;

	@ApiModelProperty(value = "")
	private List<Map> insertData;

	@ApiModelProperty(value = "")
	private String DATABASE_ENCRYPTION_KEY;

}
