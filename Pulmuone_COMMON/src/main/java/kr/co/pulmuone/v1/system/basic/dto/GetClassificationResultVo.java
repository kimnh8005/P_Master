package kr.co.pulmuone.v1.system.basic.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetClassificationResultVo {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "분류관리PK")
    long stClassificationId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "아이디")
    long id;

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
    long parentsClassificationId;

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
    long gbDictionaryMasterId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "분류 타입")
    String classificationTp;

}
