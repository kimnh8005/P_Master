package kr.co.pulmuone.v1.system.basic.dto;

import java.util.ArrayList;
import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.system.basic.dto.SaveEnvironmentRequestSaveDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " SaveEnvironmentRequestDto")
public class SaveEnvironmentRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "등록데이터", required = false)
    String insertData;

    @ApiModelProperty(value = "변경데이터", required = false)
    String updateData;

    @ApiModelProperty(value = "삭제데이터", required = false)
    String deleteData;

    @ApiModelProperty(value = "", hidden = true)
    List<SaveEnvironmentRequestSaveDto> insertRequestDtoList = new ArrayList<SaveEnvironmentRequestSaveDto>();

    @ApiModelProperty(value = "", hidden = true)
    List<SaveEnvironmentRequestSaveDto> updateRequestDtoList = new ArrayList<SaveEnvironmentRequestSaveDto>();

    @ApiModelProperty(value = "", hidden = true)
    List<SaveEnvironmentRequestSaveDto> deleteRequestDtoList = new ArrayList<SaveEnvironmentRequestSaveDto>();

}
