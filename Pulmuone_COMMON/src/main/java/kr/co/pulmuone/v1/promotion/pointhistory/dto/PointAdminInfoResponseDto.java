package kr.co.pulmuone.v1.promotion.pointhistory.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PointAdminInfoResponseDto")
public class PointAdminInfoResponseDto  extends BaseResponseDto{

	@ApiModelProperty(value = "분담조직 정보")
    private String roleName;

	@ApiModelProperty(value = "지급 가능 예산")
    private String amount;

	@ApiModelProperty(value = "조직코드")
    private String erpOrganizationCode;

    @ApiModelProperty(value = "분담조직명")
    private String erpOrganizationName;

	@ApiModelProperty(value = "조직코드")
    private String totalIssuePoint;

	@ApiModelProperty(value = "조직코드")
    private String totalUsePoint;

	@ApiModelProperty(value = "조직코드")
    private String totalExpirationPoint;

	@ApiModelProperty(value = "조직코드")
    private String totalMonthExpirationPoint;

    @ApiModelProperty(value = "지급금액")
    private String roleTotalPoint;

    @ApiModelProperty(value = "회계조직코드")
    private String erpRegalCode;

}
