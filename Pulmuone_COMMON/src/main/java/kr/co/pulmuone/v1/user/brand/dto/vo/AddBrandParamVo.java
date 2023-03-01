package kr.co.pulmuone.v1.user.brand.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(description = "AddBrandParamVo")
public class AddBrandParamVo extends BaseRequestDto {

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜크 코드")
    private String urBrandId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜크 이름")
    private String brandName;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "공급 업체 코드")
    private String urSupplierId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "입력자")
    private String createId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "수정자")
    private String modifyId;


}