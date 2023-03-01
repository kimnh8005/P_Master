package kr.co.pulmuone.v1.system.auth.dto;

import java.util.ArrayList;
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
@ApiModel(description = "SaveAuthUserRequestDto")
public class SaveAuthUserRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "등록일자")
	private String insertData;

	@ApiModelProperty(value = "역활타입아이디")
	private Long stRoleTypeId;

	@ApiModelProperty(value = "역할 명")
	private String roleName;

	@ApiModelProperty(value = "등록데이터 리스트", hidden = true)
	private List<SaveAuthUserRequestSaveDto> insertSaveDataList = new ArrayList<>();

	@ApiModelProperty(value = "ITSM 계정")
	private String itsmId;

}
