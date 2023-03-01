package kr.co.pulmuone.v1.system.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = " SaveAuthMenuRequestDtoSaveDto")
public class SaveRoleMenuAuthRequestDtoSaveDto extends BaseRequestDto{

	@ApiModelProperty(value = "역활별 매뉴별 권한 PK")
	private String stRoleMenuAuthMappingId;

	@ApiModelProperty(value = "프로그램 권한 PK")
	private String stProgramAuthId;

	@ApiModelProperty(value = "권한 명")
	private String programAuthCodeName;

}
