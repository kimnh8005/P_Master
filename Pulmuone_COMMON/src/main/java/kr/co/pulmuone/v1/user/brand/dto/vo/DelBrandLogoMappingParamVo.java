package kr.co.pulmuone.v1.user.brand.dto.vo;

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
@ApiModel(description = "DelBrandLogoMappingParamVo")
public class DelBrandLogoMappingParamVo extends BaseRequestDto {

	@ApiModelProperty(value = "FILE Seq")
	private String seq;

	@ApiModelProperty(value = "브랜드 ID")
	private String urBrandId;

	@ApiModelProperty(value = "이미지 타입")
	private String imageType;


}