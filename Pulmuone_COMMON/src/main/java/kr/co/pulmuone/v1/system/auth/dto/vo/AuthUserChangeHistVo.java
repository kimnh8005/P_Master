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
@ApiModel(description = "사용자권한 변경이력 Vo")
public class AuthUserChangeHistVo{
	
	@ApiModelProperty(value = "대상자 ID")
	private Long targetUserId;
	
	@ApiModelProperty(value = "역할타입 ID")
	private Long roleTypeId;
	
	@ApiModelProperty(value = "처리내용(C: insert, U: update, D: delete)")
	private String action;
	
	@ApiModelProperty(value = "처리자 ID")
	private Long handleUserId;
	
	@ApiModelProperty(value = "등록자 ID")
	private Long createId;
}
