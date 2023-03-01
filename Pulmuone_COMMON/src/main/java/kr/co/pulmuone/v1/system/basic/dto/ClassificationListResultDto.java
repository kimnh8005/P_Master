package kr.co.pulmuone.v1.system.basic.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import kr.co.pulmuone.v1.comm.base.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = " ClassificationListResultDto")
public class ClassificationListResultDto extends BaseDto {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "분류관리PK")
    String stClassificationId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "아이디")
    String id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "타입")
    String type;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "타입명")
    String typeName;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "길이")
    String depth;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "상위 분류 아이디")
    String parentsClassificationId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "상위 분류명")
    String parentsName;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "사용여부")
    String useYn;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "순서")
    String sort;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "사전 마스터 아이디")
    String gbDictionaryMasterId;
}
