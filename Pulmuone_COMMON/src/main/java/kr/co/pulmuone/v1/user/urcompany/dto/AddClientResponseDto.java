package kr.co.pulmuone.v1.user.urcompany.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "AddClientResponseDto")
public class AddClientResponseDto  extends BaseResponseDto{

    @ApiModelProperty(value = "Validation 코드")
    private String validationCode;
}
