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
@ApiModel(description = "SaveAuthMenuRequestDto")
public class SaveRoleMenuAuthRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "역활 PK")
	private Long stRoleTypeId;

	@ApiModelProperty(value = "메뉴 PK")
	private Long stMenuId;

	@ApiModelProperty(value = "등록데이터")
	private String insertData;

	@ApiModelProperty(value = "등록객체 리스트", hidden = true)
	private List<SaveRoleMenuAuthRequestDtoSaveDto> insertRequestDtoList = new ArrayList<>();

	@ApiModelProperty(value = "삭제데이터")
	private String deleteData;

	@ApiModelProperty(value = "삭제객체 리스트", hidden = true)
	private List<SaveRoleMenuAuthRequestDtoSaveDto> deleteRequestDtoList = new ArrayList<>();

	@ApiModelProperty(value = "itsm ID")
	private String itsmId;

	@ApiModelProperty(value = "역할 명")
	private String roleName;

	@ApiModelProperty(value = "메뉴 명")
	private String menuName;

}
