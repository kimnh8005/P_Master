package kr.co.pulmuone.v1.comm.framework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "AddFileUploadListResponseDto")
public class AddFileUploadListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "리스트")
	private	 List<FileVo> rows;

	@ApiModelProperty(value = "6")
	private int total;

}
