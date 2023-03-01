package kr.co.pulmuone.v1.system.basic.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetClassificationListResultVo {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String stClassificationId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String type;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String typeName;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String depth;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String parentsClassificationId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String parentsName;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String useYn;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String sort;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String gbDictionaryMasterId;

}
