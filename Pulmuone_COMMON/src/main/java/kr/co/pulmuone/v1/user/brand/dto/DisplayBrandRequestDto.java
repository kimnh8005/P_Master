package kr.co.pulmuone.v1.user.brand.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "DisplayBrandRequestDto")
public class DisplayBrandRequestDto extends BaseRequestDto {

	@JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "전시 브랜드 코드")
    private String dpBrandId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "Base Root Url")
    private String baseRoot;

	@ApiModelProperty(value = "브랜드 검색유형")
	private String brandSearchType;

	@ApiModelProperty(value = "브랜드 검색어")
	private String brandSearchValue;

	@ApiModelProperty(value = "사용여부")
	private String searchUseYn;

	@ApiModelProperty(value = "상위 디렉토리 위치")
	private String rootPath;

    @ApiModelProperty(value = "이미지파일")
    String addFile;

	@ApiModelProperty(value = "이미지파일 리스트")
	List<FileVo> addFileList;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "브랜드관운영여부")
	private String brandPavilionYn;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "전시 브랜드 이름")
    private String dpBrandName;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "공급업체 코드")
    private String urSupplierId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드관 운영여부")
    private String searchBrandPavilionYn;


}
