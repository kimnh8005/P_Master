package kr.co.pulmuone.v1.system.auth.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(description = "사용자권한 매핑 Vo")
public class AuthUserMappingVo{
	
	@ApiModelProperty(value = "사용자 권한 매핑 ID")
	private Long authUserMappingId;
	
	@ApiModelProperty(value = "역할타입 ID")
	private Long roleTypeId;
	
	@ApiModelProperty(value = "회원 ID")
	private Long userId;
	
	@ApiModelProperty(value = "등록자 ID")
	private Long createId;
	
}
