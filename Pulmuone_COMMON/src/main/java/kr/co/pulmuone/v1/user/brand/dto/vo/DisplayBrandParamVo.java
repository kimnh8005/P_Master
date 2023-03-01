package kr.co.pulmuone.v1.user.brand.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
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
@ApiModel(description = "DisplayBrandParamVo")
public class DisplayBrandParamVo extends BaseRequestDto{

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "전시 브랜드 코드")
    private String dpBrandId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "상위 저장위치")
    private String rootPath;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "전시 브랜드 이름")
    private String dpBrandName;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "공급업체 코드")
    private String urSupplierId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드관운영여부")
    private String brandPavilionYn;

	@ApiModelProperty(value = "FILE Seq")
	private String seq;

	@ApiModelProperty(value = "이미지 타입")
	private String imageType;

}
