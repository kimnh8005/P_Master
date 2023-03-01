package kr.co.pulmuone.v1.promotion.serialnumber.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetClauseRequestDto")
public class PutSerialNumberCancelRequestSaveDto extends BaseRequestDto {

	@ApiModelProperty(value = "이용권 PK")
	private String pmSerialNumberId;
}