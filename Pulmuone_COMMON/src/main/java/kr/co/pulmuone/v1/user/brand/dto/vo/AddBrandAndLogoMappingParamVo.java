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
@ApiModel(description = "AddBrandAndLogoMappingParamVo")
public class AddBrandAndLogoMappingParamVo extends BaseRequestDto {

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "첨부파일 고유값")
    private String psAttcId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 코드")
    private String urBrandId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "전시 브랜드 코드")
    private String dpBrandId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 Logo 유형")
    private String imageType;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "파일 위치")
    private String subPath;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "물리적인 파일 이름")
    private String physicalName;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "파일 원본이름")
    private String originName;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "생성자")
    private String createId;

}