package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = " AddMenuAssigUrlRequestSaveDto")
public class AddMenuAssigUrlRequestSaveDto  extends BaseRequestDto{

	@ApiModelProperty(value = "")
	private Long stProgramAuthUrlMappingId;

	@ApiModelProperty(value = "")
	private Long stMenuUrlId;
}
