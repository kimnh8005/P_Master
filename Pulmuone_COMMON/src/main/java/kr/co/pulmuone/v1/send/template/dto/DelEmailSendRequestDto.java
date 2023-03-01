package kr.co.pulmuone.v1.send.template.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@ApiModel(description = " DelEmailSendRequestDto")
public class DelEmailSendRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "자동발생키값")
    String snAutoSendId;

}
