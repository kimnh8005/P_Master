package kr.co.pulmuone.v1.policy.shoppingsetting.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "GetAuthMenuListRequestDto")
public class GetPolicyShopSettingFileUploadRequestDto extends BaseRequestDto{

	@JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String psKey;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String psValue;

}
