package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "SystemUrlRequestDto")
public class SystemUrlRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "")
	String inputUrl;

	@ApiModelProperty(value = "")
	String inputUrlName;

	@ApiModelProperty(value = "")
	String inputUrlUsageYn;

	@ApiModelProperty(value = "")
	String inputAuthority;

	@ApiModelProperty(value = "")
	String inputContent;

	@ApiModelProperty(value = "")
	String id;

	@ApiModelProperty(value = "")
	String url;

	@ApiModelProperty(value = "")
	String stPrgramId;

	@ApiModelProperty(value = "")
	String memo;

}
