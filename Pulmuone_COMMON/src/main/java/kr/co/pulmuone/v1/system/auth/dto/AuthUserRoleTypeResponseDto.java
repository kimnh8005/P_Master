package kr.co.pulmuone.v1.system.auth.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.system.auth.dto.vo.AuthUserRoleTypeVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "BOS 계정관리 - 역할관리 리스트")
public class AuthUserRoleTypeResponseDto{

	@ApiModelProperty(value = "역할관리 리스트")
	private List<AuthUserRoleTypeVo> rows;

}
