package kr.co.pulmuone.v1.user.brand.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "전시브랜드 검색 Request")
public class DisplayBrandListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "전시브랜드 검색유형")
	private String brandSearchType;

	@ApiModelProperty(value = "전시브랜드 검색어")
	private String brandSearchValue;

	@ApiModelProperty(value = "사용여부")
	private String searchUseYn;

	@ApiModelProperty(value = "브랜드관운영여부")
	private String searchBrandPavilionYn;


	@ApiModelProperty(value = "상위 디렉토리 위치")
	private String rootPath;

}
