package kr.co.pulmuone.v1.system.shopconfig.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetSystemShopConfigListResultVo {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    long stShopId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String shopName;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String psGroupType;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String psGroupTypeDesc;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String psKey;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String psName;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String psValue;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String useYn;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String comment;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String useYnDescription;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    long psConfigId;

}
