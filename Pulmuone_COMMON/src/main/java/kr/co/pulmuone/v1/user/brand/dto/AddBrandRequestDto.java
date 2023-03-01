package kr.co.pulmuone.v1.user.brand.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "AddBrandRequestDto")
public class AddBrandRequestDto  extends BaseRequestDto {

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜크 코드")
    private String urBrandId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜크 이름")
    private String brandName;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "공급업체 코드")
    private String urSupplierId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "사용여부")
    private String useYn;

}

