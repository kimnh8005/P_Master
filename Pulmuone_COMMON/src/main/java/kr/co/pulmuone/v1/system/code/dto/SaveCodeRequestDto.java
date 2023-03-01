package kr.co.pulmuone.v1.system.code.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " SaveCodeRequestDto")
public class SaveCodeRequestDto extends BaseRequestDto {
	@ApiModelProperty(value = "등록 데이터", required = false)
    String insertData;

    @ApiModelProperty(value = "변경 데이터", required = false)
    String updateData;

    @ApiModelProperty(value = "삭제 데이터", required = false)
    String deleteData;

    @ApiModelProperty(value = "등록 코드 리스트", hidden = true)
    List<SaveCodeRequestSaveDto> insertRequestDtoList;

    @ApiModelProperty(value = "변경 코드 리스트", hidden = true)
    List<SaveCodeRequestSaveDto> updateRequestDtoList;

    @ApiModelProperty(value = "삭제 코드 리스트", hidden = true)
    List<SaveCodeRequestSaveDto> deleteRequestDtoList;
}
