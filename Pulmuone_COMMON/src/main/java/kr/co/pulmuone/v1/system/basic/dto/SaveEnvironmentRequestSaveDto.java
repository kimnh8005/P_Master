package kr.co.pulmuone.v1.system.basic.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " SaveEnvironmentRequestSaveDto")
public class SaveEnvironmentRequestSaveDto extends BaseRequestDto {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "환경설정PK")
    long stEnvId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "환경설정키")
    String environmentKey;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "환경설정명")
    String environmentName;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "환경설정값")
    String environmentValue;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "설명")
    String comment;

}
