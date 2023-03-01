package kr.co.pulmuone.v1.system.shopconfig.dto.basic;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.constants.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " SaveShopConfigRequestSaveDto")
public class SaveSystemShopConfigRequestSaveDto extends BaseRequestDto {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String stShopId;

    public String getStShopId(){
    	return Constants.ST_SHOP_ID;
    }

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
    String psConfigId;


}
