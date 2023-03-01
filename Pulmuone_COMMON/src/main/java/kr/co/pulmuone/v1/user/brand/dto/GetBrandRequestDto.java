package kr.co.pulmuone.v1.user.brand.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBrandRequestDto")
public class GetBrandRequestDto extends BaseRequestDto {

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜크 코드")
    private String urBrandId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "Base Root Url")
    private String baseRoot;

	@ApiModelProperty(value = "브랜드 검색유형")
	private String brandSearchType;

	@ApiModelProperty(value = "브랜드 검색어")
	private String brandSearchValue;

	@ApiModelProperty(value = "사용여부")
	private String searchUseYn;

	@ApiModelProperty(value = "공급업체 ID")
	private String searchUrSupplierId;


}
