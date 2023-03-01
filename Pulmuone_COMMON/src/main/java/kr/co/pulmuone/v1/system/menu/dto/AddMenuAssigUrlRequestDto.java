package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@ApiModel(description = "AddMenuAssigUrlRequestDto")
public class AddMenuAssigUrlRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "")
	private Long stProgramAuthId;

	@ApiModelProperty(value = "", required = false)
    String insertData;

	@ApiModelProperty(value = "", required = false)
    String deleteData;

	@ApiModelProperty(value = "", hidden = true)
	List<AddMenuAssigUrlRequestSaveDto> insertRequestDtoList = new ArrayList<AddMenuAssigUrlRequestSaveDto>();

	@ApiModelProperty(value = "", hidden = true)
	List<AddMenuAssigUrlRequestSaveDto> deleteRequestDtoList = new ArrayList<AddMenuAssigUrlRequestSaveDto>();

	public AddMenuAssigUrlRequestDto(Long stProgramAuthId) {
		this.stProgramAuthId = stProgramAuthId;
	}
}
