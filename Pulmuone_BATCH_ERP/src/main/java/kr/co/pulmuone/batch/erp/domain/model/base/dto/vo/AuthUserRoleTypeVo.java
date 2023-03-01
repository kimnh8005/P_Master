package kr.co.pulmuone.batch.erp.domain.model.base.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "역할관리 Vo")
public class AuthUserRoleTypeVo {
	
	@ApiModelProperty(value = "사용자 권한 매핑 ID")
	private Long authUserMappingId;
	
	@ApiModelProperty(value = "역할타입 ID")
	private Long roleTypeId;
	
	@ApiModelProperty(value = "회원 ID")
	private Long userId;
	
	@ApiModelProperty(value = "역할타입 명")
	private String roleTypeName;
	
	@ApiModelProperty(value = "권한부여일자")
	private String createDate;
	
	@ApiModelProperty(value = "등록자 ID")
	private Long createId;
	
	@ApiModelProperty(value = "ERP 조직코드")
	private String erpOrganizationCode;
	
	@ApiModelProperty(value = "ERP 법인코드")
	private String erpRegalCode;
	
}